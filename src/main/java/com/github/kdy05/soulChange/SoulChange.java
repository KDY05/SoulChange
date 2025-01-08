package com.github.kdy05.soulChange;

import com.github.kdy05.soulChange.command.CommandController;
import com.github.kdy05.soulChange.event.EventController;
import net.pinger.disguise.DisguiseProvider;
import net.pinger.disguise.DisguiseAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class SoulChange extends JavaPlugin {
    private static SoulChange serverInstance;
    private static EventController eventController;
    private static CommandController commandController;
    private static DisguiseProvider disguiseProvider;

    @Override
    public void onEnable() {
        serverInstance = this;
        eventController = new EventController();
        commandController = new CommandController();
        disguiseProvider = DisguiseAPI.getDefaultProvider();
        if (disguiseProvider == null) {
            getLogger().info("Failed to find the provider for this version");
            getLogger().info("Disabling...");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        getLogger().info("Plugin Enabled.");
    }

    @Override
    public void onDisable() {
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

    public static DisguiseProvider getDisguiseProvider(){
        return disguiseProvider;
    }
}
