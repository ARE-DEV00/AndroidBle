package kr.co.are.androidble.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kr.co.are.androidble.ui.bluetooth.BluetoothViewModel
import kr.co.are.androidble.ui.chart.ChartScreen
import kr.co.are.androidble.ui.connection.ConnectionScreen
import kr.co.are.androidble.ui.history.HistoryScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) {
        NavigationGraph(navController = navController)
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    val bluetoothViewModel: BluetoothViewModel = hiltViewModel()

    // ViewModel 초기화
    LaunchedEffect(Unit) {
        bluetoothViewModel.initBluetooth()
    }
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Connection.screenRoute,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(BottomNavItem.Connection.screenRoute) {
            ConnectionScreen(
                bluetoothViewModel = bluetoothViewModel
            )
        }
        composable(BottomNavItem.History.screenRoute) {
            HistoryScreen(
                bluetoothViewModel = bluetoothViewModel
            )
        }
        composable(BottomNavItem.Chart.screenRoute) {
            ChartScreen(
                bluetoothViewModel = bluetoothViewModel
            )
        }
    }
}


