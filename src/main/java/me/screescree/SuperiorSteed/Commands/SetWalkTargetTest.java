package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.utils.Format;
import me.screescree.SuperiorSteed.utils.RayTraceUtils;

public class SetWalkTargetTest extends CustomCommand {
    public SetWalkTargetTest() {
        super("setwalktargettest");
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

        if (args.length < 3) {
            return false;
        }

        // convert all 3 args to ints
        int x, y, z;
        try {
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Format.colorize("&cX, Y, and Z must be integers."));
            return true;
        }


        Horse targetHorse = RayTraceUtils.getRiddenOrLookedAtHorse(player);
        if (targetHorse == null) {
            sender.sendMessage(Format.colorize("&cYou must be riding or looking at a horse."));
            return true;
        }
        // superiorHorse.setWalkTarget(x, y, z);
        sender.sendMessage(Format.colorize("&aSet walk target to " + x + ", " + y + ", " + z + "."));
        sender.sendMessage("this command most likely doesnt do anything");

        return true;
    }
}
