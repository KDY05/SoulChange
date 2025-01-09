package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.utils.ChangeSkin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class DisguiseTest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length < 1) {
            player.sendMessage( ChatColor.YELLOW + "Your name is " + player.getName() + "(" + player.getUniqueId() + ")" + ".");
            return false;
        }
        if (strings.length == 1) {
            if (Objects.equals(strings[0], "reset")){
                SoulChange.getDisguiseProvider().resetPlayer(player);
                return false;
            }
            ChangeSkin changeSkin = new ChangeSkin();
            changeSkin.changeSkin(player, strings[0]);
        } else if (strings.length == 2) {
            Player target = Bukkit.matchPlayer(strings[0]).getFirst();
            if (Objects.equals(strings[1], "reset")){
                SoulChange.getDisguiseProvider().resetPlayer(target);
                return false;
            }
            ChangeSkin changeSkin = new ChangeSkin();
            changeSkin.changeSkin(target, strings[1]);
        }
        return false;
    }
}
