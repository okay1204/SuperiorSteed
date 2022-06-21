package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;

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

        new HorseEditor(superiorHorse.getInfo(), player, "Update Horse", horseInfo -> {
            if (!player.hasPermission(getCommand().getPermission())) {
                player.sendMessage(getNoPermissionMessage());
                return;
            }

            player.sendMessage(Utils.colorize("&aYou have updated ") + superiorHorse.getName(20) + Utils.colorize("&a."));
            superiorHorse.update(horseInfo);
        });

        return true;
    }
}
