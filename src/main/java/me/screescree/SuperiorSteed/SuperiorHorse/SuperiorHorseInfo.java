package me.screescree.SuperiorSteed.superiorhorse;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;

import io.netty.util.internal.ThreadLocalRandom;

public class SuperiorHorseInfo {
    public static final List<String> STAT_NAMES = List.of("hunger", "hydration", "trust", "friendliness", "comfortability", "waterbravery");

    private double hunger;
    private double hydration;
    private double trust;
    private double friendliness;
    private double comfortability;
    private double waterBravery;
    
    private Color color;
    private Style style;

    private boolean isMale;

    public SuperiorHorseInfo() {
        color = getRandom(Color.class);
        style = getRandom(Style.class);
        hunger = 1.0;
        hydration = 1.0;
        trust = 0.5;
        friendliness = 0.3;
        comfortability = 0.2;
        waterBravery = 0.1;
        isMale = Math.random() < 0.5;
    }

    private static <E extends Enum<E>> E getRandom(Class<E> enumClass) {
        Random random = ThreadLocalRandom.current();
        return enumClass.getEnumConstants()[random.nextInt(enumClass.getEnumConstants().length)];
    }

    public static SuperiorHorseInfo startingTemplate() {
        SuperiorHorseInfo horseInfo = new SuperiorHorseInfo();
        horseInfo.setColor(Color.WHITE);
        horseInfo.setStyle(Style.NONE);
        horseInfo.setMale(true);
        return horseInfo;
    }

    public String toString() {
        return "SuperiorHorseInfo{" +
            "color=" + color +
            ", style=" + style +
            ", hunger=" + getHunger() +
            ", hydration=" + getHydration() +
            ", trust=" + getTrust() +
            ", friendliness=" + getFriendliness() +
            ", comfortability=" + getComfortability() +
            ", waterBravery=" + getWaterBravery() +
            ", isMale=" + isMale() +
            '}';
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
}
