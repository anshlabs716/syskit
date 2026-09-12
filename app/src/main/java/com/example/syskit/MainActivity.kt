package com.example.syskit

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.syskit.model.ExecutionMode
import com.example.syskit.privilege.PrivilegeManager
import com.example.syskit.theme.BrightCyan
import com.example.syskit.theme.NeonGreen
import com.example.syskit.theme.SysKitTheme
import com.example.syskit.ui.SysKitViewModel
import com.example.syskit.ui.components.TopMetricsHeader
import com.example.syskit.ui.screens.DashboardScreen
import com.example.syskit.ui.screens.NetworkScreen
import com.example.syskit.ui.screens.PackageManagerScreen
import com.example.syskit.ui.screens.PowerToolsScreen
import com.example.syskit.ui.screens.PrivilegeScreen
import com.example.syskit.ui.screens.TerminalScreen
import rikka.shizuku.Shizuku

enum class AppDestination(val title: String, val icon: ImageVector) {
    DASHBOARD("System", Icons.Default.Dashboard),
    PRIVILEGE("Pairing", Icons.Default.AdminPanelSettings),
    POWER("Tools", Icons.Default.Build),
    PACKAGES("Apps", Icons.Default.Apps),
    TERMINAL("Console", Icons.Default.Terminal),
    NETWORK("Net", Icons.Default.NetworkCheck)
}

class MainActivity : ComponentActivity() {

    private val viewModel: SysKitViewModel by viewModels()

    private val permissionResultListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (requestCode == PrivilegeManager.SHIZUKU_REQUEST_CODE) {
            viewModel.refreshPrivileges()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        runCatching {
            Shizuku.addRequestPermissionResultListener(permissionResultListener)
        }

        setContent {
            SysKitTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runCatching {
            Shizuku.removeRequestPermissionResultListener(permissionResultListener)
        }
    }
}

@Composable
fun MainAppScreen(viewModel: SysKitViewModel) {
    val privilegeState by viewModel.privilegeState.collectAsStateWithLifecycle()
    val systemInfo by viewModel.systemInfo.collectAsStateWithLifecycle()
    val hardwareStats by viewModel.hardwareStats.collectAsStateWithLifecycle()
    val packages by viewModel.packages.collectAsStateWithLifecycle()
    val networkDetails by viewModel.networkDetails.collectAsStateWithLifecycle()
    val terminalEntries by viewModel.terminalEntries.collectAsStateWithLifecycle()
    val isExecuting by viewModel.isExecuting.collectAsStateWithLifecycle()
    val feedbackMessage by viewModel.feedbackMessage.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

    var currentDestination by remember { mutableStateOf(AppDestination.DASHBOARD) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearFeedbackMessage()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopMetricsHeader(
                privilegeState = privilegeState,
                hardwareStats = hardwareStats,
                onPrivilegeClick = { currentDestination = AppDestination.PRIVILEGE }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                AppDestination.values().forEach { dest ->
                    val selected = currentDestination == dest
                    NavigationBarItem(
                        selected = selected,
                        onClick = { currentDestination = dest },
                        icon = {
                            Icon(
                                imageVector = dest.icon,
                                contentDescription = dest.title,
                                tint = if (selected) NeonGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                text = dest.title,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = if (selected) NeonGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = NeonGreen.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_${dest.name.lowercase()}")
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                AppDestination.DASHBOARD -> {
                    DashboardScreen(
                        systemInfo = systemInfo,
                        hardwareStats = hardwareStats
                    )
                }
                AppDestination.PRIVILEGE -> {
                    PrivilegeScreen(
                        privilegeState = privilegeState,
                        onRequestRoot = { viewModel.requestRoot() },
                        onRequestShizuku = { viewModel.requestShizuku() },
                        onRefresh = { viewModel.refreshPrivileges() },
                        onSetMode = { viewModel.setExecutionMode(it) }
                    )
                }
                AppDestination.POWER -> {
                    PowerToolsScreen(
                        privilegeState = privilegeState,
                        onReboot = { viewModel.reboot(it) },
                        onTrimCaches = { viewModel.trimCaches() },
                        onResetBattery = { viewModel.resetBatteryStats() },
                        onSetSELinux = { viewModel.setSELinux(it) }
                    )
                }
                AppDestination.PACKAGES -> {
                    PackageManagerScreen(
                        packages = packages,
                        searchQuery = searchQuery,
                        selectedFilter = selectedFilter,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onFilterChange = { viewModel.setFilter(it) },
                        onFreezePackage = { viewModel.freezePackage(it) },
                        onUnfreezePackage = { viewModel.unfreezePackage(it) },
                        onForceStopPackage = { viewModel.forceStopPackage(it) },
                        onClearPackageData = { viewModel.clearPackageData(it) }
                    )
                }
                AppDestination.TERMINAL -> {
                    TerminalScreen(
                        privilegeState = privilegeState,
                        terminalEntries = terminalEntries,
                        isExecuting = isExecuting,
                        onExecuteCommand = { viewModel.executeTerminalCommand(it) },
                        onClearTerminal = { viewModel.clearTerminal() }
                    )
                }
                AppDestination.NETWORK -> {
                    NetworkScreen(
                        networkDetails = networkDetails,
                        onRefresh = { viewModel.refreshNetwork() },
                        onPingHost = { pingCmd ->
                            currentDestination = AppDestination.TERMINAL
                            viewModel.executeTerminalCommand(pingCmd)
                        },
                        isExecuting = isExecuting
                    )
                }
            }
        }
    }
}
