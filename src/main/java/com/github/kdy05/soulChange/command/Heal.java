package com.github.kdy05.soulChange.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(5);
        return false;
    }
}