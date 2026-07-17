#!/usr/bin/env bash
# SysKit — Universal Unix Toolkit (Termux Optimized)
# Author: AnshLabs716
# Version: 2.0.0-termux

set -euo pipefail
IFS=$'\n\t'

# ========================================
# Global Configuration
# ========================================
VERSION="2.0.0-termux"
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
# Emojis
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

    # Skip first run wizard - just go straight to menu
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
# System Info (Termux)
# ========================================
show_system_info() {
    local info=""
    info+="Hostname: $(hostname 2>/dev/null || echo "Unknown")\n"
    info+="OS: Termux\n"
    info+="Kernel: $(uname -r)\n"
    info+="Arch: $(uname -m)\n"
    info+="Uptime: $(uptime -p 2>/dev/null || echo "Unknown")"
    print_box "System Info" "$info"
}

# ========================================
# Battery (Termux)
# ========================================
show_battery() {
    local info=""
    if command -v termux-battery-status &>/dev/null; then
        info="$(termux-battery-status 2>/dev/null)"
    else
        info="Install termux-api for battery info"
        if confirm_action "Install termux-api?"; then
            pkg install termux-api -y
        fi
    fi
    print_box "Battery" "$info"
}

# ========================================
# Network (Termux)
# ========================================
show_wifi() {
    local info=""
    if command -v termux-wifi-connectioninfo &>/dev/null; then
        info="$(termux-wifi-connectioninfo 2>/dev/null)"
    else
        info="Install termux-api for Wi-Fi info"
        if confirm_action "Install termux-api?"; then
            pkg install termux-api -y
        fi
    fi
    print_box "Wi-Fi" "$info"
}

# ========================================
# Package Manager (Termux)
# ========================================
package_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}📦 Package Manager${RESET}\n"
        print_menu "Packages" \
            "Update" \
            "Upgrade" \
            "Clean" \
            "List installed" \
            "Search" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1) show_info "Updating..."; pkg update; show_success "Done"; read -p "Enter" ;;
            2) show_info "Upgrading..."; pkg upgrade -y; show_success "Done"; read -p "Enter" ;;
            3) show_info "Cleaning..."; pkg clean; show_success "Done"; read -p "Enter" ;;
            4) pkg list-installed; read -p "Enter" ;;
            5) read -p "Search: " term; pkg search "$term"; read -p "Enter" ;;
            0|6) break ;;
        esac
    done
}

# ========================================
# Cleaner (Termux)
# ========================================
cleaner_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}🧹 Cleaner${RESET}\n"
        print_menu "Cleaner" \
            "Cache" \
            "Temp" \
            "Package cache" \
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
            0|4) break ;;
        esac
    done
}

# ========================================
# Utilities (Termux)
# ========================================
utility_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}🔑 Utilities${RESET}\n"
        print_menu "Utilities" \
            "Password Generator" \
            "UUID Generator" \
            "Hash (MD5/SHA)" \
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
            0|4) break ;;
        esac
    done
}

# ========================================
# Internet (Termux)
# ========================================
internet_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}🌤️ Internet${RESET}\n"
        print_menu "Internet" \
            "Weather" \
            "Time" \
            "Calendar" \
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
                print_box "Time" "Date: $(date '+%A, %B %d, %Y')\nTime: $(date '+%H:%M:%S')"
                read -p "Enter"
                ;;
            3)
                cal=$(cal -3 2>/dev/null || echo "Calendar not available")
                print_box "Calendar" "$cal"
                read -p "Enter"
                ;;
            0|4) break ;;
        esac
    done
}

# ========================================
# Confirm
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
# Help
# ========================================
help_menu() {
    while true; do
        clear
        echo -e "${BOLD}${CYAN}❓ Help${RESET}\n"
        print_menu "Help" \
            "About" \
            "Supported" \
            "Version" \
            "Back"

        read -p "Select: " choice
        case "$choice" in
            1)
                print_box "About" "SysKit - Termux Edition\nBy AnshLabs716\nUnix Toolkit for Termux"
                read -p "Enter"
                ;;
            2)
                print_box "Supported" "Termux\nAndroid 5.0+\npkg package manager"
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
            "Battery" \
            "Wi-Fi" \
            "Package Manager" \
            "Cleaner" \
            "Utilities" \
            "Internet" \
            "Help" \
            "Exit"

        echo ""
        read -p "Select option: " choice

        case "$choice" in
            1) show_system_info; read -p "Press Enter" ;;
            2) show_battery; read -p "Press Enter" ;;
            3) show_wifi; read -p "Press Enter" ;;
            4) package_menu ;;
            5) cleaner_menu ;;
            6) utility_menu ;;
            7) internet_menu ;;
            8) help_menu ;;
            0|9) echo -e "${GREEN}${EMOJI_CHECK} Goodbye!${RESET}"; exit 0 ;;
            *) show_error "Invalid"; sleep 1 ;;
        esac
    done
}

# ========================================
# Start
# ========================================
quick_setup
main_menu
