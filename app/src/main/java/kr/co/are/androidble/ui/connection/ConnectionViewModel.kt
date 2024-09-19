package kr.co.are.androidble.ui.connection

import android.bluetooth.le.ScanResult
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.are.androidble.domain.usecase.AddGlucoseInfoUseCase
import kr.co.are.androidble.ui.connection.model.ConnectionUiState
import kr.co.are.androidble.ui.module.bluetooth.BluetoothUtil
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
) : ViewModel() {
    var isQRCodeScanned = mutableStateOf(false)
    var uuidInput = mutableStateOf("123abc12-1234-abcd-5678-1234abcd5678")

}