package kr.co.are.androidble.ui.connection.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface ConnectionUiState {

    @Immutable
    data object Ready : ConnectionUiState


    @Immutable
    data object StartSearching : ConnectionUiState

    @Immutable
    data object StopSearching : ConnectionUiState

    @Immutable
    data object ConnectionBluetooth : ConnectionUiState

    @Immutable
    data object DisconnectionBluetooth : ConnectionUiState

    @Immutable
    data class BluetoothData(val data: String) : ConnectionUiState

    @Immutable
    data class Error(val isNetwork: Boolean) : ConnectionUiState
}
