package me.screescree.SuperiorSteed.superiorhorse;

import java.util.List;

public class SuperiorHorseInfo {
    public static final List<String> STAT_NAMES = List.of("hunger", "hydration", "trust", "friendliness", "comfortability", "waterbravery");

    private double hunger;
    private double hydration;
    private double trust;
    private double friendliness;
    private double comfortability;
    private double waterBravery;

    private boolean isMale;

    public SuperiorHorseInfo(double hunger, double hydration, double trust, double friendliness, double comfortability, double waterBravery, boolean isMale) {
        this.hunger = hunger;
        this.hydration = hydration;
        this.trust = trust;
        this.friendliness = friendliness;
        this.comfortability = comfortability;
        this.waterBravery = waterBravery;
        this.isMale = isMale;
    }

    public String toString() {
        return "SuperiorHorseInfo{" +
            "hunger=" + getHunger() +
            ", trust=" + getTrust() +
            ", friendliness=" + getFriendliness() +
            ", comfortability=" + getComfortability() +
            ", waterBravery=" + getWaterBravery() +
            ", isMale=" + isMale() +
            '}';
    }

    public static SuperiorHorseInfo generateNew() {
        return new SuperiorHorseInfo(
            1.0,
            1.0,
            0.5,
            0.3,
            0.2,
            0.1,
            Math.random() < 0.5
        );
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
