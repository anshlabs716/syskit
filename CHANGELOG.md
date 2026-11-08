# 📝 Changelog

All notable changes to SysKit are documented here.

## [1.0.6-android] - 2026-11-08

### Added
- SoC marketing names per device (Tensor / Snapdragon / Dimensity / Exynos /
  Kirin lookups) shown alongside the raw id — "Google Tensor (gs101)".
- Codename, Hardware, Platform, Bootloader and Fingerprint rows.
- Display & graphics card: resolution, refresh rate, OpenGL ES version.
- Options tab: theme mode (System / Dark / Light, persisted), console output
  font size slider, about section with GitHub links.
- Console auto-retries commands through `su` when root is granted and the
  unprivileged run hits "permission denied" (fixes setenforce etc.).
- Smooth crossfade transitions between tabs.

### Fixed
- Sensors list shows every sensor (the 14-item cap is gone).

### Changed
- Refined bugdroid ascii art in the console fetch.
- versionCode 6 → 7.


## [1.0.5-android] - 2026-11-08

### Fixed
- Duplicate "SysKit" title bar: the manifest had no theme, so the system drew
  a default action bar above the app's own header. The app now uses a
  NoActionBar theme with a dark window background.
- SoC / Processor showed the board name (e.g. "raven"); it now uses
  `Build.SOC_MODEL` / `ro.soc.model` (e.g. "Google gs101").
- SELinux showed "Unknown" on working devices; the status now reads
  `/sys/fs/selinux/enforce` directly with a `getenforce` fallback.
- neofetch art scrambled on narrow screens; the logo is padded to a fixed
  width, info lines are length-capped, and fetch output never word-wraps.
- Bloat tags misfired (blanket "Expert" for all com.android.*); heuristics are
  now conservative and more Pixel/Google packages are curated explicitly.
- Play Integrity errors dumped raw API text + documentation URLs; they are now
  one plain sentence, and verdict chips stay neutral until a check succeeds.

### Added
- Root detection now distinguishes "rooted" from "rooted but access not
  granted", and detects Magisk / KernelSU / APatch by installed app (with
  proper `<queries>` visibility), even when su access is denied.


## [1.0.4-android] - 2026-11-08

### Added
- `neofetch` / `fastfetch` / `fetch` in the Console renders a bugdroid with
  model, OS, SoC, kernel, uptime, RAM, battery, SELinux, verified boot, root
  and Shizuku status — generated locally since Android ships neither binary.
- Canta-style debloat classification: Recommended / Advanced / Expert / Keep /
  Unclassified badges on every package, plus vendor detection by package
  prefix (Google, Samsung, Xiaomi/MIUI, Meta, OnePlus, AOSP, …).
- `recommended` and `unsafe` filters in the package manager.

### Fixed
- The Shizuku authorize button never opened the permission dialog because the
  request was made from the ViewModel; it is now requested from the Activity
  context and the dialog actually appears.

### Changed
- Package cards are roomier: 48 dp icons, app name, package id, and a
  version/SDK/vendor line, with recommendation badges on the right.


## [1.0.3-android] - 2026-11-08

### Added
- Integrity tab (replaces Pairing + Tools): root checker, Google Play Integrity
  verdicts (Basic / Device / Strong / Virtual) via the official Play Integrity
  client, device posture (verified boot, dm-verity, SELinux, emulator,
  debuggable), privilege setup and system actions in one place.
- DevCheck-depth dashboard: live per-core CPU frequencies, full sensor list,
  camera resolutions and apertures, thermal status.
- Compact top bar with live Battery / RAM / Storage chips.

### Fixed
- SELinux buttons did the opposite of their label, and the app claimed success
  without root; it now re-reads the real mode and reports the truth.

### Changed
- Interface redesigned: clean sans typography (monospace only in the console
  and for package ids), calmer palette, no hard borders, uncrammed package list.
- Six tabs merged into five (System, Apps, Integrity, Console, Net).


## [1.0.2-android] - 2026-11-08

### Added
- Canta-style debloater in the Android app (Kotlin): uninstall bloat for user 0
  with `pm uninstall --user 0`, restore with `cmd package install-existing`.
- App icons in the package manager; `REMOVED` filter for user-0-uninstalled apps.
- Privilege gate: uninstall/restore/freeze explain when Shizuku or Root is
  required and link straight to the Privileges tab.
- System-app removal warnings in the confirm dialog.

### Changed
- Android APK targets SDK 37.
- README shortened; clarified that the Android APK is written **only in Kotlin**.


## [1.0.0-java-lite] - 2026-08-18

### Added

* Java Lite implementation
* Lightweight SysKit functionality
* Designed for lower-resource environments
* `SyskitLite.java`

---

## [1.0.0-java] - 2026-08-18

### Added

* Full Java implementation
* SysKit functionality implemented in Java
* Command-line interface
* System utilities and diagnostics

---

## [1.0.0-python-lite] - 2026-08-18

### Added

* Python Lite implementation
* Lightweight command-line toolkit
* Termux-focused implementation
* Reduced-resource SysKit functionality
* `syskit lite.py`

---

## [1.0.0-python] - 2026-08-18

### Added

* Full Python implementation
* SysKit functionality implemented in Python
* Command-line interface
* System utilities and diagnostics
* `syskit.py`

---

## [1.0.0-lite] - 2026-07-17

### Added

* Lightweight C implementation
* Reduced-resource SysKit toolkit
* Core system information and utility features
* `syskit-lite.c`
* `syskit-lite.sh`

---

## [1.0.0] - 2026-07-17

### Added

* Initial full SysKit release
* 100+ tools across 15 categories
* System information
* CPU, RAM, storage, and process monitoring
* Network diagnostics
* Battery and power information
* Package management
* System cleaning
* Storage utilities
* Security checks
* File utilities
* Archive utilities
* Internet utilities
* Backup and restore
* Settings and customization
* Root/sudo handling
* Dependency handling
* Progress indicators
* Fullscreen terminal support
* Public IP detection with multiple fallbacks

### Fixed

* Box wrapping and alignment
* Unbound variable errors
* Cleaner menu unexpected exits
* Duplicate top-process entries
* RAM information formatting

### Changed

* Professional CLI interface
* Improved error handling
* Expanded system utilities
