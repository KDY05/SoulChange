package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.changeTask.ChangeStatus;
import com.github.kdy05.soulChange.utils.PeriodicTask;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Change implements CommandExecutor {
    private final PeriodicTask periodicTask;

    public Change(){
        periodicTask = new PeriodicTask(SoulChange.getServerInstance(),
                ChangeStatus::changeStatus, 0.33);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (strings.length > 1) {
            player.sendMessage( ChatColor.YELLOW + "/change (start|stop)");
            return false;
        }
        if (strings.length == 0) {
            ChangeStatus.changeStatus();
        } else {
            if (strings[0].equals("start")){
                periodicTask.start();
                player.sendMessage(ChatColor.GREEN +"랜덤 타이머가 시작되었습니다.");
            } else if (strings[0].equals("stop")) {
                periodicTask.stop();
                player.sendMessage(ChatColor.RED +"랜덤 타이머가 종료되었습니다.");
            }
        }
        return false;
    }
}
