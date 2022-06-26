package me.screescree.SuperiorSteed.superiorhorse;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import me.screescree.SuperiorSteed.superiorhorse.info.Seed;
import me.screescree.SuperiorSteed.superiorhorse.info.SpeedLevel;
import me.screescree.SuperiorSteed.superiorhorse.info.Stat;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;
import me.screescree.SuperiorSteed.superiorhorse.persistenttype.PersistentDataType_BOOLEAN;

public class SuperiorHorse {
    private final String SPEED_LEVEL_KEY = "superiorsteed.speedlevel";

    private SuperiorHorseEntity nmsEntity;
    private Horse bukkitEntity;
    
    private Stat hunger;
    private Stat hydration;
    private Stat trust;
    private Stat friendliness;
    private Stat comfortability;
    private Stat waterBravery;

    private boolean isMale;
    private boolean isStallion;

    private HashSet<Trait> traits;
    private Seed favoriteSeed;

    private SpeedLevel speedLevel = SpeedLevel.CANTER;

    public SuperiorHorse(Horse horse) {
        Location spawnLocation = horse.getLocation();

        nmsEntity = new SuperiorHorseEntity(horse, this);
        bukkitEntity = ((CraftWorld) spawnLocation.getWorld()).addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        bukkitEntity.setRotation(spawnLocation.getYaw(), spawnLocation.getPitch());
        bukkitEntity.teleport(spawnLocation, TeleportCause.PLUGIN);
        bukkitEntity.setAI(horse.hasAI());
        if (horse.isAdult()) {
            bukkitEntity.setAdult();
        }
        else {
            bukkitEntity.setBaby();
        }
        
        bukkitEntity.setDomestication(horse.getDomestication());
        bukkitEntity.setMaxDomestication(horse.getMaxDomestication());
        bukkitEntity.setEatingHaystack(horse.isEatingHaystack());
        HorseInventory inventory = bukkitEntity.getInventory();
        inventory.setSaddle(horse.getInventory().getSaddle());
        inventory.setArmor(horse.getInventory().getArmor());

        bukkitEntity.setBreedCause(horse.getBreedCause());
        bukkitEntity.setLoveModeTicks(horse.getLoveModeTicks());
        bukkitEntity.setBreed(horse.canBreed());
        bukkitEntity.setAgeLock(true);
        bukkitEntity.setAbsorptionAmount(horse.getAbsorptionAmount());;

        // Copying max health is necessary here to prevent setHealth from erroring if the health is greater than the max health.
        bukkitEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        bukkitEntity.setHealth(horse.getHealth());

        boolean isPlayerRidden = false;
        for (Entity passenger : horse.getPassengers()) {
            passenger.leaveVehicle();
            bukkitEntity.addPassenger(passenger);

            if (passenger instanceof Player) {
                isPlayerRidden = true;
            }
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

        // copy attribute modifiers
        for (Attribute attribute : Attribute.values()) {
            AttributeInstance attributeInstance = horse.getAttribute(attribute);
            if (attributeInstance != null) {
                for (AttributeModifier modifier : attributeInstance.getModifiers()) {
                    
                    // skip any potion effect modifiers since we've already copied them
                    if (modifier.getName().startsWith("effect.minecraft")) {
                        continue;
                    }

                    else if (modifier.getName().equals(SPEED_LEVEL_KEY)) {
                        // If a player is not riding the horse, skip the speed level modifier.
                        if (!isPlayerRidden) {
                            continue;
                        }
                        else {
                            // If the player is riding the horse, copy the speed level modifier and speed level
                            speedLevel = SpeedLevel.getFromScalar(modifier.getAmount());
                        }
                    }

                    bukkitEntity.getAttribute(attribute).addModifier(modifier);
                }
            }
        }
        
        // Saving data
        PersistentDataContainer container = horse.getPersistentDataContainer();
        SuperiorHorseInfo generatedInfo = new SuperiorHorseInfo();
        
        double hunger = containerValueOrDefault(container, "hunger", generatedInfo.getHunger());
        double hydration = containerValueOrDefault(container, "hydration", generatedInfo.getHydration());
        double trust = containerValueOrDefault(container, "trust", generatedInfo.getTrust());
        double friendliness = containerValueOrDefault(container, "friendliness", generatedInfo.getFriendliness());
        double comfortability = containerValueOrDefault(container, "comfortability", generatedInfo.getComfortability());
        double waterBravery = containerValueOrDefault(container, "waterBravery", generatedInfo.getWaterBravery());

        boolean isMale = containerValueOrDefault(container, "isMale", generatedInfo.isMale());
        boolean isStallion = containerValueOrDefault(container, "isStallion", generatedInfo.isStallion());

        HashSet<Trait> traits = new HashSet<Trait>();
        for (Trait trait : containerValueOrDefault(container, "traits", generatedInfo.getTraits())) {
            traits.add(trait);
        }
        int favoriteSeedId = containerValueOrDefault(container, "favoriteSeed", generatedInfo.getFavoriteSeed() != null ? generatedInfo.getFavoriteSeed().getId() : -1);

        SuperiorHorseInfo horseInfo = new SuperiorHorseInfo();
        horseInfo.setHunger(hunger);
        horseInfo.setHydration(hydration);
        horseInfo.setTrust(trust);
        horseInfo.setFriendliness(friendliness);
        horseInfo.setComfortability(comfortability);
        horseInfo.setWaterBravery(waterBravery);
        
        horseInfo.setMale(isMale);
        horseInfo.setStallion(isStallion);

        horseInfo.setColor(horse.getColor());
        horseInfo.setStyle(horse.getStyle());

        horseInfo.setOwnerUuid(horse.getOwner() != null ? horse.getOwner().getUniqueId() : null);

        horseInfo.setSpeed(horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
        horseInfo.setJumpStrength(horse.getJumpStrength());
        horseInfo.setMaxHealth(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        horseInfo.setTraits(traits);
        horseInfo.setFavoriteSeed(Seed.getFromId(favoriteSeedId));
        
        update(horseInfo);
        horse.remove();
    }
    
    public SuperiorHorse(Location spawnLocation) {
        this(spawnLocation, new SuperiorHorseInfo());
    }
    
    public SuperiorHorse(Location spawnLocation, SuperiorHorseInfo horseInfo) {
        nmsEntity = new SuperiorHorseEntity(spawnLocation.getWorld(), this);
        bukkitEntity = ((CraftWorld) spawnLocation.getWorld()).addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        bukkitEntity.teleport(spawnLocation, TeleportCause.PLUGIN);

        update(horseInfo);
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

    private int containerValueOrDefault(PersistentDataContainer container, String keyName, int defaultValue) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        if (container.has(key, PersistentDataType.INTEGER)) {
            return container.get(key, PersistentDataType.INTEGER);
        }
        else {
            return defaultValue;
        }
    }

    public boolean containerValueOrDefault(PersistentDataContainer container, String keyName, boolean defaultValue) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        PersistentDataType_BOOLEAN type = new PersistentDataType_BOOLEAN();
        if (container.has(key, type)) {
            return container.get(key, type);
        }
        else {
            return defaultValue;
        }
    }

    public HashSet<Trait> containerValueOrDefault(PersistentDataContainer container, String keyName, HashSet<Trait> defaultValue) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        if (container.has(key, PersistentDataType.STRING)) {
            String[] traitStrings = container.get(key, PersistentDataType.STRING).split(",");

            HashSet<Trait> traits = new HashSet<Trait>();
            if (traitStrings[0].length() > 0) {
                for (String traitString : traitStrings) {
                    traits.add(Trait.valueOf(traitString));
                }
            }
            return traits;
        }
        else {
            return defaultValue;
        }
    }

    // bukkitEntity must be set before calling this method
    public void update(SuperiorHorseInfo horseInfo) {
        
        if (hunger == null) {
            SuperiorSteed plugin = SuperiorSteed.getInstance();
            PersistentDataContainer container = bukkitEntity.getPersistentDataContainer();
        
            hunger = new Stat(horseInfo.getHunger(), container, new NamespacedKey(plugin, "hunger"));
            hydration = new Stat(horseInfo.getHydration(), container, new NamespacedKey(plugin, "hydration"));
            trust = new Stat(horseInfo.getTrust(), container, new NamespacedKey(plugin, "trust"));
            friendliness = new Stat(horseInfo.getFriendliness(), container, new NamespacedKey(plugin, "friendliness"));
            comfortability = new Stat(horseInfo.getComfortability(), container, new NamespacedKey(plugin, "comfortability"));
            waterBravery = new Stat(horseInfo.getWaterBravery(), container, new NamespacedKey(plugin, "waterBravery"));
        }
        else {
            hunger.set(horseInfo.getHunger());
            hydration.set(horseInfo.getHydration());
            trust.set(horseInfo.getTrust());
            friendliness.set(horseInfo.getFriendliness());
            comfortability.set(horseInfo.getComfortability());
            waterBravery.set(horseInfo.getWaterBravery());
        }
        
        setMale(horseInfo.isMale());
        setStallion(horseInfo.isStallion());
        
        bukkitEntity.setColor(horseInfo.getColor());
        bukkitEntity.setStyle(horseInfo.getStyle());

        setOwner(horseInfo.getOwnerUuid() != null ? Bukkit.getOfflinePlayer(horseInfo.getOwnerUuid()) : null);
        
        bukkitEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(horseInfo.getSpeed());
        bukkitEntity.setJumpStrength(horseInfo.getJumpStrength());
        bukkitEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(horseInfo.getMaxHealth());
        
        setTraits(horseInfo.getTraits());
        setFavoriteSeed(horseInfo.getFavoriteSeed());
    }

    public boolean equals(SuperiorHorse other) {
        return nmsEntity.getUUID().equals(other.getNMSEntity().getUUID());
    }

    public boolean equals(Entity other) {
        return nmsEntity.getUUID().equals(other.getUniqueId());
    }

    public SuperiorHorseInfo getInfo() {
        SuperiorHorseInfo horseInfo = new SuperiorHorseInfo();

        horseInfo.setHunger(hunger.get());
        horseInfo.setHydration(hydration.get());
        horseInfo.setTrust(trust.get());
        horseInfo.setFriendliness(friendliness.get());
        horseInfo.setComfortability(comfortability.get());
        horseInfo.setWaterBravery(waterBravery.get());

        horseInfo.setMale(isMale);
        horseInfo.setStallion(isStallion);

        horseInfo.setColor(bukkitEntity.getColor());
        horseInfo.setStyle(bukkitEntity.getStyle());

        horseInfo.setOwnerUuid(bukkitEntity.getOwner() != null ? bukkitEntity.getOwner().getUniqueId() : null);

        horseInfo.setSpeed(bukkitEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
        horseInfo.setJumpStrength(bukkitEntity.getJumpStrength());
        horseInfo.setMaxHealth(bukkitEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        horseInfo.setTraits(getTraits());
        horseInfo.setFavoriteSeed(getFavoriteSeed());
        return horseInfo;
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

    public OfflinePlayer getOwner() {
        return bukkitEntity.getOwner() != null ? (OfflinePlayer) bukkitEntity.getOwner() : null;
    }

    public void setOwner(OfflinePlayer owner) {
        bukkitEntity.setOwner(owner);

        if (owner != null) {
            bukkitEntity.setTamed(true);
        }
        else {
            bukkitEntity.setTamed(false);
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

    public void setMale(boolean isMale) {
        this.isMale = isMale;
        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "isMale"), new PersistentDataType_BOOLEAN(), isMale);
    }

    public boolean isStallion() {
        return isStallion;
    }

    public void setStallion(boolean isStallion) {
        this.isStallion = isStallion;
        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "isStallion"), new PersistentDataType_BOOLEAN(), isStallion);
    }

    public HashSet<Trait> getTraits() {
        return traits;
    }

    public void setTraits(HashSet<Trait> traits) {
        this.traits = traits;
        String traitString = "";
        for (Trait trait : traits) {
            traitString += trait.name() + ",";
        }
        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "traits"), PersistentDataType.STRING, traitString);
    }

    public Seed getFavoriteSeed() {
        return favoriteSeed;
    }

    public void setFavoriteSeed(Seed favoriteSeed) {
        this.favoriteSeed = favoriteSeed;

        int seedId = favoriteSeed != null ? favoriteSeed.getId() : -1;
        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "favoriteSeed"), PersistentDataType.INTEGER, seedId);
    }

    public SuperiorHorseEntity getNMSEntity() {
        return nmsEntity;
    }

    public Horse getBukkitEntity() {
        return bukkitEntity;
    }

    public SpeedLevel getSpeedLevel() {
        return speedLevel;
    }
    
    public void setSpeedLevel(SpeedLevel speedLevel) {
        AttributeInstance attribute = bukkitEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
    
        // delete the modifier if it already exists
        for (AttributeModifier modifier : attribute.getModifiers()) {
            if (modifier.getName().equals(SPEED_LEVEL_KEY)) {
                attribute.removeModifier(modifier);
                break;
            }
        }

        this.speedLevel = speedLevel;
        attribute.addModifier(new AttributeModifier(SPEED_LEVEL_KEY, speedLevel.getScalar(), AttributeModifier.Operation.MULTIPLY_SCALAR_1));
    }

    public SpeedLevel cycleSpeedLevel() {
        setSpeedLevel(speedLevel.next());
        return speedLevel;
    }

    public void resetSpeedLevel() {
        setSpeedLevel(SpeedLevel.CANTER);
    }
}

