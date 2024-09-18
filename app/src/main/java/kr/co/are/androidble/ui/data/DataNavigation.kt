package kr.co.are.androidble.ui.data

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.co.are.androidble.ui.component.Route
import timber.log.Timber

fun NavController.navigateData() {
    Timber.d("### navigate: ${Route.Data.path}")
    navigate(Route.Data.path)
}

fun NavGraphBuilder.dataNavGraph(
) {
    composable(
        route = Route.Data.path,
    ) {
        DataScreen()
    }
}