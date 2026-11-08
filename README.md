# 🛠 SysKit

> One toolkit, many implementations: **C · Bash · Python · Java · GTK3 · Kotlin (Android)**

<p align="center">
  <img width="679" alt="full" src="https://github.com/user-attachments/assets/6b63de81-e649-4135-9613-74187cef7efb" />
  <img width="424" alt="lite" src="https://github.com/user-attachments/assets/3d1f114e-558b-450f-98aa-7bae12118618" />
  <img width="1366" alt="gui" src="https://github.com/user-attachments/assets/3f267f50-3e09-4e58-ae13-363b21683210" />
  <img width="424" alt="apk" src="https://github.com/user-attachments/assets/06b14e22-24b4-415a-916f-722140636304" />
</p>

**SysKit** puts system info, diagnostics, maintenance, networking, storage and utility tools into one menu-driven toolkit — instead of jumping between separate commands.

---

## 📦 Implementations

| Version | Language | File | Environment |
|---|---|---|---|
| SysKit | C | `syskit.c` | Full terminal |
| SysKit Lite | C | `syskit-lite.c` | Lightweight environments |
| SysKit GUI | C / GTK3 | `syskit-gui.c` | Linux desktop |
| SysKit | Bash | `syskit.sh` | Unix-like systems |
| SysKit Lite | Bash | `syskit-lite.sh` | Lightweight environments |
| SysKit | Python | `syskit.py` | Python environments |
| SysKit Lite | Python | `syskit lite.py` | Termux / lightweight |
| SysKit | Java | `Syskit.java` | Java environments |
| SysKit Lite | Java | `SyskitLite.java` | Lightweight Java |
| **SysKit APK** | **Kotlin only** | `app/` | **Android** |

> **📱 The Android APK is written entirely in Kotlin** (Jetpack Compose UI). It is a native
> Android implementation — not the C/Bash/Python/Java code packed into an APK. The CLI/GUI
> versions above are separate programs that share the same idea and feature set.

## ✨ Features

System info (OS, kernel, CPU, GPU, RAM, hardware) · live monitoring (CPU/RAM/disk/network/temps) · networking (ping, connectivity, IP, DNS, gateway, Wi-Fi) · power & battery · packages & cleanup · storage analysis · security checks (firewall, open ports, SSH, services) · file tools (search, trees, duplicates) · archives (ZIP/TAR.GZ — beta) · utilities (passwords, hashes, UUIDs) · internet tools · backup/restore (beta) · **Android: debloating with Shizuku or Root**.

> ℹ️ Feature availability depends on the implementation, OS, dependencies, hardware and permissions.

## 📱 Android APK

The Android app lives in [`app/`](app/) — **Kotlin + Jetpack Compose only**, no cross-compiling involved.

```bash
./gradlew assembleDebug        # or open in Android Studio
```

**Privileged features** (app debloating / freeze / process tools) use, in order:

- **Shizuku** — privileged ADB-level operations without root. Install & start Shizuku, then authorize SysKit.
- **Root** — on rooted devices, grant access via Magisk / KernelSU / APatch.

Everything that doesn't need privileges (system info, monitoring, browsing packages) works on any device. Available operations vary by device, Android version, ROM and permission state.

## 🚀 Quick start (desktop / Termux)

```bash
git clone https://github.com/anshlabs716/syskit.git && cd syskit

# C
gcc syskit.c -o syskit && ./syskit            # lite: gcc syskit-lite.c -o syskit-lite

# Bash
chmod +x syskit.sh && ./syskit.sh             # lite: ./syskit-lite.sh

# Python
python3 syskit.py                             # lite: python3 "syskit lite.py"

# Java
javac Syskit.java && java Syskit              # lite: javac SyskitLite.java && java SyskitLite

# GTK3 GUI
gcc syskit-gui.c -o syskit-gui $(pkg-config --cflags --libs gtk+-3.0) && ./syskit-gui
```

> ⚠️ **Never run the Lite versions on a real Linux machine** — they are built for restricted environments like Termux and can heavily modify behavior there.

## 🗺 Roadmap highlights

- [ ] Improve error handling & dependency detection
- [ ] Expand monitoring, hardware support & storage diagnostics
- [ ] Expand Android functionality (more Shizuku/root tools, device compatibility)
- [ ] Automated testing

## 🙏 Credits

The weather feature is based on [**onNow**](https://github.com/shozanthebozan/onNow) by [**shozanthebozan**](https://github.com/shozanthebozan). Thank you!

## ⚖️ Disclaimer

SysKit is for system administration, diagnostics, maintenance, troubleshooting, learning and personal use. Some operations can modify system files, packages, services or caches — **use system-modifying features carefully**.

## 📄 License

See [LICENSE](LICENSE).
