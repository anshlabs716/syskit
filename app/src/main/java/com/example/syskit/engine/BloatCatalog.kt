package com.example.syskit.engine

/**
 * Canta-style classification of packages: is it safe to debloat?
 *
 * Curated entries + prefix heuristics. Anything not in the catalog is
 * [BloatLevel.UNKNOWN] — the UI tells the user it's unclassified so they
 * decide, rather than pretending to know.
 */
enum class BloatLevel(val label: String) {
    RECOMMENDED("Recommended"), // safe & sensible to remove for most people
    ADVANCED("Advanced"),       // fine to remove if you don't use it
    EXPERT("Expert"),           // removable, but expect things to break
    UNSAFE("Keep"),             // removing this can break the device
    UNKNOWN("Unclassified")
}

data class BloatInfo(val level: BloatLevel, val vendor: String)

object BloatCatalog {

    private const val SELF = "com.anshlabs716.syskit"

    /** Packages we actively recommend removing (common OEM/Google/Meta junk). */
    private val recommended = setOf(
        "com.facebook.appmanager", "com.facebook.services", "com.facebook.system",
        "com.facebook.katana", "com.facebook.orca", "com.instagram.android",
        "com.linkedin.android",
        "com.google.android.apps.tachyon", "com.google.android.videos",
        "com.google.android.apps.books", "com.google.android.apps.magazines",
        "com.google.android.music", "com.google.android.vr.home",
        "com.google.android.apps.wellbeing.faceunlock",
        "com.google.ar.core", "com.google.android.tts",
        "com.samsung.android.app.spage", "com.samsung.android.oneconnect",
        "com.samsung.android.arzone", "com.samsung.android.spay",
        "com.samsung.android.game.gamehome", "com.samsung.android.game.gametools",
        "com.samsung.android.mateagent", "com.samsung.android.scloud",
        "com.sec.android.app.samsungapps",
        "com.miui.player", "com.miui.gallery", "com.miui.cloudservice",
        "com.xiaomi.midrop", "com.miui.bugreport",
        "com.huawei.appmarket", "com.hihonor.cloudservice"
    )

    /** Removing these will hurt the system. */
    private val unsafe = setOf(
        SELF, "org.fdroid.fdroid", "org.fdroid.fdroid.privileged",
        "com.android.systemui", "com.android.settings", "com.android.phone",
        "com.android.server.telecom", "com.android.providers.contacts",
        "com.android.providers.telephony", "com.android.providers.media",
        "com.android.providers.settings", "com.android.keychain",
        "com.android.bluetooth", "com.android.nfc", "com.android.se",
        "com.android.wifi", "com.android.networkstack",
        "com.google.android.gms", "com.google.android.gsf",
        "com.google.android.gsf.login", "com.google.android.backup",
        "com.google.android.configupdater", "com.google.android.partnersetup",
        "com.samsung.android.launcher", "com.sec.android.app.launcher",
        "com.miui.home", "com.google.android.launcher"
    )

    /** Works, but app views / TTS / voice features can break. */
    private val expert = setOf(
        "com.google.android.webview", "com.android.webview",
        "com.google.android.trichromelibrary", "com.android.chrome",
        "com.google.android.googlequicksearchbox",
        "com.google.android.gms.sup", "com.google.android.printservice.recommendation",
        "com.android.bips", "com.google.android.apps.docs.editors.docs",
        "com.google.android.inputmethod.latin"
    )

    /** Fine to remove if you don't use them. */
    private val advanced = setOf(
        "com.google.android.youtube", "com.google.android.apps.youtube.music",
        "com.google.android.youtube.tv", "com.google.android.gm",
        "com.google.android.apps.maps", "com.google.android.apps.photos",
        "com.google.android.apps.docs", "com.google.android.apps.plus",
        "com.google.android.apps.translate", "com.google.android.apps.recorder",
        "com.google.android.calendar", "com.google.android.contacts",
        "com.google.android.dialer", "com.google.android.messaging",
        "com.google.android.keep", "com.google.android.deskclock",
        "com.google.android.healthconnect.controller",
        "com.google.android.fitbit", "com.google.android.wearable.app",
        "com.samsung.android.app.notes", "com.samsung.android.calendar",
        "com.samsung.android.contacts", "com.samsung.android.dialer",
        "com.samsung.android.messaging", "com.samsung.android.smartswitchassistant",
        "com.sec.android.app.popupcalculator", "com.sec.android.app.clockpackage",
        "com.miui.videoplayer", "com.miui.weather2", "com.miui.compass",
        "com.android.stk", "com.android.egg", "com.android.htmlviewer"
    )

    private val vendorPrefixes = listOf(
        "com.google." to "Google", "com.samsung." to "Samsung", "com.sec." to "Samsung",
        "com.miui." to "Xiaomi (MIUI)", "com.xiaomi." to "Xiaomi", "com.duokan." to "Xiaomi",
        "com.huawei." to "Huawei", "com.hihonor." to "Honor",
        "com.oplus." to "OPPO/OnePlus", "com.oppo." to "OPPO", "com.coloros." to "OPPO",
        "com.oneplus." to "OnePlus", "net.oneplus." to "OnePlus",
        "com.vivo." to "vivo", "com.amazon." to "Amazon",
        "com.facebook." to "Meta", "com.instagram." to "Meta",
        "com.microsoft." to "Microsoft", "com.qualcomm." to "Qualcomm",
        "com.mediatek." to "MediaTek", "org.lineageos." to "LineageOS",
        "org.fdroid." to "F-Droid", "com.android." to "AOSP",
        "com.anshlabs716." to "AnshLabs"
    )

    fun vendorFor(pkg: String): String =
        vendorPrefixes.firstOrNull { pkg.startsWith(it.first) }?.second ?: ""

    fun classify(pkg: String): BloatInfo {
        val vendor = vendorFor(pkg)
        val level = when {
            pkg in unsafe -> BloatLevel.UNSAFE
            pkg in recommended -> BloatLevel.RECOMMENDED
            pkg in expert -> BloatLevel.EXPERT
            pkg in advanced -> BloatLevel.ADVANCED
            // prefix heuristics for well-known families not in the exact lists
            pkg.startsWith("com.facebook.") || pkg.startsWith("com.instagram.") -> BloatLevel.RECOMMENDED
            pkg.startsWith("com.google.android.gms") -> BloatLevel.UNSAFE
            pkg.startsWith("com.android.providers") -> BloatLevel.UNSAFE
            pkg.startsWith("com.google.android.webview") -> BloatLevel.EXPERT
            pkg.startsWith("com.google.android.apps.") -> BloatLevel.ADVANCED
            pkg.startsWith("com.samsung.android.app") -> BloatLevel.ADVANCED
            pkg.startsWith("com.miui.") -> BloatLevel.ADVANCED
            pkg.startsWith("com.android.") -> BloatLevel.EXPERT
            else -> BloatLevel.UNKNOWN
        }
        return BloatInfo(level, vendor)
    }
}
