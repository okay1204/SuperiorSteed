package me.screescree.SuperiorSteed.SuperiorHorse;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;

public class SuperiorHorse extends Horse {
    public SuperiorHorse(World world) {
        super(EntityType.HORSE, ((CraftWorld) world).getHandle());
    }
}