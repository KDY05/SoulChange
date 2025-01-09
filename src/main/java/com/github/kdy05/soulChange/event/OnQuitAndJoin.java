package com.github.kdy05.soulChange.event;

import com.github.kdy05.soulChange.utils.ChangeSkin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OnQuitAndJoin implements Listener {
    // 재접속시 변장 풀림 방지용 "UUID to 변장 닉네임" 맵
    private static final Map<UUID, String> nameCache = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        nameCache.put(player.getUniqueId(), player.getName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        String name = nameCache.get(player.getUniqueId());
        ChangeSkin changeSkin = new ChangeSkin();
        changeSkin.changeSkin(player, name);
    }
}
