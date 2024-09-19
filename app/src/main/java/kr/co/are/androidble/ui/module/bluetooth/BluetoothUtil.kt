package kr.co.are.androidble.ui.module.bluetooth

import android.annotation.SuppressLint
import android.app.Activity.BLUETOOTH_SERVICE
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothUtil @Inject constructor(
    private val application: Application
) {

    private var bluetoothAdapter: BluetoothAdapter
    private var bleScanner: BluetoothLeScanner
    private var bluetoothGatt: BluetoothGatt? = null

    private val CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8")
    private var userServiceUUID: String? = null
    private var listener: BluetoothModuleListener? = null

    init {
        val bluetoothManager = application.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bleScanner = bluetoothAdapter.bluetoothLeScanner
    }

    // ScanCallback 객체
    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val scanRecord = result.scanRecord
            val serviceUuids = scanRecord?.serviceUuids
            listener?.onScanResult(result)
            Timber.e("Device found: ${device.name} / $scanRecord / $serviceUuids")
            device.uuids?.forEach {
                Timber.e("UUID: $it")
            }

            if (userServiceUUID?.isNotEmpty() == true) {
                runCatching {
                    if (serviceUuids?.contains(ParcelUuid(UUID.fromString(userServiceUUID))) == true) {
                        stopScan()
                        connectToDevice(device)
                    }
                }.onFailure {
                    Timber.e(it)
                    stopScan()
                    listener?.onScanFailed(99999)
                }.onSuccess {
                    Timber.e("Success")
                }

            }
        }

        override fun onScanFailed(errorCode: Int) {
            listener?.onScanFailed(errorCode)
            Timber.e("Scan failed: $errorCode")
        }
    }

    // BLE 스캔 시작
    @SuppressLint("MissingPermission")
    fun startScan() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bleScanner.startScan(scanCallback)
        listener?.onScanStarted()
    }

    // BLE 스캔 중지
    @SuppressLint("MissingPermission")
    fun stopScan() {
        bleScanner.stopScan(scanCallback)
        listener?.onScanStopped()
    }

    // BLE 장치에 연결
    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        bluetoothGatt = device.connectGatt(application, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Timber.e("Connected to GATT server")
                    gatt.requestMtu(512)
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Timber.e("Disconnected from GATT server")
                    listener?.onDisconnectionGattServer()
                }
            }

            override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Timber.e("MTU changed to $mtu")
                    gatt.discoverServices()
                }
            }

            @SuppressLint("MissingPermission")
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    listener?.onConnectionGattServer()
                    val service = gatt.getService(UUID.fromString(userServiceUUID))
                    service?.let {
                        val characteristic = it.getCharacteristic(CHARACTERISTIC_UUID)
                        characteristic?.let { enableNotification(gatt, it) }
                    }
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic
            ) {
                when (characteristic.uuid) {
                    CHARACTERISTIC_UUID -> {
                        val data = String(characteristic.value, Charsets.UTF_8)
                        Timber.e(data)
                        listener?.onCharacteristicChanged(data)
                    }
                }
            }
        })
    }

    // 알림 활성화
    @SuppressLint("MissingPermission")
    private fun enableNotification(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        gatt.setCharacteristicNotification(characteristic, true)
        val descriptor =
            characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        gatt.writeDescriptor(descriptor)
    }

    fun setServiceUUID(uuid: String) {
        userServiceUUID = uuid
    }

    fun setListener(listener: BluetoothModuleListener) {
        this.listener = listener
    }

    fun isConnect(): Boolean {
        return bluetoothGatt?.let {
            it.device != null && it.services.isNotEmpty()
        } ?: false
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        listener?.onDisconnectionGattServer()
    }

    interface BluetoothModuleListener {
        fun onScanStarted()
        fun onScanResult(scanResult: ScanResult)
        fun onScanFailed(errorCode: Int)
        fun onScanStopped()
        fun onConnectionGattServer()
        fun onDisconnectionGattServer()
        fun onCharacteristicChanged(data: String)
    }
}
