#!/usr/bin/env bash
# SysKit — Universal Unix Toolkit
# Author: AnshLabs716
# Version: 2.0.0
# Description: All-in-one maintenance, diagnostics, and utility toolkit for Unix-like systems

set -euo pipefail
IFS=$'\n\t'

# ========================================
# Global Configuration
# ========================================
VERSION="2.0.0"
AUTHOR="AnshLabs716"
SCRIPT_NAME="syskit.sh"
CONFIG_DIR="${HOME}/.config/syskit"
CONFIG_FILE="${CONFIG_DIR}/config.conf"
FIRST_RUN_FILE="${CONFIG_DIR}/first_run_complete"
LOG_FILE="${CONFIG_DIR}/syskit.log"

# Root detection
IS_ROOT=false
if [[ $EUID -eq 0 ]]; then
    IS_ROOT=true
fi

# Color Support
COLORS_SUPPORTED=true
USE_EMOJIS=true
USE_ANIMATIONS=true

# Fullscreen detection
IS_FULLSCREEN=false
if [[ -n "${TERM_PROGRAM:-}" ]] || [[ -n "${TMUX:-}" ]] || [[ -n "${KONSOLE_VERSION:-}" ]] || [[ -n "${GNOME_TERMINAL_SCREEN:-}" ]]; then
    IS_FULLSCREEN=true
fi

# Terminal Dimensions
update_terminal_size() {
    TERMINAL_WIDTH=$(tput cols 2>/dev/null || echo 80)
    TERMINAL_HEIGHT=$(tput lines 2>/dev/null || echo 24)
}
update_terminal_size

# ========================================
# Color Definitions
# ========================================
if [[ -t 1 ]] && [[ "$(tput colors 2>/dev/null || echo 0)" -ge 8 ]]; then
    readonly RED=$'\033[0;31m'
    readonly GREEN=$'\033[0;32m'
    readonly YELLOW=$'\033[0;33m'
    readonly BLUE=$'\033[0;34m'
    readonly MAGENTA=$'\033[0;35m'
    readonly CYAN=$'\033[0;36m'
    readonly WHITE=$'\033[0;37m'
    readonly BOLD=$'\033[1m'
    readonly DIM=$'\033[2m'
    readonly RESET=$'\033[0m'
    readonly BG_RED=$'\033[41m'
    readonly BG_GREEN=$'\033[42m'
    readonly BG_YELLOW=$'\033[43m'
    readonly BG_BLUE=$'\033[44m'
    readonly BG_CYAN=$'\033[46m'
    readonly BG_WHITE=$'\033[47m'
else
    readonly RED=''; readonly GREEN=''; readonly YELLOW=''
    readonly BLUE=''; readonly MAGENTA=''; readonly CYAN=''
    readonly WHITE=''; readonly BOLD=''; readonly DIM=''; readonly RESET=''
    readonly BG_RED=''; readonly BG_GREEN=''; readonly BG_YELLOW=''
    readonly BG_BLUE=''; readonly BG_CYAN=''; readonly BG_WHITE=''
    COLORS_SUPPORTED=false
fi

# Root indicator
if [[ "$IS_ROOT" == true ]]; then
    ROOT_INDICATOR="${BG_RED}${WHITE} ROOT ${RESET}"
else
    ROOT_INDICATOR="${DIM}User${RESET}"
fi

# ========================================
# Emoji Definitions
# ========================================
if [[ "$USE_EMOJIS" == true ]]; then
    readonly EMOJI_SYSTEM="🖥️"
    readonly EMOJI_MONITOR="📊"
    readonly EMOJI_NETWORK="🌐"
    readonly EMOJI_POWER="🔋"
    readonly EMOJI_PACKAGE="📦"
    readonly EMOJI_CLEANER="🧹"
    readonly EMOJI_STORAGE="💾"
    readonly EMOJI_SECURITY="🔐"
    readonly EMOJI_FILES="📂"
    readonly EMOJI_ARCHIVE="🗜️"
    readonly EMOJI_UTILITY="🔑"
    readonly EMOJI_INTERNET="🌤️"
    readonly EMOJI_BACKUP="💾"
    readonly EMOJI_SETTINGS="⚙️"
    readonly EMOJI_HELP="❓"
    readonly EMOJI_CHECK="✅"
    readonly EMOJI_WARNING="⚠️"
    readonly EMOJI_ERROR="❌"
    readonly EMOJI_INFO="ℹ️"
    readonly EMOJI_STAR="⭐"
    readonly EMOJI_ROCKET="🚀"
    readonly EMOJI_GEAR="⚙️"
    readonly EMOJI_SHIELD="🛡️"
    readonly EMOJI_LOCK="🔒"
    readonly EMOJI_HOURGLASS="⏳"
else
    readonly EMOJI_SYSTEM="[System]"
    readonly EMOJI_MONITOR="[Monitor]"
    readonly EMOJI_NETWORK="[Network]"
    readonly EMOJI_POWER="[Power]"
    readonly EMOJI_PACKAGE="[Package]"
    readonly EMOJI_CLEANER="[Cleaner]"
    readonly EMOJI_STORAGE="[Storage]"
    readonly EMOJI_SECURITY="[Security]"
    readonly EMOJI_FILES="[Files]"
    readonly EMOJI_ARCHIVE="[Archive]"
    readonly EMOJI_UTILITY="[Utility]"
    readonly EMOJI_INTERNET="[Internet]"
    readonly EMOJI_BACKUP="[Backup]"
    readonly EMOJI_SETTINGS="[Settings]"
    readonly EMOJI_HELP="[Help]"
    readonly EMOJI_CHECK="[OK]"
    readonly EMOJI_WARNING="[WARN]"
    readonly EMOJI_ERROR="[ERR]"
    readonly EMOJI_INFO="[INFO]"
    readonly EMOJI_STAR="[*]"
    readonly EMOJI_ROCKET="[>>]"
    readonly EMOJI_GEAR="[#]"
    readonly EMOJI_SHIELD="[SHIELD]"
    readonly EMOJI_LOCK="[LOCK]"
    readonly EMOJI_HOURGLASS="[WAIT]"
fi

# ========================================
# OS Detection
# ========================================
detect_os() {
    if [[ "$(uname)" == "Linux" ]]; then
        if [[ -d "/data/data/com.termux" ]]; then
            OS="Termux"
        else
            OS="Linux"
        fi
    elif [[ "$(uname)" == "Darwin" ]]; then
        OS="macOS"
    elif [[ "$(uname)" == "FreeBSD" ]]; then
        OS="FreeBSD"
    elif [[ "$(uname)" == "OpenBSD" ]]; then
        OS="OpenBSD"
    elif [[ "$(uname)" == "NetBSD" ]]; then
        OS="NetBSD"
    else
        OS="Unknown"
    fi
}

detect_distro() {
    if [[ "$OS" == "Linux" ]]; then
        if [[ -f /etc/os-release ]]; then
            if source /etc/os-release 2>/dev/null; then
                DISTRO="${ID:-unknown}"
                DISTRO_VERSION="${VERSION_ID:-unknown}"
            else
                DISTRO="unknown"
                DISTRO_VERSION="unknown"
            fi
        elif [[ -f /etc/lsb-release ]]; then
            if source /etc/lsb-release 2>/dev/null; then
                DISTRO="${DISTRIB_ID:-unknown}"
                DISTRO_VERSION="${DISTRIB_RELEASE:-unknown}"
            else
                DISTRO="unknown"
                DISTRO_VERSION="unknown"
            fi
        else
            DISTRO="Unknown"
            DISTRO_VERSION="Unknown"
        fi
    elif [[ "$OS" == "macOS" ]]; then
        DISTRO="macOS"
        DISTRO_VERSION="$(sw_vers -productVersion 2>/dev/null || echo "unknown")"
    else
        DISTRO="$OS"
        DISTRO_VERSION="unknown"
    fi
}

detect_package_manager() {
    case "$OS" in
        Linux)
            if [[ "$DISTRO" == "ubuntu" ]] || [[ "$DISTRO" == "debian" ]] || [[ "$DISTRO" == "linuxmint" ]] || [[ "$DISTRO" == "pop" ]]; then
                PKG_MANAGER="apt"
                PKG_UPDATE="apt update"
                PKG_UPGRADE="apt upgrade -y"
                PKG_INSTALL="apt install -y"
                PKG_REMOVE="apt remove -y"
                PKG_SEARCH="apt search"
                PKG_LIST="apt list --installed"
                PKG_CLEAN="apt autoclean -y"
                PKG_AUTOREMOVE="apt autoremove -y"
                PKG_INSTALL_CMD="apt install -y"
            elif [[ "$DISTRO" == "fedora" ]] || [[ "$DISTRO" == "rhel" ]] || [[ "$DISTRO" == "centos" ]] || [[ "$DISTRO" == "rocky" ]] || [[ "$DISTRO" == "almalinux" ]]; then
                if command -v dnf &>/dev/null; then
                    PKG_MANAGER="dnf"
                    PKG_UPDATE="dnf check-update"
                    PKG_UPGRADE="dnf upgrade -y"
                    PKG_INSTALL="dnf install -y"
                    PKG_REMOVE="dnf remove -y"
                    PKG_SEARCH="dnf search"
                    PKG_LIST="dnf list installed"
                    PKG_CLEAN="dnf clean all"
                    PKG_AUTOREMOVE="dnf autoremove -y"
                    PKG_INSTALL_CMD="dnf install -y"
                else
                    PKG_MANAGER="yum"
                    PKG_UPDATE="yum check-update"
                    PKG_UPGRADE="yum upgrade -y"
                    PKG_INSTALL="yum install -y"
                    PKG_REMOVE="yum remove -y"
                    PKG_SEARCH="yum search"
                    PKG_LIST="yum list installed"
                    PKG_CLEAN="yum clean all"
                    PKG_AUTOREMOVE="yum autoremove -y"
                    PKG_INSTALL_CMD="yum install -y"
                fi
            elif [[ "$DISTRO" == "arch" ]] || [[ "$DISTRO" == "manjaro" ]] || [[ "$DISTRO" == "endeavouros" ]] || [[ "$DISTRO" == "artix" ]]; then
                PKG_MANAGER="pacman"
                PKG_UPDATE="pacman -Sy"
                PKG_UPGRADE="pacman -Su --noconfirm"
                PKG_INSTALL="pacman -S --noconfirm"
                PKG_REMOVE="pacman -R --noconfirm"
                PKG_SEARCH="pacman -Ss"
                PKG_LIST="pacman -Q"
                PKG_CLEAN="pacman -Sc --noconfirm"
                PKG_AUTOREMOVE="pacman -Rns --noconfirm $(pacman -Qdtq 2>/dev/null || echo '')"
                PKG_INSTALL_CMD="pacman -S --noconfirm"
            elif [[ "$DISTRO" == "opensuse" ]] || [[ "$DISTRO" == "suse" ]] || [[ "$DISTRO" == "sles" ]]; then
                PKG_MANAGER="zypper"
                PKG_UPDATE="zypper refresh"
                PKG_UPGRADE="zypper update -y"
                PKG_INSTALL="zypper install -y"
                PKG_REMOVE="zypper remove -y"
                PKG_SEARCH="zypper search"
                PKG_LIST="zypper se --installed-only"
                PKG_CLEAN="zypper clean"
                PKG_AUTOREMOVE="zypper rm -u"
                PKG_INSTALL_CMD="zypper install -y"
            elif [[ "$DISTRO" == "void" ]]; then
                PKG_MANAGER="xbps"
                PKG_UPDATE="xbps-install -S"
                PKG_UPGRADE="xbps-install -Su"
                PKG_INSTALL="xbps-install -y"
                PKG_REMOVE="xbps-remove -y"
                PKG_SEARCH="xbps-query -Rs"
                PKG_LIST="xbps-query -l"
                PKG_CLEAN="xbps-remove -O"
                PKG_AUTOREMOVE="xbps-remove -y $(xbps-query -m 2>/dev/null || echo '')"
                PKG_INSTALL_CMD="xbps-install -y"
            elif [[ "$DISTRO" == "alpine" ]]; then
                PKG_MANAGER="apk"
                PKG_UPDATE="apk update"
                PKG_UPGRADE="apk upgrade"
                PKG_INSTALL="apk add"
                PKG_REMOVE="apk del"
                PKG_SEARCH="apk search"
                PKG_LIST="apk list --installed"
                PKG_CLEAN="apk cache clean"
                PKG_AUTOREMOVE="apk del --purge"
                PKG_INSTALL_CMD="apk add"
            elif [[ "$DISTRO" == "nixos" ]]; then
                PKG_MANAGER="nix"
                PKG_UPDATE="nix-channel --update"
                PKG_UPGRADE="nix-env -u"
                PKG_INSTALL="nix-env -i"
                PKG_REMOVE="nix-env -e"
                PKG_SEARCH="nix-env -qa"
                PKG_LIST="nix-env -q"
                PKG_CLEAN="nix-collect-garbage"
                PKG_AUTOREMOVE="nix-collect-garbage -d"
                PKG_INSTALL_CMD="nix-env -i"
            elif [[ "$DISTRO" == "gentoo" ]]; then
                PKG_MANAGER="emerge"
                PKG_UPDATE="emerge --sync"
                PKG_UPGRADE="emerge -uD @world"
                PKG_INSTALL="emerge -a"
                PKG_REMOVE="emerge -C"
                PKG_SEARCH="emerge -s"
                PKG_LIST="equery list"
                PKG_CLEAN="eclean-dist"
                PKG_AUTOREMOVE="emerge --depclean"
                PKG_INSTALL_CMD="emerge -a"
            else
                PKG_MANAGER="unknown"
                PKG_UPDATE="echo 'Unknown package manager'"
                PKG_UPGRADE="echo 'Unknown package manager'"
                PKG_INSTALL="echo 'Unknown package manager'"
                PKG_REMOVE="echo 'Unknown package manager'"
                PKG_SEARCH="echo 'Unknown package manager'"
                PKG_LIST="echo 'Unknown package manager'"
                PKG_CLEAN="echo 'Unknown package manager'"
                PKG_AUTOREMOVE="echo 'Unknown package manager'"
                PKG_INSTALL_CMD="echo 'Unknown package manager'"
            fi
            ;;
        macOS)
            if command -v brew &>/dev/null; then
                PKG_MANAGER="brew"
                PKG_UPDATE="brew update"
                PKG_UPGRADE="brew upgrade"
                PKG_INSTALL="brew install"
                PKG_REMOVE="brew uninstall"
                PKG_SEARCH="brew search"
                PKG_LIST="brew list"
                PKG_CLEAN="brew cleanup"
                PKG_AUTOREMOVE="brew autoremove"
                PKG_INSTALL_CMD="brew install"
            else
                PKG_MANAGER="unknown"
                PKG_UPDATE="echo 'Homebrew not installed'"
                PKG_UPGRADE="echo 'Homebrew not installed'"
                PKG_INSTALL="echo 'Homebrew not installed'"
                PKG_REMOVE="echo 'Homebrew not installed'"
                PKG_SEARCH="echo 'Homebrew not installed'"
                PKG_LIST="echo 'Homebrew not installed'"
                PKG_CLEAN="echo 'Homebrew not installed'"
                PKG_AUTOREMOVE="echo 'Homebrew not installed'"
                PKG_INSTALL_CMD="echo 'Homebrew not installed'"
            fi
            ;;
        Termux)
            PKG_MANAGER="pkg"
            PKG_UPDATE="pkg update"
            PKG_UPGRADE="pkg upgrade -y"
            PKG_INSTALL="pkg install -y"
            PKG_REMOVE="pkg uninstall -y"
            PKG_SEARCH="pkg search"
            PKG_LIST="pkg list-installed"
            PKG_CLEAN="pkg clean"
            PKG_AUTOREMOVE="pkg autoclean"
            PKG_INSTALL_CMD="pkg install -y"
            ;;
        FreeBSD)
            PKG_MANAGER="pkg"
            PKG_UPDATE="pkg update"
            PKG_UPGRADE="pkg upgrade -y"
            PKG_INSTALL="pkg install -y"
            PKG_REMOVE="pkg delete -y"
            PKG_SEARCH="pkg search"
            PKG_LIST="pkg info"
            PKG_CLEAN="pkg clean"
            PKG_AUTOREMOVE="pkg autoremove -y"
            PKG_INSTALL_CMD="pkg install -y"
            ;;
        OpenBSD)
            PKG_MANAGER="pkg_add"
            PKG_UPDATE="pkg_add -U"
            PKG_UPGRADE="pkg_add -u"
            PKG_INSTALL="pkg_add -v"
            PKG_REMOVE="pkg_delete -v"
            PKG_SEARCH="pkg_info -Q"
            PKG_LIST="pkg_info"
            PKG_CLEAN="pkg_add -u"
            PKG_AUTOREMOVE="pkg_delete -a"
            PKG_INSTALL_CMD="pkg_add -v"
            ;;
        NetBSD)
            PKG_MANAGER="pkgin"
            PKG_UPDATE="pkgin update"
            PKG_UPGRADE="pkgin upgrade -y"
            PKG_INSTALL="pkgin install -y"
            PKG_REMOVE="pkgin remove -y"
            PKG_SEARCH="pkgin search"
            PKG_LIST="pkgin list"
            PKG_CLEAN="pkgin clean"
            PKG_AUTOREMOVE="pkgin autoremove -y"
            PKG_INSTALL_CMD="pkgin install -y"
            ;;
        *)
            PKG_MANAGER="unknown"
            PKG_UPDATE="echo 'Unknown OS'"
            PKG_UPGRADE="echo 'Unknown OS'"
            PKG_INSTALL="echo 'Unknown OS'"
            PKG_REMOVE="echo 'Unknown OS'"
            PKG_SEARCH="echo 'Unknown OS'"
            PKG_LIST="echo 'Unknown OS'"
            PKG_CLEAN="echo 'Unknown OS'"
            PKG_AUTOREMOVE="echo 'Unknown OS'"
            PKG_INSTALL_CMD="echo 'Unknown OS'"
            ;;
    esac
}

# ========================================
# Utility Functions
# ========================================
print_header() {
    clear
    update_terminal_size
    echo -e "${BOLD}${CYAN}"
    cat << "EOF"
  _____     _    _  _____ _  __
 / ____|   | |  | |/ ____| |/ /
| (___   __| |  | | (___ | ' /
 \___ \ / _` |  | |\___ \|  <
 ____) | (_| |__| |____) | . \
|_____/ \__,_\____/|_____/|_|\_\
EOF
    echo -e "${RESET}"
    echo -e "${BOLD}${WHITE}by AnshLabs716${RESET} ${ROOT_INDICATOR}"
    echo -e "${DIM}Version: ${VERSION} | ${OS} ${DISTRO}${RESET}"
    if [[ "$IS_FULLSCREEN" == true ]]; then
        echo -e "${DIM}└─ Fullscreen mode detected ${RESET}"
    fi
    echo ""
}

strip_ansi() {
    echo -e "$1" | sed -E 's/\x1b\[[0-9;]*m//g' 2>/dev/null || echo "$1"
}

print_box() {
    local title="$1"
    local content="$2"
    update_terminal_size
    local max_width=$((TERMINAL_WIDTH - 4))
    [[ $max_width -gt 80 ]] && max_width=80
    [[ $max_width -lt 40 ]] && max_width=40

    # Process content: wrap lines properly
    local processed_content=""
    while IFS= read -r line; do
        # If line is empty, add blank line
        if [[ -z "$line" ]]; then
            processed_content+=$'\n'
            continue
        fi

        # Strip ANSI for length calculation
        local visible=$(strip_ansi "$line")
        local visible_len=${#visible}

        # If line is shorter than max_width, keep as is
        if [[ $visible_len -le $max_width ]]; then
            processed_content+="$line"$'\n'
        else
            # Need to wrap - use fold but preserve ANSI codes
            # We'll wrap manually to preserve colors
            local chars=()
            local ansi_chars=()
            local i=0
            local in_escape=false
            local esc_sequence=""

            # Split into characters with ANSI tracking
            while [[ $i -lt ${#line} ]]; do
                char="${line:$i:1}"
                if [[ "$char" == $'\033' ]]; then
                    in_escape=true
                    esc_sequence=""
                fi

                if [[ "$in_escape" == true ]]; then
                    esc_sequence+="$char"
                    if [[ "$char" == "m" ]]; then
                        in_escape=false
                        ansi_chars+=("$esc_sequence")
                    fi
                else
                    chars+=("$char")
                fi
                ((i++))
            done

            # Now wrap by visible characters
            local wrapped_lines=()
            local current_line=""
            local current_len=0
            local ansi_buffer=""

            for ((j=0; j<${#chars[@]}; j++)); do
                char="${chars[$j]}"
                # Check if we have ANSI sequences to apply
                if [[ $j -lt ${#ansi_chars} ]]; then
                    ansi_buffer="${ansi_chars[$j]}"
                fi

                current_line+="$char"
                ((current_len++))

                if [[ $current_len -ge $max_width ]]; then
                    wrapped_lines+=("$current_line")
                    current_line=""
                    current_len=0
                fi
            done

            # Add remaining
            if [[ -n "$current_line" ]]; then
                wrapped_lines+=("$current_line")
            fi

            # Add wrapped lines to output
            for wrapped_line in "${wrapped_lines[@]}"; do
                processed_content+="$wrapped_line"$'\n'
            done
        fi
    done <<< "$content"

    # Remove trailing newline
    processed_content="${processed_content%$'\n'}"

    # Print box
    printf "${BOLD}${BLUE}┌"
    for ((i=0; i<max_width; i++)); do printf "─"; done
    printf "┐${RESET}\n"

    if [[ -n "$title" ]]; then
        printf "${BOLD}${BLUE}│${RESET} "
        printf "${BOLD}${WHITE}%s${RESET}" "$title"
        local title_visible=$(strip_ansi "$title")
        local title_len=${#title_visible}
        local pad=$((max_width - title_len - 1))
        [[ $pad -lt 0 ]] && pad=0
        printf "%*s" $pad ""
        printf "${BOLD}${BLUE}│${RESET}\n"
        printf "${BOLD}${BLUE}├"
        for ((i=0; i<max_width; i++)); do printf "─"; done
        printf "┤${RESET}\n"
    fi

    # Print content lines with padding
    while IFS= read -r line; do
        if [[ -z "$line" ]]; then
            printf "${BOLD}${BLUE}│${RESET} %-${max_width}s ${BOLD}${BLUE}│${RESET}\n" ""
        else
            local visible=$(strip_ansi "$line")
            local visible_len=${#visible}
            if [[ $visible_len -gt $max_width ]]; then
                # Truncate if still too long
                visible="${visible:0:$((max_width-3))}..."
                visible_len=$max_width
                printf "${BOLD}${BLUE}│${RESET} %s%*s ${BOLD}${BLUE}│${RESET}\n" "$visible" $((max_width - visible_len)) ""
            else
                printf "${BOLD}${BLUE}│${RESET} %s%*s ${BOLD}${BLUE}│${RESET}\n" "$line" $((max_width - visible_len)) ""
            fi
        fi
    done <<< "$processed_content"

    printf "${BOLD}${BLUE}└"
    for ((i=0; i<max_width; i++)); do printf "─"; done
    printf "┘${RESET}\n"
}

print_menu() {
    local title="$1"
    shift
    local items=("$@")
    local content=""
    local num=1
    for item in "${items[@]}"; do
        if [[ "$item" == *"Back to"* ]] || [[ "$item" == *"Exit"* ]] || [[ "$item" == *"Back"* ]]; then
            content+="${BOLD}${YELLOW}${num}.${RESET} ${item}"$'\n'
        else
            content+="${BOLD}${GREEN}${num}.${RESET} ${item}"$'\n'
        fi
        ((num++))
    done
    content+="${DIM}Type the number and press Enter. 0 to go back/exit.${RESET}"$'\n'
    print_box "$title" "$content"
}

show_spinner() {
    local pid=$1
    local message="$2"
    local spin='-\|/'
    local i=0
    while kill -0 $pid 2>/dev/null; do
        i=$(( (i+1) % 4 ))
        printf "\r${CYAN}[${spin:$i:1}] ${message}${RESET}"
        sleep 0.1
    done
    printf "\r${GREEN}[✓] ${message}${RESET}\n"
}

print_progress() {
    local current="$1"
    local total="$2"
    local message="$3"
    local width=40
    local percent=$((current * 100 / total))
    local filled=$((percent * width / 100))
    local empty=$((width - filled))
    printf "\r${CYAN}["
    printf "%${filled}s" | tr ' ' '='
    printf "%${empty}s" | tr ' ' ' '
    printf "] ${percent}%% ${message}${RESET}"
}

log_message() {
    local level="$1"
    local message="$2"
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    mkdir -p "$CONFIG_DIR"
    echo "[$timestamp] [$level] $message" >> "$LOG_FILE"
}

show_success() {
    echo -e "${GREEN}${EMOJI_CHECK} $1${RESET}"
    log_message "INFO" "$1"
}

show_warning() {
    echo -e "${YELLOW}${EMOJI_WARNING} $1${RESET}"
    log_message "WARNING" "$1"
}

show_error() {
    echo -e "${RED}${EMOJI_ERROR} $1${RESET}"
    log_message "ERROR" "$1"
}

show_info() {
    echo -e "${BLUE}${EMOJI_INFO} $1${RESET}"
    log_message "INFO" "$1"
}

confirm_action() {
    local prompt="$1"
    local response
    echo -e "${YELLOW}${EMOJI_WARNING} $prompt${RESET}"
    read -p "Continue? [y/N] " response
    case "$response" in
        [yY]|[yY][eE][sS]) return 0 ;;
        *) return 1 ;;
    esac
}

check_command() {
    command -v "$1" &>/dev/null
}

run_with_sudo() {
    local cmd="$1"
    if [[ "$IS_ROOT" == true ]]; then
        eval "$cmd" 2>/dev/null
    else
        echo -e "${YELLOW}${EMOJI_WARNING} This operation requires root privileges${RESET}"
        sudo bash -c "$cmd" 2>/dev/null || eval "$cmd" 2>/dev/null
    fi
}

# Auto-install missing tools
auto_install_tool() {
    local tool="$1"
    local package="${2:-$1}"

    if check_command "$tool"; then
        return 0
    fi

    show_warning "$tool is not installed"
    if confirm_action "Install $tool (package: $package)?"; then
        show_info "Installing $package..."
        case "$PKG_MANAGER" in
            apt|pkg) run_with_sudo "apt install -y $package" ;;
            dnf) run_with_sudo "dnf install -y $package" ;;
            yum) run_with_sudo "yum install -y $package" ;;
            pacman) run_with_sudo "pacman -S --noconfirm $package" ;;
            zypper) run_with_sudo "zypper install -y $package" ;;
            xbps) run_with_sudo "xbps-install -y $package" ;;
            apk) run_with_sudo "apk add $package" ;;
            brew) brew install "$package" ;;
            pkg) run_with_sudo "pkg install -y $package" ;;
            *) show_error "Cannot install $package: Unsupported package manager"; return 1 ;;
        esac

        if check_command "$tool"; then
            show_success "$tool installed successfully"
            return 0
        else
            show_error "Failed to install $tool"
            return 1
        fi
    else
        show_warning "Skipping $tool installation"
        return 1
    fi
}

install_dependencies() {
    local deps=("$@")
    local missing=()
    for dep in "${deps[@]}"; do
        if ! check_command "$dep"; then
            missing+=("$dep")
        fi
    done
    if [[ ${#missing[@]} -eq 0 ]]; then
        show_success "All dependencies are installed"
        return 0
    fi
    show_warning "Missing dependencies: ${missing[*]}"
    if confirm_action "Install missing dependencies?"; then
        for dep in "${missing[@]}"; do
            show_info "Installing $dep..."
            case "$PKG_MANAGER" in
                apt|pkg) run_with_sudo "apt install -y $dep" ;;
                dnf) run_with_sudo "dnf install -y $dep" ;;
                yum) run_with_sudo "yum install -y $dep" ;;
                pacman) run_with_sudo "pacman -S --noconfirm $dep" ;;
                zypper) run_with_sudo "zypper install -y $dep" ;;
                xbps) run_with_sudo "xbps-install -y $dep" ;;
                apk) run_with_sudo "apk add $dep" ;;
                brew) brew install "$dep" ;;
                *) show_error "Cannot install $dep: Unsupported package manager"; return 1 ;;
            esac
        done
        show_success "Dependencies installed successfully"
    else
        show_warning "Skipping dependency installation"
    fi
}

# ========================================
# First Run Wizard
# ========================================
first_run_wizard() {
    if [[ -f "$FIRST_RUN_FILE" ]]; then
        return 0
    fi
    show_info "First run detected! Running setup wizard..."
    detect_os
    detect_distro
    detect_package_manager
    if check_command "fastfetch"; then
        HAS_FASTFETCH=true
    else
        HAS_FASTFETCH=false
    fi
    local summary=""
    summary+="${BOLD}System Information:${RESET}\n"
    summary+="  Operating System: $OS\n"
    summary+="  Distribution: $DISTRO\n"
    summary+="  Distribution Version: $DISTRO_VERSION\n"
    summary+="  Package Manager: $PKG_MANAGER\n"
    summary+="  Terminal Size: ${TERMINAL_WIDTH}x${TERMINAL_HEIGHT}\n"
    summary+="  Color Support: $([ "$COLORS_SUPPORTED" == true ] && echo "Yes" || echo "No")\n"
    summary+="  Root Access: $([ "$IS_ROOT" == true ] && echo "Yes" || echo "No")\n"
    summary+="  Fullscreen Mode: $([ "$IS_FULLSCREEN" == true ] && echo "Yes" || echo "No")\n"
    summary+="  Fastfetch: $([ "$HAS_FASTFETCH" == true ] && echo "Installed" || echo "Not installed")\n"
    print_box "Setup Wizard" "$summary"

    local common_tools=("curl" "wget" "jq" "git" "tar" "unzip" "zip" "tree")
    local missing_tools=()
    for tool in "${common_tools[@]}"; do
        if ! check_command "$tool"; then
            missing_tools+=("$tool")
        fi
    done
    if [[ ${#missing_tools[@]} -gt 0 ]]; then
        show_warning "Missing common tools: ${missing_tools[*]}"
        if confirm_action "Install missing tools?"; then
            install_dependencies "${missing_tools[@]}"
        fi
    fi
    if [[ "$HAS_FASTFETCH" == false ]]; then
        if confirm_action "Install fastfetch for better system information?"; then
            install_dependencies "fastfetch"
        fi
    fi
    mkdir -p "$CONFIG_DIR"
    touch "$FIRST_RUN_FILE"
    show_success "First run setup complete!"
    sleep 2
}

# ========================================
# System Information Functions (FIXED)
# ========================================
show_fastfetch() {
    if check_command "fastfetch"; then
        fastfetch
    else
        show_warning "Fastfetch is not installed"
        if confirm_action "Install fastfetch?"; then
            install_dependencies "fastfetch"
            if check_command "fastfetch"; then
                fastfetch
            fi
        fi
    fi
}

show_system_info() {
    # Ensure hostname command exists
    if ! check_command "hostname"; then
        auto_install_tool "hostname" "inetutils"
    fi

    local info=""
    info+="${BOLD}System Information:${RESET}\n"
    info+="  Hostname: $(hostname 2>/dev/null || echo "Unknown")\n"
    info+="  OS: $OS $DISTRO $DISTRO_VERSION\n"
    info+="  Kernel: $(uname -r)\n"
    info+="  Architecture: $(uname -m)\n"
    info+="  Shell: $SHELL\n"
    info+="  User: $USER\n"
    info+="  Root Access: $([ "$IS_ROOT" == true ] && echo "Yes" || echo "No")\n"
    info+="  Fullscreen: $([ "$IS_FULLSCREEN" == true ] && echo "Yes" || echo "No")\n"
    info+="  Uptime: $(uptime -p 2>/dev/null || uptime | awk '{print $3,$4}' | sed 's/,//')\n"
    if [[ "$OS" == "Linux" ]]; then
        if [[ -f /proc/cpuinfo ]]; then
            info+="  CPU: $(grep "model name" /proc/cpuinfo | head -1 | cut -d: -f2 | xargs)\n"
            info+="  Cores: $(grep -c "processor" /proc/cpuinfo)\n"
        fi
        if [[ -f /proc/meminfo ]]; then
            local total_mem=$(grep "MemTotal" /proc/meminfo | awk '{print $2}')
            info+="  RAM: $((total_mem / 1024 / 1024)) GB\n"
        fi
    elif [[ "$OS" == "macOS" ]]; then
        info+="  CPU: $(sysctl -n machdep.cpu.brand_string 2>/dev/null || echo "Unknown")\n"
        info+="  Cores: $(sysctl -n hw.ncpu 2>/dev/null || echo "Unknown")\n"
        info+="  RAM: $(sysctl -n hw.memsize 2>/dev/null | awk '{print $1/1024/1024/1024 " GB"}')"
    fi
    print_box "System Information" "$info"
}

show_hardware_info() {
    # Check and install lshw or dmidecode
    if ! check_command "lshw" && ! check_command "dmidecode"; then
        show_info "Installing hardware detection tools..."
        auto_install_tool "lshw" "lshw" || auto_install_tool "dmidecode" "dmidecode"
    fi

    local info=""
    case "$OS" in
        Linux)
            if command -v lshw &>/dev/null; then
                info+="$(lshw -short 2>/dev/null | head -20)"
            elif command -v dmidecode &>/dev/null; then
                info+="$(dmidecode -t system 2>/dev/null)"
            else
                info+="Hardware detection tools not available"
            fi
            ;;
        macOS)
            info+="Hardware: $(system_profiler SPHardwareDataType 2>/dev/null | grep -v "Hardware Overview" | head -10)"
            ;;
        *)
            info+="Hardware information not fully supported on $OS"
            ;;
    esac
    print_box "Hardware Information" "$info"
}

show_cpu_info() {
    local info=""
    case "$OS" in
        Linux)
            if [[ -f /proc/cpuinfo ]]; then
                info+="$(cat /proc/cpuinfo | grep -E "model name|cpu cores|siblings|cache size" | head -10)"
                info+="\nCPU Usage: $(top -bn1 2>/dev/null | grep "Cpu(s)" | awk '{print $2}')%"
            fi
            ;;
        macOS)
            info+="CPU: $(sysctl -n machdep.cpu.brand_string 2>/dev/null || echo "Unknown")\n"
            info+="Cores: $(sysctl -n hw.ncpu 2>/dev/null || echo "Unknown")\n"
            info+="CPU Usage: $(top -l1 2>/dev/null | grep "CPU usage" | awk '{print $3,$4,$5}')"
            ;;
        *)
            info+="CPU information not supported on $OS"
            ;;
    esac
    print_box "CPU Information" "$info"
}

show_gpu_info() {
    local info=""
    case "$OS" in
        Linux)
            if command -v lspci &>/dev/null; then
                info+="$(lspci | grep -E "VGA|3D|Display" 2>/dev/null)"
            elif command -v glxinfo &>/dev/null; then
                info+="$(glxinfo 2>/dev/null | grep "OpenGL renderer")"
            else
                info+="Install pciutils or mesa-utils for GPU info"
                if confirm_action "Install lspci for GPU detection?"; then
                    auto_install_tool "lspci" "pciutils"
                    if command -v lspci &>/dev/null; then
                        info+="$(lspci | grep -E "VGA|3D|Display" 2>/dev/null)"
                    fi
                fi
            fi
            ;;
        macOS)
            info+="$(system_profiler SPDisplaysDataType 2>/dev/null | grep -v "Displays:" | head -20)"
            ;;
        *)
            info+="GPU information not supported on $OS"
            ;;
    esac
    print_box "GPU Information" "$info"
}

show_ram_info() {
    local info=""
    case "$OS" in
        Linux)
            if [[ -f /proc/meminfo ]]; then
                local total=$(grep "MemTotal" /proc/meminfo | awk '{print $2}')
                local free=$(grep "MemFree" /proc/meminfo | awk '{print $2}')
                local available=$(grep "MemAvailable" /proc/meminfo | awk '{print $2}')
                local used=$((total - available))
                local used_percent=$((used * 100 / total))

                info+="Total: $((total / 1024 / 1024)) GB\n"
                info+="Used: $((used / 1024 / 1024)) GB ($used_percent%)\n"
                info+="Free: $((free / 1024 / 1024)) GB\n"
                info+="Available: $((available / 1024 / 1024)) GB\n"
                info+="\nSwap Info:\n"
                info+="$(grep -E "SwapTotal|SwapFree|SwapCached" /proc/meminfo | awk '{printf "%s: %.2f GB\n", $1, $2/1024/1024}')"
            fi
            ;;
        macOS)
            local total=$(sysctl -n hw.memsize 2>/dev/null || echo 0)
            local used=$(vm_stat 2>/dev/null | grep "Pages active" | awk '{print $3}' | sed 's/\.//')
            local used_mb=$((used * 4096 / 1024 / 1024))
            info+="Total: $((total / 1024 / 1024 / 1024)) GB\n"
            info+="Used: ${used_mb} MB\n"
            ;;
        *)
            info+="RAM information not supported on $OS"
            ;;
    esac
    print_box "RAM Information" "$info"
}

show_motherboard_info() {
    if ! check_command "dmidecode"; then
        show_info "Installing dmidecode for motherboard info..."
        auto_install_tool "dmidecode" "dmidecode"
    fi

    local info=""
    case "$OS" in
        Linux)
            if command -v dmidecode &>/dev/null; then
                info+="$(dmidecode -t baseboard 2>/dev/null | grep -E "Manufacturer|Product|Version|Serial" | head -10)"
            else
                info+="dmidecode not available"
            fi
            ;;
        *)
            info+="Motherboard information only available on Linux with dmidecode"
            ;;
    esac
    print_box "Motherboard Information" "$info"
}

show_disk_info() {
    local info=""
    case "$OS" in
        Linux|Termux)
            info+="$(df -h | grep -E "^/dev|Filesystem" | column -t 2>/dev/null || df -h)"
            ;;
        macOS|FreeBSD|OpenBSD|NetBSD)
            info+="$(df -h | column -t 2>/dev/null || df -h)"
            ;;
        *)
            info+="Disk information not supported on $OS"
            ;;
    esac
    print_box "Disk Information" "$info"
}

show_kernel_info() {
    local info=""
    info+="Kernel: $(uname -r)\n"
    info+="Kernel Version: $(uname -v)\n"
    info+="Architecture: $(uname -m)\n"
    info+="Operating System: $(uname -o 2>/dev/null || echo $OS)"
    if [[ "$OS" == "Linux" ]]; then
        info+="\nKernel Modules: $(lsmod 2>/dev/null | wc -l)"
    fi
    print_box "Kernel Information" "$info"
}

show_uptime() {
    local info=""
    if command -v uptime &>/dev/null; then
        info+="Uptime: $(uptime -p 2>/dev/null || uptime | awk '{print $3,$4}' | sed 's/,//')\n"
        info+="Load: $(uptime | awk -F'load average:' '{print $2}' | xargs)"
    fi
    print_box "System Uptime" "$info"
}

show_environment_info() {
    local info=""
    info+="Shell: $SHELL\n"
    info+="Shell Version: $($SHELL --version 2>/dev/null | head -1)\n"
    info+="Terminal: $TERM\n"
    info+="Terminal Size: ${TERMINAL_WIDTH}x${TERMINAL_HEIGHT}\n"
    info+="Color Support: $([ "$COLORS_SUPPORTED" == true ] && echo "Yes" || echo "No")\n"
    info+="Emoji Support: $USE_EMOJIS\n"
    info+="Animations: $USE_ANIMATIONS\n"
    info+="Locale: $LANG\n"
    info+="Timezone: $(date +%Z)\n"
    info+="Date/Time: $(date '+%Y-%m-%d %H:%M:%S')"
    print_box "Environment Information" "$info"
}

# ========================================
# Monitoring Functions (FIXED)
# ========================================
show_cpu_usage() {
    local info=""
    case "$OS" in
        Linux) if command -v top &>/dev/null; then info+="$(top -bn1 2>/dev/null | head -15)"; fi ;;
        macOS) if command -v top &>/dev/null; then info+="$(top -l1 2>/dev/null | head -15)"; fi ;;
        *) if command -v top &>/dev/null; then info+="$(top -bn1 2>/dev/null | head -15)"; fi ;;
    esac
    print_box "CPU Usage" "$info"
}

show_ram_usage() {
    local info=""
    case "$OS" in
        Linux) if command -v free &>/dev/null; then info+="$(free -h)"; fi ;;
        macOS)
            if command -v vm_stat &>/dev/null; then
                info+="$(vm_stat 2>/dev/null)"
                info+="\nMemory Pressure: $(memory_pressure 2>/dev/null | grep "System-wide" || echo "N/A")"
            fi
            ;;
        *) if command -v free &>/dev/null; then info+="$(free -h 2>/dev/null)"; else info+="Memory monitoring not supported on $OS"; fi ;;
    esac
    print_box "RAM Usage" "$info"
}

show_disk_usage() {
    local info=""
    if command -v df &>/dev/null; then
        info+="$(df -h | grep -v "tmpfs" | column -t 2>/dev/null || df -h | grep -v "tmpfs")"
    fi
    print_box "Disk Usage" "$info"
}

show_network_usage() {
    if ! check_command "iftop"; then
        show_info "iftop not installed. Installing for network monitoring..."
        auto_install_tool "iftop" "iftop"
    fi

    local info=""
    if command -v iftop &>/dev/null; then
        info+="Interface: $(ip route 2>/dev/null | grep default | awk '{print $5}' || ifconfig 2>/dev/null | grep "inet " | head -1 | awk '{print $1}')\n"
        info+="\nNetwork Statistics:\n"
        info+="$(netstat -i 2>/dev/null | head -10 || echo "netstat not available")"
        info+="\n\nRun 'sudo iftop' for real-time monitoring"
    else
        info+="iftop installation failed"
    fi
    print_box "Network Usage" "$info"
}

show_running_processes() {
    local info=""
    if command -v ps &>/dev/null; then
        info+="$(ps aux 2>/dev/null | head -20 | column -t 2>/dev/null || ps aux | head -20)"
    fi
    print_box "Running Processes" "$info"
}

show_top_processes() {
    local info=""
    case "$OS" in
        Linux)
            info+="Top CPU Processes:\n"
            info+="$(ps aux --sort=-%cpu 2>/dev/null | head -10 | column -t 2>/dev/null || ps aux --sort=-%cpu | head -10)\n\n"
            info+="Top Memory Processes:\n"
            info+="$(ps aux --sort=-%mem 2>/dev/null | head -10 | column -t 2>/dev/null || ps aux --sort=-%mem | head -10)"
            ;;
        macOS)
            info+="Top CPU Processes:\n"
            info+="$(ps aux -r 2>/dev/null | head -10 | column -t 2>/dev/null || ps aux -r | head -10)\n\n"
            info+="Top Memory Processes:\n"
            info+="$(ps aux -m 2>/dev/null | head -10 | column -t 2>/dev/null || ps aux -m | head -10)"
            ;;
        *)
            info+="$(ps aux 2>/dev/null | sort -k3 -r | head -10 | column -t 2>/dev/null || ps aux | sort -k3 -r | head -10)"
            ;;
    esac
    print_box "Top Processes" "$info"
}

show_resource_monitor() {
    case "$OS" in
        Linux) if command -v htop &>/dev/null; then htop; return; elif command -v top &>/dev/null; then top; return; fi ;;
        macOS) if command -v htop &>/dev/null; then htop; return; elif command -v top &>/dev/null; then top; return; fi ;;
        *) if command -v top &>/dev/null; then top; return; fi ;;
    esac
    show_error "No resource monitor available"
}

show_temperature() {
    if ! check_command "sensors"; then
        show_info "lm-sensors not installed. Installing for temperature monitoring..."
        auto_install_tool "sensors" "lm-sensors"
    fi

    local info=""
    case "$OS" in
        Linux)
            if command -v sensors &>/dev/null; then
                info+="$(sensors 2>/dev/null)"
            elif [[ -f /sys/class/thermal/thermal_zone0/temp ]]; then
                local temp=$(cat /sys/class/thermal/thermal_zone0/temp 2>/dev/null || echo 0)
                info+="CPU Temperature: $((temp / 1000))°C"
            else
                info+="Temperature sensors not available"
            fi
            ;;
        macOS)
            if command -v osx-cpu-temp &>/dev/null; then
                info+="$(osx-cpu-temp 2>/dev/null)"
            else
                info+="Install osx-cpu-temp for temperature monitoring"
                if confirm_action "Install osx-cpu-temp?"; then
                    brew install osx-cpu-temp
                fi
            fi
            ;;
        *)
            info+="Temperature monitoring not supported on $OS"
            ;;
    esac
    print_box "System Temperature" "$info"
}

# ========================================
# Network Functions (FIXED)
# ========================================
ping_test() {
    local target="${1:-google.com}"
    local info="Pinging $target...\n"
    info+="$(ping -c 4 "$target" 2>&1)"
    print_box "Ping Test" "$info"
}

internet_test() {
    local info=""
    if ping -c 1 -W 2 8.8.8.8 &>/dev/null; then
        info+="Internet Connection: ✅ Connected\n"
        info+="DNS Resolution: $(nslookup google.com 2>/dev/null | grep "Address" | head -1 || echo "N/A")\n"
        info+="Public IP: $(curl -s ifconfig.me 2>/dev/null || curl -s icanhazip.com 2>/dev/null || echo "N/A")"
    else
        info+="Internet Connection: ❌ Disconnected"
    fi
    print_box "Internet Test" "$info"
}

dns_test() {
    local info=""
    info+="DNS Servers:\n"
    if [[ -f /etc/resolv.conf ]]; then
        info+="$(grep "^nameserver" /etc/resolv.conf 2>/dev/null | awk '{print $2}')\n"
    fi
    info+="\nDNS Resolution Tests:\n"
    for domain in google.com github.com stackoverflow.com; do
        info+="$domain: $(dig +short "$domain" 2>/dev/null | head -1 || echo "N/A")\n"
    done
    print_box "DNS Test" "$info"
}

show_public_ip() {
    local info=""
    # Try multiple services with fallbacks
    local ipv4=""
    local ipv6=""
    local location=""

    # Try multiple IPv4 services
    for service in ifconfig.me icanhazip.com ipinfo.io/ip api.ipify.org; do
        ipv4=$(curl -s --max-time 3 "$service" 2>/dev/null | grep -E '^[0-9.]+$' | head -1)
        if [[ -n "$ipv4" ]]; then
            break
        fi
    done
    [[ -z "$ipv4" ]] && ipv4="N/A"

    # Try IPv6
    ipv6=$(curl -s --max-time 3 -6 ifconfig.me 2>/dev/null | grep -E '^[0-9a-f:]+$' | head -1)
    [[ -z "$ipv6" ]] && ipv6="N/A"

    # Location
    location=$(curl -s --max-time 3 ipapi.co/city 2>/dev/null | grep -E '^[A-Za-z]' | head -1)
    [[ -z "$location" ]] && location=$(curl -s --max-time 3 ipinfo.io/city 2>/dev/null | tr -d '"')
    [[ -z "$location" ]] && location="N/A"

    info+="IPv4: $ipv4\n"
    info+="IPv6: $ipv6\n"
    info+="Location: $location"
    print_box "Public IP" "$info"
}

show_local_ip() {
    local info=""
    info+="Interface IPs:\n"
    if command -v ip &>/dev/null; then
        info+="$(ip addr show 2>/dev/null | grep "inet " | grep -v "127.0.0.1" | awk '{print $2, $NF}')"
    elif command -v ifconfig &>/dev/null; then
        info+="$(ifconfig 2>/dev/null | grep "inet " | grep -v "127.0.0.1" | awk '{print $2, $NF}')"
    else
        info+="No network tools available"
    fi
    print_box "Local IP" "$info"
}

show_gateway() {
    local info=""
    if command -v ip &>/dev/null; then
        info+="$(ip route 2>/dev/null | grep default | awk '{print $3}')"
    elif command -v route &>/dev/null; then
        info+="$(route -n 2>/dev/null | grep "^0.0.0.0" | awk '{print $2}')"
    else
        info+="Gateway detection not available"
    fi
    print_box "Default Gateway" "$info"
}

show_wifi_info() {
    local info=""
    case "$OS" in
        Linux)
            if command -v nmcli &>/dev/null; then
                info+="$(nmcli dev wifi list 2>/dev/null | head -10)"
            elif command -v iwconfig &>/dev/null; then
                info+="$(iwconfig 2>/dev/null | grep -E "ESSID|Signal|Quality")"
            else
                info+="Wi-Fi tools not available"
                if confirm_action "Install network manager for Wi-Fi info?"; then
                    auto_install_tool "nmcli" "networkmanager"
                    if command -v nmcli &>/dev/null; then
                        info+="$(nmcli dev wifi list 2>/dev/null | head -10)"
                    fi
                fi
            fi
            ;;
        macOS)
            if command -v /System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport &>/dev/null; then
                info+="$(/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -s 2>/dev/null | head -10)"
            else
                info+="Airport not available"
            fi
            ;;
        Termux)
            if command -v termux-wifi-connectioninfo &>/dev/null; then
                info+="$(termux-wifi-connectioninfo 2>/dev/null)"
            else
                info+="Install termux-api for Wi-Fi info"
                if confirm_action "Install termux-api?"; then
                    pkg install termux-api
                fi
            fi
            ;;
        *)
            info+="Wi-Fi information not supported on $OS"
            ;;
    esac
    print_box "Wi-Fi Information" "$info"
}

# ========================================
# Power Functions
# ========================================
show_battery_info() {
    local info=""
    case "$OS" in
        Linux)
            if [[ -d /sys/class/power_supply/BAT0 ]]; then
                local capacity=$(cat /sys/class/power_supply/BAT0/capacity 2>/dev/null || echo "N/A")
                local status=$(cat /sys/class/power_supply/BAT0/status 2>/dev/null || echo "N/A")
                local health=$(cat /sys/class/power_supply/BAT0/health 2>/dev/null || echo "N/A")
                info+="Battery: ${capacity}%\n"
                info+="Status: ${status}\n"
                info+="Health: ${health}\n"
                if [[ -f /sys/class/power_supply/BAT0/cycle_count ]]; then
                    info+="Cycles: $(cat /sys/class/power_supply/BAT0/cycle_count 2>/dev/null || echo "N/A")"
                fi
            else
                info+="No battery found"
            fi
            ;;
        macOS)
            if command -v pmset &>/dev/null; then
                info+="$(pmset -g batt 2>/dev/null | grep -v "Now drawing" | head -5)"
                info+="\nHealth: $(system_profiler SPPowerDataType 2>/dev/null | grep "Health" | awk '{print $3}')"
                info+="\nCycles: $(system_profiler SPPowerDataType 2>/dev/null | grep "Cycle Count" | awk '{print $3}')"
            fi
            ;;
        Termux)
            if command -v termux-battery-status &>/dev/null; then
                info+="$(termux-battery-status 2>/dev/null)"
            else
                info+="Install termux-api for battery info"
                if confirm_action "Install termux-api?"; then
                    pkg install termux-api
                fi
            fi
            ;;
        *)
            info+="Battery information not supported on $OS"
            ;;
    esac
    print_box "Battery Information" "$info"
}

# ========================================
# Package Manager Menu (FIXED - with sudo)
# ========================================
package_manager_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_PACKAGE} Package Manager (${PKG_MANAGER})" \
            "Update packages" \
            "Upgrade packages" \
            "Clean cache" \
            "Autoremove" \
            "Search packages" \
            "List installed packages" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1)
                show_info "Updating package lists..."
                if [[ "$IS_ROOT" == false ]]; then
                    show_warning "This requires root privileges"
                    if confirm_action "Continue with sudo?"; then
                        run_with_sudo "$PKG_UPDATE"
                    fi
                else
                    eval "$PKG_UPDATE"
                fi
                show_success "Package lists updated"
                read -p "Press Enter to continue"
                ;;
            2)
                if confirm_action "Upgrade all packages?"; then
                    show_info "Upgrading packages..."
                    if [[ "$IS_ROOT" == false ]]; then
                        show_warning "This requires root privileges"
                        if confirm_action "Continue with sudo?"; then
                            run_with_sudo "$PKG_UPGRADE"
                        fi
                    else
                        eval "$PKG_UPGRADE"
                    fi
                    show_success "Packages upgraded"
                fi
                read -p "Press Enter to continue"
                ;;
            3)
                if confirm_action "Clean package cache?"; then
                    show_info "Cleaning cache..."
                    if [[ "$IS_ROOT" == false ]]; then
                        show_warning "This requires root privileges"
                        if confirm_action "Continue with sudo?"; then
                            run_with_sudo "$PKG_CLEAN"
                        fi
                    else
                        eval "$PKG_CLEAN"
                    fi
                    show_success "Cache cleaned"
                fi
                read -p "Press Enter to continue"
                ;;
            4)
                if confirm_action "Remove unused packages?"; then
                    show_info "Removing unused packages..."
                    if [[ "$IS_ROOT" == false ]]; then
                        show_warning "This requires root privileges"
                        if confirm_action "Continue with sudo?"; then
                            run_with_sudo "$PKG_AUTOREMOVE"
                        fi
                    else
                        eval "$PKG_AUTOREMOVE"
                    fi
                    show_success "Unused packages removed"
                fi
                read -p "Press Enter to continue"
                ;;
            5)
                read -p "Search term: " search_term
                eval "$PKG_SEARCH $search_term"
                read -p "Press Enter to continue"
                ;;
            6)
                eval "$PKG_LIST"
                read -p "Press Enter to continue"
                ;;
            0|7) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
    done
}

# ========================================
# Cleaner Menu (FIXED - no exits, with sudo)
# ========================================
cleaner_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_CLEANER} System Cleaner" \
            "User cache" \
            "Package cache" \
            "Temporary files" \
            "Old logs" \
            "Empty Trash" \
            "Homebrew cleanup (macOS)" \
            "Timeshift snapshot cleanup (Linux)" \
            "Snapper cleanup (Linux)" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) clean_user_cache ;;
            2) clean_package_cache ;;
            3) clean_temp_files ;;
            4) clean_old_logs ;;
            5) clean_trash ;;
            6) clean_homebrew ;;
            7) clean_timeshift ;;
            8) clean_snapper ;;
            0|9) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

clean_user_cache() {
    local cache_dir="${HOME}/.cache"
    if [[ -d "$cache_dir" ]]; then
        local size=$(du -sh "$cache_dir" 2>/dev/null | awk '{print $1}')
        show_info "Cache directory: $cache_dir ($size)"
        if confirm_action "Delete user cache?"; then
            rm -rf "$cache_dir"/* 2>/dev/null
            show_success "User cache cleaned"
        fi
    else
        show_warning "No user cache directory found"
    fi
}

clean_package_cache() {
    if confirm_action "Clean package cache?"; then
        if [[ "$IS_ROOT" == false ]]; then
            show_warning "This requires root privileges"
            if confirm_action "Continue with sudo?"; then
                run_with_sudo "$PKG_CLEAN"
            fi
        else
            eval "$PKG_CLEAN"
        fi
        show_success "Package cache cleaned"
    fi
}

clean_temp_files() {
    local temp_dir="/tmp"
    if [[ -d "$temp_dir" ]]; then
        local size=$(du -sh "$temp_dir" 2>/dev/null | awk '{print $1}')
        show_info "Temporary directory: $temp_dir ($size)"
        if confirm_action "Delete temporary files?"; then
            if [[ "$IS_ROOT" == false ]]; then
                show_warning "This requires root privileges"
                if confirm_action "Continue with sudo?"; then
                    run_with_sudo "rm -rf /tmp/*"
                fi
            else
                rm -rf /tmp/* 2>/dev/null
            fi
            show_success "Temporary files cleaned"
        fi
    fi
}

clean_old_logs() {
    local log_dir="/var/log"
    if [[ -d "$log_dir" ]]; then
        local size=$(du -sh "$log_dir" 2>/dev/null | awk '{print $1}')
        show_info "Log directory: $log_dir ($size)"
        if confirm_action "Clean old logs (keeping last 7 days)?"; then
            if [[ "$IS_ROOT" == false ]]; then
                show_warning "This requires root privileges"
                if confirm_action "Continue with sudo?"; then
                    run_with_sudo "find $log_dir -name '*.log' -mtime +7 -delete 2>/dev/null"
                fi
            else
                find "$log_dir" -name "*.log" -mtime +7 -delete 2>/dev/null
            fi
            show_success "Old logs cleaned"
        fi
    else
        show_warning "No log directory found"
    fi
}

clean_trash() {
    local trash_dirs=()
    case "$OS" in
        Linux) trash_dirs=("${HOME}/.local/share/Trash" "${HOME}/.Trash") ;;
        macOS) trash_dirs=("${HOME}/.Trash") ;;
        *) trash_dirs=("${HOME}/.Trash" "${HOME}/Trash") ;;
    esac
    for trash in "${trash_dirs[@]}"; do
        if [[ -d "$trash" ]]; then
            local size=$(du -sh "$trash" 2>/dev/null | awk '{print $1}')
            show_info "Trash: $trash ($size)"
            if confirm_action "Empty trash?"; then
                rm -rf "$trash"/* 2>/dev/null
                show_success "Trash emptied"
            fi
        fi
    done
}

clean_homebrew() {
    if [[ "$OS" == "macOS" ]] && command -v brew &>/dev/null; then
        if confirm_action "Run Homebrew cleanup?"; then
            brew cleanup --prune=all
            show_success "Homebrew cleanup complete"
        fi
    else
        show_warning "Homebrew not available"
    fi
}

clean_timeshift() {
    if command -v timeshift &>/dev/null; then
        if confirm_action "Clean Timeshift snapshots?"; then
            if [[ "$IS_ROOT" == false ]]; then
                show_warning "This requires root privileges"
                if confirm_action "Continue with sudo?"; then
                    run_with_sudo "timeshift --list-snapshots"
                fi
            else
                timeshift --list-snapshots
            fi
            show_success "Timeshift cleanup complete"
        fi
    else
        show_warning "Timeshift not installed"
        if confirm_action "Install Timeshift?"; then
            auto_install_tool "timeshift" "timeshift"
        fi
    fi
}

clean_snapper() {
    if command -v snapper &>/dev/null; then
        if confirm_action "Clean Snapper snapshots?"; then
            if [[ "$IS_ROOT" == false ]]; then
                show_warning "This requires root privileges"
                if confirm_action "Continue with sudo?"; then
                    run_with_sudo "snapper list"
                fi
            else
                snapper list
            fi
            show_success "Snapper cleanup complete"
        fi
    else
        show_warning "Snapper not installed"
        if confirm_action "Install Snapper?"; then
            auto_install_tool "snapper" "snapper"
        fi
    fi
}

# ========================================
# Storage Menu (FIXED - with progress)
# ========================================
storage_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_STORAGE} Storage" \
            "Disk usage" \
            "Largest directories" \
            "Largest files" \
            "Mounted drives" \
            "SMART status (if available)" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) show_disk_usage ;;
            2) largest_directories ;;
            3) largest_files ;;
            4) show_mounted_drives ;;
            5) show_smart_status ;;
            0|6) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

largest_directories() {
    local dir="${1:-$HOME}"
    show_info "${EMOJI_HOURGLASS} Scanning directories in $dir (this may take a moment)..."
    local info=""
    if command -v du &>/dev/null; then
        info+="Largest directories in $dir:\n"
        info+="$(du -sh "$dir"/* 2>/dev/null 2>/dev/null | sort -hr 2>/dev/null | head -15)"
    fi
    if [[ -z "$info" ]] || [[ "$info" == "Largest directories in $dir:\n" ]]; then
        info+="No directories found or permission denied"
    fi
    print_box "Largest Directories" "$info"
}

largest_files() {
    local dir="${1:-$HOME}"
    show_info "${EMOJI_HOURGLASS} Scanning files in $dir (this may take a while)..."
    local info=""
    if command -v find &>/dev/null; then
        info+="Largest files in $dir:\n"
        # Use head -15 to limit output
        info+="$(find "$dir" -type f -exec du -h {} + 2>/dev/null | sort -hr 2>/dev/null | head -15)"
    fi
    if [[ -z "$info" ]] || [[ "$info" == "Largest files in $dir:\n" ]]; then
        info+="No files found or permission denied"
    fi
    print_box "Largest Files" "$info"
}

show_mounted_drives() {
    local info=""
    if command -v mount &>/dev/null; then
        info+="$(mount 2>/dev/null | grep -E "^/dev|^/mnt|^/media" | head -20)"
    fi
    print_box "Mounted Drives" "$info"
}

show_smart_status() {
    if ! check_command "smartctl"; then
        show_warning "smartctl not installed"
        if confirm_action "Install smartmontools?"; then
            auto_install_tool "smartctl" "smartmontools"
        fi
    fi

    local info=""
    if command -v smartctl &>/dev/null; then
        local drives=$(lsblk -d -o NAME 2>/dev/null | grep -E "^sd|^hd|^nvme" | head -3)
        for drive in $drives; do
            info+="Drive: /dev/$drive\n"
            if [[ "$IS_ROOT" == false ]]; then
                info+="  (sudo required for SMART data)\n"
                info+="  $(run_with_sudo "smartctl -H /dev/$drive" 2>/dev/null | grep "SMART overall-health" || echo "  SMART status unknown")\n\n"
            else
                info+="  $(smartctl -H /dev/"$drive" 2>/dev/null | grep "SMART overall-health" || echo "  SMART status unknown")\n"
                info+="  $(smartctl -A /dev/"$drive" 2>/dev/null | grep -E "Reallocated|Pending|Uncorrectable" | head -3 || echo "  No SMART data")\n\n"
            fi
        done
    else
        info+="smartctl not available"
    fi
    print_box "SMART Status" "$info"
}

# ========================================
# Security Menu
# ========================================
security_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_SECURITY} Security" \
            "Firewall status" \
            "Open ports" \
            "SSH status" \
            "Running services" \
            "Security recommendations" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) show_firewall_status ;;
            2) show_open_ports ;;
            3) show_ssh_status ;;
            4) show_running_services ;;
            5) show_security_recommendations ;;
            0|6) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

show_firewall_status() {
    local info=""
    case "$OS" in
        Linux)
            if command -v ufw &>/dev/null; then
                info+="UFW Status: $(ufw status 2>/dev/null | head -1)\n"
            fi
            if command -v iptables &>/dev/null; then
                info+="\nIPTables Rules:\n"
                if [[ "$IS_ROOT" == false ]]; then
                    info+="  (sudo required for iptables)\n"
                    info+="$(run_with_sudo "iptables -L -n" 2>/dev/null | head -10 || echo "  Unable to read iptables")"
                else
                    info+="$(iptables -L -n 2>/dev/null | head -10 || echo "  Unable to read iptables")"
                fi
            fi
            if command -v firewall-cmd &>/dev/null; then
                info+="\nFirewallD Status: $(firewall-cmd --state 2>/dev/null || echo "inactive")"
            fi
            ;;
        macOS)
            if command -v /usr/libexec/ApplicationFirewall/socketfilterfw &>/dev/null; then
                info+="Firewall Status: $(/usr/libexec/ApplicationFirewall/socketfilterfw --getglobalstate 2>/dev/null || echo "Unknown")"
            fi
            ;;
        OpenBSD)
            if command -v pfctl &>/dev/null; then
                info+="PF Status: $(pfctl -s info 2>/dev/null | head -5 || echo "Unable to read PF")"
            fi
            ;;
        *)
            info+="Firewall monitoring not supported on $OS"
            ;;
    esac
    print_box "Firewall Status" "$info"
}

show_open_ports() {
    local info=""
    if command -v netstat &>/dev/null; then
        info+="$(netstat -tulpn 2>/dev/null | grep LISTEN | head -15 || echo "No listening ports found")"
    elif command -v ss &>/dev/null; then
        info+="$(ss -tulpn 2>/dev/null | head -15 || echo "No listening ports found")"
    elif command -v lsof &>/dev/null; then
        if [[ "$IS_ROOT" == false ]]; then
            info+="(sudo required for lsof)\n"
            info+="$(run_with_sudo "lsof -i -P -n" 2>/dev/null | grep LISTEN | head -15 || echo "No listening ports found")"
        else
            info+="$(lsof -i -P -n 2>/dev/null | grep LISTEN | head -15 || echo "No listening ports found")"
        fi
    else
        info+="No network tools available"
    fi
    print_box "Open Ports" "$info"
}

show_ssh_status() {
    local info=""
    if command -v systemctl &>/dev/null && (systemctl is-active sshd &>/dev/null || systemctl is-active ssh &>/dev/null); then
        info+="SSH Service: Active\n"
        info+="SSH Config: $(sshd -T 2>/dev/null | grep -E "PermitRootLogin|PasswordAuthentication|Port" | head -5 || echo "Unable to read SSH config")"
    elif command -v service &>/dev/null && service ssh status &>/dev/null; then
        info+="SSH Service: $(service ssh status 2>/dev/null | head -1 || echo "Unknown")"
    else
        info+="SSH service not detected"
    fi
    print_box "SSH Status" "$info"
}

show_running_services() {
    local info=""
    if command -v systemctl &>/dev/null; then
        info+="$(systemctl list-units --type=service --state=running 2>/dev/null | head -15 || echo "No services found")"
    elif command -v service &>/dev/null; then
        info+="$(service --status-all 2>/dev/null | head -15 || echo "No services found")"
    elif [[ "$OS" == "macOS" ]]; then
        info+="$(run_with_sudo "launchctl list" 2>/dev/null | head -15 || echo "No services found")"
    else
        info+="Service management not supported on $OS"
    fi
    print_box "Running Services" "$info"
}

show_security_recommendations() {
    local info=""
    info+="${BOLD}Security Recommendations:${RESET}\n\n"
    info+="1. Keep system updated: $PKG_UPDATE && $PKG_UPGRADE\n"
    info+="2. Enable firewall: ufw enable (Ubuntu/Debian)\n"
    info+="3. Disable root SSH login: PermitRootLogin no\n"
    info+="4. Use SSH keys instead of passwords\n"
    info+="5. Remove unused packages: $PKG_AUTOREMOVE\n"
    info+="6. Check for open ports regularly\n"
    info+="7. Use strong passwords/passphrases\n"
    info+="8. Enable disk encryption\n"
    info+="9. Backup important data\n"
    info+="10. Review system logs regularly"
    print_box "Security Recommendations" "$info"
}

# ========================================
# File Tools Menu (FIXED - no exits, with progress)
# ========================================
file_tools_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_FILES} File Tools" \
            "Find files" \
            "Find duplicate files" \
            "Search text in files" \
            "Directory tree" \
            "File statistics" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) find_files ;;
            2) find_duplicates ;;
            3) search_text ;;
            4) show_directory_tree ;;
            5) show_file_stats ;;
            0|6) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

find_files() {
    read -p "Directory to search [${HOME}]: " search_dir
    search_dir="${search_dir:-$HOME}"
    read -p "Pattern (e.g., *.txt): " pattern
    if [[ -n "$pattern" ]]; then
        show_info "${EMOJI_HOURGLASS} Searching for files matching $pattern..."
        local info=""
        info+="Files matching $pattern in $search_dir:\n"
        info+="$(find "$search_dir" -type f -name "$pattern" 2>/dev/null | head -20)"
        if [[ -z "$info" ]] || [[ "$info" == "Files matching $pattern in $search_dir:\n" ]]; then
            info+="No files found"
        fi
        print_box "Find Files" "$info"
    fi
}

find_duplicates() {
    read -p "Directory to scan [${HOME}]: " scan_dir
    scan_dir="${scan_dir:-$HOME}"
    show_info "${EMOJI_HOURGLASS} Scanning for duplicates (this may take a long time)..."
    local info=""
    info+="Duplicate files (same content):\n"
    info+="$(find "$scan_dir" -type f -exec md5sum {} \; 2>/dev/null | sort | uniq -d -w32 | head -20)"
    if [[ -z "$info" ]] || [[ "$info" == "Duplicate files (same content):\n" ]]; then
        info+="No duplicates found"
    fi
    print_box "Duplicate Files" "$info"
}

search_text() {
    read -p "Text to search: " search_text
    read -p "Directory to search [${HOME}]: " search_dir
    search_dir="${search_dir:-$HOME}"
    if [[ -n "$search_text" ]]; then
        show_info "${EMOJI_HOURGLASS} Searching for '$search_text'..."
        local info=""
        info+="Searching for '$search_text' in $search_dir:\n"
        info+="$(grep -r -l "$search_text" "$search_dir" 2>/dev/null | head -20)"
        if [[ -z "$info" ]] || [[ "$info" == "Searching for '$search_text' in $search_dir:\n" ]]; then
            info+="No matches found"
        fi
        print_box "Text Search" "$info"
    fi
}

show_directory_tree() {
    read -p "Directory [${HOME}]: " tree_dir
    tree_dir="${tree_dir:-$HOME}"
    if [[ ! -d "$tree_dir" ]]; then
        show_error "Directory not found: $tree_dir"
        return
    fi
    show_info "${EMOJI_HOURGLASS} Generating tree for $tree_dir..."
    local info=""
    if command -v tree &>/dev/null; then
        info+="$(tree -L 2 "$tree_dir" 2>/dev/null | head -30)"
    else
        info+="$(find "$tree_dir" -maxdepth 2 -type d 2>/dev/null | head -20)"
    fi
    if [[ -z "$info" ]]; then
        info+="No directories found or permission denied"
    fi
    print_box "Directory Tree" "$info"
}

show_file_stats() {
    read -p "Directory [${HOME}]: " stats_dir
    stats_dir="${stats_dir:-$HOME}"
    if [[ ! -d "$stats_dir" ]]; then
        show_error "Directory not found: $stats_dir"
        return
    fi
    show_info "${EMOJI_HOURGLASS} Calculating statistics for $stats_dir..."
    local info=""
    if [[ -d "$stats_dir" ]]; then
        info+="Directory: $stats_dir\n"
        info+="Total files: $(find "$stats_dir" -type f 2>/dev/null | wc -l)\n"
        info+="Total directories: $(find "$stats_dir" -type d 2>/dev/null | wc -l)\n"
        info+="Total size: $(du -sh "$stats_dir" 2>/dev/null | awk '{print $1}')"
    fi
    print_box "File Statistics" "$info"
}

# ========================================
# Archive Tools Menu
# ========================================
archive_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_ARCHIVE} Archive Tools" \
            "Extract ZIP" \
            "Extract TAR" \
            "Create ZIP" \
            "Create TAR.GZ" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) extract_zip ;;
            2) extract_tar ;;
            3) create_zip ;;
            4) create_targz ;;
            0|5) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

extract_zip() {
    read -p "ZIP file path: " zip_file
    if [[ -f "$zip_file" ]]; then
        read -p "Extract to [current directory]: " extract_dir
        extract_dir="${extract_dir:-.}"
        if command -v unzip &>/dev/null; then
            unzip "$zip_file" -d "$extract_dir"
            show_success "Extracted to $extract_dir"
        else
            show_warning "unzip not installed"
            if confirm_action "Install unzip?"; then
                auto_install_tool "unzip" "unzip"
                if command -v unzip &>/dev/null; then
                    unzip "$zip_file" -d "$extract_dir"
                    show_success "Extracted to $extract_dir"
                fi
            fi
        fi
    else
        show_error "File not found"
    fi
}

extract_tar() {
    read -p "TAR file path: " tar_file
    if [[ -f "$tar_file" ]]; then
        read -p "Extract to [current directory]: " extract_dir
        extract_dir="${extract_dir:-.}"
        if command -v tar &>/dev/null; then
            tar -xf "$tar_file" -C "$extract_dir"
            show_success "Extracted to $extract_dir"
        else
            show_warning "tar not installed"
            if confirm_action "Install tar?"; then
                auto_install_tool "tar" "tar"
                if command -v tar &>/dev/null; then
                    tar -xf "$tar_file" -C "$extract_dir"
                    show_success "Extracted to $extract_dir"
                fi
            fi
        fi
    else
        show_error "File not found"
    fi
}

create_zip() {
    read -p "Directory to zip: " zip_dir
    if [[ -d "$zip_dir" ]]; then
        read -p "Output zip name: " zip_name
        if command -v zip &>/dev/null; then
            zip -r "$zip_name" "$zip_dir"
            show_success "Created $zip_name"
        else
            show_warning "zip not installed"
            if confirm_action "Install zip?"; then
                auto_install_tool "zip" "zip"
                if command -v zip &>/dev/null; then
                    zip -r "$zip_name" "$zip_dir"
                    show_success "Created $zip_name"
                fi
            fi
        fi
    else
        show_error "Directory not found"
    fi
}

create_targz() {
    read -p "Directory to archive: " tar_dir
    if [[ -d "$tar_dir" ]]; then
        read -p "Output tar.gz name: " tar_name
        if command -v tar &>/dev/null; then
            tar -czf "$tar_name" "$tar_dir"
            show_success "Created $tar_name"
        else
            show_warning "tar not installed"
            if confirm_action "Install tar?"; then
                auto_install_tool "tar" "tar"
                if command -v tar &>/dev/null; then
                    tar -czf "$tar_name" "$tar_dir"
                    show_success "Created $tar_name"
                fi
            fi
        fi
    else
        show_error "Directory not found"
    fi
}

# ========================================
# Utility Tools Menu
# ========================================
utility_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_UTILITY} Utility Tools" \
            "Password Generator" \
            "Hash Generator" \
            "UUID Generator" \
            "Random String Generator" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) generate_password ;;
            2) generate_hash ;;
            3) generate_uuid ;;
            4) generate_random_string ;;
            0|5) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

generate_password() {
    read -p "Password length [16]: " length
    length="${length:-16}"
    local password=$(tr -dc 'A-Za-z0-9!@#$%^&*()_+-=' < /dev/urandom 2>/dev/null | head -c "$length")
    print_box "Generated Password" "$password"
}

generate_hash() {
    clear
    print_header
    print_menu "Hash Generator" \
        "MD5" \
        "SHA1" \
        "SHA256" \
        "SHA512" \
        "Back"
    read -p "Select hash type: " hash_choice
    [[ "$hash_choice" == "0" || "$hash_choice" == "5" ]] && return
    read -p "Enter text to hash: " text
    local hash=""
    case "$hash_choice" in
        1) hash=$(echo -n "$text" | md5sum 2>/dev/null | awk '{print $1}') ;;
        2) hash=$(echo -n "$text" | sha1sum 2>/dev/null | awk '{print $1}') ;;
        3) hash=$(echo -n "$text" | sha256sum 2>/dev/null | awk '{print $1}') ;;
        4) hash=$(echo -n "$text" | sha512sum 2>/dev/null | awk '{print $1}') ;;
        *) show_error "Invalid option"; return ;;
    esac
    print_box "Generated Hash" "$hash"
}

generate_uuid() {
    local uuid=""
    if command -v uuidgen &>/dev/null; then
        uuid=$(uuidgen 2>/dev/null)
    elif [[ -f /proc/sys/kernel/random/uuid ]]; then
        uuid=$(cat /proc/sys/kernel/random/uuid 2>/dev/null)
    else
        uuid="uuidgen not available"
        if confirm_action "Install uuidgen?"; then
            auto_install_tool "uuidgen" "util-linux"
            if command -v uuidgen &>/dev/null; then
                uuid=$(uuidgen 2>/dev/null)
            fi
        fi
    fi
    print_box "Generated UUID" "$uuid"
}

generate_random_string() {
    read -p "Random string length [16]: " length
    length="${length:-16}"
    local string=$(tr -dc 'A-Za-z0-9' < /dev/urandom 2>/dev/null | head -c "$length")
    print_box "Random String" "$string"
}

# ========================================
# Internet Tools Menu
# ========================================
internet_tools_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_INTERNET} Internet Tools" \
            "Weather" \
            "Current Time" \
            "Calendar" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) show_weather ;;
            2) show_current_time ;;
            3) show_calendar ;;
            0|4) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

show_weather() {
    local info=""
    read -p "City (default: London): " city
    city="${city:-London}"
    if command -v curl &>/dev/null; then
        show_info "Fetching weather for $city..."
        local weather=$(curl -s --max-time 5 "wttr.in/$city?format=%c+%t+%w" 2>/dev/null)
        if [[ -n "$weather" ]]; then
            info+="Weather for $city:\n"
            info+="$weather"
        else
            info+="Unable to fetch weather for $city"
        fi
    else
        info+="curl is required for weather data"
        if confirm_action "Install curl?"; then
            auto_install_tool "curl" "curl"
        fi
    fi
    print_box "Weather" "$info"
}

show_current_time() {
    local info=""
    info+="Date: $(date '+%A, %B %d, %Y')\n"
    info+="Time: $(date '+%H:%M:%S')\n"
    info+="Timezone: $(date '+%Z')\n"
    info+="UTC: $(date -u '+%H:%M:%S')"
    print_box "Current Time" "$info"
}

show_calendar() {
    local info=""
    info+="$(cal -3 2>/dev/null || ncal -3 2>/dev/null || cal 2>/dev/null || echo "Calendar not available")"
    print_box "Calendar" "$info"
}

# ========================================
# Backup Menu
# ========================================
backup_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_BACKUP} Backup" \
            "Backup folder" \
            "Restore backup" \
            "Compress backup" \
            "Verify backup" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) backup_folder ;;
            2) restore_backup ;;
            3) compress_backup ;;
            4) verify_backup ;;
            0|5) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

backup_folder() {
    read -p "Folder to backup: " source_dir
    if [[ -d "$source_dir" ]]; then
        read -p "Backup name [backup_$(date +%Y%m%d)]: " backup_name
        backup_name="${backup_name:-backup_$(date +%Y%m%d)}"
        local backup_dir="${HOME}/backups"
        mkdir -p "$backup_dir"
        if command -v tar &>/dev/null; then
            show_info "Creating backup..."
            tar -czf "${backup_dir}/${backup_name}.tar.gz" -C "$(dirname "$source_dir")" "$(basename "$source_dir")"
            show_success "Backup created: ${backup_dir}/${backup_name}.tar.gz"
        else
            cp -r "$source_dir" "${backup_dir}/${backup_name}"
            show_success "Backup created: ${backup_dir}/${backup_name}"
        fi
    else
        show_error "Source directory not found"
    fi
}

restore_backup() {
    local backup_dir="${HOME}/backups"
    if [[ ! -d "$backup_dir" ]]; then
        show_error "No backups found"
        return
    fi
    clear
    print_header
    local backups=$(ls -1 "$backup_dir" 2>/dev/null)
    print_menu "Available Backups" "$backups"
    read -p "Enter backup name to restore: " backup_file
    if [[ -f "${backup_dir}/${backup_file}" ]]; then
        read -p "Restore to: " restore_dir
        mkdir -p "$restore_dir"
        if [[ "$backup_file" == *.tar.gz ]]; then
            tar -xzf "${backup_dir}/${backup_file}" -C "$restore_dir"
        else
            cp -r "${backup_dir}/${backup_file}"/* "$restore_dir"
        fi
        show_success "Restored to $restore_dir"
    else
        show_error "Backup not found"
    fi
}

compress_backup() {
    local backup_dir="${HOME}/backups"
    if [[ ! -d "$backup_dir" ]]; then
        show_error "No backups found"
        return
    fi
    local backups=$(ls -1 "$backup_dir" 2>/dev/null | grep -v ".tar.gz$")
    if [[ -z "$backups" ]]; then
        show_warning "No uncompressed backups found"
        return
    fi
    clear
    print_header
    print_menu "Uncompressed Backups" "$backups"
    read -p "Enter backup directory to compress: " backup_to_compress
    if [[ -d "${backup_dir}/${backup_to_compress}" ]]; then
        if command -v tar &>/dev/null; then
            tar -czf "${backup_dir}/${backup_to_compress}.tar.gz" -C "$backup_dir" "$backup_to_compress"
            rm -rf "${backup_dir}/${backup_to_compress}"
            show_success "Compressed: ${backup_to_compress}.tar.gz"
        else
            show_error "tar not installed"
        fi
    else
        show_error "Backup not found"
    fi
}

verify_backup() {
    local backup_dir="${HOME}/backups"
    if [[ ! -d "$backup_dir" ]]; then
        show_error "No backups found"
        return
    fi
    clear
    print_header
    local backups=$(ls -1 "$backup_dir" 2>/dev/null)
    print_menu "Available Backups" "$backups"
    read -p "Enter backup to verify: " backup_to_verify
    if [[ -f "${backup_dir}/${backup_to_verify}" ]]; then
        if [[ "$backup_to_verify" == *.tar.gz ]]; then
            if tar -tzf "${backup_dir}/${backup_to_verify}" &>/dev/null; then
                show_success "Backup is valid"
            else
                show_error "Backup is corrupted"
            fi
        else
            show_info "Backup verification not supported for this format"
        fi
    else
        show_error "Backup not found"
    fi
}

# ========================================
# Settings Menu
# ========================================
settings_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_SETTINGS} Settings" \
            "Theme (Dark/Light)" \
            "Toggle Colors" \
            "Toggle Animations" \
            "Toggle Emojis" \
            "Reset Configuration" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) toggle_theme ;;
            2) toggle_colors ;;
            3) toggle_animations ;;
            4) toggle_emojis ;;
            5) reset_config ;;
            0|6) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

toggle_theme() { show_info "Theme switching requires terminal emulator support"; }
toggle_colors() {
    if [[ "$COLORS_SUPPORTED" == true ]]; then
        COLORS_SUPPORTED=false
        show_warning "Colors disabled"
    else
        COLORS_SUPPORTED=true
        show_success "Colors enabled"
    fi
}
toggle_animations() {
    if [[ "$USE_ANIMATIONS" == true ]]; then
        USE_ANIMATIONS=false
        show_warning "Animations disabled"
    else
        USE_ANIMATIONS=true
        show_success "Animations enabled"
    fi
}
toggle_emojis() {
    if [[ "$USE_EMOJIS" == true ]]; then
        USE_EMOJIS=false
        show_warning "Emojis disabled"
    else
        USE_EMOJIS=true
        show_success "Emojis enabled"
    fi
}
reset_config() {
    if confirm_action "Reset all configuration?"; then
        rm -rf "$CONFIG_DIR" 2>/dev/null
        mkdir -p "$CONFIG_DIR"
        show_success "Configuration reset"
        FIRST_RUN_FILE=""
        first_run_wizard
    fi
}

# ========================================
# Help Menu
# ========================================
help_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_HELP} Help" \
            "About" \
            "Documentation" \
            "Supported systems" \
            "Version" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) show_about ;;
            2) show_documentation ;;
            3) show_supported_systems ;;
            4) show_version ;;
            0|5) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

show_about() {
    local about=""
    about+="SysKit - Universal Unix Toolkit\n"
    about+="Version: $VERSION\n"
    about+="Author: $AUTHOR\n"
    about+="\nSysKit is a comprehensive system maintenance,\n"
    about+="diagnostics, and utility toolkit for Unix-like\n"
    about+="operating systems. It provides a unified\n"
    about+="interface for system management across\n"
    about+="Linux, macOS, Termux, FreeBSD, OpenBSD,\n"
    about+="and NetBSD."
    print_box "About SysKit" "$about"
}

show_documentation() {
    local docs=""
    docs+="${BOLD}Documentation:${RESET}\n"
    docs+="\n1. System: View system information\n"
    docs+="2. Monitoring: Monitor system resources\n"
    docs+="3. Network: Network diagnostics tools\n"
    docs+="4. Power: Battery information\n"
    docs+="5. Packages: Package management\n"
    docs+="6. Cleaner: System cleaning\n"
    docs+="7. Storage: Storage analysis\n"
    docs+="8. Security: Security audit\n"
    docs+="9. Files: File operations\n"
    docs+="10. Archive: Archive management\n"
    docs+="11. Utilities: Password/hash generation\n"
    docs+="12. Internet: Weather/time/calendar\n"
    docs+="13. Backup: Backup and restore\n"
    docs+="14. Settings: Configuration\n"
    docs+="\nFor more help, visit:\n"
    docs+="https://github.com/AnshLabs716/syskit"
    print_box "Documentation" "$docs"
}

show_supported_systems() {
    local systems=""
    systems+="${BOLD}Supported Operating Systems:${RESET}\n"
    systems+="\n🐧 Linux - All major distributions\n"
    systems+="  - Debian/Ubuntu (apt)\n"
    systems+="  - Fedora/RHEL (dnf)\n"
    systems+="  - Arch Linux (pacman)\n"
    systems+="  - openSUSE (zypper)\n"
    systems+="  - Void Linux (xbps)\n"
    systems+="  - Alpine Linux (apk)\n"
    systems+="  - NixOS (nix)\n"
    systems+="  - Gentoo (emerge)\n"
    systems+="\n🍎 macOS - Intel and Apple Silicon\n"
    systems+="  - Homebrew package manager\n"
    systems+="\n📱 Termux - Android terminal\n"
    systems+="  - Termux API support\n"
    systems+="\n👹 FreeBSD\n"
    systems+="  - pkg package manager\n"
    systems+="\n🦉 OpenBSD\n"
    systems+="  - pkg_add package manager\n"
    systems+="\n🌐 NetBSD\n"
    systems+="  - pkgin package manager"
    print_box "Supported Systems" "$systems"
}

show_version() {
    print_box "Version" "SysKit version $VERSION"
}

# ========================================
# Main Menu
# ========================================
main_menu() {
    while true; do
        clear
        print_header
        local menu_items=(
            "${EMOJI_SYSTEM} System"
            "${EMOJI_MONITOR} Monitoring"
            "${EMOJI_NETWORK} Network"
            "${EMOJI_POWER} Power"
            "${EMOJI_PACKAGE} Package Manager"
            "${EMOJI_CLEANER} Cleaner"
            "${EMOJI_STORAGE} Storage"
            "${EMOJI_SECURITY} Security"
            "${EMOJI_FILES} File Tools"
            "${EMOJI_ARCHIVE} Archive Tools"
            "${EMOJI_UTILITY} Utility Tools"
            "${EMOJI_INTERNET} Internet Tools"
            "${EMOJI_BACKUP} Backup"
            "${EMOJI_SETTINGS} Settings"
            "${EMOJI_HELP} Help"
            "Exit"
        )
        print_menu "SysKit - Main Menu" "${menu_items[@]}"
        echo ""
        read -p "Select option: " main_choice
        case "$main_choice" in
            1) system_menu ;;
            2) monitoring_menu ;;
            3) network_menu ;;
            4) show_battery_info ;;
            5) package_manager_menu ;;
            6) cleaner_menu ;;
            7) storage_menu ;;
            8) security_menu ;;
            9) file_tools_menu ;;
            10) archive_menu ;;
            11) utility_menu ;;
            12) internet_tools_menu ;;
            13) backup_menu ;;
            14) settings_menu ;;
            15) help_menu ;;
            0|"") echo -e "${GREEN}${EMOJI_CHECK} Goodbye!${RESET}"; exit 0 ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
    done
}

# ========================================
# System Menu
# ========================================
system_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_SYSTEM} System" \
            "Fastfetch" \
            "System Information" \
            "Hardware Information" \
            "CPU Information" \
            "GPU Information" \
            "RAM Information" \
            "Motherboard Information" \
            "Disk Information" \
            "Kernel Information" \
            "Uptime" \
            "Environment Information" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) show_fastfetch ;;
            2) show_system_info ;;
            3) show_hardware_info ;;
            4) show_cpu_info ;;
            5) show_gpu_info ;;
            6) show_ram_info ;;
            7) show_motherboard_info ;;
            8) show_disk_info ;;
            9) show_kernel_info ;;
            10) show_uptime ;;
            11) show_environment_info ;;
            0|12) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

# ========================================
# Monitoring Menu
# ========================================
monitoring_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_MONITOR} Monitoring" \
            "CPU Usage" \
            "RAM Usage" \
            "Disk Usage" \
            "Network Usage" \
            "Running Processes" \
            "Top Processes" \
            "Resource Monitor" \
            "Temperature (if supported)" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) show_cpu_usage ;;
            2) show_ram_usage ;;
            3) show_disk_usage ;;
            4) show_network_usage ;;
            5) show_running_processes ;;
            6) show_top_processes ;;
            7) show_resource_monitor ;;
            8) show_temperature ;;
            0|9) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

# ========================================
# Network Menu
# ========================================
network_menu() {
    while true; do
        clear
        print_header
        print_menu "${EMOJI_NETWORK} Network" \
            "Ping Test" \
            "Internet Test" \
            "DNS Test" \
            "Public IP" \
            "Local IP" \
            "Gateway" \
            "Wi-Fi Information" \
            "Back to main menu"
        read -p "Select option: " choice
        case "$choice" in
            1) read -p "Host to ping [google.com]: " host; host="${host:-google.com}"; ping_test "$host" ;;
            2) internet_test ;;
            3) dns_test ;;
            4) show_public_ip ;;
            5) show_local_ip ;;
            6) show_gateway ;;
            7) show_wifi_info ;;
            0|8) break ;;
            *) show_error "Invalid option"; sleep 1 ;;
        esac
        read -p "Press Enter to continue"
    done
}

# ========================================
# Initialization
# ========================================
initialize() {
    detect_os
    detect_distro
    detect_package_manager
    first_run_wizard
    show_success "SysKit initialized on $OS ($DISTRO $DISTRO_VERSION)"
    show_info "Package Manager: $PKG_MANAGER"
    if [[ "$IS_ROOT" == true ]]; then
        show_warning "Running as root - be careful with destructive operations"
    fi
    echo ""
}

main() {
    initialize
    main_menu
}

trap 'echo -e "\n${YELLOW}${EMOJI_WARNING} Interrupted${RESET}"; exit 1' INT

if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi
