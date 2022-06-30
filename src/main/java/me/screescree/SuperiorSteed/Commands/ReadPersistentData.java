package me.screescree.SuperiorSteed.commands;

import java.util.List;
import java.util.Set;

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
import me.screescree.SuperiorSteed.superiorhorse.persistenttype.PersistentDataType_BOOLEAN;
import me.screescree.SuperiorSteed.superiorhorse.persistenttype.PersistentDataType_INTEGER_SET;
import me.screescree.SuperiorSteed.utils.Format;
import me.screescree.SuperiorSteed.utils.RayTraceUtils;

public class ReadPersistentData extends CustomCommand implements TabCompleter {    
    private final List<String> PERSISTENT_TYPES = List.of("double", "integer", "long", "boolean", "string", "integer_set");

    public ReadPersistentData() {
        super("readpersistentdata");
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
        
        if (args.length < 2) {
            return false;
        }
        
        String type = args[0];
        if (!PERSISTENT_TYPES.contains(type)) {
            sender.sendMessage(Format.colorize("&cInvalid type."));
            return true;
        }
        
        String key = args[1];
        Horse targetHorse = RayTraceUtils.getRiddenOrLookedAtHorse(player);
        
        if (targetHorse == null) {
            sender.sendMessage(Format.colorize("&cYou must be looking at or riding a horse to use this command."));
            return true;
        }
        
        PersistentDataContainer container = targetHorse.getPersistentDataContainer();
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        
        if (type.equals("double")) {
            Double value = container.get(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE);
            sender.sendMessage(Format.colorize("&a" + key + ": &f" + (value != null ? value.toString() : "null")));
        }
        else if (type.equals("integer")) {
            Integer value = container.get(new NamespacedKey(plugin, key), PersistentDataType.INTEGER);
            sender.sendMessage(Format.colorize("&a" + key + ": &f" + (value != null ? value.toString() : "null")));
        }
        else if (type.equals("long")) {
            Long value = container.get(new NamespacedKey(plugin, key), PersistentDataType.LONG);
            sender.sendMessage(Format.colorize("&a" + key + ": &f" + (value != null ? value.toString() : "null")));
        }
        else if (type.equals("boolean")) {
            Boolean value = container.get(new NamespacedKey(plugin, key), new PersistentDataType_BOOLEAN());
            sender.sendMessage(Format.colorize("&a" + key + ": &f" + (value != null ? value.toString() : "null")));
        }
        else if (type.equals("string")) {
            String value = container.get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
            sender.sendMessage(Format.colorize("&a" + key + ": &f" + (value != null ? value : "null")));
        }
        else if (type.equals("integer_set")) {
            Set<Integer> value = container.get(new NamespacedKey(plugin, key), new PersistentDataType_INTEGER_SET());
            sender.sendMessage(Format.colorize("&a" + key + ": &f" + (value != null ? value.toString() : "null")));
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
