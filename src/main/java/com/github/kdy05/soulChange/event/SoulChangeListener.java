package com.github.kdy05.soulChange.event;

import com.github.kdy05.soulChange.ChangeStatus;
import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.utils.ChangeSkin;
import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;


public class SoulChangeListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        // 재접속 시 스킨 유지
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String name = SoulChange.getNameCacheManager().getName(uuid);
        if (name != null) {
            new ChangeSkin().changeSkin(player, name);
        }
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent e){
        if (e.getEntityType() != EntityType.PLAYER) return;

        if (SoulChange.getPlugin().getConfig().getBoolean("damage-share", false))
            shareDamage(e);

        if (SoulChange.getPlugin().getConfig().getBoolean("change-on-damaged", false)){
            Bukkit.getScheduler().runTaskLater(SoulChange.getPlugin(), () -> {
                if (e.getDamageSource().getDamageType().equals(DamageType.GENERIC))
                    return;
                ChangeStatus.changeStatus();
            }, 1);
        }
    }

    private void shareDamage(EntityDamageEvent e) {
        if (e.getDamageSource().getDamageType().equals(DamageType.GENERIC))
            return;

        Player origin = (Player) e.getEntity();
        double rate = SoulChange.getPlugin().getConfig().getDouble("damage-share-rate", 1.0);
        double damage = e.getFinalDamage() * rate;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == origin) {
                continue;
            }
            DamageSource custom = DamageSource.builder(DamageType.GENERIC).build();
            player.damage(damage, custom);
        }
    }

}
