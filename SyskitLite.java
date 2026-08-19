// SyskitLite.java - Termux Edition
// Author: AnshLabs716
// Version: 2.0.2-termux
//
// Lightweight Java version for Termux.
// Run: javac SyskitLite.java && java SyskitLite

import java.io.*;
import java.lang.management.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;
import java.text.*;
import java.util.*;

public class SyskitLite {
    // ========================================
    // Global Configuration
    // ========================================
    public static final String VERSION = "2.0.2-termux";
    public static final String AUTHOR = "AnshLabs716";

    // Colors
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";
    public static final String BOLD = "\033[1m";
    public static final String DIM = "\033[2m";
    public static final String RESET = "\033[0m";

    // Emojis
    public static final String EMOJI_PACKAGE = "📦";
    public static final String EMOJI_CLEANER = "🧹";
    public static final String EMOJI_STORAGE = "💾";
    public static final String EMOJI_UTILITY = "🔑";
    public static final String EMOJI_INTERNET = "🌤️";
    public static final String EMOJI_SECURITY = "🔐";
    public static final String EMOJI_BACKUP = "💾";
    public static final String EMOJI_HELP = "❓";
    public static final String EMOJI_CHECK = "✅";
    public static final String EMOJI_WARNING = "⚠️";
    public static final String EMOJI_ERROR = "❌";
    public static final String EMOJI_INFO = "ℹ️";

    public static int TERMINAL_WIDTH = 80;

    // ========================================
    // Helper Functions
    // ========================================

    public static void clear()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printBox(String title, String content)
    {
        int maxWidth = TERMINAL_WIDTH - 4;
        if (maxWidth > 50)
            maxWidth = 50;
        if (maxWidth < 20)
            maxWidth = 20;

        // Split and wrap
        String[] lines = content.split("\n");
        List<String> wrapped = new ArrayList<>();
        for (String line : lines) {
            if (line.length() <= maxWidth) {
                wrapped.add(line);
            } else {
                String[] words = line.split(" ");
                StringBuilder cur = new StringBuilder();
                for (String w : words) {
                    if (cur.length() + w.length() + 1 <= maxWidth) {
                        if (cur.length() > 0)
                            cur.append(" ");
                        cur.append(w);
                    } else {
                        if (cur.length() > 0)
                            wrapped.add(cur.toString());
                        cur = new StringBuilder(w);
                    }
                }
                if (cur.length() > 0)
                    wrapped.add(cur.toString());
            }
        }
        while (!wrapped.isEmpty() && wrapped.get(wrapped.size() - 1).isEmpty()) {
            wrapped.remove(wrapped.size() - 1);
        }

        // Borders
        System.out.println(BOLD + BLUE + "┌"
            + "─".repeat(maxWidth) + "┐" + RESET);

        if (title != null && !title.isEmpty()) {
            int titleLen = title.replaceAll("\u001B\\[[;\\d]*m", "").length();
            System.out.print(BOLD + BLUE + "│" + RESET + " ");
            System.out.print(BOLD + WHITE + title + RESET);
            System.out.print(" ".repeat(Math.max(0, maxWidth - titleLen - 1)));
            System.out.println(BOLD + BLUE + "│" + RESET);
            System.out.println(BOLD + BLUE + "├"
                + "─".repeat(maxWidth) + "┤" + RESET);
        }

        for (String line : wrapped) {
            int lineLen = line.replaceAll("\u001B\\[[;\\d]*m", "").length();
            System.out.print(BOLD + BLUE + "│" + RESET + " ");
            System.out.print(line);
            System.out.print(" ".repeat(Math.max(0, maxWidth - lineLen - 1)));
            System.out.println(BOLD + BLUE + "│" + RESET);
        }

        System.out.println(BOLD + BLUE + "└"
            + "─".repeat(maxWidth) + "┘" + RESET);
    }

    public static void printMenu(String title, String[] items)
    {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            content.append(BOLD).append(GREEN).append(i + 1).append(".").append(RESET).append(" ").append(items[i]).append("\n");
        }
        content.append("\n").append(DIM).append("Type number. 0 to exit.").append(RESET);
        printBox(title, content.toString());
    }

    public static void showSuccess(String msg) { System.out.println(GREEN + EMOJI_CHECK + " " + msg + RESET); }
    public static void showWarning(String msg) { System.out.println(YELLOW + EMOJI_WARNING + " " + msg + RESET); }
    public static void showError(String msg) { System.out.println(RED + EMOJI_ERROR + " " + msg + RESET); }
    public static void showInfo(String msg) { System.out.println(BLUE + EMOJI_INFO + " " + msg + RESET); }

    public static boolean confirmAction(String prompt)
    {
        System.out.println(YELLOW + EMOJI_WARNING + " " + prompt + RESET);
        System.out.print("Continue? [y/N] ");
        try {
            Scanner sc = new Scanner(System.in);
            String resp = sc.nextLine().trim().toLowerCase();
            return resp.equals("y") || resp.equals("yes");
        } catch (Exception e) {
            return false;
        }
    }

    public static void runCmd(String cmd)
    {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", cmd);
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            showError("Command failed: " + e.getMessage());
        }
    }

    public static String runCmdCapture(String cmd)
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
            return output.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean checkCommand(String cmd)
    {
        try {
            ProcessBuilder pb = new ProcessBuilder("which", cmd);
            Process p = pb.start();
            return p.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static void updateTerminalSize()
    {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", "stty size");
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.trim().split(" ");
                if (parts.length == 2) {
                    TERMINAL_WIDTH = Integer.parseInt(parts[1]);
                }
            }
        } catch (Exception e) {
            TERMINAL_WIDTH = 80;
        }
    }

    // ========================================
    // Quick Setup
    // ========================================

    public static void quickSetup()
    {
        clear();
        System.out.println(BOLD + CYAN);
        System.out.println("  _____     _    _  _____ _  __ ");
        System.out.println(" / ____|   | |  | |/ ____| |/ / ");
        System.out.println("| (___   __| |  | | (___ | ' /  ");
        System.out.println(" \\___ \\ / _` |  | |\\___ \\|  <   ");
        System.out.println(" ____) | (_| |__| |____) | . \\  ");
        System.out.println("|_____/ \\__,_\\____/|_____/|_|\\_\\ ");
        System.out.println(RESET);
        System.out.println(BOLD + WHITE + "by " + AUTHOR + RESET);
        System.out.println(DIM + "Termux Optimized v" + VERSION + RESET);
        System.out.println();
        System.out.println(GREEN + EMOJI_CHECK + " Termux detected!" + RESET);
        System.out.println(BLUE + EMOJI_INFO + " Press Enter to continue..." + RESET);
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }

    // ========================================
    // System Information
    // ========================================

    public static void showSystemInfo()
    {
        String info = "";
        info += "Hostname: " + runCmdCapture("hostname") + "\n";
        info += "OS: Termux\n";
        info += "Android: " + runCmdCapture("getprop ro.build.version.release") + "\n";
        info += "Kernel: " + System.getProperty("os.version") + "\n";
        info += "Arch: " + System.getProperty("os.arch") + "\n";
        info += "Uptime: " + runCmdCapture("uptime -p") + "\n";
        info += "Storage: " + runCmdCapture("df -h /data 2>/dev/null | awk 'NR==2{print $2\" total \" $3\" used\"}'") + "\n";
        info += "RAM: " + runCmdCapture("free -h 2>/dev/null | awk 'NR==2{print $2\" total \"$3\" used\"}'");
        printBox("System Info", info);
    }

    public static void showHardwareInfo()
    {
        String info = "";
        info += "Device: " + runCmdCapture("getprop ro.product.model") + "\n";
        info += "Manufacturer: " + runCmdCapture("getprop ro.product.manufacturer") + "\n";
        info += "Android: " + runCmdCapture("getprop ro.build.version.release") + "\n";
        info += "SDK: " + runCmdCapture("getprop ro.build.version.sdk") + "\n";
        info += "CPU: " + System.getProperty("os.arch") + "\n";
        info += "Cores: " + runCmdCapture("nproc");
        printBox("Hardware Info", info);
    }

    public static void showCpuInfo()
    {
        String info = "";
        info += "Architecture: " + System.getProperty("os.arch") + "\n";
        info += "Cores: " + runCmdCapture("nproc") + "\n";
        info += "CPU Info: " + runCmdCapture("cat /proc/cpuinfo 2>/dev/null | grep Hardware | head -1 | cut -d: -f2 | xargs") + "\n";
        info += "Features: " + runCmdCapture("cat /proc/cpuinfo 2>/dev/null | grep Features | head -1 | cut -d: -f2 | xargs | cut -c1-50");
        printBox("CPU Info", info);
    }

    public static void showRamInfo()
    {
        String info = "";
        if (checkCommand("free")) {
            String total = runCmdCapture("free -h | awk 'NR==2{print $2}'");
            String used = runCmdCapture("free -h | awk 'NR==2{print $3}'");
            String free = runCmdCapture("free -h | awk 'NR==2{print $4}'");
            String swap = runCmdCapture("free -h | awk 'NR==3{print $2\" total \" $3\" used\"}'");
            info += "Total: " + total + "\nUsed: " + used + "\nFree: " + free + "\nSwap: " + (swap.isEmpty() ? "N/A" : swap);
        } else {
            info = "free command not available";
        }
        printBox("RAM Info", info);
    }

    public static void showBattery()
    {
        String info = "";
        if (checkCommand("termux-battery-status")) {
            String output = runCmdCapture("termux-battery-status");
            try {
                // crude JSON parse
                String perc = output.replaceAll(".*\"percentage\":\\s*(\\d+).*", "$1");
                String status = output.replaceAll(".*\"status\":\\s*\"([^\"]+)\".*", "$1");
                String health = output.replaceAll(".*\"health\":\\s*\"([^\"]+)\".*", "$1");
                String temp = output.replaceAll(".*\"temperature\":\\s*(\\d+).*", "$1");
                if (!perc.equals(output)) {
                    info += "Battery: " + perc + "%\n";
                    info += "Status: " + status + "\n";
                    info += "Health: " + health + "\n";
                    info += "Temperature: " + temp + "°C";
                } else {
                    info = output;
                }
            } catch (Exception e) {
                info = "Could not parse battery data";
            }
        } else {
            info = "Install termux-api for battery info";
            if (confirmAction("Install termux-api?")) {
                runCmd("pkg install termux-api -y");
                if (checkCommand("termux-battery-status")) {
                    showBattery();
                    return;
                }
            }
        }
        printBox("Battery", info);
    }

    public static void showNetworkInfo()
    {
        String info = "";
        String ip = runCmdCapture("ip addr show 2>/dev/null | grep 'inet ' | grep -v '127.0.0.1' | awk '{print $2}' | head -1");
        String gateway = runCmdCapture("ip route 2>/dev/null | grep default | awk '{print $3}'");
        info += "IP: " + (ip.isEmpty() ? "Unknown" : ip) + "\n";
        info += "Gateway: " + (gateway.isEmpty() ? "Unknown" : gateway);
        printBox("Network Info", info);
    }

    public static void showWifi()
    {
        String info = "";
        if (checkCommand("termux-wifi-connectioninfo")) {
            info = runCmdCapture("termux-wifi-connectioninfo");
        } else {
            info = "Install termux-api for Wi-Fi info";
            if (confirmAction("Install termux-api?")) {
                runCmd("pkg install termux-api -y");
                if (checkCommand("termux-wifi-connectioninfo")) {
                    showWifi();
                    return;
                }
            }
        }
        printBox("Wi-Fi", info);
    }

    public static void internetTest()
    {
        String info = "";
        showInfo("Testing internet connection...");
        try {
            ProcessBuilder pb = new ProcessBuilder("ping", "-c", "1", "-W", "2", "8.8.8.8");
            Process p = pb.start();
            int exit = p.waitFor();
            if (exit == 0) {
                info += "Internet: ✅ Connected\n";
                String ip = runCmdCapture("curl -s ifconfig.me");
                info += "Public IP: " + (ip.isEmpty() ? "N/A" : ip);
            } else {
                info = "Internet: ❌ Disconnected";
            }
        } catch (Exception e) {
            info = "Internet Test failed";
        }
        printBox("Internet Test", info);
    }

    // ========================================
    // Package Manager
    // ========================================

    public static void packageMenu()
    {
        while (true) {
            clear();
            System.out.println(BOLD + CYAN + EMOJI_PACKAGE + " Package Manager" + RESET + "\n");
            printMenu("Packages", new String[] { "Update", "Upgrade", "Clean cache", "Autoremove", "List installed", "Search", "Back" });
            String choice = getInput("Select: ");
            if (choice.equals("1")) {
                showInfo("Updating...");
                runCmd("pkg update");
                showSuccess("Done");
                waitEnter();
            } else if (choice.equals("2")) {
                showInfo("Upgrading...");
                runCmd("pkg upgrade -y");
                showSuccess("Done");
                waitEnter();
            } else if (choice.equals("3")) {
                showInfo("Cleaning...");
                runCmd("pkg clean");
                showSuccess("Done");
                waitEnter();
            } else if (choice.equals("4")) {
                showInfo("Autoremoving...");
                runCmd("pkg autoclean");
                showSuccess("Done");
                waitEnter();
            } else if (choice.equals("5")) {
                runCmd("pkg list-installed");
                waitEnter();
            } else if (choice.equals("6")) {
                System.out.print("Search: ");
                String term = new Scanner(System.in).nextLine().trim();
                runCmd("pkg search " + term);
                waitEnter();
            } else if (choice.equals("0") || choice.equals("7")) {
                break;
            } else {
                showError("Invalid option");
                waitEnter();
            }
        }
    }

    // ========================================
    // Cleaner
    // ========================================

    public static void cleanerMenu()
    {
        while (true) {
            clear();
            System.out.println(BOLD + CYAN + EMOJI_CLEANER + " Cleaner" + RESET + "\n");
            printMenu("Cleaner", new String[] { "User cache", "Temp files", "Package cache", "Trash", "Back" });
            String choice = getInput("Select: ");
            if (choice.equals("1")) {
                if (confirmAction("Clean cache?")) {
                    runCmd("rm -rf ~/.cache/*");
                    showSuccess("Cache cleaned");
                }
                waitEnter();
            } else if (choice.equals("2")) {
                if (confirmAction("Clean temp?")) {
                    runCmd("rm -rf /tmp/*");
                    showSuccess("Temp cleaned");
                }
                waitEnter();
            } else if (choice.equals("3")) {
                if (confirmAction("Clean package cache?")) {
                    runCmd("pkg clean");
                    showSuccess("Package cache cleaned");
                }
                waitEnter();
            } else if (choice.equals("4")) {
                if (new File(System.getProperty("user.home") + "/.local/share/Trash").exists()) {
                    if (confirmAction("Empty trash?")) {
                        runCmd("rm -rf ~/.local/share/Trash/*");
                        showSuccess("Trash emptied");
                    }
                } else {
                    showWarning("No trash found");
                }
                waitEnter();
            } else if (choice.equals("0") || choice.equals("5")) {
                break;
            } else {
                showError("Invalid option");
                waitEnter();
            }
        }
    }

    // ========================================
    // Storage
    // ========================================

    public static void storageMenu()
    {
        while (true) {
            clear();
            System.out.println(BOLD + CYAN + EMOJI_STORAGE + " Storage" + RESET + "\n");
            printMenu("Storage", new String[] { "Disk usage", "Largest directories", "Largest files", "Mounted drives", "Back" });
            String choice = getInput("Select: ");
            if (choice.equals("1")) {
                clear();
                System.out.println(BOLD + CYAN + "Disk Usage" + RESET + "\n");
                runCmd("df -h");
                waitEnter();
            } else if (choice.equals("2")) {
                clear();
                System.out.println(BOLD + CYAN + "Largest Directories" + RESET + "\n");
                runCmd("du -sh ~/* 2>/dev/null | sort -hr | head -10");
                waitEnter();
            } else if (choice.equals("3")) {
                clear();
                System.out.println(BOLD + CYAN + "Largest Files" + RESET + "\n");
                runCmd("find ~ -type f -exec du -h {} + 2>/dev/null | sort -hr | head -10");
                waitEnter();
            } else if (choice.equals("4")) {
                clear();
                System.out.println(BOLD + CYAN + "Mounted Drives" + RESET + "\n");
                runCmd("mount 2>/dev/null | grep '/data' || mount 2>/dev/null | head -10");
                waitEnter();
            } else if (choice.equals("0") || choice.equals("5")) {
                break;
            } else {
                showError("Invalid option");
                waitEnter();
            }
        }
    }

    // ========================================
    // Utilities
    // ========================================

    public static void utilityMenu()
    {
        while (true) {
            clear();
            System.out.println(BOLD + CYAN + EMOJI_UTILITY + " Utilities" + RESET + "\n");
            printMenu("Utilities", new String[] { "Password Generator", "UUID Generator", "Hash (MD5/SHA)", "Random String", "Back" });
            String choice = getInput("Select: ");
            if (choice.equals("1")) {
                String lenStr = getInput("Length [16]: ");
                int length = lenStr.isEmpty() ? 16 : Integer.parseInt(lenStr);
                String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
                Random rand = new SecureRandom();
                StringBuilder pw = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    pw.append(chars.charAt(rand.nextInt(chars.length())));
                }
                printBox("Password", pw.toString());
                waitEnter();
            } else if (choice.equals("2")) {
                String uuid;
                if (checkCommand("uuidgen")) {
                    uuid = runCmdCapture("uuidgen");
                } else {
                    uuid = runCmdCapture("cat /proc/sys/kernel/random/uuid");
                }
                printBox("UUID", uuid.isEmpty() ? "N/A" : uuid);
                waitEnter();
            } else if (choice.equals("3")) {
                System.out.print("Text to hash: ");
                String text = new Scanner(System.in).nextLine().trim();
                String md5 = runCmdCapture("echo -n '" + text + "' | md5sum | awk '{print $1}'");
                String sha1 = runCmdCapture("echo -n '" + text + "' | sha1sum | awk '{print $1}'");
                String sha256 = runCmdCapture("echo -n '" + text + "' | sha256sum | awk '{print $1}'");
                System.out.println("MD5: " + md5);
                System.out.println("SHA1: " + sha1);
                System.out.println("SHA256: " + sha256);
                waitEnter();
            } else if (choice.equals("4")) {
                String lenStr = getInput("Length [16]: ");
                int length = lenStr.isEmpty() ? 16 : Integer.parseInt(lenStr);
                String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                Random rand = new SecureRandom();
                StringBuilder randStr = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    randStr.append(chars.charAt(rand.nextInt(chars.length())));
                }
                printBox("Random String", randStr.toString());
                waitEnter();
            } else if (choice.equals("0") || choice.equals("5")) {
                break;
            } else {
                showError("Invalid option");
                waitEnter();
            }
        }
    }

    // ========================================
    // Internet
    // ========================================

    public static void internetMenu()
    {
        while (true) {
            clear();
            System.out.println(BOLD + CYAN + EMOJI_INTERNET + " Internet" + RESET + "\n");
            printMenu("Internet", new String[] { "Weather", "Time", "Calendar", "Internet Test", "Public IP", "Back" });
            String choice = getInput("Select: ");
            if (choice.equals("1")) {
                String city = getInput("City [London]: ");
                if (city.isEmpty())
                    city = "London";
                String weather = runCmdCapture("curl -s 'wttr.in/" + city + "?format=%c+%t+%w'");
                printBox("Weather", "Weather for " + city + ":\n" + (weather.isEmpty() ? "Unable to fetch" : weather));
                waitEnter();
            } else if (choice.equals("2")) {
                Date now = new Date();
                SimpleDateFormat dateFmt = new SimpleDateFormat("EEEE, MMMM d, yyyy");
                SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm:ss a");
                printBox("Time", "Date: " + dateFmt.format(now) + "\nTime: " + timeFmt.format(now) + "\nTimezone: " + TimeZone.getDefault().getDisplayName());
                waitEnter();
            } else if (choice.equals("3")) {
                String cal = runCmdCapture("cal -3");
                printBox("Calendar", cal.isEmpty() ? "Calendar not available" : cal);
                waitEnter();
            } else if (choice.equals("4")) {
                internetTest();
                waitEnter();
            } else if (choice.equals("5")) {
                String ip = runCmdCapture("curl -s ifconfig.me");
                printBox("Public IP", ip.isEmpty() ? "N/A" : ip);
                waitEnter();
            } else if (choice.equals("0") || choice.equals("6")) {
                break;
            } else {
                showError("Invalid option");
                waitEnter();
            }
        }
    }

    // ========================================
    // Security
    // ========================================

    public static void securityMenu()
    {
        while (true) {
            clear();
            System.out.println(BOLD + CYAN + EMOJI_SECURITY + " Security" + RESET + "\n");
            printMenu("Security", new String[] { "Open ports", "Running processes", "Security tips", "Back" });
            String choice = getInput("Select: ");
            if (choice.equals("1")) {
                clear();
                System.out.println(BOLD + CYAN + "Open Ports" + RESET + "\n");
                runCmd("netstat -tuln 2>/dev/null | grep LISTEN | head -10");
                waitEnter();
            } else if (choice.equals("2")) {
                clear();
                System.out.println(BOLD + CYAN + "Running Processes" + RESET + "\n");
                runCmd("ps aux 2>/dev/null | head -15");
                waitEnter();
            } else if (choice.equals("3")) {
                printBox("Security Tips",
                    "1. Keep Termux updated\n"
                        + "2. Use strong passwords\n"
                        + "3. Don't run as root\n"
                        + "4. Check permissions\n"
                        + "5. Use VPN\n"
                        + "6. Install termux-api for security features");
                waitEnter();
            } else if (choice.equals("0") || choice.equals("4")) {
                break;
            } else {
                showError("Invalid option");
                waitEnter();
            }
        }
    }

    // ========================================
    // Backup
    // ========================================

    public static void backupMenu()
    {
        while (true) {
            clear();
            System.out.println(BOLD + CYAN + EMOJI_BACKUP + " Backup" + RESET + "\n");
            printMenu("Backup", new String[] { "Backup folder", "Restore backup", "Back" });
            String choice = getInput("Select: ");
            if (choice.equals("1")) {
                String folder = getInput("Folder to backup: ");
                if (new File(folder).exists()) {
                    String backupName = "backup_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".tar.gz";
                    showInfo("Creating backup...");
                    runCmd("tar -czf $HOME/" + backupName + " '" + folder + "'");
                    showSuccess("Backup saved: $HOME/" + backupName);
                } else {
                    showError("Folder not found");
                }
                waitEnter();
            } else if (choice.equals("2")) {
                runCmd("ls -la ~/backup_*.tar.gz 2>/dev/null || echo 'No backups found'");
                String backupFile = getInput("Enter backup name to restore: ");
                if (new File(System.getProperty("user.home") + "/" + backupFile).exists()) {
                    String restoreDir = getInput("Restore to: ");
                    new File(restoreDir).mkdirs();
                    runCmd("tar -xzf ~/" + backupFile + " -C " + restoreDir);
                    showSuccess("Restored to " + restoreDir);
                } else {
                    showError("Backup not found");
                }
                waitEnter();
            } else if (choice.equals("0") || choice.equals("3")) {
                break;
            } else {
                showError("Invalid option");
                waitEnter();
            }
        }
    }

    // ========================================
    // Help
    // ========================================

    public static void helpMenu()
    {
        while (true) {
            clear();
            System.out.println(BOLD + CYAN + EMOJI_HELP + " Help" + RESET + "\n");
            printMenu("Help", new String[] { "About", "Supported", "Version", "Back" });
            String choice = getInput("Select: ");
            if (choice.equals("1")) {
                printBox("About",
                    "SysKit - Termux Edition\n"
                        + "By " + AUTHOR + "\n"
                        + "Unix Toolkit for Termux\n\n"
                        + "Features:\n"
                        + "- System Info\n"
                        + "- Battery\n"
                        + "- Wi-Fi\n"
                        + "- Package Manager\n"
                        + "- Cleaner\n"
                        + "- Utilities\n"
                        + "- Internet Tools");
                waitEnter();
            } else if (choice.equals("2")) {
                printBox("Supported",
                    "Termux\n"
                        + "Android 5.0+\n"
                        + "pkg package manager\n"
                        + "ARM/ARM64/x86_64");
                waitEnter();
            } else if (choice.equals("3")) {
                printBox("Version", "SysKit v" + VERSION);
                waitEnter();
            } else if (choice.equals("0") || choice.equals("4")) {
                break;
            } else {
                showError("Invalid option");
                waitEnter();
            }
        }
    }

    // ========================================
    // Fastfetch
    // ========================================

    public static void showFastfetch()
    {
        if (checkCommand("fastfetch")) {
            runCmd("fastfetch");
        } else {
            showWarning("Fastfetch not installed");
            if (confirmAction("Install fastfetch?")) {
                runCmd("pkg install fastfetch -y");
                if (checkCommand("fastfetch")) {
                    runCmd("fastfetch");
                } else {
                    showError("Fastfetch installation failed");
                }
            }
        }
        waitEnter();
    }

    // ========================================
    // Main Menu
    // ========================================

    public static void mainMenu()
    {
        while (true) {
            clear();
            System.out.println(BOLD + CYAN);
            System.out.println("  _____     _    _  _____ _  __ ");
            System.out.println(" / ____|   | |  | |/ ____| |/ / ");
            System.out.println("| (___   __| |  | | (___ | ' /  ");
            System.out.println(" \\___ \\ / _` |  | |\\___ \\|  <   ");
            System.out.println(" ____) | (_| |__| |____) | . \\  ");
            System.out.println("|_____/ \\__,_\\____/|_____/|_|\\_\\ ");
            System.out.println(RESET);
            System.out.println(BOLD + WHITE + "by " + AUTHOR + RESET);
            System.out.println(DIM + "Termux v" + VERSION + RESET + "\n");

            printMenu("SysKit - Termux", new String[] { "System Info", "Hardware Info", "CPU Info", "RAM Info", "Battery", "Network Info", "Wi-Fi", "Fastfetch ⚡", "Package Manager", "Cleaner", "Storage", "Utilities", "Internet", "Security", "Backup", "Help", "Exit" });

            String choice = getInput("Select option: ");
            if (choice.equals("1")) {
                showSystemInfo();
                waitEnter();
            } else if (choice.equals("2")) {
                showHardwareInfo();
                waitEnter();
            } else if (choice.equals("3")) {
                showCpuInfo();
                waitEnter();
            } else if (choice.equals("4")) {
                showRamInfo();
                waitEnter();
            } else if (choice.equals("5")) {
                showBattery();
                waitEnter();
            } else if (choice.equals("6")) {
                showNetworkInfo();
                waitEnter();
            } else if (choice.equals("7")) {
                showWifi();
                waitEnter();
            } else if (choice.equals("8")) {
                showFastfetch();
            } else if (choice.equals("9")) {
                packageMenu();
            } else if (choice.equals("10")) {
                cleanerMenu();
            } else if (choice.equals("11")) {
                storageMenu();
            } else if (choice.equals("12")) {
                utilityMenu();
            } else if (choice.equals("13")) {
                internetMenu();
            } else if (choice.equals("14")) {
                securityMenu();
            } else if (choice.equals("15")) {
                backupMenu();
            } else if (choice.equals("16")) {
                helpMenu();
            } else if (choice.equals("0") || choice.equals("17")) {
                System.out.println(GREEN + EMOJI_CHECK + " Goodbye!" + RESET);
                System.exit(0);
            } else {
                showError("Invalid option");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
    }

    // ========================================
    // Helpers
    // ========================================

    public static String getInput(String prompt)
    {
        System.out.print(prompt);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    public static void waitEnter()
    {
        System.out.print("Press Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }

    // ========================================
    // Main
    // ========================================

    public static void main(String[] args)
    {
        updateTerminalSize();
        quickSetup();
        mainMenu();
    }
}
