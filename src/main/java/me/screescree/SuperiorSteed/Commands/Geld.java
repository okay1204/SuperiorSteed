package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class Geld extends CustomCommand {
    public Geld() {
        super("geld");
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
        
        Horse targetHorse = Utils.getRiddenOrLookedAtHorse(player);
        if (targetHorse == null) {
            sender.sendMessage(Utils.colorize("&cYou must be riding or looking at a horse."));
            return true;
        }
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        SuperiorHorse superiorHorse = plugin.getHorseManager().getSuperiorHorse(targetHorse);

        if (!superiorHorse.isMale() || !superiorHorse.isStallion()) {
            sender.sendMessage(Utils.colorize("&cThis horse is not a stallion."));
            return true;
        }

        superiorHorse.setStallion(false);
        sender.sendMessage(Utils.colorize("&aYou have gelded ") + superiorHorse.getName(20) + Utils.colorize("&a."));
        return true;
    }
}
