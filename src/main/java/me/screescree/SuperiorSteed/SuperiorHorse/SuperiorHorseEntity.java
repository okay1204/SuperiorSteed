package me.screescree.SuperiorSteed.superiorhorse;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Horse;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;

public class SuperiorHorseEntity extends net.minecraft.world.entity.animal.horse.Horse {
    public SuperiorHorseEntity(World world) {
        super(EntityType.HORSE, ((CraftWorld) world).getHandle());
    }

    public SuperiorHorseEntity(Horse horse) {
        super(EntityType.HORSE, ((CraftWorld) horse.getWorld()).getHandle());
    }

    public void pathfindToWater() {
        BlockPos blockPos = new BlockPos(getX() + 10, getY(), getZ());
        getNavigation().createPath(blockPos, 1);   
    }
}