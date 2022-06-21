package me.screescree.SuperiorSteed.superiorhorse.info;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;

public enum Trait {
    LONER("Loner", false),
    PICKY_EATER("Picky Eater", false),
    HARD_KEEPER("Hard Keeper", false),
    BUSY_BEE("Busy Bee", false),
    ANGEL("Angel", true),
    FRIENDLY("Friendly", true),
    EXTROVERT("Extrovert", true);

    public static final Set<Trait> INCOMPATIBLE_TRAITS = new HashSet<>();
    static {
        INCOMPATIBLE_TRAITS.add(Trait.LONER);
        INCOMPATIBLE_TRAITS.add(Trait.FRIENDLY);
        INCOMPATIBLE_TRAITS.add(Trait.EXTROVERT);
    }

    private String formalName;
    private boolean positive;

    Trait(String formattedName, boolean positive) {
        this.formalName = formattedName;
        this.positive = positive;
    }

    public String getFormalName() {
        return (positive ? ChatColor.GREEN : ChatColor.RED) + formalName;
    }

    public boolean isPositive() {
        return positive;
    }

    public boolean isCompatible(Trait trait) {
        if (this == trait) {
            return true;
        }

        if (INCOMPATIBLE_TRAITS.contains(this) && INCOMPATIBLE_TRAITS.contains(trait)) {
            return false;
        }
        return true;
    }

    public boolean isCompatible(Collection<Trait> traits) {
        for (Trait trait : traits) {
            if (!isCompatible(trait)) {
                return false;
            }
        }
        return true;
    }
}
