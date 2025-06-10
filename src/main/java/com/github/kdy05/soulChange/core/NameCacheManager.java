package com.github.kdy05.soulChange.core;

import com.github.kdy05.soulChange.SoulChange;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class NameCacheManager {
    private final SoulChange plugin = SoulChange.getPlugin();
    private final Map<UUID, String> nameCache = new HashMap<>();
    private static final Map<UUID, String> realNameCache = new HashMap<>();
    private final File file;
    private final FileConfiguration config;

    public NameCacheManager() {
        String path = plugin.getDataFolder().getAbsolutePath();
        this.file = new File(path + "/" + "nameCache.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        loadCache();
    }

    // 데이터를 파일에 저장
    private void saveConfig() {
        if(config == null)
            return;
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 파일에서 데이터를 로드하여 캐시맵에 저장
    private void loadCache() {
        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                String name = config.getString(key);
                if (name != null) {
                    nameCache.put(uuid, name);
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("잘못된 UUID 형식: " + key);
            }
        }
    }

    // UUID 로 이름을 가져옴
    public String getName(UUID uuid) {
        return nameCache.get(uuid);
    }

    // 캐시에 이름을 설정하고 파일에 저장
    public void setName(UUID uuid, String name) {
        nameCache.put(uuid, name);
        config.set(uuid.toString(), name);
        saveConfig();
    }

    public List<UUID> getUUIDsByName(String name) {
        List<UUID> result = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : nameCache.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(name)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    // 플레이어의 실제 이름을 캐시에 저장
    public static void setRealName(UUID uuid, String realName) {
        realNameCache.put(uuid, realName);
    }

    // 플레이어의 실제 이름을 가져옴 (변장되지 않은 원래 이름)
    public static String getRealName(UUID uuid) {
        return realNameCache.get(uuid);
    }

}
