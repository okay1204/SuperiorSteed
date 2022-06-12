package me.screescree.SuperiorSteed.SuperiorHorse;

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

    SuperiorHorseInfo(double hunger, double hydration, double trust, double friendliness, double comfortability, double waterBravery, boolean isMale) {
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
            "hunger=" + hunger +
            ", trust=" + trust +
            ", friendliness=" + friendliness +
            ", comfortability=" + comfortability +
            ", waterBravery=" + waterBravery +
            ", isMale=" + isMale +
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

    public double getHydration() {
        return hydration;
    }

    public double getTrust() {
        return trust;
    }

    public double getFriendliness() {
        return friendliness;
    }

    public double getComfortability() {
        return comfortability;
    }

    public double getWaterBravery() {
        return waterBravery;
    }

    public boolean isMale() {
        return isMale;
    }
}
