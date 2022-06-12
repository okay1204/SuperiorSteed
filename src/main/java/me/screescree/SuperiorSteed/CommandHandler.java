package me.screescree.SuperiorSteed;

import org.bukkit.command.CommandExecutor;

import me.screescree.SuperiorSteed.Commands.HorseCache;
import me.screescree.SuperiorSteed.Commands.HorseStats;
import me.screescree.SuperiorSteed.Commands.ReadPersistentData;
import me.screescree.SuperiorSteed.Commands.SummonHorse;
import me.screescree.SuperiorSteed.Commands.UpdateHorse;

public class CommandHandler {
    private SuperiorSteed plugin;

    public CommandHandler(SuperiorSteed plugin) {
        this.plugin = plugin;
        
        setExecutor("horsestats", new HorseStats(plugin));
        setExecutor("summonhorse", new SummonHorse(plugin));
        setExecutor("updatehorse", new UpdateHorse(plugin));
        setExecutor("horsecache", new HorseCache(plugin));
        setExecutor("readpersistentdata", new ReadPersistentData(plugin));
    }

    private void setExecutor(String commandName, CommandExecutor executor) {
        plugin.getCommand(commandName).setExecutor(executor);
    }
}
