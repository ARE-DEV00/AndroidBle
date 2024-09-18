package kr.co.are.androidble.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.filled.Home
import kr.co.are.androidble.R

sealed class BottomNavItem(

    val title: String, @DrawableRes val iconResId: Int, val screenRoute: String
) {
    object Connection : BottomNavItem(
        title = "연결",
        iconResId = R.drawable.ic_connection,
        screenRoute = Route.Connection.path
    )
    object History : BottomNavItem(
        title = "히스토리",
        iconResId = R.drawable.ic_data_connection,
        screenRoute = Route.History.path
    )
    object Chart : BottomNavItem(
        title = "차트",
        iconResId = R.drawable.ic_chart_connection,
        screenRoute = Route.Chart.path
    )
}
