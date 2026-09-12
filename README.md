🚀 SysKit

Universal System Toolkit

<p align="left">
  <img src=""https://img.shields.io/badge/C-31.3%25-A8B9CC?style=for-the-badge&logo=c&logoColor=white" (https://img.shields.io/badge/C-31.3%25-A8B9CC?style=for-the-badge&logo=c&logoColor=white)">
    
  <img src=""https://img.shields.io/badge/Java-26.1%25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" (https://img.shields.io/badge/Java-26.1%25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)">
    
  <img src=""https://img.shields.io/badge/Shell-22.9%25-89E051?style=for-the-badge&logo=gnubash&logoColor=white" (https://img.shields.io/badge/Shell-22.9%25-89E051?style=for-the-badge&logo=gnubash&logoColor=white)">
    
  <img src=""https://img.shields.io/badge/Python-19.7%25-3776AB?style=for-the-badge&logo=python&logoColor=white" (https://img.shields.io/badge/Python-19.7%25-3776AB?style=for-the-badge&logo=python&logoColor=white)">
    
  <img src=""https://img.shields.io/badge/Linux-Supported-FCC624?style=for-the-badge&logo=linux&logoColor=black" (https://img.shields.io/badge/Linux-Supported-FCC624?style=for-the-badge&logo=linux&logoColor=black)">
    
  <img src=""https://img.shields.io/badge/Termux-Supported-000000?style=for-the-badge&logo=termux&logoColor=white" (https://img.shields.io/badge/Termux-Supported-000000?style=for-the-badge&logo=termux&logoColor=white)">
</p>

«SysKit is a multi-language system toolkit for information, diagnostics, maintenance, networking, storage, utilities, and more.»

---

🧠 What is SysKit?

SysKit brings a collection of useful system tools into one menu-driven toolkit.

Instead of jumping between different commands for system information, networking, storage, maintenance, and diagnostics, SysKit puts them together in one place.

The project currently includes implementations in C, Bash, Python, Java, and Android, with lightweight variants designed for more restricted environments.

🖥️ SysKit Full

Full

📱 SysKit Lite

Lite

🖥️ SysKit GUI

GUI

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

---

✨ Features

Category| What SysKit provides
🖥️ System| OS, kernel, CPU, GPU, RAM, hardware, uptime, environment
📊 Monitoring| CPU, RAM, disk, processes, network, temperature
🌐 Network| Ping, connectivity, IP, DNS, gateway, Wi-Fi
🔋 Power| Battery, charging, health, power information
📦 Packages| Updates, upgrades, search, installed packages, cleanup
🧹 Cleaner| Cache, temporary files, logs, trash, package cache
💾 Storage| Disk usage, mounted drives, directories, large files, SMART
🔐 Security| Firewall, open ports, SSH, services, basic checks
📁 Files| Search, text search, directory trees, statistics, duplicates
📦 Archives| ZIP and TAR.GZ creation/extraction — Beta
🛠️ Utilities| Passwords, random strings, hashes, UUIDs
🌍 Internet| Weather, time, calendar, connectivity
💾 Backup| Backup, restore, compression, verification
⚙️ Settings| Colors, emojis, animations, reset
❓ Help| Help, about, documentation, support

«⚠️ Feature availability depends on the implementation, operating system, installed dependencies, hardware, and permissions.»

---

🧩 Implementations

SysKit isn't tied to a single language.

Version| Language| File| Environment
SysKit| C| "syskit.c"| Full terminal version
SysKit Lite| C| "syskit-lite.c"| Lightweight environments
SysKit GUI| C / GTK3| "syskit-gui.c"| Linux desktop
SysKit| Bash| "syskit.sh"| Unix-like systems
SysKit Lite| Bash| "syskit-lite.sh"| Lightweight environments
SysKit| Python| "syskit.py"| Python environments
SysKit Lite| Python| "syskit lite.py"| Termux / lightweight environments
SysKit| Java| "Syskit.java"| Java environments
SysKit Lite| Java| "SyskitLite.java"| Lightweight Java environments
SysKit Android| Kotlin / Android| "app/"| Android devices

---

🖥️ Platform Support

Platform| Status| Notes
🐧 Linux| ✅| Primary platform
📱 Termux| ✅| Lite versions recommended
🤖 Android| ✅| Native APK available
🍎 macOS| 🧪| Shell compatibility needs testing
👻 BSD| 🧪| Shell compatibility needs testing

Linux

Linux is the primary target for SysKit.

The exact functionality depends on your distribution and installed system utilities.

Termux

SysKit can run in Termux, with the Lite implementations being the recommended choice.

Android does not expose every traditional Linux interface, so some features may not be available.

Android

SysKit now has a native Android application.

The Android version is built separately from the traditional Linux implementations and provides Android-specific functionality.

It supports:

- Android-native system information
- Device diagnostics
- Android-specific utilities
- Shizuku integration
- Root-aware functionality
- Android permissions
- Native Android UI
- APK distribution
- Android device testing

The Android application is available through the project's releases.

---

📱 Android

Android Feature Status

All currently planned core Android implementation tasks have been completed:

- [x] Research Android APIs
- [x] Research Android architecture
- [x] Design Android UI
- [x] Port supported functionality
- [x] Integrate Shizuku
- [x] Integrate root-aware functionality
- [x] Implement Android permissions
- [x] Build Android application
- [x] Build APK
- [x] Test APK on real devices
- [x] Add Android project structure
- [x] Add Android release
- [x] Add Android support to SysKit documentation
- [x] Add Shizuku/root support
- [x] Release SysKit Android

«📱 The Android version is a native Android implementation rather than a Linux application packaged into an APK.»

🔐 Shizuku

SysKit can use Shizuku for supported operations without requiring traditional root access.

🛡️ Root

On rooted devices, SysKit can use root-aware functionality where supported.

«⚠️ Root and Shizuku capabilities depend on the device, Android version, permissions, and the specific operation being performed.»

---

📥 Installation

Clone

git clone https://github.com/anshlabs716/syskit.git
cd syskit

---

📱 Android APK

The Android version is distributed as an APK through GitHub Releases.

Latest Android Release

SysKit v1.0.1 APK

"Download from Releases" (https://github.com/anshlabs716/syskit/releases/tag/v1.0.1-apk)

Android installation

1. Download the SysKit APK from the release page.
2. Install the APK on your Android device.
3. Open SysKit.
4. Grant the permissions required by the features you want to use.
5. If using Shizuku features, make sure Shizuku is running.
6. If using root features, grant root access when requested.

«⚠️ Only install APKs from sources you trust.»

---

🐚 Bash

Full

chmod +x syskit.sh
./syskit.sh

Lite

chmod +x syskit-lite.sh
./syskit-lite.sh

---

🐍 Python

Full

python3 syskit.py

Lite

python3 "syskit lite.py"

Install Python

Debian / Ubuntu / Mint

sudo apt update
sudo apt install python3

Fedora

sudo dnf install python3

Arch Linux

sudo pacman -S python

Alpine

sudo apk add python3

---

☕ Java

Check your JDK:

java --version
javac --version

Full

javac Syskit.java
java Syskit

Lite

javac SyskitLite.java
java SyskitLite

Install a JDK

Debian / Ubuntu / Mint

sudo apt update
sudo apt install default-jdk

Fedora

sudo dnf install java-latest-openjdk-devel

Arch Linux

sudo pacman -S jdk-openjdk

Alpine

sudo apk add openjdk17

---

🦾 C

Full

gcc syskit.c -o syskit
./syskit

Lite

gcc syskit-lite.c -o syskit-lite
./syskit-lite

Clang

clang syskit.c -o syskit
./syskit

---

🖥️ GTK3

The GTK3 version requires GTK3 development libraries and "pkg-config".

Compile

gcc syskit-gui.c -o syskit-gui $(pkg-config --cflags --libs gtk+-3.0)

Run

./syskit-gui

Debian / Ubuntu / Mint

sudo apt update
sudo apt install build-essential pkg-config libgtk-3-dev

Fedora

sudo dnf install gcc pkgconf-pkg-config gtk3-devel

Arch Linux

sudo pacman -S base-devel pkgconf gtk3

---

📱 Termux

Install the basic tools:

pkg update
pkg upgrade
pkg install git python clang

Clone SysKit:

git clone https://github.com/anshlabs716/syskit.git
cd syskit

Python Lite

python "syskit lite.py"

C Lite

clang syskit-lite.c -o syskit-lite
./syskit-lite

Java Lite

If a suitable JDK is available:

javac SyskitLite.java
java SyskitLite

«💡 Recommended: Start with a Lite implementation on Termux.»

---

📦 Dependencies

Different implementations use different dependencies.

Core

- Bash
- GCC or Clang
- Python 3
- Java JDK
- Git

Common Utilities

Some features may use:

- "curl"
- "wget"
- "jq"
- "fastfetch"
- "tar"
- "zip"
- "unzip"
- "tree"

Hardware / System

Depending on the feature and platform:

- "lshw"
- "dmidecode"
- "smartctl"
- "sensors"
- "lspci"
- "uuidgen"

Networking

Some networking features may use:

- "ping"
- "nmcli"
- NetworkManager
- DNS utilities

«Not every dependency is required to run every version of SysKit.»

---

🎛️ Main Menu

SysKit uses a simple numbered menu:

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

---

🧪 Testing

SysKit has multiple implementations, so compatibility matters.

Feature Testing

- [x] C — runs great
- [x] C Lite — super fast; NEVER run this on real Linux
- [x] Bash — works amazingly well on Linux
- [x] Bash Lite — works really well on Termux; NEVER run this on real Linux
- [x] Python — some features are a bit buggier than others
- [x] Python Lite — has some issues with device info but overall great; NEVER run this on real Linux
- [x] Java — works pretty well
- [ ] Java Lite — not tested yet; NEVER run this on real Linux
- [x] GTK3 — works well but is slower compared to the other C versions
- [x] Android APK
- [x] Shizuku support
- [x] Root-aware Android functionality

«⚠️ Never run Lite versions on real Linux. They are designed for restricted environments such as Termux and can heavily modify their behavior on Linux, potentially causing crashes. Full versions can be used on Termux where supported.»

Platform Testing

- [ ] Debian 
- [ ] Ubuntu
- [ ] Linux Mint
- [x] Fedora
- [x] Arch Linux
- [ ] Alpine
- [ ] openSUSE
- [ ] Void Linux
- [x] Termux
- [x] Android
- [ ] macOS
- [ ] BSD

Feature Areas

- [x] System information
- [x] Monitoring
- [x] Networking
- [x] Power
- [x] Package management
- [x] Cleaning
- [x] Storage
- [x] Security
- [x] File utilities
- [x] Archives — Beta
- [x] Utilities
- [x] Internet tools
- [x] Backup / restore
- [x] Settings
- [x] Android integration
- [x] Shizuku integration
- [x] Root-aware functionality

---

🗺️ Roadmap

🔧 Core

- [x] C implementation
- [x] C Lite implementation
- [x] Bash implementation
- [x] Bash Lite implementation
- [x] Python implementation
- [x] Python Lite implementation
- [x] Java implementation
- [x] Java Lite implementation
- [x] GTK3 implementation
- [x] Android implementation
- [x] Shizuku integration
- [x] Root-aware Android functionality
- [x] APK release
- [ ] Improve error handling
- [ ] Improve dependency detection
- [ ] Expand hardware support
- [ ] Expand monitoring
- [ ] Expand networking
- [ ] Improve storage diagnostics
- [ ] Improve Lite versions
- [ ] Improve documentation
- [ ] Automated testing

🌍 Compatibility

- [x] Linux
- [x] Termux
- [x] Android
- [ ] Debian-based testing
- [x] Fedora-based testing
- [x] Arch-based testing
- [ ] Alpine testing
- [ ] openSUSE testing
- [ ] Void Linux testing
- [ ] macOS testing
- [ ] BSD testing

🧪 Reliability

- [x] Test every menu option
- [x] Test every implementation
- [x] Test multiple distributions
- [ ] Test low-resource environments
- [ ] Test archive functionality
- [ ] Test backup / restore
- [ ] Test missing dependencies
- [ ] Test permission handling
- [ ] Improve compatibility detection

📱 Android

- [x] Research Android APIs
- [x] Research Android architecture
- [x] Design Android UI
- [x] Port supported functionality
- [x] Investigate Shizuku
- [x] Integrate Shizuku
- [x] Investigate ADB
- [x] Add root-aware features
- [x] Build prototype
- [x] Test on real devices
- [x] Build SysKit APK
- [x] Release SysKit APK
- [x] Add Android documentation
- [x] Add Android project structure
- [ ] Expand Android feature coverage
- [ ] Expand device compatibility
- [ ] Add more Android-specific tools

«Android uses an Android-native implementation rather than simply packaging the existing Linux code into an APK.»

---

🗂️ Project Structure

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
├── syskit-lite.sh
│
└── app/
    └── Android application

---

🛠️ Troubleshooting

"Permission denied"

chmod +x syskit.sh

Then:

./syskit.sh

Some system operations may require elevated permissions.

"command not found"

Install the missing dependency using your distribution's package manager.

C compilation fails

gcc --version

or:

clang --version

Java compilation fails

java --version
javac --version

GTK3 compilation fails

pkg-config --modversion gtk+-3.0

If GTK3 cannot be found, install the appropriate development package.

Android APK won't install

Check:

1. The APK is compatible with your Android version.
2. You downloaded the APK from the official release.
3. Android allows installation from the source you used.
4. The APK download completed correctly.

Shizuku features don't work

Check:

1. Shizuku is installed.
2. Shizuku is running.
3. SysKit has been granted Shizuku access.
4. The operation is supported on your Android version.

Root features don't work

Check:

1. Your device is rooted.
2. Your root manager is functioning correctly.
3. SysKit has been granted root access.
4. The requested operation requires root.

A feature doesn't work

Check:

1. Required dependencies
2. Platform compatibility
3. Permissions
4. Hardware support
5. Whether the Lite version works

If the problem continues, open an issue with useful information about your environment.

---

🤝 Contributing

Contributions, testing, bug reports, ideas, and improvements are welcome.

Before contributing, read ""CONTRIBUTING.md"" (https://github.com/anshlabs716/syskit/blob/main/CONTRIBUTING.md).

When submitting changes:

1. Keep changes focused.
2. Test what you changed.
3. Avoid breaking other implementations.
4. Document new functionality where appropriate.
5. Explain compatibility considerations.

---

🔐 Security

For security-related issues, see ""SECURITY.md"" (https://github.com/anshlabs716/syskit/blob/main/SECURITY.md).

Please avoid publicly exposing sensitive security issues before they can be investigated.

---

📜 Changelog

See ""CHANGELOG.md"" (https://github.com/anshlabs716/syskit/blob/main/CHANGELOG.md) for development history.

---

👥 Credits

Weather Feature

Special thanks to "@shozanthebozan" (https://github.com/shozanthebozan).

The SysKit weather feature is based on "onNow" (https://github.com/shozanthebozan/onNow) by shozanthebozan.

Please check out the original project:

🔗 "shozanthebozan/onNow" (https://github.com/shozanthebozan/onNow)

«💙 Credit goes to shozanthebozan for the original weather implementation that inspired the weather functionality in SysKit.»

---

⚠️ Disclaimer

SysKit is intended for system administration, diagnostics, maintenance, troubleshooting, learning, and personal use.

Some operations can modify system files, packages, services, caches, or other system resources.

Use system-modifying features carefully.

Feature availability varies between operating systems and implementations.

---

📄 License

SysKit is distributed under the license included in ""LICENSE"" (https://github.com/anshlabs716/syskit/blob/main/LICENSE).

---

💡 Philosophy

SysKit is built around a simple development loop:

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

«Build it. Break it. Understand it. Improve it.»

---

📞 Contact

Have a question, bug report, feature request, or idea?

- 🐛 Bug: Open an "issue" (https://github.com/anshlabs716/syskit/issues)
- 💡 Feature request: Open an "issue" (https://github.com/anshlabs716/syskit/issues)
- 🤝 Contribution: See ""CONTRIBUTING.md"" (https://github.com/anshlabs716/syskit/blob/main/CONTRIBUTING.md)
- 🔐 Security: See ""SECURITY.md"" (https://github.com/anshlabs716/syskit/blob/main/SECURITY.md)

---

🚀 SysKit

C · Bash · Python · Java · Kotlin

Terminal · Lite · GTK3 · Android

«One toolkit. Multiple implementations. Always improving.»