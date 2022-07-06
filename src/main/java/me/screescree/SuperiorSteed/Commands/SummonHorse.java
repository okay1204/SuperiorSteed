package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;
import me.screescree.SuperiorSteed.utils.Format;

public class SummonHorse extends CustomCommand {
    public SummonHorse() {
        super("summonhorse");
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

        int quantity = 1;
        if (args.length >= 1) {
            try {
                quantity = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                sender.sendMessage(Format.colorize("&cThe quantity must be a positive integer."));
                return true;
            }

            if (quantity <= 0) {
                sender.sendMessage(Format.colorize("&cThe quantity must be a positive integer."));
                return true;
            }
        }
        final int finalQuantity = quantity;
        
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        new HorseEditor(player, "Summon Horse", horseInfo -> {
            if (!player.hasPermission(getCommand().getPermission())) {
                player.sendMessage(getNoPermissionMessage());
                return;
            }
            for (int i = 0; i < finalQuantity; i++) {
                plugin.getHorseManager().newSuperiorHorse(player.getLocation(), horseInfo);
            }
            player.sendMessage(Format.colorize("&aYou have summoned " + finalQuantity +  " new horse" + (finalQuantity > 1 ? "s" : "") + "."));
        });
                
        return true;
    }
}
