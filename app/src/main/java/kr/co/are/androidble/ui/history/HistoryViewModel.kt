package kr.co.are.androidble.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity
import kr.co.are.androidble.domain.model.ResultDomain
import kr.co.are.androidble.domain.usecase.GetGlucoseInfoListUseCase
import kr.co.are.androidble.ui.history.model.HistoryUiState
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getGlucoseInfoListUseCase: GetGlucoseInfoListUseCase
) : ViewModel() {
    private val _historyUiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val historyUiState: StateFlow<HistoryUiState> = _historyUiState.asStateFlow()

    init {
        fetchGlucoseData()
    }

    private fun fetchGlucoseData() {
        viewModelScope.launch {
            getGlucoseInfoListUseCase()
                .onStart {
                    _historyUiState.value = HistoryUiState.Loading
                }
                .catch { exception ->
                    _historyUiState.value = HistoryUiState.Error(exception.message ?: "Unknown Error")
                }
                .collect { resultData ->
                    when (resultData) {
                        is ResultDomain.Error -> {
                            _historyUiState.value = HistoryUiState.Error(resultData.exception.message ?: "Unknown Error")
                        }

                        ResultDomain.Loading -> {
                            _historyUiState.value = HistoryUiState.Loading
                        }

                        is ResultDomain.Success -> {
                            _historyUiState.value = HistoryUiState.Success(resultData.data)
                        }
                    }
                }
        }
    }

    // 데이터 새로 고침 함수
    fun refreshData() {
        fetchGlucoseData()
    }
}