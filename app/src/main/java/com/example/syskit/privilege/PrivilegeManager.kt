package com.example.syskit.privilege

import android.content.Context
import android.content.pm.PackageManager
import com.example.syskit.model.ExecutionMode
import com.example.syskit.model.PrivilegeState
import com.example.syskit.model.RootManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

class PrivilegeManager(private val context: Context) {

    companion object {
        const val SHIZUKU_REQUEST_CODE = 9001
        private val SU_PATHS = listOf(
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/data/local/su",
            "/data/adb/ksu/bin/su",
            "/data/adb/ap/bin/su"
        )
    }

    suspend fun inspectPrivileges(): PrivilegeState = withContext(Dispatchers.IO) {
        val suPath = findSuBinary()
        val rootAvail = suPath.isNotBlank()
        val rootMgr = if (rootAvail) detectRootManager() else RootManager.NONE
        val rootGranted = if (rootAvail) verifyRootAccess() else false

        val shizukuInstalled = isPackageInstalled("moe.shizuku.privileged.api")
        val shizukuRunning = checkShizukuRunning()
        val shizukuGranted = if (shizukuRunning) checkShizukuPermission() else false
        val shizukuVer = if (shizukuRunning) runCatching { Shizuku.getVersion() }.getOrDefault(0) else 0

        val selinux = getSELinuxStatus()

        PrivilegeState(
            rootAvailable = rootAvail,
            rootGranted = rootGranted,
            rootManager = rootMgr,
            suBinaryPath = suPath,
            shizukuInstalled = shizukuInstalled,
            shizukuRunning = shizukuRunning,
            shizukuGranted = shizukuGranted,
            shizukuVersion = shizukuVer,
            selinuxMode = selinux
        )
    }

    private fun findSuBinary(): String {
        for (path in SU_PATHS) {
            runCatching {
                val f = File(path)
                if (f.exists()) return path
            }
        }
        val pathEnv = System.getenv("PATH") ?: ""
        for (dir in pathEnv.split(":")) {
            val su = File(dir, "su")
            if (su.exists()) return su.absolutePath
        }
        return ""
    }

    private fun detectRootManager(): RootManager {
        runCatching {
            val p = Runtime.getRuntime().exec(arrayOf("su", "-v"))
            val reader = BufferedReader(InputStreamReader(p.inputStream))
            val line = reader.readLine()?.lowercase() ?: ""
            p.waitFor(1, TimeUnit.SECONDS)
            p.destroy()
            when {
                line.contains("magisk") -> return RootManager.MAGISK
                line.contains("ksu") || line.contains("kernelsu") -> return RootManager.KERNEL_SU
                line.contains("apatch") -> return RootManager.APATCH
            }
        }

        if (File("/data/adb/ksu").exists() || File("/data/adb/ksud").exists()) return RootManager.KERNEL_SU
        if (File("/data/adb/magisk").exists()) return RootManager.MAGISK
        if (File("/data/adb/ap").exists()) return RootManager.APATCH

        return RootManager.GENERIC_SU
    }

    suspend fun requestRootAccess(): Boolean = withContext(Dispatchers.IO) {
        verifyRootAccess()
    }

    private fun verifyRootAccess(): Boolean {
        return runCatching {
            val process = ProcessBuilder("su", "-c", "id").start()
            val output = BufferedReader(InputStreamReader(process.inputStream)).readText()
            process.waitFor(3, TimeUnit.SECONDS)
            process.destroy()
            output.contains("uid=0(root)") || output.contains("uid=0")
        }.getOrDefault(false)
    }

    private fun checkShizukuRunning(): Boolean {
        return runCatching {
            Shizuku.pingBinder()
        }.getOrDefault(false)
    }

    private fun checkShizukuPermission(): Boolean {
        return runCatching {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        }.getOrDefault(false)
    }

    fun requestShizukuPermission(): Boolean {
        return runCatching {
            if (Shizuku.pingBinder()) {
                if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                    Shizuku.requestPermission(SHIZUKU_REQUEST_CODE)
                    true
                } else {
                    true
                }
            } else {
                false
            }
        }.getOrDefault(false)
    }

    private fun isPackageInstalled(pkg: String): Boolean {
        return runCatching {
            context.packageManager.getPackageInfo(pkg, 0)
            true
        }.getOrDefault(false)
    }

    private fun getSELinuxStatus(): String {
        return runCatching {
            val p = Runtime.getRuntime().exec(arrayOf("getenforce"))
            val line = BufferedReader(InputStreamReader(p.inputStream)).readLine() ?: "Unknown"
            p.waitFor(1, TimeUnit.SECONDS)
            p.destroy()
            line.trim()
        }.getOrDefault("Unknown")
    }

    data class ExecResult(
        val output: String,
        val exitCode: Int,
        val isError: Boolean,
        val modeUsed: String
    )

    suspend fun executeCommand(command: String, requestedMode: ExecutionMode): ExecResult = withContext(Dispatchers.IO) {
        val targetMode = when (requestedMode) {
            ExecutionMode.AUTO -> {
                if (verifyRootAccess()) ExecutionMode.ROOT
                else if (checkShizukuRunning() && checkShizukuPermission()) ExecutionMode.SHIZUKU
                else ExecutionMode.STANDARD
            }
            else -> requestedMode
        }

        when (targetMode) {
            ExecutionMode.ROOT -> executeRoot(command)
            ExecutionMode.SHIZUKU -> executeShizuku(command)
            else -> executeStandard(command)
        }
    }

    private fun executeRoot(command: String): ExecResult {
        return runCatching {
            val process = ProcessBuilder("su", "-c", command).start()
            val stdout = process.inputStream.bufferedReader().readText()
            val stderr = process.errorStream.bufferedReader().readText()
            val completed = process.waitFor(15, TimeUnit.SECONDS)
            val exitCode = if (completed) process.exitValue() else -1
            if (!completed) process.destroy()

            val combined = (stdout + if (stderr.isNotBlank()) "\n[stderr]: $stderr" else "").trim()
            ExecResult(
                output = if (combined.isEmpty()) "(Execution completed with code $exitCode)" else combined,
                exitCode = exitCode,
                isError = exitCode != 0,
                modeUsed = "Root (su)"
            )
        }.getOrElse { e ->
            ExecResult(
                output = "Root execution failed: ${e.localizedMessage}\nEnsure Root is granted in Magisk/KernelSU.",
                exitCode = -1,
                isError = true,
                modeUsed = "Root (su)"
            )
        }
    }

    private fun executeShizuku(command: String): ExecResult {
        return runCatching {
            if (!Shizuku.pingBinder()) {
                return@runCatching ExecResult(
                    output = "Shizuku service is not running. Please start Shizuku first.",
                    exitCode = -1,
                    isError = true,
                    modeUsed = "Shizuku"
                )
            }
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                return@runCatching ExecResult(
                    output = "Shizuku permission not granted. Please tap 'Pair / Authorize Shizuku' in Privilege Settings.",
                    exitCode = -1,
                    isError = true,
                    modeUsed = "Shizuku"
                )
            }

            val newProcessMethod: Method = Shizuku::class.java.getDeclaredMethod(
                "newProcess",
                Array<String>::class.java,
                Array<String>::class.java,
                String::class.java
            )
            newProcessMethod.isAccessible = true
            val process = newProcessMethod.invoke(
                null,
                arrayOf("sh", "-c", command),
                null,
                null
            ) as Process

            val stdout = process.inputStream.bufferedReader().readText()
            val stderr = process.errorStream.bufferedReader().readText()
            val completed = process.waitFor(15, TimeUnit.SECONDS)
            val exitCode = if (completed) process.exitValue() else -1
            if (!completed) process.destroy()

            val combined = (stdout + if (stderr.isNotBlank()) "\n[stderr]: $stderr" else "").trim()
            ExecResult(
                output = if (combined.isEmpty()) "(Execution completed with code $exitCode)" else combined,
                exitCode = exitCode,
                isError = exitCode != 0,
                modeUsed = "Shizuku (ADB)"
            )
        }.getOrElse { e ->
            ExecResult(
                output = "Shizuku execution failed: ${e.localizedMessage}",
                exitCode = -1,
                isError = true,
                modeUsed = "Shizuku"
            )
        }
    }

    private fun executeStandard(command: String): ExecResult {
        return runCatching {
            val process = ProcessBuilder("sh", "-c", command).start()
            val stdout = process.inputStream.bufferedReader().readText()
            val stderr = process.errorStream.bufferedReader().readText()
            val completed = process.waitFor(10, TimeUnit.SECONDS)
            val exitCode = if (completed) process.exitValue() else -1
            if (!completed) process.destroy()

            val combined = (stdout + if (stderr.isNotBlank()) "\n[stderr]: $stderr" else "").trim()
            ExecResult(
                output = if (combined.isEmpty()) "(Execution completed with code $exitCode)" else combined,
                exitCode = exitCode,
                isError = exitCode != 0,
                modeUsed = "Standard Shell"
            )
        }.getOrElse { e ->
            ExecResult(
                output = "Execution failed: ${e.localizedMessage}",
                exitCode = -1,
                isError = true,
                modeUsed = "Standard Shell"
            )
        }
    }
}
