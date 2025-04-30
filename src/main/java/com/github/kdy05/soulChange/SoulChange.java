package com.github.kdy05.soulChange;

import com.github.kdy05.soulChange.command.SoulChangeCommand;
import com.github.kdy05.soulChange.event.SoulChangeListener;
import com.github.kdy05.soulChange.utils.NameCacheManager;
import net.pinger.disguise.DisguiseProvider;
import net.pinger.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SoulChange extends JavaPlugin {
    private static SoulChange plugin;
    private static NameCacheManager nameCacheManager;
    private static DisguiseProvider disguiseProvider;

    public static final String PLUGIN_ID = ChatColor.LIGHT_PURPLE + "[SoulChange] " + ChatColor.WHITE;

    @Override
    public void onEnable() {
        // config 불러오기
        saveDefaultConfig();

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
        getLogger().info("Disabling plugin completed.");
        plugin = null;
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
