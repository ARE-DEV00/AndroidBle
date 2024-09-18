package kr.co.are.androidble.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kr.co.are.androidble.ui.chart.ChartScreen
import kr.co.are.androidble.ui.connection.ConnectionScreen
import kr.co.are.androidble.ui.data.DataScreen

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
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Connection.screenRoute,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(BottomNavItem.Connection.screenRoute) {
            ConnectionScreen()
        }
        composable(BottomNavItem.Data.screenRoute) {
            DataScreen()
        }
        composable(BottomNavItem.Chart.screenRoute) {
            ChartScreen()
        }
    }
}


