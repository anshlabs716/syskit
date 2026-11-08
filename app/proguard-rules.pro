
# Shizuku is accessed via reflection (PrivilegeManager) — keep it intact.
-keep class rikka.shizuku.** { *; }
-keep class moe.shizuku.** { *; }
-keep class android.content.pm.IPackageInstaller.** { *; }
