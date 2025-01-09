package com.github.kdy05.soulChange.changeTask;

import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.utils.ChangeSkin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Random;

public class ChangeStatus {

    public static void changeStatus() {
        Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        int size = onlinePlayers.length;
        if (size < 2) {
            return;
        }

        // 어그로 몹

        for (Player player : onlinePlayers){
            // 1초 무적
            player.setInvulnerable(true);
            Bukkit.getScheduler().runTaskLater(SoulChange.getServerInstance(),
                    () -> player.setInvulnerable(false), 20L);
            // 타이틀 메시지
            Bukkit.getScheduler().runTaskLater(SoulChange.getServerInstance(),
                    () -> player.sendTitle("", ChatColor.GRAY + "모든 플레이어들의 영혼이 뒤바뀌었습니다!", 5, 50, 5), 5L);
        }

        double[] playerHealths = new double[size]; // 플레이어 체력 저장
        float[][] playerFoodLevel = new float[size][2]; // 플레이어 배고픔 저장
        float[][] playerExp = new float[size][2]; // 플레이어 레벨 저장
        int[] playerAir = new int[size]; // 플레이어 산소 저장
        int[] playerFire = new int[size]; // 플레이어 불타는 상태 저장
        ItemStack[][] playerInventory = new ItemStack[size][]; // 플레이어 인벤토리 저장
        Location[] playerLocation = new Location[size]; // 플레이어 위치 저장
        Location[] playerRespawnLocation = new Location[size]; // 플레이어 리스폰 위치 저장
        List<PotionEffect>[] playerPotion = new List[size]; // 플레이어 포션 이펙트 저장
        GameMode[] playerGameMode = new GameMode[size]; // 플레이어 게임모드 저장
        String[] playerName = new String[size]; // 플레이어 이름 저장


        for (int i = 0; i < size; i++) {
            playerHealths[i] = onlinePlayers[i].getHealth();
            playerFoodLevel[i][0] = onlinePlayers[i].getFoodLevel();
            playerFoodLevel[i][1] = onlinePlayers[i].getSaturation();
            playerExp[i][0] = onlinePlayers[i].getLevel();
            playerExp[i][1] = onlinePlayers[i].getExp();
            playerAir[i] = onlinePlayers[i].getRemainingAir();
            playerFire[i] = onlinePlayers[i].getFireTicks();
            playerInventory[i] = onlinePlayers[i].getInventory().getContents();
            playerLocation[i] = onlinePlayers[i].getLocation();
            playerRespawnLocation[i] = onlinePlayers[i].getRespawnLocation();
            playerPotion[i] = onlinePlayers[i].getActivePotionEffects().stream().toList();
            for(PotionEffect potionEffect : playerPotion[i]){
                onlinePlayers[i].removePotionEffect(potionEffect.getType());
            }
            playerGameMode[i] = onlinePlayers[i].getGameMode();
            playerName[i] = onlinePlayers[i].getName();
        }

        int[] targetPlayers = generateDerangement(size);

        for (int i = 0; i < size; i++) {
            int target = targetPlayers[i];

            // 상태 불러오기
            double newHealth = playerHealths[target];
            float[] newFoodLevel = playerFoodLevel[target];
            int newLevel = (int) playerExp[target][0];
            float newExp = playerExp[target][1];
            int newAir = playerAir[target];
            int newFire = playerFire[target];
            ItemStack[] newInventory = playerInventory[target];
            Location newLocation = playerLocation[target];
            Location newRespawnLocation = playerRespawnLocation[target];
            List<PotionEffect> newPotion = playerPotion[target];
            GameMode newGameMode = playerGameMode[target];
            String newName = playerName[target];

            // 싱태 적용
            Player player = onlinePlayers[i];
            player.setHealth(newHealth);
            player.setFoodLevel((int) newFoodLevel[0]);
            player.setSaturation(newFoodLevel[1]);
            player.setLevel(newLevel);
            player.setExp(newExp);
            player.setRemainingAir(newAir);
            player.setFireTicks(newFire);
            player.getInventory().setContents(newInventory);
            player.teleport(newLocation);
            player.setRespawnLocation(newRespawnLocation, true);
            player.addPotionEffects(newPotion);
            player.setGameMode(newGameMode);
            ChangeSkin changeSkin = new ChangeSkin();
            changeSkin.changeSkin(player, newName);
        }
    }

    // derangement 생성 함수
    public static int[] generateDerangement(int n) {
        Random random = new Random();
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = i;
        }
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            if (j == i) {
                j = (j + 1) % (i + 1);
            }
            int temp = result[i];
            result[i] = result[j];
            result[j] = temp;
        }
        return result;
    }
}
