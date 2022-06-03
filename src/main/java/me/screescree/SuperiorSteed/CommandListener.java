package me.screescree.SuperiorSteed;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorse;

public class CommandListener implements CommandExecutor, TabCompleter {
    private SuperiorSteed plugin;

    public CommandListener(SuperiorSteed plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("summonhorse")) {
            Player player = (Player) sender;

            Utils.spawnEntity(new SuperiorHorse(player.getWorld()),  player.getLocation());
            plugin.getLogger().info("Summoned a horse!");
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        return new ArrayList<>();
    }
}
