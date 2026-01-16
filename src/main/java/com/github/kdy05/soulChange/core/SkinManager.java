package com.github.kdy05.soulChange.core;

import com.github.kdy05.soulChange.SoulChange;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.exception.MineSkinException;
import net.skinsrestorer.api.property.InputDataResult;
import net.skinsrestorer.api.property.SkinIdentifier;
import net.skinsrestorer.api.storage.PlayerStorage;
import net.skinsrestorer.api.storage.SkinStorage;
import org.bukkit.entity.Player;

import java.util.Optional;


public class SkinManager {

    public static void setSkinByName(Player player, String skinName) {
        SkinStorage skinStorage = SoulChange.getSkinsRestorerAPI().getSkinStorage();
        PlayerStorage playerStorage = SoulChange.getSkinsRestorerAPI().getPlayerStorage();

        // Find or fetch the skin data
        try {
            Optional<InputDataResult> result = skinStorage.findOrCreateSkinData(skinName);
            if (result.isPresent()) {
                // Set the skin identifier for the player
                playerStorage.setSkinIdOfPlayer(
                        player.getUniqueId(),
                        result.get().getIdentifier()
                );
                // Apply the skin visually
                SoulChange.getSkinsRestorerAPI().getSkinApplier(Player.class).applySkin(player);
            }
        } catch (DataRequestException | MineSkinException e) {
            SoulChange.getPlugin().getLogger().warning("Failed to fetch skin: " + e.getMessage());
        }
    }

    public static SkinIdentifier getSkinIdByPlayer(Player player) {
        SkinStorage skinStorage = SoulChange.getSkinsRestorerAPI().getSkinStorage();
        PlayerStorage playerStorage = SoulChange.getSkinsRestorerAPI().getPlayerStorage();
        Optional<SkinIdentifier> skinIdentifier = playerStorage.getSkinIdOfPlayer(player.getUniqueId());
        if (skinIdentifier.isPresent()) {
            return skinIdentifier.get();
        }
        try {
            Optional<InputDataResult> result = skinStorage.findOrCreateSkinData(player.getName());
            if (result.isPresent()) {
                return result.get().getIdentifier();
            }
        } catch (DataRequestException | MineSkinException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void setSkinById(Player player, SkinIdentifier skinIdentifier) {
        PlayerStorage playerStorage = SoulChange.getSkinsRestorerAPI().getPlayerStorage();
        try {
            if (skinIdentifier != null) {
                // Set the skin identifier for the player
                playerStorage.setSkinIdOfPlayer(
                        player.getUniqueId(),
                        skinIdentifier
                );
                // Apply the skin visually
                SoulChange.getSkinsRestorerAPI().getSkinApplier(Player.class).applySkin(player);
            }
        } catch (DataRequestException e) {
            SoulChange.getPlugin().getLogger().warning("Failed to fetch skin: " + e.getMessage());
        }
    }

}
