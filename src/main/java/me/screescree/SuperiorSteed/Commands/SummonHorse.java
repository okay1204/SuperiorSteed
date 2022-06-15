package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.horseeditor.HorseEditor;

public class SummonHorse extends CustomCommand {
    public SummonHorse() {
        super("summonhorse");
    }

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

        new HorseEditor(player, "Summon Horse", horseInfo -> {
            if (!player.hasPermission(getCommand().getPermission())) {
                player.sendMessage(getNoPermissionMessage());
                return;
            }
            plugin.getHorseManager().newSuperiorHorse(player.getLocation(), horseInfo);
            player.sendMessage(Utils.colorize("&aYou have summoned a new horse."));
        });
                
        return true;
    }
}
