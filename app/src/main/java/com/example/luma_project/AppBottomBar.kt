package com.example.luma_project

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

private data class BottomNavItem(
    val label: String,
    val route: String?,                 // null = screen not built yet
    val icon: @Composable () -> Unit,   // drawn in the tab's current tint
)

private val bottomNavItems = listOf(
    BottomNavItem("Home", "home") { Icon(Icons.Filled.Home, contentDescription = "Home") },
    BottomNavItem("Insights", "insights") { InsightsRingIcon() },
    BottomNavItem("Activity", "transactions") { Icon(Icons.Outlined.SwapHoriz, contentDescription = "Activity") },
    BottomNavItem("Accounts", "accounts") { Icon(Icons.Outlined.AccountBalanceWallet, contentDescription = "Accounts") },
)

/**
 * A ring with a small gap, like the Insights icon in the reference.
 * It picks up the tab's tint automatically (selected vs unselected).
 */
@Composable
private fun InsightsRingIcon() {
    val tint = LocalContentColor.current
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = 3.dp.toPx()
        val inset = stroke / 2
        drawArc(
            color = tint,
            startAngle = -60f,          // gap sits at the upper right
            sweepAngle = 300f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = Size(size.width - stroke, size.height - stroke),
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
    }
}

/**
 * Shared bottom bar. [currentScreen] decides which tab is highlighted,
 * [onNavigate] is called with the route of the tapped tab.
 */
@Composable
fun AppBottomBar(
    currentScreen: String,
    onNavigate: (String) -> Unit,
) {
    NavigationBar(containerColor = Color.White) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = item.route == currentScreen,
                onClick = { item.route?.let(onNavigate) },
                icon = item.icon,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}