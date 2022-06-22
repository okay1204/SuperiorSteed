package me.screescree.SuperiorSteed.superiorhorse.info;

import java.util.HashSet;

import org.bukkit.Material;

public enum Seed {
    WHEAT_SEEDS(Material.WHEAT_SEEDS, "wheat_seeds", 0),
    PUMPKIN_SEEDS(Material.PUMPKIN_SEEDS, "pumpkin_seeds", 1),
    MELON_SEEDS(Material.MELON_SEEDS, "melon_seeds", 2),
    BEETROOT_SEEDS(Material.BEETROOT_SEEDS, "beetroot_seeds", 3),
    NETHER_WART(Material.NETHER_WART, "nether_wart", 4);

    public static final HashSet<Material> MATERIALS = new HashSet<>();
    public static final HashSet<String> NAMES = new HashSet<>();
    public static final Seed ALWAYS_LIKED = BEETROOT_SEEDS;

    private final Material material;
    private final String name;
    private final int id;

    static {
        for (Seed seed : values()) {
            MATERIALS.add(seed.material);
            NAMES.add(seed.name);
        }
    }

    Seed(Material material, String name, int id) {
        this.material = material;
        this.name = name;
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static Seed getFromId(int id) {
        for (Seed seed : values()) {
            if (seed.getId() == id) {
                return seed;
            }
        }
        return null;
    }
}
