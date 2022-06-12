package me.screescree.SuperiorSteed.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;

public class SummonHorse implements CommandExecutor {
    private SuperiorSteed plugin;

    public SummonHorse(SuperiorSteed plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            sender.sendMessage(Utils.colorize("&cThis command can only be used by a player."));
            return true;
        }

        plugin.getHorseManager().newSuperiorHorse(player.getLocation());
        player.sendMessage(Utils.colorize("&aYou have summoned a new horse."));
                
        return true;
    }
}
