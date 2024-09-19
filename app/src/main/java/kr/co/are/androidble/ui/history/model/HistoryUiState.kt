package kr.co.are.androidble.ui.history.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity

@Stable
sealed interface HistoryUiState {

    @Immutable
    data class Success(val glucoseDataList: List<GlucoseInfoEntity>) : HistoryUiState

    @Immutable
    object Loading : HistoryUiState

    @Immutable
    data class Error(val message: String) : HistoryUiState
}
