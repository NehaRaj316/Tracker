package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MockTestScreen
import com.example.ui.screens.RevisionScreen
import com.example.ui.screens.RoadmapScreen
import com.example.ui.screens.SummaryDashboardScreen
import com.example.ui.screens.TodayScheduleScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.PlannerViewModel

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    TODAY(
        title = "Today",
        selectedIcon = Icons.Filled.Today,
        unselectedIcon = Icons.Outlined.Today,
        testTag = "nav_today"
    ),
    ROADMAP(
        title = "Roadmap",
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map,
        testTag = "nav_roadmap"
    ),
    DASHBOARD(
        title = "Dashboard",
        selectedIcon = Icons.Filled.PieChart,
        unselectedIcon = Icons.Outlined.PieChart,
        testTag = "nav_dashboard"
    ),
    MOCK_TESTS(
        title = "Mock Tests",
        selectedIcon = Icons.Filled.Assignment,
        unselectedIcon = Icons.Outlined.Assignment,
        testTag = "nav_mock_tests"
    ),
    REVISION(
        title = "Revision",
        selectedIcon = Icons.Filled.AutoFixHigh,
        unselectedIcon = Icons.Outlined.AutoFixHigh,
        testTag = "nav_revision"
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: PlannerViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(MainTab.TODAY) }
    var navigateToMockNumber by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = androidx.compose.ui.res.stringResource(id = R.string.app_name),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = "10 AM - 10 PM • 30 Full Mocks • Rigorous Revision",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                MainTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.TODAY -> {
                    TodayScheduleScreen(
                        viewModel = viewModel,
                        onNavigateToMockTest = { testNum ->
                            navigateToMockNumber = testNum
                            selectedTab = MainTab.MOCK_TESTS
                        }
                    )
                }
                MainTab.ROADMAP -> {
                    RoadmapScreen(
                        viewModel = viewModel,
                        onDaySelected = {
                            selectedTab = MainTab.TODAY
                        }
                    )
                }
                MainTab.DASHBOARD -> {
                    SummaryDashboardScreen(
                        viewModel = viewModel,
                        onNavigateToDay = { dayNum ->
                            viewModel.selectDay(dayNum)
                            selectedTab = MainTab.TODAY
                        },
                        onNavigateToMocks = {
                            selectedTab = MainTab.MOCK_TESTS
                        }
                    )
                }
                MainTab.MOCK_TESTS -> {
                    MockTestScreen(
                        viewModel = viewModel,
                        initialTestNumber = navigateToMockNumber
                    )
                }
                MainTab.REVISION -> {
                    RevisionScreen(viewModel = viewModel)
                }
            }
        }
    }
}
