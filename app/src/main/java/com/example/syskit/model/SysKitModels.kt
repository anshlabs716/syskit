package com.example.syskit.model

enum class ExecutionMode(val displayName: String, val promptSymbol: String) {
    AUTO("Auto Detect", "auto"),
    ROOT("Root (Magisk / KernelSU)", "#"),
    SHIZUKU("Shizuku (ADB Shell)", "$"),
    STANDARD("Standard (Unprivileged)", ">")
}

enum class RootManager(val displayName: String) {
    MAGISK("Magisk"),
    KERNEL_SU("KernelSU"),
    APATCH("APatch"),
    GENERIC_SU("Generic Superuser"),
    NONE("Not Detected")
}

data class PrivilegeState(
    val rootAvailable: Boolean = false,
    val rootGranted: Boolean = false,
    val rootManager: RootManager = RootManager.NONE,
    val suBinaryPath: String = "",
    val shizukuInstalled: Boolean = false,
    val shizukuRunning: Boolean = false,
    val shizukuGranted: Boolean = false,
    val shizukuVersion: Int = 0,
    val selinuxMode: String = "Unknown",
    val activeMode: ExecutionMode = ExecutionMode.AUTO
) {
    val effectiveLevel: String
        get() = when {
            rootGranted -> "ROOT (UID 0)"
            shizukuGranted -> "SHIZUKU (UID 2000)"
            else -> "USER (Restricted)"
        }
}

data class SystemInfo(
    val deviceModel: String = "",
    val manufacturer: String = "",
    val board: String = "",
    val androidVersion: String = "",
    val apiLevel: Int = 0,
    val securityPatch: String = "",
    val buildNumber: String = "",
    val kernelVersion: String = "",
    val uptimeFormatted: String = "",
    val cpuModel: String = "",
    val cpuArch: String = "",
    val coreCount: Int = 1,
    val selinuxStatus: String = ""
)

data class HardwareStats(
    val batteryPercent: Int = 0,
    val isCharging: Boolean = false,
    val batteryHealth: String = "",
    val batteryTempCelsius: Float = 0f,
    val batteryVoltageMv: Int = 0,
    val batteryTechnology: String = "",
    val ramTotalBytes: Long = 0L,
    val ramUsedBytes: Long = 0L,
    val ramAvailBytes: Long = 0L,
    val storageTotalBytes: Long = 0L,
    val storageUsedBytes: Long = 0L,
    val storageFreeBytes: Long = 0L
)

data class PackageItem(
    val packageName: String,
    val appName: String,
    val versionName: String,
    val isSystemApp: Boolean,
    val isEnabled: Boolean,
    val targetSdkVersion: Int
)

data class TerminalEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val command: String,
    val output: String,
    val isError: Boolean = false,
    val exitCode: Int = 0,
    val modeUsed: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class NetworkDetails(
    val isConnected: Boolean = false,
    val networkType: String = "Disconnected",
    val ipv4Address: String = "N/A",
    val ipv6Address: String = "N/A",
    val gateway: String = "N/A",
    val dnsServers: List<String> = emptyList(),
    val wifiSsid: String = "N/A",
    val wifiLinkSpeed: String = "N/A"
)
