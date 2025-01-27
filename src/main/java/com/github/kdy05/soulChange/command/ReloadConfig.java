package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.SoulChange;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfig implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        SoulChange.getPlugin().reloadConfig();
        commandSender.sendMessage(SoulChange.PLUGIN_ID + "config.yml 파일이 새로고침되었습니다.");
        commandSender.sendMessage(SoulChange.PLUGIN_ID + ChatColor.YELLOW + "타이머가 작동 중인 경우, 재시작해야 적용됩니다.");
        return false;
    }
}
