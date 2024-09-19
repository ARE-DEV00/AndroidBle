package kr.co.are.androidble.ui.connection

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.are.androidble.ui.component.Route
import timber.log.Timber

fun NavController.navigateConnection() {
    Timber.d("### navigate: ${Route.Connection.path}")
    navigate(Route.Connection.path)
}

fun NavGraphBuilder.connectionNavGraph(
) {
    composable(
        route = Route.Connection.path,
    ) {
        ConnectionScreen()
    }
}