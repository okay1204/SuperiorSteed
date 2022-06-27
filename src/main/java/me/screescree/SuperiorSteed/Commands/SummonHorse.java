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
        
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        new HorseEditor(player, "Summon Horse", horseInfo -> {
            if (!player.hasPermission(getCommand().getPermission())) {
                player.sendMessage(getNoPermissionMessage());
                return;
            }
            plugin.getHorseManager().newSuperiorHorse(player.getLocation(), horseInfo);
            player.sendMessage(Format.colorize("&aYou have summoned a new horse."));
        });
                
        return true;
    }
}
