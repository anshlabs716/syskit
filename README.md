<div align="center">

# 🚀 SysKit

### Universal System Toolkit

<p>
<img src="https://img.shields.io/badge/C-31.3%25-A8B9CC?style=for-the-badge&logo=c&logoColor=white" alt="C">&nbsp;&nbsp;&nbsp;
<img src="https://img.shields.io/badge/Java-26.1%25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">&nbsp;&nbsp;&nbsp;
<img src="https://img.shields.io/badge/Shell-22.9%25-89E051?style=for-the-badge&logo=gnubash&logoColor=white" alt="Shell">&nbsp;&nbsp;&nbsp;
<img src="https://img.shields.io/badge/Python-19.7%25-3776AB?style=for-the-badge&logo=python&logoColor=white" alt="Python">&nbsp;&nbsp;&nbsp;
<img src="https://img.shields.io/badge/Linux-Supported-FCC624?style=for-the-badge&logo=linux&logoColor=black" alt="Linux">&nbsp;&nbsp;&nbsp;
<img src="https://img.shields.io/badge/Termux-Supported-000000?style=for-the-badge&logo=termux&logoColor=white" alt="Termux">
</p>

<p>
<img src="https://img.shields.io/github/v/release/anshlabs716/syskit?style=for-the-badge&label=Latest%20Release" alt="Latest Release">&nbsp;&nbsp;&nbsp;
<img src="https://img.shields.io/github/license/anshlabs716/syskit?style=for-the-badge" alt="License">&nbsp;&nbsp;&nbsp;
<img src="https://img.shields.io/github/stars/anshlabs716/syskit?style=for-the-badge" alt="Stars">&nbsp;&nbsp;&nbsp;
<img src="https://img.shields.io/github/issues/anshlabs716/syskit?style=for-the-badge" alt="Issues">&nbsp;&nbsp;&nbsp;
<img src="https://img.shields.io/github/last-commit/anshlabs716/syskit?style=for-the-badge" alt="Last Commit">
</p>

<br>

> **SysKit** is a multi-language system toolkit for information, diagnostics, maintenance, networking, storage, utilities, and more.

</div>

---

## 🧠 What is SysKit?

SysKit brings a collection of useful system tools into one menu-driven toolkit.

Instead of jumping between different commands for system information, networking, storage, maintenance, diagnostics, and utilities, SysKit puts them together in one place.

The project includes implementations in **C, Bash, Python, and Java**, alongside a dedicated **Android APK**.

SysKit is designed to provide useful functionality across **Linux, Termux, and Android**.

---

# 📱 Android

SysKit now includes a dedicated **Android APK**.

The Android version is built specifically for Android rather than simply packaging the existing Linux versions into an APK.

### Android Features

* [x] 📱 Native Android APK
* [x] 🧩 Android-native implementation
* [x] 🔧 Shizuku support
* [x] 🔓 Root support
* [x] 🔌 ADB-related functionality
* [x] 🛠️ System tools
* [x] 📊 Device information
* [x] 🔋 Battery information
* [x] 💾 Storage information
* [x] 🌐 Network information
* [x] ⚙️ Android system utilities
* [x] 📦 Android package/app tools
* [x] 🧹 Maintenance utilities
* [x] 🔐 Permission-aware functionality
* [x] 📱 Designed for modern Android devices

### Android Access Levels

| Access                        | Support |
| ----------------------------- | ------- |
| 📱 Normal Android permissions | ✅       |
| 🔌 ADB                        | ✅       |
| 🧩 Shizuku                    | ✅       |
| 🔓 Root                       | ✅       |

> ⚠️ Some Android functionality requires Shizuku or root. Features available through normal Android permissions may work without elevated access.

---

# 📥 Android Installation

The Android APK is available through the project's releases.

### Latest APK

**v1.0.1-apk**

### Requirements

* Android device
* Compatible Android version
* Additional permissions depending on the feature
* Shizuku for Shizuku-powered functionality
* Root access for root-only functionality

---

# 🖥️ Implementations

SysKit isn't tied to a single language.

| Version            | Language         | File              | Environment                       |
| ------------------ | ---------------- | ----------------- | --------------------------------- |
| **SysKit**         | C                | `syskit.c`        | Full terminal version             |
| **SysKit Lite**    | C                | `syskit-lite.c`   | Lightweight environments          |
| **SysKit GUI**     | C / GTK3         | `syskit-gui.c`    | Linux desktop                     |
| **SysKit**         | Bash             | `syskit.sh`       | Unix-like systems                 |
| **SysKit Lite**    | Bash             | `syskit-lite.sh`  | Lightweight environments          |
| **SysKit**         | Python           | `syskit.py`       | Python environments               |
| **SysKit Lite**    | Python           | `syskit lite.py`  | Termux / lightweight environments |
| **SysKit**         | Java             | `Syskit.java`     | Java environments                 |
| **SysKit Lite**    | Java             | `SyskitLite.java` | Lightweight Java environments     |
| **SysKit Android** | Kotlin / Android | `app/`            | Android                           |

---

# ✨ Features

| Category          | What SysKit provides                                                           |
| ----------------- | ------------------------------------------------------------------------------ |
| 🖥️ **System**    | OS, kernel, CPU, GPU, RAM, hardware, uptime, environment                       |
| 📊 **Monitoring** | CPU, RAM, disk, processes, network, temperature                                |
| 🌐 **Network**    | Ping, connectivity, IP, DNS, gateway, Wi-Fi                                    |
| 🔋 **Power**      | Battery, charging, health, power information                                   |
| 📦 **Packages**   | Updates, upgrades, search, installed packages, cleanup                         |
| 🧹 **Cleaner**    | Cache, temporary files, logs, trash, package cache                             |
| 💾 **Storage**    | Disk usage, mounted drives, directories, large files, SMART                    |
| 🔐 **Security**   | Firewall, open ports, SSH, services, basic checks                              |
| 📁 **Files**      | Search, text search, directory trees, statistics, duplicates                   |
| 📦 **Archives**   | ZIP and TAR.GZ creation/extraction                                             |
| 🛠️ **Utilities** | Passwords, random strings, hashes, UUIDs                                       |
| 🌍 **Internet**   | Weather, time, calendar, connectivity                                          |
| 💾 **Backup**     | Backup, restore, compression, verification                                     |
| ⚙️ **Settings**   | Colors, emojis, animations, reset                                              |
| ❓ **Help**        | Help, about, documentation, support                                            |
| 📱 **Android**    | Device information, system utilities, storage, battery, network, package tools |

> ⚠️ Feature availability depends on the implementation, operating system, installed dependencies, hardware, and permissions.

---

# 🖼️ Screenshots

## Full

<img src="https://github.com/user-attachments/assets/69f077f3-5705-4ca7-a308-216f86dcb942" alt="SysKit Full" width="800">

## Lite

<img src="https://github.com/user-attachments/assets/a4a79df9-b15e-45fa-a63a-c5fab11d0af1" alt="SysKit Lite" width="800">

## GUI

<img src="https://github.com/user-attachments/assets/fbb6fadd-f682-4b84-87bb-197a16a7c97b" alt="SysKit GUI" width="800">

---

# 🐧 Linux

Linux is the primary target for SysKit.

SysKit supports system information, diagnostics, networking, storage, maintenance, utilities, and more across multiple Linux implementations.

---

# 📱 Termux

SysKit can run in Termux, with the Lite implementations being the recommended choice.

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

> 💡 **Recommended:** Start with a Lite implementation on Termux.

---

# 📥 Installation

## Clone

```bash
git clone https://github.com/anshlabs716/syskit.git
cd syskit
```

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

## 🐍 Python

### Full

```bash
python3 syskit.py
```

### Lite

```bash
python3 "syskit lite.py"
```

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

# 🎛️ Main Menu

SysKit uses a simple numbered menu:

```text
╭────────────────────────────╮
│          🚀 SysKit         │
├────────────────────────────┤
│  1.  System                │
│  2.  Monitoring            │
│  3.  Network               │
│  4.  Power                 │
│  5.  Packages              │
│  6.  Cleaner               │
│  7.  Storage               │
│  8.  Security              │
│  9.  Files                 │
│ 10.  Archives              │
│ 11.  Utilities             │
│ 12.  Internet              │
│ 13.  Backup                │
│ 14.  Settings              │
│ 15.  Help                  │
│ 16.  Exit                  │
╰────────────────────────────╯
```

---

# 🧪 Testing

SysKit has multiple implementations, so compatibility matters.

### Feature Testing

* [x] C — runs great
* [x] C Lite — super fast
* [x] Bash — works amazingly well on Linux
* [x] Bash Lite — works really well on Termux
* [x] Python — functional
* [x] Python Lite — functional with some device-info limitations
* [x] Java — works pretty well
* [x] Java Lite
* [x] GTK3 — functional
* [x] Android APK — released
* [x] Android Shizuku integration
* [x] Android root integration

> ⚠️ **Never run Lite versions on real Linux.** They are designed for restricted environments such as Termux.

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
* [x] APK release
* [x] Shizuku support
* [x] Root support
* [ ] Improve error handling
* [ ] Improve dependency detection
* [x] Expand hardware support
* [x] Expand monitoring
* [x] Expand networking
* [ ] Improve storage diagnostics
* [x] Improve Lite versions
* [ ] Improve documentation
* [ ] Automated testing

---

# 📱 Android

The Android version is now an active part of SysKit.

### Android Development

* [x] Research Android APIs
* [x] Research Android architecture
* [x] Design Android UI
* [x] Port supported functionality
* [x] Investigate Shizuku
* [x] Investigate ADB
* [x] Investigate root-aware features
* [x] Build Android implementation
* [x] Build APK
* [x] Test on real Android devices
* [x] Add Shizuku support
* [x] Add root support
* [x] Release Android APK

### Android Goals

* [x] Android-native implementation
* [x] System information
* [x] Device information
* [x] Storage information
* [x] Battery information
* [x] Network information
* [x] Android utilities
* [x] Elevated-access support
* [x] APK distribution
* [x] Expand Android functionality
* [x] Improve device compatibility
* [x] Add more Android-specific tools
* [x] Expand non-root functionality
* [x] Expand Shizuku functionality
* [x] Expand root functionality

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
│
├── Syskit.desktop
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

### Android feature doesn't work

Check:

1. Android version
2. Required permissions
3. Whether Shizuku is required
4. Whether root is required
5. Whether the feature is supported on your device
6. Whether the relevant Android service is available

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

---

# 🔐 Security

For security-related issues, see [`SECURITY.md`](https://github.com/anshlabs716/syskit/blob/main/SECURITY.md).

Please avoid publicly exposing sensitive security issues before they can be investigated.

---

# 📜 Changelog

See [`CHANGELOG.md`](https://github.com/anshlabs716/syskit/blob/main/CHANGELOG.md) for development history.

---

# 👥 Credits

### Weather Feature

Special thanks to [**@shozanthebozan**](https://github.com/shozanthebozan).

The SysKit weather feature is based on [**onNow**](https://github.com/shozanthebozan/onNow) by shozanthebozan.

Please check out the original project:

**🔗** [**shozanthebozan/onNow**](https://github.com/shozanthebozan/onNow)

---

# ⚠️ Disclaimer

SysKit is intended for **system administration, diagnostics, maintenance, troubleshooting, learning, and personal use**.

Some operations can modify system files, packages, services, caches, or other system resources.

**Use system-modifying features carefully.**

Feature availability varies between operating systems and implementations.

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

### 🚀 SysKit

**C · Bash · Python · Java · Android**

**Terminal · Lite · GTK3 · Android APK**

> **One toolkit. Multiple implementations. Always improving.**

</div>
