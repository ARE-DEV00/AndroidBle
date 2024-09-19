package kr.co.are.androidble.ui.chart

import GlucoseLevelCard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.co.are.androidble.ui.bluetooth.BluetoothViewModel
import kr.co.are.androidble.ui.bluetooth.model.BluetoothDataUiState
import kr.co.are.androidble.ui.bluetooth.model.BluetoothUiState
import kr.co.are.androidble.ui.component.BluetoothConnectionRequiredCard

@Composable
fun ChartScreen(
    modifier: Modifier = Modifier,
    viewModel: ChartViewModel = hiltViewModel(),
    bluetoothViewModel: BluetoothViewModel = hiltViewModel()
) {

    val bluetoothDataUiState by bluetoothViewModel.bluetoothDataUiState.collectAsStateWithLifecycle()
    val bluetoothUiState by bluetoothViewModel.bluetoothUiState.collectAsStateWithLifecycle()


    Column(modifier.fillMaxSize()) {

        if (bluetoothUiState is BluetoothUiState.ConnectionBluetooth) {
            when (bluetoothDataUiState) {
                is BluetoothDataUiState.Error -> {
                    Text(text = "ChartScreen - Error")
                }

                BluetoothDataUiState.Loading -> {
                    Text(text = "ChartScreen - Loading")
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
                }
            }
        } else {
            BluetoothConnectionRequiredCard()
        }

    }
}

