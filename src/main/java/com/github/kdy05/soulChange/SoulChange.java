package com.github.kdy05.soulChange;

import com.github.kdy05.soulChange.command.SoulChangeCommand;
import com.github.kdy05.soulChange.event.SoulChangeListener;
import net.pinger.disguise.DisguiseProvider;
import net.pinger.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SoulChange extends JavaPlugin {
    private static SoulChange plugin;
    private static NameCacheManager nameCacheManager;
    private static DisguiseProvider disguiseProvider;

    FileConfiguration config = this.getConfig();
    public static final String PLUGIN_ID = ChatColor.LIGHT_PURPLE + "[SoulChange] " + ChatColor.WHITE;

    private void initConfig() {
        if (!getDataFolder().exists()) {
            if (getDataFolder().mkdirs()){
                getLogger().info("데이터 폴더가 정상적으로 생성되었습니다.");
            } else {
                getLogger().severe("데이터 폴더를 생성하지 못했습니다.");
            }
        }
        config.addDefault("timer.interval-seconds", 30);
        config.addDefault("timer.probability", 0.33);
        config.addDefault("change-on-damaged", false);
        config.addDefault("damage-share", false);
        config.addDefault("damage-share-rate", 1.0);
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onEnable() {
        initConfig();

        // static 변수 할당
        plugin = this;
        nameCacheManager = new NameCacheManager();
        disguiseProvider = DisguiseAPI.getDefaultProvider();

        // 커맨드, 이벤트 등록
        Bukkit.getServer().getPluginManager().registerEvents(new SoulChangeListener(), this);
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("soulchange")).setExecutor(new SoulChangeCommand());

        // DisguiseAPI 의존성 검사
        if (disguiseProvider == null) {
            getLogger().info("Failed to find the provider for this version");
            getLogger().info("Disabling...");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        nameCacheManager.updateAllPlayer();
        getLogger().info("Enabling plugin completed.");
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        getLogger().info("Disabling plugin completed.");
    }


    public static SoulChange getPlugin(){
        return plugin;
    }
    
    public static NameCacheManager getNameCacheManager() {
        return nameCacheManager;
    }

    public static DisguiseProvider getDisguiseProvider(){
        return disguiseProvider;
    }
}
