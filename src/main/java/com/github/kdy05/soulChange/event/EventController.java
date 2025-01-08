package com.github.kdy05.soulChange.event;

import com.github.kdy05.soulChange.SoulChange;

public class EventController {
    private final SoulChange serverInstance;

    public EventController(){
        this.serverInstance = SoulChange.getServerInstance();
        registerEvents();
    }

    private void registerEvents(){
        //serverInstance.getServer().getPluginManager().registerEvents(new AfterRandTime(), serverInstance);
    }
}
