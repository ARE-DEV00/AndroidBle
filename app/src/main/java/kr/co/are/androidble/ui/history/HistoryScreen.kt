package kr.co.are.androidble.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kr.co.are.androidble.domain.entity.GlucoseInfoEntity
import kr.co.are.androidble.ui.bluetooth.BluetoothViewModel
import kr.co.are.androidble.ui.bluetooth.model.BluetoothDataDbUiState
import kr.co.are.androidble.ui.component.AppHeaderScreen
import kr.co.are.androidble.ui.history.model.HistoryUiState
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
    bluetoothViewModel: BluetoothViewModel = hiltViewModel()
) {
    val bluetoothDataDbUiState by bluetoothViewModel.bluetoothDataDbUiState.collectAsStateWithLifecycle()

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = bluetoothDataDbUiState is BluetoothDataDbUiState.Loading
    )

    LaunchedEffect(UInt) {
        bluetoothViewModel.refreshData()
    }

    AppHeaderScreen(
        headerTitle = "History",
        leftIconImageVector = Icons.Default.Refresh,
        onTabLeftIcon = {
            bluetoothViewModel.refreshData()
        },
        rightIconImageVector = Icons.Default.Delete,
        onTabRightIcon = {
            bluetoothViewModel.deleteAllData()
        },
        modifier = modifier.padding(bottom = 70.dp)
    ) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { bluetoothViewModel.refreshData() },
            modifier = modifier.fillMaxSize()
        ) {
            Box(modifier.fillMaxSize()) {
                when (bluetoothDataDbUiState) {
                    is BluetoothDataDbUiState.Success -> {
                        val glucoseDataList = (bluetoothDataDbUiState as BluetoothDataDbUiState.Success).glucoseDataList
                        if (glucoseDataList.isEmpty()) {
                            // 데이터가 없을 때 메시지 표시
                            EmptyDataMessage()
                        } else {
                            // 데이터 리스트 표시
                            GlucoseDataList(glucoseDataList = glucoseDataList)
                        }
                    }

                    is BluetoothDataDbUiState.Loading -> {
                        // 로딩 인디케이터 표시
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is BluetoothDataDbUiState.Error -> {
                        val errorMessage = (bluetoothDataDbUiState as BluetoothDataDbUiState.Error).message
                        // 에러 메시지 표시
                        ErrorMessage(errorMessage = errorMessage)
                    }
                }
            }
        }
    }


}

@Composable
fun GlucoseDataList(glucoseDataList: List<GlucoseInfoEntity>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(glucoseDataList.size) { index ->
            GlucoseDataItem(glucoseInfo = glucoseDataList[index])
        }
    }
}

@Composable
fun GlucoseDataItem(glucoseInfo: GlucoseInfoEntity) {
    // 날짜 및 시간 형식화
    val formattedTime = remember(glucoseInfo.time) {
        formatDateTime(glucoseInfo.time)
    }
    val formattedCreatedTime = remember(glucoseInfo.createdTime) {
        glucoseInfo.createdTime?.let { formatDateTimeToKST(it) }
    }
    val formattedModifiedTime = remember(glucoseInfo.modifiedTime) {
        glucoseInfo.modifiedTime?.let { formatDateTimeToKST(it) }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "타입: ${glucoseInfo.type}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "혈당 수치: ${glucoseInfo.glucoseLevel} ${glucoseInfo.unit}",
                style = MaterialTheme.typography.bodyLarge
            )
            /*Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "시간: $formattedTime",
                style = MaterialTheme.typography.bodyMedium
            )*/
            if (formattedCreatedTime != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "시간: $formattedCreatedTime",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            /*if (formattedModifiedTime != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "수정일: $formattedModifiedTime",
                    style = MaterialTheme.typography.bodySmall
                )
            }*/
        }
    }
}

@Composable
fun EmptyDataMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "표시할 데이터가 없습니다.")
    }
}

@Composable
fun ErrorMessage(errorMessage: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// 날짜 및 시간 형식화 함수
fun formatDateTime(dateTimeString: String): String {
    return try {
        val dateTime = OffsetDateTime.parse(dateTimeString)
        dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    } catch (e: Exception) {
        dateTimeString // 파싱 실패 시 원본 문자열 반환
    }
}


fun formatDateTimeToKST(dateTimeString: String): String {
    return try {
        // 타임존 없는 문자열을 LocalDateTime으로 파싱
        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        // 한국 시간대 (Asia/Seoul)로 변환
        val koreanDateTime = dateTime.atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.of("Asia/Seoul"))

        // 원하는 형식으로 포맷
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        koreanDateTime.format(formatter)  // 변환된 시간 포맷팅
    } catch (e: Exception) {
        dateTimeString // 파싱 실패 시 원본 문자열 반환
    }
}
