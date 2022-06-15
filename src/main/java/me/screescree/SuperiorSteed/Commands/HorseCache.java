package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;

public class HorseCache extends CustomCommand {
    public HorseCache() {
        super("horsecache");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        
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
