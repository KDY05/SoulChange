package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.ChangeStatus;
import com.github.kdy05.soulChange.utils.PeriodicTask;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Change implements CommandExecutor {
    private final PeriodicTask periodicTask;

    public Change(){
        periodicTask = new PeriodicTask(SoulChange.getPlugin(), ChangeStatus::changeStatus);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(SoulChange.PLUGIN_ID + "운영자 권한이 필요합니다.");
            return false;
        }

        if (strings.length > 1) {
            commandSender.sendMessage( ChatColor.YELLOW + "/change (start|stop)");
            return false;
        } else if (strings.length == 1) {
            if (strings[0].equals("start")){
                periodicTask.start();
                commandSender.sendMessage(SoulChange.PLUGIN_ID +"랜덤 타이머가 시작되었습니다.");
            } else if (strings[0].equals("stop")) {
                periodicTask.stop();
                commandSender.sendMessage(SoulChange.PLUGIN_ID +"랜덤 타이머가 종료되었습니다.");
            } else {
                commandSender.sendMessage(ChatColor.YELLOW + "잘못된 인자");
            }
        } else {
            ChangeStatus.changeStatus();
        }

        return false;
    }
}
