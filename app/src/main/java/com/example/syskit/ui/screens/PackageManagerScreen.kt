package com.example.syskit.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.syskit.model.PackageItem
import com.example.syskit.theme.BrightCyan
import com.example.syskit.theme.DangerRed
import com.example.syskit.theme.NeonGreen
import com.example.syskit.theme.WarningAmber

/** App icons come back as Drawables; adaptive icons draw fine onto a bitmap. */
private fun Drawable.toImageBitmap(): ImageBitmap {
    if (this is BitmapDrawable && bitmap != null) return bitmap.asImageBitmap()
    val w = intrinsicWidth.takeIf { it > 0 } ?: 48
    val h = intrinsicHeight.takeIf { it > 0 } ?: 48
    val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    setBounds(0, 0, w, h)
    draw(canvas)
    return bmp.asImageBitmap()
}

@Composable
fun PackageManagerScreen(
    packages: List<PackageItem>,
    searchQuery: String,
    selectedFilter: String,
    privilegeReady: Boolean,
    onSearchChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onOpenPrivileges: () -> Unit,
    onFreezePackage: (String) -> Unit,
    onUnfreezePackage: (String) -> Unit,
    onForceStopPackage: (String) -> Unit,
    onClearPackageData: (String) -> Unit,
    onUninstallPackage: (String) -> Unit,
    onRestorePackage: (String) -> Unit
) {
    var selectedPackage by remember { mutableStateOf<PackageItem?>(null) }

    val filtered = remember(packages, searchQuery, selectedFilter) {
        packages.filter { item ->
            val matchesSearch = item.appName.contains(searchQuery, ignoreCase = true) ||
                    item.packageName.contains(searchQuery, ignoreCase = true)
            val matchesFilter = when (selectedFilter) {
                "USER" -> !item.isSystemApp
                "SYSTEM" -> item.isSystemApp
                "DISABLED" -> !item.isEnabled
                "REMOVED" -> item.isDebloated
                "RECOMMENDED" -> item.bloatLevel == "RECOMMENDED"
                "UNSAFE" -> item.bloatLevel == "UNSAFE"
                else -> true
            }
            matchesSearch && matchesFilter
        }
    }

    if (selectedPackage != null) {
        val app = selectedPackage!!
        AlertDialog(
            onDismissRequest = { selectedPackage = null },
            title = {
                Column {
                    Text(
                        text = app.appName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = app.packageName,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Version: ${app.versionName} • Target SDK: ${app.targetSdkVersion}",
                        fontSize = 11.sp
                    )
                    Text(
                        text = when {
                            app.isDebloated -> "Status: Removed for this user (restorable)"
                            app.isEnabled -> "Status: Active ${if (app.isSystemApp) "• System App" else "• User App"}"
                            else -> "Status: Frozen / Disabled ${if (app.isSystemApp) "• System App" else "• User App"}"
                        },
                        fontSize = 11.sp,
                        color = when {
                            app.isDebloated -> DangerRed
                            app.isEnabled -> NeonGreen
                            else -> WarningAmber
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (app.isDebloated) {
                        Button(
                            onClick = {
                                onRestorePackage(app.packageName)
                                selectedPackage = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen.copy(alpha = 0.25f), contentColor = NeonGreen)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Restore (install-existing)")
                        }
                    } else {
                        if (app.isSystemApp) {
                            Text(
                                text = "⚠ System app — removing it can break features. It stays restorable via install-existing.",
                                fontSize = 10.sp,
                                color = WarningAmber
                            )
                        }
                        Button(
                            onClick = {
                                onUninstallPackage(app.packageName)
                                selectedPackage = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.25f), contentColor = DangerRed)
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Uninstall for user 0 (debloat)")
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (app.isEnabled) {
                        Button(
                            onClick = {
                                onFreezePackage(app.packageName)
                                selectedPackage = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = WarningAmber.copy(alpha = 0.25f), contentColor = WarningAmber)
                        ) {
                            Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Freeze / Disable (pm disable)")
                        }
                    } else {
                        Button(
                            onClick = {
                                onUnfreezePackage(app.packageName)
                                selectedPackage = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen.copy(alpha = 0.25f), contentColor = NeonGreen)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Unfreeze / Enable (pm enable)")
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            onForceStopPackage(app.packageName)
                            selectedPackage = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Force Stop (am force-stop)")
                    }

                    Button(
                        onClick = {
                            onClearPackageData(app.packageName)
                            selectedPackage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.2f), contentColor = DangerRed)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear Data & Cache (pm clear)")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { selectedPackage = null }) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("package_manager_screen")
    ) {
        Text(
            text = "PACKAGE MANAGER",
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "App Ops, Debloater, and Process Freeze",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (!privilegeReady) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WarningAmber.copy(alpha = 0.12f)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "⚠ Debloating needs Shizuku or Root",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = WarningAmber
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Freeze / uninstall / restore run through Shizuku (ADB) or Root. Browse and search work without them.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onOpenPrivileges,
                        colors = ButtonDefaults.buttonColors(containerColor = WarningAmber.copy(alpha = 0.25f), contentColor = WarningAmber)
                    ) {
                        Text("Open Privileges", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("package_search_input"),
            placeholder = {
                Text(
                    text = "Search package or app name...",
                    fontSize = 12.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter pills (scrollable so they never squeeze)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL", "USER", "SYSTEM", "DISABLED", "REMOVED", "RECOMMENDED", "UNSAFE").forEach { filter ->
                val selected = selectedFilter == filter
                FilterChip(
                    selected = selected,
                    onClick = { onFilterChange(filter) },
                    label = {
                        Text(
                            text = filter.lowercase(),
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonGreen.copy(alpha = 0.2f),
                        selectedLabelColor = NeonGreen
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${filtered.size} packages found (tap to manage)",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Package list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered, key = { it.packageName }) { app ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPackage = app },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    val pm = LocalContext.current.packageManager
                    val appIconBitmap = remember(app.packageName) {
                        val drawable = runCatching { pm.getApplicationIcon(app.packageName) }
                            .getOrElse { runCatching { pm.defaultActivityIcon }.getOrNull() }
                        drawable?.toImageBitmap()
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            bitmap = appIconBitmap
                                ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = app.appName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = app.packageName,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "v${app.versionName} • SDK ${app.targetSdkVersion}" +
                                    if (app.vendor.isNotBlank()) " • ${app.vendor}" else "",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (app.isDebloated || !app.isEnabled) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    if (app.isDebloated) MiniBadge("REMOVED", DangerRed)
                                    if (!app.isEnabled) MiniBadge("FROZEN", WarningAmber)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        BloatBadge(app.bloatLevel)
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniBadge(text: String, color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(text = text, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

/** Canta-style recommendation badge: Recommended / Advanced / Expert / Keep / Unclassified. */
@Composable
private fun BloatBadge(level: String) {
    val (color, label) = when (level) {
        "RECOMMENDED" -> NeonGreen to "Recommended"
        "ADVANCED" -> WarningAmber to "Advanced"
        "EXPERT" -> androidx.compose.ui.graphics.Color(0xFFFF9E64) to "Expert"
        "UNSAFE" -> DangerRed to "Keep"
        else -> MaterialTheme.colorScheme.onSurfaceVariant to "Unclassified"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 9.dp, vertical = 5.dp)
    ) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = color)
    }
}
