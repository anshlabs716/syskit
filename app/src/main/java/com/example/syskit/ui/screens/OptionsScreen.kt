package com.example.syskit.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syskit.theme.NeonGreen

/**
 * Options tab: appearance, console font, and about info. Everything persists.
 */
@Composable
fun OptionsScreen(
    themeMode: String,
    terminalFontSp: Int,
    accentMode: String,
    onThemeMode: (String) -> Unit,
    onTerminalFont: (Int) -> Unit,
    onAccentMode: (String) -> Unit
) {
    val context = LocalContext.current
    val version = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "?"
    }.getOrDefault("?")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Options",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Make SysKit yours",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---- Appearance ----
        OptionsCard(title = "Appearance") {
            Text(
                text = "App theme",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("system", "dark", "light").forEach { mode ->
                    val selected = themeMode == mode
                    Box2(
                        modifier = Modifier.weight(1f),
                        selected = selected,
                        onClick = { onThemeMode(mode) }
                    ) {
                        Text(
                            text = mode.replaceFirstChar { it.uppercase() },
                            fontSize = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ---- Accent ----
        OptionsCard(title = "Accent colour") {
            Text(
                text = "Dynamic uses your wallpaper's Material You palette (Android 12+).",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    "dynamic" to androidx.compose.ui.graphics.Color(0xFFB69DF8),
                    "cyan" to com.example.syskit.theme.AccentCyan,
                    "green" to com.example.syskit.theme.NeonGreen,
                    "amber" to com.example.syskit.theme.WarningAmber,
                    "pink" to androidx.compose.ui.graphics.Color(0xFFF48FB1)
                ).forEach { (mode, color) ->
                    val selected = accentMode == mode
                    androidx.compose.foundation.layout.Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAccentMode(mode) }
                                .padding(vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(if (selected) MaterialTheme.colorScheme.primary else color)
                                    .border(
                                        2.dp,
                                        if (selected) MaterialTheme.colorScheme.onSurface else color.copy(alpha = 0.4f),
                                        RoundedCornerShape(999.dp)
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = mode.replaceFirstChar { it.uppercase() },
                            fontSize = 9.sp,
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ---- Console ----
        OptionsCard(title = "Console") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Output font size",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${terminalFontSp}sp",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Slider(
                value = terminalFontSp.toFloat(),
                onValueChange = { onTerminalFont(it.toInt().coerceIn(9, 16)) },
                valueRange = 9f..16f,
                steps = 6
            )
            Text(
                text = "Applies to terminal output. Commands and package ids always stay monospace.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ---- About ----
        OptionsCard(title = "About") {
            AboutRow("Version", version)
            AboutRow("Implementation", "Kotlin + Jetpack Compose (native Android)")
            AboutRow("Privileges", "Shizuku / Root — both optional")
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        runCatching {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/anshlabs716/syskit"))
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("GitHub", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = {
                        runCatching {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/anshlabs716/syskit/releases"))
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Releases", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "SysKit • FOSS • GPL-3.0",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun OptionsCard(title: String, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun Box2(
    modifier: Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    content: @Composable androidx.compose.foundation.layout.BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun AboutRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
