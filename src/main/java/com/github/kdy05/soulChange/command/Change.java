package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.changeTask.ChangeStatus;
import com.github.kdy05.soulChange.utils.PeriodicTask;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Change implements CommandExecutor {
    private final PeriodicTask periodicTask;

    public Change(){
        periodicTask = new PeriodicTask(SoulChange.getServerInstance(), ChangeStatus::changeStatus);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.isOp()) {
            return false;
        }
        if (strings.length > 1) {
            commandSender.sendMessage( ChatColor.YELLOW + "/change (start|stop)");
            return false;
        }
        if (strings.length == 0) {
            ChangeStatus.changeStatus();
        } else {
            if (strings[0].equals("start")){
                periodicTask.start();
                commandSender.sendMessage(ChatColor.GREEN +"랜덤 타이머가 시작되었습니다.");
            } else if (strings[0].equals("stop")) {
                periodicTask.stop();
                commandSender.sendMessage(ChatColor.RED +"랜덤 타이머가 종료되었습니다.");
            }
        }
        return false;
    }
}
