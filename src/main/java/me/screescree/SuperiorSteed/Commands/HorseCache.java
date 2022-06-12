package me.screescree.SuperiorSteed.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;

public class HorseCache implements CommandExecutor {
    private SuperiorSteed plugin;

    public HorseCache(SuperiorSteed plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(Utils.colorize("&aHorse cache:"));
        String cacheString = plugin.getHorseManager().getCache().toString();
        if (cacheString.length() > 255) {
            cacheString = cacheString.substring(0, 255);
        }
        sender.sendMessage(plugin.getHorseManager().getCache().toString());
        sender.sendMessage("Length: " + plugin.getHorseManager().getCache().size());

        return true;
    }
}
