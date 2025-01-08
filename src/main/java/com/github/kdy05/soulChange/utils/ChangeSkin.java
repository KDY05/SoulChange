package com.github.kdy05.soulChange.utils;

import com.github.kdy05.soulChange.SoulChange;
import net.pinger.disguise.DisguiseAPI;
import net.pinger.disguise.skin.Skin;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChangeSkin {
    // 스킨 캐싱을 위한 맵
    private final Map<String, Skin> skinCache = new ConcurrentHashMap<>();

    public void changeSkin(Player player, String name){
        Skin skin = getSkinFromCache(name);
        SoulChange.getDisguiseProvider().updatePlayerSilently(player, skin, name);
    }

    private Skin getSkinFromCache(String name) {
        if (skinCache.containsKey(name)) {
            return skinCache.get(name);
        }
        Skin skin = DisguiseAPI.getSkinManager().getFromMojang(name);
        if (skin != null) {
            skinCache.put(name, skin);
        }
        return skin;
    }
}
