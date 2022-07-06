package me.screescree.SuperiorSteed.superiorhorse.features.chunkloader;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import me.screescree.SuperiorSteed.SuperiorSteed;

public class UnloadUnwantedChunks {
    public static void run() {
        for (World world : Bukkit.getWorlds()) {
            Map<Plugin, Collection<Chunk>> chunkTickets = world.getPluginChunkTickets();
            for (Plugin plugin : chunkTickets.keySet()) {
                if (plugin.equals(SuperiorSteed.getInstance())) {
                    for (Chunk chunk : chunkTickets.get(plugin)) {
                        if (!SavedChunks.hasChunk(chunk)) {
                            chunk.removePluginChunkTicket(plugin);
                        }
                    }
                }
            }
        }

        SavedChunks.clearChunks();
    }
}
