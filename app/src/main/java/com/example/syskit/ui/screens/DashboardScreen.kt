package com.example.syskit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syskit.model.HardwareStats
import com.example.syskit.model.SystemInfo
import com.example.syskit.theme.BrightCyan
import com.example.syskit.theme.ElectricBlue
import com.example.syskit.theme.NeonGreen
import com.example.syskit.theme.WarningAmber

@Composable
fun DashboardScreen(
    systemInfo: SystemInfo,
    hardwareStats: HardwareStats
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("dashboard_screen")
    ) {
        Text(
            text = "SYSTEM OVERVIEW",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Hardware diagnostics, kernel, and system telemetry",
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Device Specs Card
        InfoCard(
            title = "DEVICE IDENTIFICATION",
            icon = Icons.Default.PhoneAndroid,
            iconTint = NeonGreen
        ) {
            InfoRow("Model", "${systemInfo.manufacturer} ${systemInfo.deviceModel}")
            InfoRow("Board / Hardware", systemInfo.board)
            InfoRow("Android Version", "Android ${systemInfo.androidVersion} (API ${systemInfo.apiLevel})")
            InfoRow("Security Patch", systemInfo.securityPatch)
            InfoRow("Build", systemInfo.buildNumber)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CPU & Kernel Card
        InfoCard(
            title = "PROCESSOR & KERNEL",
            icon = Icons.Default.Memory,
            iconTint = BrightCyan
        ) {
            InfoRow("SoC / Processor", systemInfo.cpuModel)
            InfoRow("Architecture", systemInfo.cpuArch)
            InfoRow("Cores Active", "${systemInfo.coreCount} Cores")
            InfoRow("Kernel", systemInfo.kernelVersion)
            InfoRow("SELinux Mode", systemInfo.selinuxStatus)
            InfoRow("System Uptime", systemInfo.uptimeFormatted)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Battery Telemetry Card
        InfoCard(
            title = "BATTERY HEALTH & POWER",
            icon = Icons.Default.DeveloperBoard,
            iconTint = WarningAmber
        ) {
            InfoRow("Charge Level", "${hardwareStats.batteryPercent}% (${if (hardwareStats.isCharging) "Charging" else "Discharging"})")
            InfoRow("Health Status", hardwareStats.batteryHealth)
            InfoRow("Temperature", "${hardwareStats.batteryTempCelsius} °C")
            InfoRow("Voltage", "${hardwareStats.batteryVoltageMv} mV")
            InfoRow("Chemistry", hardwareStats.batteryTechnology)
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = if (value.isBlank()) "N/A" else value,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
