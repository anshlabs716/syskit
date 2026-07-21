# 🚀 SysKit — Universal Unix Toolkit (now in C and bash)

**By AnshLabs716 and shozanbozan**

---

## ⚡ **Cloning the repo** (not recomended to do unless you're planning to contribute-or want the latest versions every time, just download latest from releases)


```bash
# Ubuntu / Debian / Mint:
sudo apt update && sudo apt install -y build-essential libgtk-3-dev curl lm-sensors policykit-1 fastfetch

# Arch Linux / EndeavourOS / Manjaro:
sudo pacman -Syu --needed base-devel gtk3 curl lm_sensors polkit fastfetch

# Fedora:
sudo dnf install -y gcc gtk3-devel curl lm_sensors polkit fastfetch

# Termux (Android):
pkg update && pkg install -y clang fastfetch
```
# ========================================
# 1. CLONE
# ========================================
git clone https://github.com/anshlabs716/syskit.git
```
# ========================================
# 2. RUN GTK3 GUI VERSION (Linux Desktop)
# ========================================
cd syskit && gcc syskit-gui.c -o syskit-gui `pkg-config --cflags --libs gtk+-3.0` && ./syskit-gui
```
# ========================================
# 3. RUN BASH VERSION
# ========================================
cd syskit && chmod +x syskit.sh && ./syskit.sh
```
# ========================================
# 4. RUN FULL C VERSION (Linux/macOS/BSD)
# ========================================
cd syskit && gcc -o syskit syskit.c && ./syskit
```
# ========================================
# 5. RUN LITE C VERSION (Termux / Low-RAM)
# ========================================
cd syskit && gcc -o syskit-lite syskit-lite.c && ./syskit-lite
```
# ========================================
# 6. RUN LITE BASH VERSION (Termux)
# ========================================
cd syskit && chmod +x syskit-lite.sh && ./syskit-lite.sh
```
## ✨ FEATURES
### System: 
Fastfetch, System Info, Hardware, CPU, GPU, RAM, Motherboard, Disk, Kernel, Uptime, Environment
### Monitoring: 
CPU, RAM, Disk, Network, Processes, Top Processes, Resource Monitor, Temperature
### Network:
 Ping, Internet Test, DNS, Public IP, Local IP, Gateway, Wi-Fi
### Power: 
Battery charge, Charging, Health, Cycles
### Packages: 
Update, Upgrade, Clean, Autoremove, Search, List
### Cleaner: 
Cache, Temp, Logs, Trash, Homebrew, Timeshift, Snapper
### Storage: 
Disk Usage, Largest Directories, Largest Files, Mounted Drives, SMART
### Security: 
Firewall, Open Ports, SSH, Services, Recommendations
### Files: 
Find, Duplicates, Search Text, Tree, Stats
### Archive: 
Extract/Create ZIP, Extract/Create TAR.GZ
### Utilities: 
Password, Hash (MD5/SHA1/SHA256/SHA512), UUID, Random String
### Internet: 
Weather, Time, Calendar
### Backup: 
Backup, Restore, Compress, Verify
### Settings: 
Colors, Animations, Emojis, Reset
### Help: 
About, Docs, Support, Version

## 🖥️ SUPPORTED OS'
Any Unix-like operating system


## 📦 AUTO-INSTALL TOOLS
fastfetch, curl, wget, jq, git, tar, unzip, zip, tree, lshw, dmidecode, smartctl, sensors, iftop, nmcli, lspci, hostname, uuidgen

## 🎯 MENU
1. 🖥️ System    2. 📊 Monitoring    3. 🌐 Network
4. 🔋 Power      5. 📦 Packages      6. 🧹 Cleaner
7. 💾 Storage    8. 🔐 Security      9. 📂 Files
10. 🗜️ Archive  11. 🔑 Utilities    12. 🌤️ Internet
13. 💾 Backup    14. ⚙️ Settings     15. ❓ Help
0. Exit

## Troubleshooting

| Issue                | Solution                  |
| -------------------- | ------------------------- |
| Won't run            | chmod +x syskit.sh        |
| Colours not visible  | ./syskit.sh --color=always|
| Permission           | sudo ./syskit.sh          |

## 📜 LICENSE
MIT — free for personal and commercial use.

SysKit v2.0.0 | © 2026 AnshLabs716 and shozanthebozan
