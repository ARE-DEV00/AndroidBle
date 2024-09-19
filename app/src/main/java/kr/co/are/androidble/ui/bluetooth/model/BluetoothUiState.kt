package kr.co.are.androidble.ui.bluetooth.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface BluetoothUiState {

    @Immutable
    data object Ready : BluetoothUiState


    @Immutable
    data object StartSearching : BluetoothUiState

    @Immutable
    data object StopSearching : BluetoothUiState

    @Immutable
    data class ErrorSearching(val code:Int) : BluetoothUiState

    @Immutable
    data object ConnectionBluetooth : BluetoothUiState

    @Immutable
    data object DisconnectionBluetooth : BluetoothUiState

    @Immutable
    data class BluetoothData(val data: String) : BluetoothUiState

    @Immutable
    data class Error(val isNetwork: Boolean) : BluetoothUiState
}
