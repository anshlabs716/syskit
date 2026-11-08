package com.example.syskit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syskit.model.ExecutionMode
import com.example.syskit.model.PlayIntegrityResult
import com.example.syskit.model.PrivilegeState
import com.example.syskit.theme.BrightCyan
import com.example.syskit.theme.DangerRed
import com.example.syskit.theme.NeonGreen
import com.example.syskit.theme.WarningAmber

/**
 * One place for everything privilege- and integrity-related:
 * root checker, Play Integrity verdicts, device posture, privilege setup,
 * and the system actions that need elevated access.
 */
@Composable
fun IntegrityScreen(
    privilegeState: PrivilegeState,
    deepInfo: com.example.syskit.model.DeepDeviceInfo,
    playIntegrity: PlayIntegrityResult,
    integrityChecking: Boolean,
    onRequestRoot: () -> Unit,
    onRequestShizuku: () -> Unit,
    onRefresh: () -> Unit,
    onSetMode: (ExecutionMode) -> Unit,
    onCheckPlayIntegrity: () -> Unit,
    onReboot: (String) -> Unit,
    onTrimCaches: () -> Unit,
    onSetSELinux: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // ---- Hero verdict ----
        val rooted = privilegeState.rootGranted
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (rooted) NeonGreen.copy(alpha = 0.10f)
                else MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (rooted) Icons.Default.Verified else Icons.Default.Security,
                        contentDescription = null,
                        tint = if (rooted) NeonGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (rooted) "Device is rooted" else "Device is not rooted",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = privilegeState.rootManager.displayName +
                                if (privilegeState.shizukuGranted) " • Shizuku active" else "",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ---- Play Integrity ----
        SectionCard(title = "Google Play Integrity", subtitle = "What Google thinks of this device") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VerdictChip("Basic", playIntegrity.basicIntegrity, Modifier.weight(1f))
                VerdictChip("Device", playIntegrity.deviceIntegrity, Modifier.weight(1f))
                VerdictChip("Strong", playIntegrity.strongIntegrity, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            when {
                integrityChecking -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Asking Google…", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                playIntegrity.checked && playIntegrity.success -> {
                    Text(
                        text = "App recognition: ${playIntegrity.appRecognitionVerdict.replace('_', ' ').lowercase()}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "License: ${playIntegrity.licensingVerdict.replace('_', ' ').lowercase()}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                playIntegrity.checked -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ErrorOutline, null, tint = WarningAmber, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = playIntegrity.errorMessage,
                            fontSize = 12.sp,
                            color = WarningAmber
                        )
                    }
                }
                else -> {
                    Text(
                        text = "Shows whether this device passes Google's Basic / Device / Strong integrity checks. Needs Google Play Services (or microG with DroidGuard).",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCheckPlayIntegrity,
                enabled = !integrityChecking,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (playIntegrity.checked) "Check again" else "Check Play Integrity")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ---- Device posture ----
        SectionCard(title = "Device integrity", subtitle = "Local security checks — no Google involved") {
            PostureRow("Verified boot", deepInfo.verifiedBootState, deepInfo.verifiedBootState.startsWith("Green"))
            PostureRow("dm-verity", deepInfo.verityMode, deepInfo.verityMode.contains("enforcing", true))
            PostureRow("SELinux mode", privilegeState.selinuxMode.ifBlank { "Unknown" },
                ok = privilegeState.selinuxMode.equals("Enforcing", true))
            PostureRow("Emulator", if (deepInfo.isEmulator) "Yes" else "No", !deepInfo.isEmulator)
            PostureRow("Debuggable build", if (deepInfo.isDebuggable) "Yes" else "No", !deepInfo.isDebuggable)
            PostureRow("Thermal", deepInfo.thermalStatus, null)
            PostureRow("su binary", privilegeState.suBinaryPath.ifBlank { "not found" },
                ok = privilegeState.suBinaryPath.isNotBlank())
            PostureRow("Shizuku", when {
                privilegeState.shizukuGranted -> "Authorized"
                privilegeState.shizukuRunning -> "Running, not authorized"
                privilegeState.shizukuInstalled -> "Installed, not running"
                else -> "Not installed"
            }, ok = privilegeState.shizukuGranted)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ---- Privilege setup ----
        SectionCard(title = "Privileges", subtitle = "Unlock the debloater and system actions") {
            ActionButton(
                text = if (privilegeState.rootGranted) "Root granted ✓ (tap to re-verify)"
                else if (privilegeState.rootAvailable) "Grant root in ${privilegeState.rootManager.displayName}"
                else "Root — not available on this device",
                enabled = privilegeState.rootAvailable,
                container = if (privilegeState.rootGranted) MaterialTheme.colorScheme.surfaceVariant else NeonGreen.copy(alpha = 0.25f),
                content = if (privilegeState.rootGranted) NeonGreen else NeonGreen,
                onClick = onRequestRoot
            )
            Spacer(modifier = Modifier.height(8.dp))
            ActionButton(
                text = when {
                    privilegeState.shizukuGranted -> "Shizuku authorized ✓ (tap to re-check)"
                    privilegeState.shizukuRunning -> "Shizuku running — tap to authorize"
                    else -> "Set up Shizuku (no root needed)"
                },
                enabled = true,
                container = if (privilegeState.shizukuGranted) MaterialTheme.colorScheme.surfaceVariant else BrightCyan.copy(alpha = 0.2f),
                content = BrightCyan,
                onClick = onRequestShizuku
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Execution backend",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(ExecutionMode.AUTO, ExecutionMode.ROOT, ExecutionMode.SHIZUKU).forEach { mode ->
                    val selected = privilegeState.activeMode == mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onSetMode(mode) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.displayName.substringBefore(" ("),
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ---- System actions ----
        SectionCard(title = "System actions", subtitle = "These really do what they say") {
            ActionButton(
                text = "Trim cached files (frees storage)",
                enabled = true,
                container = MaterialTheme.colorScheme.surfaceVariant,
                content = MaterialTheme.colorScheme.onSurface,
                onClick = onTrimCaches
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "SELinux — current: ${privilegeState.selinuxMode.ifBlank { "unknown" }}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionButton(
                    text = "Permissive",
                    enabled = true,
                    container = WarningAmber.copy(alpha = 0.18f),
                    content = WarningAmber,
                    onClick = { onSetSELinux(true) },
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Enforcing",
                    enabled = true,
                    container = NeonGreen.copy(alpha = 0.18f),
                    content = NeonGreen,
                    onClick = { onSetSELinux(false) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionButton(
                    text = "Reboot",
                    enabled = true,
                    container = MaterialTheme.colorScheme.surfaceVariant,
                    content = MaterialTheme.colorScheme.onSurface,
                    onClick = { onReboot("normal") },
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Recovery",
                    enabled = true,
                    container = MaterialTheme.colorScheme.surfaceVariant,
                    content = MaterialTheme.colorScheme.onSurface,
                    onClick = { onReboot("recovery") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun VerdictChip(label: String, passed: Boolean, modifier: Modifier = Modifier) {
    val color = if (passed) NeonGreen else DangerRed
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = if (passed) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = color)
    }
}

@Composable
private fun PostureRow(label: String, value: String, ok: Boolean?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = when (ok) {
                true -> NeonGreen
                false -> WarningAmber
                null -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@Composable
private fun SectionCard(
    title: String,
    subtitle: String,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    enabled: Boolean,
    container: androidx.compose.ui.graphics.Color,
    content: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = container.copy(alpha = 0.4f),
            disabledContentColor = content.copy(alpha = 0.5f)
        )
    ) {
        Text(text = text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}
