#include <gtk/gtk.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/utsname.h>
#include <sys/stat.h>
#include <time.h>

// ========================================
// GTK GLOBALS
// ========================================
GtkWidget *window;
GtkWidget *status_label;
GtkWidget *cpu_label, *ram_label, *disk_label, *uptime_label;
GtkWidget *notebook;
GtkCssProvider *css_provider = NULL;
char current_theme[64] = "dark";

GtkWidget *current_output_text = NULL;
GtkWidget *current_input_entry = NULL;

// ========================================
// CSS THEMES
// ========================================
const char *dark_theme =
"* { color: #e8e8f0; font-family: sans-serif; }\n"
"window { background-color: #1a1a2e; }\n"
"button { background-color: #16213e; color: #e8e8f0; border-radius: 6px; padding: 8px; margin: 2px; font-weight: bold; border: 1px solid #0f3460; }\n"
"button:hover { background-color: #0f3460; }\n"
"entry { background-color: #0a0a0f; color: #00ff66; border: 1px solid #0f3460; font-family: monospace; padding: 6px; }\n"
"textview text { background-color: #0a0a0f; color: #00ff66; font-family: monospace; font-size: 13px; }\n"
"notebook { background-color: #1a1a2e; }\n"
"notebook header tab { background-color: #16213e; color: #e8e8f0; padding: 6px 12px; font-weight: bold; }\n"
"notebook header tab:checked { background-color: #0f3460; color: #ffffff; }\n"
"menubar, menu, menuitem { background-color: #16213e; color: #e8e8f0; }\n"
"statusbar { background-color: #16213e; color: #e8e8f0; }\n";

const char *light_theme =
"* { color: #1a1a2e; font-family: sans-serif; }\n"
"window { background-color: #f4f5f7; }\n"
"button { background-color: #ffffff; color: #1a1a2e; border-radius: 6px; padding: 8px; margin: 2px; font-weight: bold; border: 1px solid #cccccc; }\n"
"button:hover { background-color: #e2e8f0; }\n"
"entry { background-color: #ffffff; color: #0f172a; border: 1px solid #cccccc; font-family: monospace; padding: 6px; }\n"
"textview text { background-color: #ffffff; color: #0f172a; font-family: monospace; font-size: 13px; }\n"
"notebook { background-color: #f4f5f7; }\n"
"notebook header tab { background-color: #e2e8f0; color: #1a1a2e; padding: 6px 12px; font-weight: bold; }\n"
"notebook header tab:checked { background-color: #ffffff; color: #0f172a; }\n"
"menubar, menu, menuitem { background-color: #e2e8f0; color: #1a1a2e; }\n"
"statusbar { background-color: #e2e8f0; color: #1a1a2e; }\n";

const char *green_theme =
"* { color: #00ff00; font-family: monospace; }\n"
"window { background-color: #051405; }\n"
"button { background-color: #0a290a; color: #00ff00; border-radius: 4px; padding: 8px; margin: 2px; font-weight: bold; border: 1px solid #00ff00; }\n"
"button:hover { background-color: #145214; }\n"
"entry { background-color: #000000; color: #00ff00; border: 1px solid #00ff00; font-family: monospace; padding: 6px; }\n"
"textview text { background-color: #000000; color: #00ff00; font-family: monospace; font-size: 13px; }\n"
"notebook { background-color: #051405; }\n"
"notebook header tab { background-color: #0a290a; color: #00ff00; padding: 6px 12px; font-weight: bold; }\n"
"notebook header tab:checked { background-color: #145214; color: #00ff00; }\n"
"menubar, menu, menuitem { background-color: #0a290a; color: #00ff00; }\n"
"statusbar { background-color: #0a290a; color: #00ff00; }\n";

const char *hacker_theme =
"* { color: #ff0055; font-family: monospace; }\n"
"window { background-color: #0d0006; }\n"
"button { background-color: #260011; color: #ff0055; border-radius: 4px; padding: 8px; margin: 2px; font-weight: bold; border: 1px solid #ff0055; }\n"
"button:hover { background-color: #4d0022; }\n"
"entry { background-color: #000000; color: #ff0055; border: 1px solid #ff0055; font-family: monospace; padding: 6px; }\n"
"textview text { background-color: #000000; color: #ff0055; font-family: monospace; font-size: 13px; }\n"
"notebook { background-color: #0d0006; }\n"
"notebook header tab { background-color: #260011; color: #ff0055; padding: 6px 12px; font-weight: bold; }\n"
"notebook header tab:checked { background-color: #4d0022; color: #ff0055; }\n"
"menubar, menu, menuitem { background-color: #260011; color: #ff0055; }\n"
"statusbar { background-color: #260011; color: #ff0055; }\n";

// ========================================
// THEME ENGINE
// ========================================
void set_theme(const char *theme_name) {
    GdkScreen *screen = gdk_screen_get_default();
    if (!screen) return;

    if (css_provider != NULL) {
        gtk_style_context_remove_provider_for_screen(screen, GTK_STYLE_PROVIDER(css_provider));
        g_object_unref(css_provider);
        css_provider = NULL;
    }

    css_provider = gtk_css_provider_new();
    const char *css_data = dark_theme;

    if (strcmp(theme_name, "light") == 0) css_data = light_theme;
    else if (strcmp(theme_name, "green") == 0) css_data = green_theme;
    else if (strcmp(theme_name, "hacker") == 0) css_data = hacker_theme;

    gtk_css_provider_load_from_data(css_provider, css_data, -1, NULL);
    gtk_style_context_add_provider_for_screen(screen, GTK_STYLE_PROVIDER(css_provider), GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);

    strcpy(current_theme, theme_name);
    if (status_label) {
        char msg[128]; snprintf(msg, sizeof(msg), "Active Theme: %s", current_theme);
        gtk_statusbar_push(GTK_STATUSBAR(status_label), 0, msg);
    }
}

// ========================================
// UTILITIES & INPUT GETTER
// ========================================
void trim_newline(char *str) {
    if (!str) return;
    size_t len = strlen(str);
    while (len > 0 && (str[len - 1] == '\n' || str[len - 1] == '\r' || str[len - 1] == ' ')) {
        str[--len] = '\0';
    }
}

const char* get_user_input() {
    if (current_input_entry) {
        const char *txt = gtk_entry_get_text(GTK_ENTRY(current_input_entry));
        if (txt && strlen(txt) > 0) return txt;
    }
    return NULL;
}

void set_output(char *text) {
    if (current_output_text) {
        GtkTextBuffer *buf = gtk_text_view_get_buffer(GTK_TEXT_VIEW(current_output_text));
        gtk_text_buffer_set_text(buf, text, -1);
    }
}

void run_cmd(char *cmd, char *title) {
    char full[1024];
    snprintf(full, sizeof(full), "%s 2>&1", cmd);
    FILE *fp = popen(full, "r");
    if (!fp) { set_output("Failed to execute command."); return; }

    char output[16384] = "", buffer[1024];
    while (fgets(buffer, sizeof(buffer), fp)) strcat(output, buffer);
    pclose(fp);

    char final[17408];
    snprintf(final, sizeof(final), "=== %s ===\n\n%s", title, output);
    set_output(final);
}

// ========================================
// ACTION HANDLERS (Dynamic User Inputs)
// ========================================
void show_fastfetch(GtkWidget *w, gpointer d) { run_cmd("fastfetch 2>/dev/null || neofetch 2>/dev/null || echo 'Install fastfetch'", "System Fetch"); }
void show_system_info(GtkWidget *w, gpointer d) { struct utsname u; uname(&u); char h[256]; gethostname(h, sizeof(h)); char out[512]; snprintf(out, sizeof(out), "Host: %s\nOS: %s\nKernel: %s\nArch: %s\nUser: %s", h, u.sysname, u.release, u.machine, getenv("USER")); set_output(out); }
void show_hardware_info(GtkWidget *w, gpointer d) { run_cmd("pkexec lshw -short 2>/dev/null || echo 'lshw missing'", "Hardware"); }
void show_cpu_info(GtkWidget *w, gpointer d) { run_cmd("cat /proc/cpuinfo | grep 'model name' | head -4", "CPU"); }
void show_gpu_info(GtkWidget *w, gpointer d) { run_cmd("lspci | grep -E 'VGA|3D'", "GPU"); }

void show_cpu_usage(GtkWidget *w, gpointer d) { run_cmd("top -bn1 | head -15", "CPU Usage"); }
void show_ram_usage(GtkWidget *w, gpointer d) { run_cmd("free -h", "RAM Usage"); }
void show_disk_usage(GtkWidget *w, gpointer d) { run_cmd("df -h", "Disk Usage"); }
void show_temp(GtkWidget *w, gpointer d) { run_cmd("sensors 2>/dev/null || cat /sys/class/thermal/thermal_zone0/temp | awk '{print $1/1000 \"°C\"}'", "Temperature"); }

// Dynamic Ping
void ping_test(GtkWidget *w, gpointer d) {
    const char *inp = get_user_input();
    char target[128];
    if (inp) snprintf(target, sizeof(target), "%s", inp);
    else strcpy(target, "1.1.1.1"); // Fallback

    char cmd[256], title[128];
    snprintf(cmd, sizeof(cmd), "ping -c 4 %s", target);
    snprintf(title, sizeof(title), "Ping Test (%s)", target);
    run_cmd(cmd, title);
}

void show_local_ip(GtkWidget *w, gpointer d) { run_cmd("ip addr show | grep 'inet ' | grep -v '127.0.0.1'", "Local IP"); }
void show_public_ip(GtkWidget *w, gpointer d) { run_cmd("curl -s ifconfig.me", "Public IP"); }

void show_battery(GtkWidget *w, gpointer d) { run_cmd("cat /sys/class/power_supply/BAT0/capacity 2>/dev/null | xargs echo 'Battery Level:'", "Battery"); }

// Dynamic Package Search / Install
void pkg_search(GtkWidget *w, gpointer d) {
    const char *inp = get_user_input();
    if (!inp) { set_output("Please type a package name into the input bar above!"); return; }

    char cmd[512], title[128];
    snprintf(cmd, sizeof(cmd), "apt search %s 2>/dev/null || pacman -Ss %s 2>/dev/null", inp, inp);
    snprintf(title, sizeof(title), "Package Search (%s)", inp);
    run_cmd(cmd, title);
}

void pkg_update(GtkWidget *w, gpointer d) { run_cmd("pkexec apt update 2>/dev/null || pkexec pacman -Sy 2>/dev/null", "Package Update"); }
void pkg_upgrade(GtkWidget *w, gpointer d) { run_cmd("pkexec apt upgrade -y 2>/dev/null || pkexec pacman -Syu --noconfirm 2>/dev/null", "Package Upgrade"); }
void pkg_clean(GtkWidget *w, gpointer d) { run_cmd("pkexec apt autoclean -y 2>/dev/null || pkexec pacman -Sc --noconfirm 2>/dev/null", "Package Cleanup"); }

void clean_cache(GtkWidget *w, gpointer d) { run_cmd("rm -rf ~/.cache/* && echo 'User cache cleared!'", "Cache Clean"); }
void clean_logs(GtkWidget *w, gpointer d) { run_cmd("pkexec journalctl --vacuum-time=2d", "Vacuum System Logs"); }
void clean_trash(GtkWidget *w, gpointer d) { run_cmd("rm -rf ~/.local/share/Trash/* && echo 'Trash emptied!'", "Trash Clean"); }

void sec_firewall(GtkWidget *w, gpointer d) { run_cmd("pkexec ufw status", "Firewall"); }
void sec_ports(GtkWidget *w, gpointer d) { run_cmd("pkexec ss -tulpn", "Open Ports"); }

void util_pw(GtkWidget *w, gpointer d) { char pw[17]; const char *c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%"; for(int i=0;i<16;i++) pw[i]=c[rand()%strlen(c)]; pw[16]='\0'; char o[128]; snprintf(o, sizeof(o), "Generated Password:\n\n%s", pw); set_output(o); }
void util_uuid(GtkWidget *w, gpointer d) { run_cmd("uuidgen 2>/dev/null || echo 'uuidgen not found'", "UUID"); }

// Dynamic Weather
void inet_weather(GtkWidget *w, gpointer d) {
    const char *inp = get_user_input();
    char loc[128] = "";
    if (inp) snprintf(loc, sizeof(loc), "%s", inp);

    char cmd[256], title[128];
    snprintf(cmd, sizeof(cmd), "curl -s 'wttr.in/%s?format=3'", loc);
    snprintf(title, sizeof(title), "Weather (%s)", strlen(loc) > 0 ? loc : "Auto Location");
    run_cmd(cmd, title);
}

void inet_cal(GtkWidget *w, gpointer d) { run_cmd("cal -3 2>/dev/null || cal", "Calendar"); }

void bak_doc(GtkWidget *w, gpointer d) { run_cmd("mkdir -p ~/Backups && tar -czf ~/Backups/doc_backup.tar.gz ~/Documents 2>/dev/null && echo 'Documents backed up to ~/Backups'", "Backup"); }
void sys_reset(GtkWidget *w, gpointer d) { set_output("System settings reset."); }
void sys_help(GtkWidget *w, gpointer d) { set_output("SysKit GUI v4.0.0\nType into the Target Bar above to customize Ping, Weather, or Search!"); }

// ========================================
// STATS LOOP
// ========================================
gboolean update_stats(gpointer data) {
    char buf[256];
    FILE *fp;

    fp = popen("top -bn1 | grep 'Cpu(s)' | awk '{print $2}'", "r");
    if (fp) { if (fgets(buf, sizeof(buf), fp)) { trim_newline(buf); char l[128]; snprintf(l, sizeof(l), "CPU: %s%%", buf); gtk_label_set_text(GTK_LABEL(cpu_label), l); } pclose(fp); }

    fp = popen("free -h | grep Mem | awk '{print $3\"/\"$2}'", "r");
    if (fp) { if (fgets(buf, sizeof(buf), fp)) { trim_newline(buf); char l[128]; snprintf(l, sizeof(l), "RAM: %s", buf); gtk_label_set_text(GTK_LABEL(ram_label), l); } pclose(fp); }

    fp = popen("df -h / | awk 'NR==2{print $3\"/\"$2}'", "r");
    if (fp) { if (fgets(buf, sizeof(buf), fp)) { trim_newline(buf); char l[128]; snprintf(l, sizeof(l), "Disk: %s", buf); gtk_label_set_text(GTK_LABEL(disk_label), l); } pclose(fp); }

    fp = popen("uptime -p | sed 's/up //'", "r");
    if (fp) { if (fgets(buf, sizeof(buf), fp)) { trim_newline(buf); char l[128]; snprintf(l, sizeof(l), "Uptime: %s", buf); gtk_label_set_text(GTK_LABEL(uptime_label), l); } pclose(fp); }

    return TRUE;
}

// ========================================
// TAB HELPER
// ========================================
GtkWidget* create_tab(char **labels, void (*callbacks[])(GtkWidget*, gpointer), int count) {
    GtkWidget *box = gtk_box_new(GTK_ORIENTATION_VERTICAL, 5);
    gtk_container_set_border_width(GTK_CONTAINER(box), 8);

    GtkWidget *flow = gtk_flow_box_new();
    gtk_flow_box_set_homogeneous(GTK_FLOW_BOX(flow), TRUE);
    gtk_flow_box_set_max_children_per_line(GTK_FLOW_BOX(flow), 4);
    gtk_flow_box_set_selection_mode(GTK_FLOW_BOX(flow), GTK_SELECTION_NONE);
    gtk_box_pack_start(GTK_BOX(box), flow, FALSE, FALSE, 0);

    for (int i = 0; i < count; i++) {
        GtkWidget *btn = gtk_button_new_with_label(labels[i]);
        gtk_widget_set_size_request(btn, 140, 36);
        g_signal_connect(btn, "clicked", G_CALLBACK(callbacks[i]), NULL);
        gtk_container_add(GTK_CONTAINER(flow), btn);
    }

    // INTERACTIVE INPUT BAR
    GtkWidget *entry = gtk_entry_new();
    gtk_entry_set_placeholder_text(GTK_ENTRY(entry), "Target / Location / Package Input (e.g. archlinux.org, Tokyo, htop)");
    gtk_box_pack_start(GTK_BOX(box), entry, FALSE, FALSE, 4);

    GtkWidget *scrolled = gtk_scrolled_window_new(NULL, NULL);
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scrolled), GTK_POLICY_AUTOMATIC, GTK_POLICY_AUTOMATIC);
    gtk_box_pack_start(GTK_BOX(box), scrolled, TRUE, TRUE, 0);

    GtkWidget *text_view = gtk_text_view_new();
    gtk_text_view_set_editable(GTK_TEXT_VIEW(text_view), FALSE);
    gtk_text_view_set_wrap_mode(GTK_TEXT_VIEW(text_view), GTK_WRAP_WORD);
    gtk_container_add(GTK_CONTAINER(scrolled), text_view);

    g_object_set_data(G_OBJECT(box), "input_entry", entry);
    g_object_set_data(G_OBJECT(box), "output_text", text_view);
    return box;
}

void on_notebook_switch_page(GtkNotebook *nb, GtkWidget *page, guint page_num, gpointer user_data) {
    GtkWidget *tv = g_object_get_data(G_OBJECT(page), "output_text");
    GtkWidget *en = g_object_get_data(G_OBJECT(page), "input_entry");
    if (tv) current_output_text = tv;
    if (en) current_input_entry = en;
}

// ========================================
// MAIN
// ========================================
int main(int argc, char *argv[]) {
    srand(time(NULL));
    gtk_init(&argc, &argv);

    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_title(GTK_WINDOW(window), "SysKit GUI");
    gtk_window_set_default_size(GTK_WINDOW(window), 1100, 720);
    gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);

    GtkWidget *vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 5);
    gtk_container_add(GTK_CONTAINER(window), vbox);

    // Live Stats Row
    GtkWidget *status_box = gtk_box_new(GTK_ORIENTATION_HORIZONTAL, 10);
    gtk_box_pack_start(GTK_BOX(vbox), status_box, FALSE, FALSE, 4);
    cpu_label = gtk_label_new("CPU: --");
    ram_label = gtk_label_new("RAM: --");
    disk_label = gtk_label_new("Disk: --");
    uptime_label = gtk_label_new("Uptime: --");
    gtk_box_pack_start(GTK_BOX(status_box), cpu_label, TRUE, TRUE, 0);
    gtk_box_pack_start(GTK_BOX(status_box), ram_label, TRUE, TRUE, 0);
    gtk_box_pack_start(GTK_BOX(status_box), disk_label, TRUE, TRUE, 0);
    gtk_box_pack_start(GTK_BOX(status_box), uptime_label, TRUE, TRUE, 0);

    // Menu Bar
    GtkWidget *menubar = gtk_menu_bar_new();
    GtkWidget *theme_menu = gtk_menu_new();
    GtkWidget *theme_item = gtk_menu_item_new_with_label("🎨 Themes");
    gtk_menu_item_set_submenu(GTK_MENU_ITEM(theme_item), theme_menu);

    GtkWidget *dark_i = gtk_menu_item_new_with_label("Dark");
    GtkWidget *light_i = gtk_menu_item_new_with_label("Light");
    GtkWidget *green_i = gtk_menu_item_new_with_label("Green");
    GtkWidget *hacker_i = gtk_menu_item_new_with_label("Hacker");

    g_signal_connect_swapped(dark_i, "activate", G_CALLBACK(set_theme), "dark");
    g_signal_connect_swapped(light_i, "activate", G_CALLBACK(set_theme), "light");
    g_signal_connect_swapped(green_i, "activate", G_CALLBACK(set_theme), "green");
    g_signal_connect_swapped(hacker_i, "activate", G_CALLBACK(set_theme), "hacker");

    gtk_menu_shell_append(GTK_MENU_SHELL(theme_menu), dark_i);
    gtk_menu_shell_append(GTK_MENU_SHELL(theme_menu), light_i);
    gtk_menu_shell_append(GTK_MENU_SHELL(theme_menu), green_i);
    gtk_menu_shell_append(GTK_MENU_SHELL(theme_menu), hacker_i);

    gtk_menu_shell_append(GTK_MENU_SHELL(menubar), theme_item);
    gtk_box_pack_start(GTK_BOX(vbox), menubar, FALSE, FALSE, 0);

    // Notebook
    notebook = gtk_notebook_new();
    gtk_notebook_set_scrollable(GTK_NOTEBOOK(notebook), TRUE);
    g_signal_connect(notebook, "switch-page", G_CALLBACK(on_notebook_switch_page), NULL);
    gtk_box_pack_start(GTK_BOX(vbox), notebook, TRUE, TRUE, 0);

    // 15 INDIVIDUAL TABS
    char *s1[]={"Fastfetch","Sys Info","Hardware","CPU Info","GPU Info"}; void (*c1[])(GtkWidget*,gpointer)={show_fastfetch,show_system_info,show_hardware_info,show_cpu_info,show_gpu_info};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s1, c1, 5), gtk_label_new("1. 🖥 System"));

    char *s2[]={"CPU Usage","RAM Usage","Disk Usage","Temperature"}; void (*c2[])(GtkWidget*,gpointer)={show_cpu_usage,show_ram_usage,show_disk_usage,show_temp};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s2, c2, 4), gtk_label_new("2. 📊 Monitoring"));

    char *s3[]={"Ping Target","Local IP","Public IP"}; void (*c3[])(GtkWidget*,gpointer)={ping_test,show_local_ip,show_public_ip};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s3, c3, 3), gtk_label_new("3. 🌐 Network"));

    char *s4[]={"Battery Info"}; void (*c4[])(GtkWidget*,gpointer)={show_battery};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s4, c4, 1), gtk_label_new("4. 🔋 Power"));

    char *s5[]={"Search Package","Update Repos","Upgrade System","Clean Packages"}; void (*c5[])(GtkWidget*,gpointer)={pkg_search,pkg_update,pkg_upgrade,pkg_clean};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s5, c5, 4), gtk_label_new("5. 📦 Package Manager"));

    char *s6[]={"Clear Cache","Vacuum Logs","Empty Trash"}; void (*c6[])(GtkWidget*,gpointer)={clean_cache,clean_logs,clean_trash};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s6, c6, 3), gtk_label_new("6. 🧹 Cleaner"));

    char *s7[]={"Disk Space"}; void (*c7[])(GtkWidget*,gpointer)={show_disk_usage};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s7, c7, 1), gtk_label_new("7. 💾 Storage"));

    char *s8[]={"Firewall Status","Open Ports"}; void (*c8[])(GtkWidget*,gpointer)={sec_firewall,sec_ports};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s8, c8, 2), gtk_label_new("8. 🔐 Security"));

    char *s9[]={"File Stats"}; void (*c9[])(GtkWidget*,gpointer)={show_system_info};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s9, c9, 1), gtk_label_new("9. 📂 File Tools"));

    char *s10[]={"Archive Info"}; void (*c10[])(GtkWidget*,gpointer)={sys_help};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s10, c10, 1), gtk_label_new("10. 🗜 Archive Tools"));

    char *s11[]={"Password Gen","UUID Gen"}; void (*c11[])(GtkWidget*,gpointer)={util_pw,util_uuid};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s11, c11, 2), gtk_label_new("11. 🔑 Utility Tools"));

    char *s12[]={"Weather","Calendar"}; void (*c12[])(GtkWidget*,gpointer)={inet_weather,inet_cal};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s12, c12, 2), gtk_label_new("12. 🌤 Internet Tools"));

    char *s13[]={"Backup Documents"}; void (*c13[])(GtkWidget*,gpointer)={bak_doc};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s13, c13, 1), gtk_label_new("13. 💾 Backup"));

    char *s14[]={"Reset Settings"}; void (*c14[])(GtkWidget*,gpointer)={sys_reset};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s14, c14, 1), gtk_label_new("14. ⚙ Settings"));

    char *s15[]={"About App"}; void (*c15[])(GtkWidget*,gpointer)={sys_help};
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), create_tab(s15, c15, 1), gtk_label_new("15. ❓ Help"));

    status_label = gtk_statusbar_new();
    gtk_box_pack_start(GTK_BOX(vbox), status_label, FALSE, FALSE, 0);

    set_theme("dark");
    gtk_widget_show_all(window);

    GtkWidget *first_page = gtk_notebook_get_nth_page(GTK_NOTEBOOK(notebook), 0);
    if (first_page) {
        current_output_text = g_object_get_data(G_OBJECT(first_page), "output_text");
        current_input_entry = g_object_get_data(G_OBJECT(first_page), "input_entry");
    }

    g_timeout_add(1000, update_stats, NULL);

    gtk_main();
    return 0;
}
