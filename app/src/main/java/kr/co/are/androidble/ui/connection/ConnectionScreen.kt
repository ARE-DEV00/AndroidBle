package kr.co.are.androidble.ui.connection

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.co.are.androidble.ui.component.QRCodeScanner
import kr.co.are.androidble.ui.connection.model.ConnectionUiState

@SuppressLint("MissingPermission")
@Composable
fun ConnectionScreen(
    modifier: Modifier = Modifier,
    viewModel: ConnectionViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current

    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var isQRCodeScanned by viewModel.isQRCodeScanned
    var uuidInput by viewModel.uuidInput


    LaunchedEffect(Unit) {
        viewModel.initBluetooth()
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
            // UUID 입력 필드 추가
            OutlinedTextField(
                value = uuidInput,
                onValueChange = { uuidInput = it },
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
                        uuidInput = scannedCode
                    })
                }
                ActionButton(
                    text = "QR 코드 스캔 종료",
                    onClick = { isQRCodeScanned = false },
                )
            } else {
                ActionButton(
                    text = "QR 코드 스캔 시작",
                    onClick = { isQRCodeScanned = true },
                )
            }

            // UI 상태에 따른 화면 표시
            when (homeUiState) {
                is ConnectionUiState.BluetoothData -> {
                    // BluetoothData 상태 처리
                }

                ConnectionUiState.ConnectionBluetooth -> {
                    ActionButton(
                        text = "연결 해제",
                        onClick = { viewModel.disconnect() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MessageText(text = "블루투스 장치에 연결되었습니다.")
                }

                ConnectionUiState.DisconnectionBluetooth -> {
                    ActionButton(
                        text = "검색 시작",
                        onClick = { viewModel.startScan(uuidInput) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MessageText(text = "블루투스 장치에서 연결 해제되었습니다.")
                }

                is ConnectionUiState.Error -> {
                    val isNetwork = (homeUiState as ConnectionUiState.Error).isNetwork
                    MessageText(
                        text = "오류 발생: $isNetwork",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                ConnectionUiState.Ready -> {
                    ActionButton(
                        text = "검색 시작",
                        onClick = { viewModel.startScan(uuidInput) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MessageText(text = "블루투스 연결 준비 완료")
                }

                ConnectionUiState.StartSearching -> {
                    ActionButton(
                        text = "검색 중지",
                        onClick = { viewModel.stopScan() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))

                    MessageText(text = "장치를 검색 중입니다...")
                }

                ConnectionUiState.StopSearching -> {
                    ActionButton(
                        text = "검색 시작",
                        onClick = { viewModel.startScan(uuidInput) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MessageText(text = "검색이 중지되었습니다.")
                }

                is ConnectionUiState.ErrorSearching -> {
                    ActionButton(
                        text = "검색 시작",
                        onClick = { viewModel.startScan(uuidInput) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MessageText(
                        text = "검색 오류: ${(homeUiState as ConnectionUiState.ErrorSearching).code}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

            }
        }
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
