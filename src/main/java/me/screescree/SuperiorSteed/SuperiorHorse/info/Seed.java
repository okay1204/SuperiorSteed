package me.screescree.SuperiorSteed.superiorhorse.info;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;

public enum Seed {
    WHEAT_SEEDS(Material.WHEAT_SEEDS),
    PUMPKIN_SEEDS(Material.PUMPKIN_SEEDS),
    MELON_SEEDS(Material.MELON_SEEDS),
    BEETROOT_SEEDS(Material.BEETROOT_SEEDS),
    NETHER_WART(Material.NETHER_WART);

    public static final Seed ALWAYS_LIKED = BEETROOT_SEEDS;
    public static final Set<Material> MATERIALS = new HashSet<>();
    static {
        for (Seed seed : values()) {
            MATERIALS.add(seed.getMaterial());
        }
    }

    private final Material material;

    Seed(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
