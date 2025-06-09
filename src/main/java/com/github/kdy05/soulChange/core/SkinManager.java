package com.github.kdy05.soulChange.core;

import com.github.kdy05.soulChange.SoulChange;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.skin.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SkinManager {
    // 스킨 캐싱을 위한 맵. 캐싱을 하지 않으면 getFromMojang()의 접근 횟수 제한 때문에 오류 발생 여지가 있음.
    private static final Map<String, Skin> skinCache = new ConcurrentHashMap<>();

    public static void changeSkin(Player player, String name) {
        SoulChange.getNameCacheManager().setName(player.getUniqueId(), name);
        Skin skin = getSkinFromCache(name);
        if (skin == null) return;
        SoulChange.getDisguiseProvider().updatePlayerSilently(player, skin, name);
    }

    private static Skin getSkinFromCache(String name) {
        if (skinCache.containsKey(name)) {
            return skinCache.get(name);
        }
        Skin skin = DisguiseAPI.getSkinManager().getFromMojang(name);
        if (skin != null) {
            skinCache.put(name, skin);
        }
        return skin;
    }

    public static void updateAllPlayer() {
        for (Player player : Bukkit.getOnlinePlayers()){
            String name = SoulChange.getNameCacheManager().getName(player.getUniqueId());
            if (name != null) {
                changeSkin(player, name);
            }
        }
    }
}
