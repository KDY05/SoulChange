package com.github.kdy05.soulChange.event;

import com.github.kdy05.soulChange.SoulChange;
import org.bukkit.Bukkit;

public class EventController {
    private final SoulChange serverInstance;

    public EventController(){
        this.serverInstance = SoulChange.getServerInstance();
        registerEvents();
    }

    private void registerEvents(){
        Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), serverInstance);
    }
}
