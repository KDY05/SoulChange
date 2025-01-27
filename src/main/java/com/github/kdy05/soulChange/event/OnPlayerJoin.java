package com.github.kdy05.soulChange.event;

import com.github.kdy05.soulChange.SoulChange;
import com.github.kdy05.soulChange.utils.ChangeSkin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OnPlayerJoin implements Listener {
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
}
