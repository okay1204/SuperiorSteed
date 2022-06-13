package me.screescree.SuperiorSteed.superiorhorse;

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
    private SuperiorHorseEntity nmsEntity;
    private Horse bukkitEntity;
    
    private Stat hunger;
    private Stat hydration;
    private Stat trust;
    private Stat friendliness;
    private Stat comfortability;
    private Stat waterBravery;

    private boolean isMale;

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
        
        
        PersistentDataContainer container = horse.getPersistentDataContainer();
        SuperiorHorseInfo generatedInfo = SuperiorHorseInfo.generateNew();
        
        double hunger = containerValueOrDefault(container, "hunger", generatedInfo.getHunger());
        double hydration = containerValueOrDefault(container, "hydration", generatedInfo.getHydration());
        double trust = containerValueOrDefault(container, "trust", generatedInfo.getTrust());
        double friendliness = containerValueOrDefault(container, "friendliness", generatedInfo.getFriendliness());
        double comfortability = containerValueOrDefault(container, "comfortability", generatedInfo.getComfortability());
        double waterBravery = containerValueOrDefault(container, "waterBravery", generatedInfo.getWaterBravery());

        boolean isMale = containerValueOrDefault(container, "isMale", generatedInfo.isMale());
        
        SuperiorHorseInfo horseInfo = new SuperiorHorseInfo(hunger, hydration, trust, friendliness, comfortability, waterBravery, isMale);
        
        initializeInfo(horseInfo);
        horse.remove();
    }
    
    public SuperiorHorse(Location spawnLocation) {
        this(spawnLocation, SuperiorHorseInfo.generateNew());
    }
    
    public SuperiorHorse(Location spawnLocation, SuperiorHorseInfo horseInfo) {
        nmsEntity = new SuperiorHorseEntity(spawnLocation.getWorld());
        bukkitEntity = ((CraftWorld) spawnLocation.getWorld()).addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        bukkitEntity.teleport(spawnLocation, TeleportCause.PLUGIN);

        initializeInfo(horseInfo);
    }

    private double containerValueOrDefault(PersistentDataContainer container, String keyName, double defaultValue) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        if (container.has(key, PersistentDataType.DOUBLE)) {
            return container.get(key, PersistentDataType.DOUBLE);
        }
        else {
            return defaultValue;
        }
    }

    public boolean containerValueOrDefault(PersistentDataContainer container, String keyName, boolean defaultValue) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        BooleanTagType type = new BooleanTagType();
        if (container.has(key, type)) {
            return container.get(key, type);
        }
        else {
            return defaultValue;
        }
    }

    // bukkitEntity must be set before calling this method
    private void initializeInfo(SuperiorHorseInfo horseInfo) {
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        PersistentDataContainer container = bukkitEntity.getPersistentDataContainer();

        hunger = new Stat(horseInfo.getHunger(), container, new NamespacedKey(plugin, "hunger"));
        hydration = new Stat(horseInfo.getHydration(), container, new NamespacedKey(plugin, "hydration"));
        trust = new Stat(horseInfo.getTrust(), container, new NamespacedKey(plugin, "trust"));
        friendliness = new Stat(horseInfo.getFriendliness(), container, new NamespacedKey(plugin, "friendliness"));
        comfortability = new Stat(horseInfo.getComfortability(), container, new NamespacedKey(plugin, "comfortability"));
        waterBravery = new Stat(horseInfo.getWaterBravery(), container, new NamespacedKey(plugin, "waterBravery"));

        isMale = horseInfo.isMale();
        container.set(new NamespacedKey(plugin, "isMale"), new BooleanTagType(), isMale);
    }

    public boolean equals(SuperiorHorse other) {
        return nmsEntity.getUUID().equals(other.getNMSEntity().getUUID());
    }

    public boolean equals(Entity other) {
        return nmsEntity.getUUID().equals(other.getUniqueId());
    }

    public SuperiorHorseInfo getInfo() {
        return new SuperiorHorseInfo(hunger.get(), hydration.get(), trust.get(), friendliness.get(), comfortability.get(), waterBravery.get(), isMale());
    }

    public String getName() {
        return getBukkitEntity().getCustomName() != null ? getBukkitEntity().getCustomName() : "Horse";
    }

    public String getName(int limit) {
        String horseName = getName();

        if (horseName.length() > limit) {
            horseName = horseName.substring(0, limit);
        }

        return horseName;
    }

    public Stat getStat(String statName) {
        switch (statName.toLowerCase()) {
            case "hunger":
                return hunger;
            case "hydration":
                return hydration;
            case "trust":
                return trust;
            case "friendliness":
                return friendliness;
            case "comfortability":
                return comfortability;
            case "waterbravery":
                return waterBravery;
            default:
                return null;
        }
    }
    
    public Stat hungerStat() {
        return hunger;
    }

    public Stat hydrationStat() {
        return hydration;
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

    public boolean isMale() {
        return isMale;
    }

    public SuperiorHorseEntity getNMSEntity() {
        return nmsEntity;
    }

    public Horse getBukkitEntity() {
        return bukkitEntity;
    }
}

