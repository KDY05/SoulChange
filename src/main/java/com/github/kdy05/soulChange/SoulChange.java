package com.github.kdy05.soulChange;

import com.github.kdy05.soulChange.command.CommandController;
import com.github.kdy05.soulChange.event.EventController;
import com.github.kdy05.soulChange.utils.NameCacheManager;
import net.pinger.disguise.DisguiseProvider;
import net.pinger.disguise.DisguiseAPI;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SoulChange extends JavaPlugin {
    private static SoulChange serverInstance;
    private static EventController eventController;
    private static CommandController commandController;
    private static NameCacheManager nameCacheManager;
    private static DisguiseProvider disguiseProvider;
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        config.addDefault("for_test", true);
        config.options().copyDefaults(true);
        saveConfig();

        serverInstance = this;
        eventController = new EventController();
        commandController = new CommandController();
        nameCacheManager = new NameCacheManager();

        disguiseProvider = DisguiseAPI.getDefaultProvider();
        if (disguiseProvider == null) {
            getLogger().info("Failed to find the provider for this version");
            getLogger().info("Disabling...");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        nameCacheManager.updateAllPlayer();

        // 리스폰 화면에서 오류 발생 우려가 있어서 gamerule 설정.
        for (World world : getServer().getWorlds())  {
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        }

        getLogger().info("Plugin Enabled.");
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        getLogger().info("Plugin Disabled.");
    }


    public static SoulChange getServerInstance(){
        return serverInstance;
    }

    public static EventController getEventController(){
        return eventController;
    }

    public static CommandController getCommandController(){
        return commandController;
    }

    public static NameCacheManager getNameCacheManager() {
        return nameCacheManager;
    }

    public static DisguiseProvider getDisguiseProvider(){
        return disguiseProvider;
    }
}
