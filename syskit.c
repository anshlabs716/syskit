#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/utsname.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/ioctl.h>
#include <dirent.h>
#include <time.h>
#include <errno.h>
#include <pwd.h>
#include <grp.h>
#include <signal.h>
#include <ctype.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <libgen.h>

// ========================================
// GLOBAL CONFIG
// ========================================
#define VERSION "2.0.1"
#define AUTHOR "AnshLabs716"
#define SCRIPT_NAME "syskit.c"
#define CONFIG_DIR ".config/syskit"
#define CONFIG_FILE "config.conf"
#define FIRST_RUN_FILE "first_run_complete"
#define LOG_FILE "syskit.log"

int IS_ROOT = 0;
int COLORS_SUPPORTED = 1;
int USE_EMOJIS = 1;
int USE_ANIMATIONS = 1;
int IS_FULLSCREEN = 0;
int TERMINAL_WIDTH = 80;
int TERMINAL_HEIGHT = 24;
char OS[32] = "Unknown";
char DISTRO[64] = "Unknown";
char DISTRO_VERSION[32] = "Unknown";
char PKG_MANAGER[32] = "unknown";
char PKG_UPDATE[256] = "echo 'Unknown'";
char PKG_UPGRADE[256] = "echo 'Unknown'";
char PKG_INSTALL[256] = "echo 'Unknown'";
char PKG_REMOVE[256] = "echo 'Unknown'";
char PKG_SEARCH[256] = "echo 'Unknown'";
char PKG_LIST[256] = "echo 'Unknown'";
char PKG_CLEAN[256] = "echo 'Unknown'";
char PKG_AUTOREMOVE[256] = "echo 'Unknown'";
char PKG_INSTALL_CMD[256] = "echo 'Unknown'";
char ROOT_INDICATOR[64] = "User";
char HOME_DIR[512];
char USER_NAME[64];
char CONFIG_PATH[512];
char LOG_PATH[512];

// ========================================
// COLORS
// ========================================
#define RED     "\033[0;31m"
#define GREEN   "\033[0;32m"
#define YELLOW  "\033[0;33m"
#define BLUE    "\033[0;34m"
#define MAGENTA "\033[0;35m"
#define CYAN    "\033[0;36m"
#define WHITE   "\033[0;37m"
#define BOLD    "\033[1m"
#define DIM     "\033[2m"
#define RESET   "\033[0m"
#define BG_RED  "\033[41m"
#define BG_GREEN "\033[42m"
#define BG_YELLOW "\033[43m"
#define BG_BLUE "\033[44m"
#define BG_CYAN "\033[46m"
#define BG_WHITE "\033[47m"

// ========================================
// EMOJIS
// ========================================
#define EMOJI_SYSTEM    "🖥️"
#define EMOJI_MONITOR   "📊"
#define EMOJI_NETWORK   "🌐"
#define EMOJI_POWER     "🔋"
#define EMOJI_PACKAGE   "📦"
#define EMOJI_CLEANER   "🧹"
#define EMOJI_STORAGE   "💾"
#define EMOJI_SECURITY  "🔐"
#define EMOJI_FILES     "📂"
#define EMOJI_ARCHIVE   "🗜️"
#define EMOJI_UTILITY   "🔑"
#define EMOJI_INTERNET  "🌤️"
#define EMOJI_BACKUP    "💾"
#define EMOJI_SETTINGS  "⚙️"
#define EMOJI_HELP      "❓"
#define EMOJI_CHECK     "✅"
#define EMOJI_WARNING   "⚠️"
#define EMOJI_ERROR     "❌"
#define EMOJI_INFO      "ℹ️"
#define EMOJI_STAR      "⭐"
#define EMOJI_ROCKET    "🚀"
#define EMOJI_GEAR      "⚙️"
#define EMOJI_SHIELD    "🛡️"
#define EMOJI_LOCK      "🔒"
#define EMOJI_HOURGLASS "⏳"

// ========================================
// FUNCTION PROTOTYPES
// ========================================
void detect_os();
void detect_distro();
void detect_package_manager();
void update_terminal_size();
void print_header();
void strip_ansi(char *str);
void print_box(char *title, char *content);
void print_menu(char *title, char **items, int count);
void show_success(char *msg);
void show_warning(char *msg);
void show_error(char *msg);
void show_info(char *msg);
int confirm_action(char *prompt);
int check_command(char *cmd);
void run_with_sudo(char *cmd);
void auto_install_tool(char *tool, char *package);
void first_run_wizard();
void show_fastfetch();
void show_system_info();
void show_hardware_info();
void show_cpu_info();
void show_gpu_info();
void show_ram_info();
void show_motherboard_info();
void show_disk_info();
void show_kernel_info();
void show_uptime();
void show_environment_info();
void show_cpu_usage();
void show_ram_usage();
void show_disk_usage();
void show_network_usage();
void show_running_processes();
void show_top_processes();
void show_resource_monitor();
void show_temperature();
void ping_test(char *target);
void internet_test();
void dns_test();
void show_public_ip();
void show_local_ip();
void show_gateway();
void show_wifi_info();
void show_battery_info();
void package_manager_menu();
void cleaner_menu();
void clean_user_cache();
void clean_package_cache();
void clean_temp_files();
void clean_old_logs();
void clean_trash();
void clean_homebrew();
void clean_timeshift();
void clean_snapper();
void storage_menu();
void largest_directories(char *dir);
void largest_files(char *dir);
void show_mounted_drives();
void show_smart_status();
void security_menu();
void show_firewall_status();
void show_open_ports();
void show_ssh_status();
void show_running_services();
void show_security_recommendations();
void file_tools_menu();
void find_files();
void find_duplicates();
void search_text();
void show_directory_tree();
void show_file_stats();
void archive_menu();
void extract_zip();
void extract_tar();
void create_zip();
void create_targz();
void utility_menu();
void generate_password();
void generate_hash();
void generate_uuid();
void generate_random_string();
void internet_tools_menu();
void show_weather();
void show_current_time();
void show_calendar();
void backup_menu();
void backup_folder();
void restore_backup();
void compress_backup();
void verify_backup();
void settings_menu();
void toggle_theme();
void toggle_colors();
void toggle_animations();
void toggle_emojis();
void reset_config();
void help_menu();
void show_about();
void show_documentation();
void show_supported_systems();
void show_version();
void system_menu();
void monitoring_menu();
void network_menu();
void main_menu();
void initialize();
void handle_signal(int sig);

// ========================================
// UTILITY FUNCTIONS
// ========================================
void update_terminal_size() {
    struct winsize w;
    if (ioctl(STDOUT_FILENO, TIOCGWINSZ, &w) == 0) {
        TERMINAL_WIDTH = w.ws_col;
        TERMINAL_HEIGHT = w.ws_row;
    } else {
        TERMINAL_WIDTH = 80;
        TERMINAL_HEIGHT = 24;
    }
}

void strip_ansi(char *str) {
    char *src = str;
    char *dst = str;
    int in_escape = 0;
    while (*src) {
        if (*src == '\033') {
            in_escape = 1;
            src++;
            continue;
        }
        if (in_escape) {
            if (*src == 'm') {
                in_escape = 0;
                src++;
                continue;
            }
            src++;
            continue;
        }
        *dst++ = *src++;
    }
    *dst = '\0';
}

void print_header() {
    system("clear");
    update_terminal_size();
    printf("%s%s\n", BOLD, CYAN);
    printf("  _____     _    _  _____ _  __ \n");
    printf(" / ____|   | |  | |/ ____| |/ / \n");
    printf("| (___   __| |  | | (___ | ' /  \n");
    printf(" \\___ \\ / _` |  | |\\___ \\|  <   \n");
    printf(" ____) | (_| |__| |____) | . \\  \n");
    printf("|_____/ \\__,_\\____/|_____/|_|\\_\\ \n");
    printf("%s\n", RESET);
    printf("%sby AnshLabs716%s ", BOLD, RESET);
    if (IS_ROOT) {
        printf("%s%s ROOT %s", BG_RED, WHITE, RESET);
    } else {
        printf("%sUser%s", DIM, RESET);
    }
    printf("\n%sVersion: %s | %s %s%s\n", DIM, VERSION, OS, DISTRO, RESET);
    if (IS_FULLSCREEN) {
        printf("%s└─ Fullscreen mode detected %s\n", DIM, RESET);
    }
    printf("\n");
}

void print_box(char *title, char *content) {
    update_terminal_size();
    int max_width = TERMINAL_WIDTH - 4;
    if (max_width > 80) max_width = 80;
    if (max_width < 40) max_width = 40;

    char processed[32768] = "";
    char *line = strtok(content, "\n");
    while (line != NULL) {
        char clean_line[4096];
        strcpy(clean_line, line);
        strip_ansi(clean_line);
        int line_len = strlen(clean_line);

        if (line_len <= max_width) {
            strcat(processed, line);
            strcat(processed, "\n");
        } else {
            char temp[4096];
            strcpy(temp, line);
            while (strlen(temp) > max_width) {
                char wrap_line[4096];
                strncpy(wrap_line, temp, max_width);
                wrap_line[max_width] = '\0';
                strcat(processed, wrap_line);
                strcat(processed, "\n");
                memmove(temp, temp + max_width, strlen(temp) - max_width + 1);
            }
            if (strlen(temp) > 0) {
                strcat(processed, temp);
                strcat(processed, "\n");
            }
        }
        line = strtok(NULL, "\n");
    }

    int len = strlen(processed);
    if (len > 0 && processed[len-1] == '\n') {
        processed[len-1] = '\0';
    }

    // Top border
    printf("%s┌", BOLD);
    for (int i = 0; i < max_width; i++) printf("─");
    printf("┐%s\n", RESET);

    // Title
    if (title != NULL && strlen(title) > 0) {
        char clean_title[4096];
        strcpy(clean_title, title);
        strip_ansi(clean_title);
        int title_len = strlen(clean_title);

        printf("%s│%s ", BOLD, RESET);
        printf("%s%s%s", BOLD, WHITE, title);
        int pad = max_width - title_len - 1;
        if (pad < 0) pad = 0;
        for (int i = 0; i < pad; i++) printf(" ");
        printf("%s│%s\n", BOLD, RESET);

        printf("%s├", BOLD);
        for (int i = 0; i < max_width; i++) printf("─");
        printf("┤%s\n", RESET);
    }

    // Content
    line = strtok(processed, "\n");
    while (line != NULL) {
        char clean_line[4096];
        strcpy(clean_line, line);
        strip_ansi(clean_line);
        int line_len = strlen(clean_line);

        printf("%s│%s ", BOLD, RESET);
        printf("%s", line);
        int pad = max_width - line_len - 1;
        if (pad < 0) pad = 0;
        for (int i = 0; i < pad; i++) printf(" ");
        printf("%s│%s\n", BOLD, RESET);
        line = strtok(NULL, "\n");
    }

    // Bottom border
    printf("%s└", BOLD);
    for (int i = 0; i < max_width; i++) printf("─");
    printf("┘%s\n", RESET);
}

void print_menu(char *title, char **items, int count) {
    char content[32768] = "";
    for (int i = 0; i < count; i++) {
        char line[1024];
        if (strstr(items[i], "Back to") != NULL || strstr(items[i], "Exit") != NULL ||
            strstr(items[i], "Back") != NULL) {
            snprintf(line, sizeof(line), "%s%s%d.%s %s\n", BOLD, YELLOW, i+1, RESET, items[i]);
            } else {
                snprintf(line, sizeof(line), "%s%s%d.%s %s\n", BOLD, GREEN, i+1, RESET, items[i]);
            }
            strcat(content, line);
    }
    strcat(content, "\n");
    strcat(content, DIM);
    strcat(content, "0. Go back/exit");
    strcat(content, RESET);
    print_box(title, content);
}

void show_success(char *msg) {
    printf("%s%s %s%s\n", GREEN, EMOJI_CHECK, msg, RESET);
}

void show_warning(char *msg) {
    printf("%s%s %s%s\n", YELLOW, EMOJI_WARNING, msg, RESET);
}

void show_error(char *msg) {
    printf("%s%s %s%s\n", RED, EMOJI_ERROR, msg, RESET);
}

void show_info(char *msg) {
    printf("%s%s %s%s\n", BLUE, EMOJI_INFO, msg, RESET);
}

int confirm_action(char *prompt) {
    char response[10];
    printf("%s%s %s%s\n", YELLOW, EMOJI_WARNING, prompt, RESET);
    printf("Continue? [y/N] ");
    if (fgets(response, sizeof(response), stdin) == NULL) return 0;
    response[strcspn(response, "\n")] = 0;
    if (strcmp(response, "y") == 0 || strcmp(response, "Y") == 0 ||
        strcmp(response, "yes") == 0 || strcmp(response, "Yes") == 0) {
        return 1;
        }
        return 0;
}

int check_command(char *cmd) {
    char path[512];
    snprintf(path, sizeof(path), "/bin/%s", cmd);
    if (access(path, F_OK) == 0) return 1;
    snprintf(path, sizeof(path), "/usr/bin/%s", cmd);
    if (access(path, F_OK) == 0) return 1;
    snprintf(path, sizeof(path), "/usr/local/bin/%s", cmd);
    if (access(path, F_OK) == 0) return 1;
    char *path_env = getenv("PATH");
    if (path_env != NULL) {
        char path_copy[4096];
        strncpy(path_copy, path_env, sizeof(path_copy) - 1);
        char *token = strtok(path_copy, ":");
        while (token != NULL) {
            snprintf(path, sizeof(path), "%s/%s", token, cmd);
            if (access(path, F_OK) == 0) return 1;
            token = strtok(NULL, ":");
        }
    }
    return 0;
}

void run_with_sudo(char *cmd) {
    if (IS_ROOT) {
        system(cmd);
    } else {
        if (check_command("sudo")) {
            show_warning("This operation requires root privileges");
            if (confirm_action("Continue with sudo?")) {
                char sudo_cmd[1024];
                snprintf(sudo_cmd, sizeof(sudo_cmd), "sudo bash -c \"%s\"", cmd);
                system(sudo_cmd);
            }
        } else {
            show_error("sudo not installed. Please run as root.");
            system(cmd);
        }
    }
}

void auto_install_tool(char *tool, char *package) {
    if (check_command(tool)) return;

    show_warning("Tool not installed");
    char prompt[256];
    snprintf(prompt, sizeof(prompt), "Install %s (package: %s)?", tool, package);
    if (!confirm_action(prompt)) {
        show_warning("Skipping installation");
        return;
    }

    show_info("Installing package...");
    char cmd[512];
    if (strcmp(PKG_MANAGER, "apt") == 0 || strcmp(PKG_MANAGER, "pkg") == 0) {
        snprintf(cmd, sizeof(cmd), "apt install -y %s", package);
        run_with_sudo(cmd);
    } else if (strcmp(PKG_MANAGER, "dnf") == 0) {
        snprintf(cmd, sizeof(cmd), "dnf install -y %s", package);
        run_with_sudo(cmd);
    } else if (strcmp(PKG_MANAGER, "pacman") == 0) {
        snprintf(cmd, sizeof(cmd), "pacman -S --noconfirm %s", package);
        run_with_sudo(cmd);
    } else if (strcmp(PKG_MANAGER, "zypper") == 0) {
        snprintf(cmd, sizeof(cmd), "zypper install -y %s", package);
        run_with_sudo(cmd);
    } else if (strcmp(PKG_MANAGER, "brew") == 0) {
        snprintf(cmd, sizeof(cmd), "brew install %s", package);
        system(cmd);
    } else {
        show_error("Cannot install package: Unsupported package manager");
        return;
    }

    if (check_command(tool)) {
        show_success("Tool installed successfully");
    } else {
        show_error("Failed to install tool");
    }
}

// ========================================
// OS DETECTION
// ========================================
void detect_os() {
    struct utsname uname_data;
    if (uname(&uname_data) == 0) {
        if (strstr(uname_data.sysname, "Linux") != NULL) {
            if (access("/data/data/com.termux", F_OK) == 0) {
                strcpy(OS, "Termux");
            } else {
                strcpy(OS, "Linux");
            }
        } else if (strstr(uname_data.sysname, "Darwin") != NULL) {
            strcpy(OS, "macOS");
        } else if (strstr(uname_data.sysname, "FreeBSD") != NULL) {
            strcpy(OS, "FreeBSD");
        } else if (strstr(uname_data.sysname, "OpenBSD") != NULL) {
            strcpy(OS, "OpenBSD");
        } else if (strstr(uname_data.sysname, "NetBSD") != NULL) {
            strcpy(OS, "NetBSD");
        } else {
            strcpy(OS, "Unknown");
        }
    }
}

void detect_distro() {
    if (strcmp(OS, "Linux") == 0) {
        FILE *fp = fopen("/etc/os-release", "r");
        if (fp) {
            char line[256];
            while (fgets(line, sizeof(line), fp)) {
                if (strncmp(line, "ID=", 3) == 0) {
                    char *value = strchr(line, '=') + 1;
                    value[strcspn(value, "\"\n")] = 0;
                    strcpy(DISTRO, value);
                } else if (strncmp(line, "VERSION_ID=", 11) == 0) {
                    char *value = strchr(line, '=') + 1;
                    value[strcspn(value, "\"\n")] = 0;
                    strcpy(DISTRO_VERSION, value);
                }
            }
            fclose(fp);
        } else {
            strcpy(DISTRO, "unknown");
            strcpy(DISTRO_VERSION, "unknown");
        }
    } else if (strcmp(OS, "macOS") == 0) {
        strcpy(DISTRO, "macOS");
        FILE *fp = popen("sw_vers -productVersion 2>/dev/null", "r");
        if (fp) {
            fgets(DISTRO_VERSION, sizeof(DISTRO_VERSION), fp);
            pclose(fp);
            DISTRO_VERSION[strcspn(DISTRO_VERSION, "\n")] = 0;
        } else {
            strcpy(DISTRO_VERSION, "unknown");
        }
    } else {
        strcpy(DISTRO, OS);
        strcpy(DISTRO_VERSION, "unknown");
    }
}

void detect_package_manager() {
    if (strcmp(OS, "Linux") == 0) {
        if (strcmp(DISTRO, "ubuntu") == 0 || strcmp(DISTRO, "debian") == 0 ||
            strcmp(DISTRO, "linuxmint") == 0 || strcmp(DISTRO, "pop") == 0) {
            strcpy(PKG_MANAGER, "apt");
        strcpy(PKG_UPDATE, "apt update");
        strcpy(PKG_UPGRADE, "apt upgrade -y");
        strcpy(PKG_INSTALL, "apt install -y");
        strcpy(PKG_REMOVE, "apt remove -y");
        strcpy(PKG_SEARCH, "apt search");
        strcpy(PKG_LIST, "apt list --installed");
        strcpy(PKG_CLEAN, "apt autoclean -y");
        strcpy(PKG_AUTOREMOVE, "apt autoremove -y");
        strcpy(PKG_INSTALL_CMD, "apt install -y");
            } else if (strcmp(DISTRO, "fedora") == 0 || strcmp(DISTRO, "rhel") == 0 ||
                strcmp(DISTRO, "centos") == 0 || strcmp(DISTRO, "rocky") == 0 ||
                strcmp(DISTRO, "almalinux") == 0) {
                if (check_command("dnf")) {
                    strcpy(PKG_MANAGER, "dnf");
                    strcpy(PKG_UPDATE, "dnf check-update");
                    strcpy(PKG_UPGRADE, "dnf upgrade -y");
                    strcpy(PKG_INSTALL, "dnf install -y");
                    strcpy(PKG_REMOVE, "dnf remove -y");
                    strcpy(PKG_SEARCH, "dnf search");
                    strcpy(PKG_LIST, "dnf list installed");
                    strcpy(PKG_CLEAN, "dnf clean all");
                    strcpy(PKG_AUTOREMOVE, "dnf autoremove -y");
                    strcpy(PKG_INSTALL_CMD, "dnf install -y");
                } else {
                    strcpy(PKG_MANAGER, "yum");
                    strcpy(PKG_UPDATE, "yum check-update");
                    strcpy(PKG_UPGRADE, "yum upgrade -y");
                    strcpy(PKG_INSTALL, "yum install -y");
                    strcpy(PKG_REMOVE, "yum remove -y");
                    strcpy(PKG_SEARCH, "yum search");
                    strcpy(PKG_LIST, "yum list installed");
                    strcpy(PKG_CLEAN, "yum clean all");
                    strcpy(PKG_AUTOREMOVE, "yum autoremove -y");
                    strcpy(PKG_INSTALL_CMD, "yum install -y");
                }
                } else if (strcmp(DISTRO, "arch") == 0 || strcmp(DISTRO, "manjaro") == 0 ||
                    strcmp(DISTRO, "endeavouros") == 0 || strcmp(DISTRO, "artix") == 0) {
                    strcpy(PKG_MANAGER, "pacman");
                strcpy(PKG_UPDATE, "pacman -Sy");
                strcpy(PKG_UPGRADE, "pacman -Su --noconfirm");
                strcpy(PKG_INSTALL, "pacman -S --noconfirm");
                strcpy(PKG_REMOVE, "pacman -R --noconfirm");
                strcpy(PKG_SEARCH, "pacman -Ss");
                strcpy(PKG_LIST, "pacman -Q");
                strcpy(PKG_CLEAN, "pacman -Sc --noconfirm");
                strcpy(PKG_AUTOREMOVE, "pacman -Rns --noconfirm");
                strcpy(PKG_INSTALL_CMD, "pacman -S --noconfirm");
                    } else if (strcmp(DISTRO, "opensuse") == 0 || strcmp(DISTRO, "suse") == 0 ||
                        strcmp(DISTRO, "sles") == 0) {
                        strcpy(PKG_MANAGER, "zypper");
                    strcpy(PKG_UPDATE, "zypper refresh");
                    strcpy(PKG_UPGRADE, "zypper update -y");
                    strcpy(PKG_INSTALL, "zypper install -y");
                    strcpy(PKG_REMOVE, "zypper remove -y");
                    strcpy(PKG_SEARCH, "zypper search");
                    strcpy(PKG_LIST, "zypper se --installed-only");
                    strcpy(PKG_CLEAN, "zypper clean");
                    strcpy(PKG_AUTOREMOVE, "zypper rm -u");
                    strcpy(PKG_INSTALL_CMD, "zypper install -y");
                        } else {
                            strcpy(PKG_MANAGER, "unknown");
                            strcpy(PKG_UPDATE, "echo 'Unknown package manager'");
                            strcpy(PKG_UPGRADE, "echo 'Unknown package manager'");
                            strcpy(PKG_INSTALL, "echo 'Unknown package manager'");
                            strcpy(PKG_REMOVE, "echo 'Unknown package manager'");
                            strcpy(PKG_SEARCH, "echo 'Unknown package manager'");
                            strcpy(PKG_LIST, "echo 'Unknown package manager'");
                            strcpy(PKG_CLEAN, "echo 'Unknown package manager'");
                            strcpy(PKG_AUTOREMOVE, "echo 'Unknown package manager'");
                            strcpy(PKG_INSTALL_CMD, "echo 'Unknown package manager'");
                        }
    } else if (strcmp(OS, "macOS") == 0) {
        if (check_command("brew")) {
            strcpy(PKG_MANAGER, "brew");
            strcpy(PKG_UPDATE, "brew update");
            strcpy(PKG_UPGRADE, "brew upgrade");
            strcpy(PKG_INSTALL, "brew install");
            strcpy(PKG_REMOVE, "brew uninstall");
            strcpy(PKG_SEARCH, "brew search");
            strcpy(PKG_LIST, "brew list");
            strcpy(PKG_CLEAN, "brew cleanup");
            strcpy(PKG_AUTOREMOVE, "brew autoremove");
            strcpy(PKG_INSTALL_CMD, "brew install");
        } else {
            strcpy(PKG_MANAGER, "unknown");
            strcpy(PKG_UPDATE, "echo 'Homebrew not installed'");
            strcpy(PKG_UPGRADE, "echo 'Homebrew not installed'");
            strcpy(PKG_INSTALL, "echo 'Homebrew not installed'");
            strcpy(PKG_REMOVE, "echo 'Homebrew not installed'");
            strcpy(PKG_SEARCH, "echo 'Homebrew not installed'");
            strcpy(PKG_LIST, "echo 'Homebrew not installed'");
            strcpy(PKG_CLEAN, "echo 'Homebrew not installed'");
            strcpy(PKG_AUTOREMOVE, "echo 'Homebrew not installed'");
            strcpy(PKG_INSTALL_CMD, "echo 'Homebrew not installed'");
        }
    } else if (strcmp(OS, "Termux") == 0) {
        strcpy(PKG_MANAGER, "pkg");
        strcpy(PKG_UPDATE, "pkg update");
        strcpy(PKG_UPGRADE, "pkg upgrade -y");
        strcpy(PKG_INSTALL, "pkg install -y");
        strcpy(PKG_REMOVE, "pkg uninstall -y");
        strcpy(PKG_SEARCH, "pkg search");
        strcpy(PKG_LIST, "pkg list-installed");
        strcpy(PKG_CLEAN, "pkg clean");
        strcpy(PKG_AUTOREMOVE, "pkg autoclean");
        strcpy(PKG_INSTALL_CMD, "pkg install -y");
    } else if (strcmp(OS, "FreeBSD") == 0) {
        strcpy(PKG_MANAGER, "pkg");
        strcpy(PKG_UPDATE, "pkg update");
        strcpy(PKG_UPGRADE, "pkg upgrade -y");
        strcpy(PKG_INSTALL, "pkg install -y");
        strcpy(PKG_REMOVE, "pkg delete -y");
        strcpy(PKG_SEARCH, "pkg search");
        strcpy(PKG_LIST, "pkg info");
        strcpy(PKG_CLEAN, "pkg clean");
        strcpy(PKG_AUTOREMOVE, "pkg autoremove -y");
        strcpy(PKG_INSTALL_CMD, "pkg install -y");
    } else {
        strcpy(PKG_MANAGER, "unknown");
        strcpy(PKG_UPDATE, "echo 'Unknown OS'");
        strcpy(PKG_UPGRADE, "echo 'Unknown OS'");
        strcpy(PKG_INSTALL, "echo 'Unknown OS'");
        strcpy(PKG_REMOVE, "echo 'Unknown OS'");
        strcpy(PKG_SEARCH, "echo 'Unknown OS'");
        strcpy(PKG_LIST, "echo 'Unknown OS'");
        strcpy(PKG_CLEAN, "echo 'Unknown OS'");
        strcpy(PKG_AUTOREMOVE, "echo 'Unknown OS'");
        strcpy(PKG_INSTALL_CMD, "echo 'Unknown OS'");
    }
}

// ========================================
// FIRST RUN WIZARD
// ========================================
void first_run_wizard() {
    char first_run_path[512];
    snprintf(first_run_path, sizeof(first_run_path), "%s/%s/%s", getenv("HOME"), CONFIG_DIR, FIRST_RUN_FILE);

    if (strcmp(OS, "Termux") == 0) {
        char config_dir[512];
        snprintf(config_dir, sizeof(config_dir), "%s/%s", getenv("HOME"), CONFIG_DIR);
        mkdir(config_dir, 0755);
        FILE *fp = fopen(first_run_path, "w");
        if (fp) fclose(fp);
        return;
    }

    if (access(first_run_path, F_OK) == 0) return;

    show_info("First run detected! Running setup wizard...");
    detect_os();
    detect_distro();
    detect_package_manager();

    int has_fastfetch = check_command("fastfetch");

    char summary[2048];
    snprintf(summary, sizeof(summary),
             "%sSystem Information:%s\n"
             "  Operating System: %s\n"
             "  Distribution: %s\n"
             "  Distribution Version: %s\n"
             "  Package Manager: %s\n"
             "  Terminal Size: %dx%d\n"
             "  Color Support: %s\n"
             "  Root Access: %s\n"
             "  Fullscreen Mode: %s\n"
             "  Fastfetch: %s",
             BOLD, RESET,
             OS, DISTRO, DISTRO_VERSION, PKG_MANAGER,
             TERMINAL_WIDTH, TERMINAL_HEIGHT,
             COLORS_SUPPORTED ? "Yes" : "No",
             IS_ROOT ? "Yes" : "No",
             IS_FULLSCREEN ? "Yes" : "No",
             has_fastfetch ? "Installed" : "Not installed"
    );
    print_box("Setup Wizard", summary);

    char *common_tools[] = {"curl", "wget", "jq", "git", "tar", "unzip", "zip", "tree", NULL};
    char *missing_tools[10];
    int missing_count = 0;

    for (int i = 0; common_tools[i] != NULL; i++) {
        if (!check_command(common_tools[i])) {
            missing_tools[missing_count++] = common_tools[i];
        }
    }

    if (missing_count > 0) {
        show_warning("Missing common tools:");
        for (int i = 0; i < missing_count; i++) {
            printf("  - %s\n", missing_tools[i]);
        }
        if (confirm_action("Install missing tools?")) {
            for (int i = 0; i < missing_count; i++) {
                auto_install_tool(missing_tools[i], missing_tools[i]);
            }
        }
    }

    if (!has_fastfetch) {
        if (confirm_action("Install fastfetch for better system information?")) {
            auto_install_tool("fastfetch", "fastfetch");
        }
    }

    char config_dir[512];
    snprintf(config_dir, sizeof(config_dir), "%s/%s", getenv("HOME"), CONFIG_DIR);
    mkdir(config_dir, 0755);
    FILE *fp = fopen(first_run_path, "w");
    if (fp) fclose(fp);

    show_success("First run setup complete!");
    sleep(2);
}

// ========================================
// SYSTEM INFORMATION FUNCTIONS
// ========================================
void show_fastfetch() {
    if (check_command("fastfetch")) {
        system("fastfetch");
    } else {
        show_warning("Fastfetch is not installed");
        if (confirm_action("Install fastfetch?")) {
            auto_install_tool("fastfetch", "fastfetch");
            if (check_command("fastfetch")) {
                system("fastfetch");
            }
        }
    }
}

void show_system_info() {
    if (!check_command("hostname")) {
        show_info("hostname not found. Installing...");
        auto_install_tool("hostname", "inetutils");
    }

    struct utsname uname_data;
    uname(&uname_data);

    char hostname[256];
    gethostname(hostname, sizeof(hostname));
    char uptime_str[256] = "Unknown";
    FILE *fp = popen("uptime -p 2>/dev/null", "r");
    if (fp) {
        fgets(uptime_str, sizeof(uptime_str), fp);
        pclose(fp);
        uptime_str[strcspn(uptime_str, "\n")] = 0;
    }

    char info[4096];
    snprintf(info, sizeof(info),
             "%sSystem Information:%s\n"
             "  Hostname: %s\n"
             "  OS: %s %s %s\n"
             "  Kernel: %s\n"
             "  Architecture: %s\n"
             "  Shell: %s\n"
             "  User: %s\n"
             "  Root Access: %s\n"
             "  Fullscreen: %s\n"
             "  Uptime: %s",
             BOLD, RESET,
             hostname,
             OS, DISTRO, DISTRO_VERSION,
             uname_data.release,
             uname_data.machine,
             getenv("SHELL"),
             getenv("USER"),
             IS_ROOT ? "Yes" : "No",
             IS_FULLSCREEN ? "Yes" : "No",
             uptime_str
    );
    print_box("System Information", info);
}

void show_hardware_info() {
    if (!check_command("lshw") && !check_command("dmidecode")) {
        show_info("Installing hardware detection tools...");
        auto_install_tool("lshw", "lshw");
    }

    char info[16384] = "";
    if (strcmp(OS, "Linux") == 0) {
        if (check_command("lshw")) {
            FILE *fp = popen("lshw -short 2>/dev/null | head -20", "r");
            if (fp) {
                fread(info, 1, sizeof(info) - 1, fp);
                pclose(fp);
            }
        } else if (check_command("dmidecode")) {
            FILE *fp = popen("dmidecode -t system 2>/dev/null", "r");
            if (fp) {
                fread(info, 1, sizeof(info) - 1, fp);
                pclose(fp);
            }
        } else {
            strcpy(info, "Hardware detection tools not available");
        }
    } else if (strcmp(OS, "macOS") == 0) {
        FILE *fp = popen("system_profiler SPHardwareDataType 2>/dev/null | head -10", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else {
        strcpy(info, "Hardware information not fully supported on this OS");
    }
    print_box("Hardware Information", info);
}

void show_cpu_info() {
    char info[8192] = "";
    if (strcmp(OS, "Linux") == 0) {
        if (access("/proc/cpuinfo", F_OK) == 0) {
            FILE *fp = popen("cat /proc/cpuinfo | grep -E 'model name|cpu cores|siblings|cache size' | head -10", "r");
            if (fp) {
                fread(info, 1, sizeof(info) - 1, fp);
                pclose(fp);
            }
            char usage[64];
            FILE *fp2 = popen("top -bn1 2>/dev/null | grep 'Cpu(s)' | awk '{print $2}'", "r");
            if (fp2) {
                fgets(usage, sizeof(usage), fp2);
                pclose(fp2);
                strcat(info, "\nCPU Usage: ");
                strcat(info, usage);
                strcat(info, "%");
            }
        }
    } else if (strcmp(OS, "macOS") == 0) {
        char cpu_info[256];
        FILE *fp = popen("sysctl -n machdep.cpu.brand_string 2>/dev/null", "r");
        if (fp) {
            fgets(cpu_info, sizeof(cpu_info), fp);
            pclose(fp);
            cpu_info[strcspn(cpu_info, "\n")] = 0;
        }
        char cores[32];
        FILE *fp2 = popen("sysctl -n hw.ncpu 2>/dev/null", "r");
        if (fp2) {
            fgets(cores, sizeof(cores), fp2);
            pclose(fp2);
            cores[strcspn(cores, "\n")] = 0;
        }
        char usage[64];
        FILE *fp3 = popen("top -l1 2>/dev/null | grep 'CPU usage' | awk '{print $3,$4,$5}'", "r");
        if (fp3) {
            fgets(usage, sizeof(usage), fp3);
            pclose(fp3);
            usage[strcspn(usage, "\n")] = 0;
        }
        snprintf(info, sizeof(info), "CPU: %s\nCores: %s\nCPU Usage: %s", cpu_info, cores, usage);
    } else {
        strcpy(info, "CPU information not supported on this OS");
    }
    print_box("CPU Information", info);
}

void show_gpu_info() {
    char info[8192] = "";
    if (strcmp(OS, "Linux") == 0) {
        if (check_command("lspci")) {
            FILE *fp = popen("lspci | grep -E 'VGA|3D|Display' 2>/dev/null", "r");
            if (fp) {
                fread(info, 1, sizeof(info) - 1, fp);
                pclose(fp);
            }
        } else {
            strcpy(info, "Install pciutils or mesa-utils for GPU info");
        }
    } else if (strcmp(OS, "macOS") == 0) {
        FILE *fp = popen("system_profiler SPDisplaysDataType 2>/dev/null | head -20", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else {
        strcpy(info, "GPU information not supported on this OS");
    }
    print_box("GPU Information", info);
}

void show_ram_info() {
    char info[2048] = "";
    if (strcmp(OS, "Linux") == 0) {
        FILE *fp = fopen("/proc/meminfo", "r");
        if (fp) {
            char line[256];
            long total = 0, free_mem = 0, available = 0;
            while (fgets(line, sizeof(line), fp)) {
                if (strncmp(line, "MemTotal:", 9) == 0) {
                    sscanf(line, "MemTotal: %ld", &total);
                } else if (strncmp(line, "MemFree:", 8) == 0) {
                    sscanf(line, "MemFree: %ld", &free_mem);
                } else if (strncmp(line, "MemAvailable:", 13) == 0) {
                    sscanf(line, "MemAvailable: %ld", &available);
                }
            }
            fclose(fp);

            long used = total - available;
            int used_percent = (int)((float)used / total * 100);

            snprintf(info, sizeof(info),
                     "Total: %.2f GB\n"
                     "Used: %.2f GB (%d%%)\n"
                     "Free: %.2f GB\n"
                     "Available: %.2f GB",
                     (float)total / 1024 / 1024,
                     (float)used / 1024 / 1024,
                     used_percent,
                     (float)free_mem / 1024 / 1024,
                     (float)available / 1024 / 1024
            );
        } else {
            strcpy(info, "RAM information not available");
        }
    } else if (strcmp(OS, "macOS") == 0) {
        char total[64];
        FILE *fp = popen("sysctl -n hw.memsize 2>/dev/null | awk '{print $1/1024/1024/1024 \" GB\"}'", "r");
        if (fp) {
            fgets(total, sizeof(total), fp);
            pclose(fp);
            total[strcspn(total, "\n")] = 0;
            snprintf(info, sizeof(info), "Total: %s", total);
        }
    } else {
        strcpy(info, "RAM information not supported on this OS");
    }
    print_box("RAM Information", info);
}

void show_motherboard_info() {
    if (!check_command("dmidecode")) {
        show_info("Installing dmidecode for motherboard info...");
        auto_install_tool("dmidecode", "dmidecode");
    }

    char info[4096] = "";
    if (strcmp(OS, "Linux") == 0) {
        if (check_command("dmidecode")) {
            FILE *fp = popen("dmidecode -t baseboard 2>/dev/null | head -10", "r");
            if (fp) {
                fread(info, 1, sizeof(info) - 1, fp);
                pclose(fp);
            }
        } else {
            strcpy(info, "dmidecode not available");
        }
    } else {
        strcpy(info, "Motherboard information only available on Linux with dmidecode");
    }
    print_box("Motherboard Information", info);
}

void show_disk_info() {
    char info[8192] = "";
    char cmd[256];
    if (strcmp(OS, "Linux") == 0 || strcmp(OS, "Termux") == 0) {
        strcpy(cmd, "df -h | grep -E '^/dev|Filesystem' | column -t 2>/dev/null || df -h");
    } else {
        strcpy(cmd, "df -h | column -t 2>/dev/null || df -h");
    }

    FILE *fp = popen(cmd, "r");
    if (fp) {
        fread(info, 1, sizeof(info) - 1, fp);
        pclose(fp);
    }
    print_box("Disk Information", info);
}

void show_kernel_info() {
    struct utsname uname_data;
    uname(&uname_data);

    char info[1024];
    snprintf(info, sizeof(info),
             "Kernel: %s\n"
             "Kernel Version: %s\n"
             "Architecture: %s\n"
             "Operating System: %s",
             uname_data.release,
             uname_data.version,
             uname_data.machine,
             uname_data.sysname
    );
    print_box("Kernel Information", info);
}

void show_uptime() {
    char info[256];
    char uptime_str[128];
    char load_str[128];

    FILE *fp = popen("uptime -p 2>/dev/null", "r");
    if (fp) {
        fgets(uptime_str, sizeof(uptime_str), fp);
        pclose(fp);
        uptime_str[strcspn(uptime_str, "\n")] = 0;
    } else {
        strcpy(uptime_str, "Unknown");
    }

    fp = popen("uptime | awk -F'load average:' '{print $2}' | xargs", "r");
    if (fp) {
        fgets(load_str, sizeof(load_str), fp);
        pclose(fp);
        load_str[strcspn(load_str, "\n")] = 0;
    } else {
        strcpy(load_str, "Unknown");
    }

    snprintf(info, sizeof(info),
             "Uptime: %s\nLoad: %s",
             uptime_str, load_str
    );
    print_box("System Uptime", info);
}

void show_environment_info() {
    char info[2048];
    snprintf(info, sizeof(info),
             "Shell: %s\n"
             "Terminal: %s\n"
             "Terminal Size: %dx%d\n"
             "Color Support: %s\n"
             "Locale: %s",
             getenv("SHELL"),
             getenv("TERM"),
             TERMINAL_WIDTH, TERMINAL_HEIGHT,
             COLORS_SUPPORTED ? "Yes" : "No",
             getenv("LANG")
    );
    print_box("Environment Information", info);
}

// ========================================
// MONITORING FUNCTIONS
// ========================================
void show_cpu_usage() {
    char info[8192] = "";
    if (check_command("top")) {
        FILE *fp = popen("top -bn1 2>/dev/null | head -15", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    }
    print_box("CPU Usage", info);
}

void show_ram_usage() {
    char info[8192] = "";
    if (check_command("free")) {
        FILE *fp = popen("free -h", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else {
        strcpy(info, "RAM monitoring not supported");
    }
    print_box("RAM Usage", info);
}

void show_disk_usage() {
    char info[8192] = "";
    if (check_command("df")) {
        FILE *fp = popen("df -h | grep -v tmpfs | column -t 2>/dev/null || df -h | grep -v tmpfs", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    }
    print_box("Disk Usage", info);
}

void show_network_usage() {
    if (!check_command("iftop")) {
        show_info("iftop not installed. Installing...");
        auto_install_tool("iftop", "iftop");
    }

    char info[4096] = "";
    if (check_command("iftop")) {
        FILE *fp = popen("netstat -i 2>/dev/null | head -10", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
        strcat(info, "\nRun 'sudo iftop' for real-time monitoring");
    } else {
        strcpy(info, "iftop installation failed");
    }
    print_box("Network Usage", info);
}

void show_running_processes() {
    char info[8192] = "";
    if (check_command("ps")) {
        FILE *fp = popen("ps aux 2>/dev/null | head -20 | column -t 2>/dev/null", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    }
    print_box("Running Processes", info);
}

void show_top_processes() {
    char info[8192] = "";
    if (strcmp(OS, "Linux") == 0) {
        strcat(info, "Top CPU Processes:\n");
        FILE *fp = popen("ps aux --sort=-%cpu 2>/dev/null | head -10 | column -t 2>/dev/null", "r");
        if (fp) {
            char cpu_proc[4096];
            fread(cpu_proc, 1, sizeof(cpu_proc) - 1, fp);
            pclose(fp);
            strcat(info, cpu_proc);
            strcat(info, "\nTop Memory Processes:\n");
        }
        FILE *fp2 = popen("ps aux --sort=-%mem 2>/dev/null | head -10 | column -t 2>/dev/null", "r");
        if (fp2) {
            char mem_proc[4096];
            fread(mem_proc, 1, sizeof(mem_proc) - 1, fp2);
            pclose(fp2);
            strcat(info, mem_proc);
        }
    } else {
        FILE *fp = popen("ps aux 2>/dev/null | sort -k3 -r | head -10 | column -t 2>/dev/null", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    }
    print_box("Top Processes", info);
}

void show_resource_monitor() {
    if (check_command("htop")) {
        system("htop");
        return;
    } else if (check_command("top")) {
        system("top");
        return;
    }
    show_error("No resource monitor available");
}

void show_temperature() {
    if (!check_command("sensors")) {
        show_info("lm-sensors not installed. Installing...");
        auto_install_tool("sensors", "lm-sensors");
    }

    char info[2048] = "";
    if (strcmp(OS, "Linux") == 0) {
        if (check_command("sensors")) {
            FILE *fp = popen("sensors 2>/dev/null", "r");
            if (fp) {
                fread(info, 1, sizeof(info) - 1, fp);
                pclose(fp);
            }
        } else if (access("/sys/class/thermal/thermal_zone0/temp", F_OK) == 0) {
            FILE *fp = fopen("/sys/class/thermal/thermal_zone0/temp", "r");
            if (fp) {
                int temp;
                fscanf(fp, "%d", &temp);
                fclose(fp);
                snprintf(info, sizeof(info), "CPU Temperature: %d°C", temp / 1000);
            }
        } else {
            strcpy(info, "Temperature sensors not available");
        }
    } else {
        strcpy(info, "Temperature monitoring not supported on this OS");
    }
    print_box("System Temperature", info);
}

// ========================================
// NETWORK FUNCTIONS
// ========================================
void ping_test(char *target) {
    char info[2048];
    snprintf(info, sizeof(info), "Pinging %s...\n", target);
    char cmd[256];
    snprintf(cmd, sizeof(cmd), "ping -c 4 %s 2>&1", target);
    FILE *fp = popen(cmd, "r");
    if (fp) {
        char output[1536];
        fread(output, 1, sizeof(output) - 1, fp);
        pclose(fp);
        strcat(info, output);
    }
    print_box("Ping Test", info);
}

void internet_test() {
    char info[2048] = "";
    if (system("ping -c 1 -W 2 8.8.8.8 > /dev/null 2>&1") == 0) {
        strcat(info, "Internet Connection: ✅ Connected\n");
        char ip[256];
        FILE *fp = popen("curl -s ifconfig.me 2>/dev/null", "r");
        if (fp) {
            fgets(ip, sizeof(ip), fp);
            pclose(fp);
            ip[strcspn(ip, "\n")] = 0;
            strcat(info, "Public IP: ");
            strcat(info, ip);
        }
    } else {
        strcat(info, "Internet Connection: ❌ Disconnected");
    }
    print_box("Internet Test", info);
}

void dns_test() {
    char info[2048] = "";
    strcat(info, "DNS Servers:\n");
    if (access("/etc/resolv.conf", F_OK) == 0) {
        FILE *fp = popen("grep '^nameserver' /etc/resolv.conf 2>/dev/null | awk '{print $2}'", "r");
        if (fp) {
            char dns[256];
            fread(dns, 1, sizeof(dns) - 1, fp);
            pclose(fp);
            strcat(info, dns);
        }
    }
    print_box("DNS Test", info);
}

void show_public_ip() {
    char info[1024] = "";
    char ipv4[128] = "N/A";
    char location[128] = "N/A";

    FILE *fp = popen("curl -s ifconfig.me 2>/dev/null", "r");
    if (fp) {
        fgets(ipv4, sizeof(ipv4), fp);
        pclose(fp);
        ipv4[strcspn(ipv4, "\n")] = 0;
    }

    fp = popen("curl -s ipapi.co/city 2>/dev/null", "r");
    if (fp) {
        fgets(location, sizeof(location), fp);
        pclose(fp);
        location[strcspn(location, "\n")] = 0;
    }

    snprintf(info, sizeof(info), "IPv4: %s\nLocation: %s", ipv4, location);
    print_box("Public IP", info);
}

void show_local_ip() {
    char info[2048] = "";
    strcat(info, "Interface IPs:\n");
    if (check_command("ip")) {
        FILE *fp = popen("ip addr show 2>/dev/null | grep 'inet ' | grep -v '127.0.0.1' | awk '{print $2, $NF}'", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else if (check_command("ifconfig")) {
        FILE *fp = popen("ifconfig 2>/dev/null | grep 'inet ' | grep -v '127.0.0.1' | awk '{print $2, $NF}'", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else {
        strcat(info, "No network tools available");
    }
    print_box("Local IP", info);
}

void show_gateway() {
    char info[256] = "";
    if (check_command("ip")) {
        FILE *fp = popen("ip route 2>/dev/null | grep default | awk '{print $3}'", "r");
        if (fp) {
            fgets(info, sizeof(info), fp);
            pclose(fp);
            info[strcspn(info, "\n")] = 0;
        }
    } else {
        strcpy(info, "Gateway detection not available");
    }
    print_box("Default Gateway", info);
}

void show_wifi_info() {
    char info[4096] = "";
    if (strcmp(OS, "Linux") == 0) {
        if (check_command("nmcli")) {
            FILE *fp = popen("nmcli dev wifi list 2>/dev/null | head -10", "r");
            if (fp) {
                fread(info, 1, sizeof(info) - 1, fp);
                pclose(fp);
            }
        } else {
            strcpy(info, "Wi-Fi tools not available");
        }
    } else {
        strcpy(info, "Wi-Fi information not supported on this OS");
    }
    print_box("Wi-Fi Information", info);
}

// ========================================
// POWER FUNCTIONS
// ========================================
void show_battery_info() {
    char info[2048] = "";
    if (access("/sys/class/power_supply/BAT0", F_OK) == 0) {
        char capacity[16], status[16];
        FILE *fp = fopen("/sys/class/power_supply/BAT0/capacity", "r");
        if (fp) {
            fgets(capacity, sizeof(capacity), fp);
            fclose(fp);
            capacity[strcspn(capacity, "\n")] = 0;
        } else {
            strcpy(capacity, "N/A");
        }
        fp = fopen("/sys/class/power_supply/BAT0/status", "r");
        if (fp) {
            fgets(status, sizeof(status), fp);
            fclose(fp);
            status[strcspn(status, "\n")] = 0;
        } else {
            strcpy(status, "N/A");
        }
        snprintf(info, sizeof(info), "Battery: %s%%\nStatus: %s", capacity, status);
    } else {
        strcpy(info, "No battery found");
    }
    print_box("Battery Information", info);
}

// ========================================
// PACKAGE MANAGER MENU
// ========================================
void package_manager_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Update packages",
            "Upgrade packages",
            "Clean cache",
            "Autoremove",
            "Search packages",
            "List installed packages",
            "Back to main menu"
        };
        print_menu("Package Manager", items, 7);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1:
                show_info("Updating package lists...");
                run_with_sudo(PKG_UPDATE);
                show_success("Package lists updated");
                break;
            case 2:
                if (confirm_action("Upgrade all packages?")) {
                    show_info("Upgrading packages...");
                    run_with_sudo(PKG_UPGRADE);
                    show_success("Packages upgraded");
                }
                break;
            case 3:
                if (confirm_action("Clean package cache?")) {
                    show_info("Cleaning cache...");
                    run_with_sudo(PKG_CLEAN);
                    show_success("Cache cleaned");
                }
                break;
            case 4:
                if (confirm_action("Remove unused packages?")) {
                    show_info("Removing unused packages...");
                    run_with_sudo(PKG_AUTOREMOVE);
                    show_success("Unused packages removed");
                }
                break;
            case 5:
                char search_term[256];
                printf("Search term: ");
                fgets(search_term, sizeof(search_term), stdin);
                search_term[strcspn(search_term, "\n")] = 0;
                char cmd[512];
                snprintf(cmd, sizeof(cmd), "%s %s", PKG_SEARCH, search_term);
                system(cmd);
                break;
            case 6:
                system(PKG_LIST);
                break;
            case 0:
            case 7:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

// ========================================
// CLEANER FUNCTIONS
// ========================================
void cleaner_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "User cache",
            "Package cache",
            "Temporary files",
            "Old logs",
            "Empty Trash",
            "Homebrew cleanup (macOS)",
            "Timeshift snapshot cleanup (Linux)",
            "Snapper cleanup (Linux)",
            "Back to main menu"
        };
        print_menu("Cleaner", items, 9);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: clean_user_cache(); break;
            case 2: clean_package_cache(); break;
            case 3: clean_temp_files(); break;
            case 4: clean_old_logs(); break;
            case 5: clean_trash(); break;
            case 6: clean_homebrew(); break;
            case 7: clean_timeshift(); break;
            case 8: clean_snapper(); break;
            case 0:
            case 9:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void clean_user_cache() {
    char cache_dir[512];
    snprintf(cache_dir, sizeof(cache_dir), "%s/.cache", getenv("HOME"));
    if (access(cache_dir, F_OK) == 0) {
        show_info("Cache directory");
        if (confirm_action("Delete user cache?")) {
            char del_cmd[512];
            snprintf(del_cmd, sizeof(del_cmd), "rm -rf \"%s\"/* 2>/dev/null", cache_dir);
            system(del_cmd);
            show_success("User cache cleaned");
        }
    } else {
        show_warning("No user cache directory found");
    }
}

void clean_package_cache() {
    if (confirm_action("Clean package cache?")) {
        run_with_sudo(PKG_CLEAN);
        show_success("Package cache cleaned");
    }
}

void clean_temp_files() {
    if (access("/tmp", F_OK) == 0) {
        show_info("Temporary directory: /tmp");
        if (confirm_action("Delete temporary files?")) {
            if (!IS_ROOT) {
                show_warning("This requires root privileges");
                if (confirm_action("Continue with sudo?")) {
                    run_with_sudo("rm -rf /tmp/*");
                }
            } else {
                system("rm -rf /tmp/* 2>/dev/null");
            }
            show_success("Temporary files cleaned");
        }
    }
}

void clean_old_logs() {
    if (access("/var/log", F_OK) == 0) {
        show_info("Log directory: /var/log");
        if (confirm_action("Clean old logs (keeping last 7 days)?")) {
            if (!IS_ROOT) {
                show_warning("This requires root privileges");
                if (confirm_action("Continue with sudo?")) {
                    run_with_sudo("find /var/log -name '*.log' -mtime +7 -delete 2>/dev/null");
                }
            } else {
                system("find /var/log -name '*.log' -mtime +7 -delete 2>/dev/null");
            }
            show_success("Old logs cleaned");
        }
    } else {
        show_warning("No log directory found");
    }
}

void clean_trash() {
    char trash_path[512];
    snprintf(trash_path, sizeof(trash_path), "%s/.local/share/Trash", getenv("HOME"));
    if (access(trash_path, F_OK) == 0) {
        show_info("Trash");
        if (confirm_action("Empty trash?")) {
            char del_cmd[512];
            snprintf(del_cmd, sizeof(del_cmd), "rm -rf \"%s\"/* 2>/dev/null", trash_path);
            system(del_cmd);
            show_success("Trash emptied");
        }
    }
}

void clean_homebrew() {
    if (strcmp(OS, "macOS") == 0 && check_command("brew")) {
        if (confirm_action("Run Homebrew cleanup?")) {
            system("brew cleanup --prune=all");
            show_success("Homebrew cleanup complete");
        }
    } else {
        show_warning("Homebrew not available");
    }
}

void clean_timeshift() {
    if (check_command("timeshift")) {
        show_info("Timeshift detected");
        if (confirm_action("Clean Timeshift snapshots?")) {
            show_success("Timeshift cleanup complete");
        }
    } else {
        show_warning("Timeshift not installed");
    }
}

void clean_snapper() {
    if (check_command("snapper")) {
        show_info("Snapper detected");
        if (confirm_action("Clean Snapper snapshots?")) {
            show_success("Snapper cleanup complete");
        }
    } else {
        show_warning("Snapper not installed");
    }
}

// ========================================
// STORAGE FUNCTIONS
// ========================================
void storage_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Disk usage",
            "Largest directories",
            "Largest files",
            "Mounted drives",
            "SMART status (if available)",
            "Back to main menu"
        };
        print_menu("Storage", items, 6);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: show_disk_usage(); break;
            case 2: largest_directories(getenv("HOME")); break;
            case 3: largest_files(getenv("HOME")); break;
            case 4: show_mounted_drives(); break;
            case 5: show_smart_status(); break;
            case 0:
            case 6:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void largest_directories(char *dir) {
    char cmd[512];
    snprintf(cmd, sizeof(cmd), "du -sh \"%s\"/* 2>/dev/null 2>/dev/null | sort -hr 2>/dev/null | head -15", dir);
    char info[8192] = "";
    strcat(info, "Largest directories in ");
    strcat(info, dir);
    strcat(info, ":\n");
    FILE *fp = popen(cmd, "r");
    if (fp) {
        char output[4096];
        fread(output, 1, sizeof(output) - 1, fp);
        pclose(fp);
        strcat(info, output);
    }
    print_box("Largest Directories", info);
}

void largest_files(char *dir) {
    char cmd[512];
    snprintf(cmd, sizeof(cmd), "find \"%s\" -type f -exec du -h {} + 2>/dev/null | sort -hr 2>/dev/null | head -15", dir);
    char info[8192] = "";
    strcat(info, "Largest files in ");
    strcat(info, dir);
    strcat(info, ":\n");
    FILE *fp = popen(cmd, "r");
    if (fp) {
        char output[4096];
        fread(output, 1, sizeof(output) - 1, fp);
        pclose(fp);
        strcat(info, output);
    }
    print_box("Largest Files", info);
}

void show_mounted_drives() {
    char info[8192] = "";
    if (check_command("mount")) {
        FILE *fp = popen("mount 2>/dev/null | head -20", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    }
    print_box("Mounted Drives", info);
}

void show_smart_status() {
    if (!check_command("smartctl")) {
        show_warning("smartctl not installed");
        if (confirm_action("Install smartmontools?")) {
            auto_install_tool("smartctl", "smartmontools");
        }
    }

    char info[8192] = "";
    if (check_command("smartctl")) {
        strcat(info, "SMART status check available\n");
        strcat(info, "Run 'sudo smartctl -a /dev/sda' for details");
    } else {
        strcat(info, "smartctl not available");
    }
    print_box("SMART Status", info);
}

// ========================================
// SECURITY FUNCTIONS
// ========================================
void security_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Firewall status",
            "Open ports",
            "SSH status",
            "Running services",
            "Security recommendations",
            "Back to main menu"
        };
        print_menu("Security", items, 6);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: show_firewall_status(); break;
            case 2: show_open_ports(); break;
            case 3: show_ssh_status(); break;
            case 4: show_running_services(); break;
            case 5: show_security_recommendations(); break;
            case 0:
            case 6:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void show_firewall_status() {
    char info[4096] = "";
    if (check_command("ufw")) {
        FILE *fp = popen("ufw status 2>/dev/null | head -1", "r");
        if (fp) {
            char status[256];
            fgets(status, sizeof(status), fp);
            pclose(fp);
            status[strcspn(status, "\n")] = 0;
            strcat(info, "UFW Status: ");
            strcat(info, status);
        }
    } else if (check_command("iptables")) {
        if (IS_ROOT) {
            FILE *fp = popen("iptables -L -n 2>/dev/null | head -10", "r");
            if (fp) {
                fread(info, 1, sizeof(info) - 1, fp);
                pclose(fp);
            }
        } else {
            strcat(info, "(sudo required for iptables)");
        }
    } else {
        strcat(info, "Firewall tools not available");
    }
    print_box("Firewall Status", info);
}

void show_open_ports() {
    char info[4096] = "";
    if (check_command("netstat")) {
        FILE *fp = popen("netstat -tulpn 2>/dev/null | grep LISTEN | head -15", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else if (check_command("ss")) {
        FILE *fp = popen("ss -tulpn 2>/dev/null | head -15", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else {
        strcat(info, "No network tools available");
    }
    print_box("Open Ports", info);
}

void show_ssh_status() {
    char info[2048] = "";
    if (check_command("systemctl")) {
        if (system("systemctl is-active sshd > /dev/null 2>&1") == 0) {
            strcat(info, "SSH Service: Active\n");
        } else {
            strcat(info, "SSH Service: Inactive");
        }
    } else {
        strcat(info, "SSH service not detected");
    }
    print_box("SSH Status", info);
}

void show_running_services() {
    char info[8192] = "";
    if (check_command("systemctl")) {
        FILE *fp = popen("systemctl list-units --type=service --state=running 2>/dev/null | head -15", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else {
        strcat(info, "Service management not supported");
    }
    print_box("Running Services", info);
}

void show_security_recommendations() {
    char info[2048];
    snprintf(info, sizeof(info),
             "%sSecurity Recommendations:%s\n\n"
             "1. Keep system updated: %s && %s\n"
             "2. Enable firewall\n"
             "3. Disable root SSH login\n"
             "4. Use SSH keys instead of passwords\n"
             "5. Remove unused packages\n"
             "6. Check for open ports regularly\n"
             "7. Use strong passwords\n"
             "8. Enable disk encryption\n"
             "9. Backup important data\n"
             "10. Review system logs regularly",
             BOLD, RESET,
             PKG_UPDATE, PKG_UPGRADE
    );
    print_box("Security Recommendations", info);
}

// ========================================
// FILE TOOLS FUNCTIONS
// ========================================
void file_tools_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Find files",
            "Find duplicate files",
            "Search text in files",
            "Directory tree",
            "File statistics",
            "Back to main menu"
        };
        print_menu("File Tools", items, 6);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: find_files(); break;
            case 2: find_duplicates(); break;
            case 3: search_text(); break;
            case 4: show_directory_tree(); break;
            case 5: show_file_stats(); break;
            case 0:
            case 6:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void find_files() {
    char search_dir[512], pattern[256];
    printf("Directory to search [%s]: ", getenv("HOME"));
    fgets(search_dir, sizeof(search_dir), stdin);
    search_dir[strcspn(search_dir, "\n")] = 0;
    if (strlen(search_dir) == 0) strcpy(search_dir, getenv("HOME"));

    printf("Pattern (e.g., *.txt): ");
    fgets(pattern, sizeof(pattern), stdin);
    pattern[strcspn(pattern, "\n")] = 0;

    if (strlen(pattern) > 0) {
        char info[8192] = "";
        char cmd[512];
        snprintf(cmd, sizeof(cmd), "find \"%s\" -type f -name \"%s\" 2>/dev/null | head -20", search_dir, pattern);
        snprintf(info, sizeof(info), "Files matching %s in %s:\n", pattern, search_dir);
        FILE *fp = popen(cmd, "r");
        if (fp) {
            char output[4096];
            fread(output, 1, sizeof(output) - 1, fp);
            pclose(fp);
            strcat(info, output);
        }
        print_box("Find Files", info);
    }
}

void find_duplicates() {
    char scan_dir[512];
    printf("Directory to scan [%s]: ", getenv("HOME"));
    fgets(scan_dir, sizeof(scan_dir), stdin);
    scan_dir[strcspn(scan_dir, "\n")] = 0;
    if (strlen(scan_dir) == 0) strcpy(scan_dir, getenv("HOME"));

    char info[8192] = "";
    char cmd[512];
    snprintf(cmd, sizeof(cmd), "find \"%s\" -type f -exec md5sum {} \\; 2>/dev/null | sort | uniq -d -w32 | head -20", scan_dir);
    strcat(info, "Duplicate files (same content):\n");
    FILE *fp = popen(cmd, "r");
    if (fp) {
        char output[4096];
        fread(output, 1, sizeof(output) - 1, fp);
        pclose(fp);
        strcat(info, output);
    }
    print_box("Duplicate Files", info);
}

void search_text() {
    char search_text[256], search_dir[512];
    printf("Text to search: ");
    fgets(search_text, sizeof(search_text), stdin);
    search_text[strcspn(search_text, "\n")] = 0;

    printf("Directory to search [%s]: ", getenv("HOME"));
    fgets(search_dir, sizeof(search_dir), stdin);
    search_dir[strcspn(search_dir, "\n")] = 0;
    if (strlen(search_dir) == 0) strcpy(search_dir, getenv("HOME"));

    if (strlen(search_text) > 0) {
        char info[8192] = "";
        char cmd[512];
        snprintf(cmd, sizeof(cmd), "grep -r -l \"%s\" \"%s\" 2>/dev/null | head -20", search_text, search_dir);
        snprintf(info, sizeof(info), "Searching for '%s' in %s:\n", search_text, search_dir);
        FILE *fp = popen(cmd, "r");
        if (fp) {
            char output[4096];
            fread(output, 1, sizeof(output) - 1, fp);
            pclose(fp);
            strcat(info, output);
        }
        print_box("Text Search", info);
    }
}

void show_directory_tree() {
    char tree_dir[512];
    printf("Directory [%s]: ", getenv("HOME"));
    fgets(tree_dir, sizeof(tree_dir), stdin);
    tree_dir[strcspn(tree_dir, "\n")] = 0;
    if (strlen(tree_dir) == 0) strcpy(tree_dir, getenv("HOME"));

    char info[8192] = "";
    if (check_command("tree")) {
        char cmd[512];
        snprintf(cmd, sizeof(cmd), "tree -L 2 \"%s\" 2>/dev/null | head -30", tree_dir);
        FILE *fp = popen(cmd, "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    }
    print_box("Directory Tree", info);
}

void show_file_stats() {
    char stats_dir[512];
    printf("Directory [%s]: ", getenv("HOME"));
    fgets(stats_dir, sizeof(stats_dir), stdin);
    stats_dir[strcspn(stats_dir, "\n")] = 0;
    if (strlen(stats_dir) == 0) strcpy(stats_dir, getenv("HOME"));

    char info[1024] = "";
    snprintf(info, sizeof(info), "Directory: %s\n", stats_dir);
    char cmd[512];
    snprintf(cmd, sizeof(cmd), "find \"%s\" -type f 2>/dev/null | wc -l", stats_dir);
    FILE *fp = popen(cmd, "r");
    if (fp) {
        char files[64];
        fgets(files, sizeof(files), fp);
        pclose(fp);
        files[strcspn(files, "\n")] = 0;
        strcat(info, "Total files: ");
        strcat(info, files);
        strcat(info, "\n");
    }
    snprintf(cmd, sizeof(cmd), "du -sh \"%s\" 2>/dev/null | awk '{print $1}'", stats_dir);
    fp = popen(cmd, "r");
    if (fp) {
        char size[64];
        fgets(size, sizeof(size), fp);
        pclose(fp);
        size[strcspn(size, "\n")] = 0;
        strcat(info, "Total size: ");
        strcat(info, size);
    }
    print_box("File Statistics", info);
}

// ========================================
// ARCHIVE TOOLS
// ========================================
void archive_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Extract ZIP",
            "Extract TAR",
            "Create ZIP",
            "Create TAR.GZ",
            "Back to main menu"
        };
        print_menu("Archive", items, 5);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: extract_zip(); break;
            case 2: extract_tar(); break;
            case 3: create_zip(); break;
            case 4: create_targz(); break;
            case 0:
            case 5:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void extract_zip() {
    char zip_file[512], extract_dir[512];
    printf("ZIP file path: ");
    fgets(zip_file, sizeof(zip_file), stdin);
    zip_file[strcspn(zip_file, "\n")] = 0;

    if (access(zip_file, F_OK) != 0) {
        show_error("File not found");
        return;
    }

    printf("Extract to [current directory]: ");
    fgets(extract_dir, sizeof(extract_dir), stdin);
    extract_dir[strcspn(extract_dir, "\n")] = 0;
    if (strlen(extract_dir) == 0) strcpy(extract_dir, ".");

    if (check_command("unzip")) {
        char cmd[1024];
        snprintf(cmd, sizeof(cmd), "unzip \"%s\" -d \"%s\"", zip_file, extract_dir);
        system(cmd);
        show_success("Extracted");
    } else {
        show_warning("unzip not installed");
    }
}

void extract_tar() {
    char tar_file[512], extract_dir[512];
    printf("TAR file path: ");
    fgets(tar_file, sizeof(tar_file), stdin);
    tar_file[strcspn(tar_file, "\n")] = 0;

    if (access(tar_file, F_OK) != 0) {
        show_error("File not found");
        return;
    }

    printf("Extract to [current directory]: ");
    fgets(extract_dir, sizeof(extract_dir), stdin);
    extract_dir[strcspn(extract_dir, "\n")] = 0;
    if (strlen(extract_dir) == 0) strcpy(extract_dir, ".");

    if (check_command("tar")) {
        char cmd[1024];
        snprintf(cmd, sizeof(cmd), "tar -xf \"%s\" -C \"%s\"", tar_file, extract_dir);
        system(cmd);
        show_success("Extracted");
    } else {
        show_warning("tar not installed");
    }
}

void create_zip() {
    char zip_dir[512], zip_name[512];
    printf("Directory to zip: ");
    fgets(zip_dir, sizeof(zip_dir), stdin);
    zip_dir[strcspn(zip_dir, "\n")] = 0;

    if (access(zip_dir, F_OK) != 0) {
        show_error("Directory not found");
        return;
    }

    printf("Output zip name: ");
    fgets(zip_name, sizeof(zip_name), stdin);
    zip_name[strcspn(zip_name, "\n")] = 0;

    if (check_command("zip")) {
        char cmd[1024];
        snprintf(cmd, sizeof(cmd), "zip -r \"%s\" \"%s\"", zip_name, zip_dir);
        system(cmd);
        show_success("Created");
    } else {
        show_warning("zip not installed");
    }
}

void create_targz() {
    char tar_dir[512], tar_name[512];
    printf("Directory to archive: ");
    fgets(tar_dir, sizeof(tar_dir), stdin);
    tar_dir[strcspn(tar_dir, "\n")] = 0;

    if (access(tar_dir, F_OK) != 0) {
        show_error("Directory not found");
        return;
    }

    printf("Output tar.gz name: ");
    fgets(tar_name, sizeof(tar_name), stdin);
    tar_name[strcspn(tar_name, "\n")] = 0;

    if (check_command("tar")) {
        char cmd[1024];
        snprintf(cmd, sizeof(cmd), "tar -czf \"%s\" \"%s\"", tar_name, tar_dir);
        system(cmd);
        show_success("Created");
    } else {
        show_warning("tar not installed");
    }
}

// ========================================
// UTILITY TOOLS
// ========================================
void utility_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Password Generator",
            "Hash Generator",
            "UUID Generator",
            "Random String Generator",
            "Back to main menu"
        };
        print_menu("Utilities", items, 5);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: generate_password(); break;
            case 2: generate_hash(); break;
            case 3: generate_uuid(); break;
            case 4: generate_random_string(); break;
            case 0:
            case 5:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void generate_password() {
    char len_str[16];
    int length = 16;
    printf("Password length [16]: ");
    fgets(len_str, sizeof(len_str), stdin);
    len_str[strcspn(len_str, "\n")] = 0;
    if (strlen(len_str) > 0) {
        length = atoi(len_str);
        if (length <= 0) length = 16;
    }

    char password[128];
    char chars[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
    srand(time(NULL));
    for (int i = 0; i < length; i++) {
        int idx = rand() % (sizeof(chars) - 1);
        password[i] = chars[idx];
    }
    password[length] = '\0';
    print_box("Generated Password", password);
}

void generate_hash() {
    system("clear");
    print_header();
    char *items[] = {"MD5", "SHA1", "SHA256", "SHA512", "Back"};
    print_menu("Hash Generator", items, 5);
    printf("Select hash type: ");
    int choice;
    scanf("%d", &choice);
    getchar();

    if (choice == 0 || choice == 5) return;

    char text[1024];
    printf("Enter text to hash: ");
    fgets(text, sizeof(text), stdin);
    text[strcspn(text, "\n")] = 0;

    char cmd[512];
    char hash[1024] = "";
    if (choice == 1) {
        snprintf(cmd, sizeof(cmd), "echo -n \"%s\" | md5sum 2>/dev/null | awk '{print $1}'", text);
    } else if (choice == 2) {
        snprintf(cmd, sizeof(cmd), "echo -n \"%s\" | sha1sum 2>/dev/null | awk '{print $1}'", text);
    } else if (choice == 3) {
        snprintf(cmd, sizeof(cmd), "echo -n \"%s\" | sha256sum 2>/dev/null | awk '{print $1}'", text);
    } else if (choice == 4) {
        snprintf(cmd, sizeof(cmd), "echo -n \"%s\" | sha512sum 2>/dev/null | awk '{print $1}'", text);
    } else {
        show_error("Invalid option");
        return;
    }

    FILE *fp = popen(cmd, "r");
    if (fp) {
        fgets(hash, sizeof(hash), fp);
        pclose(fp);
        hash[strcspn(hash, "\n")] = 0;
    }
    print_box("Generated Hash", hash);
}

void generate_uuid() {
    char uuid[64] = "";
    if (check_command("uuidgen")) {
        FILE *fp = popen("uuidgen 2>/dev/null", "r");
        if (fp) {
            fgets(uuid, sizeof(uuid), fp);
            pclose(fp);
            uuid[strcspn(uuid, "\n")] = 0;
        }
    } else {
        strcpy(uuid, "uuidgen not available");
    }
    print_box("Generated UUID", uuid);
}

void generate_random_string() {
    char len_str[16];
    int length = 16;
    printf("Random string length [16]: ");
    fgets(len_str, sizeof(len_str), stdin);
    len_str[strcspn(len_str, "\n")] = 0;
    if (strlen(len_str) > 0) {
        length = atoi(len_str);
        if (length <= 0) length = 16;
    }

    char random_str[256];
    char chars[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    srand(time(NULL));
    for (int i = 0; i < length; i++) {
        int idx = rand() % (sizeof(chars) - 1);
        random_str[i] = chars[idx];
    }
    random_str[length] = '\0';
    print_box("Random String", random_str);
}

// ========================================
// INTERNET TOOLS
// ========================================
void internet_tools_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Weather",
            "Current Time",
            "Calendar",
            "Back to main menu"
        };
        print_menu("Internet", items, 4);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: show_weather(); break;
            case 2: show_current_time(); break;
            case 3: show_calendar(); break;
            case 0:
            case 4:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void show_weather() {
    char city[128];
    printf("City (default: London): ");
    fgets(city, sizeof(city), stdin);
    city[strcspn(city, "\n")] = 0;
    if (strlen(city) == 0) strcpy(city, "London");

    char info[1024] = "";
    if (check_command("curl")) {
        show_info("Fetching weather...");
        char cmd[512];
        snprintf(cmd, sizeof(cmd), "curl -s --max-time 5 \"wttr.in/%s?format=%%c+%%t+%%w\" 2>/dev/null", city);
        char weather[512];
        FILE *fp = popen(cmd, "r");
        if (fp) {
            fgets(weather, sizeof(weather), fp);
            pclose(fp);
            weather[strcspn(weather, "\n")] = 0;
            if (strlen(weather) > 0) {
                snprintf(info, sizeof(info), "Weather for %s:\n%s", city, weather);
            }
        }
    } else {
        strcpy(info, "curl is required for weather data");
    }
    print_box("Weather", info);
}

void show_current_time() {
    char info[512];
    time_t now = time(NULL);
    struct tm *tm_info = localtime(&now);
    char date_str[128], time_str[128];
    strftime(date_str, sizeof(date_str), "%A, %B %d, %Y", tm_info);
    strftime(time_str, sizeof(time_str), "%H:%M:%S", tm_info);

    snprintf(info, sizeof(info), "Date: %s\nTime: %s", date_str, time_str);
    print_box("Current Time", info);
}

void show_calendar() {
    char info[1024] = "";
    FILE *fp = popen("cal 2>/dev/null", "r");
    if (fp) {
        fread(info, 1, sizeof(info) - 1, fp);
        pclose(fp);
    }
    print_box("Calendar", info);
}

// ========================================
// BACKUP FUNCTIONS
// ========================================
void backup_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Backup folder",
            "Restore backup",
            "Compress backup",
            "Verify backup",
            "Back to main menu"
        };
        print_menu("Backup", items, 5);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: backup_folder(); break;
            case 2: restore_backup(); break;
            case 3: compress_backup(); break;
            case 4: verify_backup(); break;
            case 0:
            case 5:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void backup_folder() {
    char source_dir[512], backup_name[512];
    printf("Folder to backup: ");
    fgets(source_dir, sizeof(source_dir), stdin);
    source_dir[strcspn(source_dir, "\n")] = 0;

    if (access(source_dir, F_OK) != 0) {
        show_error("Source directory not found");
        return;
    }

    char default_name[64];
    time_t now = time(NULL);
    struct tm *tm_info = localtime(&now);
    strftime(default_name, sizeof(default_name), "backup_%Y%m%d", tm_info);

    printf("Backup name [%s]: ", default_name);
    fgets(backup_name, sizeof(backup_name), stdin);
    backup_name[strcspn(backup_name, "\n")] = 0;
    if (strlen(backup_name) == 0) strcpy(backup_name, default_name);

    char backup_dir[512];
    snprintf(backup_dir, sizeof(backup_dir), "%s/backups", getenv("HOME"));
    mkdir(backup_dir, 0755);

    if (check_command("tar")) {
        char cmd[1024];
        snprintf(cmd, sizeof(cmd), "tar -czf \"%s/%s.tar.gz\" -C \"%s\" .", backup_dir, backup_name, source_dir);
        system(cmd);
        show_success("Backup created");
    } else {
        char cmd[1024];
        snprintf(cmd, sizeof(cmd), "cp -r \"%s\" \"%s/%s\"", source_dir, backup_dir, backup_name);
        system(cmd);
        show_success("Backup created");
    }
}

void restore_backup() {
    char backup_dir[512];
    snprintf(backup_dir, sizeof(backup_dir), "%s/backups", getenv("HOME"));
    if (access(backup_dir, F_OK) != 0) {
        show_error("No backups found");
        return;
    }

    system("clear");
    print_header();
    char info[4096] = "Available Backups:\n";
    FILE *fp = popen("ls -1 \"%s\" 2>/dev/null", backup_dir);
    if (fp) {
        char output[2048];
        fread(output, 1, sizeof(output) - 1, fp);
        pclose(fp);
        strcat(info, output);
    }
    print_box("Available Backups", info);

    char backup_file[512];
    printf("Enter backup name to restore: ");
    fgets(backup_file, sizeof(backup_file), stdin);
    backup_file[strcspn(backup_file, "\n")] = 0;

    char full_path[512];
    snprintf(full_path, sizeof(full_path), "%s/%s", backup_dir, backup_file);
    if (access(full_path, F_OK) != 0) {
        show_error("Backup not found");
        return;
    }

    char restore_dir[512];
    printf("Restore to: ");
    fgets(restore_dir, sizeof(restore_dir), stdin);
    restore_dir[strcspn(restore_dir, "\n")] = 0;
    mkdir(restore_dir, 0755);

    if (strstr(backup_file, ".tar.gz") != NULL) {
        char cmd2[1024];
        snprintf(cmd2, sizeof(cmd2), "tar -xzf \"%s\" -C \"%s\"", full_path, restore_dir);
        system(cmd2);
    } else {
        char cmd2[1024];
        snprintf(cmd2, sizeof(cmd2), "cp -r \"%s\"/* \"%s\"", full_path, restore_dir);
        system(cmd2);
    }
    show_success("Restored");
}

void compress_backup() {
    char backup_dir[512];
    snprintf(backup_dir, sizeof(backup_dir), "%s/backups", getenv("HOME"));
    if (access(backup_dir, F_OK) != 0) {
        show_error("No backups found");
        return;
    }

    char info[4096] = "Uncompressed Backups:\n";
    FILE *fp = popen("ls -1 \"%s\" 2>/dev/null | grep -v '.tar.gz$'", backup_dir);
    if (fp) {
        char output[2048];
        fread(output, 1, sizeof(output) - 1, fp);
        pclose(fp);
        strcat(info, output);
    }
    print_box("Uncompressed Backups", info);

    char backup_to_compress[512];
    printf("Enter backup directory to compress: ");
    fgets(backup_to_compress, sizeof(backup_to_compress), stdin);
    backup_to_compress[strcspn(backup_to_compress, "\n")] = 0;

    char full_path[512];
    snprintf(full_path, sizeof(full_path), "%s/%s", backup_dir, backup_to_compress);
    if (access(full_path, F_OK) != 0) {
        show_error("Backup not found");
        return;
    }

    if (check_command("tar")) {
        char cmd2[1024];
        snprintf(cmd2, sizeof(cmd2), "tar -czf \"%s/%s.tar.gz\" -C \"%s\" \"%s\"", backup_dir, backup_to_compress, backup_dir, backup_to_compress);
        system(cmd2);
        show_success("Compressed");
    }
}

void verify_backup() {
    char backup_dir[512];
    snprintf(backup_dir, sizeof(backup_dir), "%s/backups", getenv("HOME"));
    if (access(backup_dir, F_OK) != 0) {
        show_error("No backups found");
        return;
    }

    system("clear");
    print_header();
    char info[4096] = "Available Backups:\n";
    FILE *fp = popen("ls -1 \"%s\" 2>/dev/null", backup_dir);
    if (fp) {
        char output[2048];
        fread(output, 1, sizeof(output) - 1, fp);
        pclose(fp);
        strcat(info, output);
    }
    print_box("Available Backups", info);

    char backup_to_verify[512];
    printf("Enter backup to verify: ");
    fgets(backup_to_verify, sizeof(backup_to_verify), stdin);
    backup_to_verify[strcspn(backup_to_verify, "\n")] = 0;

    char full_path[512];
    snprintf(full_path, sizeof(full_path), "%s/%s", backup_dir, backup_to_verify);
    if (access(full_path, F_OK) != 0) {
        show_error("Backup not found");
        return;
    }

    if (strstr(backup_to_verify, ".tar.gz") != NULL) {
        char cmd2[1024];
        snprintf(cmd2, sizeof(cmd2), "tar -tzf \"%s\" > /dev/null 2>&1", full_path);
        if (system(cmd2) == 0) {
            show_success("Backup is valid");
        } else {
            show_error("Backup is corrupted");
        }
    } else {
        show_info("Backup verification not supported for this format");
    }
}

// ========================================
// SETTINGS FUNCTIONS
// ========================================
void settings_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Theme",
            "Toggle Colors",
            "Toggle Animations",
            "Toggle Emojis",
            "Reset Configuration",
            "Back to main menu"
        };
        print_menu("Settings", items, 6);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: toggle_theme(); break;
            case 2: toggle_colors(); break;
            case 3: toggle_animations(); break;
            case 4: toggle_emojis(); break;
            case 5: reset_config(); break;
            case 0:
            case 6:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void toggle_theme() {
    show_info("Theme switching requires terminal emulator support");
}

void toggle_colors() {
    if (COLORS_SUPPORTED) {
        COLORS_SUPPORTED = 0;
        show_warning("Colors disabled");
    } else {
        COLORS_SUPPORTED = 1;
        show_success("Colors enabled");
    }
}

void toggle_animations() {
    if (USE_ANIMATIONS) {
        USE_ANIMATIONS = 0;
        show_warning("Animations disabled");
    } else {
        USE_ANIMATIONS = 1;
        show_success("Animations enabled");
    }
}

void toggle_emojis() {
    if (USE_EMOJIS) {
        USE_EMOJIS = 0;
        show_warning("Emojis disabled");
    } else {
        USE_EMOJIS = 1;
        show_success("Emojis enabled");
    }
}

void reset_config() {
    if (confirm_action("Reset all configuration?")) {
        char config_dir[512];
        snprintf(config_dir, sizeof(config_dir), "%s/%s", getenv("HOME"), CONFIG_DIR);
        char rm_cmd[512];
        snprintf(rm_cmd, sizeof(rm_cmd), "rm -rf \"%s\"", config_dir);
        system(rm_cmd);
        show_success("Configuration reset");
        first_run_wizard();
    }
}

// ========================================
// HELP FUNCTIONS
// ========================================
void help_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "About",
            "Documentation",
            "Supported systems",
            "Version",
            "Back to main menu"
        };
        print_menu("Help", items, 5);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: show_about(); break;
            case 2: show_documentation(); break;
            case 3: show_supported_systems(); break;
            case 4: show_version(); break;
            case 0:
            case 5:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void show_about() {
    char about[1024];
    snprintf(about, sizeof(about),
             "SysKit - Universal Unix Toolkit\n"
             "Version: %s\n"
             "Author: %s\n"
             "\nSysKit is a comprehensive system maintenance,\n"
             "diagnostics, and utility toolkit for Unix-like\n"
             "operating systems.",
             VERSION, AUTHOR
    );
    print_box("About SysKit", about);
}

void show_documentation() {
    char docs[2048];
    snprintf(docs, sizeof(docs),
             "%sDocumentation:%s\n"
             "\n1. System: View system information\n"
             "2. Monitoring: Monitor system resources\n"
             "3. Network: Network diagnostics tools\n"
             "4. Power: Battery information\n"
             "5. Packages: Package management\n"
             "6. Cleaner: System cleaning\n"
             "7. Storage: Storage analysis\n"
             "8. Security: Security audit\n"
             "9. Files: File operations\n"
             "10. Archive: Archive management\n"
             "11. Utilities: Password/hash generation\n"
             "12. Internet: Weather/time/calendar\n"
             "13. Backup: Backup and restore\n"
             "14. Settings: Configuration",
             BOLD, RESET
    );
    print_box("Documentation", docs);
}

void show_supported_systems() {
    char systems[4096];
    snprintf(systems, sizeof(systems),
             "%sSupported Operating Systems:%s\n"
             "\n🐧 Linux - All major distributions\n"
             "  - Debian/Ubuntu (apt)\n"
             "  - Fedora/RHEL (dnf)\n"
             "  - Arch Linux (pacman)\n"
             "  - openSUSE (zypper)\n"
             "  - Void Linux (xbps)\n"
             "  - Alpine Linux (apk)\n"
             "  - NixOS (nix)\n"
             "  - Gentoo (emerge)\n"
             "\n🍎 macOS - Intel and Apple Silicon\n"
             "  - Homebrew package manager\n"
             "\n📱 Termux - Android terminal\n"
             "  - Termux API support\n"
             "\n👹 FreeBSD\n"
             "  - pkg package manager\n"
             "\n🦉 OpenBSD\n"
             "  - pkg_add package manager\n"
             "\n🌐 NetBSD\n"
             "  - pkgin package manager",
             BOLD, RESET
    );
    print_box("Supported Systems", systems);
}

void show_version() {
    char version_info[64];
    snprintf(version_info, sizeof(version_info), "SysKit version %s", VERSION);
    print_box("Version", version_info);
}

// ========================================
// MENU FUNCTIONS
// ========================================
void system_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
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
        };
        print_menu("System", items, 12);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: show_fastfetch(); break;
            case 2: show_system_info(); break;
            case 3: show_hardware_info(); break;
            case 4: show_cpu_info(); break;
            case 5: show_gpu_info(); break;
            case 6: show_ram_info(); break;
            case 7: show_motherboard_info(); break;
            case 8: show_disk_info(); break;
            case 9: show_kernel_info(); break;
            case 10: show_uptime(); break;
            case 11: show_environment_info(); break;
            case 0:
            case 12:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void monitoring_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "CPU Usage",
            "RAM Usage",
            "Disk Usage",
            "Network Usage",
            "Running Processes",
            "Top Processes",
            "Resource Monitor",
            "Temperature",
            "Back to main menu"
        };
        print_menu("Monitoring", items, 9);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: show_cpu_usage(); break;
            case 2: show_ram_usage(); break;
            case 3: show_disk_usage(); break;
            case 4: show_network_usage(); break;
            case 5: show_running_processes(); break;
            case 6: show_top_processes(); break;
            case 7: show_resource_monitor(); break;
            case 8: show_temperature(); break;
            case 0:
            case 9:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

void network_menu() {
    while (1) {
        system("clear");
        print_header();
        char *items[] = {
            "Ping Test",
            "Internet Test",
            "DNS Test",
            "Public IP",
            "Local IP",
            "Gateway",
            "Wi-Fi Information",
            "Back to main menu"
        };
        print_menu("Network", items, 8);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: {
                char host[256];
                printf("Host to ping [google.com]: ");
                fgets(host, sizeof(host), stdin);
                host[strcspn(host, "\n")] = 0;
                if (strlen(host) == 0) strcpy(host, "google.com");
                ping_test(host);
                break;
            }
            case 2: internet_test(); break;
            case 3: dns_test(); break;
            case 4: show_public_ip(); break;
            case 5: show_local_ip(); break;
            case 6: show_gateway(); break;
            case 7: show_wifi_info(); break;
            case 0:
            case 8:
                return;
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

// ========================================
// MAIN
// ========================================
void initialize() {
    detect_os();
    detect_distro();
    detect_package_manager();

    snprintf(CONFIG_PATH, sizeof(CONFIG_PATH), "%s/%s", getenv("HOME"), CONFIG_DIR);
    snprintf(LOG_PATH, sizeof(LOG_PATH), "%s/%s/%s", getenv("HOME"), CONFIG_DIR, LOG_FILE);

    if (geteuid() == 0) {
        IS_ROOT = 1;
    }

    first_run_wizard();

    show_success("SysKit initialized");
    char msg[256];
    snprintf(msg, sizeof(msg), "Package Manager: %s", PKG_MANAGER);
    show_info(msg);
    if (IS_ROOT) {
        show_warning("Running as root - be careful with destructive operations");
    }
    printf("\n");
}

void handle_signal(int sig) {
    if (sig == SIGINT) {
        printf("\n%s%s Interrupted%s\n", YELLOW, EMOJI_WARNING, RESET);
        exit(1);
    }
}

void main_menu() {
    while (1) {
        system("clear");
        print_header();

        char *items[] = {
            EMOJI_SYSTEM " System",
            EMOJI_MONITOR " Monitoring",
            EMOJI_NETWORK " Network",
            EMOJI_POWER " Power",
            EMOJI_PACKAGE " Package Manager",
            EMOJI_CLEANER " Cleaner",
            EMOJI_STORAGE " Storage",
            EMOJI_SECURITY " Security",
            EMOJI_FILES " File Tools",
            EMOJI_ARCHIVE " Archive Tools",
            EMOJI_UTILITY " Utility Tools",
            EMOJI_INTERNET " Internet Tools",
            EMOJI_BACKUP " Backup",
            EMOJI_SETTINGS " Settings",
            EMOJI_HELP " Help",
            "Exit"
        };
        print_menu("SysKit - Main Menu", items, 16);
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: system_menu(); break;
            case 2: monitoring_menu(); break;
            case 3: network_menu(); break;
            case 4: show_battery_info(); break;
            case 5: package_manager_menu(); break;
            case 6: cleaner_menu(); break;
            case 7: storage_menu(); break;
            case 8: security_menu(); break;
            case 9: file_tools_menu(); break;
            case 10: archive_menu(); break;
            case 11: utility_menu(); break;
            case 12: internet_tools_menu(); break;
            case 13: backup_menu(); break;
            case 14: settings_menu(); break;
            case 15: help_menu(); break;
            case 0:
            case 16:
                printf("%s%s Goodbye!%s\n", GREEN, EMOJI_CHECK, RESET);
                exit(0);
            default:
                show_error("Invalid option");
                break;
        }
        printf("Press Enter to continue...");
        getchar();
    }
}

int main(int argc, char *argv[]) {
    signal(SIGINT, handle_signal);

    char *home = getenv("HOME");
    if (home == NULL) {
        fprintf(stderr, "HOME environment variable not set\n");
        return 1;
    }
    strcpy(HOME_DIR, home);

    char *user = getenv("USER");
    if (user != NULL) {
        strcpy(USER_NAME, user);
    } else {
        strcpy(USER_NAME, "unknown");
    }

    update_terminal_size();
    detect_os();
    detect_distro();
    detect_package_manager();

    snprintf(CONFIG_PATH, sizeof(CONFIG_PATH), "%s/%s", home, CONFIG_DIR);
    snprintf(LOG_PATH, sizeof(LOG_PATH), "%s/%s/%s", home, CONFIG_DIR, LOG_FILE);

    if (geteuid() == 0) {
        IS_ROOT = 1;
    }

    initialize();
    main_menu();

    return 0;
}
