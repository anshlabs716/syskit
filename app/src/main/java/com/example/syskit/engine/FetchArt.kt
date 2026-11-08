package com.example.syskit.engine

import com.example.syskit.model.DeepDeviceInfo
import com.example.syskit.model.HardwareStats
import com.example.syskit.model.PrivilegeState
import com.example.syskit.model.SystemInfo
import java.util.Locale

/**
 * Neofetch/fastfetch-style readout with a bugdroid, generated locally.
 *
 * Alignment rules that keep it from scrambling:
 *  - every logo line is padded to exactly [LOGO_W] characters
 *  - every info line is truncated to exactly [INFO_W] characters
 *  - total line width stays under ~46 chars so phone terminals never wrap
 */
object FetchArt {

    private const val LOGO_W = 20
    private const val INFO_W = 24

    // Refined bugdroid: body, shading, antennae, feet — every line exactly 20 chars.
    private val bugdroid = listOf(
        "    -.          -.  ",
        "     +hydNNNNdyh+   ",
        "   +mMMMMMMMMMMMMm+ ",
        "  `dMMm:NMMMMNMmMd` ",
        "  hMMMMMMMMMMMMMMMh ",
        " .MMMMMMMMMMMMMMMMM.",
        " MMMMM  MMMMM  MMMMM",
        " MMMMM  MMMMM  MMMMM",
        " `MMMMMMMMMMMMMMMM' ",
        "  sMMMMMMMMMMMMMMs  ",
        "   `-+yMMMMMMy+-`   ",
        "       .+ss+.       "
    )

    fun generate(
        info: SystemInfo,
        stats: HardwareStats,
        deep: DeepDeviceInfo,
        priv: PrivilegeState
    ): String {
        val ramUsed = stats.ramUsedBytes / (1024.0 * 1024.0 * 1024.0)
        val ramTotal = stats.ramTotalBytes / (1024.0 * 1024.0 * 1024.0)
        val device = "${info.manufacturer} ${info.deviceModel}".trim()
        val soc = if (info.socName.isNotBlank()) "${info.socName} (${info.socRaw})"
                  else info.socRaw.ifBlank { "unknown" }
        val rootLine = when {
            priv.rootGranted -> "Root: yes (${priv.rootManager.displayName})"
            priv.rootAvailable -> "Root: yes (no access)"
            else -> "Root: no"
        }

        val infoLines = listOf(
            fit(device.ifBlank { "Android" }, INFO_W),
            "------------------------".take(INFO_W),
            fit("OS: Android ${info.androidVersion}", INFO_W),
            fit("API: ${info.apiLevel}", INFO_W),
            fit("Patch: ${info.securityPatch}", INFO_W),
            fit("SoC: $soc", INFO_W),
            fit("Codename: ${info.codename}", INFO_W),
            fit("Cores: ${info.coreCount} ${info.cpuArch}", INFO_W),
            fit("Kernel: ${info.kernelVersion.substringBefore("-android")}", INFO_W),
            fit("Up: ${info.uptimeFormatted}", INFO_W),
            fit("RAM: " + String.format(Locale.US, "%.1f/%.0f GiB", ramUsed, ramTotal), INFO_W),
            fit("Bat: ${stats.batteryPercent}% ${"%.0f".format(Locale.US, stats.batteryTempCelsius)}°C${if (stats.isCharging) "⚡" else ""}", INFO_W),
            fit("Display: ${deep.refreshRateHz.ifBlank { "?" }}", INFO_W),
            fit("SELinux: ${info.selinuxStatus.ifBlank { "?" }}", INFO_W),
            fit("Boot: ${deep.verifiedBootState.substringBefore(' ').lowercase()}", INFO_W),
            fit(rootLine, INFO_W) + if (priv.shizukuGranted) " •Shizuku" else ""
        )

        return buildString {
            val rows = maxOf(bugdroid.size, infoLines.size)
            for (i in 0 until rows) {
                append(bugdroid.getOrElse(i) { " ".repeat(LOGO_W) }.take(LOGO_W).padEnd(LOGO_W))
                append("  ")
                append(infoLines.getOrElse(i) { "" })
                if (i < rows - 1) append('\n')
            }
        }
    }

    private fun fit(s: String, n: Int): String =
        if (s.length <= n) s else s.take(n - 1) + "…"
}
