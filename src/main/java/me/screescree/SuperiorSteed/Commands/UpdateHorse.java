package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.utils.Format;
import me.screescree.SuperiorSteed.utils.RayTraceUtils;

public class UpdateHorse extends CustomCommand implements Listener {
    public UpdateHorse() {
        super("updatehorse");
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

        new HorseEditor(superiorHorse.getInfo(), player, "Update Horse", horseInfo -> {
            if (!player.hasPermission(getCommand().getPermission())) {
                player.sendMessage(getNoPermissionMessage());
                return;
            }

            player.sendMessage(Format.colorize("&aYou have updated ") + superiorHorse.getName(20) + Format.colorize("&a."));
            superiorHorse.update(horseInfo);
        });

        return true;
    }
}
