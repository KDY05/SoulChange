package com.github.kdy05.soulChange.event;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginEvents {
    public static void registerEvents(JavaPlugin plugin){
        Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerDamaged(), plugin);
    }
}
