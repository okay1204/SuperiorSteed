package me.screescree.SuperiorSteed;

import org.bukkit.command.CommandExecutor;

import me.screescree.SuperiorSteed.commands.HorseCache;
import me.screescree.SuperiorSteed.commands.HorseStats;
import me.screescree.SuperiorSteed.commands.ReadPersistentData;
import me.screescree.SuperiorSteed.commands.SummonHorse;
import me.screescree.SuperiorSteed.commands.UpdateHorse;

public class CommandHandler {
    public CommandHandler() {
        setExecutor("horsestats", new HorseStats());
        setExecutor("summonhorse", new SummonHorse());
        setExecutor("updatehorse", new UpdateHorse());
        setExecutor("horsecache", new HorseCache());
        setExecutor("readpersistentdata", new ReadPersistentData());
    }

    private void setExecutor(String commandName, CommandExecutor executor) {
        SuperiorSteed.getInstance().getCommand(commandName).setExecutor(executor);
    }
}
