package com.meow.meowfly;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LanguageManager {
    private Map<String, String> messages = new HashMap<>();
    private FileConfiguration config;

    public LanguageManager(FileConfiguration config) {
        this.config = config;
        loadLanguage();
    }

    public void loadLanguage() {
        // 有效的语言列表
        Set<String> validLanguages = new HashSet<>(Arrays.asList("zh_cn", "en_us"));

        // 读取配置中的语言设置，默认为zh_cn
        String language = config.getString("language", "zh_cn");

        // 如果读取的语言不在有效列表中，则设为默认值
        if (!validLanguages.contains(language.toLowerCase())) {
            language = "zh_cn";
        }
        messages.clear();

        if ("zh_cn".equalsIgnoreCase(language)) {
            // 中文消息
            messages.put("TranslationContributors", "当前语言: 简体中文 (贡献者: Zhang1233)");
            messages.put("CanNotFoundMeowLibs", "未找到 MeowLibs, 请安装前置依赖 MeowLibs!");            
            messages.put("startup", "MeowFly 已加载!");
            messages.put("shutdown", "MeowFly 已卸载!");
            messages.put("nowusingversion", "当前使用版本:");
            messages.put("checkingupdate", "正在检查更新...");
            messages.put("checkfailed", "检查更新失败，请检查你的网络状况!");
            messages.put("updateavailable", "发现新版本:");
            messages.put("updatemessage", "更新内容如下:");
            messages.put("updateurl", "新版本下载地址:");
            messages.put("oldversionmaycauseproblem", "旧版本可能会导致问题，请尽快更新!");
            messages.put("nowusinglatestversion", "您正在使用最新版本!");
            messages.put("reloaded", "配置文件已重载!");
            messages.put("nopermission", "你没有权限执行此命令!");
            messages.put("flyModeEnabled", "§a飞行模式已开启!");
            messages.put("flyModeDisabled", "§c飞行模式已关闭!");
            messages.put("usage", "用法:");
            messages.put("notplayer", "只有玩家才能执行此命令!");
            messages.put("failedtocreateymlfile", "无法创建飞行状态文件, 请检查你的硬盘是否已满!");
            messages.put("failedtoreadymlstatus", "无法读取飞行状态文件, 请检查你的配置文件是否损坏!");
            messages.put("failedtosaveymlstatus", "无法保存飞行状态文件, 请检查你的配置文件是否损坏!");
            messages.put("failedtoclosedatabaseconnection", "无法关闭数据库连接, 请检查你的数据库配置!");
            messages.put("failedtoconnectdatabase", "无法连接至数据库, 请检查你的数据库配置!");
            messages.put("failedtoreaddatabase", "无法读取数据库数据, 请检查你的数据库配置!");
            messages.put("failedtosavedatabase", "无法保存数据库数据, 请检查你的数据库配置!");
        } else if ("en_us".equalsIgnoreCase(language)) {
            // English messages
            messages.put("TranslationContributors", "Current Language: English (Contributors: Zhang1233)");
            messages.put("CanNotFoundMeowLibs", "MeowLibs not found, please install the dependency MeowLibs!");
            messages.put("startup", "MeowFly has been loaded!");
            messages.put("shutdown", "MeowFly has been disabled!");
            messages.put("nowusingversion", "Currently using version:");
            messages.put("checkingupdate", "Checking for updates...");
            messages.put("checkfailed", "Update check failed, please check your network!");
            messages.put("updateavailable", "A new version is available:");
            messages.put("updatemessage", "Update content:");
            messages.put("updateurl", "Download update at:");
            messages.put("oldversionmaycauseproblem", "Old versions may cause problems!");
            messages.put("nowusinglatestversion", "You are using the latest version!");
            messages.put("reloaded", "Configuration file has been reloaded!");
            messages.put("nopermission", "You do not have permission to execute this command!");
            messages.put("flyModeEnabled", "§aFly mode enabled!");
            messages.put("flyModeDisabled", "§cFly mode disabled!");
            messages.put("usage", "Usage:");
            messages.put("notplayer", "Only players can execute this command!");
            messages.put("failedtocreateymlfile", "Failed to create the flight status file. Please check if your hard drive is full!");
            messages.put("failedtoreadymlstatus", "Failed to read the flight status file. Please check if your configuration file is corrupted!");
            messages.put("failedtosaveymlstatus", "Failed to save the flight status file. Please check if your configuration file is corrupted!");
            messages.put("failedtoclosedatabase", "Failed to close the database connection. Please check your database configuration!");
            messages.put("failedtoconnectdatabase", "Failed to connect to the database. Please check your database configuration!");
            messages.put("failedtoreaddatabase", "Failed to read data from the database. Please check your database configuration!");
            messages.put("failedtosavedatabase", "Failed to save data to the database. Please check your database configuration!");
        }
    }

    /**
     * 获取语言消息
     * @param key 消息键名
     * @return 对应的语言消息，如果不存在则返回键名
     */
    public String getMessage(String key) {
        return messages.getOrDefault(key, key);
    }
}
