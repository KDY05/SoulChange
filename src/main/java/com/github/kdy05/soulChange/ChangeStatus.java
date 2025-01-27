package com.github.kdy05.soulChange;

import com.github.kdy05.soulChange.utils.ChangeSkin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

public class ChangeStatus {

    public static void changeStatus() {
        // 유효 플레이어 배열 생성
        Player[] onlinePlayers = Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getGameMode() != GameMode.SPECTATOR)  // 관전자 모드 제외
                .filter(player -> !player.isDead())                            // 사망 상태 제외
                .toArray(Player[]::new);

        // 2명 미만이면 중지.
        int size = onlinePlayers.length;
        if (size < 2) {
            Bukkit.getLogger().info("유효 플레이어가 2명 미만입니다.");
            return;
        }

        // 공지 타이틀 출력.
        sendTitleToPlayers();

        /* 모든 플레이어들의 상태를 저장.
        체력, 배고픔, 레벨, 공기, 불타는 시간, 인벤토리, 핫바 슬롯 위치, 현재 위치,
        리스폰 위치, 탑승 상태, 포션효과, 어그로 끌린 몹, 게임모드, 변장한 스킨의 이름 */
        double[] playerHealths = saveHealth(onlinePlayers);
        float[][] playerFoodLevels = saveFoodLevels(onlinePlayers);
        float[][] playerExps = saveExperience(onlinePlayers);
        int[] playerAir = saveAir(onlinePlayers);
        int[] playerFire = saveFireTicks(onlinePlayers);
        ItemStack[][] playerInventories = saveInventories(onlinePlayers);
        int[] playerSlotPos = saveSlotPos(onlinePlayers);
        Location[] playerLocations = saveLocations(onlinePlayers);
        Location[] playerRespawnLocations = saveRespawnLocations(onlinePlayers);
        Entity[] playerVehicle = saveVehicles(onlinePlayers);
        ArrayList<PotionEffect>[] playerPotions = savePotionEffects(onlinePlayers);
        ArrayList<Entity>[] playerAggros = saveAggroStatus(onlinePlayers);
        GameMode[] playerGameModes = saveGameModes(onlinePlayers);
        String[] playerNames = savePlayerNames(onlinePlayers);

        // 상태를 적용할 대상의 인덱스를 생성. 단 자기 자신의 상태를 적용할 수 없음.
        int[] targetPlayers = generateDerangement(size);

        // 각 플레이어에게 랜덤한 다른 플레이어의 상태를 적용.
        for (int i = 0; i < size; i++) {
            int target = targetPlayers[i];
            applyStatus(onlinePlayers[i], playerHealths[target], playerFoodLevels[target], playerExps[target],
                    playerAir[target], playerFire[target], playerInventories[target], playerSlotPos[target],
                    playerLocations[target], playerRespawnLocations[target], playerVehicle[target], playerPotions[target],
                    playerAggros[target], playerGameModes[target], playerNames[target]);
        }
    }

    private static void sendTitleToPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()){
            // 공지 타이틀
            Bukkit.getScheduler().runTaskLater(SoulChange.getPlugin(),
                    () -> player.sendTitle("", ChatColor.GRAY + "모든 플레이어들의 영혼이 뒤바뀌었습니다!", 5, 50, 5), 5L);
        }
    }

    private static double[] saveHealth(Player[] players) {
        double[] healths = new double[players.length];
        for (int i = 0; i < players.length; i++) {
            healths[i] = players[i].getHealth();
        }
        return healths;
    }

    private static float[][] saveFoodLevels(Player[] players) {
        float[][] foodLevels = new float[players.length][2];
        for (int i = 0; i < players.length; i++) {
            foodLevels[i][0] = players[i].getFoodLevel();
            foodLevels[i][1] = players[i].getSaturation();
        }
        return foodLevels;
    }

    private static float[][] saveExperience(Player[] players) {
        float[][] exps = new float[players.length][2];
        for (int i = 0; i < players.length; i++) {
            exps[i][0] = players[i].getLevel();
            exps[i][1] = players[i].getExp();
        }
        return exps;
    }

    private static int[] saveAir(Player[] players) {
        int[] air = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            air[i] = players[i].getRemainingAir();
        }
        return air;
    }

    private static int[] saveFireTicks(Player[] players) {
        int[] fireTicks = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            fireTicks[i] = players[i].getFireTicks();
        }
        return fireTicks;
    }

    private static ItemStack[][] saveInventories(Player[] players) {
        ItemStack[][] inventories = new ItemStack[players.length][];
        for (int i = 0; i < players.length; i++) {
            inventories[i] = players[i].getInventory().getContents();
        }
        return inventories;
    }

    private static int[] saveSlotPos(Player[] players){
        int[] slotpos = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            slotpos[i] = players[i].getInventory().getHeldItemSlot();
        }
        return slotpos;
    }

    private static Location[] saveLocations(Player[] players) {
        Location[] locations = new Location[players.length];
        for (int i = 0; i < players.length; i++) {
            locations[i] = players[i].getLocation();
        }
        return locations;
    }

    private static Location[] saveRespawnLocations(Player[] players) {
        Location[] respawnLocations = new Location[players.length];
        for (int i = 0; i < players.length; i++) {
            respawnLocations[i] = players[i].getRespawnLocation();
        }
        return respawnLocations;
    }

    private static Entity[] saveVehicles(Player[] players) {
        Entity[] vehicles = new Entity[players.length];
        for (int i = 0; i < players.length; i++) {
            vehicles[i] = players[i].getVehicle();
        }
        return vehicles;
    }

    private static ArrayList<PotionEffect>[] savePotionEffects(Player[] players) {
        @SuppressWarnings("unchecked")
        ArrayList<PotionEffect>[] potionEffects = new ArrayList[players.length];
        for (int i = 0; i < players.length; i++) {
            potionEffects[i] = new ArrayList<>(players[i].getActivePotionEffects());
            // 기존 플레이어의 효과를 제거.
            for (PotionEffect effect : potionEffects[i]) {
                players[i].removePotionEffect(effect.getType());
            }
        }
        return potionEffects;
    }

    private static ArrayList<Entity>[] saveAggroStatus(Player[] players) {
        @SuppressWarnings("unchecked")
        ArrayList<Entity>[] aggroEntities = new ArrayList[players.length];
        Predicate<Entity> isMob = entity -> entity instanceof Mob;
        for (int i = 0; i < players.length; i++) {
            aggroEntities[i] = new ArrayList<>();
            // 반지름 10m 구 범위의 어그로 끌린 몹을 저장.
            for (Entity entity : players[i].getWorld().getNearbyEntities(players[i].getLocation(), 10, 10, 10, isMob)) {
                if (((Mob) entity).getTarget() == players[i]) {
                    aggroEntities[i].add(entity);
                }
            }
        }
        return aggroEntities;
    }

    private static GameMode[] saveGameModes(Player[] players) {
        return Arrays.stream(players).map(Player::getGameMode).toArray(GameMode[]::new);
    }

    private static String[] savePlayerNames(Player[] players) {
        return Arrays.stream(players).map(Player::getName).toArray(String[]::new);
    }

    private static void applyStatus(Player player, double health, float[] foodLevel, float[] exp,
                                    int air, int fireTicks, ItemStack[] inventory, int slotPos, Location location,
                                    Location respawnLocation, Entity vehicle, ArrayList<PotionEffect> potions,
                                    ArrayList<Entity> aggros, GameMode gameMode, String playerName) {
        player.setHealth(health);
        player.setFoodLevel((int) foodLevel[0]);
        player.setSaturation(foodLevel[1]);
        player.setLevel((int) exp[0]);
        player.setExp(exp[1]);
        player.setRemainingAir(air);
        player.setFireTicks(fireTicks);
        player.getInventory().setContents(inventory);
        player.getInventory().setHeldItemSlot(slotPos);
        player.teleport(location);
        player.setRespawnLocation(respawnLocation, true);
        if (vehicle != null) {
            vehicle.addPassenger(player);
        }
        player.addPotionEffects(potions);
        for (Entity entity : aggros) {
            ((Mob) entity).setTarget(null);
            ((Mob) entity).setTarget(player);
        }
        player.setGameMode(gameMode);
        new ChangeSkin().changeSkin(player, playerName);
    }

    private static int[] generateDerangement(int n) {
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
