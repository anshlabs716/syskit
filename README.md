<div align="center">

# 🚀 SysKit

### Universal System Toolkit

<p>
<a href="https://github.com/anshlabs716/syskit"><img src="https://img.shields.io/badge/C-31.3%25-A8B9CC?style=for-the-badge&logo=c&logoColor=white" alt="C"></a>&nbsp;&nbsp;
<a href="https://github.com/anshlabs716/syskit"><img src="https://img.shields.io/badge/Java-26.1%25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"></a>&nbsp;&nbsp;
<a href="https://github.com/anshlabs716/syskit"><img src="https://img.shields.io/badge/Shell-22.9%25-89E051?style=for-the-badge&logo=gnubash&logoColor=white" alt="Shell"></a>&nbsp;&nbsp;
<a href="https://github.com/anshlabs716/syskit"><img src="https://img.shields.io/badge/Python-19.7%25-3776AB?style=for-the-badge&logo=python&logoColor=white" alt="Python"></a>&nbsp;&nbsp;
<a href="https://github.com/anshlabs716/syskit"><img src="https://img.shields.io/badge/Linux-Supported-FCC624?style=for-the-badge&logo=linux&logoColor=black" alt="Linux"></a>&nbsp;&nbsp;
<a href="https://github.com/anshlabs716/syskit"><img src="https://img.shields.io/badge/Termux-Supported-000000?style=for-the-badge&logo=termux&logoColor=white" alt="Termux"></a>&nbsp;&nbsp;
<a href="https://github.com/anshlabs716/syskit"><img src="https://img.shields.io/badge/Android-APK-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android APK"></a>&nbsp;&nbsp;
<a href="https://github.com/anshlabs716/syskit"><img src="https://img.shields.io/badge/Shizuku-Supported-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Shizuku"></a>&nbsp;&nbsp;
<a href="https://github.com/anshlabs716/syskit"><img src="https://img.shields.io/badge/Root-Supported-000000?style=for-the-badge&logo=android&logoColor=white" alt="Root"></a>
</p>

<img width="320" height="320" alt="SysKit Full" src="https://private-user-images.githubusercontent.com/233554853/638142142-1be2c14c-7db4-4a24-baa5-d1c3c6d74ca5.png" />

<img width="320" height="320" alt="SysKit Lite" src="https://private-user-images.githubusercontent.com/233554853/638142244-4caddf1b-2079-42c5-934e-59d530b1cc81.png" />

<img width="320" height="320" alt="SysKit GUI" src="https://private-user-images.githubusercontent.com/233554853/638147438-5c6c45ee-8565-47d2-a9be-353ef5a19f28.png" />

> **SysKit** is a multi-language system toolkit for information, diagnostics, maintenance, networking, storage, utilities, and more.

</div>

---

## 🧠 What is SysKit?

SysKit brings a collection of useful system tools into one menu-driven toolkit.

Instead of jumping between different commands for system information, networking, storage, maintenance, and diagnostics, SysKit puts them together in one place.

The project includes implementations in **C, Bash, Python, and Java**, lightweight variants for restricted environments such as Termux, a GTK3 desktop interface, and now an **Android APK**.

```text
                         🚀 SysKit
                            │
          ┌─────────────────┼──────────────────┐
          │                 │                  │
       Desktop            Termux            Android
          │                 │                  │
     ┌────┼────┐       ┌────┼────┐       ┌─────┴─────┐
     │    │    │       │    │    │       │           │
     C   Bash Python   C   Bash Python  Shizuku     Root
          │
         Java
          │
         GTK3
```

---

# ✨ Features

| Category          | What SysKit provides                                         |
| ----------------- | ------------------------------------------------------------ |
| 🖥️ **System**    | OS, kernel, CPU, GPU, RAM, hardware, uptime, environment     |
| 📊 **Monitoring** | CPU, RAM, disk, processes, network, temperature              |
| 🌐 **Network**    | Ping, connectivity, IP, DNS, gateway, Wi-Fi                  |
| 🔋 **Power**      | Battery, charging, health, power information                 |
| 📦 **Packages**   | Updates, upgrades, search, installed packages, cleanup       |
| 🧹 **Cleaner**    | Cache, temporary files, logs, trash, package cache           |
| 💾 **Storage**    | Disk usage, mounted drives, directories, large files, SMART  |
| 🔐 **Security**   | Firewall, open ports, SSH, services, basic checks            |
| 📁 **Files**      | Search, text search, directory trees, statistics, duplicates |
| 📦 **Archives**   | ZIP and TAR.GZ creation/extraction                           |
| 🛠️ **Utilities** | Passwords, random strings, hashes, UUIDs                     |
| 🌍 **Internet**   | Weather, time, calendar, connectivity                        |
| 💾 **Backup**     | Backup, restore, compression, verification                   |
| ⚙️ **Settings**   | Colors, emojis, animations, reset                            |
| ❓ **Help**        | Help, about, documentation, support                          |

> ⚠️ Feature availability depends on the implementation, operating system, installed dependencies, hardware, and permissions.

---

# 🧩 Implementations

SysKit isn't tied to a single language or platform.

| Version            | Language / Technology | File / Location   | Environment                       |
| ------------------ | --------------------- | ----------------- | --------------------------------- |
| **SysKit**         | C                     | `syskit.c`        | Full terminal version             |
| **SysKit Lite**    | C                     | `syskit-lite.c`   | Lightweight environments          |
| **SysKit GUI**     | C / GTK3              | `syskit-gui.c`    | Linux desktop                     |
| **SysKit**         | Bash                  | `syskit.sh`       | Unix-like systems                 |
| **SysKit Lite**    | Bash                  | `syskit-lite.sh`  | Lightweight environments          |
| **SysKit**         | Python                | `syskit.py`       | Python environments               |
| **SysKit Lite**    | Python                | `syskit lite.py`  | Termux / lightweight environments |
| **SysKit**         | Java                  | `Syskit.java`     | Java environments                 |
| **SysKit Lite**    | Java                  | `SyskitLite.java` | Lightweight Java environments     |
| **SysKit Android** | Kotlin / Android      | `app/`            | Android                           |

---

# 📱 Android

SysKit now has an **Android-native APK implementation**.

The Android version is separate from the traditional Linux/Termux implementations and uses an Android project structure with **Gradle and Kotlin**.

### Android capabilities

* 📱 Native Android application
* 🧩 Android-specific system functionality
* 🔌 **Shizuku support**
* 🔓 **Root-aware functionality**
* ⚙️ Android-native architecture
* 🛠️ Built using Gradle
* 🧑‍💻 Kotlin-based Android code
* 📦 Distributed as an APK release

### 🔌 Shizuku

SysKit can use **Shizuku** for Android operations that require elevated system-level access without requiring traditional root.

This allows SysKit to access additional functionality through Android's supported privileged APIs when Shizuku is available.

### 🔓 Root

The Android version also includes **root-aware functionality**.

When a device is rooted, SysKit can use root access for functionality that requires elevated permissions.

> ⚠️ Root functionality depends on the device, Android version, root implementation, granted permissions, and what the specific operation requires.

### 📦 APK

The Android APK is available through the project's GitHub Releases.

**Latest APK release:** `v1.0.1-apk`

[Download the latest SysKit APK release](https://github.com/anshlabs716/syskit/releases/tag/v1.0.1-apk?utm_source=chatgpt.com)

> ⚠️ Only install APKs from sources you trust. SysKit is an open-source project, so the source code is available in this repository.

---

# 🤖 Android Development

The Android implementation uses a standard Android project structure.

```text
app/
├── src/
│   └── main/
│       ├── java/
│       ├── res/
│       └── AndroidManifest.xml
│
├── build.gradle.kts
│
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── gradle/
```

The Android project uses:

* **Kotlin**
* **Gradle**
* **Android SDK**
* Android-native APIs
* Shizuku integration
* Root-aware functionality

---

# 🖥️ Platform Support

| Platform   | Status | Notes                             |
| ---------- | ------ | --------------------------------- |
| 🐧 Linux   | ✅      | Primary desktop platform          |
| 📱 Termux  | ✅      | Lite versions recommended         |
| 🤖 Android | ✅      | Native APK available              |
| 🔌 Shizuku | ✅      | Supported by Android version      |
| 🔓 Root    | ✅      | Supported by Android version      |
| 🍎 macOS   | 🧪     | Shell compatibility needs testing |
| 👻 BSD     | 🧪     | Shell compatibility needs testing |

### Linux

Linux is the primary target for the original SysKit implementations.

The exact functionality depends on your distribution and installed system utilities.

### Termux

SysKit can run in Termux, with the Lite implementations being the recommended choice.

Android does not expose every traditional Linux interface, so some features may not be available.

### Android

The Android version is a **native Android application**, rather than simply packaging the Linux version into an APK.

Android-specific functionality is implemented separately and can use Android APIs, Shizuku, and root where supported.

### macOS / BSD

Shell-based compatibility is planned, but these platforms still need proper testing.

---

# 📥 Installation

## Clone

```bash
git clone https://github.com/anshlabs716/syskit.git
cd syskit
```

---

## 🐚 Bash

### Full

```bash
chmod +x syskit.sh
./syskit.sh
```

### Lite

```bash
chmod +x syskit-lite.sh
./syskit-lite.sh
```

---

## 🐍 Python

### Full

```bash
python3 syskit.py
```

### Lite

```bash
python3 "syskit lite.py"
```

---

## ☕ Java

Check your JDK:

```bash
java --version
javac --version
```

### Full

```bash
javac Syskit.java
java Syskit
```

### Lite

```bash
javac SyskitLite.java
java SyskitLite
```

---

## 🦾 C

### Full

```bash
gcc syskit.c -o syskit
./syskit
```

### Lite

```bash
gcc syskit-lite.c -o syskit-lite
./syskit-lite
```

### Clang

```bash
clang syskit.c -o syskit
./syskit
```

---

## 🖥️ GTK3

The GTK3 version requires GTK3 development libraries and `pkg-config`.

### Compile

```bash
gcc syskit-gui.c -o syskit-gui $(pkg-config --cflags --libs gtk+-3.0)
```

### Run

```bash
./syskit-gui
```

### Debian / Ubuntu / Mint

```bash
sudo apt update
sudo apt install build-essential pkg-config libgtk-3-dev
```

### Fedora

```bash
sudo dnf install gcc pkgconf-pkg-config gtk3-devel
```

### Arch Linux

```bash
sudo pacman -S base-devel pkgconf gtk3
```

---

# 📱 Termux

Install the basic tools:

```bash
pkg update
pkg upgrade
pkg install git python clang
```

Clone SysKit:

```bash
git clone https://github.com/anshlabs716/syskit.git
cd syskit
```

### Python Lite

```bash
python "syskit lite.py"
```

### C Lite

```bash
clang syskit-lite.c -o syskit-lite
./syskit-lite
```

### Java Lite

If a suitable JDK is available:

```bash
javac SyskitLite.java
java SyskitLite
```

> 💡 **Recommended:** Start with a Lite implementation on Termux.

---

# 📦 Android APK Installation

Download the latest APK from the GitHub Releases page.

[SysKit Releases](https://github.com/anshlabs716/syskit/releases?utm_source=chatgpt.com)

Then install the APK on a compatible Android device.

For advanced functionality:

* **Shizuku** can be configured for supported operations.
* **Root access** can be granted where root functionality is required.

> ⚠️ Android permissions and available functionality vary by device and Android version.

---

# 📦 Dependencies

Different implementations use different dependencies.

## Core

* Bash
* GCC or Clang
* Python 3
* Java JDK
* Git

## Common Utilities

Some features may use:

* `curl`
* `wget`
* `jq`
* `fastfetch`
* `tar`
* `zip`
* `unzip`
* `tree`

## Hardware / System

Depending on the feature and platform:

* `lshw`
* `dmidecode`
* `smartctl`
* `sensors`
* `lspci`
* `uuidgen`

## Networking

Some networking features may use:

* `ping`
* `nmcli`
* NetworkManager
* DNS utilities

## Android

The Android build uses the Android project configuration contained in the repository.

Android-specific functionality may additionally depend on:

* Android SDK
* Android system APIs
* Shizuku
* Root access where required

> Not every dependency is required to run every version of SysKit.

---

# 🎛️ Main Menu

SysKit uses a simple numbered menu:

```text
╭────────────────────────────╮
│          🚀 SysKit         │
├────────────────────────────┤
│  1.  System               │
│  2.  Monitoring           │
│  3.  Network              │
│  4.  Power                │
│  5.  Packages             │
│  6.  Cleaner              │
│  7.  Storage              │
│  8.  Security             │
│  9.  Files                │
│ 10.  Archives             │
│ 11.  Utilities            │
│ 12.  Internet             │
│ 13.  Backup               │
│ 14.  Settings             │
│ 15.  Help                 │
│ 16.  Exit                 │
╰────────────────────────────╯
```

---

# 🧪 Testing

SysKit has multiple implementations, so compatibility matters.

## Feature Testing

* C — runs great
* C Lite — super fast; **NEVER run this on real Linux**
* Bash — works amazingly well on Linux
* Bash Lite — works really well on Termux; **NEVER run this on real Linux**
* Python — some features are a bit buggier than others
* Python Lite — has some issues with device info but overall great; **NEVER run this on real Linux**
* Java — works pretty well
* Java Lite — not tested yet; **NEVER run this on real Linux**
* GTK3 — works well but is slower compared to the other C versions
* Android — APK implementation with Shizuku/root support

> ⚠️ **Never run Lite versions on real Linux.**
>
> They are designed for restricted environments such as Termux and can behave incorrectly on full Linux systems.

## Android Testing

Android functionality should be tested separately from the desktop implementations.

Testing areas include:

* APK installation
* Android system information
* Device compatibility
* Shizuku functionality
* Root functionality
* Permissions
* Android-version compatibility
* Hardware-specific behavior

> ⚠️ Android features may behave differently across manufacturers, Android versions, root implementations, and device configurations.

## Platform Testing

* Debian
* Ubuntu
* Linux Mint
* Fedora
* Arch Linux
* Alpine
* openSUSE
* Void Linux
* Termux
* Android
* macOS
* BSD

---

# 🗺️ Roadmap

## 🔧 Core

* C implementation
* C Lite implementation
* Bash implementation
* Bash Lite implementation
* Python implementation
* Python Lite implementation
* Java implementation
* Java Lite implementation
* GTK3 implementation
* Improve error handling
* Improve dependency detection
* Expand hardware support
* Expand monitoring
* Expand networking
* Improve storage diagnostics
* Improve Lite versions
* Improve documentation
* Automated testing

## 📱 Android

* Native Android implementation
* Android system information
* Android-specific diagnostics
* Expand Shizuku integration
* Expand root-aware functionality
* Improve device compatibility
* Improve Android permissions handling
* Test across Android versions
* Test across different devices
* Improve APK releases
* Expand Android feature parity with desktop versions

## 🌍 Compatibility

* Linux
* Termux
* Android
* Debian-based testing
* Fedora-based testing
* Arch-based testing
* Alpine testing
* openSUSE testing
* Void Linux testing
* macOS testing
* BSD testing

## 🧪 Reliability

* Test every menu option
* Test every implementation
* Test multiple distributions
* Test low-resource environments
* Test archive functionality
* Test backup / restore
* Test missing dependencies
* Test permission handling
* Improve compatibility detection
* Test Android permissions
* Test Shizuku functionality
* Test root functionality

---

# 🗂️ Project Structure

```text
syskit/
├── .gitignore
│
├── CHANGELOG.md
├── CONTRIBUTING.md
├── LICENSE
├── README.md
├── SECURITY.md
│
├── Syskit.desktop
│
├── Syskit.java
├── SyskitLite.java
│
├── syskit.py
├── syskit lite.py
│
├── syskit.c
├── syskit-lite.c
├── syskit-gui.c
│
├── syskit.sh
├── syskit-lite.sh
│
├── app/
├── gradle/
├── build.gradle.kts
├── gradle.properties
└── settings.gradle.kts
```

---

# 🛠️ Troubleshooting

### `Permission denied`

```bash
chmod +x syskit.sh
```

Then:

```bash
./syskit.sh
```

Some system operations may require elevated permissions.

### `command not found`

Install the missing dependency using your distribution's package manager.

### C compilation fails

```bash
gcc --version
```

or:

```bash
clang --version
```

### Java compilation fails

```bash
java --version
javac --version
```

### GTK3 compilation fails

```bash
pkg-config --modversion gtk+-3.0
```

If GTK3 cannot be found, install the appropriate development package.

### Android functionality doesn't work

Check:

1. Android version compatibility
2. App permissions
3. Whether the required Android API is available
4. Whether Shizuku is running when required
5. Whether SysKit has been granted the required Shizuku permission
6. Whether root access is available when required
7. Device-specific restrictions

### A feature doesn't work

Check:

1. Required dependencies
2. Platform compatibility
3. Permissions
4. Hardware support
5. Whether the Lite version works
6. Whether the feature requires Shizuku or root on Android

If the problem continues, open an issue with useful information about your environment.

---

# 🤝 Contributing

Contributions, testing, bug reports, ideas, and improvements are welcome.

Before contributing, read [`CONTRIBUTING.md`](https://github.com/anshlabs716/syskit/blob/main/CONTRIBUTING.md).

When submitting changes:

1. Keep changes focused.
2. Test what you changed.
3. Avoid breaking other implementations.
4. Document new functionality where appropriate.
5. Explain compatibility considerations.
6. For Android changes, include relevant Android-version/device information when reporting compatibility issues.

---

# 🔐 Security

For security-related issues, see [`SECURITY.md`](https://github.com/anshlabs716/syskit/blob/main/SECURITY.md).

Please avoid publicly exposing sensitive security issues before they can be investigated.

---

# 📜 Changelog

See [`CHANGELOG.md`](https://github.com/anshlabs716/syskit/blob/main/CHANGELOG.md) for development history.

---

# 👥 Credits

## Weather Feature

Special thanks to [**@shozanthebozan**](https://github.com/shozanthebozan).

The SysKit weather feature is based on [**onNow**](https://github.com/shozanthebozan/onNow) by shozanthebozan.

Please check out the original project:

**🔗** [**shozanthebozan/onNow**](https://github.com/shozanthebozan/onNow)

> 💙 Credit goes to shozanthebozan for the original weather implementation that inspired the weather functionality in SysKit.

---

# ⚠️ Disclaimer

SysKit is intended for **system administration, diagnostics, maintenance, troubleshooting, learning, and personal use**.

Some operations can modify system files, packages, services, caches, or other system resources.

Android features may also require additional permissions, Shizuku, or root depending on the operation.

**Use system-modifying features carefully.**

Feature availability varies between operating systems, Android versions, devices, implementations, and permissions.

---

# 📄 License

SysKit is distributed under the license included in [`LICENSE`](https://github.com/anshlabs716/syskit/blob/main/LICENSE).

---

# 💡 Philosophy

SysKit is built around a simple development loop:

```text
        💡 Idea
           │
           ▼
        🔨 Build
           │
           ▼
        💥 Break
           │
           ▼
      🔎 Investigate
           │
           ▼
        🔧 Fix
           │
           ▼
      🚀 Improve
           │
           ▼
        🧠 Learn
           │
           └───────────↻
```

> **Build it. Break it. Understand it. Improve it.**

---

<div align="center">

# 🚀 SysKit

**C · Bash · Python · Java · Kotlin**

**Linux · Termux · Android · GTK3**

**Terminal · Lite · GUI · APK · Shizuku · Root**

> **One toolkit. Multiple implementations. Always improving.**

</div>
