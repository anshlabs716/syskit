package com.example.syskit.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = app.packageName,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Version: ${app.versionName} • Target SDK: ${app.targetSdkVersion}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                    Text(
                        text = when {
                            app.isDebloated -> "Status: Removed for this user (restorable)"
                            app.isEnabled -> "Status: Active ${if (app.isSystemApp) "• System App" else "• User App"}"
                            else -> "Status: Frozen / Disabled ${if (app.isSystemApp) "• System App" else "• User App"}"
                        },
                        fontFamily = FontFamily.Monospace,
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
                            Text("Restore (install-existing)", fontFamily = FontFamily.Monospace)
                        }
                    } else {
                        if (app.isSystemApp) {
                            Text(
                                text = "⚠ System app — removing it can break features. It stays restorable via install-existing.",
                                fontFamily = FontFamily.Monospace,
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
                            Text("Uninstall for user 0 (debloat)", fontFamily = FontFamily.Monospace)
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
                            Text("Freeze / Disable (pm disable)", fontFamily = FontFamily.Monospace)
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
                            Text("Unfreeze / Enable (pm enable)", fontFamily = FontFamily.Monospace)
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
                        Text("Force Stop (am force-stop)", fontFamily = FontFamily.Monospace)
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
                        Text("Clear Data & Cache (pm clear)", fontFamily = FontFamily.Monospace)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { selectedPackage = null }) {
                    Text("Close", fontFamily = FontFamily.Monospace)
                }
            },
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
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "App Ops, Debloater, and Process Freeze",
            fontFamily = FontFamily.Monospace,
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
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = WarningAmber
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Freeze / uninstall / restore run through Shizuku (ADB) or Root. Browse and search work without them.",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onOpenPrivileges,
                        colors = ButtonDefaults.buttonColors(containerColor = WarningAmber.copy(alpha = 0.25f), contentColor = WarningAmber)
                    ) {
                        Text("Open Privileges", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
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
                    fontFamily = FontFamily.Monospace,
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

        // Filter pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL", "USER", "SYSTEM", "DISABLED", "REMOVED").forEach { filter ->
                val selected = selectedFilter == filter
                FilterChip(
                    selected = selected,
                    onClick = { onFilterChange(filter) },
                    label = {
                        Text(
                            text = filter,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
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
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Package list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(filtered, key = { it.packageName }) { app ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .clickable { selectedPackage = app },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(8.dp)
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
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            bitmap = appIconBitmap
                                ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = app.appName,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                if (app.isDebloated) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(DangerRed.copy(alpha = 0.2f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "REMOVED",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DangerRed
                                        )
                                    }
                                }
                                if (!app.isEnabled) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(WarningAmber.copy(alpha = 0.2f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "FROZEN",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = WarningAmber
                                        )
                                    }
                                }
                            }
                            Text(
                                text = app.packageName,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (app.isSystemApp) DangerRed.copy(alpha = 0.15f) else BrightCyan.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (app.isSystemApp) "SYS" else "USER",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (app.isSystemApp) DangerRed else BrightCyan
                            )
                        }
                    }
                }
            }
        }
    }
}
