package com.example.syskit.engine

import com.example.syskit.model.DeepDeviceInfo
import com.example.syskit.model.HardwareStats
import com.example.syskit.model.PrivilegeState
import com.example.syskit.model.SystemInfo

/**
 * Neofetch/fastfetch-style system readout with a bugdroid, generated locally —
 * Android ships neither binary, so the console intercepts those commands.
 */
object FetchArt {

    private val bugdroid = listOf(
        "     -o         o-     ",
        "      +hydNNNNdyh+     ",
        "    +mMMMMMMMMMMMMm+   ",
        "  `dMMm:NMMSSMMMNd`    ",
        "  hMMMMMMMMMMMMMMMMh   ",
        "  MMMMMMMMMMMMMMMMMM   ",
        " .MMMMMMMMMMMMMMMMMM.  ",
        " dMMMMMMMMMMMMMMMMMMd  ",
        " sMMMMMMMMMMMMMMMMMMs  ",
        "  MMMM  MMMMMM  MMMM   "
    )

    fun generate(
        info: SystemInfo,
        stats: HardwareStats,
        deep: DeepDeviceInfo,
        priv: PrivilegeState
    ): String {
        val ramUsedGb = stats.ramUsedBytes / (1024.0 * 1024.0 * 1024.0)
        val ramTotalGb = stats.ramTotalBytes / (1024.0 * 1024.0 * 1024.0)
        val infoLines = listOf(
            "${info.manufacturer} ${info.deviceModel}".trim().ifBlank { "Android" },
            "-----------------------",
            "OS: Android ${info.androidVersion} (API ${info.apiLevel})",
            "Security patch: ${info.securityPatch.ifBlank { "unknown" }}",
            "SoC: ${info.cpuModel.ifBlank { "unknown" }}",
            "Cores: ${info.coreCount} (${info.cpuArch})",
            "Kernel: ${info.kernelVersion.take(34)}",
            "Uptime: ${info.uptimeFormatted}",
            "RAM: ${"%.1f".format(ramUsedGb)} / ${"%.1f".format(ramTotalGb)} GiB",
            "Battery: ${stats.batteryPercent}% ${stats.batteryTempCelsius}°C" + if (stats.isCharging) " ⚡" else "",
            "SELinux: ${info.selinuxStatus.ifBlank { "unknown" }}",
            "Verified boot: ${deep.verifiedBootState}",
            "Root: ${if (priv.rootGranted) "yes (${priv.rootManager.displayName})" else "no"}" +
                if (priv.shizukuGranted) " • Shizuku" else "",
            "Apps: SysKit v2 fetch"
        )

        val width = bugdroid.maxOf { it.length }
        val lines = mutableListOf<String>()
        val rows = maxOf(bugdroid.size, infoLines.size)
        for (i in 0 until rows) {
            val logo = bugdroid.getOrElse(i) { " ".repeat(width) }
            val text = infoLines.getOrElse(i) { "" }
            lines.add(logo + "  " + text)
        }
        return lines.joinToString("\n")
    }
}
