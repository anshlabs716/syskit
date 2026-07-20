# 🚀 SysKit — Universal Unix Toolkit (now in C and bash)

**By AnshLabs716 and shozanbozan**

---

## ⚡ **Cloning the repo** (not recomended to do unless you're planning to contribute-or want the latest versions every time, just download latest from releases)


```bash
# how to clone
git clone https://github.com/anshlabs716/syskit.git 

# how to run
cd syskit && chmod +x syskit.sh && bash syskit.sh

# how to run C version
cd syskit gcc -o syskit syskit.c
./syskit

# how to run lite C version (if on termux follow the pkg steps)
pkg update
pkg install clang
pkg install fastfetch
cd syskit gcc -o syskit-lite.c
./syskit-lite
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

SysKit v2.0.0 | © 2026 AnshLabs716
