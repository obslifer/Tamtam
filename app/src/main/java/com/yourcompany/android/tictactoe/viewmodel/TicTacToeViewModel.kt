package com.yourcompany.android.tictactoe.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import com.yourcompany.android.tictactoe.BuildConfig
import com.yourcompany.android.tictactoe.domain.model.GameState
import com.yourcompany.android.tictactoe.domain.model.TicTacToe
import com.yourcompany.android.tictactoe.routing.Screen
import com.yourcompany.android.tictactoe.routing.TicTacToeRouter
import kotlin.text.Charsets.UTF_8

class TicTacToeViewModel(private val connectionsClient: ConnectionsClient) : ViewModel() {
  private var game = TicTacToe()

  private val _state = MutableLiveData(
    GameState(game.playerTurn, game.playerWon, game.isOver, game.board)
  )
  val state: LiveData<GameState> = _state

  private val STRATEGY = Strategy.P2P_STAR

  private fun getLocalUserName() = "User" + System.currentTimeMillis().toString()
  private var localPlayer: Int = 0
  private var opponentPlayer: Int = 0
  private var opponentEndpointId: String = ""

  private val payloadCallback: PayloadCallback = object : PayloadCallback() {
    override fun onPayloadReceived(endpointId: String, payload: Payload) {
      Log.d(TAG, "onPayloadReceived")
      // This always gets the full data of the payload. Is null if it's not a BYTES payload.
      if (payload.getType() == Payload.Type.BYTES) {
        val positionStr = String(payload.asBytes()!!, UTF_8)
        val positionArray = positionStr.split(",")
        val position = positionArray[0].toInt() to positionArray[1].toInt()
        Log.d(TAG, "Received [${position.first},${position.second}] from $endpointId")
        play(opponentPlayer, position)
      }
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
      // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
      // after the call to onPayloadReceived().
      Log.d(TAG, "onPayloadTransferUpdate")
    }
  }

  private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
    override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
      Log.d(TAG, "onEndpointFound")

      Log.d(TAG, "Requesting connection...")
      connectionsClient.requestConnection(
        getLocalUserName(),
        endpointId,
        connectionLifecycleCallback
      ).addOnSuccessListener {
        // Successfully requested a connection. Now both sides
        // must accept before the connection is established.
        Log.d(TAG, "Successfully requested a connection")
      }.addOnFailureListener { e: Exception ->
        // Failed to request the connection.
        Log.d(TAG, "Failed to request the connection")
      }
    }

    override fun onEndpointLost(endpointId: String) {
      // A previously discovered endpoint has gone away.
      Log.d(TAG, "onEndpointLost")
    }
  }

  private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
      Log.d(TAG, "onConnectionInitiated")

      // Instead of accepting connection, you can show an AlertDialog on both devices so both have to accept
      // See: https://developers.google.com/nearby/connections/android/manage-connections#authenticate_a_connection
      Log.d(TAG, "Accepting connection...")
      connectionsClient.acceptConnection(endpointId, payloadCallback)
    }

    override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
      Log.d(TAG, "onConnectionResult")

      when (resolution.status.statusCode) {
        ConnectionsStatusCodes.STATUS_OK -> {
          // Connected! Can now start sending and receiving data.
          Log.d(TAG, "ConnectionsStatusCodes.STATUS_OK")

          connectionsClient.stopAdvertising()
          connectionsClient.stopDiscovery()
          opponentEndpointId = endpointId
          newGame()
          TicTacToeRouter.navigateTo(Screen.Game)
        }
        ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
          // The connection was rejected by one or both sides.
          Log.d(TAG, "ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED")
        }
        ConnectionsStatusCodes.STATUS_ERROR -> {
          // The connection broke before it was able to be accepted.
          Log.d(TAG, "ConnectionsStatusCodes.STATUS_ERROR")
        }
        else -> {
          // Unknown status code
          Log.d(TAG, "Unknown status code ${resolution.status.statusCode}")
        }
      }
    }

    override fun onDisconnected(endpointId: String) {
      // Disconnected from this endpoint. No more data can be
      // sent or received.
      Log.d(TAG, "onDisconnected")
    }
  }

  fun startHosting() {
    Log.d(TAG, "Start advertising...")
    val advertisingOptions = AdvertisingOptions.Builder().setStrategy(STRATEGY).build()

    connectionsClient.startAdvertising(
      getLocalUserName(),
      BuildConfig.APPLICATION_ID,
      connectionLifecycleCallback,
      advertisingOptions
    ).addOnSuccessListener {
      // Advertising...
      Log.d(TAG, "Advertising...")
      TicTacToeRouter.navigateTo(Screen.Hosting)
      localPlayer = 1
      opponentPlayer = 2
    }.addOnFailureListener {
      // Unable to start advertising
      Log.d(TAG, "Unable to start advertising")
    }
  }

  fun stopHosting() {
    Log.d(TAG, "Stop advertising")
    connectionsClient.stopAdvertising()
    TicTacToeRouter.navigateTo(Screen.HostOrDiscover)
    localPlayer = 0
    opponentPlayer = 0
    opponentEndpointId = ""
  }

  fun startDiscovering() {
    Log.d(TAG, "Start discovering...")
    val discoveryOptions = DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
    connectionsClient
      .startDiscovery(
        BuildConfig.APPLICATION_ID,
        endpointDiscoveryCallback,
        discoveryOptions
      ).addOnSuccessListener {
        // Discovering...
        Log.d(TAG, "Discovering...")
        TicTacToeRouter.navigateTo(Screen.Discovering)
        localPlayer = 2
        opponentPlayer = 1
      }.addOnFailureListener {
        // Unable to start discovering
        Log.d(TAG, "Unable to start discovering")
      }
  }

  fun stopDiscovering() {
    Log.d(TAG, "Stop discovering")
    connectionsClient.stopDiscovery()
    TicTacToeRouter.navigateTo(Screen.HostOrDiscover)
    localPlayer = 0
    opponentPlayer = 0
    opponentEndpointId = ""
  }

  fun newGame() {
    game = TicTacToe()
    _state.value = GameState(game.playerTurn, game.playerWon, game.isOver, game.board)
  }

  fun play(position: Pair<Int, Int>) {
    if (game.playerTurn != localPlayer) return
    if (!game.isNonPlayedBucket(position)) return

    play(localPlayer, position)

    Log.d(TAG, "Sending [${position.first},${position.second}] to $opponentEndpointId")
    connectionsClient.sendPayload(
      opponentEndpointId,
      Payload.fromBytes("${position.first},${position.second}".toByteArray())
    )
  }

  private fun play(player: Int, position: Pair<Int, Int>) {
    Log.d(TAG, "Player $player played [${position.first},${position.second}]")

    game.play(player, position)
    _state.value = GameState(game.playerTurn, game.playerWon, game.isOver, game.board)
  }

  override fun onCleared() {
    Log.d(TAG, "Stop advertising, discovering, all endpoints")
    connectionsClient.stopAdvertising()
    connectionsClient.stopDiscovery()
    connectionsClient.stopAllEndpoints()
    super.onCleared()
  }

  private companion object {
    const val TAG = "ViewModel"
  }
}