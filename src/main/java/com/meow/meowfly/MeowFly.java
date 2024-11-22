package com.meow.meowfly;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;

public class MeowFly extends JavaPlugin implements Listener {

    // 一堆存储消息的变量
    private String startupMessage;
    private String shutdownMessage;
    private String nowusingversionMessage;
    private String checkingupdateMessage;
    private String checkfailedMessage;
    private String updateavailableMessage;
    private String updateurlMessage;
    private String oldversionmaycauseproblemMessage;
    private String nowusinglatestversionMessage;
    private String reloadedMessage;
    private String nopermissionMessage;
    private String flyModeEnabledMessage;
    private String flyModeDisabledMessage; 
    private String usageMessage;   

    @Override
    public void onEnable() {
        // bstats
        int pluginId = 23964;
        Metrics metrics = new Metrics(this, pluginId);
        loadLanguage(); // 加载语言配置
        // 首次加载进行英文提示
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getLogger().warning("The default language is Simplified Chinese. If you need English, you can configure it in the config.yml.");
        }
        // 加载配置文件
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info(startupMessage);

        String currentVersion = getDescription().getVersion();
        getLogger().info(nowusingversionMessage + " v" + currentVersion);
        getLogger().info(checkingupdateMessage);
        // 异步更新检查
        new BukkitRunnable() {
            @Override
            public void run() {
                check_update();
            }
        }.runTaskAsynchronously(this);
    }

    private void loadLanguage() {
        FileConfiguration config = getConfig();
        String language = config.getString("language", "zh_cn");

        if ("zh_cn".equalsIgnoreCase(language)) {
            // 中文消息
            startupMessage = "MeowFly 已加载！";
            shutdownMessage = "MeowFly 已卸载！";
            nowusingversionMessage = "当前使用版本:";
            checkingupdateMessage = "正在检查更新...";
            checkfailedMessage = "检查更新失败，请检查你的网络状况！";
            updateavailableMessage = "发现新版本:";
            updateurlMessage = "新版本下载地址:";
            oldversionmaycauseproblemMessage = "旧版本可能会导致问题，请尽快更新！";
            nowusinglatestversionMessage = "您正在使用最新版本！";
            reloadedMessage = "配置文件已重载！";
            nopermissionMessage = "你没有权限执行此命令！";
            flyModeEnabledMessage = "§a飞行模式已开启！";
            flyModeDisabledMessage = "§c飞行模式已关闭！"; 
            usageMessage = "用法:";           
        } else {
            // English message
            startupMessage = "MeowFly has been loaded!";
            shutdownMessage = "MeowFly has been disabled!";
            nowusingversionMessage = "Currently using version:";
            checkingupdateMessage = "Checking for updates...";
            checkfailedMessage = "Update check failed, please check your network!";
            updateavailableMessage = "A new version is available:";
            updateurlMessage = "Download update at:";
            oldversionmaycauseproblemMessage = "Old versions may cause problems!";
            nowusinglatestversionMessage = "You are using the latest version!";
            reloadedMessage = "Configuration file has been reloaded!";
            nopermissionMessage = "You do not have permission to execute this command!";
            flyModeEnabledMessage = "§aFly mode enabled!";
            flyModeDisabledMessage = "§cFly mode disabled!";   
            usageMessage = "Usage:";         
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {         
            suggestions.add("reload");
        }
        return suggestions;
    }

    private void check_update() {
        // 获取当前版本号
        String currentVersion = getDescription().getVersion();
        // github加速地址，挨个尝试
        String[] githubUrls = {
            "https://ghp.ci/",
            "https://raw.fastgit.org/",
            ""
            //最后使用源地址
        };
        // 获取 github release 最新版本号作为最新版本
        // 仓库地址：https://github.com/Zhang12334/MeowFly
        String latestVersionUrl = "https://github.com/Zhang12334/MeowFly/releases/latest";
        // 获取版本号
        try {
            String latestVersion = null;
            for (String url : githubUrls) {
                HttpURLConnection connection = (HttpURLConnection) new URL(url + latestVersionUrl).openConnection();
                connection.setInstanceFollowRedirects(false); // 不自动跟随重定向
                int responseCode = connection.getResponseCode();
                if (responseCode == 302) { // 如果 302 了
                    String redirectUrl = connection.getHeaderField("Location");
                    if (redirectUrl != null && redirectUrl.contains("tag/")) {
                        // 从重定向URL中提取版本号
                        latestVersion = extractVersionFromUrl(redirectUrl);
                        break; // 找到版本号后退出循环
                    }
                }
                connection.disconnect();
                if (latestVersion != null) {
                    break; // 找到版本号后退出循环
                }
            }
            if (latestVersion == null) {
                getLogger().warning(checkfailedMessage);
                return;
            }
            // 比较版本号
            if (isVersionGreater(latestVersion, currentVersion)) {
                // 如果有新版本，则提示新版本
                getLogger().warning(updateavailableMessage + " v" + latestVersion);
                // 提示下载地址（latest release地址）
                getLogger().warning(updateurlMessage + " https://github.com/Zhang12334/MeowFly/releases/latest");
                getLogger().warning(oldversionmaycauseproblemMessage);
            } else {
                getLogger().info(nowusinglatestversionMessage);
            }
        } catch (Exception e) {
            getLogger().warning(checkfailedMessage);
        }
    }

    // 版本比较
    private boolean isVersionGreater(String version1, String version2) {
        String[] v1Parts = version1.split("\\.");
        String[] v2Parts = version2.split("\\.");
        for (int i = 0; i < Math.max(v1Parts.length, v2Parts.length); i++) {
            int v1Part = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int v2Part = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
            if (v1Part > v2Part) {
                return true;
            } else if (v1Part < v2Part) {
                return false;
            }
        }
        return false;
    }
    
    private String extractVersionFromUrl(String url) {
        // 解析 302 URL 中的版本号
        int tagIndex = url.indexOf("tag/");
        if (tagIndex != -1) {
            int endIndex = url.indexOf('/', tagIndex + 4);
            if (endIndex == -1) {
                endIndex = url.length();
            }
            return url.substring(tagIndex + 4, endIndex);
        }
        return null;
    }

    @Override
    public void onDisable() {
        getLogger().info(shutdownMessage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mfly")) {
            if (args.length == 0) {
                sender.sendMessage(usageMessage + " /mfly <use|reload>");
                return true;
            }

            // 处理 /mfly use 命令
            if (args[0].equalsIgnoreCase("use")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(nopermissionMessage);
                    return true;
                }

                Player player = (Player) sender;

                // 检查权限
                if (!player.hasPermission("meowfly.use")) {
                    player.sendMessage(nopermissionMessage);
                    return true;
                }

                // 切换飞行模式
                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage(flyModeDisabledMessage);
                } else {
                    player.setAllowFlight(true);
                    player.sendMessage(flyModeEnabledMessage);
                }
                return true;
            }

            // 处理 /mfly reload 命令
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("meowfly.reload")) {
                    sender.sendMessage(nopermissionMessage);
                    return true;
                }
                reloadConfig();
                loadLanguage();
                sender.sendMessage(reloadedMessage);
                return true;
            }
        }
        return false;
    }
}
