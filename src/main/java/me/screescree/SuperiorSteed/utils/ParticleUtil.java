package me.screescree.SuperiorSteed.utils;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import io.netty.util.internal.ThreadLocalRandom;

public class ParticleUtil {
    public static void spawnParticles(Particle particle, World world, double x, double y, double z) {
        spawnParticles(particle, new Location(world, x, y, z));
    }

    public static void spawnParticles(Particle particle, Location location) {
        Random random = ThreadLocalRandom.current();
        for (int i = 0; i < 10; i++) {
            double randX = random.nextDouble(-1, 1);
            double randY = random.nextDouble(1, 2);
            double randZ = random.nextDouble(-1, 1);
            location.getWorld().spawnParticle(particle, location.clone().add(randX, randY, randZ), 1);
        }
    }
}
