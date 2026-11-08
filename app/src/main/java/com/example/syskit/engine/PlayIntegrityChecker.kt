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

    /** Maps raw Play Integrity API errors to one plain sentence — no URL dumps. */
    private fun friendlyError(raw: String): String {
        val code = Regex("\\((-?\\d+)\\)").findAll(raw)
            .mapNotNull { it.groupValues[1].toIntOrNull() }
            .firstOrNull() ?: 0
        return when (code) {
            -1 -> "Google Play Services is missing or broken — Play Integrity can't run on this device"
            -2 -> "Google Play Store is missing or unofficial — Play Integrity needs the real Play Store"
            -3 -> "Network error while contacting Google — check your connection"
            -4 -> "The installed Play Store is too old — update it via Google"
            -5, -6, -7 -> "Play Integrity is not available for this app build on this device"
            -8 -> "Too many requests — try again in a moment"
            else -> "Play Integrity unavailable on this device"
        }
    }

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
                errorMessage = friendlyError(e.message ?: "")
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
