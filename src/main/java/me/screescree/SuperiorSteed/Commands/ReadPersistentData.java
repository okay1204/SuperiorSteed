package me.screescree.SuperiorSteed.commands;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.BooleanTagType;

public class ReadPersistentData extends CustomCommand implements TabCompleter {    
    private final List<String> PERSISTENT_TYPES = List.of("double", "boolean");

    public ReadPersistentData() {
        super("readpersistentdata");
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

        if (args.length < 2) {
            return false;
        }

        String type = args[0];
        if (!PERSISTENT_TYPES.contains(type)) {
            sender.sendMessage(Utils.colorize("&cInvalid type."));
            return true;
        }

        String key = args[1];
        Horse targetHorse = Utils.getRiddenOrLookedAtHorse(player);

        PersistentDataContainer container = targetHorse.getPersistentDataContainer();

        if (type.equals("double")) {
            Double value = container.get(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE);
            sender.sendMessage(Utils.colorize("&a" + key + ": &f" + (value != null ? value.toString() : "null")));
        }
        else if (type.equals("boolean")) {
            Boolean value = container.get(new NamespacedKey(plugin, key), new BooleanTagType());
            sender.sendMessage(Utils.colorize("&a" + key + ": &f" + (value != null ? value.toString() : "null")));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return PERSISTENT_TYPES;

        return List.of();
    }
}
