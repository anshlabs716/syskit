package com.example.syskit.engine

import android.content.Context
import com.example.syskit.model.PlayIntegrityResult
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.util.Base64

/**
 * Google Play Integrity check.
 *
 * The token can only be produced by Google Play Services (DroidGuard) on-device —
 * exactly how Play Integrity API Checker / SPIC do it. The JWS payload is decoded
 * locally (no server) to read the verdicts. On devices without GMS the request
 * fails and we explain that in plain language.
 */
object PlayIntegrityChecker {

    private const val PACKAGE_NAME = "com.anshlabs716.syskit"

    suspend fun check(context: Context): PlayIntegrityResult = withContext(Dispatchers.IO) {
        // 1. Ask Google Play Services for an integrity token (needs GMS/DroidGuard).
        val token = runCatching {
            val integrityManager = IntegrityManagerFactory.create(context.applicationContext)
            val nonce = ByteArray(16).also { SecureRandom().nextBytes(it) }
                .let { Base64.getUrlEncoder().withoutPadding().encodeToString(it) }
            val response = kotlinx.coroutines.suspendCancellableCoroutine<com.google.android.play.core.integrity.IntegrityTokenResponse> { cont ->
                integrityManager
                    .requestIntegrityToken(IntegrityTokenRequest.builder().setNonce(nonce).build())
                    .addOnSuccessListener { r -> if (cont.isActive) cont.resumeWith(Result.success(r)) }
                    .addOnFailureListener { e -> if (cont.isActive) cont.resumeWith(Result.failure(e)) }
            }
            response.token()
        }.getOrElse { e ->
            return@withContext PlayIntegrityResult(
                checked = true,
                success = false,
                errorMessage = "Needs Google Play Services (DroidGuard): ${e.message ?: "unavailable on this device"}"
            )
        }

        // 2. Decode the JWS payload locally — verdicts are plain JSON fields.
        runCatching {
            val payloadB64 = token.split(".").getOrNull(1)
                ?: return@withContext PlayIntegrityResult(
                    checked = true,
                    success = false,
                    errorMessage = "Unexpected token format from Google"
                )
            val json = JSONObject(
                String(Base64.getUrlDecoder().decode(padBase64Url(payloadB64)), Charsets.UTF_8)
            )

            val deviceVerdicts = json.optJSONObject("deviceIntegrity")
                ?.optJSONArray("deviceRecognitionVerdict")
                ?.let { arr -> (0 until arr.length()).map { arr.getString(it) } }
                ?: emptyList()

            PlayIntegrityResult(
                checked = true,
                success = true,
                appRecognitionVerdict = json.optJSONObject("appIntegrity")
                    ?.optString("appRecognitionVerdict", "UNKNOWN") ?: "UNKNOWN",
                deviceVerdicts = deviceVerdicts,
                licensingVerdict = json.optJSONObject("accountDetails")
                    ?.optString("appLicensingVerdict", "UNKNOWN") ?: "UNKNOWN"
            )
        }.getOrElse { e ->
            PlayIntegrityResult(
                checked = true,
                success = false,
                errorMessage = "Couldn't decode verdicts: ${e.message}"
            )
        }
    }

    private fun padBase64Url(s: String): String =
        s + "=".repeat((4 - s.length % 4) % 4)
}
