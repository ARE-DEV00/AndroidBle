package kr.co.are.androidble.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.filled.Home
import kr.co.are.androidble.R

sealed class BottomNavItem(

    val title: String, @DrawableRes val iconResId: Int, val screenRoute: String
) {
    data object Connection : BottomNavItem(
        title = "Connection",
        iconResId = R.drawable.ic_connection,
        screenRoute = Route.Connection.path
    )
    data object History : BottomNavItem(
        title = "History",
        iconResId = R.drawable.ic_data_connection,
        screenRoute = Route.History.path
    )
    data object Chart : BottomNavItem(
        title = "Chart",
        iconResId = R.drawable.ic_chart_connection,
        screenRoute = Route.Chart.path
    )
}
