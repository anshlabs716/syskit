import java.io.*;
import java.lang.management.*;
import java.net.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.regex.*;

/**
 * SysKit - Universal Unix Toolkit
 * Java translation of syskit.c v2.0.1 / Python syskit.py
 */
public class Syskit {
    // ========================================
    // GLOBAL CONFIG
    // ========================================
    public static final String VERSION = "2.0.1";
    public static final String AUTHOR = "AnshLabs716";
    public static final String SCRIPT_NAME = "Syskit.java";
    public static final String CONFIG_DIR = ".config/syskit";
    public static final String CONFIG_FILE = "config.conf";
    public static final String FIRST_RUN_FILE = "first_run_complete";
    public static final String LOG_FILE = "syskit.log";

    public static boolean IS_ROOT = false;
    public static boolean COLORS_SUPPORTED = true;
    public static boolean USE_EMOJIS = true;
    public static boolean USE_ANIMATIONS = true;
    public static boolean IS_FULLSCREEN = false;
    public static int TERMINAL_WIDTH = 80;
    public static int TERMINAL_HEIGHT = 24;
    public static String OS_NAME = "Unknown";
    public static String DISTRO = "Unknown";
    public static String DISTRO_VERSION = "Unknown";
    public static String PKG_MANAGER = "unknown";
    public static String PKG_UPDATE = "echo 'Unknown'";
    public static String PKG_UPGRADE = "echo 'Unknown'";
    public static String PKG_INSTALL = "echo 'Unknown'";
    public static String PKG_REMOVE = "echo 'Unknown'";
    public static String PKG_SEARCH = "echo 'Unknown'";
    public static String PKG_LIST = "echo 'Unknown'";
    public static String PKG_CLEAN = "echo 'Unknown'";
    public static String PKG_AUTOREMOVE = "echo 'Unknown'";
    public static String PKG_INSTALL_CMD = "echo 'Unknown'";
    public static String ROOT_INDICATOR = "User";
    public static String HOME_DIR = System.getProperty("user.home");
    public static String USER_NAME = System.getProperty("user.name");
    public static String CONFIG_PATH = "";
    public static String LOG_PATH = "";

    // ========================================
    // COLORS
    // ========================================
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String MAGENTA = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";
    public static final String BOLD = "\033[1m";
    public static final String DIM = "\033[2m";
    public static final String RESET = "\033[0m";
    public static final String BG_RED = "\033[41m";
    public static final String BG_GREEN = "\033[42m";
    public static final String BG_YELLOW = "\033[43m";
    public static final String BG_BLUE = "\033[44m";
    public static final String BG_CYAN = "\033[46m";
    public static final String BG_WHITE = "\033[47m";

    // ========================================
    // EMOJIS
    // ========================================
    public static final String EMOJI_SYSTEM = "🖥️";
    public static final String EMOJI_MONITOR = "📊";
    public static final String EMOJI_NETWORK = "🌐";
    public static final String EMOJI_POWER = "🔋";
    public static final String EMOJI_PACKAGE = "📦";
    public static final String EMOJI_CLEANER = "🧹";
    public static final String EMOJI_STORAGE = "💾";
    public static final String EMOJI_SECURITY = "🔐";
    public static final String EMOJI_FILES = "📂";
    public static final String EMOJI_ARCHIVE = "🗜️";
    public static final String EMOJI_UTILITY = "🔑";
    public static final String EMOJI_INTERNET = "🌤️";
    public static final String EMOJI_BACKUP = "💾";
    public static final String EMOJI_SETTINGS = "⚙️";
    public static final String EMOJI_HELP = "❓";
    public static final String EMOJI_CHECK = "✅";
    public static final String EMOJI_WARNING = "⚠️";
    public static final String EMOJI_ERROR = "❌";
    public static final String EMOJI_INFO = "ℹ️";
    public static final String EMOJI_STAR = "⭐";
    public static final String EMOJI_ROCKET = "🚀";
    public static final String EMOJI_GEAR = "⚙️";
    public static final String EMOJI_SHIELD = "🛡️";
    public static final String EMOJI_LOCK = "🔒";
    public static final String EMOJI_HOURGLASS = "⏳";

    // ========================================
    // UTILITY FUNCTIONS
    // ========================================
    public static void updateTerminalSize()
    {
        // Java cannot easily get terminal size; use fallback
        try {
            // Try using stty if available
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", "stty size");
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.trim().split(" ");
                if (parts.length == 2) {
                    TERMINAL_HEIGHT = Integer.parseInt(parts[0]);
                    TERMINAL_WIDTH = Integer.parseInt(parts[1]);
                }
            }
        } catch (Exception e) {
            // fallback defaults
            TERMINAL_WIDTH = 80;
            TERMINAL_HEIGHT = 24;
        }
    }

    public static String stripAnsi(String text)
    {
        return text.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    public static void printHeader()
    {
        clearScreen();
        updateTerminalSize();
        System.out.println(BOLD + CYAN);
        System.out.println("  _____     _    _  _____ _  __ ");
        System.out.println(" / ____|   | |  | |/ ____| |/ / ");
        System.out.println("| (___   __| |  | | (___ | ' /  ");
        System.out.println(" \\___ \\ / _` |  | |\\___ \\|  <   ");
        System.out.println(" ____) | (_| |__| |____) | . \\  ");
        System.out.println("|_____/ \\__,_\\____/|_____/|_|\\_\\ ");
        System.out.println(RESET);
        if (IS_ROOT) {
            System.out.println(BOLD + "by AnshLabs716" + RESET + " " + BG_RED + WHITE + " ROOT " + RESET);
        } else {
            System.out.println(BOLD + "by AnshLabs716" + RESET + " " + DIM + "User" + RESET);
        }
        System.out.println(DIM + "Version: " + VERSION + " | " + OS_NAME + " " + DISTRO + RESET);
        if (IS_FULLSCREEN) {
            System.out.println(DIM + "└─ Fullscreen mode detected " + RESET);
        }
        System.out.println();
    }

    public static void printBox(String title, String content)
    {
        updateTerminalSize();
        int maxWidth = TERMINAL_WIDTH - 4;
        if (maxWidth > 80)
            maxWidth = 80;
        if (maxWidth < 40)
            maxWidth = 40;

        // Split content into lines, wrap if needed
        String[] lines = content.split("\n");
        List<String> processed = new ArrayList<>();
        for (String line : lines) {
            String cleanLine = stripAnsi(line);
            int lineLen = cleanLine.length();
            if (lineLen <= maxWidth) {
                processed.add(line);
            } else {
                // wrap line
                String[] words = line.split(" ");
                StringBuilder cur = new StringBuilder();
                for (String w : words) {
                    if (cur.length() + w.length() + 1 <= maxWidth) {
                        if (cur.length() > 0)
                            cur.append(" ");
                        cur.append(w);
                    } else {
                        if (cur.length() > 0)
                            processed.add(cur.toString());
                        cur = new StringBuilder(w);
                    }
                }
                if (cur.length() > 0)
                    processed.add(cur.toString());
            }
        }
        // remove trailing empty lines
        while (!processed.isEmpty() && processed.get(processed.size() - 1).isEmpty()) {
            processed.remove(processed.size() - 1);
        }

        // Top border
        System.out.println(BOLD + "┌"
            + "─".repeat(maxWidth) + "┐" + RESET);

        // Title
        if (title != null && !title.isEmpty()) {
            String cleanTitle = stripAnsi(title);
            int titleLen = cleanTitle.length();
            System.out.print(BOLD + "│" + RESET + " ");
            System.out.print(BOLD + WHITE + title + RESET);
            int pad = maxWidth - titleLen - 1;
            if (pad < 0)
                pad = 0;
            System.out.print(" ".repeat(pad));
            System.out.println(BOLD + "│" + RESET);
            System.out.println(BOLD + "├"
                + "─".repeat(maxWidth) + "┤" + RESET);
        }

        for (String line : processed) {
            String cleanLine = stripAnsi(line);
            int lineLen = cleanLine.length();
            System.out.print(BOLD + "│" + RESET + " ");
            System.out.print(line);
            int pad = maxWidth - lineLen - 1;
            if (pad < 0)
                pad = 0;
            System.out.print(" ".repeat(pad));
            System.out.println(BOLD + "│" + RESET);
        }

        // Bottom border
        System.out.println(BOLD + "└"
            + "─".repeat(maxWidth) + "┘" + RESET);
    }

    public static void printMenu(String title, String[] items)
    {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            if (item.contains("Back to") || item.contains("Exit") || item.contains("Back")) {
                content.append(BOLD).append(YELLOW).append(i + 1).append(".").append(RESET).append(" ").append(item).append("\n");
            } else {
                content.append(BOLD).append(GREEN).append(i + 1).append(".").append(RESET).append(" ").append(item).append("\n");
            }
        }
        content.append("\n");
        content.append(DIM).append("0. Go back/exit").append(RESET);
        printBox(title, content.toString());
    }

    public static void showSuccess(String msg)
    {
        System.out.println(GREEN + EMOJI_CHECK + " " + msg + RESET);
    }

    public static void showWarning(String msg)
    {
        System.out.println(YELLOW + EMOJI_WARNING + " " + msg + RESET);
    }

    public static void showError(String msg)
    {
        System.out.println(RED + EMOJI_ERROR + " " + msg + RESET);
    }

    public static void showInfo(String msg)
    {
        System.out.println(BLUE + EMOJI_INFO + " " + msg + RESET);
    }

    public static boolean confirmAction(String prompt)
    {
        System.out.println(YELLOW + EMOJI_WARNING + " " + prompt + RESET);
        System.out.print("Continue? [y/N] ");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }

    public static boolean checkCommand(String cmd)
    {
        try {
            ProcessBuilder pb = new ProcessBuilder("which", cmd);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            int exit = p.waitFor();
            return exit == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static void runWithSudo(String cmd)
    {
        if (IS_ROOT) {
            runCommand(cmd);
        } else {
            if (checkCommand("sudo")) {
                showWarning("This operation requires root privileges");
                if (confirmAction("Continue with sudo?")) {
                    runCommand("sudo bash -c \"" + cmd + "\"");
                }
            } else {
                showError("sudo not installed. Please run as root.");
                runCommand(cmd);
            }
        }
    }

    public static void autoInstallTool(String tool, String packageName)
    {
        if (checkCommand(tool))
            return;
        showWarning("Tool not installed");
        if (confirmAction("Install " + tool + " (package: " + packageName + ")?")) {
            showInfo("Installing package...");
            String cmd = "";
            if (PKG_MANAGER.equals("apt") || PKG_MANAGER.equals("pkg")) {
                cmd = "apt install -y " + packageName;
            } else if (PKG_MANAGER.equals("dnf")) {
                cmd = "dnf install -y " + packageName;
            } else if (PKG_MANAGER.equals("pacman")) {
                cmd = "pacman -S --noconfirm " + packageName;
            } else if (PKG_MANAGER.equals("zypper")) {
                cmd = "zypper install -y " + packageName;
            } else if (PKG_MANAGER.equals("brew")) {
                cmd = "brew install " + packageName;
                runCommand(cmd);
                if (checkCommand(tool)) {
                    showSuccess("Tool installed successfully");
                } else {
                    showError("Failed to install tool");
                }
                return;
            } else {
                showError("Cannot install package: Unsupported package manager");
                return;
            }
            runWithSudo(cmd);
            if (checkCommand(tool)) {
                showSuccess("Tool installed successfully");
            } else {
                showError("Failed to install tool");
            }
        }
    }

    public static void runCommand(String cmd)
    {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", cmd);
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            showError("Error running command: " + e.getMessage());
        }
    }

    public static String runCommandOutput(String cmd)
    {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", cmd);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line).append("\n");
            }
            p.waitFor();
            return output.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // ========================================
    // OS DETECTION
    // ========================================
    public static void detectOS()
    {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("linux")) {
            if (Files.exists(Paths.get("/data/data/com.termux"))) {
                OS_NAME = "Termux";
            } else {
                OS_NAME = "Linux";
            }
        } else if (os.contains("mac")) {
            OS_NAME = "macOS";
        } else if (os.contains("freebsd")) {
            OS_NAME = "FreeBSD";
        } else if (os.contains("openbsd")) {
            OS_NAME = "OpenBSD";
        } else if (os.contains("netbsd")) {
            OS_NAME = "NetBSD";
        } else {
            OS_NAME = "Unknown";
        }
    }

    public static void detectDistro()
    {
        if (OS_NAME.equals("Linux")) {
            Path osRelease = Paths.get("/etc/os-release");
            if (Files.exists(osRelease)) {
                try {
                    List<String> lines = Files.readAllLines(osRelease);
                    for (String line : lines) {
                        if (line.startsWith("ID=")) {
                            DISTRO = line.substring(3).replace("\"", "").trim();
                        } else if (line.startsWith("VERSION_ID=")) {
                            DISTRO_VERSION = line.substring(11).replace("\"", "").trim();
                        }
                    }
                } catch (IOException e) {
                    DISTRO = "unknown";
                    DISTRO_VERSION = "unknown";
                }
            } else {
                DISTRO = "unknown";
                DISTRO_VERSION = "unknown";
            }
        } else if (OS_NAME.equals("macOS")) {
            DISTRO = "macOS";
            try {
                ProcessBuilder pb = new ProcessBuilder("sw_vers", "-productVersion");
                Process p = pb.start();
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                DISTRO_VERSION = br.readLine().trim();
            } catch (Exception e) {
                DISTRO_VERSION = "unknown";
            }
        } else {
            DISTRO = OS_NAME;
            DISTRO_VERSION = "unknown";
        }
    }

    public static void detectPackageManager()
    {
        if (OS_NAME.equals("Linux")) {
            if (DISTRO.equals("ubuntu") || DISTRO.equals("debian") || DISTRO.equals("linuxmint") || DISTRO.equals("pop")) {
                PKG_MANAGER = "apt";
                PKG_UPDATE = "apt update";
                PKG_UPGRADE = "apt upgrade -y";
                PKG_INSTALL = "apt install -y";
                PKG_REMOVE = "apt remove -y";
                PKG_SEARCH = "apt search";
                PKG_LIST = "apt list --installed";
                PKG_CLEAN = "apt autoclean -y";
                PKG_AUTOREMOVE = "apt autoremove -y";
                PKG_INSTALL_CMD = "apt install -y";
            } else if (DISTRO.equals("fedora") || DISTRO.equals("rhel") || DISTRO.equals("centos") || DISTRO.equals("rocky") || DISTRO.equals("almalinux")) {
                if (checkCommand("dnf")) {
                    PKG_MANAGER = "dnf";
                    PKG_UPDATE = "dnf check-update";
                    PKG_UPGRADE = "dnf upgrade -y";
                    PKG_INSTALL = "dnf install -y";
                    PKG_REMOVE = "dnf remove -y";
                    PKG_SEARCH = "dnf search";
                    PKG_LIST = "dnf list installed";
                    PKG_CLEAN = "dnf clean all";
                    PKG_AUTOREMOVE = "dnf autoremove -y";
                    PKG_INSTALL_CMD = "dnf install -y";
                } else {
                    PKG_MANAGER = "yum";
                    PKG_UPDATE = "yum check-update";
                    PKG_UPGRADE = "yum upgrade -y";
                    PKG_INSTALL = "yum install -y";
                    PKG_REMOVE = "yum remove -y";
                    PKG_SEARCH = "yum search";
                    PKG_LIST = "yum list installed";
                    PKG_CLEAN = "yum clean all";
                    PKG_AUTOREMOVE = "yum autoremove -y";
                    PKG_INSTALL_CMD = "yum install -y";
                }
            } else if (DISTRO.equals("arch") || DISTRO.equals("manjaro") || DISTRO.equals("endeavouros") || DISTRO.equals("artix")) {
                PKG_MANAGER = "pacman";
                PKG_UPDATE = "pacman -Sy";
                PKG_UPGRADE = "pacman -Su --noconfirm";
                PKG_INSTALL = "pacman -S --noconfirm";
                PKG_REMOVE = "pacman -R --noconfirm";
                PKG_SEARCH = "pacman -Ss";
                PKG_LIST = "pacman -Q";
                PKG_CLEAN = "pacman -Sc --noconfirm";
                PKG_AUTOREMOVE = "pacman -Rns --noconfirm";
                PKG_INSTALL_CMD = "pacman -S --noconfirm";
            } else if (DISTRO.equals("opensuse") || DISTRO.equals("suse") || DISTRO.equals("sles")) {
                PKG_MANAGER = "zypper";
                PKG_UPDATE = "zypper refresh";
                PKG_UPGRADE = "zypper update -y";
                PKG_INSTALL = "zypper install -y";
                PKG_REMOVE = "zypper remove -y";
                PKG_SEARCH = "zypper search";
                PKG_LIST = "zypper se --installed-only";
                PKG_CLEAN = "zypper clean";
                PKG_AUTOREMOVE = "zypper rm -u";
                PKG_INSTALL_CMD = "zypper install -y";
            } else {
                PKG_MANAGER = "unknown";
                PKG_UPDATE = "echo 'Unknown package manager'";
                PKG_UPGRADE = "echo 'Unknown package manager'";
                PKG_INSTALL = "echo 'Unknown package manager'";
                PKG_REMOVE = "echo 'Unknown package manager'";
                PKG_SEARCH = "echo 'Unknown package manager'";
                PKG_LIST = "echo 'Unknown package manager'";
                PKG_CLEAN = "echo 'Unknown package manager'";
                PKG_AUTOREMOVE = "echo 'Unknown package manager'";
                PKG_INSTALL_CMD = "echo 'Unknown package manager'";
            }
        } else if (OS_NAME.equals("macOS")) {
            if (checkCommand("brew")) {
                PKG_MANAGER = "brew";
                PKG_UPDATE = "brew update";
                PKG_UPGRADE = "brew upgrade";
                PKG_INSTALL = "brew install";
                PKG_REMOVE = "brew uninstall";
                PKG_SEARCH = "brew search";
                PKG_LIST = "brew list";
                PKG_CLEAN = "brew cleanup";
                PKG_AUTOREMOVE = "brew autoremove";
                PKG_INSTALL_CMD = "brew install";
            } else {
                PKG_MANAGER = "unknown";
                PKG_UPDATE = "echo 'Homebrew not installed'";
                PKG_UPGRADE = "echo 'Homebrew not installed'";
                PKG_INSTALL = "echo 'Homebrew not installed'";
                PKG_REMOVE = "echo 'Homebrew not installed'";
                PKG_SEARCH = "echo 'Homebrew not installed'";
                PKG_LIST = "echo 'Homebrew not installed'";
                PKG_CLEAN = "echo 'Homebrew not installed'";
                PKG_AUTOREMOVE = "echo 'Homebrew not installed'";
                PKG_INSTALL_CMD = "echo 'Homebrew not installed'";
            }
        } else if (OS_NAME.equals("Termux")) {
            PKG_MANAGER = "pkg";
            PKG_UPDATE = "pkg update";
            PKG_UPGRADE = "pkg upgrade -y";
            PKG_INSTALL = "pkg install -y";
            PKG_REMOVE = "pkg uninstall -y";
            PKG_SEARCH = "pkg search";
            PKG_LIST = "pkg list-installed";
            PKG_CLEAN = "pkg clean";
            PKG_AUTOREMOVE = "pkg autoclean";
            PKG_INSTALL_CMD = "pkg install -y";
        } else if (OS_NAME.equals("FreeBSD")) {
            PKG_MANAGER = "pkg";
            PKG_UPDATE = "pkg update";
            PKG_UPGRADE = "pkg upgrade -y";
            PKG_INSTALL = "pkg install -y";
            PKG_REMOVE = "pkg delete -y";
            PKG_SEARCH = "pkg search";
            PKG_LIST = "pkg info";
            PKG_CLEAN = "pkg clean";
            PKG_AUTOREMOVE = "pkg autoremove -y";
            PKG_INSTALL_CMD = "pkg install -y";
        } else {
            PKG_MANAGER = "unknown";
            PKG_UPDATE = "echo 'Unknown OS'";
            PKG_UPGRADE = "echo 'Unknown OS'";
            PKG_INSTALL = "echo 'Unknown OS'";
            PKG_REMOVE = "echo 'Unknown OS'";
            PKG_SEARCH = "echo 'Unknown OS'";
            PKG_LIST = "echo 'Unknown OS'";
            PKG_CLEAN = "echo 'Unknown OS'";
            PKG_AUTOREMOVE = "echo 'Unknown OS'";
            PKG_INSTALL_CMD = "echo 'Unknown OS'";
        }
    }

    // ========================================
    // FIRST RUN WIZARD
    // ========================================
    public static void firstRunWizard()
    {
        Path firstRunPath = Paths.get(HOME_DIR, CONFIG_DIR, FIRST_RUN_FILE);
        if (OS_NAME.equals("Termux")) {
            try {
                Files.createDirectories(firstRunPath.getParent());
                Files.createFile(firstRunPath);
            } catch (IOException ignored) {
            }
            return;
        }

        if (Files.exists(firstRunPath))
            return;

        showInfo("First run detected! Running setup wizard...");
        detectOS();
        detectDistro();
        detectPackageManager();

        boolean hasFastfetch = checkCommand("fastfetch");

        String summary = BOLD + "System Information:" + RESET + "\n"
            + "  Operating System: " + OS_NAME + "\n"
            + "  Distribution: " + DISTRO + "\n"
            + "  Distribution Version: " + DISTRO_VERSION + "\n"
            + "  Package Manager: " + PKG_MANAGER + "\n"
            + "  Terminal Size: " + TERMINAL_WIDTH + "x" + TERMINAL_HEIGHT + "\n"
            + "  Color Support: " + (COLORS_SUPPORTED ? "Yes" : "No") + "\n"
            + "  Root Access: " + (IS_ROOT ? "Yes" : "No") + "\n"
            + "  Fullscreen Mode: " + (IS_FULLSCREEN ? "Yes" : "No") + "\n"
            + "  Fastfetch: " + (hasFastfetch ? "Installed" : "Not installed");
        printBox("Setup Wizard", summary);

        String[] commonTools = { "curl", "wget", "jq", "git", "tar", "unzip", "zip", "tree" };
        List<String> missingTools = new ArrayList<>();
        for (String tool : commonTools) {
            if (!checkCommand(tool)) {
                missingTools.add(tool);
            }
        }

        if (!missingTools.isEmpty()) {
            showWarning("Missing common tools:");
            for (String tool : missingTools) {
                System.out.println("  - " + tool);
            }
            if (confirmAction("Install missing tools?")) {
                for (String tool : missingTools) {
                    autoInstallTool(tool, tool);
                }
            }
        }

        if (!hasFastfetch) {
            if (confirmAction("Install fastfetch for better system information?")) {
                autoInstallTool("fastfetch", "fastfetch");
            }
        }

        try {
            Files.createDirectories(firstRunPath.getParent());
            Files.createFile(firstRunPath);
        } catch (IOException ignored) {
        }
        showSuccess("First run setup complete!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }

    // ========================================
    // SYSTEM INFORMATION FUNCTIONS
    // ========================================
    public static void showFastfetch()
    {
        if (checkCommand("fastfetch")) {
            runCommand("fastfetch");
        } else {
            showWarning("Fastfetch is not installed");
            if (confirmAction("Install fastfetch?")) {
                autoInstallTool("fastfetch", "fastfetch");
                if (checkCommand("fastfetch")) {
                    runCommand("fastfetch");
                }
            }
        }
    }

    public static void showSystemInfo()
    {
        if (!checkCommand("hostname")) {
            showInfo("hostname not found. Installing...");
            autoInstallTool("hostname", "inetutils");
        }

        String hostname = "";
        try {
            ProcessBuilder pb = new ProcessBuilder("hostname");
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            hostname = br.readLine().trim();
        } catch (Exception e) {
        }

        String uptimeStr = "Unknown";
        try {
            ProcessBuilder pb = new ProcessBuilder("uptime", "-p");
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            uptimeStr = br.readLine().trim();
        } catch (Exception e) {
        }

        String os = System.getProperty("os.name");
        String kernel = System.getProperty("os.version");
        String arch = System.getProperty("os.arch");
        String shell = System.getenv("SHELL");
        String user = System.getenv("USER");

        String info = BOLD + "System Information:" + RESET + "\n"
            + "  Hostname: " + hostname + "\n"
            + "  OS: " + OS_NAME + " " + DISTRO + " " + DISTRO_VERSION + "\n"
            + "  Kernel: " + kernel + "\n"
            + "  Architecture: " + arch + "\n"
            + "  Shell: " + shell + "\n"
            + "  User: " + user + "\n"
            + "  Root Access: " + (IS_ROOT ? "Yes" : "No") + "\n"
            + "  Fullscreen: " + (IS_FULLSCREEN ? "Yes" : "No") + "\n"
            + "  Uptime: " + uptimeStr;
        printBox("System Information", info);
    }

    public static void showHardwareInfo()
    {
        if (!checkCommand("lshw") && !checkCommand("dmidecode")) {
            showInfo("Installing hardware detection tools...");
            autoInstallTool("lshw", "lshw");
        }

        String info = "";
        if (OS_NAME.equals("Linux")) {
            if (checkCommand("lshw")) {
                info = runCommandOutput("lshw -short 2>/dev/null | head -20");
            } else if (checkCommand("dmidecode")) {
                info = runCommandOutput("dmidecode -t system 2>/dev/null");
            } else {
                info = "Hardware detection tools not available";
            }
        } else if (OS_NAME.equals("macOS")) {
            info = runCommandOutput("system_profiler SPHardwareDataType 2>/dev/null | head -10");
        } else {
            info = "Hardware information not fully supported on this OS";
        }
        printBox("Hardware Information", info);
    }

    public static void showCpuInfo()
    {
        String info = "";
        if (OS_NAME.equals("Linux")) {
            if (Files.exists(Paths.get("/proc/cpuinfo"))) {
                info = runCommandOutput("cat /proc/cpuinfo | grep -E 'model name|cpu cores|siblings|cache size' | head -10");
                String usage = runCommandOutput("top -bn1 2>/dev/null | grep 'Cpu(s)' | awk '{print $2}'").trim();
                if (!usage.isEmpty()) {
                    info += "\nCPU Usage: " + usage + "%";
                }
            }
        } else if (OS_NAME.equals("macOS")) {
            String cpuInfo = runCommandOutput("sysctl -n machdep.cpu.brand_string 2>/dev/null").trim();
            String cores = runCommandOutput("sysctl -n hw.ncpu 2>/dev/null").trim();
            String usage = runCommandOutput("top -l1 2>/dev/null | grep 'CPU usage' | awk '{print $3,$4,$5}'").trim();
            info = "CPU: " + cpuInfo + "\nCores: " + cores + "\nCPU Usage: " + usage;
        } else {
            info = "CPU information not supported on this OS";
        }
        printBox("CPU Information", info);
    }

    public static void showGpuInfo()
    {
        String info = "";
        if (OS_NAME.equals("Linux")) {
            if (checkCommand("lspci")) {
                info = runCommandOutput("lspci | grep -E 'VGA|3D|Display' 2>/dev/null");
            } else {
                info = "Install pciutils or mesa-utils for GPU info";
            }
        } else if (OS_NAME.equals("macOS")) {
            info = runCommandOutput("system_profiler SPDisplaysDataType 2>/dev/null | head -20");
        } else {
            info = "GPU information not supported on this OS";
        }
        printBox("GPU Information", info);
    }

    public static void showRamInfo()
    {
        String info = "";
        if (OS_NAME.equals("Linux")) {
            try {
                Path meminfo = Paths.get("/proc/meminfo");
                List<String> lines = Files.readAllLines(meminfo);
                long total = 0, free = 0, available = 0;
                for (String line : lines) {
                    if (line.startsWith("MemTotal:")) {
                        total = Long.parseLong(line.replaceAll("[^0-9]", ""));
                    } else if (line.startsWith("MemFree:")) {
                        free = Long.parseLong(line.replaceAll("[^0-9]", ""));
                    } else if (line.startsWith("MemAvailable:")) {
                        available = Long.parseLong(line.replaceAll("[^0-9]", ""));
                    }
                }
                double totalGB = total / 1024.0 / 1024.0;
                double usedGB = (total - available) / 1024.0 / 1024.0;
                double freeGB = free / 1024.0 / 1024.0;
                double availGB = available / 1024.0 / 1024.0;
                int usedPercent = (int)(((total - available) * 100.0) / total);
                info = String.format("Total: %.2f GB\nUsed: %.2f GB (%d%%)\nFree: %.2f GB\nAvailable: %.2f GB",
                    totalGB, usedGB, usedPercent, freeGB, availGB);
            } catch (Exception e) {
                info = "RAM information not available";
            }
        } else if (OS_NAME.equals("macOS")) {
            String total = runCommandOutput("sysctl -n hw.memsize 2>/dev/null | awk '{print $1/1024/1024/1024 \" GB\"}'").trim();
            info = "Total: " + total;
        } else {
            info = "RAM information not supported on this OS";
        }
        printBox("RAM Information", info);
    }

    public static void showMotherboardInfo()
    {
        if (!checkCommand("dmidecode")) {
            showInfo("Installing dmidecode for motherboard info...");
            autoInstallTool("dmidecode", "dmidecode");
        }
        String info = "";
        if (OS_NAME.equals("Linux") && checkCommand("dmidecode")) {
            info = runCommandOutput("dmidecode -t baseboard 2>/dev/null | head -10");
        } else {
            info = "Motherboard information only available on Linux with dmidecode";
        }
        printBox("Motherboard Information", info);
    }

    public static void showDiskInfo()
    {
        String cmd = "";
        if (OS_NAME.equals("Linux") || OS_NAME.equals("Termux")) {
            cmd = "df -h | grep -E '^/dev|Filesystem' | column -t 2>/dev/null || df -h";
        } else {
            cmd = "df -h | column -t 2>/dev/null || df -h";
        }
        String info = runCommandOutput(cmd);
        printBox("Disk Information", info);
    }

    public static void showKernelInfo()
    {
        String info = "Kernel: " + System.getProperty("os.version") + "\n"
            + "Kernel Version: " + System.getProperty("os.version") + "\n"
            + "Architecture: " + System.getProperty("os.arch") + "\n"
            + "Operating System: " + System.getProperty("os.name");
        printBox("Kernel Information", info);
    }

    public static void showUptime()
    {
        String uptimeStr = "Unknown";
        String loadStr = "Unknown";
        try {
            ProcessBuilder pb = new ProcessBuilder("uptime", "-p");
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            uptimeStr = br.readLine().trim();
        } catch (Exception e) {
        }
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", "uptime | awk -F'load average:' '{print $2}' | xargs");
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            loadStr = br.readLine().trim();
        } catch (Exception e) {
        }
        String info = "Uptime: " + uptimeStr + "\nLoad: " + loadStr;
        printBox("System Uptime", info);
    }

    public static void showEnvironmentInfo()
    {
        String info = "Shell: " + System.getenv("SHELL") + "\n"
            + "Terminal: " + System.getenv("TERM") + "\n"
            + "Terminal Size: " + TERMINAL_WIDTH + "x" + TERMINAL_HEIGHT + "\n"
            + "Color Support: " + (COLORS_SUPPORTED ? "Yes" : "No") + "\n"
            + "Locale: " + System.getenv("LANG");
        printBox("Environment Information", info);
    }

    // ========================================
    // MONITORING FUNCTIONS
    // ========================================
    public static void showCpuUsage()
    {
        String info = "";
        if (checkCommand("top")) {
            info = runCommandOutput("top -bn1 2>/dev/null | head -15");
        }
        printBox("CPU Usage", info);
    }

    public static void showRamUsage()
    {
        String info = "";
        if (checkCommand("free")) {
            info = runCommandOutput("free -h");
        } else {
            info = "RAM monitoring not supported";
        }
        printBox("RAM Usage", info);
    }

    public static void showDiskUsage()
    {
        String info = "";
        if (checkCommand("df")) {
            info = runCommandOutput("df -h | grep -v tmpfs | column -t 2>/dev/null || df -h | grep -v tmpfs");
        }
        printBox("Disk Usage", info);
    }

    public static void showNetworkUsage()
    {
        if (!checkCommand("iftop")) {
            showInfo("iftop not installed. Installing...");
            autoInstallTool("iftop", "iftop");
        }
        String info = "";
        if (checkCommand("iftop")) {
            info = runCommandOutput("netstat -i 2>/dev/null | head -10");
            info += "\nRun 'sudo iftop' for real-time monitoring";
        } else {
            info = "iftop installation failed";
        }
        printBox("Network Usage", info);
    }

    public static void showRunningProcesses()
    {
        String info = "";
        if (checkCommand("ps")) {
            info = runCommandOutput("ps aux 2>/dev/null | head -20 | column -t 2>/dev/null");
        }
        printBox("Running Processes", info);
    }

    public static void showTopProcesses()
    {
        String info = "";
        if (OS_NAME.equals("Linux")) {
            info += "Top CPU Processes:\n";
            info += runCommandOutput("ps aux --sort=-%cpu 2>/dev/null | head -10 | column -t 2>/dev/null");
            info += "\nTop Memory Processes:\n";
            info += runCommandOutput("ps aux --sort=-%mem 2>/dev/null | head -10 | column -t 2>/dev/null");
        } else {
            info = runCommandOutput("ps aux 2>/dev/null | sort -k3 -r | head -10 | column -t 2>/dev/null");
        }
        printBox("Top Processes", info);
    }

    public static void showResourceMonitor()
    {
        if (checkCommand("htop")) {
            runCommand("htop");
        } else if (checkCommand("top")) {
            runCommand("top");
        } else {
            showError("No resource monitor available");
        }
    }

    public static void showTemperature()
    {
        if (!checkCommand("sensors")) {
            showInfo("lm-sensors not installed. Installing...");
            autoInstallTool("sensors", "lm-sensors");
        }
        String info = "";
        if (OS_NAME.equals("Linux")) {
            if (checkCommand("sensors")) {
                info = runCommandOutput("sensors 2>/dev/null");
            } else if (Files.exists(Paths.get("/sys/class/thermal/thermal_zone0/temp"))) {
                try {
                    String temp = Files.readString(Paths.get("/sys/class/thermal/thermal_zone0/temp")).trim();
                    int t = Integer.parseInt(temp) / 1000;
                    info = "CPU Temperature: " + t + "°C";
                } catch (Exception e) {
                }
            } else {
                info = "Temperature sensors not available";
            }
        } else {
            info = "Temperature monitoring not supported on this OS";
        }
        printBox("System Temperature", info);
    }

    // ========================================
    // NETWORK FUNCTIONS
    // ========================================
    public static void pingTest(String target)
    {
        String info = "Pinging " + target + "...\n";
        info += runCommandOutput("ping -c 4 " + target + " 2>&1");
        printBox("Ping Test", info);
    }

    public static void internetTest()
    {
        String info = "";
        try {
            ProcessBuilder pb = new ProcessBuilder("ping", "-c", "1", "-W", "2", "8.8.8.8");
            Process p = pb.start();
            int exit = p.waitFor();
            if (exit == 0) {
                info += "Internet Connection: ✅ Connected\n";
                String ip = runCommandOutput("curl -s ifconfig.me 2>/dev/null").trim();
                info += "Public IP: " + ip;
            } else {
                info = "Internet Connection: ❌ Disconnected";
            }
        } catch (Exception e) {
            info = "Internet Test failed";
        }
        printBox("Internet Test", info);
    }

    public static void dnsTest()
    {
        String info = "DNS Servers:\n";
        if (Files.exists(Paths.get("/etc/resolv.conf"))) {
            info += runCommandOutput("grep '^nameserver' /etc/resolv.conf 2>/dev/null | awk '{print $2}'");
        }
        printBox("DNS Test", info);
    }

    public static void showPublicIp()
    {
        String ipv4 = "N/A";
        String location = "N/A";
        try {
            ipv4 = runCommandOutput("curl -s ifconfig.me 2>/dev/null").trim();
            location = runCommandOutput("curl -s ipapi.co/city 2>/dev/null").trim();
        } catch (Exception e) {
        }
        String info = "IPv4: " + ipv4 + "\nLocation: " + location;
        printBox("Public IP", info);
    }

    public static void showLocalIp()
    {
        String info = "Interface IPs:\n";
        if (checkCommand("ip")) {
            info += runCommandOutput("ip addr show 2>/dev/null | grep 'inet ' | grep -v '127.0.0.1' | awk '{print $2, $NF}'");
        } else if (checkCommand("ifconfig")) {
            info += runCommandOutput("ifconfig 2>/dev/null | grep 'inet ' | grep -v '127.0.0.1' | awk '{print $2, $NF}'");
        } else {
            info += "No network tools available";
        }
        printBox("Local IP", info);
    }

    public static void showGateway()
    {
        String info = "";
        if (checkCommand("ip")) {
            info = runCommandOutput("ip route 2>/dev/null | grep default | awk '{print $3}'").trim();
        } else {
            info = "Gateway detection not available";
        }
        printBox("Default Gateway", info);
    }

    public static void showWifiInfo()
    {
        String info = "";
        if (OS_NAME.equals("Linux") && checkCommand("nmcli")) {
            info = runCommandOutput("nmcli dev wifi list 2>/dev/null | head -10");
        } else {
            info = "Wi-Fi information not supported on this OS";
        }
        printBox("Wi-Fi Information", info);
    }

    // ========================================
    // POWER FUNCTIONS
    // ========================================
    public static void showBatteryInfo()
    {
        String info = "";
        Path bat = Paths.get("/sys/class/power_supply/BAT0");
        if (Files.exists(bat)) {
            String capacity = "N/A", status = "N/A";
            try {
                capacity = Files.readString(bat.resolve("capacity")).trim();
                status = Files.readString(bat.resolve("status")).trim();
            } catch (IOException e) {
            }
            info = "Battery: " + capacity + "%\nStatus: " + status;
        } else {
            info = "No battery found";
        }
        printBox("Battery Information", info);
    }

    // ========================================
    // PACKAGE MANAGER MENU
    // ========================================
    public static void packageManagerMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Update packages",
                "Upgrade packages",
                "Clean cache",
                "Autoremove",
                "Search packages",
                "List installed packages",
                "Back to main menu"
            };
            printMenu("Package Manager", items);
            int choice = getIntInput("Select option: ");
            if (choice == 1) {
                showInfo("Updating package lists...");
                runWithSudo(PKG_UPDATE);
                showSuccess("Package lists updated");
            } else if (choice == 2) {
                if (confirmAction("Upgrade all packages?")) {
                    showInfo("Upgrading packages...");
                    runWithSudo(PKG_UPGRADE);
                    showSuccess("Packages upgraded");
                }
            } else if (choice == 3) {
                if (confirmAction("Clean package cache?")) {
                    showInfo("Cleaning cache...");
                    runWithSudo(PKG_CLEAN);
                    showSuccess("Cache cleaned");
                }
            } else if (choice == 4) {
                if (confirmAction("Remove unused packages?")) {
                    showInfo("Removing unused packages...");
                    runWithSudo(PKG_AUTOREMOVE);
                    showSuccess("Unused packages removed");
                }
            } else if (choice == 5) {
                System.out.print("Search term: ");
                Scanner sc = new Scanner(System.in);
                String term = sc.nextLine().trim();
                runCommand(PKG_SEARCH + " " + term);
            } else if (choice == 6) {
                runCommand(PKG_LIST);
            } else if (choice == 0 || choice == 7) {
                break;
            } else {
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    // ========================================
    // CLEANER FUNCTIONS
    // ========================================
    public static void cleanerMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
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
            printMenu("Cleaner", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                cleanUserCache();
                break;
            case 2:
                cleanPackageCache();
                break;
            case 3:
                cleanTempFiles();
                break;
            case 4:
                cleanOldLogs();
                break;
            case 5:
                cleanTrash();
                break;
            case 6:
                cleanHomebrew();
                break;
            case 7:
                cleanTimeshift();
                break;
            case 8:
                cleanSnapper();
                break;
            case 0:
            case 9:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void cleanUserCache()
    {
        Path cacheDir = Paths.get(HOME_DIR, ".cache");
        if (Files.exists(cacheDir)) {
            showInfo("Cache directory");
            if (confirmAction("Delete user cache?")) {
                try {
                    Files.walk(cacheDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    showSuccess("User cache cleaned");
                } catch (IOException e) {
                    showError("Failed to clean cache");
                }
            }
        } else {
            showWarning("No user cache directory found");
        }
    }

    public static void cleanPackageCache()
    {
        if (confirmAction("Clean package cache?")) {
            runWithSudo(PKG_CLEAN);
            showSuccess("Package cache cleaned");
        }
    }

    public static void cleanTempFiles()
    {
        Path tmp = Paths.get("/tmp");
        if (Files.exists(tmp)) {
            showInfo("Temporary directory: /tmp");
            if (confirmAction("Delete temporary files?")) {
                if (!IS_ROOT) {
                    showWarning("This requires root privileges");
                    if (confirmAction("Continue with sudo?")) {
                        runWithSudo("rm -rf /tmp/*");
                    }
                } else {
                    try {
                        Files.walk(tmp).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                        Files.createDirectories(tmp);
                        showSuccess("Temporary files cleaned");
                    } catch (IOException e) {
                        showError("Failed to clean temp files");
                    }
                }
            }
        }
    }

    public static void cleanOldLogs()
    {
        Path logDir = Paths.get("/var/log");
        if (Files.exists(logDir)) {
            showInfo("Log directory: /var/log");
            if (confirmAction("Clean old logs (keeping last 7 days)?")) {
                if (!IS_ROOT) {
                    showWarning("This requires root privileges");
                    if (confirmAction("Continue with sudo?")) {
                        runWithSudo("find /var/log -name '*.log' -mtime +7 -delete 2>/dev/null");
                    }
                } else {
                    runCommand("find /var/log -name '*.log' -mtime +7 -delete 2>/dev/null");
                }
                showSuccess("Old logs cleaned");
            }
        } else {
            showWarning("No log directory found");
        }
    }

    public static void cleanTrash()
    {
        Path trash = Paths.get(HOME_DIR, ".local/share/Trash");
        if (Files.exists(trash)) {
            showInfo("Trash");
            if (confirmAction("Empty trash?")) {
                try {
                    Files.walk(trash).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    showSuccess("Trash emptied");
                } catch (IOException e) {
                    showError("Failed to empty trash");
                }
            }
        }
    }

    public static void cleanHomebrew()
    {
        if (OS_NAME.equals("macOS") && checkCommand("brew")) {
            if (confirmAction("Run Homebrew cleanup?")) {
                runCommand("brew cleanup --prune=all");
                showSuccess("Homebrew cleanup complete");
            }
        } else {
            showWarning("Homebrew not available");
        }
    }

    public static void cleanTimeshift()
    {
        if (checkCommand("timeshift")) {
            showInfo("Timeshift detected");
            if (confirmAction("Clean Timeshift snapshots?")) {
                showSuccess("Timeshift cleanup complete");
            }
        } else {
            showWarning("Timeshift not installed");
        }
    }

    public static void cleanSnapper()
    {
        if (checkCommand("snapper")) {
            showInfo("Snapper detected");
            if (confirmAction("Clean Snapper snapshots?")) {
                showSuccess("Snapper cleanup complete");
            }
        } else {
            showWarning("Snapper not installed");
        }
    }

    // ========================================
    // STORAGE FUNCTIONS
    // ========================================
    public static void storageMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Disk usage",
                "Largest directories",
                "Largest files",
                "Mounted drives",
                "SMART status (if available)",
                "Back to main menu"
            };
            printMenu("Storage", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                showDiskUsage();
                break;
            case 2:
                largestDirectories(HOME_DIR);
                break;
            case 3:
                largestFiles(HOME_DIR);
                break;
            case 4:
                showMountedDrives();
                break;
            case 5:
                showSmartStatus();
                break;
            case 0:
            case 6:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void largestDirectories(String dir)
    {
        String cmd = "du -sh \"" + dir + "\"/* 2>/dev/null 2>/dev/null | sort -hr 2>/dev/null | head -15";
        String info = "Largest directories in " + dir + ":\n" + runCommandOutput(cmd);
        printBox("Largest Directories", info);
    }

    public static void largestFiles(String dir)
    {
        String cmd = "find \"" + dir + "\" -type f -exec du -h {} + 2>/dev/null | sort -hr 2>/dev/null | head -15";
        String info = "Largest files in " + dir + ":\n" + runCommandOutput(cmd);
        printBox("Largest Files", info);
    }

    public static void showMountedDrives()
    {
        String info = "";
        if (checkCommand("mount")) {
            info = runCommandOutput("mount 2>/dev/null | head -20");
        }
        printBox("Mounted Drives", info);
    }

    public static void showSmartStatus()
    {
        if (!checkCommand("smartctl")) {
            showWarning("smartctl not installed");
            if (confirmAction("Install smartmontools?")) {
                autoInstallTool("smartctl", "smartmontools");
            }
        }
        String info = "";
        if (checkCommand("smartctl")) {
            info = "SMART status check available\nRun 'sudo smartctl -a /dev/sda' for details";
        } else {
            info = "smartctl not available";
        }
        printBox("SMART Status", info);
    }

    // ========================================
    // SECURITY FUNCTIONS
    // ========================================
    public static void securityMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Firewall status",
                "Open ports",
                "SSH status",
                "Running services",
                "Security recommendations",
                "Back to main menu"
            };
            printMenu("Security", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                showFirewallStatus();
                break;
            case 2:
                showOpenPorts();
                break;
            case 3:
                showSshStatus();
                break;
            case 4:
                showRunningServices();
                break;
            case 5:
                showSecurityRecommendations();
                break;
            case 0:
            case 6:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void showFirewallStatus()
    {
        String info = "";
        if (checkCommand("ufw")) {
            String status = runCommandOutput("ufw status 2>/dev/null | head -1").trim();
            info = "UFW Status: " + status;
        } else if (checkCommand("iptables")) {
            if (IS_ROOT) {
                info = runCommandOutput("iptables -L -n 2>/dev/null | head -10");
            } else {
                info = "(sudo required for iptables)";
            }
        } else {
            info = "Firewall tools not available";
        }
        printBox("Firewall Status", info);
    }

    public static void showOpenPorts()
    {
        String info = "";
        if (checkCommand("netstat")) {
            info = runCommandOutput("netstat -tulpn 2>/dev/null | grep LISTEN | head -15");
        } else if (checkCommand("ss")) {
            info = runCommandOutput("ss -tulpn 2>/dev/null | head -15");
        } else {
            info = "No network tools available";
        }
        printBox("Open Ports", info);
    }

    public static void showSshStatus()
    {
        String info = "";
        if (checkCommand("systemctl")) {
            int exit = 0;
            try {
                ProcessBuilder pb = new ProcessBuilder("systemctl", "is-active", "sshd");
                Process p = pb.start();
                exit = p.waitFor();
            } catch (Exception e) {
            }
            info = exit == 0 ? "SSH Service: Active" : "SSH Service: Inactive";
        } else {
            info = "SSH service not detected";
        }
        printBox("SSH Status", info);
    }

    public static void showRunningServices()
    {
        String info = "";
        if (checkCommand("systemctl")) {
            info = runCommandOutput("systemctl list-units --type=service --state=running 2>/dev/null | head -15");
        } else {
            info = "Service management not supported";
        }
        printBox("Running Services", info);
    }

    public static void showSecurityRecommendations()
    {
        String info = BOLD + "Security Recommendations:" + RESET + "\n\n"
            + "1. Keep system updated: " + PKG_UPDATE + " && " + PKG_UPGRADE + "\n"
            + "2. Enable firewall\n"
            + "3. Disable root SSH login\n"
            + "4. Use SSH keys instead of passwords\n"
            + "5. Remove unused packages\n"
            + "6. Check for open ports regularly\n"
            + "7. Use strong passwords\n"
            + "8. Enable disk encryption\n"
            + "9. Backup important data\n"
            + "10. Review system logs regularly";
        printBox("Security Recommendations", info);
    }

    // ========================================
    // FILE TOOLS FUNCTIONS
    // ========================================
    public static void fileToolsMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Find files",
                "Find duplicate files",
                "Search text in files",
                "Directory tree",
                "File statistics",
                "Back to main menu"
            };
            printMenu("File Tools", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                findFiles();
                break;
            case 2:
                findDuplicates();
                break;
            case 3:
                searchText();
                break;
            case 4:
                showDirectoryTree();
                break;
            case 5:
                showFileStats();
                break;
            case 0:
            case 6:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void findFiles()
    {
        System.out.print("Directory to search [" + HOME_DIR + "]: ");
        Scanner sc = new Scanner(System.in);
        String dir = sc.nextLine().trim();
        if (dir.isEmpty())
            dir = HOME_DIR;
        System.out.print("Pattern (e.g., *.txt): ");
        String pattern = sc.nextLine().trim();
        if (!pattern.isEmpty()) {
            String cmd = "find \"" + dir + "\" -type f -name \"" + pattern + "\" 2>/dev/null | head -20";
            String info = "Files matching " + pattern + " in " + dir + ":\n" + runCommandOutput(cmd);
            printBox("Find Files", info);
        }
    }

    public static void findDuplicates()
    {
        System.out.print("Directory to scan [" + HOME_DIR + "]: ");
        Scanner sc = new Scanner(System.in);
        String dir = sc.nextLine().trim();
        if (dir.isEmpty())
            dir = HOME_DIR;
        String cmd = "find \"" + dir + "\" -type f -exec md5sum {} \\; 2>/dev/null | sort | uniq -d -w32 | head -20";
        String info = "Duplicate files (same content):\n" + runCommandOutput(cmd);
        printBox("Duplicate Files", info);
    }

    public static void searchText()
    {
        System.out.print("Text to search: ");
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine().trim();
        System.out.print("Directory to search [" + HOME_DIR + "]: ");
        String dir = sc.nextLine().trim();
        if (dir.isEmpty())
            dir = HOME_DIR;
        if (!text.isEmpty()) {
            String cmd = "grep -r -l \"" + text + "\" \"" + dir + "\" 2>/dev/null | head -20";
            String info = "Searching for '" + text + "' in " + dir + ":\n" + runCommandOutput(cmd);
            printBox("Text Search", info);
        }
    }

    public static void showDirectoryTree()
    {
        System.out.print("Directory [" + HOME_DIR + "]: ");
        Scanner sc = new Scanner(System.in);
        String dir = sc.nextLine().trim();
        if (dir.isEmpty())
            dir = HOME_DIR;
        String info = "";
        if (checkCommand("tree")) {
            info = runCommandOutput("tree -L 2 \"" + dir + "\" 2>/dev/null | head -30");
        } else {
            info = "tree command not installed";
        }
        printBox("Directory Tree", info);
    }

    public static void showFileStats()
    {
        System.out.print("Directory [" + HOME_DIR + "]: ");
        Scanner sc = new Scanner(System.in);
        String dir = sc.nextLine().trim();
        if (dir.isEmpty())
            dir = HOME_DIR;
        String info = "Directory: " + dir + "\n";
        String files = runCommandOutput("find \"" + dir + "\" -type f 2>/dev/null | wc -l").trim();
        info += "Total files: " + files + "\n";
        String size = runCommandOutput("du -sh \"" + dir + "\" 2>/dev/null | awk '{print $1}'").trim();
        info += "Total size: " + size;
        printBox("File Statistics", info);
    }

    // ========================================
    // ARCHIVE TOOLS
    // ========================================
    public static void archiveMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Extract ZIP",
                "Extract TAR",
                "Create ZIP",
                "Create TAR.GZ",
                "Back to main menu"
            };
            printMenu("Archive", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                extractZip();
                break;
            case 2:
                extractTar();
                break;
            case 3:
                createZip();
                break;
            case 4:
                createTargz();
                break;
            case 0:
            case 5:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void extractZip()
    {
        System.out.print("ZIP file path: ");
        Scanner sc = new Scanner(System.in);
        String zip = sc.nextLine().trim();
        if (!Files.exists(Paths.get(zip))) {
            showError("File not found");
            return;
        }
        System.out.print("Extract to [current directory]: ");
        String dir = sc.nextLine().trim();
        if (dir.isEmpty())
            dir = ".";
        if (checkCommand("unzip")) {
            runCommand("unzip \"" + zip + "\" -d \"" + dir + "\"");
            showSuccess("Extracted");
        } else {
            showWarning("unzip not installed");
        }
    }

    public static void extractTar()
    {
        System.out.print("TAR file path: ");
        Scanner sc = new Scanner(System.in);
        String tar = sc.nextLine().trim();
        if (!Files.exists(Paths.get(tar))) {
            showError("File not found");
            return;
        }
        System.out.print("Extract to [current directory]: ");
        String dir = sc.nextLine().trim();
        if (dir.isEmpty())
            dir = ".";
        if (checkCommand("tar")) {
            runCommand("tar -xf \"" + tar + "\" -C \"" + dir + "\"");
            showSuccess("Extracted");
        } else {
            showWarning("tar not installed");
        }
    }

    public static void createZip()
    {
        System.out.print("Directory to zip: ");
        Scanner sc = new Scanner(System.in);
        String dir = sc.nextLine().trim();
        if (!Files.exists(Paths.get(dir))) {
            showError("Directory not found");
            return;
        }
        System.out.print("Output zip name: ");
        String name = sc.nextLine().trim();
        if (checkCommand("zip")) {
            runCommand("zip -r \"" + name + "\" \"" + dir + "\"");
            showSuccess("Created");
        } else {
            showWarning("zip not installed");
        }
    }

    public static void createTargz()
    {
        System.out.print("Directory to archive: ");
        Scanner sc = new Scanner(System.in);
        String dir = sc.nextLine().trim();
        if (!Files.exists(Paths.get(dir))) {
            showError("Directory not found");
            return;
        }
        System.out.print("Output tar.gz name: ");
        String name = sc.nextLine().trim();
        if (checkCommand("tar")) {
            runCommand("tar -czf \"" + name + "\" \"" + dir + "\"");
            showSuccess("Created");
        } else {
            showWarning("tar not installed");
        }
    }

    // ========================================
    // UTILITY TOOLS
    // ========================================
    public static void utilityMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Password Generator",
                "Hash Generator",
                "UUID Generator",
                "Random String Generator",
                "Back to main menu"
            };
            printMenu("Utilities", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                generatePassword();
                break;
            case 2:
                generateHash();
                break;
            case 3:
                generateUuid();
                break;
            case 4:
                generateRandomString();
                break;
            case 0:
            case 5:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void generatePassword()
    {
        System.out.print("Password length [16]: ");
        Scanner sc = new Scanner(System.in);
        String lenStr = sc.nextLine().trim();
        int length = 16;
        try {
            length = Integer.parseInt(lenStr);
        } catch (NumberFormatException e) {
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        printBox("Generated Password", password.toString());
    }

    public static void generateHash()
    {
        clearScreen();
        printHeader();
        String[] items = { "MD5", "SHA1", "SHA256", "SHA512", "Back" };
        printMenu("Hash Generator", items);
        int choice = getIntInput("Select hash type: ");
        if (choice == 0 || choice == 5)
            return;
        System.out.print("Enter text to hash: ");
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine().trim();
        String cmd = "";
        switch (choice) {
        case 1:
            cmd = "echo -n \"" + text + "\" | md5sum 2>/dev/null | awk '{print $1}'";
            break;
        case 2:
            cmd = "echo -n \"" + text + "\" | sha1sum 2>/dev/null | awk '{print $1}'";
            break;
        case 3:
            cmd = "echo -n \"" + text + "\" | sha256sum 2>/dev/null | awk '{print $1}'";
            break;
        case 4:
            cmd = "echo -n \"" + text + "\" | sha512sum 2>/dev/null | awk '{print $1}'";
            break;
        default:
            showError("Invalid option");
            return;
        }
        String hash = runCommandOutput(cmd).trim();
        printBox("Generated Hash", hash);
    }

    public static void generateUuid()
    {
        String uuid = "uuidgen not available";
        if (checkCommand("uuidgen")) {
            uuid = runCommandOutput("uuidgen 2>/dev/null").trim();
        }
        printBox("Generated UUID", uuid);
    }

    public static void generateRandomString()
    {
        System.out.print("Random string length [16]: ");
        Scanner sc = new Scanner(System.in);
        String lenStr = sc.nextLine().trim();
        int length = 16;
        try {
            length = Integer.parseInt(lenStr);
        } catch (NumberFormatException e) {
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new SecureRandom();
        StringBuilder randStr = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            randStr.append(chars.charAt(random.nextInt(chars.length())));
        }
        printBox("Random String", randStr.toString());
    }

    // ========================================
    // INTERNET TOOLS
    // ========================================
    public static void internetToolsMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Weather",
                "Current Time",
                "Calendar",
                "Back to main menu"
            };
            printMenu("Internet", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                showWeather();
                break;
            case 2:
                showCurrentTime();
                break;
            case 3:
                showCalendar();
                break;
            case 0:
            case 4:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void showWeather()
    {
        System.out.print("City (default: London): ");
        Scanner sc = new Scanner(System.in);
        String city = sc.nextLine().trim();
        if (city.isEmpty())
            city = "London";
        if (checkCommand("curl")) {
            showInfo("Fetching weather...");
            String cmd = "curl -s --max-time 5 \"wttr.in/" + city + "?format=%c+%t+%w\" 2>/dev/null";
            String weather = runCommandOutput(cmd).trim();
            if (!weather.isEmpty()) {
                printBox("Weather", "Weather for " + city + ":\n" + weather);
            } else {
                printBox("Weather", "Unable to fetch weather");
            }
        } else {
            printBox("Weather", "curl is required for weather data");
        }
    }

    public static void showCurrentTime()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");
        String info = "Date: " + now.format(dateFormat) + "\nTime: " + now.format(timeFormat);
        printBox("Current Time", info);
    }

    public static void showCalendar()
    {
        String info = runCommandOutput("cal 2>/dev/null");
        printBox("Calendar", info);
    }

    // ========================================
    // BACKUP FUNCTIONS
    // ========================================
    public static void backupMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Backup folder",
                "Restore backup",
                "Compress backup",
                "Verify backup",
                "Back to main menu"
            };
            printMenu("Backup", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                backupFolder();
                break;
            case 2:
                restoreBackup();
                break;
            case 3:
                compressBackup();
                break;
            case 4:
                verifyBackup();
                break;
            case 0:
            case 5:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void backupFolder()
    {
        System.out.print("Folder to backup: ");
        Scanner sc = new Scanner(System.in);
        String src = sc.nextLine().trim();
        if (!Files.exists(Paths.get(src))) {
            showError("Source directory not found");
            return;
        }
        String defaultName = "backup_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.print("Backup name [" + defaultName + "]: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty())
            name = defaultName;
        Path backupDir = Paths.get(HOME_DIR, "backups");
        try {
            Files.createDirectories(backupDir);
        } catch (IOException e) {
        }
        if (checkCommand("tar")) {
            runCommand("tar -czf \"" + backupDir.resolve(name + ".tar.gz").toString() + "\" -C \"" + Paths.get(src).getParent().toString() + "\" \"" + Paths.get(src).getFileName().toString() + "\"");
            showSuccess("Backup created");
        } else {
            try {
                Files.copy(Paths.get(src), backupDir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
                showSuccess("Backup created");
            } catch (IOException e) {
                showError("Backup failed");
            }
        }
    }

    public static void restoreBackup()
    {
        Path backupDir = Paths.get(HOME_DIR, "backups");
        if (!Files.exists(backupDir)) {
            showError("No backups found");
            return;
        }
        clearScreen();
        printHeader();
        String info = "Available Backups:\n";
        info += runCommandOutput("ls -1 \"" + backupDir.toString() + "\" 2>/dev/null");
        printBox("Available Backups", info);
        System.out.print("Enter backup name to restore: ");
        Scanner sc = new Scanner(System.in);
        String file = sc.nextLine().trim();
        Path full = backupDir.resolve(file);
        if (!Files.exists(full)) {
            showError("Backup not found");
            return;
        }
        System.out.print("Restore to: ");
        String dest = sc.nextLine().trim();
        if (dest.isEmpty()) {
            showError("No restore directory");
            return;
        }
        try {
            Files.createDirectories(Paths.get(dest));
        } catch (IOException e) {
        }
        if (file.endsWith(".tar.gz")) {
            runCommand("tar -xzf \"" + full.toString() + "\" -C \"" + dest + "\"");
        } else {
            try {
                Files.walk(Paths.get(dest)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                Files.copy(full, Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                showError("Restore failed");
            }
        }
        showSuccess("Restored");
    }

    public static void compressBackup()
    {
        Path backupDir = Paths.get(HOME_DIR, "backups");
        if (!Files.exists(backupDir)) {
            showError("No backups found");
            return;
        }
        String info = "Uncompressed Backups:\n";
        info += runCommandOutput("ls -1 \"" + backupDir.toString() + "\" 2>/dev/null | grep -v '.tar.gz$'");
        printBox("Uncompressed Backups", info);
        System.out.print("Enter backup directory to compress: ");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine().trim();
        Path full = backupDir.resolve(name);
        if (!Files.exists(full)) {
            showError("Backup not found");
            return;
        }
        if (checkCommand("tar")) {
            runCommand("tar -czf \"" + backupDir.resolve(name + ".tar.gz").toString() + "\" -C \"" + backupDir.toString() + "\" \"" + name + "\"");
            try {
                Files.delete(full);
            } catch (IOException e) {
            }
            showSuccess("Compressed");
        } else {
            showError("tar not installed");
        }
    }

    public static void verifyBackup()
    {
        Path backupDir = Paths.get(HOME_DIR, "backups");
        if (!Files.exists(backupDir)) {
            showError("No backups found");
            return;
        }
        clearScreen();
        printHeader();
        String info = "Available Backups:\n";
        info += runCommandOutput("ls -1 \"" + backupDir.toString() + "\" 2>/dev/null");
        printBox("Available Backups", info);
        System.out.print("Enter backup to verify: ");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine().trim();
        Path full = backupDir.resolve(name);
        if (!Files.exists(full)) {
            showError("Backup not found");
            return;
        }
        if (name.endsWith(".tar.gz")) {
            try {
                ProcessBuilder pb = new ProcessBuilder("tar", "-tzf", full.toString());
                Process p = pb.start();
                int exit = p.waitFor();
                if (exit == 0) {
                    showSuccess("Backup is valid");
                } else {
                    showError("Backup is corrupted");
                }
            } catch (Exception e) {
                showError("Verification failed");
            }
        } else {
            showInfo("Backup verification not supported for this format");
        }
    }

    // ========================================
    // SETTINGS FUNCTIONS
    // ========================================
    public static void settingsMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Theme",
                "Toggle Colors",
                "Toggle Animations",
                "Toggle Emojis",
                "Reset Configuration",
                "Back to main menu"
            };
            printMenu("Settings", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                toggleTheme();
                break;
            case 2:
                toggleColors();
                break;
            case 3:
                toggleAnimations();
                break;
            case 4:
                toggleEmojis();
                break;
            case 5:
                resetConfig();
                break;
            case 0:
            case 6:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void toggleTheme()
    {
        showInfo("Theme switching requires terminal emulator support");
    }

    public static void toggleColors()
    {
        COLORS_SUPPORTED = !COLORS_SUPPORTED;
        if (COLORS_SUPPORTED)
            showSuccess("Colors enabled");
        else
            showWarning("Colors disabled");
    }

    public static void toggleAnimations()
    {
        USE_ANIMATIONS = !USE_ANIMATIONS;
        if (USE_ANIMATIONS)
            showSuccess("Animations enabled");
        else
            showWarning("Animations disabled");
    }

    public static void toggleEmojis()
    {
        USE_EMOJIS = !USE_EMOJIS;
        if (USE_EMOJIS)
            showSuccess("Emojis enabled");
        else
            showWarning("Emojis disabled");
    }

    public static void resetConfig()
    {
        if (confirmAction("Reset all configuration?")) {
            try {
                Path configDir = Paths.get(HOME_DIR, CONFIG_DIR);
                if (Files.exists(configDir)) {
                    Files.walk(configDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                }
                showSuccess("Configuration reset");
                firstRunWizard();
            } catch (IOException e) {
                showError("Reset failed");
            }
        }
    }

    // ========================================
    // HELP FUNCTIONS
    // ========================================
    public static void helpMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "About",
                "Documentation",
                "Supported systems",
                "Version",
                "Back to main menu"
            };
            printMenu("Help", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                showAbout();
                break;
            case 2:
                showDocumentation();
                break;
            case 3:
                showSupportedSystems();
                break;
            case 4:
                showVersion();
                break;
            case 0:
            case 5:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void showAbout()
    {
        String about = "SysKit - Universal Unix Toolkit\n"
            + "Version: " + VERSION + "\n"
            + "Author: " + AUTHOR + "\n\n"
            + "SysKit is a comprehensive system maintenance,\n"
            + "diagnostics, and utility toolkit for Unix-like\n"
            + "operating systems.";
        printBox("About SysKit", about);
    }

    public static void showDocumentation()
    {
        String docs = BOLD + "Documentation:" + RESET + "\n\n"
            + "1. System: View system information\n"
            + "2. Monitoring: Monitor system resources\n"
            + "3. Network: Network diagnostics tools\n"
            + "4. Power: Battery information\n"
            + "5. Packages: Package management\n"
            + "6. Cleaner: System cleaning\n"
            + "7. Storage: Storage analysis\n"
            + "8. Security: Security audit\n"
            + "9. Files: File operations\n"
            + "10. Archive: Archive management\n"
            + "11. Utilities: Password/hash generation\n"
            + "12. Internet: Weather/time/calendar\n"
            + "13. Backup: Backup and restore\n"
            + "14. Settings: Configuration";
        printBox("Documentation", docs);
    }

    public static void showSupportedSystems()
    {
        String systems = BOLD + "Supported Operating Systems:" + RESET + "\n\n"
            + "🐧 Linux - All major distributions\n"
            + "  - Debian/Ubuntu (apt)\n"
            + "  - Fedora/RHEL (dnf)\n"
            + "  - Arch Linux (pacman)\n"
            + "  - openSUSE (zypper)\n"
            + "  - Void Linux (xbps)\n"
            + "  - Alpine Linux (apk)\n"
            + "  - NixOS (nix)\n"
            + "  - Gentoo (emerge)\n\n"
            + "🍎 macOS - Intel and Apple Silicon\n"
            + "  - Homebrew package manager\n\n"
            + "📱 Termux - Android terminal\n"
            + "  - Termux API support\n\n"
            + "👹 FreeBSD\n"
            + "  - pkg package manager\n\n"
            + "🦉 OpenBSD\n"
            + "  - pkg_add package manager\n\n"
            + "🌐 NetBSD\n"
            + "  - pkgin package manager";
        printBox("Supported Systems", systems);
    }

    public static void showVersion()
    {
        printBox("Version", "SysKit version " + VERSION);
    }

    // ========================================
    // MENU FUNCTIONS
    // ========================================
    public static void systemMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
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
            printMenu("System", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                showFastfetch();
                break;
            case 2:
                showSystemInfo();
                break;
            case 3:
                showHardwareInfo();
                break;
            case 4:
                showCpuInfo();
                break;
            case 5:
                showGpuInfo();
                break;
            case 6:
                showRamInfo();
                break;
            case 7:
                showMotherboardInfo();
                break;
            case 8:
                showDiskInfo();
                break;
            case 9:
                showKernelInfo();
                break;
            case 10:
                showUptime();
                break;
            case 11:
                showEnvironmentInfo();
                break;
            case 0:
            case 12:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void monitoringMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
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
            printMenu("Monitoring", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                showCpuUsage();
                break;
            case 2:
                showRamUsage();
                break;
            case 3:
                showDiskUsage();
                break;
            case 4:
                showNetworkUsage();
                break;
            case 5:
                showRunningProcesses();
                break;
            case 6:
                showTopProcesses();
                break;
            case 7:
                showResourceMonitor();
                break;
            case 8:
                showTemperature();
                break;
            case 0:
            case 9:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void networkMenu()
    {
        while (true) {
            clearScreen();
            printHeader();
            String[] items = {
                "Ping Test",
                "Internet Test",
                "DNS Test",
                "Public IP",
                "Local IP",
                "Gateway",
                "Wi-Fi Information",
                "Back to main menu"
            };
            printMenu("Network", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1: {
                System.out.print("Host to ping [google.com]: ");
                Scanner sc = new Scanner(System.in);
                String host = sc.nextLine().trim();
                if (host.isEmpty())
                    host = "google.com";
                pingTest(host);
                break;
            }
            case 2:
                internetTest();
                break;
            case 3:
                dnsTest();
                break;
            case 4:
                showPublicIp();
                break;
            case 5:
                showLocalIp();
                break;
            case 6:
                showGateway();
                break;
            case 7:
                showWifiInfo();
                break;
            case 0:
            case 8:
                return;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    // ========================================
    // MAIN
    // ========================================
    public static void initialize()
    {
        detectOS();
        detectDistro();
        detectPackageManager();

        CONFIG_PATH = Paths.get(HOME_DIR, CONFIG_DIR).toString();
        LOG_PATH = Paths.get(HOME_DIR, CONFIG_DIR, LOG_FILE).toString();

        firstRunWizard();

        showSuccess("SysKit initialized");
        showInfo("Package Manager: " + PKG_MANAGER);
        if (IS_ROOT) {
            showWarning("Running as root - be careful with destructive operations");
        }
        System.out.println();
    }

    public static void signalHandler(int sig)
    {
        System.out.println("\n" + YELLOW + EMOJI_WARNING + " Interrupted" + RESET);
        System.exit(1);
    }

    public static void mainMenu()
    {
        while (true) {
            clearScreen();
            printHeader();

            String[] items = {
                EMOJI_SYSTEM + " System",
                EMOJI_MONITOR + " Monitoring",
                EMOJI_NETWORK + " Network",
                EMOJI_POWER + " Power",
                EMOJI_PACKAGE + " Package Manager",
                EMOJI_CLEANER + " Cleaner",
                EMOJI_STORAGE + " Storage",
                EMOJI_SECURITY + " Security",
                EMOJI_FILES + " File Tools",
                EMOJI_ARCHIVE + " Archive Tools",
                EMOJI_UTILITY + " Utility Tools",
                EMOJI_INTERNET + " Internet Tools",
                EMOJI_BACKUP + " Backup",
                EMOJI_SETTINGS + " Settings",
                EMOJI_HELP + " Help",
                "Exit"
            };
            printMenu("SysKit - Main Menu", items);
            int choice = getIntInput("Select option: ");
            switch (choice) {
            case 1:
                systemMenu();
                break;
            case 2:
                monitoringMenu();
                break;
            case 3:
                networkMenu();
                break;
            case 4:
                showBatteryInfo();
                break;
            case 5:
                packageManagerMenu();
                break;
            case 6:
                cleanerMenu();
                break;
            case 7:
                storageMenu();
                break;
            case 8:
                securityMenu();
                break;
            case 9:
                fileToolsMenu();
                break;
            case 10:
                archiveMenu();
                break;
            case 11:
                utilityMenu();
                break;
            case 12:
                internetToolsMenu();
                break;
            case 13:
                backupMenu();
                break;
            case 14:
                settingsMenu();
                break;
            case 15:
                helpMenu();
                break;
            case 0:
            case 16:
                System.out.println(GREEN + EMOJI_CHECK + " Goodbye!" + RESET);
                System.exit(0);
                break;
            default:
                showError("Invalid option");
            }
            System.out.print("Press Enter to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    public static int getIntInput(String prompt)
    {
        System.out.print(prompt);
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args)
    {
        // Setup signal handler for Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n" + YELLOW + EMOJI_WARNING + " Interrupted" + RESET);
        }));

        HOME_DIR = System.getProperty("user.home");
        USER_NAME = System.getProperty("user.name");
        IS_ROOT = System.getProperty("user.name").equals("root");

        updateTerminalSize();
        detectOS();
        detectDistro();
        detectPackageManager();

        CONFIG_PATH = Paths.get(HOME_DIR, CONFIG_DIR).toString();
        LOG_PATH = Paths.get(HOME_DIR, CONFIG_DIR, LOG_FILE).toString();

        initialize();
        mainMenu();
    }
}
