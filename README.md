# 🚀 SysKit

### Universal System Toolkit

**A powerful multi-language toolkit for system information, monitoring, maintenance, diagnostics, networking, storage, utilities, and more.**

SysKit brings useful system tools together into one organized interface across Linux, Termux, Unix-like systems, and other supported environments.

<p align="center">

![C](https://img.shields.io/badge/C-00599C?style=for-the-badge&logo=c&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![Bash](https://img.shields.io/badge/Bash-4EAA25?style=for-the-badge&logo=gnubash&logoColor=white)
![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black)
![Termux](https://img.shields.io/badge/Termux-000000?style=for-the-badge&logo=termux&logoColor=white)

</p>

---

## ⚡ About

**SysKit** is an all-in-one system toolkit built with **C, Bash, Python, and Java**.

It provides system information, monitoring, networking, storage utilities, package management, maintenance tools, file utilities, security checks, backups, archives, and other useful system functions.

SysKit includes multiple implementations so users can choose the version that best fits their environment.

> **One toolkit. Multiple implementations. Multiple environments.**

---

# ✨ Features

## 🖥️ System Information

- System information
- CPU information
- GPU information
- RAM information
- Storage information
- Kernel information
- Hardware information
- Uptime
- Environment information
- Fastfetch integration

## 📊 Monitoring

- CPU usage
- RAM usage
- Disk usage
- Running processes
- Network information
- Temperature information

## 🌐 Networking

- Ping
- Internet connectivity checks
- Local IP
- Public IP
- DNS information
- Gateway information
- Wi-Fi information

## 🔋 Power

- Battery information
- Battery percentage
- Charging status
- Battery health
- Power information

> Battery functionality depends on the operating system and hardware.

## 📦 Package Management

Depending on the operating system:

- Package updates
- Package upgrades
- Package cleanup
- Autoremove
- Package searching
- Installed package information

Package-manager functionality is platform-specific.

## 🧹 System Cleaning

- Cache cleanup
- Temporary file cleanup
- Log cleanup
- Trash cleanup
- Package cache cleanup
- System maintenance

> Some cleaning operations may require administrator/root privileges.

## 💾 Storage

- Disk usage
- Mounted drives
- Directory sizes
- Large file detection
- Storage information
- SMART information

> SMART and hardware-level storage information may require additional permissions and dependencies.

## 🔐 Security

- Firewall information
- Open ports
- SSH information
- Running services
- Basic security checks

> Security features are intended for system information and defensive diagnostics.

## 📂 File Utilities

- File searching
- Text searching
- Directory trees
- File statistics
- Duplicate detection

## 🗜️ Archives

- ZIP creation
- ZIP extraction
- TAR.GZ creation
- TAR.GZ extraction

> ⚠️ Archive functionality still needs additional testing across platforms.

## 🔑 Utilities

- Password generation
- Random string generation
- MD5
- SHA1
- SHA256
- SHA512
- UUID generation

## 🌍 Internet Utilities

- Weather
- Time
- Calendar
- Connectivity tools

## 💾 Backup

- Backup
- Restore
- Compression
- Backup verification

> Backup functionality may depend on available system tools and permissions.

## ⚙️ Settings

- Colors
- Emojis
- Animations
- Configuration reset

## ❓ Help

- About information
- Help menu
- Documentation
- Support information

---

# 🧩 Implementations

SysKit currently has multiple implementations.

| Implementation | Language | Purpose |
|---|---|---|
| `syskit.sh` | Bash | Full-featured shell toolkit |
| `syskit-lite.sh` | Bash | Lightweight shell implementation |
| `syskit.c` | C | Native C implementation |
| `syskit-lite.c` | C | Lightweight C implementation |
| `syskit-gui.c` | C / GTK3 | Graphical desktop interface |
| `syskit.py` | Python | Python implementation |
| `syskit lite.py` | Python | Lightweight Python / Termux implementation |
| `Syskit.java` | Java | Java terminal implementation |
| `SyskitLite.java` | Java | Lightweight Java implementation |

The implementations aim to provide similar functionality, but feature availability can vary depending on the language, operating system, permissions, and available dependencies.

---

# 🖥️ Platform Support

| Platform | Status |
|---|---|
| 🐧 Linux | ✅ Primary platform |
| 📱 Termux | ✅ Supported — Lite versions recommended |
| 🍎 macOS | ⚠️ Shell targeted — needs testing |
| 👹 BSD | ⚠️ Shell targeted — needs testing |
| 💻 Low-RAM systems | ✅ Lite versions recommended |
| 🪟 Windows | ⚠️ Not a primary target |

> ⚠️ Not every SysKit feature works on every operating system.

> ⚠️ Some features depend on commands, permissions, hardware, drivers, or platform-specific APIs.

> ⚠️ macOS and BSD support still require additional testing.

---

# 📦 Dependencies

SysKit does **not** require every dependency below for every feature.

Install the dependencies needed for the implementation and features you want to use.

## 🐧 Arch Linux / Arch-based

```bash
sudo pacman -Syu
sudo pacman -S base-devel gcc clang python openjdk curl wget git jq fastfetch tar zip unzip tree lshw dmidecode smartmontools lm_sensors pciutils util-linux
