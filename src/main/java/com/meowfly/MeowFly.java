package com.meowfly;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.Arrays;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MeowFly extends JavaPlugin implements Listener {

    // 配置项
    private Integer tick_delay;
    private String storageType;
    private Connection connection;
    private File dataFile;
    private Map<String, Boolean> flightData;
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        // ------------------------------------------初始化阶段------------------------------------------

        // bstats
        int pluginId = 23964;
        Metrics metrics = new Metrics(this, pluginId);
        
        // 加载默认配置文件
        saveDefaultConfig();

        // 初始化 LanguageManager
        languageManager = new LanguageManager(getConfig());

        // 检查前置库是否加载
        if (!Bukkit.getPluginManager().isPluginEnabled("MeowLibs")) {
            getLogger().warning(languageManager.getMessage("CanNotFoundMeowLibs"));
            // 禁用插件
            getServer().getPluginManager().disablePlugin(this); 
            return;           
        }

        // ------------------------------------------开始加载插件------------------------------------------


        // 翻译者
        getLogger().info(languageManager.getMessage("TranslationContributors"));

        // 注册事件
        getServer().getPluginManager().registerEvents(this, this);

        // 启动消息
        getLogger().info(languageManager.getMessage("startup"));
        String currentVersion = getDescription().getVersion();
        getLogger().info(languageManager.getMessage("nowusingversion") + " v" + currentVersion);
        getLogger().info(languageManager.getMessage("checkingupdate"));

        // 初始化存储
        storageType = getConfig().getString("storage", "yml");
        tick_delay = getConfig().getInt("tick_delay", 20);
        if (storageType.equalsIgnoreCase("mysql")) {
            initMySQL();
        } else {
            initLocalStorage();
        }

        // 创建 CheckUpdate 实例
        CheckUpdate updateChecker = new CheckUpdate(
            getLogger(), // log记录器
            languageManager, // 语言管理器
            getDescription() // 插件版本信息
        );    

        // 异步执行更新检查
        new BukkitRunnable() {
            @Override
            public void run() {
                updateChecker.checkUpdate();
            }
        }.runTaskAsynchronously(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            // 创建列表
            List<String> commands = Arrays.asList("use", "reload");

            // 匹配
            for (String commandOption : commands) {
                if (commandOption.startsWith(args[0].toLowerCase())) {
                    suggestions.add(commandOption);  // 添加匹配的建议到 suggestions 列表
                }
            }
        }

        return suggestions;
    }

    // 初始化本地存储
    private void initLocalStorage() {
        flightData = new HashMap<>();
        dataFile = new File(getDataFolder(), "flight_data.yml");
        if (!dataFile.exists()) {
            try {
                // 本地文件不存在, 进行创建
                dataFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning(languageManager.getMessage("failedtocreateymlfile"));
            }
        } else {
            // 存在本地文件, 加载
            loadLocalFlightData();
        }
    }

    // 加载本地数据
    private void loadLocalFlightData() {
        try {
            // 读取本地文件
            FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
            for (String playerName : config.getKeys(false)) {
                // 读取本地数据到 flightData
                flightData.put(playerName, config.getBoolean(playerName, false));
            }
        } catch (Exception e) {
            getLogger().warning(languageManager.getMessage("failedtoreadymlstatus"));
        }
    }

    // 保存本地数据
    private void saveLocalFlightData() {
        try {
            FileConfiguration config = new YamlConfiguration();
            for (Map.Entry<String, Boolean> entry : flightData.entrySet()) {
                // 保存数据到本地文件
                config.set(entry.getKey(), entry.getValue());
            }
            config.save(dataFile);
        } catch (IOException e) {
            getLogger().warning(languageManager.getMessage("failedtosaveymlstatus"));
        }
    }

    // 初始化 MySQL
    private void initMySQL() {
        try {
            // 读取 MySQL 配置项
            String host = getConfig().getString("mysql.host");
            int port = getConfig().getInt("mysql.port");
            String database = getConfig().getString("mysql.database");
            String username = getConfig().getString("mysql.username");
            String password = getConfig().getString("mysql.password");

            // 连接到 MySQL
            connection = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);

            // 创建表
            connection.createStatement().executeUpdate(
                "CREATE TABLE IF NOT EXISTS player_flight (player_name VARCHAR(50) PRIMARY KEY, flight_status BOOLEAN)");

        } catch (SQLException e) {
            getLogger().warning(languageManager.getMessage("failedtoconnectdatabase"));
        }
    }

    // 获取玩家飞行状态
    private boolean getFlightStatus(String playerName) {
        if (storageType.equalsIgnoreCase("mysql")) {
            // 从数据库中获取
            try {
                PreparedStatement ps = connection.prepareStatement(
                    "SELECT flight_status FROM player_flight WHERE player_name = ?");
                ps.setString(1, playerName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getBoolean("flight_status");
                }
            } catch (SQLException e) {
                getLogger().warning(languageManager.getMessage("failedtoreaddatabase"));
            }
        } else {
            // 从本地文件获取(直接读内存)
            return flightData.getOrDefault(playerName, false);
        }
        return false;
    }

    // 设置玩家飞行状态
    private void setFlightStatus(String playerName, boolean status) {
        if (storageType.equalsIgnoreCase("mysql")) {
            // 更新数据库
            try {
                PreparedStatement ps = connection.prepareStatement(
                    "REPLACE INTO player_flight (player_name, flight_status) VALUES (?, ?)");
                ps.setString(1, playerName);
                ps.setBoolean(2, status);
                ps.executeUpdate();
            } catch (SQLException e) {
                getLogger().warning(languageManager.getMessage("failedtosavedatabase"));
            }
        } else {
            // 更新内存列表, 然后保存文件
            flightData.put(playerName, status);
            saveLocalFlightData();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 延迟 自定义 tick 执行异步任务
        new BukkitRunnable() {
            @Override
            public void run() {
                // 异步获取飞行状态
                boolean shouldAllowFlight = getFlightStatus(player.getName());

                // 使用主线程进行权限检查和状态设置
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // 检查玩家是否有权限
                        if (player.hasPermission("meowfly.use")) {
                            if (shouldAllowFlight){
                                // 如果退出之前为飞行状态，恢复飞行权限
                                player.setAllowFlight(true);
                                player.setFlying(true);                                
                            } else {
                                // 允许飞行但不设置飞行
                                player.setAllowFlight(true);
                            }
                        }
                    }
                }.runTask(MeowFly.this);  // 在主线程中执行
            }
        }.runTaskLater(MeowFly.this, tick_delay);  // 延迟自定义 tick 执行
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // 异步保存退出时玩家的飞行状态
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                // 保存退出时玩家的飞行状态
                setFlightStatus(player.getName(), player.isFlying());
            }
        }.runTaskAsynchronously(this);
    }

    @Override
    public void onDisable() {
        getLogger().info(languageManager.getMessage("shutdown"));
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                getLogger().warning(languageManager.getMessage("failedtoclosedatabaseconnection"));
            }
        } else {
            saveLocalFlightData();
        }
    }

    public void closeDatabaseConnection() {
        if (connection != null) {
            try {
                connection.close(); // 关闭数据库连接
                connection = null;  // 将连接设置为 null，表示已关闭
            } catch (SQLException e) {
                // 如果关闭连接时发生错误，捕获并打印异常信息
                getLogger().warning(languageManager.getMessage("failedtoclosedatabaseconnection") + e.getMessage());
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mfly") || command.getName().equalsIgnoreCase("meowfly")) {
            if (args.length == 0) {
                sender.sendMessage(languageManager.getMessage("usage") + " /mfly <use|reload>");
                return true;
            }

            // 重新加载配置命令
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("meowfly.reload")) {
                    // 获取当前存储类型
                    String currentStorageType = getConfig().getString("storage", "yml"); // 默认是 yml

                    // 调用配置加载方法
                    saveDefaultConfig();
                    reloadConfig();
                    languageManager = new LanguageManager(getConfig());

                    // 获取新的存储类型
                    String newStorageType = getConfig().getString("storage", "yml");

                    // 如果存储类型改变了，进行相应的处理
                    if (!currentStorageType.equals(newStorageType)) {
                        if (currentStorageType.equals("mysql") && newStorageType.equals("yml")) {
                            // 从 MySQL 改为 yml，关闭数据库连接并初始化本地文件
                            closeDatabaseConnection();
                            initLocalStorage(); // 重新初始化
                        } else if (currentStorageType.equals("yml") && newStorageType.equals("mysql")) {
                            // 从 yml 改为 MySQL，进行数据库初始化
                            initLocalStorage(); // 初始化
                        }
                    }

                    sender.sendMessage(ChatColor.GREEN + languageManager.getMessage("reloaded"));
                } else {
                    sender.sendMessage(ChatColor.RED + languageManager.getMessage("nopermission"));
                }
                return true;
            }


            if (args[0].equalsIgnoreCase("use")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + languageManager.getMessage("notplayer"));
                    return true;
                }
                Player player = (Player) sender;

                // 检查权限
                if (!player.hasPermission("meowfly.use")) {
                    player.sendMessage(ChatColor.RED + languageManager.getMessage("nopermission"));
                    return true;
                }

                // 切换飞行模式
                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage(ChatColor.RED + languageManager.getMessage("flyModeDisabled"));
                } else {
                    player.setAllowFlight(true);
                    player.sendMessage(ChatColor.GREEN + languageManager.getMessage("flyModeEnabled"));
                }
                return true;
            }
        }
        return false;
    }
}
