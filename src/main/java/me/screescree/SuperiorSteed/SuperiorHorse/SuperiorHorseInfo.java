package me.screescree.SuperiorSteed.SuperiorHorse;

import java.util.Map;
import static java.util.Map.entry;

public class SuperiorHorseInfo {
    public static final Map<String, Object> DEFAULT_MAP = Map.ofEntries(
        entry("hunger", 1.0),
        entry("trust", 0.5),
        entry("friendliness", 0.3),
        entry("comfortability", 0.2),
        entry("waterBravery", 0.1)
    );
    public static final SuperiorHorseInfo DEFAULT = new SuperiorHorseInfo(
        (double) DEFAULT_MAP.get("hunger"),
        (double) DEFAULT_MAP.get("trust"),
        (double) DEFAULT_MAP.get("friendliness"),
        (double) DEFAULT_MAP.get("comfortability"),
        (double) DEFAULT_MAP.get("waterBravery")
    );

    private double hunger;
    private double trust;
    private double friendliness;
    private double comfortability;
    private double waterBravery;

    SuperiorHorseInfo(double hunger, double trust, double friendliness, double comfortability, double waterBravery) {
        this.hunger = hunger;
        this.trust = trust;
        this.friendliness = friendliness;
        this.comfortability = comfortability;
        this.waterBravery = waterBravery;
    }

    public String toString() {
        return "SuperiorHorseInfo{" +
            "hunger=" + hunger +
            ", trust=" + trust +
            ", friendliness=" + friendliness +
            ", comfortability=" + comfortability +
            ", waterBravery=" + waterBravery +
            '}';
    }

    public double getHunger() {
        return hunger;
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
}
