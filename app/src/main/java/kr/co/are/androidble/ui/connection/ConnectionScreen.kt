package kr.co.are.androidble.ui.connection

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.co.are.androidble.ui.bluetooth.BluetoothViewModel
import kr.co.are.androidble.ui.bluetooth.model.BluetoothUiState
import kr.co.are.androidble.ui.component.BluetoothConnectedCard
import kr.co.are.androidble.ui.component.BluetoothConnectionRequiredCard
import kr.co.are.androidble.ui.component.QRCodeScanner
import kr.co.are.androidble.ui.component.SearchBluetoothDeviceCard

@SuppressLint("MissingPermission")
@Composable
fun ConnectionScreen(
    modifier: Modifier = Modifier,
    viewModel: ConnectionViewModel = hiltViewModel(),
    bluetoothViewModel: BluetoothViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val bluetoothUiState by bluetoothViewModel.bluetoothUiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var isQRCodeScanned by viewModel.isQRCodeScanned
    var uuidInput by viewModel.uuidInput


    LaunchedEffect(Unit) {
        bluetoothViewModel.initBluetooth()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                // 터치 시 키보드 숨기기
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        contentAlignment = Alignment.Center // 콘텐츠를 가운데로 정렬
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp, 16.dp, 16.dp, 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // UI 상태에 따른 화면 표시
            when (bluetoothUiState) {
                BluetoothUiState.ConnectionBluetooth -> {
                    BluetoothConnectedCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    ActionButton(
                        text = "연결 해제",
                        onClick = { bluetoothViewModel.disconnect() }
                    )
                }

                BluetoothUiState.DisconnectionBluetooth -> {
                    BluetoothConnectionRequiredCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    UUIDInputField(
                        uuidInput = uuidInput,
                        onUUIDInputChanged = { uuidInput = it },
                        onIsQRCodeScannedChanged = { isQRCodeScanned = it },
                        isQRCodeScanned = isQRCodeScanned
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ActionButton(
                        text = "검색 시작",
                        onClick = { bluetoothViewModel.startScan(uuidInput) }
                    )
                }

                is BluetoothUiState.Error -> {
                    val isNetwork = (bluetoothUiState as BluetoothUiState.Error).isNetwork
                    MessageText(
                        text = "오류 발생: $isNetwork",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                BluetoothUiState.Ready -> {
                    BluetoothConnectionRequiredCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    UUIDInputField(
                        uuidInput = uuidInput,
                        onUUIDInputChanged = { uuidInput = it },
                        onIsQRCodeScannedChanged = { isQRCodeScanned = it },
                        isQRCodeScanned = isQRCodeScanned
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ActionButton(
                        text = "검색 시작",
                        onClick = { bluetoothViewModel.startScan(uuidInput) }
                    )
                }

                is BluetoothUiState.BluetoothData,
                BluetoothUiState.StartSearching -> {

                    SearchBluetoothDeviceCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    ActionButton(
                        text = "검색 중지",
                        onClick = { bluetoothViewModel.stopScan() }
                    )
                }

                BluetoothUiState.StopSearching -> {

                    BluetoothConnectionRequiredCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    UUIDInputField(
                        uuidInput = uuidInput,
                        onUUIDInputChanged = { uuidInput = it },
                        onIsQRCodeScannedChanged = { isQRCodeScanned = it },
                        isQRCodeScanned = isQRCodeScanned
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ActionButton(
                        text = "검색 시작",
                        onClick = { bluetoothViewModel.startScan(uuidInput) }
                    )
                }

                is BluetoothUiState.ErrorSearching -> {

                    BluetoothConnectionRequiredCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    UUIDInputField(
                        uuidInput = uuidInput,
                        onUUIDInputChanged = { uuidInput = it },
                        onIsQRCodeScannedChanged = { isQRCodeScanned = it },
                        isQRCodeScanned = isQRCodeScanned
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ActionButton(
                        text = "검색 시작",
                        onClick = { bluetoothViewModel.startScan(uuidInput) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MessageText(
                        text = "검색 오류: ${(bluetoothUiState as BluetoothUiState.ErrorSearching).code}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

            }
        }
    }
}

@Composable
fun UUIDInputField(
    uuidInput: String,
    onUUIDInputChanged: (String) -> Unit,
    onIsQRCodeScannedChanged: (Boolean) -> Unit,
    isQRCodeScanned: Boolean,
) {
    // UUID 입력 필드 추가
    OutlinedTextField(
        value = uuidInput,
        onValueChange = { onUUIDInputChanged(it) },
        label = { Text("UUID 입력") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )

    if (isQRCodeScanned) {
        Box(
            Modifier
                .width(300.dp)
                .height(300.dp)
        ) {
            QRCodeScanner(onQRCodeScanned = { scannedCode ->
                onUUIDInputChanged(scannedCode)
            })
        }
        ActionButton(
            text = "QR 코드 스캔 종료",
            onClick = { onIsQRCodeScannedChanged(false) },
        )
    } else {
        ActionButton(
            text = "QR 코드 스캔 시작",
            onClick = { onIsQRCodeScannedChanged(true) },
        )
    }
}



@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun MessageText(
    text: String,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge
    )
}
