package com.github.kdy05.soulChange;

import com.github.kdy05.soulChange.command.SoulChangeCommand;
import com.github.kdy05.soulChange.listener.SoulChangeListener;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SoulChange extends JavaPlugin {
    private static SoulChange plugin;
    private static SkinsRestorer skinsRestorerAPI;

    public static final String PLUGIN_ID = "<light_purple>[SoulChange] <white>";

    @Override
    public void onEnable() {
        saveDefaultConfig();

        plugin = this;
        skinsRestorerAPI = SkinsRestorerProvider.get();

        Bukkit.getServer().getPluginManager().registerEvents(new SoulChangeListener(this), this);
        Objects.requireNonNull(getCommand("soulchange")).setExecutor(new SoulChangeCommand());
        Objects.requireNonNull(getCommand("soulchange")).setTabCompleter(new SoulChangeCommand());

        getLogger().info("Enabling plugin completed.");
    }

    @Override
    public void onDisable() {
        plugin = null;
        skinsRestorerAPI = null;
        getLogger().info("Disabling plugin completed.");
    }

    public static SoulChange getPlugin(){
        return plugin;
    }

    public static SkinsRestorer getSkinsRestorerAPI() {
        return skinsRestorerAPI;
    }

}
