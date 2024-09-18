package kr.co.are.androidble.ui.connection

import android.app.Application
import android.bluetooth.le.ScanResult
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.co.are.androidble.bluetooth.BluetoothModule
import kr.co.are.androidble.ui.connection.model.ConnectionUiState

class ConnectionViewModel (private val application: Application) :
    AndroidViewModel(application) {
    private val _homeUiState = MutableStateFlow<ConnectionUiState>(ConnectionUiState.Ready)
    val homeUiState = _homeUiState.asStateFlow()

    init {

    }

    fun initBluetooth(userServiceUUID: String = "4fafc201-1fb5-459e-8fcc-c5c9c331914b") {
        BluetoothModule.getInstance(application).setServiceUUID(userServiceUUID)
        BluetoothModule.getInstance(application).setListener(object : BluetoothModule.BluetoothModuleListener {
            override fun onScanStarted() {
                _homeUiState.value = ConnectionUiState.StartSearching
            }

            override fun onScanResult(scanResult: ScanResult) {

            }

            override fun onScanFailed(errorCode: Int) {

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
    fun startScan() {
        BluetoothModule.getInstance(application).startScan()
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