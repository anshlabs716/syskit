# 🚀 SysKit

### Universal System Toolkit

<div align="center">

<img width="679" height="516" alt="Screenshot_20260819_175036" src="https://github.com/user-attachments/assets/6b63de81-e649-4135-9613-74187ceff7ef" /> full

<img width="424" height="521" alt="Screenshot_20260819_174953" src="https://github.com/user-attachments/assets/3d1f114e-558b-450f-98aa-7bae12118618" /> lite

<img width="1366" height="719" alt="Screenshot_20260819_180708" src="https://github.com/user-attachments/assets/3f267f50-3e09-458d-ae13-363b21683210" /> gui


<img width="424" height="1687" alt="image" src="https://github.com/user-attachments/assets/06b14e22-24b4-415a-916f-722140636304" /> apk


  

  

  

  

  

</div>

> **SysKit** is a multi-language system toolkit for information, diagnostics, maintenance, networking, storage, utilities, and more.

---

## 🧠 What is SysKit?

SysKit brings a collection of useful system tools into one menu-driven toolkit.

Instead of jumping between different commands for system information, networking, storage, maintenance, diagnostics, and utilities, SysKit puts them together in one place.

The project currently has implementations in **C, Bash, Python, and Java**, alongside a Linux GTK3 interface, Termux-focused Lite versions, and a native **Android APK**.

```text
                         🚀 SysKit
                            │
          ┌─────────────────┼─────────────────┐
          │                 │                 │
       Full CLI            Lite              GUI
          │                 │                 │
     ┌────┼────┐       ┌────┼────┐           │
     │    │    │       │    │    │          GTK3
     C  Bash Python    C  Bash Python
          │
         Java
          │
       Android APK
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
| 📦 **Archives**   | ZIP and TAR.GZ creation/extraction — **still in beta**       |
| 🛠️ **Utilities** | Passwords, random strings, hashes, UUIDs                     |
| 🌍 **Internet**   | Weather, time, calendar, connectivity                        |
| 💾 **Backup**     | Backup, restore, compression, verification                   |
| ⚙️ **Settings**   | Colors, emojis, animations, reset                            |
| ❓ **Help**        | Help, about, documentation, support                          |

> ⚠️ Feature availability depends on the implementation, operating system, installed dependencies, hardware, and permissions.

---

# 🧩 Implementations

SysKit isn't tied to a single language or interface.

| Version            | Language | File              | Environment                       |
| ------------------ | -------- | ----------------- | --------------------------------- |
| **SysKit**         | C        | `syskit.c`        | Full terminal version             |
| **SysKit Lite**    | C        | `syskit-lite.c`   | Lightweight environments          |
| **SysKit GUI**     | C / GTK3 | `syskit-gui.c`    | Linux desktop                     |
| **SysKit**         | Bash     | `syskit.sh`       | Unix-like systems                 |
| **SysKit Lite**    | Bash     | `syskit-lite.sh`  | Lightweight environments          |
| **SysKit**         | Python   | `syskit.py`       | Python environments               |
| **SysKit Lite**    | Python   | `syskit lite.py`  | Termux / lightweight environments |
| **SysKit**         | Java     | `Syskit.java`     | Java environments                 |
| **SysKit Lite**    | Java     | `SyskitLite.java` | Lightweight Java environments     |
| **SysKit Android** | Kotlin   | `app/`            | Android                           |

---

# 📱 Android

SysKit now has a **native Android application**.

The Android version is built as a proper Android project rather than simply packaging the existing Linux versions into an APK.

### Android capabilities

* [x] Native Android application
* [x] Android project structure
* [x] APK build
* [x] Android system toolkit interface
* [x] Shizuku support
* [x] Root support
* [x] Root-aware functionality
* [x] Permission-aware operations
* [x] Android system information
* [x] Android diagnostics
* [x] Android utilities
* [x] Android-focused system operations
* [x] Real-device testing
* [x] Android APK release
* [x] Android implementation integrated into SysKit

### 🔐 Shizuku & Root

SysKit Android supports elevated functionality through:

* **Shizuku** — for supported privileged operations without requiring root
* **Root** — for functionality requiring direct elevated access

The available functionality depends on the Android version, device, ROM, permissions, Shizuku availability, and whether the device is rooted.

> ⚠️ Root functionality can modify system-level resources. Use elevated features carefully.

---

# 🖥️ Platform Support

| Platform   | Status | Notes                             |
| ---------- | ------ | --------------------------------- |
| 🐧 Linux   | ✅      | Primary platform                  |
| 📱 Termux  | ✅      | Lite versions recommended         |
| 🤖 Android | ✅      | Native Android APK                |
| 🍎 macOS   | 🧪     | Shell compatibility needs testing |
| 👻 BSD     | 🧪     | Shell compatibility needs testing |

### Linux

Linux is the primary target for the desktop and terminal implementations.

The exact functionality depends on your distribution and installed system utilities.

### Termux

SysKit can run in Termux, with the Lite implementations being the recommended choice.

Android does not expose every traditional Linux interface, so some features may not be available through Termux.

### Android

The Android version is a dedicated native application.

It does **not** depend on simply running the Linux CLI inside an APK.

Android-specific functionality uses Android APIs and can use **Shizuku** or **root** where supported.

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

# 🤖 Android APK

The Android application is located inside the `app/` Android project directory.

### Build

Open the project in Android Studio or build it using Gradle.

```bash
./gradlew assembleDebug
```

The resulting APK can then be installed on a compatible Android device.

> 💡 The Android version is a separate native implementation and does not require the desktop/Linux implementations to run.

### Elevated access

For features that support elevated access:

**Shizuku**

* Install and start Shizuku on the Android device.
* Grant SysKit the required Shizuku permission.
* Supported operations can then use Shizuku.

**Root**

* On rooted devices, SysKit can use root access for supported operations.
* Root access must be granted through the device's root manager when requested.

> ⚠️ Available Android operations vary by device, Android version, ROM, permissions, Shizuku support, and root access.

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

The Android application uses Android-native APIs and components.

Supported privileged functionality may additionally use:

* Shizuku
* Root access

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
│ 12.  Internet              │
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

* [x] C — runs great
* [x] C Lite — super fast; **NEVER run this on real Linux**
* [x] Bash — works amazingly well on Linux
* [x] Bash Lite — works really well on Termux; **NEVER run this on real Linux**
* [x] Python — some features are a bit buggier than others
* [x] Python Lite — has some issues with device info but overall great; **NEVER run this on real Linux**
* [x] Java — works pretty well
* [x] Java Lite — not tested yet; **NEVER run this on real Linux**
* [x] GTK3 — works well but is slower compared to the other C versions
* [x] Android APK — released
* [x] Shizuku integration
* [x] Root integration

> ⚠️ **Never run Lite versions on real Linux.** They are designed for restricted environments such as Termux and can heavily modify their behavior on Linux, potentially causing crashes.

## Platform Testing

* [x] Debian
* [x] Ubuntu
* [x] Linux Mint
* [x] Fedora
* [x] Arch Linux
* [ ] Alpine
* [ ] openSUSE
* [ ] Void Linux
* [x] Termux
* [x] Android
* [ ] macOS
* [ ] BSD

## Feature Areas

* [x] System information
* [x] Monitoring
* [x] Networking
* [x] Power
* [x] Package management
* [x] Cleaning
* [x] Storage
* [x] Security
* [x] File utilities
* [x] Archives — **beta**
* [x] Utilities
* [x] Internet tools
* [x] Backup / restore — **beta**
* [x] Settings
* [x] Android system tools
* [x] Shizuku support
* [x] Root support

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
* [x] Shizuku support
* [x] Root support
* [x] APK release
* [ ] Improve error handling
* [ ] Improve dependency detection
* [ ] Expand hardware support
* [ ] Expand monitoring
* [x] Expand networking
* [ ] Improve storage diagnostics
* [ ] Improve Lite versions
* [ ] Improve documentation
* [ ] Automated testing

## 🌍 Compatibility

* [x] Linux
* [x] Termux
* [x] Android
* [x] Debian-based testing
* [x] Fedora-based testing
* [x] Arch-based testing
* [ ] Alpine testing
* [ ] openSUSE testing
* [ ] Void Linux testing
* [ ] macOS testing
* [ ] BSD testing

## 🧪 Reliability

* [x] Test every menu option
* [x] Test every implementation
* [x] Test multiple distributions
* [ ] Test low-resource environments
* [ ] Test archive functionality
* [ ] Test backup / restore
* [ ] Test missing dependencies
* [ ] Test permission handling
* [ ] Improve compatibility detection
* [ ] Expand Android device testing

## 📱 Android

* [x] Research Android APIs
* [x] Research Android architecture
* [x] Design Android UI
* [x] Port supported functionality
* [x] Investigate Shizuku
* [x] Investigate ADB
* [x] Investigate root-aware features
* [x] Build Android implementation
* [x] Test on real devices
* [x] Build SysKit APK
* [x] Release SysKit APK
* [ ] Expand Android functionality
* [ ] Expand device compatibility
* [ ] Add more Android-specific tools
* [ ] Improve privileged operations
* [ ] Improve Android testing

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
│   └── Android application
│
├── gradle/
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── metadata.json
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

### Android build fails

Make sure the Android project has the required Android SDK and Gradle components installed.

```bash
./gradlew assembleDebug
```

### Shizuku features don't work

Check:

1. Shizuku is installed and running.
2. SysKit has been granted Shizuku permission.
3. Your Android version supports the requested operation.
4. The specific feature supports Shizuku.

### Root features don't work

Check:

1. The device is actually rooted.
2. SysKit has been granted root access.
3. The requested operation requires root.
4. Your ROM or Android version does not restrict the operation.

### A feature doesn't work

Check:

1. Required dependencies
2. Platform compatibility
3. Permissions
4. Hardware support
5. Whether the Lite version works
6. Android/Shizuku/root availability when using the Android version

If the problem continues, open an issue with useful information about your environment.

---

# 🤝 Contributing

Contributions, testing, bug reports, ideas, and improvements are welcome.

Before contributing, read [`CONTRIBUTING.md`](CONTRIBUTING.md).

When submitting changes:

1. Keep changes focused.
2. Test what you changed.
3. Avoid breaking other implementations.
4. Document new functionality where appropriate.
5. Explain compatibility considerations.

---

# 🔐 Security

For security-related issues, see [`SECURITY.md`](SECURITY.md).

Please avoid publicly exposing sensitive security issues before they can be investigated.

---

# 📜 Changelog

See [`CHANGELOG.md`](CHANGELOG.md) for development history.

---

# 👥 Credits

### Weather Feature

Special thanks to [**@shozanthebozan**](https://github.com/shozanthebozan).

The SysKit weather feature is based on [**onNow**](https://github.com/shozanthebozan/onNow) by shozanthebozan.

Please check out the original project:

**🔗** [**shozanthebozan/onNow**](https://github.com/shozanthebozan/onNow)

> 💙 Credit goes to shozanthebozan for the original weather implementation that inspired the weather functionality in SysKit.

---

# ⚠️ Disclaimer

SysKit is intended for **system administration, diagnostics, maintenance, troubleshooting, learning, and personal use**.

Some operations can modify system files, packages, services, caches, or other system resources.

**Use system-modifying features carefully.**

Feature availability varies between operating systems and implementations.

---

# 📄 License

SysKit is distributed under the license included in [`LICENSE`](LICENSE).

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

## 🚀 SysKit

**C · Bash · Python · Java · Kotlin**

**Terminal · Lite · GTK3 · Android**

> **One toolkit. Multiple implementations. Always improving.**
