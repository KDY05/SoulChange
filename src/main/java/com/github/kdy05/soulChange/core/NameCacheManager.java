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
    private final File nameFile;
    private final FileConfiguration nameConfig;
    private static File realNameFile;
    private static FileConfiguration realNameConfig;

    public NameCacheManager() {
        String path = plugin.getDataFolder().getAbsolutePath();
        this.nameFile = new File(path + "/" + "nameCache.yml");
        this.nameConfig = YamlConfiguration.loadConfiguration(nameFile);
        
        // realName 파일 초기화
        realNameFile = new File(path + "/" + "realName.yml");
        realNameConfig = YamlConfiguration.loadConfiguration(realNameFile);
        
        loadNameCache();
        loadRealNameCache();
    }

    // 파일에서 데이터를 로드하여 캐시맵에 저장
    private void loadNameCache() {
        for (String key : nameConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                String name = nameConfig.getString(key);
                if (name != null) {
                    nameCache.put(uuid, name);
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("잘못된 UUID 형식: " + key);
            }
        }
    }

    // realName 파일에서 데이터를 로드
    private void loadRealNameCache() {
        for (String key : realNameConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                String realName = realNameConfig.getString(key);
                if (realName != null) {
                    realNameCache.put(uuid, realName);
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("잘못된 UUID 형식 (realName): " + key);
            }
        }
    }

    // 데이터를 파일에 저장
    private void saveNameConfig() {
        if(nameConfig == null)
            return;
        try {
            this.nameConfig.save(this.nameFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // realName 데이터를 파일에 저장
    private static void saveRealNameConfig() {
        if (realNameConfig == null || realNameFile == null)
            return;
        try {
            realNameConfig.save(realNameFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // UUID 로 이름을 가져옴
    public String getName(UUID uuid) {
        return nameCache.get(uuid);
    }

    // 플레이어의 실제 이름을 가져옴 (변장되지 않은 원래 이름)
    public static String getRealName(UUID uuid) {
        return realNameCache.get(uuid);
    }

    // 캐시에 이름을 설정하고 파일에 저장
    public void setName(UUID uuid, String name) {
        nameCache.put(uuid, name);
        nameConfig.set(uuid.toString(), name);
        saveNameConfig();
    }

    // 플레이어의 실제 이름을 캐시에 저장
    public static void setRealName(UUID uuid, String realName) {
        realNameCache.put(uuid, realName);
        realNameConfig.set(uuid.toString(), realName);
        saveRealNameConfig();
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

}
