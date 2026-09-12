package com.example.syskit.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.syskit.data.SystemDataProvider
import com.example.syskit.model.ExecutionMode
import com.example.syskit.model.HardwareStats
import com.example.syskit.model.NetworkDetails
import com.example.syskit.model.PackageItem
import com.example.syskit.model.PrivilegeState
import com.example.syskit.model.SystemInfo
import com.example.syskit.model.TerminalEntry
import com.example.syskit.privilege.PrivilegeManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SysKitViewModel(application: Application) : AndroidViewModel(application) {

    private val privilegeManager = PrivilegeManager(application)
    private val dataProvider = SystemDataProvider(application)

    private val _privilegeState = MutableStateFlow(PrivilegeState())
    val privilegeState: StateFlow<PrivilegeState> = _privilegeState.asStateFlow()

    private val _systemInfo = MutableStateFlow(SystemInfo())
    val systemInfo: StateFlow<SystemInfo> = _systemInfo.asStateFlow()

    private val _hardwareStats = MutableStateFlow(HardwareStats())
    val hardwareStats: StateFlow<HardwareStats> = _hardwareStats.asStateFlow()

    private val _packages = MutableStateFlow<List<PackageItem>>(emptyList())
    val packages: StateFlow<List<PackageItem>> = _packages.asStateFlow()

    private val _networkDetails = MutableStateFlow(NetworkDetails())
    val networkDetails: StateFlow<NetworkDetails> = _networkDetails.asStateFlow()

    private val _terminalEntries = MutableStateFlow<List<TerminalEntry>>(
        listOf(
            TerminalEntry(
                command = "syskit --init",
                output = "SysKit Core Engine v2.0 ready.\nSupported backends: Magisk, KernelSU, APatch, Shizuku (ADB API).\nType any command or use quick actions below.",
                modeUsed = "Init"
            )
        )
    )
    val terminalEntries: StateFlow<List<TerminalEntry>> = _terminalEntries.asStateFlow()

    private val _isExecuting = MutableStateFlow(false)
    val isExecuting: StateFlow<Boolean> = _isExecuting.asStateFlow()

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("ALL") // "ALL", "USER", "SYSTEM", "DISABLED"
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    init {
        loadInitialData()
        startStatsMonitor()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            refreshPrivileges()
            _systemInfo.value = dataProvider.getSystemInfo()
            _hardwareStats.value = dataProvider.getHardwareStats()
            _networkDetails.value = dataProvider.getNetworkDetails()
            loadPackages()
        }
    }

    fun refreshPrivileges() {
        viewModelScope.launch {
            val currentMode = _privilegeState.value.activeMode
            val newState = privilegeManager.inspectPrivileges()
            _privilegeState.value = newState.copy(activeMode = currentMode)
        }
    }

    private fun startStatsMonitor() {
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(3000)
                _hardwareStats.value = dataProvider.getHardwareStats()
            }
        }
    }

    fun requestRoot() {
        viewModelScope.launch {
            _isExecuting.value = true
            val granted = privilegeManager.requestRootAccess()
            refreshPrivileges()
            _isExecuting.value = false
            _feedbackMessage.value = if (granted) "Root permission verified (UID 0)" else "Root access denied or not granted"
        }
    }

    fun requestShizuku() {
        val initiated = privilegeManager.requestShizukuPermission()
        if (!initiated) {
            _feedbackMessage.value = "Shizuku service not running or not installed"
        } else {
            _feedbackMessage.value = "Shizuku permission dialog prompted"
        }
        viewModelScope.launch {
            delay(1500)
            refreshPrivileges()
        }
    }

    fun setExecutionMode(mode: ExecutionMode) {
        _privilegeState.value = _privilegeState.value.copy(activeMode = mode)
        _feedbackMessage.value = "Active Mode: ${mode.displayName}"
    }

    fun clearFeedbackMessage() {
        _feedbackMessage.value = null
    }

    fun loadPackages() {
        viewModelScope.launch {
            _packages.value = dataProvider.getInstalledPackages()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun executeTerminalCommand(cmd: String) {
        if (cmd.isBlank()) return
        viewModelScope.launch {
            _isExecuting.value = true
            val currentMode = _privilegeState.value.activeMode
            val res = privilegeManager.executeCommand(cmd.trim(), currentMode)

            val entry = TerminalEntry(
                command = cmd.trim(),
                output = res.output,
                isError = res.isError,
                exitCode = res.exitCode,
                modeUsed = res.modeUsed
            )
            _terminalEntries.value = _terminalEntries.value + entry
            _isExecuting.value = false
        }
    }

    fun clearTerminal() {
        _terminalEntries.value = emptyList()
    }

    // App debloater / manager actions
    fun freezePackage(packageName: String) {
        viewModelScope.launch {
            val cmd = "pm disable-user --user 0 $packageName"
            val res = privilegeManager.executeCommand(cmd, _privilegeState.value.activeMode)
            _feedbackMessage.value = if (!res.isError) "Froze $packageName" else "Failed: ${res.output.lines().firstOrNull()}"
            loadPackages()
        }
    }

    fun unfreezePackage(packageName: String) {
        viewModelScope.launch {
            val cmd = "pm enable $packageName"
            val res = privilegeManager.executeCommand(cmd, _privilegeState.value.activeMode)
            _feedbackMessage.value = if (!res.isError) "Enabled $packageName" else "Failed: ${res.output.lines().firstOrNull()}"
            loadPackages()
        }
    }

    fun forceStopPackage(packageName: String) {
        viewModelScope.launch {
            val cmd = "am force-stop $packageName"
            val res = privilegeManager.executeCommand(cmd, _privilegeState.value.activeMode)
            _feedbackMessage.value = if (!res.isError) "Stopped $packageName" else "Failed: ${res.output.lines().firstOrNull()}"
        }
    }

    fun clearPackageData(packageName: String) {
        viewModelScope.launch {
            val cmd = "pm clear $packageName"
            val res = privilegeManager.executeCommand(cmd, _privilegeState.value.activeMode)
            _feedbackMessage.value = if (!res.isError) "Cleared data for $packageName" else "Failed: ${res.output.lines().firstOrNull()}"
        }
    }

    // Power & System tools
    fun reboot(type: String) {
        viewModelScope.launch {
            val cmd = when (type) {
                "recovery" -> "svc power reboot recovery || reboot recovery"
                "bootloader" -> "svc power reboot bootloader || reboot bootloader"
                "soft" -> "setprop ctl.restart zygote"
                "shutdown" -> "svc power shutdown || reboot -p"
                else -> "svc power reboot || reboot"
            }
            val res = privilegeManager.executeCommand(cmd, _privilegeState.value.activeMode)
            _feedbackMessage.value = res.output.lines().firstOrNull() ?: "Reboot command sent"
        }
    }

    fun setSELinux(permissive: Boolean) {
        viewModelScope.launch {
            val cmd = if (permissive) "setenforce 0" else "setenforce 1"
            val res = privilegeManager.executeCommand(cmd, ExecutionMode.ROOT)
            _feedbackMessage.value = if (!res.isError) {
                "SELinux set to ${if (permissive) "Permissive" else "Enforcing"}"
            } else {
                "SELinux change requires Root (su)"
            }
            refreshPrivileges()
            _systemInfo.value = dataProvider.getSystemInfo()
        }
    }

    fun trimCaches() {
        viewModelScope.launch {
            _isExecuting.value = true
            val cmd = "pm trim-caches 999999999999; rm -rf /data/local/tmp/*"
            val res = privilegeManager.executeCommand(cmd, _privilegeState.value.activeMode)
            _isExecuting.value = false
            _feedbackMessage.value = "Cache trim completed: ${res.output.lines().firstOrNull() ?: "Done"}"
            _hardwareStats.value = dataProvider.getHardwareStats()
        }
    }

    fun resetBatteryStats() {
        viewModelScope.launch {
            val cmd = "dumpsys batterystats --reset"
            val res = privilegeManager.executeCommand(cmd, _privilegeState.value.activeMode)
            _feedbackMessage.value = if (!res.isError) "Battery statistics reset" else "Battery reset failed: ${res.output.lines().firstOrNull()}"
        }
    }

    fun refreshNetwork() {
        viewModelScope.launch {
            _networkDetails.value = dataProvider.getNetworkDetails()
        }
    }
}
