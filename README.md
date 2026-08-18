# 🚀 SysKit

<div align="center">

### Universal Unix Toolkit

**A powerful all-in-one toolkit for system information, maintenance, diagnostics, networking, storage, utilities, and more.**

[![C](https://img.shields.io/badge/C-57.7%25-00599C?style=for-the-badge&logo=c&logoColor=white)](https://github.com/anshlabs716/syskit)
[![Shell](https://img.shields.io/badge/Shell-42.3%25-4EAA25?style=for-the-badge&logo=gnu-bash&logoColor=white)](https://github.com/anshlabs716/syskit)
[![License](https://img.shields.io/badge/license-MIT-yellow?style=for-the-badge)](https://github.com/anshlabs716/syskit)
[![Platform](https://img.shields.io/badge/platform-Unix-lightgrey?style=for-the-badge)](https://github.com/anshlabs716/syskit)

</div>

---

## ⚡ About

**SysKit** is an all-in-one Unix toolkit built with **C and Bash**.

It brings system information, monitoring, maintenance, networking, storage, security checks, file utilities, backups, and other useful tools into one organized interface.

SysKit includes full-featured and lightweight implementations for different environments.

---

## ✨ Features

### 🖥️ System Information

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

### 📊 Monitoring

- CPU usage
- RAM usage
- Disk usage
- Running processes
- Network information
- Temperature information

### 🌐 Networking

- Ping
- Internet connectivity checks
- Local IP
- Public IP
- DNS information
- Gateway information
- Wi-Fi information

### 🔋 Power

- Battery information
- Battery percentage
- Charging status
- Battery health
- Power information

### 📦 Package Management

- Package updates
- Package upgrades
- Package cleanup
- Autoremove
- Package searching
- Installed package information

### 🧹 System Cleaning

- Cache cleanup
- Temporary file cleanup
- Log cleanup
- Trash cleanup
- Package cache cleanup
- System maintenance

### 💾 Storage

- Disk usage
- Mounted drives
- Directory sizes
- Large file detection
- Storage information
- SMART information

### 🔐 Security

- Firewall information
- Open ports
- SSH information
- Running services
- Basic security checks

### 📂 File Utilities

- File searching
- Text searching
- Directory trees
- File statistics
- Duplicate detection

### 🗜️ Archives

- ZIP creation
- ZIP extraction
- TAR.GZ creation
- TAR.GZ extraction

> ⚠️ Archive creation and extraction are currently **untested**.

### 🔑 Utilities

- Password generation
- Random string generation
- MD5
- SHA1
- SHA256
- SHA512
- UUID generation

### 🌍 Internet Utilities

- Weather
- Time
- Calendar
- Connectivity tools

### 💾 Backup

- Backup
- Restore
- Compression
- Backup verification

### ⚙️ Settings

- Colors
- Emojis
- Animations
- Configuration reset

### ❓ Help

- About information
- Help menu
- Documentation
- Support information

---

## 🧩 Implementations

| File | Language | Description |
|---|---|---|
| `syskit.sh` | Bash | Full-featured Shell toolkit |
| `syskit.c` | C | Full native C implementation |
| `syskit-lite.sh` | Bash | Lightweight Shell implementation |
| `syskit-lite.c` | C | Lightweight C implementation |
| `syskit-gui.c` | C / GTK3 | Graphical desktop interface |

---

## 🖥️ Platform Support

| Platform | Support |
|---|---|
| 🐧 Linux | ✅ Fully supported — all SysKit versions |
| 📱 Termux | ✅ Supported — Lite versions recommended |
| 🍎 macOS | ⚠️ Shell targeted — untested |
| 👹 BSD | ⚠️ Shell targeted — untested |
| 💻 Low-RAM systems | ✅ Supported — Lite versions recommended |

> ⚠️ Some tools may not work on every system depending on the OS, hardware, drivers, dependencies, permissions, or platform-specific commands.

> ⚠️ macOS and BSD support are currently untested.

> ⚠️ Archive creation and extraction are currently untested.

---

## 📦 Requirements

Depending on the feature being used, SysKit may require:

- Bash
- GCC
- Clang
- curl
- wget
- git
- fastfetch
- jq
- tar
- zip
- unzip
- tree
- lshw
- dmidecode
- smartctl
- sensors
- nmcli
- lspci
- uuidgen

You do not need every dependency for every feature.

---

## 📥 Installation

### Clone

    git clone https://github.com/anshlabs716/syskit.git
    cd syskit

### Bash

    chmod +x syskit.sh
    ./syskit.sh

### Lite Bash

    chmod +x syskit-lite.sh
    ./syskit-lite.sh

### C

    gcc syskit.c -o syskit
    ./syskit

### Lite C

    gcc syskit-lite.c -o syskit-lite
    ./syskit-lite

### GTK3 GUI

    gcc syskit-gui.c -o syskit-gui $(pkg-config --cflags --libs gtk+-3.0)
    ./syskit-gui

### Termux

    pkg update
    pkg install clang
    clang syskit-lite.c -o syskit-lite
    ./syskit-lite

---

## 📋 Main Menu

    ╔══════════════════════════════════════╗
    ║              🚀 SYSKIT               ║
    ╚══════════════════════════════════════╝

     1. 🖥️  System
     2. 📊 Monitoring
     3. 🌐 Network
     4. 🔋 Power
     5. 📦 Packages
     6. 🧹 Cleaner
     7. 💾 Storage
     8. 🔐 Security
     9. 📂 Files
    10. 🗜️  Archives
    11. 🔑 Utilities
    12. 🌍 Internet
    13. 💾 Backup
    14. ⚙️  Settings
    15. ❓ Help
    16. 🚪 Exit

---

## 📁 Project Structure

    syskit/
    ├── syskit.c
    ├── syskit-lite.c
    ├── syskit-gui.c
    ├── syskit.sh
    ├── syskit-lite.sh
    ├── Syskit.desktop
    ├── CHANGELOG.md
    ├── CONTRIBUTING.md
    ├── SECURITY.md
    ├── LICENSE
    └── README.md

---

## 🛠️ Troubleshooting

### Permission Denied

    chmod +x syskit.sh

### Missing Command

Install the dependency required by the feature you are trying to use.

### C Compilation Fails

Make sure GCC or Clang is installed.

### GTK3 Compilation Fails

Make sure GTK3 development libraries and `pkg-config` are installed.

### A Feature Does Not Work

The feature may be platform-specific, dependency-dependent, or currently untested.

Check your operating system, hardware, dependencies, permissions, and SysKit implementation before reporting the issue.

---

## 🗺️ Roadmap

- [ ] Expand cross-platform support
- [ ] Test macOS
- [ ] Test BSD
- [ ] Improve Termux support
- [ ] Improve GTK3 GUI
- [ ] Expand monitoring tools
- [ ] Add more networking utilities
- [ ] Improve hardware detection
- [ ] Improve Lite implementations
- [ ] Expand documentation
- [ ] Add automated testing
- [ ] Fully test archive functionality
- [ ] Improve platform-specific compatibility

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create a branch
3. Make your changes
4. Test your changes
5. Commit your changes
6. Push your branch
7. Open a Pull Request

See [`CONTRIBUTING.md`](CONTRIBUTING.md) for more information.

---

## 🔐 Security

If you discover a security issue, please report it responsibly.

See [`SECURITY.md`](SECURITY.md) for the security policy.

---

## 📝 Changelog

See [`CHANGELOG.md`](CHANGELOG.md) for the project changelog.

---

## 📜 License

SysKit is released under the **MIT License**.

See [`LICENSE`](LICENSE) for the full license text.

---

## 👨‍💻 Authors

**AnshLabs716**

**shozanthebozan**

---

## ⭐ Support

If you find SysKit useful:

- ⭐ Star the repository
- 🐛 Report bugs
- 💡 Suggest features
- 🔧 Contribute code
- 📖 Improve documentation

---

<div align="center">

### 🚀 SysKit

**One toolkit. Multiple Unix environments.**

**C • Bash • GTK3 • Unix**

</div>
