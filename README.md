<div align="center">

# 🚀 SysKit

### Universal System Toolkit

<img src="https://img.shields.io/badge/C-24.4%25-A8B9CC?style=for-the-badge&logo=c&logoColor=white" alt="C">

 

<img src="https://img.shields.io/badge/Kotlin-22%25-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">

 

<img src="https://img.shields.io/badge/Java-20.4%25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">

 

<img src="https://img.shields.io/badge/Shell-17.9%25-89E051?style=for-the-badge&logo=gnubash&logoColor=white" alt="Shell">

 

<img src="https://img.shields.io/badge/Python-15.3%25-3776AB?style=for-the-badge&logo=python&logoColor=white" alt="Python">

 

<img src="https://img.shields.io/badge/Linux-Supported-FCC624?style=for-the-badge&logo=linux&logoColor=black" alt="Linux">

 

<img src="https://img.shields.io/badge/Android-Supported-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android">

 

<img src="https://img.shields.io/badge/Termux-Supported-000000?style=for-the-badge&logo=termux&logoColor=white" alt="Termux">

 

<img src="https://img.shields.io/badge/GTK3-Supported-7FE719?style=for-the-badge&logo=gtk&logoColor=white" alt="GTK3">

 

<img src="https://img.shields.io/github/license/anshlabs716/syskit?style=for-the-badge" alt="License">

 

<img src="https://img.shields.io/github/stars/anshlabs716/syskit?style=for-the-badge" alt="GitHub Stars">

 

<img src="https://img.shields.io/github/issues/anshlabs716/syskit?style=for-the-badge" alt="GitHub Issues">

 

<img src="https://img.shields.io/github/last-commit/anshlabs716/syskit?style=for-the-badge" alt="Last Commit">

</div>

---

<div align="center">

<img width="320" height="320" alt="SysKit Full" src="https://github.com/user-attachments/assets/69f077f3-5705-4ca7-a308-216f86dcb942" />

<img width="320" height="320" alt="SysKit Lite" src="https://github.com/user-attachments/assets/a4a79df9-b15e-45fa-a63a-c5fab11d0af1" />

<img width="320" height="320" alt="SysKit GUI" src="https://github.com/user-attachments/assets/fbb6fadd-f682-4b84-87bb-197a16a7c97b" />

</div>

> **SysKit** is a multi-language system toolkit for information, diagnostics, maintenance, networking, storage, utilities, and more.

---

# 🧠 What is SysKit?

SysKit brings a collection of useful system tools into one menu-driven toolkit.

Instead of jumping between different commands for system information, networking, storage, maintenance, and diagnostics, SysKit puts them together in one place.

The project includes multiple implementations for different environments:

* 🦾 C
* 🐚 Bash
* 🐍 Python
* ☕ Java
* 📱 Android / Kotlin
* 🖥️ GTK3
* 📱 Termux

SysKit is designed to adapt to the platform it is running on instead of forcing every environment to use the same implementation.

---

# 🧩 Implementations

| Version            | Language | Environment                       |
| ------------------ | -------- | --------------------------------- |
| **SysKit**         | C        | Full terminal version             |
| **SysKit Lite**    | C        | Lightweight environments          |
| **SysKit GUI**     | C / GTK3 | Linux desktop                     |
| **SysKit**         | Bash     | Unix-like systems                 |
| **SysKit Lite**    | Bash     | Lightweight environments          |
| **SysKit**         | Python   | Python environments               |
| **SysKit Lite**    | Python   | Termux / lightweight environments |
| **SysKit**         | Java     | Java environments                 |
| **SysKit Lite**    | Java     | Lightweight Java environments     |
| **SysKit Android** | Kotlin   | Android                           |

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

# 📱 Android

SysKit now includes a **native Android APK**.

The Android version is a separate Android implementation rather than simply packaging the existing Linux CLI versions into an APK.

The repository contains an Android application under `app/`, and Kotlin is now one of the project's tracked languages.

## 📦 Android APK

The current Android release is:

**`v1.0.1-apk`**

[**📥 Download SysKit Android v1.0.1**](https://github.com/anshlabs716/syskit/releases/tag/v1.0.1-apk)

The `v1.0.1-apk` release is marked as the latest Android APK release. The release notes state that root, Shizuku, and a shell were added in this version.

> ⚠️ **Do not use ****`v1.0.0-apk`****.** The current release notes explicitly identify it as broken. Use `v1.0.1-apk` instead.

---

## 🔐 Shizuku Support

SysKit Android supports **Shizuku** for elevated Android functionality.

Shizuku allows compatible Android applications to use supported system APIs through an ADB or root-backed service.

SysKit can therefore use Shizuku-based functionality without requiring every user to have a rooted device.

> Shizuku-dependent features require Shizuku to be running and the required permissions to be granted.

Shizuku itself supports both rooted and non-rooted setups, with ADB-based operation available on supported Android versions.

---

## 🔓 Root Support

The Android version also includes **root-aware functionality**.

Root is optional for the application, but supported functionality can use elevated access when available.

> ⚠️ Root-level operations can have significant system access. Use elevated functionality carefully.

The current `v1.0.1-apk` release specifically states that root support was added.

---

## 🐚 Android Shell

The current Android release also includes a **shell**.

This allows SysKit Android to provide functionality that goes beyond a normal Android application where the required access is available.

The release notes for `v1.0.1-apk` explicitly mention that a shell was added alongside root and Shizuku support.

---

## 📱 Android Architecture

The Android implementation uses the repository's Android project structure:

```text
syskit/
├── app/
│   └── Android application
│
├── gradle/
│
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── metadata.json
```

The repository currently contains `app/`, `gradle/`, `build.gradle.kts`, `gradle.properties`, `settings.gradle.kts`, and `metadata.json` alongside the original SysKit implementations.

---

# 🖥️ Platform Support

| Platform   | Status | Notes                             |
| ---------- | ------ | --------------------------------- |
| 🐧 Linux   | ✅      | Primary platform                  |
| 📱 Android | ✅      | Native Android APK                |
| 📱 Termux  | ✅      | Lite versions recommended         |
| 🍎 macOS   | 🧪     | Shell compatibility needs testing |
| 👻 BSD     | 🧪     | Shell compatibility needs testing |

### Linux

Linux is the primary target for the original SysKit implementations.

The exact functionality depends on your distribution and installed system utilities.

### Android

Android has its own native implementation.

Android does not expose every traditional Linux interface to normal applications, so Android-specific functionality is implemented separately.

SysKit Android can also use supported elevated functionality through Shizuku and root.

### Termux

SysKit can run in Termux, with the Lite implementations being the recommended choice.

Android does not expose every traditional Linux interface, so some functionality may not be available.

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

### Install Python

**Debian / Ubuntu / Mint**

```bash
sudo apt update
sudo apt install python3
```

**Fedora**

```bash
sudo dnf install python3
```

**Arch Linux**

```bash
sudo pacman -S python
```

**Alpine**

```bash
sudo apk add python3
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

### Install a JDK

**Debian / Ubuntu / Mint**

```bash
sudo apt update
sudo apt install default-jdk
```

**Fedora**

```bash
sudo dnf install java-latest-openjdk-devel
```

**Arch Linux**

```bash
sudo pacman -S jdk-openjdk
```

**Alpine**

```bash
sudo apk add openjdk17
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

# 📱 Android Installation

Download the current Android APK from the GitHub Releases page:

[**📥 SysKit v1.0.1-apk**](https://github.com/anshlabs716/syskit/releases/tag/v1.0.1-apk)

### Setup

1. Download `v1.0.1-apk`.
2. Install SysKit on your Android device.
3. Open SysKit.
4. Grant any Android permissions required by the features you use.
5. If using Shizuku functionality, make sure Shizuku is running.
6. If using root functionality, grant root access when requested.

> ⚠️ Exact functionality depends on your Android version, device, permissions, and whether elevated access is available.

---

# 📱 Android Access Modes

SysKit Android can operate with different levels of access:

| Mode       | Access                                   |
| ---------- | ---------------------------------------- |
| 📱 Normal  | Standard Android application permissions |
| 🔐 Shizuku | Elevated APIs through Shizuku            |
| 🔓 Root    | Root-level access where supported        |

This allows SysKit to provide different capabilities depending on the user's device configuration.

---

# 📱 Android vs Linux

| Feature                        |              Linux | Android |
| ------------------------------ | -----------------: | ------: |
| System information             |                  ✅ |       ✅ |
| Battery information            |                  ✅ |       ✅ |
| Storage information            |                  ✅ |       ✅ |
| Networking                     |                  ✅ |       ✅ |
| Native terminal implementation |                  ✅ |       ❌ |
| Native Android application     |                  ❌ |       ✅ |
| Shizuku                        |                  ❌ |       ✅ |
| Root-aware functionality       | Platform-dependent |       ✅ |
| Android APIs                   |                  ❌ |       ✅ |
| Termux support                 |                  — |       ✅ |

> ⚠️ This is a platform-level comparison. Individual SysKit features can have different availability depending on the implementation and permissions.

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

The Android implementation uses Android-specific APIs and components.

Additional elevated functionality can use:

* Shizuku
* Root access

> Not every dependency is required to run every version of SysKit.

---

# 🎛️ Main Menu

SysKit uses a simple numbered menu in its terminal implementations:

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

The Android version uses a native Android interface instead of the terminal menu.

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
* Android — current APK available

> ⚠️ **Never run Lite versions on real Linux.** They are designed for restricted environments such as Termux.

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

* [x] C implementation
* [x] C Lite implementation
* [x] Bash implementation
* [x] Bash Lite implementation
* [x] Python implementation
* [x] Python Lite implementation
* [x] Java implementation
* [x] Java Lite implementation
* [x] GTK3 implementation
* [x] Android implementation
* [ ] Improve error handling
* [ ] Improve dependency detection
* [x] Expand hardware support
* [x] Expand monitoring
* [x] Expand networking
* [ ] Improve storage diagnostics
* [x] Improve Lite versions
* [ ] Improve documentation
* [ ] Automated testing

## 📱 Android

* [x] Android project
* [x] Native Android APK
* [x] Kotlin implementation
* [x] Shizuku support
* [x] Root-aware functionality
* [x] Android shell functionality
* [x] Android release
* [x] Expand Android-specific features
* [x] Improve device compatibility
* [x] Expand Shizuku functionality
* [x] Expand root functionality
* [x] Improve Android UI
* [x] Add more Android diagnostics

The Android implementation is no longer a planned experiment — it is an actual released part of SysKit. The current release is `v1.0.1-apk`.

---

# 🌍 Compatibility

| Platform   | Status |
| ---------- | ------ |
| 🐧 Linux   | ✅      |
| 📱 Android | ✅      |
| 📱 Termux  | ✅      |
| 🍎 macOS   | 🧪     |
| 👻 BSD     | 🧪     |

---

# 🗂️ Project Structure

```text
syskit/
├── app/
│   └── Android application
│
├── gradle/
│
├── CHANGELOG.md
├── CONTRIBUTING.md
├── LICENSE
├── README.md
├── SECURITY.md
├── metadata.json
│
├── Syskit.desktop
│
├── Syskit.java
├── SyskitLite.java
│
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
│
├── syskit.py
├── syskit lite.py
│
├── syskit.c
├── syskit-lite.c
├── syskit-gui.c
│
├── syskit.sh
└── syskit-lite.sh
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
2. Required Android permissions
3. Whether Shizuku is running
4. Whether SysKit has the required Shizuku permission
5. Whether root access is available when required
6. Whether the functionality is supported on your device
7. Whether you are using the current `v1.0.1-apk` release

### A feature doesn't work

Check:

1. Required dependencies
2. Platform compatibility
3. Permissions
4. Hardware support
5. Whether the Lite version works
6. Whether Android functionality requires Shizuku or root

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
6. Test Android-specific changes on real devices where possible.

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

Android functionality using Shizuku or root can provide elevated system access.

**Use system-modifying and elevated-access features carefully.**

Feature availability varies between operating systems, implementations, devices, permissions, and dependencies.

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

### C · Bash · Python · Java · Kotlin

### Terminal · Lite · GTK3 · Android

> **One toolkit. Multiple implementations. Always improving.**

</div>
