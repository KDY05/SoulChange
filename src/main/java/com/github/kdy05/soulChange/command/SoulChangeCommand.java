package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.core.NameCacheManager;
import com.github.kdy05.soulChange.core.StatusChanger;
import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.core.SkinManager;
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
import java.util.Arrays;
import java.util.List;

public class SoulChangeCommand implements CommandExecutor, TabCompleter {

    private PeriodicTask periodicTask;
    private static final List<String> SUB_COMMANDS = Arrays.asList("help", "reload", "change", "swap", "skin", "resetskin", "realname");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("soulchange.use")) {
            sender.sendMessage(ChatColor.RED + "운영자 권한이 필요합니다.");
            return false;
        }

        if (strings.length == 0) {
            return false;
        }

        String subCommand = strings[0].toLowerCase();
        switch (subCommand) {
            case "help" -> handleHelp(sender);
            case "reload" -> handleReload(sender);
            case "change" -> handleChange(sender, strings);
            case "swap" -> handleSwap(sender, strings);
            case "skin" -> handleSkin(sender, strings);
            case "resetskin" -> handleResetSkin(sender, strings);
            case "realname" -> handleRealName(sender, strings);
            default -> sender.sendMessage(ChatColor.YELLOW + "잘못된 명령입니다.");
        }

        return false;
    }

    private void handleHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "/sc help: 이 메시지를 띄웁니다.");
        sender.sendMessage(ChatColor.YELLOW + "/sc reload: config.yml 설정을 불러옵니다.");
        sender.sendMessage(ChatColor.YELLOW + "/sc change [start|stop|run]: 타이머 시작(start)과 종료(stop), 혹은 즉시 교체(run)를 실행합니다.");
        sender.sendMessage(ChatColor.YELLOW + "/sc swap [player1] [player2]: player1을 player2와 영혼을 맞바꿉니다.");
        sender.sendMessage(ChatColor.YELLOW + "/sc skin [player]: player로 변장합니다.");
        sender.sendMessage(ChatColor.YELLOW + "/sc skin [player1] [player2]: player1을 player2로 변장시킵니다.");
        sender.sendMessage(ChatColor.YELLOW + "/sc resetskin [player]: player의 변장을 해제합니다.");
        sender.sendMessage(ChatColor.YELLOW + "/sc realname [player]: player의 실제 이름을 알아냅니다.");
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("soulchange.reload")) {
            sender.sendMessage(ChatColor.RED + "리로드 권한이 필요합니다.");
            return;
        }
        SoulChange.getPlugin().reloadConfig();
        sender.sendMessage(SoulChange.PLUGIN_ID + "config.yml 파일이 새로고침되었습니다.");
        sender.sendMessage(SoulChange.PLUGIN_ID + ChatColor.YELLOW + "타이머가 작동 중인 경우, 재시작해야 적용됩니다.");
    }

    private void handleChange(CommandSender sender, String[] strings) {
        if (strings.length != 2) {
            sender.sendMessage( ChatColor.YELLOW + "/sc change [start|stop|run]");
            return;
        }

        switch (strings[1]) {
            case "start" -> {
                if (periodicTask != null) {
                    sender.sendMessage(SoulChange.PLUGIN_ID + "이미 타이머가 실행 중입니다. 종료 후 다시 실행해주세요.");
                    return;
                }
                periodicTask = new PeriodicTask(SoulChange.getPlugin(), StatusChanger::changeStatus);
                periodicTask.start();
                sender.sendMessage(SoulChange.PLUGIN_ID + "랜덤 타이머가 시작되었습니다.");
            }
            case "stop" -> {
                if (periodicTask == null) {
                    sender.sendMessage(SoulChange.PLUGIN_ID + "실행 중인 타이머가 없습니다.");
                    return;
                }
                periodicTask.stop();
                periodicTask = null;
                sender.sendMessage(SoulChange.PLUGIN_ID + "랜덤 타이머가 종료되었습니다.");
            }
            case "run" -> StatusChanger.changeStatus();
            default -> sender.sendMessage( ChatColor.YELLOW + "/sc change [start|stop|run]");
        }
    }

    private void handleSwap(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.YELLOW + "/sc swap [player1] [player2]");
            return;
        }

        Player origin = getPlayerByName(args[1]);
        Player target = getPlayerByName(args[2]);

        if (origin == null || target == null) {
            sender.sendMessage(SoulChange.PLUGIN_ID + "닉네임이 유효하지 않습니다.");
            return;
        }

        if (StatusChanger.swapStatus(origin, target)) {
            sender.sendMessage(SoulChange.PLUGIN_ID + "두 플레이어의 영혼을 교체했습니다: " +
                    origin.getName() + " ↔ " + target.getName());
        } else {
            sender.sendMessage(SoulChange.PLUGIN_ID + "플레이어가 영혼을 교체할 수 없는 상태입니다.");
        }
    }

    @Nullable
    private Player getPlayerByName(String name) {
        return Bukkit.matchPlayer(name).stream().findFirst().orElse(null);
    }

    private void handleSkin(CommandSender sender, String[] strings) {
        if (!(sender instanceof Player player)){
            sender.sendMessage(SoulChange.PLUGIN_ID + "플레이어 전용 커맨드입니다.");
            return;
        }

        switch (strings.length) {
            case 2:
                SkinManager.changeSkin(player, strings[1]);
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
                SkinManager.changeSkin(target, strings[2]);
                player.sendMessage(SoulChange.PLUGIN_ID + "스킨을 변경하였습니다.");
                return;

            default:
                player.sendMessage(SoulChange.PLUGIN_ID + "잘못된 사용");
        }
    }

    private void handleResetSkin(CommandSender sender, String[] strings) {
        if (strings.length != 2) {
            sender.sendMessage(SoulChange.PLUGIN_ID + "잘못된 사용");
            return;
        }
        Player target;
        if (!Bukkit.matchPlayer(strings[1]).isEmpty()) {
            target = Bukkit.matchPlayer(strings[1]).getFirst();
        } else {
            sender.sendMessage(SoulChange.PLUGIN_ID + "닉네임이 유효하지 않습니다.");
            return;
        }
        SoulChange.getDisguiseProvider().resetPlayer(target);
        Bukkit.getScheduler().runTaskLater(SoulChange.getPlugin(),
                () -> SoulChange.getNameCacheManager().setName(target.getUniqueId(), target.getName()), 1L);
    }

    private void handleRealName(CommandSender sender, String[] strings) {
        if (strings.length != 2) {
            sender.sendMessage(SoulChange.PLUGIN_ID + "잘못된 사용");
            return;
        }
        Player target;
        if (!Bukkit.matchPlayer(strings[1]).isEmpty()) {
            target = Bukkit.matchPlayer(strings[1]).getFirst();
        } else {
            sender.sendMessage(SoulChange.PLUGIN_ID + "닉네임이 유효하지 않습니다.");
            return;
        }
        sender.sendMessage(SoulChange.PLUGIN_ID
                + target.getName() + "의 실제 이름: " + NameCacheManager.getRealName(target.getUniqueId()));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("soulchange.use")) {
            return new ArrayList<>();
        }

        List<String> completions = new ArrayList<>();
        if (strings.length == 1) {
            return filterCompletions(strings[0]);
        }
        else if (strings.length == 2 && strings[0].equals("change")) {
            completions.add("start");
            completions.add("stop");
            completions.add("run");
        }
        else if (strings.length == 2 && (strings[0].equalsIgnoreCase("skin") || strings[0].equalsIgnoreCase("resetskin")
                || strings[0].equalsIgnoreCase("realname") || strings[0].equalsIgnoreCase("swap"))) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                completions.add(onlinePlayer.getName());
            }
            return completions.stream()
                    .filter(name -> name.toLowerCase().startsWith(strings[1].toLowerCase()))
                    .toList();
        }
        else if (strings.length == 3 && strings[0].equalsIgnoreCase("swap")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                completions.add(onlinePlayer.getName());
            }
            return completions.stream()
                    .filter(name -> name.toLowerCase().startsWith(strings[2].toLowerCase()))
                    .toList();
        }

        return completions;
    }

    @NotNull
    private List<String> filterCompletions(@NotNull String input) {
        return SoulChangeCommand.SUB_COMMANDS.stream()
                .filter(completion -> completion.toLowerCase().startsWith(input.toLowerCase()))
                .toList();
    }

}
