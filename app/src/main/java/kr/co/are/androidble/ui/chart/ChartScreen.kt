package kr.co.are.androidble.ui.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kr.co.are.androidble.ui.bluetooth.BluetoothViewModel

@Composable
fun ChartScreen(
    modifier: Modifier = Modifier,
    viewModel: ChartViewModel = hiltViewModel(),
    bluetoothViewModel: BluetoothViewModel = hiltViewModel()
) {
    Box(modifier) {
        Text(text = "ChartScreen")
    }
}