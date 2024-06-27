package com.yourcompany.android.tictactoe.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.nearby.connection.*
import com.yourcompany.android.tictactoe.BuildConfig
import com.yourcompany.android.tictactoe.domain.model.Message
import com.yourcompany.android.tictactoe.domain.model.MessageState
import com.yourcompany.android.tictactoe.routing.Screen
import com.yourcompany.android.tictactoe.routing.MessagingRouter
import java.util.*
import kotlin.text.Charsets.UTF_8

class MessagingViewModel(private val connectionsClient: ConnectionsClient) : ViewModel() {
  private var localUsername = "user"
  private var opponentEndpointId: String = ""

  fun setLocalUsername(username: String) {
    localUsername = username
  }
  private fun parseMessage(messageString: String): Pair<String, String> {
    // Example format: "Message(sender=Yann, content=Yo)"
    val startIndex = messageString.indexOf("sender=")
    val endIndex = messageString.indexOf(", content=")

    if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
      // Handle invalid message format, or fallback logic if needed
      return Pair("", messageString) // Assume no sender, treat whole message as content
    }

    val senderUsername = messageString.substring(startIndex + "sender=".length, endIndex).trim()

    val contentStartIndex = endIndex + ", content=".length
    val content = messageString.substring(contentStartIndex, messageString.length - 1).trim()

    return Pair(senderUsername, content)
  }

  private val _state = MutableLiveData(MessageState(localUser = localUsername))
  val state: LiveData<MessageState> = _state

  private val payloadCallback: PayloadCallback = object : PayloadCallback() {
    override fun onPayloadReceived(endpointId: String, payload: Payload) {
      Log.d(TAG, "onPayloadReceived")

      if (payload.type == Payload.Type.BYTES) {
        val messageBytes = payload.asBytes() ?: return
        val messageString = messageBytes.toString(UTF_8)
        Log.d(TAG, "Received message: $messageBytes from $endpointId")

        // Parse the messageString to retrieve senderUsername and content
        val (sender, content) = parseMessage(messageString)

        addMessage(Message(sender = sender, content = content))
      }
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
      Log.d(TAG, "onPayloadTransferUpdate")
    }
  }

  private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
    override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
      Log.d(TAG, "onEndpointFound")

      Log.d(TAG, "Requesting connection...")
      connectionsClient.requestConnection(
        localUsername,
        endpointId,
        connectionLifecycleCallback
      ).addOnSuccessListener {
        Log.d(TAG, "Successfully requested a connection")
      }.addOnFailureListener {
        Log.d(TAG, "Failed to request the connection")
      }
    }

    override fun onEndpointLost(endpointId: String) {
      Log.d(TAG, "onEndpointLost")
    }
  }

  private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
      Log.d(TAG, "onConnectionInitiated")

      Log.d(TAG, "Accepting connection...")
      connectionsClient.acceptConnection(endpointId, payloadCallback)
    }

    override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
      Log.d(TAG, "onConnectionResult")

      when (resolution.status.statusCode) {
        ConnectionsStatusCodes.STATUS_OK -> {
          Log.d(TAG, "ConnectionsStatusCodes.STATUS_OK")

          connectionsClient.stopAdvertising()
          connectionsClient.stopDiscovery()
          opponentEndpointId = endpointId
          Log.d(TAG, "opponentEndpointId: $opponentEndpointId")
          MessagingRouter.navigateTo(Screen.Chat)
        }
        ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
          Log.d(TAG, "ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED")
        }
        ConnectionsStatusCodes.STATUS_ERROR -> {
          Log.d(TAG, "ConnectionsStatusCodes.STATUS_ERROR")
        }
        else -> {
          Log.d(TAG, "Unknown status code ${resolution.status.statusCode}")
        }
      }
    }

    override fun onDisconnected(endpointId: String) {
      Log.d(TAG, "onDisconnected")
      goToHome()
    }
  }

  fun startHosting() {
    Log.d(TAG, "Start advertising...")
    MessagingRouter.navigateTo(Screen.Hosting)
    val advertisingOptions = AdvertisingOptions.Builder().setStrategy(STRATEGY).build()

    connectionsClient.startAdvertising(
      localUsername,
      BuildConfig.APPLICATION_ID,
      connectionLifecycleCallback,
      advertisingOptions
    ).addOnSuccessListener {
      Log.d(TAG, "Advertising...")
    }.addOnFailureListener {
      Log.d(TAG, "Unable to start advertising")
      MessagingRouter.navigateTo(Screen.Home)
    }
  }

  fun startDiscovering() {
    Log.d(TAG, "Start discovering...")
    MessagingRouter.navigateTo(Screen.Discovering)
    val discoveryOptions = DiscoveryOptions.Builder().setStrategy(STRATEGY).build()

    connectionsClient.startDiscovery(
      BuildConfig.APPLICATION_ID,
      endpointDiscoveryCallback,
      discoveryOptions
    ).addOnSuccessListener {
      Log.d(TAG, "Discovering...")
    }.addOnFailureListener {
      Log.d(TAG, "Unable to start discovering")
      MessagingRouter.navigateTo(Screen.Home)
    }
  }

  fun sendMessage(content: String) {
    Log.d(TAG, "Sending message: $content to $opponentEndpointId")
    val message = Message(sender = localUsername, content = content)
    val payload = Payload.fromBytes(message.toString().toByteArray(UTF_8))
    connectionsClient.sendPayload(opponentEndpointId, payload)
    Log.d(TAG, localUsername)
    addMessage(Message(sender = localUsername, content = content))
  }

  private fun addMessage(message: Message) {
    val currentState = _state.value ?: return
    _state.value = currentState.copy(messages = currentState.messages + message)
    Log.d(TAG, "opponent: $opponentEndpointId")
  }

  override fun onCleared() {
    stopClient()
    super.onCleared()
  }

  fun goToHome() {
    stopClient()
    MessagingRouter.navigateTo(Screen.Home)
  }

  private fun stopClient() {
    Log.d(TAG, "Stop advertising, discovering, all endpoints")
    connectionsClient.stopAdvertising()
    connectionsClient.stopDiscovery()
    connectionsClient.stopAllEndpoints()
    opponentEndpointId = ""
  }

  companion object {
    const val TAG = "MessagingVM"
    val STRATEGY = Strategy.P2P_POINT_TO_POINT
  }
}
