package me.screescree.SuperiorSteed.superiorhorse.features.chunkloader;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Chunk;

import me.screescree.SuperiorSteed.SuperiorSteed;

public class SavedChunks {
    private static Set<Chunk> chunks = new HashSet<>();

    public static void addChunk(Chunk chunk) {
        chunks.add(chunk);
        chunk.addPluginChunkTicket(SuperiorSteed.getInstance());
    }

    public static boolean hasChunk(Chunk chunk) {
        return chunks.contains(chunk);
    }

    public static void clearChunks() {
        chunks.clear();
    }
}
