# 🚀 SysKit

### Universal System Toolkit

[![C](https://img.shields.io/badge/C-31.3%25-A8B9CC?style=for-the-badge\&logo=c\&logoColor=white)](https://github.com/anshlabs716/syskit)
[![Java](https://img.shields.io/badge/Java-26.1%25-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)](https://github.com/anshlabs716/syskit)
[![Shell](https://img.shields.io/badge/Shell-22.9%25-89E051?style=for-the-badge\&logo=gnubash\&logoColor=white)](https://github.com/anshlabs716/syskit)
[![Python](https://img.shields.io/badge/Python-19.7%25-3776AB?style=for-the-badge\&logo=python\&logoColor=white)](https://github.com/anshlabs716/syskit)
[![Linux](https://img.shields.io/badge/Linux-Supported-FCC624?style=for-the-badge\&logo=linux\&logoColor=black)](https://github.com/anshlabs716/syskit)
[![Termux](https://img.shields.io/badge/Termux-Supported-000000?style=for-the-badge\&logo=termux\&logoColor=white)](https://github.com/anshlabs716/syskit)

> **SysKit** is a multi-language system toolkit for information, diagnostics, maintenance, networking, storage, utilities, and more.

---

## 🧠 What is SysKit?

SysKit brings a collection of useful system tools into one menu-driven toolkit.

Instead of jumping between different commands for system information, networking, storage, maintenance, and diagnostics, SysKit puts them together in one place.

The project currently has implementations in **C, Bash, Python, and Java**, with lightweight variants designed for more restricted environments.


<img width="679" height="516" alt="Screenshot_20260819_175036" src="https://github.com/user-attachments/assets/1be2c14c-7db4-4a24-baa5-d1c3c6d74ca5" /> 
this is full
<img width="424" height="521" alt="Screenshot_20260819_174953" src="https://github.com/user-attachments/assets/4caddf1b-2079-42c5-934e-59d530b1cc81" />
this is lite 
<img width="1366" height="719" alt="Screenshot_20260819_180708" src="https://github.com/user-attachments/assets/5c6c45ee-8565-47d2-a9be-353ef5a19f28" />
this is the gui








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
```

---

## ✨ Features

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

SysKit isn't tied to a single language.

| Version         | Language | File              | Environment                       |
| --------------- | -------- | ----------------- | --------------------------------- |
| **SysKit**      | C        | `syskit.c`        | Full terminal version             |
| **SysKit Lite** | C        | `syskit-lite.c`   | Lightweight environments          |
| **SysKit GUI**  | C / GTK3 | `syskit-gui.c`    | Linux desktop                     |
| **SysKit**      | Bash     | `syskit.sh`       | Unix-like systems                 |
| **SysKit Lite** | Bash     | `syskit-lite.sh`  | Lightweight environments          |
| **SysKit**      | Python   | `syskit.py`       | Python environments               |
| **SysKit Lite** | Python   | `syskit lite.py`  | Termux / lightweight environments |
| **SysKit**      | Java     | `Syskit.java`     | Java environments                 |
| **SysKit Lite** | Java     | `SyskitLite.java` | Lightweight Java environments     |

---

# 🖥️ Platform Support

| Platform  | Status | Notes                             |
| --------- | :----: | --------------------------------- |
| 🐧 Linux  |    ✅   | Primary platform                  |
| 📱 Termux |    ✅   | Lite versions recommended         |
| 🍎 macOS  |   🧪   | Shell compatibility needs testing |
| 👻 BSD    |   🧪   | Shell compatibility needs testing |

### Linux

Linux is the primary target for SysKit.

The exact functionality depends on your distribution and installed system utilities.

### Termux

SysKit can run in Termux, with the Lite implementations being the recommended choice.

Android does not expose every traditional Linux interface, so some features may not be available.

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

### Python

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

* [x] C (runs great)
* [x] C Lite (super fast NEVER run this on real linux)
* [x] Bash (works amazingly good on linux)
* [x] Bash Lite (works really well on termux NEVER run this on real linux)
* [x] Python (some features are a bit more buggier than others)
* [ ] Python Lite (not tested yet NEVER run this on real linux
* [x] Java (works pretty well)
* [ ] Java Lite (not tested yet NEVER run this on real linux)
* [X] GTK3 (works well but is slower compared to the other C versions)
 NEVER run lite versions on real linux but u can do vice versa on termux. (Because on linux the lites tweak the hell out and crash)
## Platform Testing

* [ ] Debian
* [ ] Ubuntu
* [ ] Linux Mint
* [x] Fedora
* [x] Arch Linux
* [ ] Alpine
* [ ] openSUSE
* [ ] Void Linux
* [x] Termux
* [ ] macOS
* [ ] BSD

## Feature Areas

* [x] System information
* [x] Monitoring
* [x] Networking
* [x] Power
* [ ] Package management
* [ ] Cleaning
* [x] Storage
* [x] Security
* [x] File utilities
* [ ] Archives
* [x] Utilities
* [x] Internet tools
* [ ] Backup / restore
* [x] Settings

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
* [ ] Improve dependency detection
* [ ] Expand hardware support
* [ ] Expand monitoring
* [ ] Expand networking
* [ ] Improve storage diagnostics
* [X] Improve Lite versions
* [ ] Improve documentation
* [ ] Automated testing
* [ ] maybe a apk soon?
## 🌍 Compatibility

* [x] Linux
* [x] Termux
* [ ] Debian-based testing
* [ ] Fedora-based testing
* [x] Arch-based testing
* [ ] Alpine testing
* [ ] openSUSE testing
* [ ] Void Linux testing
* [ ] macOS testing
* [ ] BSD testing

## 🧪 Reliability

* [x] Test every menu option
* [x] Test every implementation
* [ ] Test multiple distributions
* [x] Test low-resource environments
* [ ] Test archive functionality
* [ ] Test backup / restore
* [ ] Test missing dependencies
* [ ] Test permission handling
* [ ] Improve compatibility detection

## 📱 Android

Android is a **possible future direction**, not a completed feature.

* [ ] Research Android APIs
* [ ] Research Android architecture
* [ ] Design Android UI
* [ ] Port supported functionality
* [ ] Investigate Shizuku
* [ ] Investigate ADB
* [ ] Investigate root-aware features
* [ ] Build prototype
* [ ] Test on real devices
* [ ] **Maybe build a SysKit APK**
* [ ] **Decide whether a full Android version is practical**

> Android would require an Android-native implementation rather than simply packaging the existing Linux code into an APK.

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

### A feature doesn't work

Check:

1. Required dependencies
2. Platform compatibility
3. Permissions
4. Hardware support
5. Whether the Lite version works

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

**C · Bash · Python · Java**

**Terminal · Lite · GTK3 · Maybe Android**

> **One toolkit. Multiple implementations. Always improving.**
