package com.github.kdy05.soulChange.core;

import com.github.kdy05.soulChange.SoulChange;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StatusChanger {

    public static boolean swapStatus(Player origin, Player target) {
        if (!origin.isOnline() || !target.isOnline()) return false;
        if (origin.isDead() || target.isDead()) return false;
        PlayerState originState = PlayerState.saveFrom(origin);
        PlayerState targetState = PlayerState.saveFrom(target);
        originState.applyTo(target);
        targetState.applyTo(origin);
        return true;
    }

    public static void changeStatus() {
        // 유효 플레이어 리스트 생성
        List<? extends Player> validPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getGameMode() != GameMode.SPECTATOR)  // 관전자 모드 제외
                .filter(player -> !player.isDead())                            // 사망 상태 제외
                .toList();

        // 2명 미만이면 중지
        if (validPlayers.size() < 2) {
            SoulChange.getPlugin().getLogger().info("유효 플레이어가 2명 미만입니다.");
            return;
        }

        // 공지 타이틀 출력
        sendTitleToPlayers();

        // 모든 플레이어의 상태를 저장
        List<PlayerState> playerStates = new ArrayList<>();
        for (Player player : validPlayers) {
            playerStates.add(PlayerState.saveFrom(player));
        }

        // 상태를 적용할 대상의 인덱스를 생성 (자기 자신 제외)
        int[] targetIndices = generateDerangement(validPlayers.size());

        // 각 플레이어에게 다른 플레이어의 상태를 적용
        for (int i = 0; i < validPlayers.size(); i++) {
            int targetIndex = targetIndices[i];
            playerStates.get(targetIndex).applyTo(validPlayers.get(i));
        }

        // change-on-damaged가 켜진 경우 일시 무적 적용
        if (SoulChange.getPlugin().getConfig().getBoolean("change-on-damaged", false)) {
            for (Player player : validPlayers) {
                player.setNoDamageTicks(20);
            }
        }
    }

    private static void sendTitleToPlayers() {
        String option = SoulChange.getPlugin().getConfig().getString("notification", "TITLE");
        if ("NONE".equals(option)) {
            return;
        }
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            switch (option) {
                case "TITLE" -> Bukkit.getScheduler().runTaskLater(SoulChange.getPlugin(),
                        () -> player.sendTitle("", ChatColor.GRAY + "모든 플레이어들의 영혼이 뒤바뀌었습니다!", 5, 50, 5), 5L);
                case "CHAT" -> Bukkit.getScheduler().runTaskLater(SoulChange.getPlugin(),
                        () -> player.sendMessage(SoulChange.PLUGIN_ID + "모든 플레이어들의 영혼이 뒤바뀌었습니다!"), 5L);
            }
        }
    }


    // 자기 자신과 교환되지 않도록 보장하는 완전순열 생성
    private static int[] generateDerangement(int n) {
        Random random = new Random();
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = i;
        }

        // Sattolo's algorithm
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i); // j < i, not j <= i
            int temp = result[i];
            result[i] = result[j];
            result[j] = temp;
        }

        return result;
    }
}
