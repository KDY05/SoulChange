package com.github.kdy05.soulChange.logic;

import com.github.kdy05.soulChange.utils.SkinManager;
import net.skinsrestorer.api.property.SkinIdentifier;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.function.Predicate;

public class PlayerState {
    private final double health;
    private final int foodLevel;
    private final float saturation;
    private final int level;
    private final float exp;
    private final int remainingAir;
    private final int fireTicks;
    private final ItemStack[] inventory;
    private final int heldItemSlot;
    private final Location location;
    private final Location respawnLocation;
    private final Entity vehicle;
    private final ArrayList<PotionEffect> potionEffects;
    private final ArrayList<Entity> aggroEntities;
    private final GameMode gameMode;
    private final SkinIdentifier skinIdentifier;

    private PlayerState(Builder builder) {
        this.health = builder.health;
        this.foodLevel = builder.foodLevel;
        this.saturation = builder.saturation;
        this.level = builder.level;
        this.exp = builder.exp;
        this.remainingAir = builder.remainingAir;
        this.fireTicks = builder.fireTicks;
        this.inventory = builder.inventory;
        this.heldItemSlot = builder.heldItemSlot;
        this.location = builder.location;
        this.respawnLocation = builder.respawnLocation;
        this.vehicle = builder.vehicle;
        this.potionEffects = builder.potionEffects;
        this.aggroEntities = builder.aggroEntities;
        this.gameMode = builder.gameMode;
        this.skinIdentifier = builder.skinIdentifier;
    }

    public static PlayerState saveFrom(Player player) {
        return new Builder()
                .health(player.getHealth())
                .foodLevel(player.getFoodLevel())
                .saturation(player.getSaturation())
                .level(player.getLevel())
                .exp(player.getExp())
                .remainingAir(player.getRemainingAir())
                .fireTicks(player.getFireTicks())
                .inventory(player.getInventory().getContents())
                .heldItemSlot(player.getInventory().getHeldItemSlot())
                .location(player.getLocation())
                .respawnLocation(player.getRespawnLocation())
                .vehicle(player.getVehicle())
                .potionEffects(saveAndClearPotionEffects(player))
                .aggroEntities(saveAggroEntities(player))
                .gameMode(player.getGameMode())
                .skinIdentifier(SkinManager.getSkinIdByPlayer(player))
                .build();
    }

    public void applyTo(Player player) {
        player.setHealth(health);
        player.setFoodLevel(foodLevel);
        player.setSaturation(saturation);
        player.setLevel(level);
        player.setExp(exp);
        player.setRemainingAir(remainingAir);
        player.setFireTicks(fireTicks);
        player.getInventory().setContents(inventory);
        player.getInventory().setHeldItemSlot(heldItemSlot);
        player.teleport(location);
        player.setRespawnLocation(respawnLocation, true);
        if (vehicle != null) {
            vehicle.addPassenger(player);
        }
        player.addPotionEffects(potionEffects);
        for (Entity entity : aggroEntities) {
            ((Mob) entity).setTarget(null);
            ((Mob) entity).setTarget(player);
        }
        player.setGameMode(gameMode);
        SkinManager.setSkinById(player, skinIdentifier);
    }

    private static ArrayList<PotionEffect> saveAndClearPotionEffects(Player player) {
        ArrayList<PotionEffect> effects = new ArrayList<>(player.getActivePotionEffects());
        for (PotionEffect effect : effects) {
            player.removePotionEffect(effect.getType());
        }
        return effects;
    }

    private static ArrayList<Entity> saveAggroEntities(Player player) {
        ArrayList<Entity> aggroEntities = new ArrayList<>();
        Predicate<Entity> isMob = entity -> entity instanceof Mob;
        
        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10, isMob)) {
            if (((Mob) entity).getTarget() == player) {
                aggroEntities.add(entity);
            }
        }
        return aggroEntities;
    }

    public static class Builder {
        private double health;
        private int foodLevel;
        private float saturation;
        private int level;
        private float exp;
        private int remainingAir;
        private int fireTicks;
        private ItemStack[] inventory;
        private int heldItemSlot;
        private Location location;
        private Location respawnLocation;
        private Entity vehicle;
        private ArrayList<PotionEffect> potionEffects;
        private ArrayList<Entity> aggroEntities;
        private GameMode gameMode;
        private SkinIdentifier skinIdentifier;

        public Builder health(double health) {
            this.health = health;
            return this;
        }

        public Builder foodLevel(int foodLevel) {
            this.foodLevel = foodLevel;
            return this;
        }

        public Builder saturation(float saturation) {
            this.saturation = saturation;
            return this;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Builder exp(float exp) {
            this.exp = exp;
            return this;
        }

        public Builder remainingAir(int remainingAir) {
            this.remainingAir = remainingAir;
            return this;
        }

        public Builder fireTicks(int fireTicks) {
            this.fireTicks = fireTicks;
            return this;
        }

        public Builder inventory(ItemStack[] inventory) {
            this.inventory = inventory;
            return this;
        }

        public Builder heldItemSlot(int heldItemSlot) {
            this.heldItemSlot = heldItemSlot;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder respawnLocation(Location respawnLocation) {
            this.respawnLocation = respawnLocation;
            return this;
        }

        public Builder vehicle(Entity vehicle) {
            this.vehicle = vehicle;
            return this;
        }

        public Builder potionEffects(ArrayList<PotionEffect> potionEffects) {
            this.potionEffects = potionEffects;
            return this;
        }

        public Builder aggroEntities(ArrayList<Entity> aggroEntities) {
            this.aggroEntities = aggroEntities;
            return this;
        }

        public Builder gameMode(GameMode gameMode) {
            this.gameMode = gameMode;
            return this;
        }

        public Builder skinIdentifier(SkinIdentifier skinIdentifier) {
            this.skinIdentifier = skinIdentifier;
            return this;
        }

        public PlayerState build() {
            return new PlayerState(this);
        }
    }
}