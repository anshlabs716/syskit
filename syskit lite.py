#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# ========================================
# SysKit Lite - Termux Edition (Python)
# Author: AnshLabs716
# Version: 2.0.2-termux
# ========================================
# Fully functional for Termux.
# No external dependencies beyond Python 3 and standard libs.

import os
import sys
import subprocess
import time
import platform
import random
import string
import re
import shutil
from datetime import datetime
from pathlib import Path

# ========================================
# Global Configuration
# ========================================
VERSION = "2.0.2-termux"
AUTHOR = "AnshLabs716"

# Terminal colors (ANSI)
if os.isatty(sys.stdout.fileno()):
    RED = "\033[0;31m"
    GREEN = "\033[0;32m"
    YELLOW = "\033[0;33m"
    BLUE = "\033[0;34m"
    CYAN = "\033[0;36m"
    WHITE = "\033[0;37m"
    BOLD = "\033[1m"
    DIM = "\033[2m"
    RESET = "\033[0m"
else:
    RED = GREEN = YELLOW = BLUE = CYAN = WHITE = BOLD = DIM = RESET = ""

# Emojis (fallback to text if not supported)
EMOJIS = {
    "system": "📱" if sys.stdout.encoding == 'utf-8' else "[SYSTEM]",
    "monitor": "📊",
    "network": "🌐",
    "power": "🔋",
    "package": "📦",
    "cleaner": "🧹",
    "storage": "💾",
    "security": "🔐",
    "files": "📂",
    "utility": "🔑",
    "internet": "🌤️",
    "backup": "💾",
    "settings": "⚙️",
    "help": "❓",
    "check": "✅",
    "warning": "⚠️",
    "error": "❌",
    "info": "ℹ️",
    "rocket": "🚀",
    "fastfetch": "⚡"
}

# Terminal dimensions (fallback)
try:
    cols, rows = shutil.get_terminal_size()
    TERMINAL_WIDTH = cols
except:
    TERMINAL_WIDTH = 80

# ========================================
# Helper Functions
# ========================================

def clear():
    os.system("clear")

def print_box(title, content):
    """Draw a bordered box with title and content."""
    max_width = TERMINAL_WIDTH - 4
    if max_width > 50:
        max_width = 50
    if max_width < 20:
        max_width = 20

    # Wrap content lines
    lines = []
    for line in content.splitlines():
        if len(line) <= max_width:
            lines.append(line)
        else:
            # simple word wrap
            words = line.split()
            cur = ""
            for w in words:
                if len(cur) + len(w) + 1 <= max_width:
                    cur += w + " "
                else:
                    if cur:
                        lines.append(cur.rstrip())
                    cur = w + " "
            if cur:
                lines.append(cur.rstrip())
    # Remove trailing empty
    while lines and lines[-1] == "":
        lines.pop()

    # Top border
    print(f"{BOLD}{BLUE}┌{'─' * max_width}┐{RESET}")

    # Title
    if title:
        clean_title = re.sub(r'\x1b\[[0-9;]*m', '', title)
        title_len = len(clean_title)
        print(f"{BOLD}{BLUE}│{RESET} {BOLD}{WHITE}{title}{RESET} {' ' * (max_width - title_len - 1)}{BOLD}{BLUE}│{RESET}")
        print(f"{BOLD}{BLUE}├{'─' * max_width}┤{RESET}")

    # Content
    for line in lines:
        clean_line = re.sub(r'\x1b\[[0-9;]*m', '', line)
        line_len = len(clean_line)
        print(f"{BOLD}{BLUE}│{RESET} {line}{' ' * (max_width - line_len - 1)}{BOLD}{BLUE}│{RESET}")

    # Bottom border
    print(f"{BOLD}{BLUE}└{'─' * max_width}┘{RESET}")

def print_menu(title, items):
    """Print a menu with numbered items."""
    content = ""
    for i, item in enumerate(items, start=1):
        content += f"{BOLD}{GREEN}{i}.{RESET} {item}\n"
    content += f"\n{DIM}Type number. 0 to exit.{RESET}"
    print_box(title, content)

def show_success(msg): print(f"{GREEN}{EMOJIS['check']} {msg}{RESET}")
def show_warning(msg): print(f"{YELLOW}{EMOJIS['warning']} {msg}{RESET}")
def show_error(msg): print(f"{RED}{EMOJIS['error']} {msg}{RESET}")
def show_info(msg): print(f"{BLUE}{EMOJIS['info']} {msg}{RESET}")

def confirm_action(prompt):
    print(f"{YELLOW}{EMOJIS['warning']} {prompt}{RESET}")
    resp = input("Continue? [y/N] ").strip().lower()
    return resp in ('y', 'yes')

def run_cmd(cmd, capture=False):
    """Run a shell command, optionally capture output."""
    try:
        if capture:
            return subprocess.check_output(cmd, shell=True, text=True, stderr=subprocess.DEVNULL).strip()
        else:
            subprocess.run(cmd, shell=True, check=False)
            return ""
    except:
        return ""

def check_command(cmd):
    return shutil.which(cmd) is not None

# ========================================
# Banner and Setup
# ========================================

def quick_setup():
    clear()
    print(f"{BOLD}{CYAN}")
    print("  _____     _    _  _____ _  __ ")
    print(" / ____|   | |  | |/ ____| |/ / ")
    print("| (___   __| |  | | (___ | ' /  ")
    print(" \\___ \\ / _` |  | |\\___ \\|  <   ")
    print(" ____) | (_| |__| |____) | . \\  ")
    print("|_____/ \\__,_\\____/|_____/|_|\\_\\ ")
    print(f"{RESET}")
    print(f"{BOLD}{WHITE}by {AUTHOR}{RESET}")
    print(f"{DIM}Termux Optimized v{VERSION}{RESET}\n")
    print(f"{GREEN}{EMOJIS['check']} Termux detected!{RESET}")
    print(f"{BLUE}{EMOJIS['info']} Press any key to continue...{RESET}")
    input()

# ========================================
# System Information
# ========================================

def show_system_info():
    info = ""
    info += f"Hostname: {run_cmd('hostname') or 'Unknown'}\n"
    info += "OS: Termux\n"
    info += f"Android: {run_cmd('getprop ro.build.version.release') or 'Unknown'}\n"
    info += f"Kernel: {os.uname().release}\n"
    info += f"Arch: {os.uname().machine}\n"
    info += f"Uptime: {run_cmd('uptime -p') or 'Unknown'}\n"
    # Fixed storage command
    storage_cmd = "df -h /data 2>/dev/null | awk 'NR==2{print $2\" total \" $3\" used\"}'"
    info += f"Storage: {run_cmd(storage_cmd) or 'Unknown'}\n"
    # Fixed RAM command
    ram_cmd = "free -h 2>/dev/null | awk 'NR==2{print $2\" total \"$3\" used\"}'"
    info += f"RAM: {run_cmd(ram_cmd) or 'Unknown'}"
    print_box("System Info", info)

def show_hardware_info():
    info = ""
    info += f"Device: {run_cmd('getprop ro.product.model') or 'Unknown'}\n"
    info += f"Manufacturer: {run_cmd('getprop ro.product.manufacturer') or 'Unknown'}\n"
    info += f"Android: {run_cmd('getprop ro.build.version.release') or 'Unknown'}\n"
    info += f"SDK: {run_cmd('getprop ro.build.version.sdk') or 'Unknown'}\n"
    info += f"CPU: {os.uname().machine}\n"
    info += f"Cores: {run_cmd('nproc') or 'Unknown'}"
    print_box("Hardware Info", info)

def show_cpu_info():
    info = ""
    info += f"Architecture: {os.uname().machine}\n"
    info += f"Cores: {run_cmd('nproc') or 'Unknown'}\n"
    cpu_hw_cmd = "cat /proc/cpuinfo 2>/dev/null | grep Hardware | head -1 | cut -d: -f2 | xargs"
    info += f"CPU Info: {run_cmd(cpu_hw_cmd) or 'Unknown'}\n"
    features_cmd = "cat /proc/cpuinfo 2>/dev/null | grep Features | head -1 | cut -d: -f2 | xargs | cut -c1-50"
    info += f"Features: {run_cmd(features_cmd) or 'Unknown'}..."
    print_box("CPU Info", info)

def show_ram_info():
    info = ""
    if check_command("free"):
        total = run_cmd("free -h | awk 'NR==2{print $2}'")
        used = run_cmd("free -h | awk 'NR==2{print $3}'")
        free = run_cmd("free -h | awk 'NR==2{print $4}'")
        swap = run_cmd("free -h | awk 'NR==3{print $2\" total \" $3\" used\"}'") or "N/A"
        info += f"Total: {total}\nUsed: {used}\nFree: {free}\nSwap: {swap}"
    else:
        info = "free command not available"
    print_box("RAM Info", info)

def show_battery():
    info = ""
    if check_command("termux-battery-status"):
        output = run_cmd("termux-battery-status", capture=True)
        try:
            import json
            data = json.loads(output)
            info += f"Battery: {data.get('percentage', 'N/A')}%\n"
            info += f"Status: {data.get('status', 'N/A')}\n"
            info += f"Health: {data.get('health', 'N/A')}\n"
            info += f"Temperature: {data.get('temperature', 'N/A')}°C"
        except:
            info = "Could not parse battery data"
    else:
        info = "Install termux-api for battery info"
        if confirm_action("Install termux-api?"):
            run_cmd("pkg install termux-api -y")
            if check_command("termux-battery-status"):
                show_battery()
                return
    print_box("Battery", info)

def show_network_info():
    info = ""
    ip = run_cmd("ip addr show 2>/dev/null | grep 'inet ' | grep -v '127.0.0.1' | awk '{print $2}' | head -1") or "Unknown"
    gateway = run_cmd("ip route 2>/dev/null | grep default | awk '{print $3}'") or "Unknown"
    info += f"IP: {ip}\nGateway: {gateway}"
    print_box("Network Info", info)

def show_wifi():
    info = ""
    if check_command("termux-wifi-connectioninfo"):
        info = run_cmd("termux-wifi-connectioninfo", capture=True)
    else:
        info = "Install termux-api for Wi-Fi info"
        if confirm_action("Install termux-api?"):
            run_cmd("pkg install termux-api -y")
            if check_command("termux-wifi-connectioninfo"):
                show_wifi()
                return
    print_box("Wi-Fi", info)

def internet_test():
    info = ""
    show_info("Testing internet connection...")
    if os.system("ping -c 1 -W 2 8.8.8.8 > /dev/null 2>&1") == 0:
        info += "Internet: ✅ Connected\n"
        ip = run_cmd("curl -s ifconfig.me") or "N/A"
        info += f"Public IP: {ip}"
    else:
        info = "Internet: ❌ Disconnected"
    print_box("Internet Test", info)

# ========================================
# Package Manager
# ========================================

def package_menu():
    while True:
        clear()
        print(f"{BOLD}{CYAN}{EMOJIS['package']} Package Manager{RESET}\n")
        print_menu("Packages", [
            "Update",
            "Upgrade",
            "Clean cache",
            "Autoremove",
            "List installed",
            "Search",
            "Back"
        ])
        choice = input("Select: ").strip()
        if choice == "1":
            show_info("Updating...")
            run_cmd("pkg update")
            show_success("Done")
            input("Enter")
        elif choice == "2":
            show_info("Upgrading...")
            run_cmd("pkg upgrade -y")
            show_success("Done")
            input("Enter")
        elif choice == "3":
            show_info("Cleaning...")
            run_cmd("pkg clean")
            show_success("Done")
            input("Enter")
        elif choice == "4":
            show_info("Autoremoving...")
            run_cmd("pkg autoclean")
            show_success("Done")
            input("Enter")
        elif choice == "5":
            run_cmd("pkg list-installed")
            input("Enter")
        elif choice == "6":
            term = input("Search: ").strip()
            run_cmd(f"pkg search {term}")
            input("Enter")
        elif choice in ("0", "7"):
            break
        else:
            show_error("Invalid option")
            input("Enter")

# ========================================
# Cleaner
# ========================================

def cleaner_menu():
    while True:
        clear()
        print(f"{BOLD}{CYAN}{EMOJIS['cleaner']} Cleaner{RESET}\n")
        print_menu("Cleaner", [
            "User cache",
            "Temp files",
            "Package cache",
            "Trash",
            "Back"
        ])
        choice = input("Select: ").strip()
        if choice == "1":
            if confirm_action("Clean cache?"):
                run_cmd("rm -rf ~/.cache/*")
                show_success("Cache cleaned")
            input("Enter")
        elif choice == "2":
            if confirm_action("Clean temp?"):
                run_cmd("rm -rf /tmp/*")
                show_success("Temp cleaned")
            input("Enter")
        elif choice == "3":
            if confirm_action("Clean package cache?"):
                run_cmd("pkg clean")
                show_success("Package cache cleaned")
            input("Enter")
        elif choice == "4":
            if os.path.exists(os.path.expanduser("~/.local/share/Trash")):
                if confirm_action("Empty trash?"):
                    run_cmd("rm -rf ~/.local/share/Trash/*")
                    show_success("Trash emptied")
            else:
                show_warning("No trash found")
            input("Enter")
        elif choice in ("0", "5"):
            break
        else:
            show_error("Invalid option")
            input("Enter")

# ========================================
# Storage
# ========================================

def storage_menu():
    while True:
        clear()
        print(f"{BOLD}{CYAN}{EMOJIS['storage']} Storage{RESET}\n")
        print_menu("Storage", [
            "Disk usage",
            "Largest directories",
            "Largest files",
            "Mounted drives",
            "Back"
        ])
        choice = input("Select: ").strip()
        if choice == "1":
            clear()
            print(f"{BOLD}{CYAN}Disk Usage{RESET}\n")
            run_cmd("df -h")
            input("Enter")
        elif choice == "2":
            clear()
            print(f"{BOLD}{CYAN}Largest Directories{RESET}\n")
            run_cmd("du -sh ~/* 2>/dev/null | sort -hr | head -10")
            input("Enter")
        elif choice == "3":
            clear()
            print(f"{BOLD}{CYAN}Largest Files{RESET}\n")
            run_cmd("find ~ -type f -exec du -h {} + 2>/dev/null | sort -hr | head -10")
            input("Enter")
        elif choice == "4":
            clear()
            print(f"{BOLD}{CYAN}Mounted Drives{RESET}\n")
            run_cmd("mount 2>/dev/null | grep '/data' || mount 2>/dev/null | head -10")
            input("Enter")
        elif choice in ("0", "5"):
            break
        else:
            show_error("Invalid option")
            input("Enter")

# ========================================
# Utilities
# ========================================

def utility_menu():
    while True:
        clear()
        print(f"{BOLD}{CYAN}{EMOJIS['utility']} Utilities{RESET}\n")
        print_menu("Utilities", [
            "Password Generator",
            "UUID Generator",
            "Hash (MD5/SHA)",
            "Random String",
            "Back"
        ])
        choice = input("Select: ").strip()
        if choice == "1":
            length = input("Length [16]: ").strip()
            length = int(length) if length.isdigit() else 16
            pw = ''.join(random.choices(string.ascii_letters + string.digits + "!@#$%^&*", k=length))
            print_box("Password", pw)
            input("Enter")
        elif choice == "2":
            if check_command("uuidgen"):
                uuid = run_cmd("uuidgen", capture=True)
            else:
                with open("/proc/sys/kernel/random/uuid", "r") as f:
                    uuid = f.read().strip()
            print_box("UUID", uuid)
            input("Enter")
        elif choice == "3":
            text = input("Text to hash: ").strip()
            md5_cmd = f"echo -n '{text}' | md5sum | awk '{{print $1}}'"
            sha1_cmd = f"echo -n '{text}' | sha1sum | awk '{{print $1}}'"
            sha256_cmd = f"echo -n '{text}' | sha256sum | awk '{{print $1}}'"
            print(f"MD5: {run_cmd(md5_cmd, capture=True)}")
            print(f"SHA1: {run_cmd(sha1_cmd, capture=True)}")
            print(f"SHA256: {run_cmd(sha256_cmd, capture=True)}")
            input("Enter")
        elif choice == "4":
            length = input("Length [16]: ").strip()
            length = int(length) if length.isdigit() else 16
            rand_str = ''.join(random.choices(string.ascii_letters + string.digits, k=length))
            print_box("Random String", rand_str)
            input("Enter")
        elif choice in ("0", "5"):
            break
        else:
            show_error("Invalid option")
            input("Enter")

# ========================================
# Internet
# ========================================

def internet_menu():
    while True:
        clear()
        print(f"{BOLD}{CYAN}{EMOJIS['internet']} Internet{RESET}\n")
        print_menu("Internet", [
            "Weather",
            "Time",
            "Calendar",
            "Internet Test",
            "Public IP",
            "Back"
        ])
        choice = input("Select: ").strip()
        if choice == "1":
            city = input("City [London]: ").strip() or "London"
            weather = run_cmd(f"curl -s 'wttr.in/{city}?format=%c+%t+%w'", capture=True)
            print_box("Weather", f"Weather for {city}:\n{weather}")
            input("Enter")
        elif choice == "2":
            now = datetime.now()
            print_box("Time", f"Date: {now.strftime('%A, %B %d, %Y')}\nTime: {now.strftime('%H:%M:%S')}\nTimezone: {datetime.now().astimezone().tzname()}")
            input("Enter")
        elif choice == "3":
            cal = run_cmd("cal -3", capture=True) or "Calendar not available"
            print_box("Calendar", cal)
            input("Enter")
        elif choice == "4":
            internet_test()
            input("Enter")
        elif choice == "5":
            ip = run_cmd("curl -s ifconfig.me", capture=True) or "N/A"
            print_box("Public IP", ip)
            input("Enter")
        elif choice in ("0", "6"):
            break
        else:
            show_error("Invalid option")
            input("Enter")

# ========================================
# Security
# ========================================

def security_menu():
    while True:
        clear()
        print(f"{BOLD}{CYAN}{EMOJIS['security']} Security{RESET}\n")
        print_menu("Security", [
            "Open ports",
            "Running processes",
            "Security tips",
            "Back"
        ])
        choice = input("Select: ").strip()
        if choice == "1":
            clear()
            print(f"{BOLD}{CYAN}Open Ports{RESET}\n")
            run_cmd("netstat -tuln 2>/dev/null | grep LISTEN | head -10")
            input("Enter")
        elif choice == "2":
            clear()
            print(f"{BOLD}{CYAN}Running Processes{RESET}\n")
            run_cmd("ps aux 2>/dev/null | head -15")
            input("Enter")
        elif choice == "3":
            print_box("Security Tips",
                "1. Keep Termux updated\n"
                "2. Use strong passwords\n"
                "3. Don't run as root\n"
                "4. Check permissions\n"
                "5. Use VPN\n"
                "6. Install termux-api for security features")
            input("Enter")
        elif choice in ("0", "4"):
            break
        else:
            show_error("Invalid option")
            input("Enter")

# ========================================
# Backup
# ========================================

def backup_menu():
    while True:
        clear()
        print(f"{BOLD}{CYAN}{EMOJIS['backup']} Backup{RESET}\n")
        print_menu("Backup", [
            "Backup folder",
            "Restore backup",
            "Back"
        ])
        choice = input("Select: ").strip()
        if choice == "1":
            folder = input("Folder to backup: ").strip()
            if os.path.isdir(folder):
                backup_name = f"backup_{datetime.now().strftime('%Y%m%d')}.tar.gz"
                show_info("Creating backup...")
                run_cmd(f"tar -czf $HOME/{backup_name} '{folder}'")
                show_success(f"Backup saved: $HOME/{backup_name}")
            else:
                show_error("Folder not found")
            input("Enter")
        elif choice == "2":
            run_cmd("ls -la ~/backup_*.tar.gz 2>/dev/null || echo 'No backups found'")
            backup_file = input("Enter backup name to restore: ").strip()
            if os.path.exists(os.path.expanduser(f"~/{backup_file}")):
                restore_dir = input("Restore to: ").strip()
                os.makedirs(restore_dir, exist_ok=True)
                run_cmd(f"tar -xzf ~/{backup_file} -C {restore_dir}")
                show_success(f"Restored to {restore_dir}")
            else:
                show_error("Backup not found")
            input("Enter")
        elif choice in ("0", "3"):
            break
        else:
            show_error("Invalid option")
            input("Enter")

# ========================================
# Help
# ========================================

def help_menu():
    while True:
        clear()
        print(f"{BOLD}{CYAN}{EMOJIS['help']} Help{RESET}\n")
        print_menu("Help", [
            "About",
            "Supported",
            "Version",
            "Back"
        ])
        choice = input("Select: ").strip()
        if choice == "1":
            print_box("About",
                "SysKit - Termux Edition\n"
                f"By {AUTHOR}\n"
                "Unix Toolkit for Termux\n\n"
                "Features:\n"
                "- System Info\n"
                "- Battery\n"
                "- Wi-Fi\n"
                "- Package Manager\n"
                "- Cleaner\n"
                "- Utilities\n"
                "- Internet Tools")
            input("Enter")
        elif choice == "2":
            print_box("Supported",
                "Termux\n"
                "Android 5.0+\n"
                "pkg package manager\n"
                "ARM/ARM64/x86_64")
            input("Enter")
        elif choice == "3":
            print_box("Version", f"SysKit v{VERSION}")
            input("Enter")
        elif choice in ("0", "4"):
            break
        else:
            show_error("Invalid option")
            input("Enter")

# ========================================
# Fastfetch
# ========================================

def show_fastfetch():
    if check_command("fastfetch"):
        run_cmd("fastfetch")
    else:
        show_warning("Fastfetch not installed")
        if confirm_action("Install fastfetch?"):
            run_cmd("pkg install fastfetch -y")
            if check_command("fastfetch"):
                run_cmd("fastfetch")
            else:
                show_error("Fastfetch installation failed")

# ========================================
# Main Menu
# ========================================

def main_menu():
    while True:
        clear()
        print(f"{BOLD}{CYAN}")
        print("  _____     _    _  _____ _  __ ")
        print(" / ____|   | |  | |/ ____| |/ / ")
        print("| (___   __| |  | | (___ | ' /  ")
        print(" \\___ \\ / _` |  | |\\___ \\|  <   ")
        print(" ____) | (_| |__| |____) | . \\  ")
        print("|_____/ \\__,_\\____/|_____/|_|\\_\\ ")
        print(f"{RESET}")
        print(f"{BOLD}{WHITE}by {AUTHOR}{RESET}")
        print(f"{DIM}Termux v{VERSION}{RESET}\n")

        print_menu("SysKit - Termux", [
            "System Info",
            "Hardware Info",
            "CPU Info",
            "RAM Info",
            "Battery",
            "Network Info",
            "Wi-Fi",
            "Fastfetch ⚡",
            "Package Manager",
            "Cleaner",
            "Storage",
            "Utilities",
            "Internet",
            "Security",
            "Backup",
            "Help",
            "Exit"
        ])

        choice = input("Select option: ").strip()
        if choice == "1":
            show_system_info()
            input("Press Enter")
        elif choice == "2":
            show_hardware_info()
            input("Press Enter")
        elif choice == "3":
            show_cpu_info()
            input("Press Enter")
        elif choice == "4":
            show_ram_info()
            input("Press Enter")
        elif choice == "5":
            show_battery()
            input("Press Enter")
        elif choice == "6":
            show_network_info()
            input("Press Enter")
        elif choice == "7":
            show_wifi()
            input("Press Enter")
        elif choice == "8":
            show_fastfetch()
            input("Press Enter")
        elif choice == "9":
            package_menu()
        elif choice == "10":
            cleaner_menu()
        elif choice == "11":
            storage_menu()
        elif choice == "12":
            utility_menu()
        elif choice == "13":
            internet_menu()
        elif choice == "14":
            security_menu()
        elif choice == "15":
            backup_menu()
        elif choice == "16":
            help_menu()
        elif choice in ("0", "17"):
            print(f"{GREEN}{EMOJIS['check']} Goodbye!{RESET}")
            sys.exit(0)
        else:
            show_error("Invalid option")
            time.sleep(1)

# ========================================
# Entry Point
# ========================================

if __name__ == "__main__":
    quick_setup()
    main_menu()
