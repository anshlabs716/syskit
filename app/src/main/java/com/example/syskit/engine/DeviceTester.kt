package com.example.syskit.engine

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File

/**
 * DevCheck-style device tests, all local and honest:
 *  - multi-core CPU benchmark (floating-point ops per second)
 *  - memory bandwidth (write + read a fixed buffer)
 *  - storage write/read speed (app cache, cleaned up after)
 *  - sensor sampling (do the listed sensors actually deliver values?)
 */
object DeviceTester {

    suspend fun runAll(context: Context, report: (String) -> Unit) = withContext(Dispatchers.Default) {
        report("Starting device tests…")

        report(cpuTest())
        report(memTest())
        report(storageTest(context))
        report(sensorTest(context))

        report("All tests finished.")
    }

    private fun cpuTest(): String {
        val threads = Runtime.getRuntime().availableProcessors().coerceIn(1, 16)
        val durationMs = 1500L
        var totalOps = 0L
        val start = System.nanoTime()
        val end = start + durationMs * 1_000_000
        val workers = (0 until threads).map { t ->
            Thread {
                var ops = 0L
                var acc = 1.0000001
                while (System.nanoTime() < end) {
                    repeat(10_000) {
                        acc = acc * 1.0000001 + 0.0000001
                    }
                    ops += 10_000
                }
                synchronized(Unit) { totalOps += ops }
                // keep acc alive
                if (acc == 42.0) println(acc)
                t.hashCode()
            }
        }
        workers.forEach { it.start() }
        workers.forEach { it.join() }
        val elapsedSec = (System.nanoTime() - start) / 1e9
        val mops = totalOps / 1e6 / elapsedSec
        return "CPU: %.1f Mops/s across %d threads (%.1fs)".format(
            java.util.Locale.US, mops, threads, elapsedSec
        )
    }

    private suspend fun memTest(): String = withContext(Dispatchers.Default) {
        val size = 32 * 1024 * 1024 // 32 MiB
        val a = ByteArray(size)
        val b = ByteArray(size)
        var t = System.nanoTime()
        for (i in a.indices step 4096) a[i] = (i % 251).toByte()
        for (i in a.indices step 4096) b[i] = a[i]
        var writeNs = System.nanoTime() - t
        t = System.nanoTime()
        var sum = 0L
        for (i in b.indices step 4096) sum += b[i]
        val readNs = System.nanoTime() - t
        if (sum < 0) println(sum) // keep
        val mb = size / (1024.0 * 1024.0)
        "Memory: %.0f MB/s copy, %.0f MB/s scan (32 MiB)".format(
            java.util.Locale.US, mb / (writeNs / 1e9), mb / (readNs / 1e9)
        )
    }

    private suspend fun storageTest(context: Context): String = withContext(Dispatchers.IO) {
        val dir = context.cacheDir.resolve("syskit_test")
        dir.mkdirs()
        val file = File(dir, "speedtest.bin")
        val size = 8 * 1024 * 1024 // 8 MiB
        val buf = ByteArray(64 * 1024)
        java.util.Random().nextBytes(buf)
        var t = System.nanoTime()
        file.outputStream().use { out ->
            var written = 0
            while (written < size) { out.write(buf); written += buf.size }
            out.flush()
            out.fd.sync()
        }
        val writeNs = System.nanoTime() - t
        t = System.nanoTime()
        file.inputStream().use { inp ->
            var read = 0
            val sink = ByteArray(64 * 1024)
            while (read < size) { val r = inp.read(sink); if (r < 0) break; read += r }
        }
        val readNs = System.nanoTime() - t
        file.delete(); dir.delete()
        val mb = size / (1024.0 * 1024.0)
        "Storage: %.0f MB/s write, %.0f MB/s read (cache, 8 MiB)".format(
            java.util.Locale.US, mb / (writeNs / 1e9), mb / (readNs / 1e9)
        )
    }

    private suspend fun sensorTest(context: Context): String = withContext(Dispatchers.Default) {
        val sm = context.getSystemService(android.hardware.SensorManager::class.java)
            ?: return@withContext "Sensors: unavailable"
        val targets = listOf(
            android.hardware.Sensor.TYPE_ACCELEROMETER to "accelerometer",
            android.hardware.Sensor.TYPE_GYROSCOPE to "gyroscope",
            android.hardware.Sensor.TYPE_LIGHT to "light",
            android.hardware.Sensor.TYPE_STEP_COUNTER to "step counter"
        )
        val results = targets.map { (type, label) ->
            async {
                val sensor = sm.getDefaultSensor(type)
                    ?: return@async "$label: not present"
                var sample: String? = null
                val listener = object : android.hardware.SensorEventListener {
                    override fun onSensorChanged(event: android.hardware.SensorEvent?) {
                        if (sample == null && event != null) {
                            sample = event.values.take(3).joinToString(", ") {
                                String.format(java.util.Locale.US, "%.2f", it)
                            }
                        }
                    }
                    override fun onAccuracyChanged(sensor: android.hardware.Sensor?, accuracy: Int) {}
                }
                sm.registerListener(listener, sensor, android.hardware.SensorManager.SENSOR_DELAY_FASTEST)
                // wait up to 1.5 s for a first sample
                val deadline = System.currentTimeMillis() + 1500
                while (sample == null && System.currentTimeMillis() < deadline) {
                    Thread.sleep(50)
                }
                sm.unregisterListener(listener)
                if (sample != null) "$label: OK ($sample)" else "$label: present, no samples"
            }
        }.awaitAll()
        "Sensors → " + results.joinToString(" | ")
    }
}
