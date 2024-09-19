package kr.co.are.androidble.ui.bluetooth.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity

@Stable
sealed interface BluetoothDataDbUiState {

    @Immutable
    data class Success(val glucoseDataList: MutableList<GlucoseInfoEntity>) : BluetoothDataDbUiState

    @Immutable
    data object Loading : BluetoothDataDbUiState

    @Immutable
    data class Error(val message: String) : BluetoothDataDbUiState
}
