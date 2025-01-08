package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.SoulChange;

public class CommandController {
    private final SoulChange serverInstance;

    public CommandController(){
        this.serverInstance = SoulChange.getServerInstance();
        registerCommands();
    }

    private void registerCommands() throws NullPointerException{
        serverInstance.getServer().getPluginCommand("change").setExecutor(new triggerChange());
        serverInstance.getServer().getPluginCommand("heal").setExecutor(new Heal());
        serverInstance.getServer().getPluginCommand("skin").setExecutor(new DisguiseTest());
    }
}

