package me.screescree.SuperiorSteed.superiorhorse.info;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;

import io.netty.util.internal.ThreadLocalRandom;
import me.screescree.SuperiorSteed.utils.RandomUtil;

public class SuperiorHorseInfo {
    // Stats
    private double hunger = 1.0;
    private double hydration = 1.0;
    private double trust = 0.5;
    private double friendliness = 0.3;
    private double comfortability = 0.2;
    private double waterBravery = 0.1;

    // 5184000 is 72 hours- when a foal turns into an adult.
    public static final long AGE_ADULT = 5184000;
    // 311040000 is 6 months- when an adult is labelled as a senior.
    public static final long AGE_SENIOR = 311040000;
    private long age = AGE_ADULT;

    // Will have numbers from 1 to 5, each number signifying a different grooming item.
    private Set<Integer> groomedBy = new HashSet<>();
    private int groomExpireTimer = 0;
    
    private boolean isMale = ThreadLocalRandom.current().nextDouble() < 0.5;
    // Male horses can either be a stallion or a gelding
    private boolean isStallion = true;
    
    // Looks
    private Color color = RandomUtil.getRandomEnum(Color.class);;
    private Style style = RandomUtil.getRandomEnum(Style.class);

    // Owner
    private UUID ownerUuid = null;

    // Attributes
    private double speed;
    private double jumpStrength;
    private double maxHealth;

    // Traits
    private Set<Trait> traits = new HashSet<>();
    // Trait attributes
    private Seed favoriteSeed = RandomUtil.getRandomEnum(Seed.class);

    // Pregnancy
    private int pregnancyTimer = 0;
    private double pregnancyComplication = 1.0;
    private SuperiorHorseInfo pregnantWith = null;

    // Misc
    private long lastRidden = 0;

    public SuperiorHorseInfo() {
        Random random = ThreadLocalRandom.current();

        // These generators were copied from actual Minecraft code.
        speed = randomSpeed();
        jumpStrength = randomJumpStrength();
        maxHealth = randomMaxHealth();

        // shuffle values from Trait enum
        Trait[] shuffledTraits = Trait.values();
        for (int i = 0; i < shuffledTraits.length; i++) {
            int j = random.nextInt(shuffledTraits.length);
            Trait temp = shuffledTraits[i];
            shuffledTraits[i] = shuffledTraits[j];
            shuffledTraits[j] = temp;
        }

        // for each trait, 5% chance of adding it to the horse (if it's compatible with traits already on the horse)
        for (Trait trait : shuffledTraits) {
            if (random.nextDouble() < 0.05) {
                if (trait.isCompatible(traits)) {
                    traits.add(trait);
                }
            }
        }
    }

    public static SuperiorHorseInfo startingTemplate() {
        SuperiorHorseInfo horseInfo = new SuperiorHorseInfo();
        // Override all random values with starting values
        horseInfo.setMale(true);

        horseInfo.setColor(Color.WHITE);
        horseInfo.setStyle(Style.NONE);

        horseInfo.setSpeed(0.225);
        horseInfo.setJumpStrength(0.7);
        horseInfo.setMaxHealth(22.5);

        horseInfo.clearTraits();

        return horseInfo;
    }

    public static double randomSpeed() {
        Random random = ThreadLocalRandom.current();
        return (0.44999998807907104D + random.nextDouble() * 0.3D + random.nextDouble() * 0.3D + random.nextDouble() * 0.3D) * 0.25D;
    }

    public static double randomJumpStrength() {
        Random random = ThreadLocalRandom.current();
        return 0.4000000059604645D + random.nextDouble() * 0.2D + random.nextDouble() * 0.2D + random.nextDouble() * 0.2D;
    }

    public static double randomMaxHealth() {
        Random random = ThreadLocalRandom.current();
        return 15.0D + (double) random.nextInt(8) + (double) random.nextInt(9);
    }

    public double getHunger() {
        return hunger;
    }

    public void setHunger(double hunger) {
        this.hunger = hunger;
    }

    public double getHydration() {
        return hydration;
    }

    public void setHydration(double hydration) {
        this.hydration = hydration;
    }

    public double getTrust() {
        return trust;
    }

    public void setTrust(double trust) {
        this.trust = trust;
    }

    public double getFriendliness() {
        return friendliness;
    }

    public void setFriendliness(double friendliness) {
        this.friendliness = friendliness;
    }

    public double getComfortability() {
        return comfortability;
    }

    public void setComfortability(double comfortability) {
        this.comfortability = comfortability;
    }

    public double getWaterBravery() {
        return waterBravery;
    }

    public void setWaterBravery(double waterBravery) {
        this.waterBravery = waterBravery;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public Set<Integer> getGroomedBy() {
        return groomedBy;
    }

    public void setGroomedBy(Set<Integer> groomedBy) {
        this.groomedBy = groomedBy;
    }

    public int getGroomExpireTimer() {
        return groomExpireTimer;
    }

    public void setGroomExpireTimer(int groomExpireTimer) {
        this.groomExpireTimer = groomExpireTimer;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean isMale) {
        this.isMale = isMale;
    }

    public boolean isStallion() {
        return isStallion;
    }

    public void setStallion(boolean isStallion) {
        this.isStallion = isStallion;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getJumpStrength() {
        return jumpStrength;
    }

    public void setJumpStrength(double jumpStrength) {
        this.jumpStrength = jumpStrength;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Set<Trait> getTraits() {
        return traits;
    }

    public void setTraits(Set<Trait> traits) {
        this.traits = traits;
    }

    // Returns true if the trait was compatible with the horse's traits
    public boolean addTrait(Trait trait) {
        if (trait.isCompatible(traits)) {
            traits.add(trait);
            return true;
        }

        return false;
    }

    public void removeTrait(Trait trait) {
        traits.remove(trait);
    }

    public void clearTraits() {
        traits.clear();
    }

    public boolean hasTrait(Trait trait) {
        return traits.contains(trait);
    }

    public Seed getFavoriteSeed() {
        return favoriteSeed;
    }

    public void setFavoriteSeed(Seed favoriteSeed) {
        this.favoriteSeed = favoriteSeed;
    }

    public int getPregnancyTimer() {
        return pregnancyTimer;
    }

    public void setPregnancyTimer(int pregnancyTimer) {
        this.pregnancyTimer = pregnancyTimer;
    }

    public double getPregnancyComplication() {
        return pregnancyComplication;
    }

    public void setPregnancyComplication(double pregnancyComplication) {
        this.pregnancyComplication = pregnancyComplication;
    }

    public SuperiorHorseInfo getPregnantWith() {
        return pregnantWith;
    }

    public void setPregnantWith(SuperiorHorseInfo pregnantWith) {
        this.pregnantWith = pregnantWith;
    }

    public long getLastRidden() {
        return lastRidden;
    }

    public void setLastRidden(long lastRidden) {
        this.lastRidden = lastRidden;
    }
}
