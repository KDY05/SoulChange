package com.github.kdy05.soulChange.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MojangNameFetcher {

    // 캐시: UUID -> 유저네임
    private static final Map<UUID, String> nameCache = new ConcurrentHashMap<>();

    /**
     * UUID로부터 유저네임을 가져옵니다. 캐시가 우선 사용되며, 없으면 Mojang API를 통해 조회합니다.
     *
     * @param uuid 대상 플레이어의 UUID
     * @return 유저네임 (조회 실패 시 null)
     */
    public static String getNameFromUUID(UUID uuid) {
        if (nameCache.containsKey(uuid)) {
            return nameCache.get(uuid);
        }

        try {
            // Mojang API는 하이픈 없는 UUID를 요구함
            String trimmedUUID = uuid.toString().replace("-", "");
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + trimmedUUID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == 200) {
                try (Reader reader = new InputStreamReader(connection.getInputStream())) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    String name = json.get("name").getAsString();
                    nameCache.put(uuid, name); // 캐싱
                    return name;
                }
            } else {
                Bukkit.getLogger().warning("Mojang API 요청 실패: 응답 코드 " + connection.getResponseCode());
            }

        } catch (Exception e) {
            Bukkit.getLogger().warning("UUID로부터 이름을 조회하는 도중 오류 발생: " + e.getMessage());
        }

        return null;
    }

    /**
     * 캐시 초기화 (선택적으로 제공)
     */
    public static void clearCache() {
        nameCache.clear();
    }
}
