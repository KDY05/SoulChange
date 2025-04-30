package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.core.ChangeStatus;
import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.utils.ChangeSkinWithCaching;
import com.github.kdy05.soulChange.utils.PeriodicTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoulChangeCommand implements CommandExecutor, TabCompleter {

    private PeriodicTask periodicTask;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(SoulChange.PLUGIN_ID + "운영자 권한이 필요합니다.");
            return false;
        }

        if (strings.length == 0) {
            return false;
        }

        String subCommand = strings[0].toLowerCase();
        switch (subCommand) {
            case "help" -> handleHelp(commandSender);
            case "reload" -> handleReload(commandSender);
            case "change" -> handleChange(commandSender, strings);
            case "skin" -> handleSkin(commandSender, strings);
        }

        return false;
    }

    private void handleHelp(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.YELLOW + "/sc(soulchange) help: 이 메시지를 띄웁니다.");
        commandSender.sendMessage(ChatColor.YELLOW + "/sc(soulchange) reload: config.yml 설정을 불러옵니다.");
        commandSender.sendMessage(ChatColor.YELLOW + "/sc(soulchange) change [start|stop|run]: 타이머 시작(start)과 종료(run), 혹은 즉시 교체를 실행(run)합니다.");
        commandSender.sendMessage(ChatColor.YELLOW + "/sc(soulchange) skin [player1|reset]: player1으로 변장하거나 변장을 해제합니다.");
        commandSender.sendMessage(ChatColor.YELLOW + "/sc(soulchange) skin [player1] [player2|reset]: player1을 player2로 변장시키거나 변장을 해제합니다.");
    }

    private void handleReload(CommandSender commandSender) {
        SoulChange.getPlugin().reloadConfig();
        commandSender.sendMessage(SoulChange.PLUGIN_ID + "config.yml 파일이 새로고침되었습니다.");
        commandSender.sendMessage(SoulChange.PLUGIN_ID + ChatColor.YELLOW + "타이머가 작동 중인 경우, 재시작해야 적용됩니다.");
    }

    private void handleChange(CommandSender commandSender, String[] strings) {
        if (strings.length != 2) {
            commandSender.sendMessage( ChatColor.YELLOW + "/sc change (start|stop|run)");
            return;
        }

        switch (strings[1]) {
            case "start" -> {
                if (periodicTask != null) {
                    commandSender.sendMessage(SoulChange.PLUGIN_ID + "이미 타이머가 실행 중입니다. 종료 후 다시 실행해주세요.");
                    return;
                }
                periodicTask = new PeriodicTask(SoulChange.getPlugin(), ChangeStatus::changeStatus);
                periodicTask.start();
                commandSender.sendMessage(SoulChange.PLUGIN_ID + "랜덤 타이머가 시작되었습니다.");
            }
            case "stop" -> {
                if (periodicTask == null) {
                    commandSender.sendMessage(SoulChange.PLUGIN_ID + "실행 중인 타이머가 없습니다.");
                    return;
                }
                periodicTask.stop();
                periodicTask = null;
                commandSender.sendMessage(SoulChange.PLUGIN_ID + "랜덤 타이머가 종료되었습니다.");
            }
            case "run" -> ChangeStatus.changeStatus();
            default -> commandSender.sendMessage( ChatColor.YELLOW + "/sc change (start|stop|run)");
        }
    }

    private void handleSkin(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof Player player)){
            commandSender.sendMessage(SoulChange.PLUGIN_ID + "플레이어 전용 커맨드입니다.");
            return;
        }

        switch (strings.length) {
            case 1:
                player.sendMessage(SoulChange.PLUGIN_ID + "Your name is " + player.getName() + "(" + player.getUniqueId() + ")" + ".");
                return;

            case 2:
                if (Objects.equals(strings[1], "reset")) {
                    SoulChange.getDisguiseProvider().resetPlayer(player);
                    return;
                }
                new ChangeSkinWithCaching().changeSkin(player, strings[1]);
                player.sendMessage(SoulChange.PLUGIN_ID + "스킨을 변경하였습니다.");
                return;

            case 3:
                Player target;
                if (!Bukkit.matchPlayer(strings[1]).isEmpty()) {
                    target = Bukkit.matchPlayer(strings[1]).getFirst();
                } else {
                    player.sendMessage(SoulChange.PLUGIN_ID + "닉네임이 유효하지 않습니다.");
                    return;
                }
                if (Objects.equals(strings[2], "reset")) {
                    SoulChange.getDisguiseProvider().resetPlayer(target);
                    return;
                }
                new ChangeSkinWithCaching().changeSkin(target, strings[2]);
                player.sendMessage(SoulChange.PLUGIN_ID + "스킨을 변경하였습니다.");
                return;

            default:
                player.sendMessage(SoulChange.PLUGIN_ID + "잘못된 사용");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();
        if (strings.length == 1) {
            completions.add("reload");
            completions.add("change");
            completions.add("skin");
            completions.add("help");
        } else if (strings.length == 2 && strings[0].equals("change")) {
            completions.add("start");
            completions.add("stop");
            completions.add("run");
        }
        return completions;
    }
}
