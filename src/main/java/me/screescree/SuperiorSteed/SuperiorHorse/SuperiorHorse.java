package me.screescree.SuperiorSteed.SuperiorHorse;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.screescree.SuperiorSteed.SuperiorSteed;

public class SuperiorHorse {
    private static SuperiorSteed plugin;

    private SuperiorHorseEntity nmsEntity;
    private Horse bukkitEntity;
    
    private Stat hunger;
    private Stat trust;
    private Stat friendliness;
    private Stat comfortability;
    private Stat waterBravery;

    public static void setPlugin(SuperiorSteed plugin) {
        SuperiorHorse.plugin = plugin;
    }

    public SuperiorHorse(Horse horse) {
        Location spawnLocation = horse.getLocation();

        nmsEntity = new SuperiorHorseEntity(horse);
        bukkitEntity = ((CraftWorld) spawnLocation.getWorld()).addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        bukkitEntity.setRotation(spawnLocation.getYaw(), spawnLocation.getPitch());
        bukkitEntity.teleport(spawnLocation, TeleportCause.PLUGIN);
        bukkitEntity.setAI(horse.hasAI());
        bukkitEntity.setTamed(horse.isTamed());
        bukkitEntity.setOwner(horse.getOwner());
        if (horse.isAdult()) {
            bukkitEntity.setAdult();
        }
        else {
            bukkitEntity.setBaby();
        }
        
        bukkitEntity.setDomestication(horse.getDomestication());
        bukkitEntity.setMaxDomestication(horse.getMaxDomestication());
        bukkitEntity.setJumpStrength(horse.getJumpStrength());
        bukkitEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
        bukkitEntity.setEatingHaystack(horse.isEatingHaystack());
        bukkitEntity.setStyle(horse.getStyle());
        bukkitEntity.setColor(horse.getColor());
        HorseInventory inventory = bukkitEntity.getInventory();
        inventory.setSaddle(horse.getInventory().getSaddle());
        inventory.setArmor(horse.getInventory().getArmor());

        bukkitEntity.setBreedCause(horse.getBreedCause());
        bukkitEntity.setLoveModeTicks(horse.getLoveModeTicks());
        bukkitEntity.setBreed(horse.canBreed());
        bukkitEntity.setAgeLock(true);
        bukkitEntity.setAbsorptionAmount(horse.getAbsorptionAmount());;
        bukkitEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        bukkitEntity.setHealth(horse.getHealth());

        for (Entity passenger : horse.getPassengers()) {
            passenger.leaveVehicle();
            bukkitEntity.addPassenger(passenger);
        }
        for (String scoreboardTag : horse.getScoreboardTags()) {
            bukkitEntity.addScoreboardTag(scoreboardTag);
        }
        bukkitEntity.setFallDistance(horse.getFallDistance());
        bukkitEntity.setFireTicks(horse.getFireTicks());
        bukkitEntity.setFreezeTicks(horse.getFreezeTicks());
        bukkitEntity.setLastDamageCause(horse.getLastDamageCause());
        bukkitEntity.setPortalCooldown(horse.getPortalCooldown());
        bukkitEntity.setTicksLived(horse.getTicksLived() < 1 ? 1 : horse.getTicksLived());

        if (horse.isInsideVehicle()) {
            Entity vehicle = horse.getVehicle();
            horse.leaveVehicle();
            vehicle.addPassenger(bukkitEntity);
        }

        bukkitEntity.setInvulnerable(horse.isInvulnerable());
        bukkitEntity.setPersistent(horse.isPersistent());
        bukkitEntity.setSilent(horse.isSilent());
        bukkitEntity.setVisualFire(horse.isVisualFire());
        bukkitEntity.setCustomName(horse.getCustomName());
        bukkitEntity.setCustomNameVisible(horse.isCustomNameVisible());
        bukkitEntity.setGlowing(horse.isGlowing());
        bukkitEntity.setGravity(horse.hasGravity());
        bukkitEntity.addPotionEffects(horse.getActivePotionEffects());
        bukkitEntity.setCollidable(horse.isCollidable());
        bukkitEntity.setGliding(horse.isGliding());
        bukkitEntity.setInvisible(horse.isInvisible());
        bukkitEntity.setLastDamage(horse.getLastDamage());
        if (horse.isLeashed()) {
            bukkitEntity.setLeashHolder(horse.getLeashHolder());
        }
        bukkitEntity.setMaximumAir(horse.getMaximumAir());
        bukkitEntity.setMaximumNoDamageTicks(horse.getMaximumNoDamageTicks());
        bukkitEntity.setNoDamageTicks(horse.getNoDamageTicks());
        bukkitEntity.setRemainingAir(horse.getRemainingAir());
        bukkitEntity.setSwimming(horse.isSwimming());
        
        horse.remove();
        
        // // TODO copy all the data from the normal horse to the superior horse
        PersistentDataContainer container = bukkitEntity.getPersistentDataContainer();

        double hunger = containerValueOrDefault(container, "hunger", PersistentDataType.DOUBLE);
        double trust = containerValueOrDefault(container, "trust", PersistentDataType.DOUBLE);
        double friendliness = containerValueOrDefault(container, "friendliness", PersistentDataType.DOUBLE);
        double comfortability = containerValueOrDefault(container, "comfortability", PersistentDataType.DOUBLE);
        double waterBravery = containerValueOrDefault(container, "waterBravery", PersistentDataType.DOUBLE);

        SuperiorHorseInfo horseInfo = new SuperiorHorseInfo(hunger, trust, friendliness, comfortability, waterBravery);

        initializeInfo(horseInfo);
    }
    
    public SuperiorHorse(Location spawnLocation) {
        this(spawnLocation, SuperiorHorseInfo.DEFAULT);
    }
    
    public SuperiorHorse(Location spawnLocation, SuperiorHorseInfo horseInfo) {
        nmsEntity = new SuperiorHorseEntity(spawnLocation.getWorld());
        bukkitEntity = ((CraftWorld) spawnLocation.getWorld()).addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        bukkitEntity.teleport(spawnLocation, TeleportCause.PLUGIN);

        initializeInfo(horseInfo);
    }

    private double containerValueOrDefault(PersistentDataContainer container, String keyName, PersistentDataType<Double, Double> type) {
        NamespacedKey key = new NamespacedKey(plugin, keyName);
        if (container.has(key, type)) {
            return container.get(key, type);
        }
        else {
            return (double) SuperiorHorseInfo.DEFAULT_MAP.get(keyName);
        }
    }

    // bukkitEntity must be set before calling this method
    private void initializeInfo(SuperiorHorseInfo horseInfo) {
        PersistentDataContainer container = bukkitEntity.getPersistentDataContainer();

        hunger = new Stat(horseInfo.getHunger(), container, new NamespacedKey(plugin, "hunger"));
        trust = new Stat(horseInfo.getTrust(), container, new NamespacedKey(plugin, "trust"));
        friendliness = new Stat(horseInfo.getFriendliness(), container, new NamespacedKey(plugin, "friendliness"));
        comfortability = new Stat(horseInfo.getComfortability(), container, new NamespacedKey(plugin, "comfortability"));
        waterBravery = new Stat(horseInfo.getWaterBravery(), container, new NamespacedKey(plugin, "waterBravery"));
    }

    public boolean equals(SuperiorHorse other) {
        return nmsEntity.getUUID().equals(other.getNMSEntity().getUUID());
    }

    public boolean equals(Entity other) {
        return nmsEntity.getUUID().equals(other.getUniqueId());
    }

    public SuperiorHorseInfo getInfo() {
        return new SuperiorHorseInfo(hunger.get(), trust.get(), friendliness.get(), comfortability.get(), waterBravery.get());
    }
    
    public Stat hungerStat() {
        return hunger;
    }

    public Stat trustStat() {
        return trust;
    }

    public Stat friendlinessStat() {
        return friendliness;
    }

    public Stat comfortabilityStat() {
        return comfortability;
    }

    public Stat waterBraveryStat() {
        return waterBravery;
    }

    public SuperiorHorseEntity getNMSEntity() {
        return nmsEntity;
    }
}

