package com.github.kdy05.soulChange.event;

import com.github.kdy05.soulChange.ChangeStatus;
import com.github.kdy05.soulChange.SoulChange;
import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;


public class OnPlayerDamaged implements Listener {
    @EventHandler
    public void onPlayerJoin(EntityDamageEvent e){
        if (e.getEntityType() != EntityType.PLAYER) {
            return;
        }

        if (SoulChange.getPlugin().getConfig().getBoolean("damage-share")){
            shareDamage(e);
        }

        if (SoulChange.getPlugin().getConfig().getBoolean("change-on-damaged")){
            Bukkit.getScheduler().runTaskLater(SoulChange.getPlugin(), () ->
                    changeStatus(e), 1);
        }
    }

    private void shareDamage(EntityDamageEvent e) {
        if (e.getDamageSource().getDamageType().equals(DamageType.GENERIC)) {
            return;
        }

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

    private void changeStatus(EntityDamageEvent e) {
        if (e.getDamageSource().getDamageType().equals(DamageType.GENERIC)) {
            return;
        }
        ChangeStatus.changeStatus();
    }

}
