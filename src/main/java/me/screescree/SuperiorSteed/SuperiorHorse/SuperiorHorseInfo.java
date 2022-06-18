package me.screescree.SuperiorSteed.superiorhorse;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;

import io.netty.util.internal.ThreadLocalRandom;

public class SuperiorHorseInfo {
    public static final List<String> STAT_NAMES = List.of("hunger", "hydration", "trust", "friendliness", "comfortability", "waterbravery");
    
    // Stats
    private double hunger;
    private double hydration;
    private double trust;
    private double friendliness;
    private double comfortability;
    private double waterBravery;
    
    private boolean isMale;
    // Male horses can either be a stallion or a gelding
    private boolean isStallion;
    
    // Looks
    private Color color;
    private Style style;

    // Attributes
    private double speed;
    private double jumpStrength;
    private double maxHealth;

    public SuperiorHorseInfo() {
        hunger = 1.0;
        hydration = 1.0;
        trust = 0.5;
        friendliness = 0.3;
        comfortability = 0.2;
        waterBravery = 0.1;
        
        isMale = Math.random() < 0.5;
        isStallion = true;
        
        color = getRandomEnum(Color.class);
        style = getRandomEnum(Style.class);

        Random random = ThreadLocalRandom.current();

        // These generators were copied from actual Minecraft code.
        speed = (0.44999998807907104D + random.nextDouble() * 0.3D + random.nextDouble() * 0.3D + random.nextDouble() * 0.3D) * 0.25D;
        jumpStrength = 0.4000000059604645D + random.nextDouble() * 0.2D + random.nextDouble() * 0.2D + random.nextDouble() * 0.2D;
        maxHealth = 15.0F + (float) random.nextInt(8) + (float) random.nextInt(9);
    }

    private static <E extends Enum<E>> E getRandomEnum(Class<E> enumClass) {
        Random random = ThreadLocalRandom.current();
        return enumClass.getEnumConstants()[random.nextInt(enumClass.getEnumConstants().length)];
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
        return horseInfo;
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
}
