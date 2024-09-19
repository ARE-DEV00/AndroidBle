package kr.co.are.androidble.ui.chart

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.are.androidble.ui.component.Route
import timber.log.Timber

fun NavController.navigateChart() {
    Timber.d("### navigate: ${Route.Chart.path}")
    navigate(Route.Chart.path)
}

fun NavGraphBuilder.chartNavGraph(
) {
    composable(
        route = Route.Chart.path,
    ) {
        ChartScreen()
    }
}