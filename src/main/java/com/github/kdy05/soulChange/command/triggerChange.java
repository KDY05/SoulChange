package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.changeTask.ChangeStatus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class triggerChange implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length != 0) {
            player.sendMessage( ChatColor.YELLOW + "/change");
            return false;
        }
        ChangeStatus.changeStatus();
        return false;
    }
}
