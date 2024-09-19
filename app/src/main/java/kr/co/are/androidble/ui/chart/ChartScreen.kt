package kr.co.are.androidble.ui.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.co.are.androidble.ui.bluetooth.BluetoothViewModel
import kr.co.are.androidble.ui.bluetooth.model.BluetoothDataUiState

@Composable
fun ChartScreen(
    modifier: Modifier = Modifier,
    viewModel: ChartViewModel = hiltViewModel(),
    bluetoothViewModel: BluetoothViewModel = hiltViewModel()
) {

    val bluetoothDataUiState by bluetoothViewModel.bluetoothDataUiState.collectAsStateWithLifecycle()

    Box(modifier) {
        when(bluetoothDataUiState){
            is BluetoothDataUiState.Error -> {
                Text(text = "ChartScreen - Error")
            }
            BluetoothDataUiState.Loading -> {
                Text(text = "ChartScreen - Loading")
            }
            is BluetoothDataUiState.Success -> {
                Text(text = "ChartScreen - ${(bluetoothDataUiState as BluetoothDataUiState.Success).data.glucoseLevel}")
            }
        }


    }
}