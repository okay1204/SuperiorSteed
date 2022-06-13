package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;

public class SummonHorse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SuperiorSteed plugin = SuperiorSteed.getInstance();

        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            sender.sendMessage(Utils.colorize("&cThis command can only be used by a player."));
            return true;
        }

        HorseEditor editor = new HorseEditor("Summon Horse");
        editor.show(player);

        // plugin.getHorseManager().newSuperiorHorse(player.getLocation());
        // player.sendMessage(Utils.colorize("&aYou have summoned a new horse."));
                
        return true;
    }
}
