package com.github.kdy05.soulChange.listener;

import com.github.kdy05.soulChange.logic.StatusChanger;
import com.github.kdy05.soulChange.SoulChange;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SoulChangeListener implements Listener {

    private final SoulChange plugin;

    public SoulChangeListener(SoulChange plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent e){
        if (e.getEntityType() != EntityType.PLAYER) return;

        if (plugin.getConfig().getBoolean("damage-share.enabled", false))
            shareDamage(e);

        if (plugin.getConfig().getBoolean("change-on-damaged.enabled", false)){
            Player player = (Player) e.getEntity();
            if (player.getHealth() <= e.getFinalDamage()) return; // 죽을 예정이면 실행하지 않음
            if (e.getFinalDamage() == 0) return; // 방패 예외 처리

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (e.getDamageSource().getDamageType().equals(DamageType.GENERIC))
                    return;
                notifyDamagedPlayer(player);
                if (plugin.getConfig().getBoolean("change-on-damaged.swap-mode", false)){
                    Player target = getRandomPlayer(player);
                    if (target != null) StatusChanger.swapStatus(player, target);
                } else {
                    StatusChanger.changeStatus();
                }
            }, 1);
        }
    }

    private void shareDamage(EntityDamageEvent e) {
        if (e.getDamageSource().getDamageType().equals(DamageType.GENERIC))
            return;

        Player origin = (Player) e.getEntity();
        double rate = plugin.getConfig().getDouble("damage-share.rate", 1.0);
        double damage = e.getDamage() * rate;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == origin) {
                continue;
            }
            DamageSource custom = DamageSource.builder(DamageType.GENERIC).build();
            player.damage(damage, custom);
        }
    }

    private void notifyDamagedPlayer(Player player) {
        if (!plugin.getConfig().getBoolean("change-on-damaged.notify-damagee", false))
            return;
        if (player != null) {
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
                    SoulChange.PLUGIN_ID + "대미지 입은 사람: " + player.getName()));
        }
    }

    @Nullable
    private static Player getRandomPlayer(@NotNull Player exclude) {
        // 조건을 만족하는 플레이어 리스트 생성
        List<Player> eligiblePlayers = Bukkit.getOnlinePlayers().stream()
                .filter(player -> !player.equals(exclude))           // 제외할 플레이어 필터링
                .filter(Player::isOnline)                            // 온라인 확인
                .filter(player -> !player.isDead())                  // 죽지 않은 플레이어
                .filter(player -> player.getGameMode() != GameMode.SPECTATOR) // 관전자 모드 제외
                .collect(Collectors.toList());

        // 조건을 만족하는 플레이어가 없으면 null 반환
        if (eligiblePlayers.isEmpty()) {
            return null;
        }

        // 셔플 후 첫 번째 요소 반환
        Collections.shuffle(eligiblePlayers);
        return eligiblePlayers.getFirst();
    }

}
