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
#include <signal.h>
#include <ctype.h>
#include <sys/wait.h>
#include <fcntl.h>

// ========================================
// GLOBAL CONFIG
// ========================================
#define VERSION "2.0.2-termux"
#define AUTHOR "AnshLabs716"
#define CONFIG_DIR ".config/syskit"
#define FIRST_RUN_FILE "first_run_complete"

int TERMINAL_WIDTH = 80;
int TERMINAL_HEIGHT = 24;
int COLORS_SUPPORTED = 1;
int USE_EMOJIS = 1;

// ========================================
// COLORS
// ========================================
#define RED     "\033[0;31m"
#define GREEN   "\033[0;32m"
#define YELLOW  "\033[0;33m"
#define BLUE    "\033[0;34m"
#define CYAN    "\033[0;36m"
#define WHITE   "\033[0;37m"
#define BOLD    "\033[1m"
#define DIM     "\033[2m"
#define RESET   "\033[0m"

// ========================================
// EMOJIS
// ========================================
#define EMOJI_SYSTEM    "📱"
#define EMOJI_MONITOR   "📊"
#define EMOJI_NETWORK   "🌐"
#define EMOJI_POWER     "🔋"
#define EMOJI_PACKAGE   "📦"
#define EMOJI_CLEANER   "🧹"
#define EMOJI_STORAGE   "💾"
#define EMOJI_SECURITY  "🔐"
#define EMOJI_FILES     "📂"
#define EMOJI_UTILITY   "🔑"
#define EMOJI_INTERNET  "🌤️"
#define EMOJI_BACKUP    "💾"
#define EMOJI_SETTINGS  "⚙️"
#define EMOJI_HELP      "❓"
#define EMOJI_CHECK     "✅"
#define EMOJI_WARNING   "⚠️"
#define EMOJI_ERROR     "❌"
#define EMOJI_INFO      "ℹ️"
#define EMOJI_ROCKET    "🚀"
#define EMOJI_FASTFETCH "⚡"

// ========================================
// FUNCTION PROTOTYPES
// ========================================
void update_terminal_size();
void print_banner();
void quick_setup();
void print_box(char *title, char *content);
void print_menu(char *title, char **items, int count);
void show_success(char *msg);
void show_warning(char *msg);
void show_error(char *msg);
void show_info(char *msg);
int confirm_action(char *prompt);
void show_fastfetch();
void show_system_info();
void show_hardware_info();
void show_cpu_info();
void show_battery();
void show_ram_info();
void show_network_info();
void show_wifi();
void internet_test();
void package_menu();
void cleaner_menu();
void storage_menu();
void utility_menu();
void internet_menu();
void security_menu();
void backup_menu();
void help_menu();
void main_menu();

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

void print_banner() {
    printf("%s%s\n", BOLD, CYAN);
    printf("  _____     _    _  _____ _  __ \n");
    printf(" / ____|   | |  | |/ ____| |/ / \n");
    printf("| (___   __| |  | | (___ | ' /  \n");
    printf(" \\___ \\ / _` |  | |\\___ \\|  <   \n");
    printf(" ____) | (_| |__| |____) | . \\  \n");
    printf("|_____/ \\__,_\\____/|_____/|_|\\_\\ \n");
    printf("%s\n", RESET);
    printf("%sby AnshLabs716%s\n", BOLD, RESET);
    printf("%sTermux Optimized v%s%s\n", DIM, VERSION, RESET);
}

void quick_setup() {
    system("clear");
    print_banner();
    printf("\n");
    printf("%s%s Termux detected!%s\n", GREEN, EMOJI_CHECK, RESET);
    printf("%s%s Press any key to continue...%s\n", BLUE, EMOJI_INFO, RESET);
    getchar();
}

void print_box(char *title, char *content) {
    update_terminal_size();
    int max_width = TERMINAL_WIDTH - 4;
    if (max_width > 50) max_width = 50;
    if (max_width < 20) max_width = 20;

    char processed[16384] = "";
    char *line = strtok(content, "\n");
    while (line != NULL) {
        strcat(processed, line);
        strcat(processed, "\n");
        line = strtok(NULL, "\n");
    }

    int len = strlen(processed);
    if (len > 0 && processed[len-1] == '\n') {
        processed[len-1] = '\0';
    }

    printf("%s┌", BOLD);
    for (int i = 0; i < max_width; i++) printf("─");
    printf("┐%s\n", RESET);

    if (title != NULL && strlen(title) > 0) {
        printf("%s│%s ", BOLD, RESET);
        printf("%s%s%s", BOLD, WHITE, title);
        int title_len = strlen(title);
        int pad = max_width - title_len - 1;
        if (pad < 0) pad = 0;
        for (int i = 0; i < pad; i++) printf(" ");
        printf("%s│%s\n", BOLD, RESET);
        printf("%s├", BOLD);
        for (int i = 0; i < max_width; i++) printf("─");
        printf("┤%s\n", RESET);
    }

    line = strtok(processed, "\n");
    while (line != NULL) {
        int line_len = strlen(line);
        printf("%s│%s ", BOLD, RESET);
        printf("%s", line);
        int pad = max_width - line_len - 1;
        if (pad < 0) pad = 0;
        for (int i = 0; i < pad; i++) printf(" ");
        printf("%s│%s\n", BOLD, RESET);
        line = strtok(NULL, "\n");
    }

    printf("%s└", BOLD);
    for (int i = 0; i < max_width; i++) printf("─");
    printf("┘%s\n", RESET);
}

void print_menu(char *title, char **items, int count) {
    char content[16384] = "";
    for (int i = 0; i < count; i++) {
        char line[1024];
        snprintf(line, sizeof(line), "%s%s%d.%s %s\n", BOLD, GREEN, i+1, RESET, items[i]);
        strcat(content, line);
    }
    strcat(content, "\n");
    strcat(content, DIM);
    strcat(content, "Type number. 0 to exit.");
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

// ========================================
// FASTFETCH
// ========================================
void show_fastfetch() {
    if (system("command -v fastfetch > /dev/null 2>&1") == 0) {
        system("fastfetch");
    } else {
        show_warning("Fastfetch not installed");
        if (confirm_action("Install fastfetch?")) {
            system("pkg install fastfetch -y 2>/dev/null");
            if (system("command -v fastfetch > /dev/null 2>&1") == 0) {
                system("fastfetch");
            } else {
                show_error("Fastfetch installation failed");
            }
        }
    }
    printf("Press Enter to continue...");
    getchar();
}

// ========================================
// SYSTEM INFO
// ========================================
void show_system_info() {
    char info[4096] = "";
    char hostname[256];
    gethostname(hostname, sizeof(hostname));
    struct utsname uname_data;
    uname(&uname_data);

    char android_version[64] = "Unknown";
    FILE *fp = popen("getprop ro.build.version.release 2>/dev/null", "r");
    if (fp) {
        fgets(android_version, sizeof(android_version), fp);
        pclose(fp);
        android_version[strcspn(android_version, "\n")] = 0;
    }

    char storage[128] = "Unknown";
    fp = popen("df -h /data 2>/dev/null | awk 'NR==2{print $2\" total \" $3\" used\"}'", "r");
    if (fp) {
        fgets(storage, sizeof(storage), fp);
        pclose(fp);
        storage[strcspn(storage, "\n")] = 0;
    }

    char ram[128] = "Unknown";
    fp = popen("free -h 2>/dev/null | awk 'NR==2{print $2\" total \" $3\" used\"}'", "r");
    if (fp) {
        fgets(ram, sizeof(ram), fp);
        pclose(fp);
        ram[strcspn(ram, "\n")] = 0;
    }

    char uptime_str[128] = "Unknown";
    fp = popen("uptime -p 2>/dev/null", "r");
    if (fp) {
        fgets(uptime_str, sizeof(uptime_str), fp);
        pclose(fp);
        uptime_str[strcspn(uptime_str, "\n")] = 0;
    }

    snprintf(info, sizeof(info),
        "Hostname: %s\n"
        "OS: Termux\n"
        "Android: %s\n"
        "Kernel: %s\n"
        "Arch: %s\n"
        "Uptime: %s\n"
        "Storage: %s\n"
        "RAM: %s",
        hostname,
        android_version,
        uname_data.release,
        uname_data.machine,
        uptime_str,
        storage,
        ram
    );
    print_box("System Info", info);
}

// ========================================
// HARDWARE INFO
// ========================================
void show_hardware_info() {
    char info[2048] = "";
    char device[128] = "Unknown";
    char manufacturer[128] = "Unknown";
    char android_ver[64] = "Unknown";
    char sdk[32] = "Unknown";

    FILE *fp = popen("getprop ro.product.model 2>/dev/null", "r");
    if (fp) {
        fgets(device, sizeof(device), fp);
        pclose(fp);
        device[strcspn(device, "\n")] = 0;
    }

    fp = popen("getprop ro.product.manufacturer 2>/dev/null", "r");
    if (fp) {
        fgets(manufacturer, sizeof(manufacturer), fp);
        pclose(fp);
        manufacturer[strcspn(manufacturer, "\n")] = 0;
    }

    fp = popen("getprop ro.build.version.release 2>/dev/null", "r");
    if (fp) {
        fgets(android_ver, sizeof(android_ver), fp);
        pclose(fp);
        android_ver[strcspn(android_ver, "\n")] = 0;
    }

    fp = popen("getprop ro.build.version.sdk 2>/dev/null", "r");
    if (fp) {
        fgets(sdk, sizeof(sdk), fp);
        pclose(fp);
        sdk[strcspn(sdk, "\n")] = 0;
    }

    char cores[32];
    fp = popen("nproc 2>/dev/null", "r");
    if (fp) {
        fgets(cores, sizeof(cores), fp);
        pclose(fp);
        cores[strcspn(cores, "\n")] = 0;
    } else {
        strcpy(cores, "Unknown");
    }

    struct utsname uname_data;
    uname(&uname_data);

    snprintf(info, sizeof(info),
        "Device: %s\n"
        "Manufacturer: %s\n"
        "Android: %s\n"
        "SDK: %s\n"
        "CPU: %s\n"
        "Cores: %s",
        device, manufacturer, android_ver, sdk, uname_data.machine, cores
    );
    print_box("Hardware Info", info);
}

// ========================================
// CPU INFO
// ========================================
void show_cpu_info() {
    char info[2048] = "";
    struct utsname uname_data;
    uname(&uname_data);

    char cores[32];
    FILE *fp = popen("nproc 2>/dev/null", "r");
    if (fp) {
        fgets(cores, sizeof(cores), fp);
        pclose(fp);
        cores[strcspn(cores, "\n")] = 0;
    } else {
        strcpy(cores, "Unknown");
    }

    char cpu_hw[256] = "Unknown";
    fp = popen("cat /proc/cpuinfo 2>/dev/null | grep 'Hardware' | head -1 | cut -d: -f2 | xargs", "r");
    if (fp) {
        fgets(cpu_hw, sizeof(cpu_hw), fp);
        pclose(fp);
        cpu_hw[strcspn(cpu_hw, "\n")] = 0;
    }

    char features[256] = "Unknown";
    fp = popen("cat /proc/cpuinfo 2>/dev/null | grep 'Features' | head -1 | cut -d: -f2 | xargs | cut -c1-50", "r");
    if (fp) {
        fgets(features, sizeof(features), fp);
        pclose(fp);
        features[strcspn(features, "\n")] = 0;
    }

    snprintf(info, sizeof(info),
        "Architecture: %s\n"
        "Cores: %s\n"
        "CPU Info: %s\n"
        "Features: %s...",
        uname_data.machine, cores, cpu_hw, features
    );
    print_box("CPU Info", info);
}

// ========================================
// BATTERY
// ========================================
void show_battery() {
    char info[2048] = "";
    if (system("command -v termux-battery-status > /dev/null 2>&1") == 0) {
        FILE *fp = popen("termux-battery-status 2>/dev/null", "r");
        if (fp) {
            char buffer[4096];
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else {
        strcpy(info, "Install termux-api for battery info");
        if (confirm_action("Install termux-api?")) {
            system("pkg install termux-api -y 2>/dev/null");
            if (system("command -v termux-battery-status > /dev/null 2>&1") == 0) {
                show_battery();
                return;
            }
        }
    }
    print_box("Battery", info);
}

// ========================================
// RAM INFO
// ========================================
void show_ram_info() {
    char info[1024] = "";
    if (system("command -v free > /dev/null 2>&1") == 0) {
        FILE *fp = popen("free -h 2>/dev/null", "r");
        if (fp) {
            char lines[1024];
            fread(lines, 1, sizeof(lines) - 1, fp);
            pclose(fp);
            char total[32], used[32], free_mem[32];
            sscanf(lines, "Mem: %s %s %s", total, used, free_mem);
            snprintf(info, sizeof(info),
                "Total: %s\n"
                "Used: %s\n"
                "Free: %s",
                total, used, free_mem
            );
        }
    } else {
        strcpy(info, "free command not available");
    }
    print_box("RAM Info", info);
}

// ========================================
// NETWORK INFO
// ========================================
void show_network_info() {
    char info[1024] = "";
    char ip[128] = "Unknown";
    char gateway[128] = "Unknown";

    FILE *fp = popen("ip addr show 2>/dev/null | grep 'inet ' | grep -v '127.0.0.1' | awk '{print $2}' | head -1", "r");
    if (fp) {
        fgets(ip, sizeof(ip), fp);
        pclose(fp);
        ip[strcspn(ip, "\n")] = 0;
    }

    fp = popen("ip route 2>/dev/null | grep default | awk '{print $3}'", "r");
    if (fp) {
        fgets(gateway, sizeof(gateway), fp);
        pclose(fp);
        gateway[strcspn(gateway, "\n")] = 0;
    }

    snprintf(info, sizeof(info), "IP: %s\nGateway: %s", ip, gateway);
    print_box("Network Info", info);
}

// ========================================
// WIFI
// ========================================
void show_wifi() {
    char info[4096] = "";
    if (system("command -v termux-wifi-connectioninfo > /dev/null 2>&1") == 0) {
        FILE *fp = popen("termux-wifi-connectioninfo 2>/dev/null", "r");
        if (fp) {
            fread(info, 1, sizeof(info) - 1, fp);
            pclose(fp);
        }
    } else {
        strcpy(info, "Install termux-api for Wi-Fi info");
        if (confirm_action("Install termux-api?")) {
            system("pkg install termux-api -y 2>/dev/null");
            if (system("command -v termux-wifi-connectioninfo > /dev/null 2>&1") == 0) {
                show_wifi();
                return;
            }
        }
    }
    print_box("Wi-Fi", info);
}

// ========================================
// INTERNET TEST
// ========================================
void internet_test() {
    char info[1024] = "";
    show_info("Testing internet connection...");
    if (system("ping -c 1 -W 2 8.8.8.8 > /dev/null 2>&1") == 0) {
        strcat(info, "Internet: ✅ Connected\n");
        char ip[128];
        FILE *fp = popen("curl -s ifconfig.me 2>/dev/null", "r");
        if (fp) {
            fgets(ip, sizeof(ip), fp);
            pclose(fp);
            ip[strcspn(ip, "\n")] = 0;
            strcat(info, "Public IP: ");
            strcat(info, ip);
        } else {
            strcat(info, "Public IP: N/A");
        }
    } else {
        strcat(info, "Internet: ❌ Disconnected");
    }
    print_box("Internet Test", info);
}

// ========================================
// PACKAGE MENU
// ========================================
void package_menu() {
    while (1) {
        system("clear");
        printf("%s%s%s%s\n", BOLD, CYAN, EMOJI_PACKAGE, RESET);
        printf("%s Package Manager%s\n", BOLD, RESET);
        printf("\n");

        char *items[] = {"Update", "Upgrade", "Clean cache", "Autoremove", "List installed", "Search", "Back"};
        print_menu("Packages", items, 7);
        printf("Select: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1:
                show_info("Updating...");
                system("pkg update 2>/dev/null");
                show_success("Done");
                break;
            case 2:
                show_info("Upgrading...");
                system("pkg upgrade -y 2>/dev/null");
                show_success("Done");
                break;
            case 3:
                show_info("Cleaning...");
                system("pkg clean 2>/dev/null");
                show_success("Done");
                break;
            case 4:
                show_info("Autoremoving...");
                system("pkg autoclean 2>/dev/null");
                show_success("Done");
                break;
            case 5:
                system("pkg list-installed 2>/dev/null");
                break;
            case 6: {
                char term[256];
                printf("Search: ");
                fgets(term, sizeof(term), stdin);
                term[strcspn(term, "\n")] = 0;
                char cmd[512];
                snprintf(cmd, sizeof(cmd), "pkg search %s 2>/dev/null", term);
                system(cmd);
                break;
            }
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
// CLEANER MENU
// ========================================
void cleaner_menu() {
    while (1) {
        system("clear");
        printf("%s%s%s%s\n", BOLD, CYAN, EMOJI_CLEANER, RESET);
        printf("%s Cleaner%s\n", BOLD, RESET);
        printf("\n");

        char *items[] = {"User cache", "Temp files", "Package cache", "Trash", "Back"};
        print_menu("Cleaner", items, 5);
        printf("Select: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1:
                if (confirm_action("Clean cache?")) {
                    system("rm -rf ~/.cache/* 2>/dev/null");
                    show_success("Cache cleaned");
                }
                break;
            case 2:
                if (confirm_action("Clean temp?")) {
                    system("rm -rf /tmp/* 2>/dev/null");
                    show_success("Temp cleaned");
                }
                break;
            case 3:
                if (confirm_action("Clean package cache?")) {
                    system("pkg clean 2>/dev/null");
                    show_success("Package cache cleaned");
                }
                break;
            case 4:
                if (access("/.local/share/Trash", F_OK) == 0) {
                    if (confirm_action("Empty trash?")) {
                        system("rm -rf ~/.local/share/Trash/* 2>/dev/null");
                        show_success("Trash emptied");
                    }
                } else {
                    show_warning("No trash found");
                }
                break;
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

// ========================================
// STORAGE MENU
// ========================================
void storage_menu() {
    while (1) {
        system("clear");
        printf("%s%s%s%s\n", BOLD, CYAN, EMOJI_STORAGE, RESET);
        printf("%s Storage%s\n", BOLD, RESET);
        printf("\n");

        char *items[] = {"Disk usage", "Largest directories", "Largest files", "Mounted drives", "Back"};
        print_menu("Storage", items, 5);
        printf("Select: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1:
                system("clear");
                printf("%s%sDisk Usage%s\n", BOLD, CYAN, RESET);
                system("df -h 2>/dev/null");
                break;
            case 2:
                system("clear");
                printf("%s%sLargest Directories%s\n", BOLD, CYAN, RESET);
                system("du -sh ~/* 2>/dev/null | sort -hr | head -10");
                break;
            case 3:
                system("clear");
                printf("%s%sLargest Files%s\n", BOLD, CYAN, RESET);
                system("find ~ -type f -exec du -h {} + 2>/dev/null | sort -hr | head -10");
                break;
            case 4:
                system("clear");
                printf("%s%sMounted Drives%s\n", BOLD, CYAN, RESET);
                system("mount 2>/dev/null | grep '/data' || mount 2>/dev/null | head -10");
                break;
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

// ========================================
// UTILITY MENU
// ========================================
void utility_menu() {
    while (1) {
        system("clear");
        printf("%s%s%s%s\n", BOLD, CYAN, EMOJI_UTILITY, RESET);
        printf("%s Utilities%s\n", BOLD, RESET);
        printf("\n");

        char *items[] = {"Password Generator", "UUID Generator", "Hash (MD5/SHA)", "Random String", "Back"};
        print_menu("Utilities", items, 5);
        printf("Select: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: {
                char len_str[16];
                int length = 16;
                printf("Length [16]: ");
                fgets(len_str, sizeof(len_str), stdin);
                len_str[strcspn(len_str, "\n")] = 0;
                if (strlen(len_str) > 0) {
                    length = atoi(len_str);
                    if (length <= 0) length = 16;
                }
                char cmd[512];
                snprintf(cmd, sizeof(cmd), "tr -dc 'A-Za-z0-9!@#%%^&*' < /dev/urandom 2>/dev/null | head -c %d", length);
                char pw[128];
                FILE *fp = popen(cmd, "r");
                if (fp) {
                    fgets(pw, sizeof(pw), fp);
                    pclose(fp);
                    pw[strcspn(pw, "\n")] = 0;
                    print_box("Password", pw);
                }
                break;
            }
            case 2: {
                char uuid[64] = "";
                if (system("command -v uuidgen > /dev/null 2>&1") == 0) {
                    FILE *fp = popen("uuidgen 2>/dev/null", "r");
                    if (fp) {
                        fgets(uuid, sizeof(uuid), fp);
                        pclose(fp);
                        uuid[strcspn(uuid, "\n")] = 0;
                    }
                } else {
                    FILE *fp = fopen("/proc/sys/kernel/random/uuid", "r");
                    if (fp) {
                        fgets(uuid, sizeof(uuid), fp);
                        fclose(fp);
                        uuid[strcspn(uuid, "\n")] = 0;
                    } else {
                        strcpy(uuid, "N/A");
                    }
                }
                print_box("UUID", uuid);
                break;
            }
            case 3: {
                char text[1024];
                printf("Text to hash: ");
                fgets(text, sizeof(text), stdin);
                text[strcspn(text, "\n")] = 0;
                char md5[128], sha1[128], sha256[128];
                FILE *fp = popen("echo -n \"%s\" | md5sum 2>/dev/null | awk '{print $1}'", "r");
                if (fp) {
                    fgets(md5, sizeof(md5), fp);
                    pclose(fp);
                    md5[strcspn(md5, "\n")] = 0;
                }
                fp = popen("echo -n \"%s\" | sha1sum 2>/dev/null | awk '{print $1}'", "r");
                if (fp) {
                    fgets(sha1, sizeof(sha1), fp);
                    pclose(fp);
                    sha1[strcspn(sha1, "\n")] = 0;
                }
                fp = popen("echo -n \"%s\" | sha256sum 2>/dev/null | awk '{print $1}'", "r");
                if (fp) {
                    fgets(sha256, sizeof(sha256), fp);
                    pclose(fp);
                    sha256[strcspn(sha256, "\n")] = 0;
                }
                char hash_info[1024];
                snprintf(hash_info, sizeof(hash_info), "MD5: %s\nSHA1: %s\nSHA256: %s", md5, sha1, sha256);
                print_box("Hash", hash_info);
                break;
            }
            case 4: {
                char len_str[16];
                int length = 16;
                printf("Length [16]: ");
                fgets(len_str, sizeof(len_str), stdin);
                len_str[strcspn(len_str, "\n")] = 0;
                if (strlen(len_str) > 0) {
                    length = atoi(len_str);
                    if (length <= 0) length = 16;
                }
                char cmd[512];
                snprintf(cmd, sizeof(cmd), "tr -dc 'A-Za-z0-9' < /dev/urandom 2>/dev/null | head -c %d", length);
                char rand_str[128];
                FILE *fp = popen(cmd, "r");
                if (fp) {
                    fgets(rand_str, sizeof(rand_str), fp);
                    pclose(fp);
                    rand_str[strcspn(rand_str, "\n")] = 0;
                    print_box("Random String", rand_str);
                }
                break;
            }
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

// ========================================
// INTERNET MENU
// ========================================
void internet_menu() {
    while (1) {
        system("clear");
        printf("%s%s%s%s\n", BOLD, CYAN, EMOJI_INTERNET, RESET);
        printf("%s Internet%s\n", BOLD, RESET);
        printf("\n");

        char *items[] = {"Weather", "Time", "Calendar", "Internet Test", "Public IP", "Back"};
        print_menu("Internet", items, 6);
        printf("Select: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: {
                char city[128];
                printf("City [London]: ");
                fgets(city, sizeof(city), stdin);
                city[strcspn(city, "\n")] = 0;
                if (strlen(city) == 0) strcpy(city, "London");
                char cmd[512];
                snprintf(cmd, sizeof(cmd), "curl -s \"wttr.in/%s?format=%%c+%%t+%%w\" 2>/dev/null", city);
                char weather[512];
                FILE *fp = popen(cmd, "r");
                if (fp) {
                    fgets(weather, sizeof(weather), fp);
                    pclose(fp);
                    weather[strcspn(weather, "\n")] = 0;
                    char info[1024];
                    snprintf(info, sizeof(info), "Weather for %s:\n%s", city, weather);
                    print_box("Weather", info);
                }
                break;
            }
            case 2: {
                char info[512];
                time_t now = time(NULL);
                struct tm *tm_info = localtime(&now);
                char date_str[128], time_str[128], tz_str[64];
                strftime(date_str, sizeof(date_str), "%A, %B %d, %Y", tm_info);
                strftime(time_str, sizeof(time_str), "%H:%M:%S", tm_info);
                strftime(tz_str, sizeof(tz_str), "%Z", tm_info);
                snprintf(info, sizeof(info), "Date: %s\nTime: %s\nTimezone: %s", date_str, time_str, tz_str);
                print_box("Time", info);
                break;
            }
            case 3: {
                char cal[2048] = "";
                FILE *fp = popen("cal -3 2>/dev/null", "r");
                if (fp) {
                    fread(cal, 1, sizeof(cal) - 1, fp);
                    pclose(fp);
                } else {
                    strcpy(cal, "Calendar not available");
                }
                print_box("Calendar", cal);
                break;
            }
            case 4:
                internet_test();
                break;
            case 5: {
                char ip[128] = "N/A";
                FILE *fp = popen("curl -s ifconfig.me 2>/dev/null", "r");
                if (fp) {
                    fgets(ip, sizeof(ip), fp);
                    pclose(fp);
                    ip[strcspn(ip, "\n")] = 0;
                }
                print_box("Public IP", ip);
                break;
            }
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

// ========================================
// SECURITY MENU
// ========================================
void security_menu() {
    while (1) {
        system("clear");
        printf("%s%s%s%s\n", BOLD, CYAN, EMOJI_SECURITY, RESET);
        printf("%s Security%s\n", BOLD, RESET);
        printf("\n");

        char *items[] = {"Open ports", "Running processes", "Security tips", "Back"};
        print_menu("Security", items, 4);
        printf("Select: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1:
                system("clear");
                printf("%s%sOpen Ports%s\n", BOLD, CYAN, RESET);
                system("netstat -tuln 2>/dev/null | grep LISTEN | head -10 || echo 'No open ports found'");
                break;
            case 2:
                system("clear");
                printf("%s%sRunning Processes%s\n", BOLD, CYAN, RESET);
                system("ps aux 2>/dev/null | head -15");
                break;
            case 3:
                print_box("Security Tips", "1. Keep Termux updated\n2. Use strong passwords\n3. Don't run as root\n4. Check permissions\n5. Use VPN\n6. Install termux-api for security features");
                break;
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

// ========================================
// BACKUP MENU
// ========================================
void backup_menu() {
    while (1) {
        system("clear");
        printf("%s%s%s%s\n", BOLD, CYAN, EMOJI_BACKUP, RESET);
        printf("%s Backup%s\n", BOLD, RESET);
        printf("\n");

        char *items[] = {"Backup folder", "Restore backup", "Back"};
        print_menu("Backup", items, 3);
        printf("Select: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: {
                char folder[512];
                printf("Folder to backup: ");
                fgets(folder, sizeof(folder), stdin);
                folder[strcspn(folder, "\n")] = 0;
                if (access(folder, F_OK) == 0) {
                    char backup_name[64];
                    time_t now = time(NULL);
                    struct tm *tm_info = localtime(&now);
                    strftime(backup_name, sizeof(backup_name), "backup_%Y%m%d.tar.gz", tm_info);
                    show_info("Creating backup...");
                    char cmd[1024];
                    snprintf(cmd, sizeof(cmd), "tar -czf \"%s/%s\" \"%s\" 2>/dev/null", getenv("HOME"), backup_name, folder);
                    system(cmd);
                    char msg[512];
                    snprintf(msg, sizeof(msg), "Backup saved: %s/%s", getenv("HOME"), backup_name);
                    show_success(msg);
                } else {
                    show_error("Folder not found");
                }
                break;
            }
            case 2: {
                system("ls -la ~/backup_*.tar.gz 2>/dev/null || echo 'No backups found'");
                char backup_file[512];
                printf("Enter backup name to restore: ");
                fgets(backup_file, sizeof(backup_file), stdin);
                backup_file[strcspn(backup_file, "\n")] = 0;
                char full_path[512];
                snprintf(full_path, sizeof(full_path), "%s/%s", getenv("HOME"), backup_file);
                if (access(full_path, F_OK) == 0) {
                    char restore_dir[512];
                    printf("Restore to: ");
                    fgets(restore_dir, sizeof(restore_dir), stdin);
                    restore_dir[strcspn(restore_dir, "\n")] = 0;
                    mkdir(restore_dir, 0755);
                    char cmd[1024];
                    snprintf(cmd, sizeof(cmd), "tar -xzf \"%s\" -C \"%s\" 2>/dev/null", full_path, restore_dir);
                    system(cmd);
                    show_success("Restored");
                } else {
                    show_error("Backup not found");
                }
                break;
            }
            case 0:
            case 3:
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
// HELP MENU
// ========================================
void help_menu() {
    while (1) {
        system("clear");
        printf("%s%s%s%s\n", BOLD, CYAN, EMOJI_HELP, RESET);
        printf("%s Help%s\n", BOLD, RESET);
        printf("\n");

        char *items[] = {"About", "Supported", "Version", "Back"};
        print_menu("Help", items, 4);
        printf("Select: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1:
                print_box("About", "SysKit - Termux Edition\nBy AnshLabs716\nUnix Toolkit for Termux\n\nFeatures:\n- System Info\n- Battery\n- Wi-Fi\n- Package Manager\n- Cleaner\n- Utilities\n- Internet Tools");
                break;
            case 2:
                print_box("Supported", "Termux\nAndroid 5.0+\npkg package manager\nARM/ARM64/x86_64");
                break;
            case 3: {
                char ver[64];
                snprintf(ver, sizeof(ver), "SysKit v%s", VERSION);
                print_box("Version", ver);
                break;
            }
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

// ========================================
// MAIN MENU
// ========================================
void main_menu() {
    while (1) {
        system("clear");
        print_banner();
        printf("\n");

        char *items[] = {
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
        };
        print_menu("SysKit - Termux", items, 17);
        printf("\n");
        printf("Select option: ");
        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1: show_system_info(); printf("Press Enter to continue..."); getchar(); break;
            case 2: show_hardware_info(); printf("Press Enter to continue..."); getchar(); break;
            case 3: show_cpu_info(); printf("Press Enter to continue..."); getchar(); break;
            case 4: show_ram_info(); printf("Press Enter to continue..."); getchar(); break;
            case 5: show_battery(); printf("Press Enter to continue..."); getchar(); break;
            case 6: show_network_info(); printf("Press Enter to continue..."); getchar(); break;
            case 7: show_wifi(); printf("Press Enter to continue..."); getchar(); break;
            case 8: show_fastfetch(); break;
            case 9: package_menu(); break;
            case 10: cleaner_menu(); break;
            case 11: storage_menu(); break;
            case 12: utility_menu(); break;
            case 13: internet_menu(); break;
            case 14: security_menu(); break;
            case 15: backup_menu(); break;
            case 16: help_menu(); break;
            case 0:
            case 17:
                printf("%s%s Goodbye!%s\n", GREEN, EMOJI_CHECK, RESET);
                exit(0);
            default:
                show_error("Invalid option");
                sleep(1);
                break;
        }
    }
}

// ========================================
// MAIN
// ========================================
int main(int argc, char *argv[]) {
    update_terminal_size();
    quick_setup();
    main_menu();
    return 0;
}
