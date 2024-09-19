package kr.co.are.androidble.ui.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity
import kr.co.are.androidble.domain.model.ResultDomain
import kr.co.are.androidble.domain.usecase.AddGlucoseInfoUseCase
import kr.co.are.androidble.domain.usecase.DeleteAllGlucoseInfoUseCase
import kr.co.are.androidble.domain.usecase.GetGlucoseInfoListUseCase
import kr.co.are.androidble.ui.bluetooth.model.BluetoothDataDbUiState
import kr.co.are.androidble.ui.bluetooth.model.BluetoothDataUiState
import kr.co.are.androidble.ui.bluetooth.model.BluetoothUiState
import kr.co.are.androidble.ui.module.bluetooth.BluetoothUtil
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothModule: BluetoothUtil,
    private val addGlucoseUseCase: AddGlucoseInfoUseCase,
    private val getGlucoseInfoListUseCase: GetGlucoseInfoListUseCase,
    private val deleteAllGlucoseInfoUseCase: DeleteAllGlucoseInfoUseCase,
) : ViewModel() {
    private val _bluetoothDataDbUiState = MutableStateFlow<BluetoothDataDbUiState>(BluetoothDataDbUiState.Loading)
    val bluetoothDataDbUiState: StateFlow<BluetoothDataDbUiState> = _bluetoothDataDbUiState.asStateFlow()

    private val _bluetoothUiState = MutableStateFlow<BluetoothUiState>(BluetoothUiState.Ready)
    val bluetoothUiState: StateFlow<BluetoothUiState> = _bluetoothUiState.asStateFlow()

    private val _bluetoothDataUiState = MutableStateFlow<BluetoothDataUiState>(BluetoothDataUiState.Loading)
    val bluetoothDataUiState: StateFlow<BluetoothDataUiState> = _bluetoothDataUiState.asStateFlow()


    fun initBluetooth() {
        bluetoothModule
            .setListener(object : BluetoothUtil.BluetoothModuleListener {
                override fun onScanStarted() {
                    _bluetoothUiState.value = BluetoothUiState.StartSearching
                }

                @SuppressLint("MissingPermission")
                override fun onScanResult(scanResult: ScanResult) {
                    _bluetoothUiState.value = BluetoothUiState.BluetoothData(scanResult.device.name ?: "Unknown")

                }

                override fun onScanFailed(errorCode: Int) {
                    _bluetoothUiState.value = BluetoothUiState.ErrorSearching(errorCode)
                }

                override fun onScanStopped() {
                    _bluetoothUiState.value = BluetoothUiState.StopSearching
                }

                override fun onConnectionGattServer() {
                    _bluetoothUiState.value = BluetoothUiState.ConnectionBluetooth
                }

                override fun onDisconnectionGattServer() {
                    _bluetoothUiState.value = BluetoothUiState.DisconnectionBluetooth
                }

                override fun onCharacteristicChanged(data: String) {
                    viewModelScope.launch {
                        addGlucoseUseCase(data)
                            .collectLatest {
                                Timber.d("#### addGlucoseUseCase: $it")
                            }
                        runCatching {
                            val moshi = Moshi.Builder()
                                .add(KotlinJsonAdapterFactory())
                                .build()

                            // Moshi 어댑터 생성
                            val adapter = moshi.adapter(GlucoseInfoEntity::class.java)

                            // JSON 문자열을 GlucoseInfoEntity로 변환
                            adapter.fromJson(data)
                        }.onSuccess { glucoseInfoEntity ->
                            glucoseInfoEntity?.let {
                                _bluetoothDataUiState.value = BluetoothDataUiState.Success(glucoseInfoEntity)
                            }
                        }.onFailure {
                            _bluetoothDataUiState.value = BluetoothDataUiState.Error(it.message ?: "Unknown Error")
                            Timber.e(it)
                        }
                    }

                }

            })
    }

    private fun loadGlucoseData() {
        viewModelScope.launch {
            getGlucoseInfoListUseCase()
                .onStart {
                    _bluetoothDataDbUiState.value = BluetoothDataDbUiState.Loading
                }
                .catch { exception ->
                    _bluetoothDataDbUiState.value = BluetoothDataDbUiState.Error(exception.message ?: "Unknown Error")
                }
                .collect { resultData ->
                    when (resultData) {
                        is ResultDomain.Error -> {
                            _bluetoothDataDbUiState.value = BluetoothDataDbUiState.Error(resultData.exception.message ?: "Unknown Error")
                        }

                        ResultDomain.Loading -> {
                            _bluetoothDataDbUiState.value = BluetoothDataDbUiState.Loading
                        }

                        is ResultDomain.Success -> {
                            _bluetoothDataDbUiState.value = BluetoothDataDbUiState.Success(resultData.data)
                        }
                    }
                }
        }
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

    // 데이터 새로 고침 함수
    fun refreshData() {
        loadGlucoseData()
    }

    fun deleteAllData() {
        viewModelScope.launch {
            deleteAllGlucoseInfoUseCase()
                .collect{
                    refreshData()
                }
        }
    }


}