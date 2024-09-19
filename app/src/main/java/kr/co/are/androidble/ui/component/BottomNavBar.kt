package kr.co.are.androidble.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Connection,
        BottomNavItem.Chart,
        BottomNavItem.History,
    )
    Surface(
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent, // 배경색을 투명하게 설정
        ) {
            val currentRoute = currentRoute(navController)
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(id = item.iconResId),
                            contentDescription = null
                        )
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.screenRoute,
                    onClick = {
                        navController.navigate(item.screenRoute) {
                            // 동일한 화면 스택이 쌓이지 않도록 설정
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // 현재 경로를 다시 시작하지 않도록 설정
                            launchSingleTop = true
                            // 이전 상태 복원
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}