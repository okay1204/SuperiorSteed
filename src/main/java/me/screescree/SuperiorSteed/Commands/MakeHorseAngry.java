package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.utils.Format;
import me.screescree.SuperiorSteed.utils.RayTraceUtils;

public class MakeHorseAngry extends CustomCommand {
    public MakeHorseAngry() {
        super("makehorseangry");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            sender.sendMessage(Format.colorize("&cThis command can only be used by a player."));
            return true;
        }

        Horse targetHorse = RayTraceUtils.getRiddenOrLookedAtHorse(player);
        if (targetHorse == null) {
            sender.sendMessage(Format.colorize("&cYou must be riding or looking at a horse."));
            return true;
        }
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        SuperiorHorse superiorHorse = plugin.getHorseManager().getSuperiorHorse(targetHorse);

        superiorHorse.setMadAtHorse(true);
        sender.sendMessage(Format.colorize("&aYou have made &e" + superiorHorse.getName(20) + " &aangry."));
        return true;
    }
}
