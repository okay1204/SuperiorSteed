package me.screescree.SuperiorSteed.commands;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.utils.Format;

public class GetChunkTickets extends CustomCommand {
    public GetChunkTickets() {
        super("getchunktickets");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage(Format.colorize("&aChunk tickets:"));

        for (World world : Bukkit.getWorlds()) {
            Map<Plugin, Collection<Chunk>> chunkTickets = world.getPluginChunkTickets();

            Collection<Chunk> chunks = null;
            for (Plugin plugin : chunkTickets.keySet()) {
                if (plugin.equals(SuperiorSteed.getInstance())) {
                    chunks = chunkTickets.get(plugin);
                    break;
                }
            }

            if (chunks == null) {
                continue;
            }

            sender.sendMessage(Format.colorize("&aWorld \"" + world.getName() + "\":"));
            for (Chunk chunk : chunks) {
                sender.sendMessage(Format.colorize("&e" + chunk.getX() + ", " + chunk.getZ()));
            }
        }
        return true;
    }
}
