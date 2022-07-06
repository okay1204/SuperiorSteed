package me.screescree.SuperiorSteed.superiorhorse.features.chunkloader;

import org.bukkit.Chunk;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class LoadChunksAroundHorse implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        return 1;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        Chunk currentChunk = superiorHorse.getBukkitEntity().getLocation().getChunk();
        int chunkRadius = SuperiorSteed.getInstance().getConfig().getInt("horseChunkDistance", 2) - 1;

        for (int x = -chunkRadius; x <= chunkRadius; x++) {
            for (int z = -chunkRadius; z <= chunkRadius; z++) {
                SavedChunks.addChunk(currentChunk.getWorld().getChunkAt(currentChunk.getX() + x, currentChunk.getZ() + z));
            }
        }
    }
}
