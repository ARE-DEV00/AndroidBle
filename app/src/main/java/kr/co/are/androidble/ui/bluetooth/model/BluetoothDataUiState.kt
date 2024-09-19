package kr.co.are.androidble.ui.bluetooth.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity

@Stable
sealed interface BluetoothDataUiState {

    @Immutable
    data class Success(val data: GlucoseInfoEntity) : BluetoothDataUiState

    @Immutable
    data object Loading : BluetoothDataUiState

    @Immutable
    data class Error(val message: String) : BluetoothDataUiState
}
