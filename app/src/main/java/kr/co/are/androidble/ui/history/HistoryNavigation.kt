package kr.co.are.androidble.ui.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.are.androidble.ui.component.Route
import timber.log.Timber

fun NavController.navigateHistory() {
    Timber.d("### navigate: ${Route.History.path}")
    navigate(Route.History.path)
}

fun NavGraphBuilder.historyNavGraph(
) {
    composable(
        route = Route.History.path,
    ) {
        HistoryScreen()
    }
}