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
        } else if ("zh_tc".equalsIgnoreCase(language)) {
            // 繁體中文消息
            messages.put("TranslationContributors", "當前語言: 繁體中文 (貢獻者: Zhang1233)");
            messages.put("CanNotFoundMeowLibs", "未找到 MeowLibs, 请安装前置依赖 MeowLibs!");
            messages.put("startup", "MeowFly 已載入!");
            messages.put("shutdown", "MeowFly 已卸載!");
            messages.put("nowusingversion", "當前使用版本:");
            messages.put("checkingupdate", "正在檢查更新...");
            messages.put("checkfailed", "檢查更新失敗，請檢查你的網絡狀態!");
            messages.put("updateavailable", "發現新版本:");
            messages.put("updatemessage", "更新內容如下:");
            messages.put("updateurl", "新版本下載地址:");
            messages.put("oldversionmaycauseproblem", "舊版本可能會導致問題，請尽快更新!");
            messages.put("nowusinglatestversion", "您正在使用最新版本!");
            messages.put("reloaded", "配置文件已重載!");
            messages.put("nopermission", "你沒有權限執行此命令!");
            messages.put("flyModeEnabled", "§a飞行模式已开启!");
            messages.put("flyModeDisabled", "§c飞行模式已关闭!");
            messages.put("usage", "用法:");
            messages.put("notplayer", "只有玩家才能执行此命令!");
            messages.put("failedtocreateymlfile", "無法創建飛行狀態文件, 請檢查你的硬碟是否已滿!");
            messages.put("failedtoreadymlstatus", "無法讀取飛行狀態文件, 請檢查你的配置文件是否損壞!");
            messages.put("failedtosaveymlstatus", "無法保存飛行狀態文件, 請檢查你的配置文件是否損壞!");
            messages.put("failedtoclosedatabaseconnection", "無法關閉數據庫連接, 請檢查你的數據庫配置!");
            messages.put("failedtoconnectdatabase", "無法連接至數據庫, 請檢查你的數據庫配置!");
            messages.put("failedtoreaddatabase", "無法讀取數據庫數據, 請檢查你的數據庫配置!");
            messages.put("failedtosavedatabase", "無法保存數據庫數據, 請檢查你的數據庫配置!");
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
        } else if ("ja_jp".equalsIgnoreCase(language)) {
            // 日本语消息
            messages.put("TranslationContributors", "現在の言語: 日本語 (寄稿者: Zhang1233)");
            messages.put("CanNotFoundMeowLibs", "MeowLibsが見つかりません。プレフィックス依存をインストールしてください!");
            messages.put("startup", "MeowFlyがロードされました!");
            messages.put("shutdown", "MeowFlyが無効化されました!");
            messages.put("nowusingversion", "現在使用中のバージョン:");
            messages.put("checkingupdate", "更新を確認中...");
            messages.put("checkfailed", "更新チェックに失敗しました。ネットワークを確認してください!");
            messages.put("updateavailable", "新しいバージョンが利用できます:");
            messages.put("updatemessage", "アップデート内容:");
            messages.put("updateurl", "更新をダウンロードするURL:");
            messages.put("oldversionmaycauseproblem", "古いバージョンは問題を引き起こす可能性があります!");
            messages.put("nowusinglatestversion", "現在最新バージョンを使用しています!");
            messages.put("reloaded", "設定ファイルがリロードされました!");
            messages.put("nopermission", "このコマンドの実行に権限がありません!");
            messages.put("flyModeEnabled", "§a飛行モードが有効化されました!");
            messages.put("flyModeDisabled", "§c飛行モードが無効化されました!");
            messages.put("usage", "使用法:");
            messages.put("notplayer", "プレイヤーのみがこのコマンドを実行できます!");
            messages.put("failedtocreateymlfile", "フライトステータスファイルの作成に失敗しました。ハードディスクが一杯の可能性があります!");
            messages.put("failedtoreadymlstatus", "フライトステータスファイルの読み込みに失敗しました。設定ファイルが破損している可能性があります!");
            messages.put("failedtosaveymlstatus", "フライトステータスファイルの保存に失敗しました。設定ファイルが破損している可能性があります!");
            messages.put("failedtoclosedatabase", "データベース接続のクローズに失敗しました。データベース設定を確認してください!");
            messages.put("failedtoconnectdatabase", "データベースへの接続に失敗しました。データベース設定を確認してください!");
            messages.put("failedtoreaddatabase", "データベースからのデータの読み込みに失敗しました。データベース設定を確認してください!");
            messages.put("failedtosavedatabase", "データベースへのデータの保存に失敗しました。データベース設定を確認してください!");
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
