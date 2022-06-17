package me.screescree.SuperiorSteed.superiorhorse;

import org.bukkit.ChatColor;

public enum SpeedLevel {
    WALK(0.50 - 1, ChatColor.RED),
    TROT(0.75 - 1, ChatColor.GOLD),
    CANTER(1.0 - 1, ChatColor.YELLOW),
    GALLOP(1.25 - 1, ChatColor.GREEN);

    private double scalar;
    private ChatColor color;

    SpeedLevel(double scalar, ChatColor color) {
        this.scalar = scalar;
        this.color = color;
    }

    public double getScalar() {
        return scalar;
    }

    public SpeedLevel next() {
        int nextIndex = ordinal() + 1;

        if (nextIndex >= values().length) {
            nextIndex = 0;
        }

        return values()[nextIndex];
    }

    public String getName() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }

    public ChatColor getColor() {
        return color;
    }

    public static SpeedLevel getFromScalar(double scalar) {
        for (SpeedLevel level : values()) {
            if (level.getScalar() == scalar) {
                return level;
            }
        }

        return null;
    }
}
