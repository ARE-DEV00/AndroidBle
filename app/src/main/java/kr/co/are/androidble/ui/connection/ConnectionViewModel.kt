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
    private val bluetoothModule: BluetoothUtil,
    private val addGlucoseUseCase: AddGlucoseInfoUseCase
) : ViewModel() {
    private val _homeUiState = MutableStateFlow<ConnectionUiState>(ConnectionUiState.Ready)
    val homeUiState = _homeUiState.asStateFlow()

    var isQRCodeScanned = mutableStateOf(false)
    var uuidInput = mutableStateOf("123abc12-1234-abcd-5678-1234abcd5678")

    fun initBluetooth() {
        bluetoothModule
            .setListener(object : BluetoothUtil.BluetoothModuleListener {
                override fun onScanStarted() {
                    _homeUiState.value = ConnectionUiState.StartSearching
                }

                override fun onScanResult(scanResult: ScanResult) {

                }

                override fun onScanFailed(errorCode: Int) {
                    _homeUiState.value = ConnectionUiState.ErrorSearching(errorCode)
                }

                override fun onScanStopped() {
                    _homeUiState.value = ConnectionUiState.StopSearching
                }

                override fun onConnectionGattServer() {
                    _homeUiState.value = ConnectionUiState.ConnectionBluetooth
                }

                override fun onDisconnectionGattServer() {
                    _homeUiState.value = ConnectionUiState.DisconnectionBluetooth
                }

                override fun onCharacteristicChanged(data: String) {
                    viewModelScope.launch {
                        addGlucoseUseCase(data)
                            .collectLatest {
                                Timber.d("#### addGlucoseUseCase: $it")
                            }
                    }

                }

            })
    }

    // BLE 스캔 시작
    fun startScan(serviceUuid: String) {
        if (serviceUuid.isNotEmpty()) {
            bluetoothModule.setServiceUUID(serviceUuid)
            bluetoothModule.startScan()
        }
    }

    // BLE 스캔 중지
    fun stopScan() {
        bluetoothModule.stopScan()
    }

    // BLE 연결 해제
    fun disconnect() {
        bluetoothModule.disconnect()
    }


}