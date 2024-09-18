package kr.co.are.androidble.ui.connection

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.co.are.androidble.ui.connection.model.ConnectionUiState

@SuppressLint("MissingPermission")
@Composable
fun ConnectionScreen(
    modifier: Modifier = Modifier,
    viewModel: ConnectionViewModel = viewModel()
) {
    // Bluetooth 초기화 (필요한 경우)
    LaunchedEffect(Unit) {
        viewModel.initBluetooth()
    }

    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()

    Box(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // UI 상태에 따른 화면 표시
            when (homeUiState) {
                is ConnectionUiState.BluetoothData -> {

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
                        onClick = { viewModel.startScan() }
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
                        onClick = { viewModel.startScan() }
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
                        onClick = { viewModel.startScan() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MessageText(text = "검색이 중지되었습니다.")
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
            .fillMaxWidth()
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
