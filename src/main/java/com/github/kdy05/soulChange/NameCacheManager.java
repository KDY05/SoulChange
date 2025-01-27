package com.github.kdy05.soulChange;

import com.github.kdy05.soulChange.utils.ChangeSkin;
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
    private final SoulChange plugin = SoulChange.getPlugin();
    private final Map<UUID, String> nameCache = new HashMap<>();
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

    public void updateAllPlayer() {
        for (Player player : Bukkit.getOnlinePlayers()){
            String name = getName(player.getUniqueId());
            new ChangeSkin().changeSkin(player, name);
        }
    }
}
