package kr.co.are.androidble.ui.connection

import android.app.Application
import android.bluetooth.le.ScanResult
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.co.are.androidble.ui.module.BluetoothModule
import kr.co.are.androidble.ui.connection.model.ConnectionUiState
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(private val application: Application) :
    AndroidViewModel(application) {
    private val _homeUiState = MutableStateFlow<ConnectionUiState>(ConnectionUiState.Ready)
    val homeUiState = _homeUiState.asStateFlow()

    var isQRCodeScanned = mutableStateOf(false)
    var qrCode = mutableStateOf<String?>(null)
    var uuidInput = mutableStateOf("")

    init {

    }

    fun initBluetooth() {
        BluetoothModule.getInstance(application)
            .setListener(object : BluetoothModule.BluetoothModuleListener {
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
                }

            })
    }

    // BLE 스캔 시작
    fun startScan(serviceUuid: String) {
        if (serviceUuid.isNotEmpty()) {
            BluetoothModule.getInstance(application).setServiceUUID(serviceUuid)
            BluetoothModule.getInstance(application).startScan()
        }
    }

    // BLE 스캔 중지
    fun stopScan() {
        BluetoothModule.getInstance(application).stopScan()
    }

    // BLE 연결 해제
    fun disconnect() {
        BluetoothModule.getInstance(application).disconnect()
    }


}