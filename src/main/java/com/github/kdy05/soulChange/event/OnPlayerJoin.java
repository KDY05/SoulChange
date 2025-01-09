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
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String name = SoulChange.getNameCacheManager().getName(uuid);
        if (name != null) {
            ChangeSkin changeSkin = new ChangeSkin();
            changeSkin.changeSkin(player, name);
        }
    }
}
