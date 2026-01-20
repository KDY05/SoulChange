package com.github.kdy05.soulChange.command;

import com.github.kdy05.soulChange.logic.StatusChanger;
import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.utils.PeriodicTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
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
    private static final List<String> SUB_COMMANDS = Arrays.asList("help", "reload", "change", "swap");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!sender.hasPermission("soulchange.use")) {
            sendMessageAsComponent(sender, "<red>운영자 권한이 필요합니다.");
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
            default -> sendMessageAsComponent(sender, "<yellow>잘못된 명령입니다.");
        }

        return false;
    }

    private void handleHelp(CommandSender sender) {
        sendMessageAsComponent(sender, "<yellow>/sc help: 이 메시지를 띄웁니다.", false);
        sendMessageAsComponent(sender, "<yellow>/sc reload: config.yml 설정을 불러옵니다.", false);
        sendMessageAsComponent(sender, "<yellow>/sc change [start|stop|run]: 타이머 시작(start)과 종료(stop), 혹은 즉시 교체(run)를 실행합니다.", false);
        sendMessageAsComponent(sender, "<yellow>/sc swap [player1] [player2]: player1을 player2와 영혼을 맞바꿉니다.", false);
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("soulchange.reload")) {
            sendMessageAsComponent(sender, "<red>리로드 권한이 필요합니다.");
            return;
        }
        SoulChange.getPlugin().reloadConfig();
        sendMessageAsComponent(sender, "config.yml 파일이 새로고침되었습니다.");
        sendMessageAsComponent(sender, "<yellow>타이머가 작동 중인 경우, 재시작해야 적용됩니다.");
    }

    private void handleChange(CommandSender sender, String[] strings) {
        if (strings.length != 2) {
            sendMessageAsComponent(sender, "<yellow>/sc change [start|stop|run]");
            return;
        }

        switch (strings[1]) {
            case "start" -> {
                if (periodicTask != null) {
                    sendMessageAsComponent(sender, "이미 타이머가 실행 중입니다. 종료 후 다시 실행해주세요.");
                    return;
                }
                periodicTask = new PeriodicTask(SoulChange.getPlugin(), StatusChanger::changeStatus);
                periodicTask.start();
                sendMessageAsComponent(sender, "랜덤 타이머가 시작되었습니다.");
            }
            case "stop" -> {
                if (periodicTask == null) {
                    sendMessageAsComponent(sender, "실행 중인 타이머가 없습니다.");
                    return;
                }
                periodicTask.stop();
                periodicTask = null;
                sendMessageAsComponent(sender, "랜덤 타이머가 종료되었습니다.");
            }
            case "run" -> StatusChanger.changeStatus();
            default -> sendMessageAsComponent(sender, "<yellow>/sc change [start|stop|run]", false);
        }
    }

    private void handleSwap(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sendMessageAsComponent(sender, "<yellow>/sc swap [player1] [player2]", false);
            return;
        }

        Player origin = getPlayerByName(args[1]);
        Player target = getPlayerByName(args[2]);

        if (origin == null || target == null) {
            sendMessageAsComponent(sender, "닉네임이 유효하지 않습니다.");
            return;
        }

        if (StatusChanger.swapStatus(origin, target)) {
            sendMessageAsComponent(sender, "두 플레이어의 영혼을 교체했습니다: " +
                    origin.getName() + " ↔ " + target.getName());
        } else {
            sendMessageAsComponent(sender, "플레이어가 영혼을 교체할 수 없는 상태입니다.");
        }
    }

    @Nullable
    private Player getPlayerByName(String name) {
        return Bukkit.matchPlayer(name).stream().findFirst().orElse(null);
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
        else if ((strings.length == 2 || strings.length == 3) && strings[0].equalsIgnoreCase("swap")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                completions.add(onlinePlayer.getName());
            }
            return completions.stream()
                    .filter(name -> name.toLowerCase().startsWith(strings[strings.length - 1].toLowerCase()))
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

    /**
     * String을 Component로 변환합니다 (색상 코드 지원).
     *
     * @param text 텍스트
     * @return Component
     */
    private Component toComponent(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    private void sendMessageAsComponent(CommandSender sender, String text) {
        sendMessageAsComponent(sender, text, true);
    }

    private void sendMessageAsComponent(CommandSender sender, String text, boolean prefix) {
        if (prefix) {
            sender.sendMessage(toComponent(SoulChange.PLUGIN_ID + text));
        } else {
            sender.sendMessage(toComponent(text));
        }
    }

}
