package com.example.syskit.data

import android.app.ActivityManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.PowerManager
import android.os.Build
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Environment
import android.os.StatFs
import android.os.SystemClock
import com.example.syskit.model.HardwareStats
import com.example.syskit.model.NetworkDetails
import com.example.syskit.model.PackageItem
import com.example.syskit.model.SystemInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface
import java.util.concurrent.TimeUnit

class SystemDataProvider(private val context: Context) {

    suspend fun getSystemInfo(): SystemInfo = withContext(Dispatchers.IO) {
        val socRaw = getSocRaw()
        val socName = socMarketingName(socRaw)
        val cpuArch = System.getProperty("os.arch") ?: Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"
        val coreCount = Runtime.getRuntime().availableProcessors()
        val kernel = getKernelVersion()
        val uptime = formatUptime(SystemClock.elapsedRealtime())

        SystemInfo(
            deviceModel = Build.MODEL,
            manufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() },
            codename = Build.DEVICE,
            hardware = Build.HARDWARE,
            androidVersion = Build.VERSION.RELEASE,
            apiLevel = Build.VERSION.SDK_INT,
            securityPatch = Build.VERSION.SECURITY_PATCH ?: "N/A",
            buildNumber = Build.DISPLAY,
            bootloader = Build.BOOTLOADER,
            fingerprint = Build.FINGERPRINT,
            socName = socName,
            socRaw = socRaw,
            platform = getProp("ro.board.platform"),
            kernelVersion = kernel,
            uptimeFormatted = uptime,
            cpuArch = cpuArch,
            coreCount = coreCount,
            selinuxStatus = getSELinuxStatus()
        )
    }

    suspend fun getHardwareStats(): HardwareStats = withContext(Dispatchers.IO) {
        // Battery
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, ifilter)

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = if (level >= 0 && scale > 0) ((level / scale.toFloat()) * 100).toInt() else 0

        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        val healthCode = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: -1
        val healthStr = when (healthCode) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Failure"
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            else -> "Normal"
        }

        val tempTenths = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        val tempCelsius = tempTenths / 10.0f
        val voltageMv = batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) ?: 0
        val technology = batteryStatus?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Li-ion"

        // RAM
        val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager?.getMemoryInfo(memInfo)
        val ramTotal = memInfo.totalMem
        val ramAvail = memInfo.availMem
        val ramUsed = (ramTotal - ramAvail).coerceAtLeast(0L)

        // Storage
        val dataPath = Environment.getDataDirectory()
        val stat = StatFs(dataPath.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong
        val storageTotal = totalBlocks * blockSize
        val storageFree = availableBlocks * blockSize
        val storageUsed = (storageTotal - storageFree).coerceAtLeast(0L)

        HardwareStats(
            batteryPercent = batteryPct,
            isCharging = isCharging,
            batteryHealth = healthStr,
            batteryTempCelsius = tempCelsius,
            batteryVoltageMv = voltageMv,
            batteryTechnology = technology,
            ramTotalBytes = ramTotal,
            ramUsedBytes = ramUsed,
            ramAvailBytes = ramAvail,
            storageTotalBytes = storageTotal,
            storageUsedBytes = storageUsed,
            storageFreeBytes = storageFree
        )
    }

    suspend fun getInstalledPackages(): List<PackageItem> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        // MATCH_UNINSTALLED_PACKAGES also returns packages that were removed for
        // this user with `pm uninstall --user 0` so they can be restored.
        val flags = PackageManager.GET_META_DATA or PackageManager.MATCH_UNINSTALLED_PACKAGES
        val disabledByUser = PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER

        pm.getInstalledApplications(flags)
            .filter { appInfo ->
                // Keep normal packages plus anything removed only for this user.
                appInfo.sourceDir != null || runCatching {
                    pm.getApplicationEnabledSetting(appInfo.packageName) == disabledByUser
                }.getOrDefault(false)
            }
            .map { appInfo ->
                val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                val isEnabled = appInfo.enabled
                val debloated = runCatching {
                    pm.getApplicationEnabledSetting(appInfo.packageName) == disabledByUser
                }.getOrDefault(false)
                val label = runCatching { pm.getApplicationLabel(appInfo).toString() }.getOrDefault(appInfo.packageName)
                val ver = runCatching { pm.getPackageInfo(appInfo.packageName, 0).versionName ?: "1.0" }.getOrDefault("1.0")

                PackageItem(
                    packageName = appInfo.packageName,
                    appName = label,
                    versionName = ver,
                    isSystemApp = isSystem,
                    isEnabled = isEnabled,
                    targetSdkVersion = appInfo.targetSdkVersion,
                    isDebloated = debloated
                )
            }
            .sortedBy { it.appName.lowercase() }
    }

    suspend fun getNetworkDetails(): NetworkDetails = withContext(Dispatchers.IO) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetwork = cm?.activeNetwork
        val caps = cm?.getNetworkCapabilities(activeNetwork)
        val isConnected = caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        val netType = when {
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "Wi-Fi"
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "Cellular"
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> "Ethernet"
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true -> "VPN"
            isConnected -> "Connected"
            else -> "Disconnected"
        }

        var ipv4 = "N/A"
        var ipv6 = "N/A"

        runCatching {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val intf = interfaces.nextElement()
                if (intf.isLoopback || !intf.isUp) continue
                val addrs = intf.inetAddresses
                while (addrs.hasMoreElements()) {
                    val addr = addrs.nextElement()
                    if (!addr.isLoopbackAddress) {
                        if (addr is Inet4Address && ipv4 == "N/A") {
                            ipv4 = addr.hostAddress ?: "N/A"
                        } else if (addr is Inet6Address && ipv6 == "N/A") {
                            val full = addr.hostAddress ?: ""
                            ipv6 = full.substringBefore("%")
                        }
                    }
                }
            }
        }

        var wifiSsid = "N/A"
        var linkSpeed = "N/A"

        if (caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
            val wm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            val connectionInfo = wm?.connectionInfo
            if (connectionInfo != null) {
                val ssid = connectionInfo.ssid.replace("\"", "")
                wifiSsid = if (ssid != "<unknown ssid>") ssid else "Connected Wi-Fi"
                linkSpeed = "${connectionInfo.linkSpeed} Mbps"
            }
        }

        NetworkDetails(
            isConnected = isConnected,
            networkType = netType,
            ipv4Address = ipv4,
            ipv6Address = ipv6,
            wifiSsid = wifiSsid,
            wifiLinkSpeed = linkSpeed
        )
    }

    /** Marketing names for known SoC ids — looked up per device, never hardcoded specs. */
    private val socMarketing = mapOf(
        "gs101" to "Tensor", "gs201" to "Tensor G2", "zuma" to "Tensor G3", "zumapro" to "Tensor G4",
        "raviole" to "Tensor", "slider" to "Tensor G2",
        "sm8450" to "Snapdragon 8 Gen 1", "sm8475" to "Snapdragon 8+ Gen 1",
        "sm8550" to "Snapdragon 8 Gen 2", "sm8650" to "Snapdragon 8 Gen 3", "sm8750" to "Snapdragon 8 Elite",
        "sdm845" to "Snapdragon 845", "sm8250" to "Snapdragon 865", "sm7250" to "Snapdragon 765G",
        "sm6350" to "Snapdragon 695", "sm6375" to "Snapdragon 778G", "qcs6490" to "Snapdragon 7c+",
        "mt6893" to "Dimensity 1200", "mt6983" to "Dimensity 9000", "mt6985" to "Dimensity 9200",
        "mt6989" to "Dimensity 9300", "mt6835" to "Dimensity 700",
        "exynos2100" to "Exynos 2100", "s5e9925" to "Exynos 2200", "s5e9945" to "Exynos 2400",
        "kirin9000" to "Kirin 9000", "unisoc t618" to "Unisoc T618"
    )

    private fun getSocRaw(): String {
        if (Build.VERSION.SDK_INT >= 31) {
            val model = Build.SOC_MODEL
            if (!model.isNullOrBlank() && model != Build.UNKNOWN) return model
        }
        return getProp("ro.soc.model").ifEmpty {
            getProp("ro.board.platform").ifEmpty { Build.HARDWARE }
        }
    }

    private fun socMarketingName(raw: String): String {
        if (raw.isBlank()) return ""
        val key = raw.lowercase()
        return socMarketing[key]
            ?: socMarketing.entries.firstOrNull { key.contains(it.key) }?.value
            ?: ""
    }

    private fun getCpuModel(): String {
        // Official SoC fields first (API 31+): e.g. "Google gs101", not the board name.
        runCatching {
            if (Build.VERSION.SDK_INT >= 31) {
                val model = Build.SOC_MODEL
                val mfg = Build.SOC_MANUFACTURER
                if (!model.isNullOrBlank() && model != Build.UNKNOWN) {
                    return if (!mfg.isNullOrBlank() && mfg != Build.UNKNOWN) "$mfg $model" else model
                }
            }
        }
        runCatching {
            val prop = Runtime.getRuntime().exec(arrayOf("getprop", "ro.soc.model"))
                .inputStream.bufferedReader().readText().trim()
            if (prop.isNotEmpty()) {
                val mfg = Runtime.getRuntime().exec(arrayOf("getprop", "ro.soc.manufacturer"))
                    .inputStream.bufferedReader().readText().trim()
                return if (mfg.isNotEmpty()) "$mfg $prop" else prop
            }
        }
        return runCatching {
            val cpuinfo = File("/proc/cpuinfo")
            if (cpuinfo.exists()) {
                BufferedReader(FileReader(cpuinfo)).use { br ->
                    var line: String?
                    while (br.readLine().also { line = it } != null) {
                        if (line!!.startsWith("Hardware", ignoreCase = true) || line!!.startsWith("model name", ignoreCase = true)) {
                            val parts = line!!.split(":")
                            if (parts.size > 1) return parts[1].trim()
                        }
                    }
                }
            }
            val platform = Runtime.getRuntime().exec(arrayOf("getprop", "ro.board.platform"))
                .inputStream.bufferedReader().readText().trim()
            platform.ifEmpty { Build.HARDWARE }
        }.getOrDefault(Build.HARDWARE)
    }

    private fun getKernelVersion(): String {
        return runCatching {
            val p = Runtime.getRuntime().exec(arrayOf("uname", "-r"))
            val res = BufferedReader(InputStreamReader(p.inputStream)).readLine()
            p.waitFor(1, TimeUnit.SECONDS)
            p.destroy()
            res?.trim() ?: System.getProperty("os.version") ?: "Unknown"
        }.getOrDefault(System.getProperty("os.version") ?: "Unknown")
    }

    fun getSELinuxStatusPublic(): String = getSELinuxStatus()

    private fun getSELinuxStatus(): String {
        // The kernel flag is world-readable and doesn't depend on getenforce
        // being resolvable from the app process.
        runCatching {
            when (File("/sys/fs/selinux/enforce").readText().trim()) {
                "1" -> return "Enforcing"
                "0" -> return "Permissive"
            }
        }
        return runCatching {
            val p = Runtime.getRuntime().exec("/system/bin/getenforce")
            val res = BufferedReader(InputStreamReader(p.inputStream)).readLine()
            p.waitFor(2, TimeUnit.SECONDS)
            p.destroy()
            res?.trim()?.ifEmpty { "Unknown" } ?: "Unknown"
        }.getOrDefault("Unknown")
    }

    private fun formatUptime(millis: Long): String {
        val seconds = millis / 1000
        val days = seconds / (24 * 3600)
        val hours = (seconds % (24 * 3600)) / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return if (days > 0) "${days}d ${hours}h ${minutes}m" else "${hours}h ${minutes}m ${secs}s"
    }

    // ---- DevCheck-style deep info ----

    suspend fun getDeepDeviceInfo(): com.example.syskit.model.DeepDeviceInfo = withContext(Dispatchers.IO) {
        val sensors = runCatching {
            val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            sm?.getSensorList(Sensor.TYPE_ALL)?.map {
                com.example.syskit.model.SensorItem(
                    name = it.name,
                    vendor = it.vendor,
                    typeLabel = sensorTypeName(it.type),
                    powerMA = it.power
                )
            } ?: emptyList()
        }.getOrDefault(emptyList())

        val cameras = runCatching {
            val cm = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
            cm?.cameraIdList?.mapNotNull { id ->
                runCatching {
                    val chars = cm.getCameraCharacteristics(id)
                    val facing = when (chars.get(CameraCharacteristics.LENS_FACING)) {
                        CameraCharacteristics.LENS_FACING_BACK -> "Back"
                        CameraCharacteristics.LENS_FACING_FRONT -> "Front"
                        CameraCharacteristics.LENS_FACING_EXTERNAL -> "External"
                        else -> "Unknown"
                    }
                    val res = chars.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)
                    val apertures = chars.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES)
                        ?.joinToString("/") { String.format(java.util.Locale.US, "%.1f", it) } ?: "N/A"
                    com.example.syskit.model.CameraItem(
                        id = id,
                        facing = facing,
                        maxResolution = res?.let { "${it.width.toInt()} x ${it.height.toInt()}" } ?: "N/A",
                        apertures = apertures
                    )
                }.getOrNull()
            }?.filterNotNull() ?: emptyList()
        }.getOrDefault(emptyList())

        val coreFreqs = readCpuCoreFreqsMHz()

        val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val thermal = if (Build.VERSION.SDK_INT >= 30) {
            when (pm?.currentThermalStatus) {
                PowerManager.THERMAL_STATUS_NONE -> "None (cool)"
                PowerManager.THERMAL_STATUS_LIGHT -> "Light"
                PowerManager.THERMAL_STATUS_MODERATE -> "Moderate"
                PowerManager.THERMAL_STATUS_SEVERE -> "Severe"
                PowerManager.THERMAL_STATUS_CRITICAL -> "Critical"
                PowerManager.THERMAL_STATUS_EMERGENCY -> "Emergency"
                PowerManager.THERMAL_STATUS_SHUTDOWN -> "Shutdown imminent"
                else -> "Unknown"
            }
        } else "Needs Android 11+"

        val verifiedBoot = getProp("ro.boot.verifiedbootstate").let {
            when (it) {
                "green" -> "Green (locked, certified)"
                "yellow" -> "Yellow (locked, custom OS)"
                "orange" -> "Orange (bootloader unlocked)"
                else -> it.ifEmpty { "Unknown" }
            }
        }
        val verity = getProp("ro.boot.veritymode").ifEmpty { "Unknown" }
        val display = runCatching {
            val dm = context.getSystemService(android.hardware.display.DisplayManager::class.java)
            val d = dm?.getDisplay(android.view.Display.DEFAULT_DISPLAY)
            val mode = d?.supportedModes?.maxByOrNull { it.refreshRate }
            Triple(
                mode?.let { "${it.physicalWidth} x ${it.physicalHeight}" } ?: "",
                mode?.refreshRate?.let { String.format(java.util.Locale.US, "%.0f Hz", it) } ?: "",
                d?.hdrCapabilities?.supportedHdrTypes?.isNotEmpty() == true
            )
        }.getOrDefault(Triple("", "", false))
        val glEs = runCatching {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            am?.deviceConfigurationInfo?.glEsVersion ?: ""
        }.getOrDefault("")

        val emulator = getProp("ro.kernel.qemu") == "1" ||
            android.os.Build.FINGERPRINT.contains("generic", true) ||
            android.os.Build.MODEL.contains("Emulator", true) ||
            android.os.Build.MODEL.contains("sdk_gphone", true)

        com.example.syskit.model.DeepDeviceInfo(
            sensors = sensors,
            cameras = cameras,
            cpuCoreFreqsMHz = coreFreqs,
            thermalStatus = thermal,
            verifiedBootState = verifiedBoot,
            verityMode = verity,
            isEmulator = emulator,
            isDebuggable = (context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0,
            displayResolution = display.first,
            refreshRateHz = display.second,
            glEsVersion = glEs
        )
    }

    fun readCpuCoreFreqsMHz(): List<Int> {
        val freqs = mutableListOf<Int>()
        var core = 0
        while (core < 16) {
            val f = runCatching {
                java.io.File("/sys/devices/system/cpu/cpu$core/cpufreq/scaling_cur_freq")
                    .readText().trim().toLong() / 1000
            }.getOrNull() ?: break
            freqs.add(f.toInt())
            core++
        }
        return freqs
    }

    private fun getProp(key: String): String = runCatching {
        Runtime.getRuntime().exec(arrayOf("getprop", key)).inputStream
            .bufferedReader().readText().trim()
    }.getOrDefault("")

    private fun sensorTypeName(type: Int): String = when (type) {
        Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
        Sensor.TYPE_GYROSCOPE -> "Gyroscope"
        Sensor.TYPE_MAGNETIC_FIELD -> "Magnetometer"
        Sensor.TYPE_LIGHT -> "Light"
        Sensor.TYPE_PROXIMITY -> "Proximity"
        Sensor.TYPE_PRESSURE -> "Barometer"
        Sensor.TYPE_AMBIENT_TEMPERATURE -> "Ambient temp"
        Sensor.TYPE_RELATIVE_HUMIDITY -> "Humidity"
        Sensor.TYPE_STEP_COUNTER -> "Step counter"
        Sensor.TYPE_STEP_DETECTOR -> "Step detector"
        Sensor.TYPE_SIGNIFICANT_MOTION -> "Significant motion"
        Sensor.TYPE_GRAVITY -> "Gravity"
        Sensor.TYPE_LINEAR_ACCELERATION -> "Linear accel"
        Sensor.TYPE_ROTATION_VECTOR -> "Rotation vector"
        Sensor.TYPE_ORIENTATION -> "Orientation"
        Sensor.TYPE_HEART_RATE -> "Heart rate"
        else -> "Type $type"
    }
}
