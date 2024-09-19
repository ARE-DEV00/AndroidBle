package kr.co.are.androidble.ui.chart

import GlucoseLevelCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kr.co.are.androidble.ui.bluetooth.BluetoothViewModel
import kr.co.are.androidble.ui.bluetooth.model.BluetoothDataDbUiState
import kr.co.are.androidble.ui.bluetooth.model.BluetoothDataUiState
import kr.co.are.androidble.ui.bluetooth.model.BluetoothUiState
import kr.co.are.androidble.ui.component.BluetoothConnectionRequiredCard
import kr.co.are.androidble.ui.component.chart.GlucoseLineChart

@Composable
fun ChartScreen(
    modifier: Modifier = Modifier,
    viewModel: ChartViewModel = hiltViewModel(),
    bluetoothViewModel: BluetoothViewModel = hiltViewModel()
) {

    val bluetoothDataUiState by bluetoothViewModel.bluetoothDataUiState.collectAsStateWithLifecycle()
    val bluetoothUiState by bluetoothViewModel.bluetoothUiState.collectAsStateWithLifecycle()
    val bluetoothDataDbUiState by bluetoothViewModel.bluetoothDataDbUiState.collectAsStateWithLifecycle()
    val dbModelProducer = remember { CartesianChartModelProducer() }
    val realTimeModelProducer = remember { CartesianChartModelProducer() }
    val scrollState = rememberScrollState()

    val realTimeData = bluetoothViewModel.realTimeData

    LaunchedEffect(Unit) {
        bluetoothViewModel.refreshData()
    }

    Column(
        modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp, 100.dp)
    ) {
        if (bluetoothUiState is BluetoothUiState.ConnectionBluetooth) {
            when (bluetoothDataUiState) {
                is BluetoothDataUiState.Error,
                BluetoothDataUiState.Loading -> {
                }

                is BluetoothDataUiState.Success -> {
                    val glucoseInfo = (bluetoothDataUiState as BluetoothDataUiState.Success).data
                    GlucoseLevelCard(
                        glucoseLevel = glucoseInfo.glucoseLevel,
                        unit = glucoseInfo.unit,
                        time = glucoseInfo.time,
                        createdTime = glucoseInfo.createdTime,
                        modifiedTime = glucoseInfo.modifiedTime
                    )
                    Spacer(modifier = Modifier.size(32.dp))
                }
            }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.Default) {
                    while (true) {
                        if (realTimeData.isNotEmpty()) {
                            realTimeModelProducer.runTransaction {
                                lineSeries {
                                    series(
                                        realTimeData.map { it?.glucoseLevel ?: 0 })
                                }
                            }
                        }
                        delay(1000)
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, start = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "실시간 Trend",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            IconButton(
                                onClick = {
                                    realTimeData.clear()
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                    GlucoseLineChart(
                        true,
                        realTimeData,
                        realTimeModelProducer,
                        modifier.height(150.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(32.dp))

            when (bluetoothDataDbUiState) {
                is BluetoothDataDbUiState.Error -> {

                }

                BluetoothDataDbUiState.Loading -> {

                }

                is BluetoothDataDbUiState.Success -> {
                    val glucoseDataList =
                        (bluetoothDataDbUiState as BluetoothDataDbUiState.Success).glucoseDataList
                    LaunchedEffect(Unit) {
                        withContext(Dispatchers.Default) {
                            if (glucoseDataList.isNotEmpty()) {
                                dbModelProducer.runTransaction {
                                    lineSeries {
                                        series(
                                            glucoseDataList.map { it.glucoseLevel ?: 0 })
                                    }
                                }
                            }

                        }
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 16.dp, end = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "전체 히스토리",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                IconButton(
                                    onClick = {
                                        bluetoothViewModel.refreshData()
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            GlucoseLineChart(
                                false,
                                glucoseDataList.toMutableList(),
                                dbModelProducer,
                                Modifier.height(150.dp)
                            )
                        }

                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BluetoothConnectionRequiredCard()

            }
        }

    }
}


