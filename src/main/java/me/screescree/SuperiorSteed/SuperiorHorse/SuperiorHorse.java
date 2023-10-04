package me.screescree.SuperiorSteed.superiorhorse;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import io.netty.util.internal.ThreadLocalRandom;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import me.screescree.SuperiorSteed.superiorhorse.info.Seed;
import me.screescree.SuperiorSteed.superiorhorse.info.SpeedLevel;
import me.screescree.SuperiorSteed.superiorhorse.info.Stat;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;
import me.screescree.SuperiorSteed.superiorhorse.persistenttype.PersistentDataType_BOOLEAN;
import me.screescree.SuperiorSteed.superiorhorse.persistenttype.PersistentDataType_ENUM;
import me.screescree.SuperiorSteed.superiorhorse.persistenttype.PersistentDataType_ENUM_SET;
import me.screescree.SuperiorSteed.superiorhorse.persistenttype.PersistentDataType_HORSE_INFO;
import me.screescree.SuperiorSteed.superiorhorse.persistenttype.PersistentDataType_INTEGER_SET;
import me.screescree.SuperiorSteed.utils.RandomUtil;

public class SuperiorHorse {
    private static final String SPEED_LEVEL_KEY = "superiorsteed.speedlevel";
    private static final String WATER_BRAVERY_KEY = "superiorsteed.waterbravery";
    private static final String SLOW_THIRST_KEY = "superiorsteed.slowthirst";

    public static final int GROOMING_ITEM_AMOUNT = 5;

    private double lastWaterBraveryMultiplier;

    private SuperiorHorseEntity nmsEntity;
    private Horse bukkitEntity;
    
    private Stat hunger;
    private Stat hydration;
    private Stat trust;
    private Stat friendliness;
    private Stat comfortability;
    private Stat waterBravery;

    private long age;

    private Set<Integer> groomedBy = new HashSet<>();
    private int groomExpireTimer = 0;

    private boolean isMale;
    private boolean isStallion;

    private Set<Trait> traits;
    private Seed favoriteSeed;

    private SpeedLevel speedLevel = SpeedLevel.CANTER;

    private int pregnancyTimer = 0;
    private Stat pregnancyComplication;
    private SuperiorHorseInfo pregnantWith = null;

    private long lastRidden;

    private boolean isMadAtPlayer = false;
    private boolean isMadAtHorse = false;
    private boolean isAttackingBack = false;
    private double attackedByHorseTimer = 0;

    private static final double SLOW_THIRST_MULTIPLIER = 0.5;
    private boolean isSlowThirsty = false;
    
    private long stomachHurtDelay = (long) (ThreadLocalRandom.current().nextDouble(3) * 20);

    public SuperiorHorse(Horse horse) {
        Location spawnLocation = horse.getLocation();

        nmsEntity = new SuperiorHorseEntity(horse, this);
        bukkitEntity = ((CraftWorld) spawnLocation.getWorld()).addEntity(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        bukkitEntity.setRotation(spawnLocation.getYaw(), spawnLocation.getPitch());
        bukkitEntity.teleport(spawnLocation, TeleportCause.PLUGIN);
        bukkitEntity.setAI(horse.hasAI());
        
        bukkitEntity.setDomestication(horse.getDomestication());
        bukkitEntity.setMaxDomestication(horse.getMaxDomestication());
        bukkitEntity.setEatingHaystack(horse.isEatingHaystack());
        HorseInventory inventory = bukkitEntity.getInventory();
        inventory.setSaddle(horse.getInventory().getSaddle());
        inventory.setArmor(horse.getInventory().getArmor());

        bukkitEntity.setBreedCause(horse.getBreedCause());
        bukkitEntity.setLoveModeTicks(horse.getLoveModeTicks());
        bukkitEntity.setBreed(horse.canBreed());
        bukkitEntity.setAgeLock(false);
        bukkitEntity.setAbsorptionAmount(horse.getAbsorptionAmount());

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
            // remove the "Random spawn bonus" modifier, from bukkitEntity because it spawns with it naturally
            AttributeInstance naturalAttributeInstance = bukkitEntity.getAttribute(attribute);

            if (naturalAttributeInstance != null) {
                for (AttributeModifier modifier : naturalAttributeInstance.getModifiers()) {
                    if (modifier.getName().equals("Random spawn bonus")) {
                        naturalAttributeInstance.removeModifier(modifier);
                    }
                }
            }

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

                    else if (modifier.getName().equals(SLOW_THIRST_KEY)) {
                        isSlowThirsty = true;
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

        long age = containerValueOrDefault(container, "age", generatedInfo.getAge());
        
        Set<Integer> groomedBy = containerIntegersValueOrDefault(container, "groomedBy", generatedInfo.getGroomedBy());
        int groomExpireTimer = containerValueOrDefault(container, "groomExpireTimer", generatedInfo.getGroomExpireTimer());

        boolean isMale = containerValueOrDefault(container, "isMale", generatedInfo.isMale());
        boolean isStallion = containerValueOrDefault(container, "isStallion", generatedInfo.isStallion());

        Set<Trait> traits = containerTraitsValueOrDefault(container, "traits", generatedInfo.getTraits());

        Seed favoriteSeed = containerValueOrDefault(container, "favoriteSeed", generatedInfo.getFavoriteSeed(), Seed.class);

        int pregnancyTimer = containerValueOrDefault(container, "pregnancyTimer", generatedInfo.getPregnancyTimer());
        double pregnancyComplication = containerValueOrDefault(container, "pregnancyComplication", generatedInfo.getPregnancyComplication());
        SuperiorHorseInfo pregnantWith = containerValueOrDefault(container, "pregnantWith", generatedInfo.getPregnantWith());

        long lastRidden = containerValueOrDefault(container, "lastRidden", generatedInfo.getLastRidden());

        SuperiorHorseInfo horseInfo = new SuperiorHorseInfo();
        horseInfo.setHunger(hunger);
        horseInfo.setHydration(hydration);
        horseInfo.setTrust(trust);
        horseInfo.setFriendliness(friendliness);
        horseInfo.setComfortability(comfortability);
        horseInfo.setWaterBravery(waterBravery);

        horseInfo.setAge(age);

        horseInfo.setGroomedBy(groomedBy);
        horseInfo.setGroomExpireTimer(groomExpireTimer);
        
        horseInfo.setMale(isMale);
        horseInfo.setStallion(isStallion);

        horseInfo.setColor(horse.getColor());
        horseInfo.setStyle(horse.getStyle());

        horseInfo.setOwnerUuid(horse.getOwner() != null ? horse.getOwner().getUniqueId() : null);

        horseInfo.setSpeed(horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
        horseInfo.setJumpStrength(horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue());
        horseInfo.setMaxHealth(horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        horseInfo.setTraits(traits);
        horseInfo.setFavoriteSeed(favoriteSeed);

        horseInfo.setPregnancyTimer(pregnancyTimer);
        horseInfo.setPregnancyComplication(pregnancyComplication);
        horseInfo.setPregnantWith(pregnantWith);

        horseInfo.setLastRidden(lastRidden);
        
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
        bukkitEntity.setAgeLock(false);

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

    private long containerValueOrDefault(PersistentDataContainer container, String keyName, long defaultValue) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        if (container.has(key, PersistentDataType.LONG)) {
            return container.get(key, PersistentDataType.LONG);
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

    public <E extends Enum<E>> E containerValueOrDefault(PersistentDataContainer container, String keyName, E defaultValue, Class<E> enumClass) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        PersistentDataType_ENUM<E> type = new PersistentDataType_ENUM<>(enumClass);
        if (container.has(key, type)) {
            return container.get(key, type);
        }
        else {
            return defaultValue;
        }
    }

    public Set<Integer> containerIntegersValueOrDefault(PersistentDataContainer container, String keyName, Set<Integer> defaultValue) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        PersistentDataType_INTEGER_SET type = new PersistentDataType_INTEGER_SET();
        if (container.has(key, type)) {
            return container.get(key, type);
        }
        else {
            return defaultValue;
        }
    }

    public Set<Trait> containerTraitsValueOrDefault(PersistentDataContainer container, String keyName, Set<Trait> defaultValue) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        PersistentDataType_ENUM_SET<Trait> type = new PersistentDataType_ENUM_SET<>(Trait.class);
        if (container.has(key, type)) {
            return container.get(key, type);
        }
        else {
            return defaultValue;
        }
    }

    public SuperiorHorseInfo containerValueOrDefault(PersistentDataContainer container, String keyName, SuperiorHorseInfo defaultValue) {
        NamespacedKey key = new NamespacedKey(SuperiorSteed.getInstance(), keyName);
        PersistentDataType_HORSE_INFO type = new PersistentDataType_HORSE_INFO();
        if (container.has(key, type)) {
            return container.get(key, type);
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
            
            pregnancyComplication = new Stat(horseInfo.getPregnancyComplication(), container, new NamespacedKey(plugin, "pregnancyComplication"));
        }
        else {
            hunger.set(horseInfo.getHunger());
            hydration.set(horseInfo.getHydration());
            trust.set(horseInfo.getTrust());
            friendliness.set(horseInfo.getFriendliness());
            comfortability.set(horseInfo.getComfortability());
            waterBravery.set(horseInfo.getWaterBravery());

            pregnancyComplication.set(horseInfo.getPregnancyComplication());
        }

        setAge(horseInfo.getAge());
        if (age >= SuperiorHorseInfo.AGE_ADULT) {
            bukkitEntity.setAdult();
        }
        else {
            bukkitEntity.setBaby();
        }

        // setGroomedBy(horseInfo.getGroomedBy());
        setGroomedBy(horseInfo.getGroomedBy());
        setGroomExpireTimer(horseInfo.getGroomExpireTimer());
        
        setMale(horseInfo.isMale());
        setStallion(horseInfo.isStallion());
        
        bukkitEntity.setColor(horseInfo.getColor());
        bukkitEntity.setStyle(horseInfo.getStyle());

        setOwner(horseInfo.getOwnerUuid() != null ? Bukkit.getOfflinePlayer(horseInfo.getOwnerUuid()) : null);
        
        bukkitEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(horseInfo.getSpeed());
        bukkitEntity.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(horseInfo.getJumpStrength());
        bukkitEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(horseInfo.getMaxHealth());
        
        setTraits(horseInfo.getTraits());
        setFavoriteSeed(horseInfo.getFavoriteSeed());
        
        setPregnancyTimer(horseInfo.getPregnancyTimer());
        setPregnantWith(horseInfo.getPregnantWith());

        setLastRidden(horseInfo.getLastRidden());
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

        horseInfo.setAge(age);

        horseInfo.setGroomedBy(groomedBy);
        horseInfo.setGroomExpireTimer(groomExpireTimer);

        horseInfo.setMale(isMale);
        horseInfo.setStallion(isStallion);

        horseInfo.setColor(bukkitEntity.getColor());
        horseInfo.setStyle(bukkitEntity.getStyle());

        horseInfo.setOwnerUuid(bukkitEntity.getOwner() != null ? bukkitEntity.getOwner().getUniqueId() : null);

        horseInfo.setSpeed(bukkitEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
        horseInfo.setJumpStrength(bukkitEntity.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue());
        horseInfo.setMaxHealth(bukkitEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        horseInfo.setTraits(getTraits());
        horseInfo.setFavoriteSeed(getFavoriteSeed());

        horseInfo.setPregnancyTimer(getPregnancyTimer());
        horseInfo.setPregnancyComplication(pregnancyComplication.get());
        horseInfo.setPregnantWith(getPregnantWith());

        horseInfo.setLastRidden(getLastRidden());
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
    
    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "age"), PersistentDataType.LONG, age);
        
        if (age >= SuperiorHorseInfo.AGE_ADULT) {
            bukkitEntity.setAdult();
        }
        else {
            bukkitEntity.setBaby();
        }
    }

    public void incrementAge() {
        setAge(age + 1);
    }

    public Set<Integer> getGroomedBy() {
        Set<Integer> copy = new HashSet<>();
        copy.addAll(groomedBy);
        return copy;
    }

    public void setGroomedBy(Set<Integer> groomedBy) {
        this.groomedBy = groomedBy;

        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "groomedBy"), new PersistentDataType_INTEGER_SET(), groomedBy);
    }

    public boolean verifyGrooming() {
        if (groomedBy.size() >= GROOMING_ITEM_AMOUNT) {
            setGroomedBy(new HashSet<Integer>());
            // one day later, the horse will need to be groomed again
            setGroomExpireTimer(1728000);
            return true;
        }
        return false;
    }

    public int getGroomExpireTimer() {
        return groomExpireTimer;
    }

    public void setGroomExpireTimer(int groomExpireTimer) {
        this.groomExpireTimer = groomExpireTimer;

        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "groomExpireTimer"), PersistentDataType.INTEGER, groomExpireTimer);
    }

    public void decrementGroomExpireTimer() {
        if (groomExpireTimer > 0) {
            setGroomExpireTimer(groomExpireTimer - 1);
        }
    }

    public boolean isFinishedGrooming() {
        return groomExpireTimer > 0;
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

    public Set<Trait> getTraits() {
        return traits;
    }

    public void setTraits(Set<Trait> traits) {
        this.traits = traits;

        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "traits"), new PersistentDataType_ENUM_SET<Trait>(Trait.class), traits);
    }

    public Seed getFavoriteSeed() {
        return favoriteSeed;
    }

    public void setFavoriteSeed(Seed favoriteSeed) {
        this.favoriteSeed = favoriteSeed;

        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "favoriteSeed"), new PersistentDataType_ENUM<Seed>(Seed.class), favoriteSeed);
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

    public int getPregnancyTimer() {
        return pregnancyTimer;
    }

    public void setPregnancyTimer(int pregnancyTimer) {
        this.pregnancyTimer = pregnancyTimer;

        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "pregnancyTimer"), PersistentDataType.INTEGER, pregnancyTimer);
    }

    public Stat pregnancyComplicationStat() {
        return pregnancyComplication;
    }

    public SuperiorHorseInfo getPregnantWith() {
        return pregnantWith;
    }

    public void setPregnantWith(SuperiorHorseInfo pregnantWith) {
        this.pregnantWith = pregnantWith;
        PersistentDataContainer container = bukkitEntity.getPersistentDataContainer();

        if (pregnantWith != null) {
            container.set(new NamespacedKey(SuperiorSteed.getInstance(), "pregnantWith"), new PersistentDataType_HORSE_INFO(), pregnantWith);
        }
        else if (container.has(new NamespacedKey(SuperiorSteed.getInstance(), "pregnantWith"), new PersistentDataType_HORSE_INFO())) {
            container.remove(new NamespacedKey(SuperiorSteed.getInstance(), "pregnantWith"));
        }
    }

    public void setWaterBraveryFactor(double multiplier) {
        if (lastWaterBraveryMultiplier == multiplier) {
            return;
        }

        Set<AttributeInstance> attributes = new HashSet<>();

        attributes.add(bukkitEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED));
        attributes.add(bukkitEntity.getAttribute(Attribute.HORSE_JUMP_STRENGTH));
    
        for (AttributeInstance attribute : attributes) {
            for (AttributeModifier modifier : attribute.getModifiers()) {
                if (modifier.getName().equals(WATER_BRAVERY_KEY)) {
                    attribute.removeModifier(modifier);
                    break;
                }
            }
            attribute.addModifier(new AttributeModifier(WATER_BRAVERY_KEY, multiplier - 1, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }

        lastWaterBraveryMultiplier = multiplier;
    }

    public void setSlowThirst(boolean isSlowThirsty) {
        if (this.isSlowThirsty == isSlowThirsty) {
            return;
        }

        this.isSlowThirsty = isSlowThirsty;
        AttributeInstance movementSpeed = bukkitEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (isSlowThirsty) {
            movementSpeed.addModifier(new AttributeModifier(SLOW_THIRST_KEY, SLOW_THIRST_MULTIPLIER - 1, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }
        else {
            for (AttributeModifier modifier : movementSpeed.getModifiers()) {
                if (modifier.getName().equals(SLOW_THIRST_KEY)) {
                    movementSpeed.removeModifier(modifier);
                    break;
                }
            }
        }
    }

    public boolean isPregnant() {
        return pregnancyTimer > 0;
    }

    public void becomePregnant(SuperiorHorse maleHorse) {
        // 5184000 is 3 days in ticks
        setPregnancyTimer(5184000);
        pregnancyComplication.set(1.0);

        SuperiorHorseInfo pregnantWith = new SuperiorHorseInfo();

        Random random = ThreadLocalRandom.current();

        // small chance to generate a new color, or inherit one of parent's colors
        if (random.nextDouble() < 0.1111) {
            pregnantWith.setColor(RandomUtil.getRandomEnum(Color.class));
        }
        else {
            if (random.nextDouble() < 0.5) {
                pregnantWith.setColor(maleHorse.getBukkitEntity().getColor());
            }
            else {
                pregnantWith.setColor(bukkitEntity.getColor());
            }
        }

        // small chance to generate a new style, or inherit one of parent's styles
        if (random.nextDouble() < 0.2) {
            pregnantWith.setStyle(RandomUtil.getRandomEnum(Style.class));
        }
        else {
            if (random.nextDouble() < 0.5) {
                pregnantWith.setStyle(maleHorse.getBukkitEntity().getStyle());
            }
            else {
                pregnantWith.setStyle(bukkitEntity.getStyle());
            }
        }

        // stats will be averaged between this horse, the father, and a randomized set of stats
        pregnantWith.setHunger((hunger.get() + maleHorse.hungerStat().get() + random.nextDouble()) / 3);
        pregnantWith.setHydration((hydration.get() + maleHorse.hydrationStat().get() + random.nextDouble()) / 3);
        pregnantWith.setTrust((trust.get() + maleHorse.trustStat().get() + random.nextDouble()) / 3);
        pregnantWith.setFriendliness((friendliness.get() + maleHorse.friendlinessStat().get() + random.nextDouble()) / 3);
        pregnantWith.setComfortability((comfortability.get() + maleHorse.comfortabilityStat().get() + random.nextDouble()) / 3);
        pregnantWith.setWaterBravery((waterBravery.get() + maleHorse.waterBraveryStat().get() + random.nextDouble()) / 3);

        pregnantWith.setSpeed((
            bukkitEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() +
            maleHorse.getBukkitEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() +
            SuperiorHorseInfo.randomSpeed()
        ) / 3);

        pregnantWith.setJumpStrength((
            bukkitEntity.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue() +
            maleHorse.getBukkitEntity().getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue() +
            SuperiorHorseInfo.randomJumpStrength()
        ) / 3);

        pregnantWith.setMaxHealth((
            bukkitEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() +
            maleHorse.getBukkitEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() +
            SuperiorHorseInfo.randomMaxHealth()
        ) / 3);

        pregnantWith.setAge(0);
        pregnantWith.setOwnerUuid(getOwner() != null ? getOwner().getUniqueId() : null);
        
        // horse will have much higher chance of recieving traits from parents

        // shuffle values from Trait enum
        Trait[] shuffledTraits = Trait.values();
        for (int i = 0; i < shuffledTraits.length; i++) {
            int j = random.nextInt(shuffledTraits.length);
            Trait temp = shuffledTraits[i];
            shuffledTraits[i] = shuffledTraits[j];
            shuffledTraits[j] = temp;
        }

        Set<Trait> traits = new HashSet<>();
        Seed favoriteSeed = RandomUtil.getRandomEnum(Seed.class);

        for (Trait trait : shuffledTraits) {
            double chance = 0.05;
            if (getTraits().contains(trait)) {
                chance += 0.3;
            }
            if (maleHorse.getTraits().contains(trait)) {
                chance += 0.3;
            }

            if (random.nextDouble() < chance) {
                if (trait.isCompatible(traits)) {
                    traits.add(trait);
                }
            }
        }

        pregnantWith.setTraits(traits);
        pregnantWith.setFavoriteSeed(favoriteSeed);

        setPregnantWith(pregnantWith);
    }

    public long getLastRidden() {
        return lastRidden;
    }

    public void setLastRidden(long lastRidden) {
        this.lastRidden = lastRidden;

        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(SuperiorSteed.getInstance(), "lastRidden"), PersistentDataType.LONG, lastRidden);
    }

    public void incrementLastRidden() {
        setLastRidden(getLastRidden() + 1);
    }

    public boolean isMadAtPlayer() {
        return isMadAtPlayer;
    }

    public void setMadAtPlayer(boolean madAtPlayer) {
        this.isMadAtPlayer = madAtPlayer;
    }

    public boolean isMadAtHorse() {
        return isMadAtHorse;
    }

    public void setMadAtHorse(boolean madAtHorse) {
        this.isMadAtHorse = madAtHorse;
    }
    
    public boolean isAttackingBack() {
        return isAttackingBack;
    }

    public void setAttackingBack(boolean attackingBack) {
        this.isAttackingBack = attackingBack;
    }

    public double getAttackedByHorseTimer() {
        return attackedByHorseTimer;
    }

    public void setAttackedByHorseTimer(double attackedByHorseTimer) {
        this.attackedByHorseTimer = attackedByHorseTimer;
    }

    public void decrementAttackedByHorseTimer() {
        if (attackedByHorseTimer > 0) {
            attackedByHorseTimer--;
        }
    }

    public boolean isLeashedByPlayer() {
        return bukkitEntity.isLeashed() && bukkitEntity.getLeashHolder().getType() == EntityType.PLAYER;
    }

    public long getStomachHurtDelay() {
        return stomachHurtDelay;
    }
}

