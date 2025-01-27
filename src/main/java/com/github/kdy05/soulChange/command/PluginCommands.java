package com.github.kdy05.soulChange.command;

import org.bukkit.Bukkit;

import java.util.Objects;

public class PluginCommands {
    public static void registerCommands() throws NullPointerException {
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("change")).setExecutor(new Change());
        Objects.requireNonNull(Bukkit.getServer().getPluginCommand("skin")).setExecutor(new Skin());
    }
}
