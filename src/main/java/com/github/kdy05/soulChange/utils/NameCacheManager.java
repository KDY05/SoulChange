package com.github.kdy05.soulChange.utils;

import com.github.kdy05.soulChange.SoulChange;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NameCacheManager {
    private final SoulChange plugin = SoulChange.getServerInstance();
    private final File dataFile;
    private final FileConfiguration dataConfig;
    private final Map<UUID, String> nameCache = new HashMap<>();

    public NameCacheManager() {
        this.dataFile = new File(plugin.getDataFolder(), "nameCache.yml");

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("nameCache.yml 파일을 생성할 수 없습니다.");
            }
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        loadCache();
    }

    // 파일에서 데이터를 로드하여 캐시에 저장
    private void loadCache() {
        for (String key : dataConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                String name = dataConfig.getString(key);
                if (name != null) {
                    nameCache.put(uuid, name);
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("잘못된 UUID 형식: " + key);
            }
        }
    }

    // 캐시에 이름을 설정하고 파일에 저장
    public void setName(UUID uuid, String name) {
        nameCache.put(uuid, name);
        dataConfig.set(uuid.toString(), name);
        saveData();
    }

    // UUID 로 이름을 가져옴
    public String getName(UUID uuid) {
        return nameCache.get(uuid);
    }

    // 데이터를 파일에 저장
    private void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("nameCache.yml 파일을 저장할 수 없습니다!");
        }
    }

    public void updateAllPlayer() {
        for (Player player : Bukkit.getOnlinePlayers()){
            String name = getName(player.getUniqueId());
            ChangeSkin changeSkin = new ChangeSkin();
            changeSkin.changeSkin(player, name);
        }
    }
}
