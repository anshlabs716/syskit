#!/usr/bin/env bash
# SysKit — Universal Unix Toolkit (Termux Optimized)
# Author: AnshLabs716
# Version: 2.0.1-termux

set -euo pipefail
IFS=$'\n\t'

# ========================================
# Global Configuration
# ========================================
VERSION="2.0.1-termux"
AUTHOR="AnshLabs716"
CONFIG_DIR="${HOME}/.config/syskit"
FIRST_RUN_FILE="${CONFIG_DIR}/first_run_complete"

# Force Termux mode
OS="Termux"
PKG_MANAGER="pkg"
PKG_UPDATE="pkg update"
PKG_UPGRADE="pkg upgrade -y"
PKG_INSTALL="pkg install -y"
PKG_REMOVE="pkg uninstall -y"
PKG_SEARCH="pkg search"
PKG_LIST="pkg list-installed"
PKG_CLEAN="pkg clean"
PKG_AUTOREMOVE="pkg autoclean"

# Terminal
TERMINAL_WIDTH=$(tput cols 2>/dev/null || echo 80)
TERMINAL_HEIGHT=$(tput lines 2>/dev/null || echo 24)
COLORS_SUPPORTED=true
USE_EMOJIS=true

# ========================================
# Colors
# ========================================
if [[ -t 1 ]] && [[ "$(tput colors 2>/dev/null || echo 0)" -ge 8 ]]; then
    RED=$'\033[0;31m'
    GREEN=$'\033[0;32m'
    YELLOW=$'\033[0;33m'
    BLUE=$'\033[0;34m'
    CYAN=$'\033[0;36m'
    WHITE=$'\033[0;37m'
    BOLD=$'\033[1m'
    DIM=$'\033[2m'
    RESET=$'\033[0m'
else
    RED=''; GREEN=''; YELLOW=''; BLUE=''; CYAN=''; WHITE=''; BOLD=''; DIM=''; RESET=''
fi

# ========================================
# Android Emojis
# ========================================
EMOJI_SYSTEM="📱"
EMOJI_MONITOR="📊"
EMOJI_NETWORK="🌐"
EMOJI_POWER="🔋"
EMOJI_PACKAGE="📦"
EMOJI_CLEANER="🧹"
EMOJI_STORAGE="💾"
EMOJI_SECURITY="🔐"
EMOJI_FILES="📂"
EMOJI_UTILITY="🔑"
EMOJI_INTERNET="🌤️"
EMOJI_BACKUP="💾"
EMOJI_SETTINGS="⚙️"
EMOJI_HELP="❓"
EMOJI_CHECK="✅"
EMOJI_WARNING="⚠️"
EMOJI_ERROR="❌"
EMOJI_INFO="ℹ️"
EMOJI_ROCKET="🚀"
EMOJI_FASTFETCH="⚡"
EMOJI_STORAGE="💿"
EMOJI_SECURITY="🛡️"
EMOJI_FILES="📁"
EMOJI_ARCHIVE="🗜️"

# ========================================
# Quick Setup
# ========================================
quick_setup() {
    clear
    echo -e "${BOLD}${CYAN}"
    echo "  _____     _    _  _____ _  __ "
    echo " / ____|   | |  | |/ ____| |/ / "
    echo "| (___   __| |  | | (___ | ' /  "
    echo " \___ \ / _\` |  | |\___ \|  <   "
    echo " ____) | (_| |__| |____) | . \  "
    echo "|_____/ \__,_\____/|_____/|_|\_\ "
    echo -e "${RESET}"
    echo -e "${BOLD}${WHITE}by AnshLabs716${RESET}"
    echo -e "${DIM}Termux Optimized v${VERSION}${RESET}"
    echo ""

    echo -e "${GREEN}${EMOJI_CHECK} Termux detected!${RESET}"
    echo -e "${BLUE}${EMOJI_INFO} Press any key to continue...${RESET}"
    read -n 1 -s
}

# ========================================
# Box Functions
# ========================================
print_box() {
    local title="$1"
    local content="$2"
    local max_width=$((TERMINAL_WIDTH - 4))
    [[ $max_width -gt 50 ]] && max_width=50

    echo "${BOLD}${BLUE}┌$(printf '%*s' "$max_width" | tr ' ' '─')┐${RESET}"

    if [[ -n "$title" ]]; then
        echo "${BOLD}${BLUE}│${RESET} ${BOLD}${WHITE}${title}${RESET} $(printf '%*s' $((max_width - ${#title} - 1)) '')${BOLD}${BLUE}│${RESET}"
        echo "${BOLD}${BLUE}├$(printf '%*s' "$max_width" | tr ' ' '─')┤${RESET}"
    fi

    while IFS= read -r line; do
        if [[ -n "$line" ]]; then
            printf "${BOLD}${BLUE}│${RESET} %-${max_width}s ${BOLD}${BLUE}│${RESET}\n" "$line"
        else
            printf "${BOLD}${BLUE}│${RESET} %-${max_width}s ${BOLD}${BLUE}│${RESET}\n" ""
        fi
    done <<< "$content"

    echo "${BOLD}${BLUE}└$(printf '%*s' "$max_width" | tr ' ' '─')┘${RESET}"
}

print_menu() {
    local title="$1"
    shift
    local items=("$@")
    local content=""
    local num=1
    for item in "${items[@]}"; do
        content+="${BOLD}${GREEN}${num}.${RESET} ${item}"$'\n'
        ((num++))
    done
    content+="${DIM}Type number. 0 to exit.${RESET}"$'\n'
    print_box "$title" "$content"
}

# ========================================
# Show Functions
# ========================================
show_success() { echo -e "${GREEN}${EMOJI_CHECK} $1${RESET}"; }
show_warning() { echo -e "${YELLOW}${EMOJI_WARNING} $1${RESET}"; }
show_error() { echo -e "${RED}${EMOJI_ERROR} $1${RESET}"; }
show_info() { echo -e "${BLUE}${EMOJI_INFO} $1${RESET}"; }

# ========================================
# Confirm Action
# ========================================
confirm_action() {
    local prompt="$1"
    echo -e "${YELLOW}${EMOJI_WARNING} $prompt${RESET}"
    read -p "Continue? [y/N] " response
    case "$response" in
        [yY]|[yY][eE][sS]) return 0 ;;
        *) return 1 ;;
    esac
}

# ========================================
# Fastfetch (Termux)
# ========================================
show_fastfetch() {
    if command -v fastfetch &>/dev/null; then
        fastfetch
    else
        show_warning "Fastfetch not installed"
        if confirm_action "Install fastfetch?"; then
            pkg install fastfetch -y
            if command -v fastfetch &>/dev/null; then
                fastfetch
            else
                show_error "Fastfetch installation failed"
            fi
        fi
    fi
    read -p "Press Enter to continue"
}

# ========================================
# System Info (Termux)
# ========================================
show_system_info() {
    local info=""
    info+="Hostname: $(hostname 2>/dev/null || echo "Unknown")\n"
    info+="OS: Termux\n"
    info+="Android: $(getprop ro.build.version.release 2>/dev/null || echo "Unknown")\n"
    info+="Kernel: $(uname -r)\n"
    info+="Arch: $(uname -m)\n"
    info+="Uptime: $(uptime -p 2>/dev/null || echo "Unknown")\n"
    info+="Storage: $(df -h /data 2>/dev/null | awk 'NR==2{print $2" used "$3" free")" || echo "Unknown")"
    info+="RAM: $(free -h 2>/dev/null | awk 'NR==2{print $2" total "$3" used"}')"
    print_box "System Info" "$info"
}

# ========================================
# Hardware Info (Termux)
# ========================================
show_hardware_info() {
    local info=""
    info+="Device: $(getprop ro.product.model 2>/dev/null || echo "Unknown")\n"
    info+="Manufacturer: $(getprop ro.product.manufacturer 2>/dev/null || echo "Unknown")\n"
    info+="Android: $(getprop ro.build.version.release 2>/dev/null || echo "Unknown")\n"
    info+="SDK: $(getprop ro.build.version.sdk 2>/dev/null || echo "Unknown")\n"
    info+="CPU: $(uname -m)\n"
    info+="Cores: $(nproc 2>/dev/null || echo "Unknown")"
    print_box "Hardware Info" "$info"
}

# ========================================
# CPU Info (Termux)
# ========================================
show_cpu_info() {
    local info=""
    info+="Architecture: $(uname -m)\n"
    info+="Cores: $(nproc 2>/dev/null || echo "Unknown")\n"
    info+="CPU Info: $(cat /proc/cpuinfo | grep "Hardware" | head -1 | cut -d: -f2 | xargs 2>/dev/null || echo "Unknown")\n"
    info+="Features: $(cat /proc/cpuinfo | grep "Features" | head -1 | cut -d: -f2 | xargs 2>/dev/null | cut -c1-50)..."
    print_box "CPU Info" "$info"
}

# ========================================
# Battery (Termux)
# ========================================
show_battery() {
    local info=""
    if command -v termux-battery-status &>/dev/null; then
        local battery_data=$(termux-battery-status 2>/dev/null)
        local percentage=$(echo "$battery_data" | grep "percentage" | awk '{print $2}' | tr -d ',')
        local status=$(echo "$battery_data" | grep "status" | awk '{print $2}' | tr -d '"')
        local health=$(echo "$battery_data" | grep "health" | awk '{print $2}' | tr -d '"')
        local temperature=$(echo "$battery_data" | grep "temperature" | awk '{print $2}' | tr -d ',')
        info+="Battery: ${percentage}%\n"
        info+="Status: ${status}\n"
        info+="Health: ${health}\n"
        info+="Temperature: ${temperature}°C"
    else
        info="Install termux-api for battery info"
        if confirm_action "Install termux-api?"; then
            pkg install termux-api -y
            if command -v termux-battery-status &>/dev/null; then
                show_battery
                return
            fi
        fi
    fi
    print_box "Battery" "$info"
}

# ========================================
# RAM Info (Termux)
# ========================================
show_ram_info() {
    local info=""
    if command -v free &>/dev/null; then
        local total=$(free -h | awk 'NR==2{print $2}')
        local used=$(free -h | awk 'NR==2{print $3}')
        local free=$(free -h | awk 'NR==2{print $4}')
        info+="Total: $total\n"
        info+="Used: $used\n"
        info+="Free: $free\n"
        info+="Swap: $(free -h | awk 'NR==3{print $2" total " $3" used"}')"
    else
        info+="free command not available"
    fi
    print_box "RAM Info" "$info"
}

# ========================================
# Network (Termux)
# ========================================
show_network_info() {
    local info=""
    info+="IP: $(ip addr show 2>/dev/null | grep "inet " | grep -v "127.0.0.1" | awk '{print $2}' | head -1 || echo "Unknown")\n"
    info+="Gateway: $(ip route 2>/dev/null | grep default | awk '{print $3}' || echo "Unknown")"
    print_box "Network Info" "$info"
}

show_wifi() {
    local info=""
    if command -v termux-wifi-connectioninfo &>/dev/null; then
        info="$(termux-wifi-connectioninfo 2>/dev/null)"
    else
        info="Install termux-api for Wi-Fi info"
        if confirm_action "Install termux-api?"; then
            pkg install termux-api -y
            if command -v termux-wifi-connectioninfo &>/dev/null; then
                show_wifi
                return
            fi
        fi
    fi
    print_box "Wi-Fi" "$info"
}

# ========================================
# Internet Test (Termux)
# ========================================
internet_test() {
    local info=""
    show_info "Testing internet connection..."
    if ping -c 1 -W 2 8.8.8.8 &>/dev/null; then
        info+="Internet: ✅ Connected\n"
        info+="Public IP: $(curl -s ifconfig.me 2>/dev/null || echo "N/A")"
    else
        info+="Internet: ❌ Disconnected"
    fi
    print_box "Internet Test" "$info"
}

# ========================================
# Package Manager (Termux)
# ========================================
package_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}${EMOJI_PACKAGE} Package Manager${RESET}\n"
        print_menu "Packages" \
            "Update" \
            "Upgrade" \
            "Clean cache" \
            "Autoremove" \
            "List installed" \
            "Search" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1) show_info "Updating..."; pkg update; show_success "Done"; read -p "Enter" ;;
            2) show_info "Upgrading..."; pkg upgrade -y; show_success "Done"; read -p "Enter" ;;
            3) show_info "Cleaning..."; pkg clean; show_success "Done"; read -p "Enter" ;;
            4) show_info "Autoremoving..."; pkg autoclean; show_success "Done"; read -p "Enter" ;;
            5) pkg list-installed; read -p "Enter" ;;
            6) read -p "Search: " term; pkg search "$term"; read -p "Enter" ;;
            0|7) break ;;
        esac
    done
}

# ========================================
# Cleaner (Termux)
# ========================================
cleaner_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}${EMOJI_CLEANER} Cleaner${RESET}\n"
        print_menu "Cleaner" \
            "User cache" \
            "Temp files" \
            "Package cache" \
            "Trash" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1)
                if confirm_action "Clean cache?"; then
                    rm -rf ~/.cache/* 2>/dev/null
                    show_success "Cache cleaned"
                fi
                read -p "Enter"
                ;;
            2)
                if confirm_action "Clean temp?"; then
                    rm -rf /tmp/* 2>/dev/null
                    show_success "Temp cleaned"
                fi
                read -p "Enter"
                ;;
            3)
                if confirm_action "Clean package cache?"; then
                    pkg clean
                    show_success "Package cache cleaned"
                fi
                read -p "Enter"
                ;;
            4)
                if [[ -d ~/.local/share/Trash ]]; then
                    if confirm_action "Empty trash?"; then
                        rm -rf ~/.local/share/Trash/* 2>/dev/null
                        show_success "Trash emptied"
                    fi
                else
                    show_warning "No trash found"
                fi
                read -p "Enter"
                ;;
            0|5) break ;;
        esac
    done
}

# ========================================
# Storage (Termux)
# ========================================
storage_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}${EMOJI_STORAGE} Storage${RESET}\n"
        print_menu "Storage" \
            "Disk usage" \
            "Largest directories" \
            "Largest files" \
            "Mounted drives" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1)
                clear
                echo -e "${BOLD}${CYAN}Disk Usage${RESET}\n"
                df -h
                read -p "Enter"
                ;;
            2)
                clear
                echo -e "${BOLD}${CYAN}Largest Directories${RESET}\n"
                du -sh ~/* 2>/dev/null | sort -hr | head -10
                read -p "Enter"
                ;;
            3)
                clear
                echo -e "${BOLD}${CYAN}Largest Files${RESET}\n"
                find ~ -type f -exec du -h {} + 2>/dev/null | sort -hr | head -10
                read -p "Enter"
                ;;
            4)
                clear
                echo -e "${BOLD}${CYAN}Mounted Drives${RESET}\n"
                mount | grep "/data" || mount | head -10
                read -p "Enter"
                ;;
            0|5) break ;;
        esac
    done
}

# ========================================
# Utilities (Termux)
# ========================================
utility_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}${EMOJI_UTILITY} Utilities${RESET}\n"
        print_menu "Utilities" \
            "Password Generator" \
            "UUID Generator" \
            "Hash (MD5/SHA)" \
            "Random String" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1)
                read -p "Length [16]: " len
                len="${len:-16}"
                pw=$(tr -dc 'A-Za-z0-9!@#$%^&*' < /dev/urandom | head -c "$len")
                print_box "Password" "$pw"
                read -p "Enter"
                ;;
            2)
                if command -v uuidgen &>/dev/null; then
                    uuid=$(uuidgen)
                else
                    uuid=$(cat /proc/sys/kernel/random/uuid 2>/dev/null || echo "N/A")
                fi
                print_box "UUID" "$uuid"
                read -p "Enter"
                ;;
            3)
                read -p "Text to hash: " text
                echo "MD5: $(echo -n "$text" | md5sum | awk '{print $1}')"
                echo "SHA1: $(echo -n "$text" | sha1sum | awk '{print $1}')"
                echo "SHA256: $(echo -n "$text" | sha256sum | awk '{print $1}')"
                read -p "Enter"
                ;;
            4)
                read -p "Length [16]: " len
                len="${len:-16}"
                rand=$(tr -dc 'A-Za-z0-9' < /dev/urandom | head -c "$len")
                print_box "Random String" "$rand"
                read -p "Enter"
                ;;
            0|5) break ;;
        esac
    done
}

# ========================================
# Internet (Termux)
# ========================================
internet_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}${EMOJI_INTERNET} Internet${RESET}\n"
        print_menu "Internet" \
            "Weather" \
            "Time" \
            "Calendar" \
            "Internet Test" \
            "Public IP" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1)
                read -p "City [London]: " city
                city="${city:-London}"
                weather=$(curl -s "wttr.in/$city?format=%c+%t+%w" 2>/dev/null)
                print_box "Weather" "Weather for $city:\n$weather"
                read -p "Enter"
                ;;
            2)
                print_box "Time" "Date: $(date '+%A, %B %d, %Y')\nTime: $(date '+%H:%M:%S')\nTimezone: $(date '+%Z')"
                read -p "Enter"
                ;;
            3)
                cal=$(cal -3 2>/dev/null || echo "Calendar not available")
                print_box "Calendar" "$cal"
                read -p "Enter"
                ;;
            4)
                internet_test
                read -p "Enter"
                ;;
            5)
                ip=$(curl -s ifconfig.me 2>/dev/null || echo "N/A")
                print_box "Public IP" "$ip"
                read -p "Enter"
                ;;
            0|6) break ;;
        esac
    done
}

# ========================================
# Security (Termux)
# ========================================
security_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}${EMOJI_SECURITY} Security${RESET}\n"
        print_menu "Security" \
            "Open ports" \
            "Running processes" \
            "Security tips" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1)
                clear
                echo -e "${BOLD}${CYAN}Open Ports${RESET}\n"
                netstat -tuln 2>/dev/null | grep LISTEN | head -10 || echo "No open ports found"
                read -p "Enter"
                ;;
            2)
                clear
                echo -e "${BOLD}${CYAN}Running Processes${RESET}\n"
                ps aux | head -15
                read -p "Enter"
                ;;
            3)
                print_box "Security Tips" "1. Keep Termux updated\n2. Use strong passwords\n3. Don't run as root\n4. Check permissions\n5. Use VPN\n6. Install termux-api for security features"
                read -p "Enter"
                ;;
            0|4) break ;;
        esac
    done
}

# ========================================
# Backup (Termux)
# ========================================
backup_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}${EMOJI_BACKUP} Backup${RESET}\n"
        print_menu "Backup" \
            "Backup folder" \
            "Restore backup" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1)
                read -p "Folder to backup: " folder
                if [[ -d "$folder" ]]; then
                    backup_name="backup_$(date +%Y%m%d).tar.gz"
                    show_info "Creating backup..."
                    tar -czf "$HOME/$backup_name" "$folder" 2>/dev/null
                    show_success "Backup saved: $HOME/$backup_name"
                else
                    show_error "Folder not found"
                fi
                read -p "Enter"
                ;;
            2)
                ls -la ~/backup_*.tar.gz 2>/dev/null || echo "No backups found"
                read -p "Enter backup name to restore: " backup_file
                if [[ -f "$HOME/$backup_file" ]]; then
                    read -p "Restore to: " restore_dir
                    mkdir -p "$restore_dir"
                    tar -xzf "$HOME/$backup_file" -C "$restore_dir" 2>/dev/null
                    show_success "Restored to $restore_dir"
                else
                    show_error "Backup not found"
                fi
                read -p "Enter"
                ;;
            0|3) break ;;
        esac
    done
}

# ========================================
# Help
# ========================================
help_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}${EMOJI_HELP} Help${RESET}\n"
        print_menu "Help" \
            "About" \
            "Supported" \
            "Version" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1)
                print_box "About" "SysKit - Termux Edition\nBy AnshLabs716\nUnix Toolkit for Termux\n\nFeatures:\n- System Info\n- Battery\n- Wi-Fi\n- Package Manager\n- Cleaner\n- Utilities\n- Internet Tools"
                read -p "Enter"
                ;;
            2)
                print_box "Supported" "Termux\nAndroid 5.0+\npkg package manager\nARM/ARM64/x86_64"
                read -p "Enter"
                ;;
            3)
                print_box "Version" "SysKit v$VERSION"
                read -p "Enter"
                ;;
            0|4) break ;;
        esac
    done
}

# ========================================
# Main Menu
# ========================================
main_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}"
        echo "  _____     _    _  _____ _  __ "
        echo " / ____|   | |  | |/ ____| |/ / "
        echo "| (___   __| |  | | (___ | ' /  "
        echo " \___ \ / _\` |  | |\___ \|  <   "
        echo " ____) | (_| |__| |____) | . \  "
        echo "|_____/ \__,_\____/|_____/|_|\_\ "
        echo -e "${RESET}"
        echo -e "${BOLD}${WHITE}by AnshLabs716${RESET}"
        echo -e "${DIM}Termux v${VERSION}${RESET}"
        echo ""

        print_menu "SysKit - Termux" \
            "System Info" \
            "Hardware Info" \
            "CPU Info" \
            "RAM Info" \
            "Battery" \
            "Network Info" \
            "Wi-Fi" \
            "Fastfetch" \
            "Package Manager" \
            "Cleaner" \
            "Storage" \
            "Utilities" \
            "Internet" \
            "Security" \
            "Backup" \
            "Help" \
            "Exit"

        echo ""
        read -p "Select option: " choice

        case "$choice" in
            1) show_system_info; read -p "Press Enter" ;;
            2) show_hardware_info; read -p "Press Enter" ;;
            3) show_cpu_info; read -p "Press Enter" ;;
            4) show_ram_info; read -p "Press Enter" ;;
            5) show_battery; read -p "Press Enter" ;;
            6) show_network_info; read -p "Press Enter" ;;
            7) show_wifi; read -p "Press Enter" ;;
            8) show_fastfetch ;;
            9) package_menu ;;
            10) cleaner_menu ;;
            11) storage_menu ;;
            12) utility_menu ;;
            13) internet_menu ;;
            14) security_menu ;;
            15) backup_menu ;;
            16) help_menu ;;
            0|17) echo -e "${GREEN}${EMOJI_CHECK} Goodbye!${RESET}"; exit 0 ;;
            *) show_error "Invalid"; sleep 1 ;;
        esac
    done
}

# ========================================
# Start
# ========================================
quick_setup
main_menu
