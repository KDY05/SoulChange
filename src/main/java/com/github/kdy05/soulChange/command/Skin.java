package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.utils.ChangeSkin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Skin implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(SoulChange.PLUGIN_ID + "운영자 권한이 필요합니다.");
            return false;
        }
        if (!(commandSender instanceof Player player)){
            commandSender.sendMessage(SoulChange.PLUGIN_ID + "콘솔에서 사용 불가능합니다.");
            return false;
        }

        switch (strings.length) {
            case 1:
                if (Objects.equals(strings[0], "reset")) {
                    SoulChange.getDisguiseProvider().resetPlayer(player);
                    return false;
                }
                new ChangeSkin().changeSkin(player, strings[0]);
                player.sendMessage(SoulChange.PLUGIN_ID + "정상적으로 처리되었습니다.");

            case 2:
                Player target;
                if (!Bukkit.matchPlayer(strings[0]).isEmpty()) {
                    target = Bukkit.matchPlayer(strings[0]).getFirst();
                } else {
                    player.sendMessage(SoulChange.PLUGIN_ID + "닉네임이 유효하지 않습니다.");
                    return false;
                }
                if (Objects.equals(strings[1], "reset")) {
                    SoulChange.getDisguiseProvider().resetPlayer(target);
                    return false;
                }
                new ChangeSkin().changeSkin(target, strings[1]);
                player.sendMessage(SoulChange.PLUGIN_ID + "정상적으로 처리되었습니다.");

            default:
                player.sendMessage(SoulChange.PLUGIN_ID + "Your name is " + player.getName() + "(" + player.getUniqueId() + ")" + ".");
        }

        return false;
    }
}
