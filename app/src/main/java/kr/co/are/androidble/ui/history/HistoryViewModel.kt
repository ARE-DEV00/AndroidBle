package kr.co.are.androidble.ui.history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.co.are.androidble.ui.history.model.HistoryUiState
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(

) : ViewModel() {
    private val _historyUiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val historyUiState: StateFlow<HistoryUiState> = _historyUiState.asStateFlow()


}