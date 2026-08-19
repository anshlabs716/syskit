# 🚀 SysKit

### Universal System Toolkit

[![C](https://img.shields.io/badge/C-31.3%25-A8B9CC?style=for-the-badge\&logo=c\&logoColor=white)](https://github.com/anshlabs716/syskit)
[![Java](https://img.shields.io/badge/Java-26.1%25-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)](https://github.com/anshlabs716/syskit)
[![Shell](https://img.shields.io/badge/Shell-22.9%25-89E051?style=for-the-badge\&logo=gnubash\&logoColor=white)](https://github.com/anshlabs716/syskit)
[![Python](https://img.shields.io/badge/Python-19.7%25-3776AB?style=for-the-badge\&logo=python\&logoColor=white)](https://github.com/anshlabs716/syskit)

[![Linux](https://img.shields.io/badge/Linux-Supported-FCC624?style=for-the-badge\&logo=linux\&logoColor=black)](https://github.com/anshlabs716/syskit)
[![Termux](https://img.shields.io/badge/Termux-Supported-000000?style=for-the-badge\&logo=termux\&logoColor=white)](https://github.com/anshlabs716/syskit)
[![GTK3](https://img.shields.io/badge/GTK3-GUI-7FE719?style=for-the-badge\&logo=gtk\&logoColor=black)](https://github.com/anshlabs716/syskit)
[![Open Source](https://img.shields.io/badge/Open%20Source-Yes-3DA639?style=for-the-badge\&logo=opensourceinitiative\&logoColor=white)](https://github.com/anshlabs716/syskit)

> **SysKit** is an all-in-one toolkit for system information, diagnostics, maintenance, networking, storage, security, utilities, and more.
>
> Available in **C, Bash, Python, and Java**, with Lite versions for more restricted environments and a GTK3 GUI for Linux.

---

## ✨ What is SysKit?

SysKit brings commonly used system tools together into one organized interface.

Instead of remembering dozens of commands, SysKit gives you a single place to access system information, monitoring, networking, storage tools, maintenance utilities, diagnostics, and more.

The project is built around **multiple implementations**, allowing SysKit to work across different environments rather than being locked to one language or platform.

```text
                 ┌──────────────────────┐
                 │       🚀 SysKit      │
                 └──────────┬───────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
     Terminal             Lite                 GUI
        │                   │                   │
   ┌────┼────┐        ┌─────┼─────┐          GTK3
   │    │    │        │     │     │
   C  Bash  Python    C   Bash  Python
            │
          Java
```

---

## 🧰 Features

### 🖥️ System Information

* OS information
* Kernel information
* CPU information
* GPU information
* RAM information
* Storage information
* Hardware information
* Uptime
* Environment information
* Fastfetch integration

### 📊 Monitoring

* CPU usage
* RAM usage
* Disk usage
* Running processes
* Network information
* Temperature information
* System status

### 🌐 Networking

* Ping
* Internet connectivity checks
* Local IP
* Public IP
* DNS information
* Gateway information
* Wi-Fi information
* Basic network diagnostics

### 🔋 Power

* Battery information
* Battery percentage
* Charging status
* Battery health
* Power information

### 📦 Package Management

Where supported by the host system:

* Update packages
* Upgrade packages
* Search packages
* View installed packages
* Cleanup
* Autoremove

### 🧹 System Cleaning

* Cache cleanup
* Temporary-file cleanup
* Log cleanup
* Trash cleanup
* Package-cache cleanup
* General maintenance

> ⚠️ Some cleaning operations can modify system files and may require elevated permissions.

### 💾 Storage

* Disk usage
* Mounted drives
* Directory sizes
* Large-file detection
* Storage information
* SMART information where supported

### 🔐 Security & Diagnostics

* Firewall status
* Open-port checks
* SSH information
* Service information
* Basic security checks
* System diagnostics

### 📁 File Utilities

* File searching
* Text searching
* Directory trees
* File statistics
* Duplicate detection
* Directory information

### 📦 Archives

* ZIP creation
* ZIP extraction
* TAR.GZ creation
* TAR.GZ extraction

> 🧪 Archive functionality is still being tested across different environments.

### 🛠️ Utilities

* Password generation
* Random strings
* MD5
* SHA1
* SHA256
* SHA512
* UUID generation

### 🌍 Internet Utilities

* Weather
* Time
* Calendar
* Connectivity checks

### 💾 Backup

* Backup creation
* Backup restoration
* Compression
* Verification

### ⚙️ Settings

* Colors
* Emojis
* Animations
* Reset settings

### ❓ Help

* Help menu
* About SysKit
* Documentation
* Support information

---

## 🧩 Implementations

| Implementation  | Language | File              | Purpose                             |
| --------------- | -------- | ----------------- | ----------------------------------- |
| **SysKit**      | C        | `syskit.c`        | Full native terminal version        |
| **SysKit Lite** | C        | `syskit-lite.c`   | Lightweight C version               |
| **SysKit GUI**  | C / GTK3 | `syskit-gui.c`    | Linux graphical interface           |
| **SysKit**      | Bash     | `syskit.sh`       | Shell implementation                |
| **SysKit Lite** | Bash     | `syskit-lite.sh`  | Lightweight shell version           |
| **SysKit**      | Python   | `syskit.py`       | Python terminal version             |
| **SysKit Lite** | Python   | `syskit lite.py`  | Lightweight Python / Termux version |
| **SysKit**      | Java     | `Syskit.java`     | Java terminal version               |
| **SysKit Lite** | Java     | `SyskitLite.java` | Lightweight Java version            |

---

# 🖥️ Platform Support

| Platform           |    Status   | Recommended Version |
| ------------------ | :---------: | ------------------- |
| 🐧 Linux           | ✅ Supported | Full SysKit         |
| 📱 Termux          | ✅ Supported | Lite versions       |
| 🍎 macOS           |  🧪 Testing | Bash / Lite         |
| 👻 BSD             |  🧪 Testing | Bash / Lite         |
| 💻 Low-RAM systems | ✅ Supported | Lite versions       |

### 🐧 Linux

Linux is the primary target for SysKit.

The exact feature set depends on your distribution, installed utilities, hardware, and permissions.

### 📱 Termux

Termux is supported, with the **Lite implementations** recommended.

Android/Termux does not expose every Linux utility or system interface, so some full SysKit features may not be available.

### 🍎 macOS & BSD

Shell compatibility is a goal, but these platforms still require additional testing.

---

# 📥 Installation

## 1. Clone SysKit

```bash
git clone https://github.com/anshlabs716/syskit.git
cd syskit
```

---

# 🐚 Bash

### Full Version

```bash
chmod +x syskit.sh
./syskit.sh
```

### Lite Version

```bash
chmod +x syskit-lite.sh
./syskit-lite.sh
```

---

# 🐍 Python

### Full Version

```bash
python3 syskit.py
```

### Lite Version

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

# ☕ Java

Java versions require a JDK.

Check your installation:

```bash
java --version
javac --version
```

### Full Version

```bash
javac Syskit.java
java Syskit
```

### Lite Version

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

# 🦾 C

### Full Version

```bash
gcc syskit.c -o syskit
./syskit
```

### Lite Version

```bash
gcc syskit-lite.c -o syskit-lite
./syskit-lite
```

### Using Clang

```bash
clang syskit.c -o syskit
./syskit
```

---

# 🖥️ GTK3 GUI

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

Install the core tools:

```bash
pkg update
pkg upgrade
pkg install git python clang
```

Clone the repository:

```bash
git clone https://github.com/anshlabs716/syskit.git
cd syskit
```

### 🐍 Python Lite

```bash
python "syskit lite.py"
```

### 🦾 C Lite

```bash
clang syskit-lite.c -o syskit-lite
./syskit-lite
```

### ☕ Java Lite

If a suitable JDK is available:

```bash
javac SyskitLite.java
java SyskitLite
```

> 💡 **Tip:** Start with a Lite version on Termux. Full versions may depend on Linux utilities that Android does not provide.

---

# 📦 Dependencies

SysKit does **not** require every dependency for every implementation.

Install only what your chosen implementation and features need.

## Core

* Bash
* GCC or Clang
* Python 3
* Java JDK
* Git

## Common Utilities

* `curl`
* `wget`
* `jq`
* `fastfetch`
* `tar`
* `zip`
* `unzip`
* `tree`

## Hardware & System Tools

Some features may use:

* `lshw`
* `dmidecode`
* `smartctl`
* `sensors`
* `lspci`
* `uuidgen`

## Networking

Depending on your platform:

* `ping`
* `nmcli`
* DNS utilities
* NetworkManager

> ⚠️ Dependency availability varies between distributions. If a feature reports that a command is missing, install the corresponding package for your operating system.

---

# 🧭 Main Menu

SysKit organizes its tools into a simple menu:

```text
╭────────────────────────────╮
│          🚀 SysKit         │
├────────────────────────────┤
│  1. System                │
│  2. Monitoring            │
│  3. Network               │
│  4. Power                 │
│  5. Packages              │
│  6. Cleaner               │
│  7. Storage               │
│  8. Security              │
│  9. Files                 │
│ 10. Archives              │
│ 11. Utilities             │
│ 12. Internet              │
│ 13. Backup                │
│ 14. Settings              │
│ 15. Help                  │
│ 16. Exit                  │
╰────────────────────────────╯
```

---

# 🧪 Testing

SysKit has several implementations, so compatibility testing is an important part of development.

## Current Testing Checklist

* [ ] Test all C features
* [ ] Test all Bash features
* [ ] Test all Python features
* [ ] Test all Java features
* [ ] Test Lite implementations
* [ ] Test GTK3 GUI
* [ ] Test Termux
* [ ] Test package management
* [ ] Test cleaning functions
* [ ] Test backup and restore
* [ ] Test archive creation
* [ ] Test archive extraction
* [ ] Test hardware detection
* [ ] Test network tools
* [ ] Test storage tools
* [ ] Test security checks
* [ ] Test error handling
* [ ] Test missing dependencies
* [ ] Test permission handling

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
* [ ] Improve error handling
* [ ] Improve compatibility detection
* [ ] Expand hardware detection
* [ ] Expand monitoring
* [ ] Expand networking
* [ ] Expand storage diagnostics
* [ ] Improve Lite implementations
* [ ] Improve documentation
* [ ] Add automated tests

## 🌍 Cross-Platform

* [x] Linux support
* [x] Termux support
* [ ] Test Debian-based distributions
* [ ] Test Fedora-based distributions
* [ ] Test Arch-based distributions
* [ ] Test Alpine
* [ ] Test openSUSE
* [ ] Test Void Linux
* [ ] Test macOS
* [ ] Test BSD
* [ ] Improve platform-specific compatibility

## 🧪 Reliability

* [ ] Test every menu option
* [ ] Test every implementation
* [ ] Test multiple Linux distributions
* [ ] Test low-resource systems
* [ ] Fully test archive functionality
* [ ] Fully test backup / restore
* [ ] Improve dependency detection
* [ ] Improve failure messages
* [ ] Add automated regression testing

## 📱 Android

A possible future direction is turning SysKit into a proper Android application.

* [ ] Research Android system APIs
* [ ] Research Java/Kotlin Android architecture
* [ ] Design Android UI
* [ ] Port supported SysKit functionality
* [ ] Investigate Shizuku integration
* [ ] Investigate ADB integration
* [ ] Investigate root-aware features
* [ ] Build Android prototype
* [ ] Test on real devices
* [ ] **Maybe build a full SysKit APK**
* [ ] **Maybe don't — depends on complexity and Android restrictions**

> 🚧 The Android version would not simply execute the existing Linux code. Android has different permissions, APIs, and system interfaces, so supported features would need Android-native implementations.

---

# 🗂️ Project Structure

```text
syskit/
├── .gitignore
├── .gitignore (C)
├── .gitignore (java)
├── .gitignore (py)
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
└── syskit-lite.sh
```

---

# 🛠️ Troubleshooting

## `Permission denied`

Make the script executable:

```bash
chmod +x syskit.sh
```

Then:

```bash
./syskit.sh
```

Some system operations may require elevated permissions:

```bash
sudo ./syskit.sh
```

> ⚠️ Only use `sudo` when the operation actually requires elevated permissions.

---

## `command not found`

A required dependency may be missing.

Check the dependency list above and install the missing tool using your distribution's package manager.

---

## C compilation fails

Check your compiler:

```bash
gcc --version
```

or:

```bash
clang --version
```

---

## Java compilation fails

Check both Java and `javac`:

```bash
java --version
javac --version
```

---

## GTK3 compilation fails

Check GTK3:

```bash
pkg-config --modversion gtk+-3.0
```

If it cannot find GTK3, install the appropriate development package for your distribution.

---

## A feature does not work

SysKit features can depend on:

* Operating system
* Distribution
* Installed utilities
* Hardware
* Permissions
* Network availability
* Android/Termux restrictions

Try the following:

1. Check the required dependency.
2. Check whether your platform supports the feature.
3. Try the Lite implementation.
4. Reproduce the issue.
5. Open an issue with useful system information.

---

# 🤝 Contributing

Contributions, fixes, testing, and ideas are welcome.

Before contributing:

1. Read [`CONTRIBUTING.md`](CONTRIBUTING.md).
2. Create a branch for your changes.
3. Keep changes focused.
4. Test your changes.
5. Avoid breaking existing implementations.
6. Document new functionality where appropriate.
7. Open a pull request.

---

# 🔐 Security

If you discover a security issue, please follow the instructions in [`SECURITY.md`](SECURITY.md).

Please avoid publicly exposing sensitive security issues before they can be investigated.

---

# 📜 Changelog

Development history is available in [`CHANGELOG.md`](CHANGELOG.md).

---

# ⚠️ Disclaimer

SysKit is intended for:

* System administration
* Diagnostics
* Maintenance
* Troubleshooting
* Learning
* Personal use

Some features can modify system resources, packages, files, services, caches, or other system settings.

**Use system-modifying features carefully.**

Not every feature is supported on every operating system.

---

# 📄 License

SysKit is distributed under the license included in [`LICENSE`](LICENSE).

---

# 💡 Philosophy

SysKit follows a simple development loop:

```text
       💡 Idea
          ↓
       🔨 Build
          ↓
       💥 Break
          ↓
     🔎 Investigate
          ↓
       🔧 Fix
          ↓
      🚀 Improve
          ↓
       🧠 Learn
          │
          └──────────────↻
```

> **Build it. Break it. Understand it. Improve it.**

---

# 🚀 SysKit

**C · Bash · Python · Java**

**Terminal · Lite · GTK3 · Maybe Android**

> One toolkit. Multiple implementations. One project that keeps growing.
