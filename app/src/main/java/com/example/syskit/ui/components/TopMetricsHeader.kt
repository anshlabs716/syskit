package com.example.syskit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syskit.model.HardwareStats
import com.example.syskit.model.PrivilegeState
import com.example.syskit.theme.BrightCyan
import com.example.syskit.theme.DangerRed
import com.example.syskit.theme.NeonGreen
import com.example.syskit.theme.WarningAmber

/**
 * Compact top bar: brand + privilege chip on one line, three live metric chips
 * under it. No dead space, no heavy borders.
 */
@Composable
fun TopMetricsHeader(
    privilegeState: PrivilegeState,
    hardwareStats: HardwareStats,
    onPrivilegeClick: () -> Unit
) {
    val accent = when {
        privilegeState.rootGranted -> NeonGreen
        privilegeState.shizukuGranted -> BrightCyan
        else -> WarningAmber
    }
    val levelLabel = when {
        privilegeState.rootGranted -> "Root"
        privilegeState.shizukuGranted -> "Shizuku"
        else -> "No privileges"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SysKit",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Privilege chip — tap to manage
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(accent.copy(alpha = 0.14f))
                    .clickable { onPrivilegeClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(accent)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = levelLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = accent
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricChip(
                modifier = Modifier.weight(1f),
                label = "Battery",
                value = "${hardwareStats.batteryPercent}%",
                progress = (hardwareStats.batteryPercent / 100f).coerceIn(0f, 1f),
                valueColor = when {
                    hardwareStats.isCharging -> NeonGreen
                    hardwareStats.batteryPercent < 20 -> DangerRed
                    else -> MaterialTheme.colorScheme.onSurface
                },
                sub = "${hardwareStats.batteryTempCelsius}°C" + if (hardwareStats.isCharging) " • charging" else ""
            )
            MetricChip(
                modifier = Modifier.weight(1f),
                label = "RAM",
                value = if (hardwareStats.ramTotalBytes > 0)
                    "${hardwareStats.ramUsedBytes * 100 / hardwareStats.ramTotalBytes}%" else "—",
                progress = if (hardwareStats.ramTotalBytes > 0)
                    (hardwareStats.ramUsedBytes.toFloat() / hardwareStats.ramTotalBytes) else 0f,
                valueColor = MaterialTheme.colorScheme.onSurface,
                sub = formatGB(hardwareStats.ramUsedBytes) + " / " + formatGB(hardwareStats.ramTotalBytes)
            )
            MetricChip(
                modifier = Modifier.weight(1f),
                label = "Storage",
                value = if (hardwareStats.storageTotalBytes > 0)
                    "${hardwareStats.storageUsedBytes * 100 / hardwareStats.storageTotalBytes}%" else "—",
                progress = if (hardwareStats.storageTotalBytes > 0)
                    (hardwareStats.storageUsedBytes.toFloat() / hardwareStats.storageTotalBytes) else 0f,
                valueColor = MaterialTheme.colorScheme.onSurface,
                sub = formatGB(hardwareStats.storageFreeBytes) + " free"
            )
        }
    }
}

private fun formatGB(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val gb = bytes / (1024.0 * 1024.0 * 1024.0)
    return if (gb >= 1.0) String.format(java.util.Locale.US, "%.1f GB", gb)
    else String.format(java.util.Locale.US, "%.0f MB", bytes / (1024.0 * 1024.0))
}

@Composable
private fun MetricChip(
    modifier: Modifier,
    label: String,
    value: String,
    progress: Float,
    valueColor: androidx.compose.ui.graphics.Color,
    sub: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(5.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = valueColor.copy(alpha = 0.9f),
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            strokeCap = StrokeCap.Round
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = sub,
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}
