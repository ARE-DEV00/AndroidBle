package kr.co.are.androidble.ui.data

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kr.co.are.androidble.bluetooth.BluetoothModule

@Composable
fun DataScreen(
    modifier: Modifier = Modifier
) {

    // LocalContext를 사용하여 현재 Application 가져오기
    val context = LocalContext.current.applicationContext as android.app.Application

    // BluetoothModule 사용


    Box(modifier) {
        if(BluetoothModule.getInstance(application = context).isConnect())
            Text(text = "Connected")
        else
            Text(text = "Disconnected")
    }
}