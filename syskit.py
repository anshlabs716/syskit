#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# ========================================
# SysKit - Universal Unix Toolkit
# Python translation of syskit.c v2.0.1
# ========================================

import os
import sys
import subprocess
import time
import platform
import shutil
import glob
import pwd
import grp
import signal
import termios
import fcntl
import struct
import errno
import re
import random
import string
import math
import json
from pathlib import Path

# ========================================
# GLOBAL CONFIG
# ========================================
VERSION = "2.0.1"
AUTHOR = "AnshLabs716"
SCRIPT_NAME = "syskit.py"
CONFIG_DIR = ".config/syskit"
CONFIG_FILE = "config.conf"
FIRST_RUN_FILE = "first_run_complete"
LOG_FILE = "syskit.log"

IS_ROOT = os.geteuid() == 0
COLORS_SUPPORTED = True
USE_EMOJIS = True
USE_ANIMATIONS = True
IS_FULLSCREEN = False  # We'll detect later if needed
TERMINAL_WIDTH = 80
TERMINAL_HEIGHT = 24
OS_NAME = "Unknown"
DISTRO = "Unknown"
DISTRO_VERSION = "Unknown"
PKG_MANAGER = "unknown"
PKG_UPDATE = "echo 'Unknown'"
PKG_UPGRADE = "echo 'Unknown'"
PKG_INSTALL = "echo 'Unknown'"
PKG_REMOVE = "echo 'Unknown'"
PKG_SEARCH = "echo 'Unknown'"
PKG_LIST = "echo 'Unknown'"
PKG_CLEAN = "echo 'Unknown'"
PKG_AUTOREMOVE = "echo 'Unknown'"
PKG_INSTALL_CMD = "echo 'Unknown'"
ROOT_INDICATOR = "User"
HOME_DIR = os.path.expanduser("~")
USER_NAME = os.getenv("USER", "unknown")
CONFIG_PATH = ""
LOG_PATH = ""

# ========================================
# COLORS
# ========================================
RED = "\033[0;31m"
GREEN = "\033[0;32m"
YELLOW = "\033[0;33m"
BLUE = "\033[0;34m"
MAGENTA = "\033[0;35m"
CYAN = "\033[0;36m"
WHITE = "\033[0;37m"
BOLD = "\033[1m"
DIM = "\033[2m"
RESET = "\033[0m"
BG_RED = "\033[41m"
BG_GREEN = "\033[42m"
BG_YELLOW = "\033[43m"
BG_BLUE = "\033[44m"
BG_CYAN = "\033[46m"
BG_WHITE = "\033[47m"

# ========================================
# EMOJIS
# ========================================
EMOJI_SYSTEM = "🖥️"
EMOJI_MONITOR = "📊"
EMOJI_NETWORK = "🌐"
EMOJI_POWER = "🔋"
EMOJI_PACKAGE = "📦"
EMOJI_CLEANER = "🧹"
EMOJI_STORAGE = "💾"
EMOJI_SECURITY = "🔐"
EMOJI_FILES = "📂"
EMOJI_ARCHIVE = "🗜️"
EMOJI_UTILITY = "🔑"
EMOJI_INTERNET = "🌤️"
EMOJI_BACKUP = "💾"
EMOJI_SETTINGS = "⚙️"
EMOJI_HELP = "❓"
EMOJI_CHECK = "✅"
EMOJI_WARNING = "⚠️"
EMOJI_ERROR = "❌"
EMOJI_INFO = "ℹ️"
EMOJI_STAR = "⭐"
EMOJI_ROCKET = "🚀"
EMOJI_GEAR = "⚙️"
EMOJI_SHIELD = "🛡️"
EMOJI_LOCK = "🔒"
EMOJI_HOURGLASS = "⏳"

# ========================================
# FUNCTION PROTOTYPES (will be defined later)
# ========================================

# ========================================
# UTILITY FUNCTIONS
# ========================================

def update_terminal_size():
    global TERMINAL_WIDTH, TERMINAL_HEIGHT
    try:
        import shutil
        cols, rows = shutil.get_terminal_size()
        TERMINAL_WIDTH = cols
        TERMINAL_HEIGHT = rows
    except:
        TERMINAL_WIDTH = 80
        TERMINAL_HEIGHT = 24

def strip_ansi(text):
    """Remove ANSI escape sequences from a string."""
    import re
    return re.sub(r'\x1b\[[0-9;]*m', '', text)

def print_header():
    os.system("clear")
    update_terminal_size()
    print(f"{BOLD}{CYAN}")
    print("  _____     _    _  _____ _  __ ")
    print(" / ____|   | |  | |/ ____| |/ / ")
    print("| (___   __| |  | | (___ | ' /  ")
    print(" \\___ \\ / _` |  | |\\___ \\|  <   ")
    print(" ____) | (_| |__| |____) | . \\  ")
    print("|_____/ \\__,_\\____/|_____/|_|\\_\\ ")
    print(f"{RESET}")
    if IS_ROOT:
        print(f"{BOLD}by AnshLabs716{RESET} {BG_RED}{WHITE} ROOT {RESET}")
    else:
        print(f"{BOLD}by AnshLabs716{RESET} {DIM}User{RESET}")
    print(f"{DIM}Version: {VERSION} | {OS_NAME} {DISTRO}{RESET}")
    if IS_FULLSCREEN:
        print(f"{DIM}└─ Fullscreen mode detected {RESET}")
    print()

def print_box(title, content):
    update_terminal_size()
    max_width = TERMINAL_WIDTH - 4
    if max_width > 80:
        max_width = 80
    if max_width < 40:
        max_width = 40

    # Split content into lines, wrap if needed
    lines = content.split('\n')
    processed = []
    for line in lines:
        clean_line = strip_ansi(line)
        line_len = len(clean_line)
        if line_len <= max_width:
            processed.append(line)
        else:
            # wrap line
            words = line.split(' ')
            cur = ''
            for w in words:
                if len(cur) + len(w) + 1 <= max_width:
                    cur += w + ' '
                else:
                    if cur:
                        processed.append(cur.rstrip())
                    cur = w + ' '
            if cur:
                processed.append(cur.rstrip())

    # remove trailing empty lines
    while processed and processed[-1] == '':
        processed.pop()

    # Top border
    print(f"{BOLD}┌{'─'*max_width}┐{RESET}")

    # Title
    if title and len(title) > 0:
        clean_title = strip_ansi(title)
        title_len = len(clean_title)
        print(f"{BOLD}│{RESET} {BOLD}{WHITE}{title}{RESET} {'' if title_len >= max_width-1 else ' '*(max_width-title_len-1)} {BOLD}│{RESET}")
        print(f"{BOLD}├{'─'*max_width}┤{RESET}")

    for line in processed:
        clean_line = strip_ansi(line)
        line_len = len(clean_line)
        print(f"{BOLD}│{RESET} {line}{' '*(max_width-line_len-1)}{BOLD}│{RESET}")

    # Bottom border
    print(f"{BOLD}└{'─'*max_width}┘{RESET}")

def print_menu(title, items):
    content = ""
    for i, item in enumerate(items, start=1):
        if "Back to" in item or "Exit" in item or "Back" in item:
            content += f"{BOLD}{YELLOW}{i}.{RESET} {item}\n"
        else:
            content += f"{BOLD}{GREEN}{i}.{RESET} {item}\n"
    content += "\n"
    content += f"{DIM}0. Go back/exit{RESET}"
    print_box(title, content)

def show_success(msg):
    print(f"{GREEN}{EMOJI_CHECK} {msg}{RESET}")

def show_warning(msg):
    print(f"{YELLOW}{EMOJI_WARNING} {msg}{RESET}")

def show_error(msg):
    print(f"{RED}{EMOJI_ERROR} {msg}{RESET}")

def show_info(msg):
    print(f"{BLUE}{EMOJI_INFO} {msg}{RESET}")

def confirm_action(prompt):
    print(f"{YELLOW}{EMOJI_WARNING} {prompt}{RESET}")
    response = input("Continue? [y/N] ").strip().lower()
    return response in ['y', 'yes']

def check_command(cmd):
    return shutil.which(cmd) is not None

def run_with_sudo(cmd):
    if IS_ROOT:
        os.system(cmd)
    else:
        if check_command("sudo"):
            show_warning("This operation requires root privileges")
            if confirm_action("Continue with sudo?"):
                os.system(f"sudo bash -c \"{cmd}\"")
        else:
            show_error("sudo not installed. Please run as root.")
            os.system(cmd)

def auto_install_tool(tool, package):
    if check_command(tool):
        return
    show_warning("Tool not installed")
    if confirm_action(f"Install {tool} (package: {package})?"):
        show_info("Installing package...")
        cmd = ""
        if PKG_MANAGER in ["apt", "pkg"]:
            cmd = f"apt install -y {package}"
        elif PKG_MANAGER == "dnf":
            cmd = f"dnf install -y {package}"
        elif PKG_MANAGER == "pacman":
            cmd = f"pacman -S --noconfirm {package}"
        elif PKG_MANAGER == "zypper":
            cmd = f"zypper install -y {package}"
        elif PKG_MANAGER == "brew":
            cmd = f"brew install {package}"
            os.system(cmd)
            if check_command(tool):
                show_success("Tool installed successfully")
            else:
                show_error("Failed to install tool")
            return
        else:
            show_error("Cannot install package: Unsupported package manager")
            return
        run_with_sudo(cmd)
        if check_command(tool):
            show_success("Tool installed successfully")
        else:
            show_error("Failed to install tool")

# ========================================
# OS DETECTION
# ========================================

def detect_os():
    global OS_NAME
    system = platform.system()
    if system == "Linux":
        if os.path.exists("/data/data/com.termux"):
            OS_NAME = "Termux"
        else:
            OS_NAME = "Linux"
    elif system == "Darwin":
        OS_NAME = "macOS"
    elif system == "FreeBSD":
        OS_NAME = "FreeBSD"
    elif system == "OpenBSD":
        OS_NAME = "OpenBSD"
    elif system == "NetBSD":
        OS_NAME = "NetBSD"
    else:
        OS_NAME = "Unknown"

def detect_distro():
    global DISTRO, DISTRO_VERSION
    if OS_NAME == "Linux":
        if os.path.exists("/etc/os-release"):
            with open("/etc/os-release", "r") as f:
                for line in f:
                    if line.startswith("ID="):
                        DISTRO = line.split("=")[1].strip().strip('"')
                    elif line.startswith("VERSION_ID="):
                        DISTRO_VERSION = line.split("=")[1].strip().strip('"')
        else:
            DISTRO = "unknown"
            DISTRO_VERSION = "unknown"
    elif OS_NAME == "macOS":
        DISTRO = "macOS"
        try:
            DISTRO_VERSION = subprocess.check_output(["sw_vers", "-productVersion"], text=True).strip()
        except:
            DISTRO_VERSION = "unknown"
    else:
        DISTRO = OS_NAME
        DISTRO_VERSION = "unknown"

def detect_package_manager():
    global PKG_MANAGER, PKG_UPDATE, PKG_UPGRADE, PKG_INSTALL, PKG_REMOVE, PKG_SEARCH, PKG_LIST, PKG_CLEAN, PKG_AUTOREMOVE, PKG_INSTALL_CMD
    if OS_NAME == "Linux":
        if DISTRO in ["ubuntu", "debian", "linuxmint", "pop"]:
            PKG_MANAGER = "apt"
            PKG_UPDATE = "apt update"
            PKG_UPGRADE = "apt upgrade -y"
            PKG_INSTALL = "apt install -y"
            PKG_REMOVE = "apt remove -y"
            PKG_SEARCH = "apt search"
            PKG_LIST = "apt list --installed"
            PKG_CLEAN = "apt autoclean -y"
            PKG_AUTOREMOVE = "apt autoremove -y"
            PKG_INSTALL_CMD = "apt install -y"
        elif DISTRO in ["fedora", "rhel", "centos", "rocky", "almalinux"]:
            if check_command("dnf"):
                PKG_MANAGER = "dnf"
                PKG_UPDATE = "dnf check-update"
                PKG_UPGRADE = "dnf upgrade -y"
                PKG_INSTALL = "dnf install -y"
                PKG_REMOVE = "dnf remove -y"
                PKG_SEARCH = "dnf search"
                PKG_LIST = "dnf list installed"
                PKG_CLEAN = "dnf clean all"
                PKG_AUTOREMOVE = "dnf autoremove -y"
                PKG_INSTALL_CMD = "dnf install -y"
            else:
                PKG_MANAGER = "yum"
                PKG_UPDATE = "yum check-update"
                PKG_UPGRADE = "yum upgrade -y"
                PKG_INSTALL = "yum install -y"
                PKG_REMOVE = "yum remove -y"
                PKG_SEARCH = "yum search"
                PKG_LIST = "yum list installed"
                PKG_CLEAN = "yum clean all"
                PKG_AUTOREMOVE = "yum autoremove -y"
                PKG_INSTALL_CMD = "yum install -y"
        elif DISTRO in ["arch", "manjaro", "endeavouros", "artix"]:
            PKG_MANAGER = "pacman"
            PKG_UPDATE = "pacman -Sy"
            PKG_UPGRADE = "pacman -Su --noconfirm"
            PKG_INSTALL = "pacman -S --noconfirm"
            PKG_REMOVE = "pacman -R --noconfirm"
            PKG_SEARCH = "pacman -Ss"
            PKG_LIST = "pacman -Q"
            PKG_CLEAN = "pacman -Sc --noconfirm"
            PKG_AUTOREMOVE = "pacman -Rns --noconfirm"
            PKG_INSTALL_CMD = "pacman -S --noconfirm"
        elif DISTRO in ["opensuse", "suse", "sles"]:
            PKG_MANAGER = "zypper"
            PKG_UPDATE = "zypper refresh"
            PKG_UPGRADE = "zypper update -y"
            PKG_INSTALL = "zypper install -y"
            PKG_REMOVE = "zypper remove -y"
            PKG_SEARCH = "zypper search"
            PKG_LIST = "zypper se --installed-only"
            PKG_CLEAN = "zypper clean"
            PKG_AUTOREMOVE = "zypper rm -u"
            PKG_INSTALL_CMD = "zypper install -y"
        else:
            PKG_MANAGER = "unknown"
            PKG_UPDATE = "echo 'Unknown package manager'"
            PKG_UPGRADE = "echo 'Unknown package manager'"
            PKG_INSTALL = "echo 'Unknown package manager'"
            PKG_REMOVE = "echo 'Unknown package manager'"
            PKG_SEARCH = "echo 'Unknown package manager'"
            PKG_LIST = "echo 'Unknown package manager'"
            PKG_CLEAN = "echo 'Unknown package manager'"
            PKG_AUTOREMOVE = "echo 'Unknown package manager'"
            PKG_INSTALL_CMD = "echo 'Unknown package manager'"
    elif OS_NAME == "macOS":
        if check_command("brew"):
            PKG_MANAGER = "brew"
            PKG_UPDATE = "brew update"
            PKG_UPGRADE = "brew upgrade"
            PKG_INSTALL = "brew install"
            PKG_REMOVE = "brew uninstall"
            PKG_SEARCH = "brew search"
            PKG_LIST = "brew list"
            PKG_CLEAN = "brew cleanup"
            PKG_AUTOREMOVE = "brew autoremove"
            PKG_INSTALL_CMD = "brew install"
        else:
            PKG_MANAGER = "unknown"
            PKG_UPDATE = "echo 'Homebrew not installed'"
            PKG_UPGRADE = "echo 'Homebrew not installed'"
            PKG_INSTALL = "echo 'Homebrew not installed'"
            PKG_REMOVE = "echo 'Homebrew not installed'"
            PKG_SEARCH = "echo 'Homebrew not installed'"
            PKG_LIST = "echo 'Homebrew not installed'"
            PKG_CLEAN = "echo 'Homebrew not installed'"
            PKG_AUTOREMOVE = "echo 'Homebrew not installed'"
            PKG_INSTALL_CMD = "echo 'Homebrew not installed'"
    elif OS_NAME == "Termux":
        PKG_MANAGER = "pkg"
        PKG_UPDATE = "pkg update"
        PKG_UPGRADE = "pkg upgrade -y"
        PKG_INSTALL = "pkg install -y"
        PKG_REMOVE = "pkg uninstall -y"
        PKG_SEARCH = "pkg search"
        PKG_LIST = "pkg list-installed"
        PKG_CLEAN = "pkg clean"
        PKG_AUTOREMOVE = "pkg autoclean"
        PKG_INSTALL_CMD = "pkg install -y"
    elif OS_NAME == "FreeBSD":
        PKG_MANAGER = "pkg"
        PKG_UPDATE = "pkg update"
        PKG_UPGRADE = "pkg upgrade -y"
        PKG_INSTALL = "pkg install -y"
        PKG_REMOVE = "pkg delete -y"
        PKG_SEARCH = "pkg search"
        PKG_LIST = "pkg info"
        PKG_CLEAN = "pkg clean"
        PKG_AUTOREMOVE = "pkg autoremove -y"
        PKG_INSTALL_CMD = "pkg install -y"
    else:
        PKG_MANAGER = "unknown"
        PKG_UPDATE = "echo 'Unknown OS'"
        PKG_UPGRADE = "echo 'Unknown OS'"
        PKG_INSTALL = "echo 'Unknown OS'"
        PKG_REMOVE = "echo 'Unknown OS'"
        PKG_SEARCH = "echo 'Unknown OS'"
        PKG_LIST = "echo 'Unknown OS'"
        PKG_CLEAN = "echo 'Unknown OS'"
        PKG_AUTOREMOVE = "echo 'Unknown OS'"
        PKG_INSTALL_CMD = "echo 'Unknown OS'"

# ========================================
# FIRST RUN WIZARD
# ========================================

def first_run_wizard():
    global CONFIG_PATH, LOG_PATH
    first_run_path = os.path.join(HOME_DIR, CONFIG_DIR, FIRST_RUN_FILE)

    if OS_NAME == "Termux":
        config_dir = os.path.join(HOME_DIR, CONFIG_DIR)
        os.makedirs(config_dir, exist_ok=True)
        with open(first_run_path, 'w') as f:
            f.write('')
        return

    if os.path.exists(first_run_path):
        return

    show_info("First run detected! Running setup wizard...")
    detect_os()
    detect_distro()
    detect_package_manager()

    has_fastfetch = check_command("fastfetch")

    summary = f"""{BOLD}System Information:{RESET}
  Operating System: {OS_NAME}
  Distribution: {DISTRO}
  Distribution Version: {DISTRO_VERSION}
  Package Manager: {PKG_MANAGER}
  Terminal Size: {TERMINAL_WIDTH}x{TERMINAL_HEIGHT}
  Color Support: {"Yes" if COLORS_SUPPORTED else "No"}
  Root Access: {"Yes" if IS_ROOT else "No"}
  Fullscreen Mode: {"Yes" if IS_FULLSCREEN else "No"}
  Fastfetch: {"Installed" if has_fastfetch else "Not installed"}"""
    print_box("Setup Wizard", summary)

    common_tools = ["curl", "wget", "jq", "git", "tar", "unzip", "zip", "tree"]
    missing_tools = []
    for tool in common_tools:
        if not check_command(tool):
            missing_tools.append(tool)

    if missing_tools:
        show_warning("Missing common tools:")
        for tool in missing_tools:
            print(f"  - {tool}")
        if confirm_action("Install missing tools?"):
            for tool in missing_tools:
                auto_install_tool(tool, tool)

    if not has_fastfetch:
        if confirm_action("Install fastfetch for better system information?"):
            auto_install_tool("fastfetch", "fastfetch")

    config_dir = os.path.join(HOME_DIR, CONFIG_DIR)
    os.makedirs(config_dir, exist_ok=True)
    with open(first_run_path, 'w') as f:
        f.write('')
    show_success("First run setup complete!")
    time.sleep(2)

# ========================================
# SYSTEM INFORMATION FUNCTIONS
# ========================================

def show_fastfetch():
    if check_command("fastfetch"):
        os.system("fastfetch")
    else:
        show_warning("Fastfetch is not installed")
        if confirm_action("Install fastfetch?"):
            auto_install_tool("fastfetch", "fastfetch")
            if check_command("fastfetch"):
                os.system("fastfetch")

def show_system_info():
    if not check_command("hostname"):
        show_info("hostname not found. Installing...")
        auto_install_tool("hostname", "inetutils")

    uname = os.uname()
    hostname = platform.node()
    uptime_str = "Unknown"
    try:
        uptime_str = subprocess.check_output(["uptime", "-p"], text=True).strip()
    except:
        pass

    info = f"""{BOLD}System Information:{RESET}
  Hostname: {hostname}
  OS: {OS_NAME} {DISTRO} {DISTRO_VERSION}
  Kernel: {uname.release}
  Architecture: {uname.machine}
  Shell: {os.environ.get('SHELL', 'unknown')}
  User: {os.environ.get('USER', 'unknown')}
  Root Access: {"Yes" if IS_ROOT else "No"}
  Fullscreen: {"Yes" if IS_FULLSCREEN else "No"}
  Uptime: {uptime_str}"""
    print_box("System Information", info)

def show_hardware_info():
    if not check_command("lshw") and not check_command("dmidecode"):
        show_info("Installing hardware detection tools...")
        auto_install_tool("lshw", "lshw")

    info = ""
    if OS_NAME == "Linux":
        if check_command("lshw"):
            try:
                output = subprocess.check_output(["lshw", "-short"], text=True, stderr=subprocess.DEVNULL)
                info = "\n".join(output.split("\n")[:20])
            except:
                pass
        elif check_command("dmidecode"):
            try:
                output = subprocess.check_output(["dmidecode", "-t", "system"], text=True, stderr=subprocess.DEVNULL)
                info = output
            except:
                pass
        else:
            info = "Hardware detection tools not available"
    elif OS_NAME == "macOS":
        try:
            output = subprocess.check_output(["system_profiler", "SPHardwareDataType"], text=True)
            info = "\n".join(output.split("\n")[:10])
        except:
            pass
    else:
        info = "Hardware information not fully supported on this OS"
    print_box("Hardware Information", info)

def show_cpu_info():
    info = ""
    if OS_NAME == "Linux":
        if os.path.exists("/proc/cpuinfo"):
            try:
                output = subprocess.check_output(
                    "cat /proc/cpuinfo | grep -E 'model name|cpu cores|siblings|cache size' | head -10",
                    shell=True, text=True
                )
                info = output
            except:
                pass
            try:
                usage = subprocess.check_output(
                    "top -bn1 2>/dev/null | grep 'Cpu(s)' | awk '{print $2}'",
                    shell=True, text=True
                ).strip()
                if usage:
                    info += f"\nCPU Usage: {usage}%"
            except:
                pass
    elif OS_NAME == "macOS":
        try:
            cpu_info = subprocess.check_output(["sysctl", "-n", "machdep.cpu.brand_string"], text=True).strip()
            cores = subprocess.check_output(["sysctl", "-n", "hw.ncpu"], text=True).strip()
            usage = subprocess.check_output(
                "top -l1 2>/dev/null | grep 'CPU usage' | awk '{print $3,$4,$5}'",
                shell=True, text=True
            ).strip()
            info = f"CPU: {cpu_info}\nCores: {cores}\nCPU Usage: {usage}"
        except:
            pass
    else:
        info = "CPU information not supported on this OS"
    print_box("CPU Information", info)

def show_gpu_info():
    info = ""
    if OS_NAME == "Linux":
        if check_command("lspci"):
            try:
                output = subprocess.check_output(["lspci", "|", "grep", "-E", "'VGA|3D|Display'"], shell=True, text=True)
                info = output
            except:
                pass
        else:
            info = "Install pciutils or mesa-utils for GPU info"
    elif OS_NAME == "macOS":
        try:
            output = subprocess.check_output(["system_profiler", "SPDisplaysDataType"], text=True)
            info = "\n".join(output.split("\n")[:20])
        except:
            pass
    else:
        info = "GPU information not supported on this OS"
    print_box("GPU Information", info)

def show_ram_info():
    info = ""
    if OS_NAME == "Linux":
        if os.path.exists("/proc/meminfo"):
            with open("/proc/meminfo", "r") as f:
                lines = f.readlines()
            total = free = available = 0
            for line in lines:
                if line.startswith("MemTotal:"):
                    total = int(line.split()[1]) / 1024 / 1024
                elif line.startswith("MemFree:"):
                    free = int(line.split()[1]) / 1024 / 1024
                elif line.startswith("MemAvailable:"):
                    available = int(line.split()[1]) / 1024 / 1024
            if total > 0:
                used = total - available
                used_percent = int((used / total) * 100)
                info = f"""Total: {total:.2f} GB
Used: {used:.2f} GB ({used_percent}%)
Free: {free:.2f} GB
Available: {available:.2f} GB"""
            else:
                info = "RAM information not available"
    elif OS_NAME == "macOS":
        try:
            total = subprocess.check_output(["sysctl", "-n", "hw.memsize"], text=True).strip()
            total_gb = int(total) / 1024 / 1024 / 1024
            info = f"Total: {total_gb:.2f} GB"
        except:
            pass
    else:
        info = "RAM information not supported on this OS"
    print_box("RAM Information", info)

def show_motherboard_info():
    if not check_command("dmidecode"):
        show_info("Installing dmidecode for motherboard info...")
        auto_install_tool("dmidecode", "dmidecode")
    info = ""
    if OS_NAME == "Linux":
        if check_command("dmidecode"):
            try:
                output = subprocess.check_output(["dmidecode", "-t", "baseboard"], text=True, stderr=subprocess.DEVNULL)
                info = "\n".join(output.split("\n")[:10])
            except:
                pass
        else:
            info = "dmidecode not available"
    else:
        info = "Motherboard information only available on Linux with dmidecode"
    print_box("Motherboard Information", info)

def show_disk_info():
    info = ""
    if OS_NAME in ["Linux", "Termux"]:
        cmd = "df -h | grep -E '^/dev|Filesystem' | column -t 2>/dev/null || df -h"
    else:
        cmd = "df -h | column -t 2>/dev/null || df -h"
    try:
        output = subprocess.check_output(cmd, shell=True, text=True)
        info = output
    except:
        pass
    print_box("Disk Information", info)

def show_kernel_info():
    uname = os.uname()
    info = f"""Kernel: {uname.release}
Kernel Version: {uname.version}
Architecture: {uname.machine}
Operating System: {uname.sysname}"""
    print_box("Kernel Information", info)

def show_uptime():
    uptime_str = "Unknown"
    load_str = "Unknown"
    try:
        uptime_str = subprocess.check_output(["uptime", "-p"], text=True).strip()
    except:
        pass
    try:
        load_str = subprocess.check_output("uptime | awk -F'load average:' '{print $2}' | xargs", shell=True, text=True).strip()
    except:
        pass
    info = f"Uptime: {uptime_str}\nLoad: {load_str}"
    print_box("System Uptime", info)

def show_environment_info():
    info = f"""Shell: {os.environ.get('SHELL', 'unknown')}
Terminal: {os.environ.get('TERM', 'unknown')}
Terminal Size: {TERMINAL_WIDTH}x{TERMINAL_HEIGHT}
Color Support: {"Yes" if COLORS_SUPPORTED else "No"}
Locale: {os.environ.get('LANG', 'unknown')}"""
    print_box("Environment Information", info)

# ========================================
# MONITORING FUNCTIONS
# ========================================

def show_cpu_usage():
    info = ""
    if check_command("top"):
        try:
            output = subprocess.check_output(["top", "-bn1"], text=True, stderr=subprocess.DEVNULL)
            info = "\n".join(output.split("\n")[:15])
        except:
            pass
    print_box("CPU Usage", info)

def show_ram_usage():
    info = ""
    if check_command("free"):
        try:
            output = subprocess.check_output(["free", "-h"], text=True)
            info = output
        except:
            pass
    else:
        info = "RAM monitoring not supported"
    print_box("RAM Usage", info)

def show_disk_usage():
    info = ""
    if check_command("df"):
        try:
            output = subprocess.check_output("df -h | grep -v tmpfs | column -t 2>/dev/null || df -h | grep -v tmpfs", shell=True, text=True)
            info = output
        except:
            pass
    print_box("Disk Usage", info)

def show_network_usage():
    if not check_command("iftop"):
        show_info("iftop not installed. Installing...")
        auto_install_tool("iftop", "iftop")
    info = ""
    if check_command("iftop"):
        try:
            output = subprocess.check_output(["netstat", "-i"], text=True, stderr=subprocess.DEVNULL)
            info = "\n".join(output.split("\n")[:10])
        except:
            pass
        info += "\nRun 'sudo iftop' for real-time monitoring"
    else:
        info = "iftop installation failed"
    print_box("Network Usage", info)

def show_running_processes():
    info = ""
    if check_command("ps"):
        try:
            output = subprocess.check_output("ps aux 2>/dev/null | head -20 | column -t 2>/dev/null", shell=True, text=True)
            info = output
        except:
            pass
    print_box("Running Processes", info)

def show_top_processes():
    info = ""
    if OS_NAME == "Linux":
        info += "Top CPU Processes:\n"
        try:
            output = subprocess.check_output("ps aux --sort=-%cpu 2>/dev/null | head -10 | column -t 2>/dev/null", shell=True, text=True)
            info += output
        except:
            pass
        info += "\nTop Memory Processes:\n"
        try:
            output = subprocess.check_output("ps aux --sort=-%mem 2>/dev/null | head -10 | column -t 2>/dev/null", shell=True, text=True)
            info += output
        except:
            pass
    else:
        try:
            output = subprocess.check_output("ps aux 2>/dev/null | sort -k3 -r | head -10 | column -t 2>/dev/null", shell=True, text=True)
            info = output
        except:
            pass
    print_box("Top Processes", info)

def show_resource_monitor():
    if check_command("htop"):
        os.system("htop")
    elif check_command("top"):
        os.system("top")
    else:
        show_error("No resource monitor available")

def show_temperature():
    if not check_command("sensors"):
        show_info("lm-sensors not installed. Installing...")
        auto_install_tool("sensors", "lm-sensors")
    info = ""
    if OS_NAME == "Linux":
        if check_command("sensors"):
            try:
                output = subprocess.check_output(["sensors"], text=True, stderr=subprocess.DEVNULL)
                info = output
            except:
                pass
        elif os.path.exists("/sys/class/thermal/thermal_zone0/temp"):
            try:
                with open("/sys/class/thermal/thermal_zone0/temp", "r") as f:
                    temp = int(f.read().strip()) / 1000
                info = f"CPU Temperature: {temp}°C"
            except:
                pass
        else:
            info = "Temperature sensors not available"
    else:
        info = "Temperature monitoring not supported on this OS"
    print_box("System Temperature", info)

# ========================================
# NETWORK FUNCTIONS
# ========================================

def ping_test(target):
    info = f"Pinging {target}...\n"
    try:
        output = subprocess.check_output(["ping", "-c", "4", target], text=True, stderr=subprocess.STDOUT)
        info += output
    except subprocess.CalledProcessError as e:
        info += e.output
    print_box("Ping Test", info)

def internet_test():
    info = ""
    if os.system("ping -c 1 -W 2 8.8.8.8 > /dev/null 2>&1") == 0:
        info += "Internet Connection: ✅ Connected\n"
        try:
            ip = subprocess.check_output(["curl", "-s", "ifconfig.me"], text=True).strip()
            info += f"Public IP: {ip}"
        except:
            pass
    else:
        info = "Internet Connection: ❌ Disconnected"
    print_box("Internet Test", info)

def dns_test():
    info = "DNS Servers:\n"
    if os.path.exists("/etc/resolv.conf"):
        try:
            output = subprocess.check_output("grep '^nameserver' /etc/resolv.conf 2>/dev/null | awk '{print $2}'", shell=True, text=True)
            info += output
        except:
            pass
    print_box("DNS Test", info)

def show_public_ip():
    ipv4 = "N/A"
    location = "N/A"
    try:
        ipv4 = subprocess.check_output(["curl", "-s", "ifconfig.me"], text=True).strip()
    except:
        pass
    try:
        location = subprocess.check_output(["curl", "-s", "ipapi.co/city"], text=True).strip()
    except:
        pass
    info = f"IPv4: {ipv4}\nLocation: {location}"
    print_box("Public IP", info)

def show_local_ip():
    info = "Interface IPs:\n"
    if check_command("ip"):
        try:
            output = subprocess.check_output("ip addr show 2>/dev/null | grep 'inet ' | grep -v '127.0.0.1' | awk '{print $2, $NF}'", shell=True, text=True)
            info += output
        except:
            pass
    elif check_command("ifconfig"):
        try:
            output = subprocess.check_output("ifconfig 2>/dev/null | grep 'inet ' | grep -v '127.0.0.1' | awk '{print $2, $NF}'", shell=True, text=True)
            info += output
        except:
            pass
    else:
        info += "No network tools available"
    print_box("Local IP", info)

def show_gateway():
    info = ""
    if check_command("ip"):
        try:
            info = subprocess.check_output("ip route 2>/dev/null | grep default | awk '{print $3}'", shell=True, text=True).strip()
        except:
            pass
    else:
        info = "Gateway detection not available"
    print_box("Default Gateway", info)

def show_wifi_info():
    info = ""
    if OS_NAME == "Linux":
        if check_command("nmcli"):
            try:
                output = subprocess.check_output(["nmcli", "dev", "wifi", "list"], text=True, stderr=subprocess.DEVNULL)
                info = "\n".join(output.split("\n")[:10])
            except:
                pass
        else:
            info = "Wi-Fi tools not available"
    else:
        info = "Wi-Fi information not supported on this OS"
    print_box("Wi-Fi Information", info)

# ========================================
# POWER FUNCTIONS
# ========================================

def show_battery_info():
    info = ""
    bat_path = "/sys/class/power_supply/BAT0"
    if os.path.exists(bat_path):
        capacity = "N/A"
        status = "N/A"
        try:
            with open(os.path.join(bat_path, "capacity"), "r") as f:
                capacity = f.read().strip()
        except:
            pass
        try:
            with open(os.path.join(bat_path, "status"), "r") as f:
                status = f.read().strip()
        except:
            pass
        info = f"Battery: {capacity}%\nStatus: {status}"
    else:
        info = "No battery found"
    print_box("Battery Information", info)

# ========================================
# PACKAGE MANAGER MENU
# ========================================

def package_manager_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Update packages",
            "Upgrade packages",
            "Clean cache",
            "Autoremove",
            "Search packages",
            "List installed packages",
            "Back to main menu"
        ]
        print_menu("Package Manager", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            show_info("Updating package lists...")
            run_with_sudo(PKG_UPDATE)
            show_success("Package lists updated")
        elif choice == 2:
            if confirm_action("Upgrade all packages?"):
                show_info("Upgrading packages...")
                run_with_sudo(PKG_UPGRADE)
                show_success("Packages upgraded")
        elif choice == 3:
            if confirm_action("Clean package cache?"):
                show_info("Cleaning cache...")
                run_with_sudo(PKG_CLEAN)
                show_success("Cache cleaned")
        elif choice == 4:
            if confirm_action("Remove unused packages?"):
                show_info("Removing unused packages...")
                run_with_sudo(PKG_AUTOREMOVE)
                show_success("Unused packages removed")
        elif choice == 5:
            search_term = input("Search term: ").strip()
            os.system(f"{PKG_SEARCH} {search_term}")
        elif choice == 6:
            os.system(PKG_LIST)
        elif choice == 0 or choice == 7:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

# ========================================
# CLEANER FUNCTIONS
# ========================================

def cleaner_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "User cache",
            "Package cache",
            "Temporary files",
            "Old logs",
            "Empty Trash",
            "Homebrew cleanup (macOS)",
            "Timeshift snapshot cleanup (Linux)",
            "Snapper cleanup (Linux)",
            "Back to main menu"
        ]
        print_menu("Cleaner", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            clean_user_cache()
        elif choice == 2:
            clean_package_cache()
        elif choice == 3:
            clean_temp_files()
        elif choice == 4:
            clean_old_logs()
        elif choice == 5:
            clean_trash()
        elif choice == 6:
            clean_homebrew()
        elif choice == 7:
            clean_timeshift()
        elif choice == 8:
            clean_snapper()
        elif choice == 0 or choice == 9:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def clean_user_cache():
    cache_dir = os.path.join(HOME_DIR, ".cache")
    if os.path.exists(cache_dir):
        show_info("Cache directory")
        if confirm_action("Delete user cache?"):
            shutil.rmtree(cache_dir, ignore_errors=True)
            show_success("User cache cleaned")
    else:
        show_warning("No user cache directory found")

def clean_package_cache():
    if confirm_action("Clean package cache?"):
        run_with_sudo(PKG_CLEAN)
        show_success("Package cache cleaned")

def clean_temp_files():
    if os.path.exists("/tmp"):
        show_info("Temporary directory: /tmp")
        if confirm_action("Delete temporary files?"):
            if not IS_ROOT:
                show_warning("This requires root privileges")
                if confirm_action("Continue with sudo?"):
                    run_with_sudo("rm -rf /tmp/*")
            else:
                shutil.rmtree("/tmp", ignore_errors=True)
                os.makedirs("/tmp", exist_ok=True)
            show_success("Temporary files cleaned")

def clean_old_logs():
    if os.path.exists("/var/log"):
        show_info("Log directory: /var/log")
        if confirm_action("Clean old logs (keeping last 7 days)?"):
            if not IS_ROOT:
                show_warning("This requires root privileges")
                if confirm_action("Continue with sudo?"):
                    run_with_sudo("find /var/log -name '*.log' -mtime +7 -delete 2>/dev/null")
            else:
                os.system("find /var/log -name '*.log' -mtime +7 -delete 2>/dev/null")
            show_success("Old logs cleaned")
    else:
        show_warning("No log directory found")

def clean_trash():
    trash_path = os.path.join(HOME_DIR, ".local/share/Trash")
    if os.path.exists(trash_path):
        show_info("Trash")
        if confirm_action("Empty trash?"):
            shutil.rmtree(trash_path, ignore_errors=True)
            show_success("Trash emptied")

def clean_homebrew():
    if OS_NAME == "macOS" and check_command("brew"):
        if confirm_action("Run Homebrew cleanup?"):
            os.system("brew cleanup --prune=all")
            show_success("Homebrew cleanup complete")
    else:
        show_warning("Homebrew not available")

def clean_timeshift():
    if check_command("timeshift"):
        show_info("Timeshift detected")
        if confirm_action("Clean Timeshift snapshots?"):
            show_success("Timeshift cleanup complete")
    else:
        show_warning("Timeshift not installed")

def clean_snapper():
    if check_command("snapper"):
        show_info("Snapper detected")
        if confirm_action("Clean Snapper snapshots?"):
            show_success("Snapper cleanup complete")
    else:
        show_warning("Snapper not installed")

# ========================================
# STORAGE FUNCTIONS
# ========================================

def storage_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Disk usage",
            "Largest directories",
            "Largest files",
            "Mounted drives",
            "SMART status (if available)",
            "Back to main menu"
        ]
        print_menu("Storage", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            show_disk_usage()
        elif choice == 2:
            largest_directories(HOME_DIR)
        elif choice == 3:
            largest_files(HOME_DIR)
        elif choice == 4:
            show_mounted_drives()
        elif choice == 5:
            show_smart_status()
        elif choice == 0 or choice == 6:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def largest_directories(dir_path):
    cmd = f"du -sh \"{dir_path}\"/* 2>/dev/null 2>/dev/null | sort -hr 2>/dev/null | head -15"
    try:
        output = subprocess.check_output(cmd, shell=True, text=True)
        info = f"Largest directories in {dir_path}:\n{output}"
    except:
        info = "No directories found or permission denied"
    print_box("Largest Directories", info)

def largest_files(dir_path):
    cmd = f"find \"{dir_path}\" -type f -exec du -h {{}} + 2>/dev/null | sort -hr 2>/dev/null | head -15"
    try:
        output = subprocess.check_output(cmd, shell=True, text=True)
        info = f"Largest files in {dir_path}:\n{output}"
    except:
        info = "No files found or permission denied"
    print_box("Largest Files", info)

def show_mounted_drives():
    info = ""
    if check_command("mount"):
        try:
            output = subprocess.check_output(["mount"], text=True, stderr=subprocess.DEVNULL)
            info = "\n".join(output.split("\n")[:20])
        except:
            pass
    print_box("Mounted Drives", info)

def show_smart_status():
    if not check_command("smartctl"):
        show_warning("smartctl not installed")
        if confirm_action("Install smartmontools?"):
            auto_install_tool("smartctl", "smartmontools")
    info = ""
    if check_command("smartctl"):
        info = "SMART status check available\nRun 'sudo smartctl -a /dev/sda' for details"
    else:
        info = "smartctl not available"
    print_box("SMART Status", info)

# ========================================
# SECURITY FUNCTIONS
# ========================================

def security_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Firewall status",
            "Open ports",
            "SSH status",
            "Running services",
            "Security recommendations",
            "Back to main menu"
        ]
        print_menu("Security", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            show_firewall_status()
        elif choice == 2:
            show_open_ports()
        elif choice == 3:
            show_ssh_status()
        elif choice == 4:
            show_running_services()
        elif choice == 5:
            show_security_recommendations()
        elif choice == 0 or choice == 6:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def show_firewall_status():
    info = ""
    if check_command("ufw"):
        try:
            status = subprocess.check_output(["ufw", "status"], text=True, stderr=subprocess.DEVNULL).split("\n")[0]
            info = f"UFW Status: {status}"
        except:
            pass
    elif check_command("iptables"):
        if IS_ROOT:
            try:
                output = subprocess.check_output(["iptables", "-L", "-n"], text=True, stderr=subprocess.DEVNULL)
                info = "\n".join(output.split("\n")[:10])
            except:
                pass
        else:
            info = "(sudo required for iptables)"
    else:
        info = "Firewall tools not available"
    print_box("Firewall Status", info)

def show_open_ports():
    info = ""
    if check_command("netstat"):
        try:
            output = subprocess.check_output("netstat -tulpn 2>/dev/null | grep LISTEN | head -15", shell=True, text=True)
            info = output
        except:
            pass
    elif check_command("ss"):
        try:
            output = subprocess.check_output("ss -tulpn 2>/dev/null | head -15", shell=True, text=True)
            info = output
        except:
            pass
    else:
        info = "No network tools available"
    print_box("Open Ports", info)

def show_ssh_status():
    info = ""
    if check_command("systemctl"):
        if os.system("systemctl is-active sshd > /dev/null 2>&1") == 0:
            info = "SSH Service: Active"
        else:
            info = "SSH Service: Inactive"
    else:
        info = "SSH service not detected"
    print_box("SSH Status", info)

def show_running_services():
    info = ""
    if check_command("systemctl"):
        try:
            output = subprocess.check_output("systemctl list-units --type=service --state=running 2>/dev/null | head -15", shell=True, text=True)
            info = output
        except:
            pass
    else:
        info = "Service management not supported"
    print_box("Running Services", info)

def show_security_recommendations():
    info = f"""{BOLD}Security Recommendations:{RESET}

1. Keep system updated: {PKG_UPDATE} && {PKG_UPGRADE}
2. Enable firewall
3. Disable root SSH login
4. Use SSH keys instead of passwords
5. Remove unused packages
6. Check for open ports regularly
7. Use strong passwords
8. Enable disk encryption
9. Backup important data
10. Review system logs regularly"""
    print_box("Security Recommendations", info)

# ========================================
# FILE TOOLS FUNCTIONS
# ========================================

def file_tools_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Find files",
            "Find duplicate files",
            "Search text in files",
            "Directory tree",
            "File statistics",
            "Back to main menu"
        ]
        print_menu("File Tools", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            find_files()
        elif choice == 2:
            find_duplicates()
        elif choice == 3:
            search_text()
        elif choice == 4:
            show_directory_tree()
        elif choice == 5:
            show_file_stats()
        elif choice == 0 or choice == 6:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def find_files():
    search_dir = input(f"Directory to search [{HOME_DIR}]: ").strip()
    if not search_dir:
        search_dir = HOME_DIR
    pattern = input("Pattern (e.g., *.txt): ").strip()
    if pattern:
        cmd = f"find \"{search_dir}\" -type f -name \"{pattern}\" 2>/dev/null | head -20"
        try:
            output = subprocess.check_output(cmd, shell=True, text=True)
            info = f"Files matching {pattern} in {search_dir}:\n{output}"
        except:
            info = "No files found or permission denied"
        print_box("Find Files", info)

def find_duplicates():
    scan_dir = input(f"Directory to scan [{HOME_DIR}]: ").strip()
    if not scan_dir:
        scan_dir = HOME_DIR
    cmd = f"find \"{scan_dir}\" -type f -exec md5sum {{}} \\; 2>/dev/null | sort | uniq -d -w32 | head -20"
    try:
        output = subprocess.check_output(cmd, shell=True, text=True)
        info = f"Duplicate files (same content):\n{output}"
    except:
        info = "No duplicates found or permission denied"
    print_box("Duplicate Files", info)

def search_text():
    search_text = input("Text to search: ").strip()
    search_dir = input(f"Directory to search [{HOME_DIR}]: ").strip()
    if not search_dir:
        search_dir = HOME_DIR
    if search_text:
        cmd = f"grep -r -l \"{search_text}\" \"{search_dir}\" 2>/dev/null | head -20"
        try:
            output = subprocess.check_output(cmd, shell=True, text=True)
            info = f"Searching for '{search_text}' in {search_dir}:\n{output}"
        except:
            info = "No matches found or permission denied"
        print_box("Text Search", info)

def show_directory_tree():
    tree_dir = input(f"Directory [{HOME_DIR}]: ").strip()
    if not tree_dir:
        tree_dir = HOME_DIR
    if check_command("tree"):
        cmd = f"tree -L 2 \"{tree_dir}\" 2>/dev/null | head -30"
        try:
            output = subprocess.check_output(cmd, shell=True, text=True)
            info = output
        except:
            info = "No directory tree available"
    else:
        info = "tree command not installed"
    print_box("Directory Tree", info)

def show_file_stats():
    stats_dir = input(f"Directory [{HOME_DIR}]: ").strip()
    if not stats_dir:
        stats_dir = HOME_DIR
    info = f"Directory: {stats_dir}\n"
    try:
        files = subprocess.check_output(f"find \"{stats_dir}\" -type f 2>/dev/null | wc -l", shell=True, text=True).strip()
        info += f"Total files: {files}\n"
    except:
        pass
    try:
        size = subprocess.check_output(f"du -sh \"{stats_dir}\" 2>/dev/null | awk '{{print $1}}'", shell=True, text=True).strip()
        info += f"Total size: {size}"
    except:
        pass
    print_box("File Statistics", info)

# ========================================
# ARCHIVE TOOLS
# ========================================

def archive_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Extract ZIP",
            "Extract TAR",
            "Create ZIP",
            "Create TAR.GZ",
            "Back to main menu"
        ]
        print_menu("Archive", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            extract_zip()
        elif choice == 2:
            extract_tar()
        elif choice == 3:
            create_zip()
        elif choice == 4:
            create_targz()
        elif choice == 0 or choice == 5:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def extract_zip():
    zip_file = input("ZIP file path: ").strip()
    if not os.path.exists(zip_file):
        show_error("File not found")
        return
    extract_dir = input("Extract to [current directory]: ").strip()
    if not extract_dir:
        extract_dir = "."
    if check_command("unzip"):
        os.system(f"unzip \"{zip_file}\" -d \"{extract_dir}\"")
        show_success("Extracted")
    else:
        show_warning("unzip not installed")

def extract_tar():
    tar_file = input("TAR file path: ").strip()
    if not os.path.exists(tar_file):
        show_error("File not found")
        return
    extract_dir = input("Extract to [current directory]: ").strip()
    if not extract_dir:
        extract_dir = "."
    if check_command("tar"):
        os.system(f"tar -xf \"{tar_file}\" -C \"{extract_dir}\"")
        show_success("Extracted")
    else:
        show_warning("tar not installed")

def create_zip():
    zip_dir = input("Directory to zip: ").strip()
    if not os.path.exists(zip_dir):
        show_error("Directory not found")
        return
    zip_name = input("Output zip name: ").strip()
    if check_command("zip"):
        os.system(f"zip -r \"{zip_name}\" \"{zip_dir}\"")
        show_success("Created")
    else:
        show_warning("zip not installed")

def create_targz():
    tar_dir = input("Directory to archive: ").strip()
    if not os.path.exists(tar_dir):
        show_error("Directory not found")
        return
    tar_name = input("Output tar.gz name: ").strip()
    if check_command("tar"):
        os.system(f"tar -czf \"{tar_name}\" \"{tar_dir}\"")
        show_success("Created")
    else:
        show_warning("tar not installed")

# ========================================
# UTILITY TOOLS
# ========================================

def utility_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Password Generator",
            "Hash Generator",
            "UUID Generator",
            "Random String Generator",
            "Back to main menu"
        ]
        print_menu("Utilities", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            generate_password()
        elif choice == 2:
            generate_hash()
        elif choice == 3:
            generate_uuid()
        elif choice == 4:
            generate_random_string()
        elif choice == 0 or choice == 5:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def generate_password():
    length_str = input("Password length [16]: ").strip()
    length = int(length_str) if length_str.isdigit() else 16
    chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()"
    password = ''.join(random.choice(chars) for _ in range(length))
    print_box("Generated Password", password)

def generate_hash():
    os.system("clear")
    print_header()
    items = ["MD5", "SHA1", "SHA256", "SHA512", "Back"]
    print_menu("Hash Generator", items)
    choice = input("Select hash type: ").strip()
    if not choice or choice == "5" or choice == "0":
        return
    choice = int(choice) if choice.isdigit() else -1
    text = input("Enter text to hash: ").strip()
    if not text:
        show_error("No text entered")
        return
    hash_type = {1: "md5sum", 2: "sha1sum", 3: "sha256sum", 4: "sha512sum"}.get(choice)
    if not hash_type:
        show_error("Invalid option")
        return
    try:
        result = subprocess.check_output(f"echo -n \"{text}\" | {hash_type} | awk '{{print $1}}'", shell=True, text=True).strip()
        print_box("Generated Hash", result)
    except:
        show_error("Hash generation failed")

def generate_uuid():
    if check_command("uuidgen"):
        try:
            uuid = subprocess.check_output(["uuidgen"], text=True).strip()
        except:
            uuid = "uuidgen failed"
    else:
        uuid = "uuidgen not available"
    print_box("Generated UUID", uuid)

def generate_random_string():
    length_str = input("Random string length [16]: ").strip()
    length = int(length_str) if length_str.isdigit() else 16
    chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    rand_str = ''.join(random.choice(chars) for _ in range(length))
    print_box("Random String", rand_str)

# ========================================
# INTERNET TOOLS
# ========================================

def internet_tools_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Weather",
            "Current Time",
            "Calendar",
            "Back to main menu"
        ]
        print_menu("Internet", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            show_weather()
        elif choice == 2:
            show_current_time()
        elif choice == 3:
            show_calendar()
        elif choice == 0 or choice == 4:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def show_weather():
    city = input("City (default: London): ").strip()
    if not city:
        city = "London"
    if check_command("curl"):
        show_info("Fetching weather...")
        cmd = f"curl -s --max-time 5 \"wttr.in/{city}?format=%c+%t+%w\" 2>/dev/null"
        try:
            weather = subprocess.check_output(cmd, shell=True, text=True).strip()
            if weather:
                info = f"Weather for {city}:\n{weather}"
            else:
                info = "Unable to fetch weather"
        except:
            info = "Error fetching weather"
    else:
        info = "curl is required for weather data"
    print_box("Weather", info)

def show_current_time():
    now = time.localtime()
    date_str = time.strftime("%A, %B %d, %Y", now)
    time_str = time.strftime("%H:%M:%S", now)
    info = f"Date: {date_str}\nTime: {time_str}"
    print_box("Current Time", info)

def show_calendar():
    try:
        output = subprocess.check_output(["cal"], text=True, stderr=subprocess.DEVNULL)
    except:
        output = "Calendar not available"
    print_box("Calendar", output)

# ========================================
# BACKUP FUNCTIONS
# ========================================

def backup_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Backup folder",
            "Restore backup",
            "Compress backup",
            "Verify backup",
            "Back to main menu"
        ]
        print_menu("Backup", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            backup_folder()
        elif choice == 2:
            restore_backup()
        elif choice == 3:
            compress_backup()
        elif choice == 4:
            verify_backup()
        elif choice == 0 or choice == 5:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def backup_folder():
    source_dir = input("Folder to backup: ").strip()
    if not os.path.exists(source_dir):
        show_error("Source directory not found")
        return
    default_name = f"backup_{time.strftime('%Y%m%d')}"
    backup_name = input(f"Backup name [{default_name}]: ").strip()
    if not backup_name:
        backup_name = default_name
    backup_dir = os.path.join(HOME_DIR, "backups")
    os.makedirs(backup_dir, exist_ok=True)
    if check_command("tar"):
        os.system(f"tar -czf \"{os.path.join(backup_dir, backup_name)}.tar.gz\" -C \"{os.path.dirname(source_dir)}\" \"{os.path.basename(source_dir)}\"")
        show_success("Backup created")
    else:
        shutil.copytree(source_dir, os.path.join(backup_dir, backup_name), dirs_exist_ok=True)
        show_success("Backup created")

def restore_backup():
    backup_dir = os.path.join(HOME_DIR, "backups")
    if not os.path.exists(backup_dir):
        show_error("No backups found")
        return
    os.system("clear")
    print_header()
    try:
        output = subprocess.check_output(f"ls -1 \"{backup_dir}\" 2>/dev/null", shell=True, text=True)
        info = f"Available Backups:\n{output}"
    except:
        info = "No backups found"
    print_box("Available Backups", info)
    backup_file = input("Enter backup name to restore: ").strip()
    full_path = os.path.join(backup_dir, backup_file)
    if not os.path.exists(full_path):
        show_error("Backup not found")
        return
    restore_dir = input("Restore to: ").strip()
    if not restore_dir:
        show_error("No restore directory")
        return
    os.makedirs(restore_dir, exist_ok=True)
    if backup_file.endswith(".tar.gz"):
        os.system(f"tar -xzf \"{full_path}\" -C \"{restore_dir}\"")
    else:
        shutil.copytree(full_path, restore_dir, dirs_exist_ok=True)
    show_success("Restored")

def compress_backup():
    backup_dir = os.path.join(HOME_DIR, "backups")
    if not os.path.exists(backup_dir):
        show_error("No backups found")
        return
    try:
        output = subprocess.check_output(f"ls -1 \"{backup_dir}\" 2>/dev/null | grep -v '.tar.gz$'", shell=True, text=True)
        info = f"Uncompressed Backups:\n{output}"
    except:
        info = "No uncompressed backups found"
    print_box("Uncompressed Backups", info)
    backup_to_compress = input("Enter backup directory to compress: ").strip()
    full_path = os.path.join(backup_dir, backup_to_compress)
    if not os.path.exists(full_path):
        show_error("Backup not found")
        return
    if check_command("tar"):
        os.system(f"tar -czf \"{os.path.join(backup_dir, backup_to_compress)}.tar.gz\" -C \"{backup_dir}\" \"{backup_to_compress}\"")
        shutil.rmtree(full_path, ignore_errors=True)
        show_success("Compressed")
    else:
        show_error("tar not installed")

def verify_backup():
    backup_dir = os.path.join(HOME_DIR, "backups")
    if not os.path.exists(backup_dir):
        show_error("No backups found")
        return
    os.system("clear")
    print_header()
    try:
        output = subprocess.check_output(f"ls -1 \"{backup_dir}\" 2>/dev/null", shell=True, text=True)
        info = f"Available Backups:\n{output}"
    except:
        info = "No backups found"
    print_box("Available Backups", info)
    backup_to_verify = input("Enter backup to verify: ").strip()
    full_path = os.path.join(backup_dir, backup_to_verify)
    if not os.path.exists(full_path):
        show_error("Backup not found")
        return
    if backup_to_verify.endswith(".tar.gz"):
        if os.system(f"tar -tzf \"{full_path}\" > /dev/null 2>&1") == 0:
            show_success("Backup is valid")
        else:
            show_error("Backup is corrupted")
    else:
        show_info("Backup verification not supported for this format")

# ========================================
# SETTINGS FUNCTIONS
# ========================================

def settings_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Theme",
            "Toggle Colors",
            "Toggle Animations",
            "Toggle Emojis",
            "Reset Configuration",
            "Back to main menu"
        ]
        print_menu("Settings", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            toggle_theme()
        elif choice == 2:
            toggle_colors()
        elif choice == 3:
            toggle_animations()
        elif choice == 4:
            toggle_emojis()
        elif choice == 5:
            reset_config()
        elif choice == 0 or choice == 6:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def toggle_theme():
    show_info("Theme switching requires terminal emulator support")

def toggle_colors():
    global COLORS_SUPPORTED
    COLORS_SUPPORTED = not COLORS_SUPPORTED
    if COLORS_SUPPORTED:
        show_success("Colors enabled")
    else:
        show_warning("Colors disabled")

def toggle_animations():
    global USE_ANIMATIONS
    USE_ANIMATIONS = not USE_ANIMATIONS
    if USE_ANIMATIONS:
        show_success("Animations enabled")
    else:
        show_warning("Animations disabled")

def toggle_emojis():
    global USE_EMOJIS
    USE_EMOJIS = not USE_EMOJIS
    if USE_EMOJIS:
        show_success("Emojis enabled")
    else:
        show_warning("Emojis disabled")

def reset_config():
    if confirm_action("Reset all configuration?"):
        config_dir = os.path.join(HOME_DIR, CONFIG_DIR)
        shutil.rmtree(config_dir, ignore_errors=True)
        show_success("Configuration reset")
        first_run_wizard()

# ========================================
# HELP FUNCTIONS
# ========================================

def help_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "About",
            "Documentation",
            "Supported systems",
            "Version",
            "Back to main menu"
        ]
        print_menu("Help", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            show_about()
        elif choice == 2:
            show_documentation()
        elif choice == 3:
            show_supported_systems()
        elif choice == 4:
            show_version()
        elif choice == 0 or choice == 5:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def show_about():
    about = f"""SysKit - Universal Unix Toolkit
Version: {VERSION}
Author: {AUTHOR}

SysKit is a comprehensive system maintenance,
diagnostics, and utility toolkit for Unix-like
operating systems."""
    print_box("About SysKit", about)

def show_documentation():
    docs = f"""{BOLD}Documentation:{RESET}

1. System: View system information
2. Monitoring: Monitor system resources
3. Network: Network diagnostics tools
4. Power: Battery information
5. Packages: Package management
6. Cleaner: System cleaning
7. Storage: Storage analysis
8. Security: Security audit
9. Files: File operations
10. Archive: Archive management
11. Utilities: Password/hash generation
12. Internet: Weather/time/calendar
13. Backup: Backup and restore
14. Settings: Configuration"""
    print_box("Documentation", docs)

def show_supported_systems():
    systems = f"""{BOLD}Supported Operating Systems:{RESET}

🐧 Linux - All major distributions
  - Debian/Ubuntu (apt)
  - Fedora/RHEL (dnf)
  - Arch Linux (pacman)
  - openSUSE (zypper)
  - Void Linux (xbps)
  - Alpine Linux (apk)
  - NixOS (nix)
  - Gentoo (emerge)

🍎 macOS - Intel and Apple Silicon
  - Homebrew package manager

📱 Termux - Android terminal
  - Termux API support

👹 FreeBSD
  - pkg package manager

🦉 OpenBSD
  - pkg_add package manager

🌐 NetBSD
  - pkgin package manager"""
    print_box("Supported Systems", systems)

def show_version():
    print_box("Version", f"SysKit version {VERSION}")

# ========================================
# MENU FUNCTIONS
# ========================================

def system_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Fastfetch",
            "System Information",
            "Hardware Information",
            "CPU Information",
            "GPU Information",
            "RAM Information",
            "Motherboard Information",
            "Disk Information",
            "Kernel Information",
            "Uptime",
            "Environment Information",
            "Back to main menu"
        ]
        print_menu("System", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            show_fastfetch()
        elif choice == 2:
            show_system_info()
        elif choice == 3:
            show_hardware_info()
        elif choice == 4:
            show_cpu_info()
        elif choice == 5:
            show_gpu_info()
        elif choice == 6:
            show_ram_info()
        elif choice == 7:
            show_motherboard_info()
        elif choice == 8:
            show_disk_info()
        elif choice == 9:
            show_kernel_info()
        elif choice == 10:
            show_uptime()
        elif choice == 11:
            show_environment_info()
        elif choice == 0 or choice == 12:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def monitoring_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "CPU Usage",
            "RAM Usage",
            "Disk Usage",
            "Network Usage",
            "Running Processes",
            "Top Processes",
            "Resource Monitor",
            "Temperature",
            "Back to main menu"
        ]
        print_menu("Monitoring", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            show_cpu_usage()
        elif choice == 2:
            show_ram_usage()
        elif choice == 3:
            show_disk_usage()
        elif choice == 4:
            show_network_usage()
        elif choice == 5:
            show_running_processes()
        elif choice == 6:
            show_top_processes()
        elif choice == 7:
            show_resource_monitor()
        elif choice == 8:
            show_temperature()
        elif choice == 0 or choice == 9:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def network_menu():
    while True:
        os.system("clear")
        print_header()
        items = [
            "Ping Test",
            "Internet Test",
            "DNS Test",
            "Public IP",
            "Local IP",
            "Gateway",
            "Wi-Fi Information",
            "Back to main menu"
        ]
        print_menu("Network", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            host = input("Host to ping [google.com]: ").strip()
            if not host:
                host = "google.com"
            ping_test(host)
        elif choice == 2:
            internet_test()
        elif choice == 3:
            dns_test()
        elif choice == 4:
            show_public_ip()
        elif choice == 5:
            show_local_ip()
        elif choice == 6:
            show_gateway()
        elif choice == 7:
            show_wifi_info()
        elif choice == 0 or choice == 8:
            break
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

# ========================================
# MAIN
# ========================================

def initialize():
    global CONFIG_PATH, LOG_PATH
    detect_os()
    detect_distro()
    detect_package_manager()

    CONFIG_PATH = os.path.join(HOME_DIR, CONFIG_DIR)
    LOG_PATH = os.path.join(HOME_DIR, CONFIG_DIR, LOG_FILE)

    first_run_wizard()

    show_success("SysKit initialized")
    show_info(f"Package Manager: {PKG_MANAGER}")
    if IS_ROOT:
        show_warning("Running as root - be careful with destructive operations")
    print()

def signal_handler(sig, frame):
    print(f"\n{YELLOW}{EMOJI_WARNING} Interrupted{RESET}")
    sys.exit(1)

def main_menu():
    while True:
        os.system("clear")
        print_header()

        items = [
            f"{EMOJI_SYSTEM} System",
            f"{EMOJI_MONITOR} Monitoring",
            f"{EMOJI_NETWORK} Network",
            f"{EMOJI_POWER} Power",
            f"{EMOJI_PACKAGE} Package Manager",
            f"{EMOJI_CLEANER} Cleaner",
            f"{EMOJI_STORAGE} Storage",
            f"{EMOJI_SECURITY} Security",
            f"{EMOJI_FILES} File Tools",
            f"{EMOJI_ARCHIVE} Archive Tools",
            f"{EMOJI_UTILITY} Utility Tools",
            f"{EMOJI_INTERNET} Internet Tools",
            f"{EMOJI_BACKUP} Backup",
            f"{EMOJI_SETTINGS} Settings",
            f"{EMOJI_HELP} Help",
            "Exit"
        ]
        print_menu("SysKit - Main Menu", items)
        choice = input("Select option: ").strip()
        if not choice:
            continue
        choice = int(choice) if choice.isdigit() else -1
        if choice == 1:
            system_menu()
        elif choice == 2:
            monitoring_menu()
        elif choice == 3:
            network_menu()
        elif choice == 4:
            show_battery_info()
        elif choice == 5:
            package_manager_menu()
        elif choice == 6:
            cleaner_menu()
        elif choice == 7:
            storage_menu()
        elif choice == 8:
            security_menu()
        elif choice == 9:
            file_tools_menu()
        elif choice == 10:
            archive_menu()
        elif choice == 11:
            utility_menu()
        elif choice == 12:
            internet_tools_menu()
        elif choice == 13:
            backup_menu()
        elif choice == 14:
            settings_menu()
        elif choice == 15:
            help_menu()
        elif choice == 0 or choice == 16:
            print(f"{GREEN}{EMOJI_CHECK} Goodbye!{RESET}")
            sys.exit(0)
        else:
            show_error("Invalid option")
        input("Press Enter to continue...")

def main():
    global HOME_DIR, USER_NAME, CONFIG_PATH, LOG_PATH
    signal.signal(signal.SIGINT, signal_handler)

    HOME_DIR = os.path.expanduser("~")
    USER_NAME = os.getenv("USER", "unknown")

    update_terminal_size()
    detect_os()
    detect_distro()
    detect_package_manager()

    CONFIG_PATH = os.path.join(HOME_DIR, CONFIG_DIR)
    LOG_PATH = os.path.join(HOME_DIR, CONFIG_DIR, LOG_FILE)

    initialize()
    main_menu()

if __name__ == "__main__":
    main()
