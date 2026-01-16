package com.github.kdy05.soulChange.listener;

import com.github.kdy05.soulChange.core.StatusChanger;
import com.github.kdy05.soulChange.SoulChange;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;


public class SoulChangeListener implements Listener {

    private final SoulChange plugin;

    public SoulChangeListener(SoulChange plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent e){
        if (e.getEntityType() != EntityType.PLAYER) return;

        if (plugin.getConfig().getBoolean("damage-share", false))
            shareDamage(e);

        if (plugin.getConfig().getBoolean("change-on-damaged", false)){
            Player player = (Player) e.getEntity();
            if (player.getHealth() <= e.getFinalDamage()) return; // 죽을 예정이면 실행하지 않음
            if (e.getFinalDamage() == 0) return; // 방패 예외 처리

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (e.getDamageSource().getDamageType().equals(DamageType.GENERIC))
                    return;
                notifyDamagedPlayer(player);
                StatusChanger.changeStatus();
            }, 1);
        }
    }

    private void shareDamage(EntityDamageEvent e) {
        if (e.getDamageSource().getDamageType().equals(DamageType.GENERIC))
            return;

        Player origin = (Player) e.getEntity();
        double rate = plugin.getConfig().getDouble("damage-share-rate", 1.0);
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
        if (!plugin.getConfig().getBoolean("notify-damaged-player", false))
            return;
        if (player != null) {
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
                    SoulChange.PLUGIN_ID + "대미지 입은 사람: " + player.getName()));
        }
    }

}
