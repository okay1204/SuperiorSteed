package me.screescree.SuperiorSteed;

import org.bukkit.command.CommandExecutor;

import me.screescree.SuperiorSteed.Commands.HorseCache;
import me.screescree.SuperiorSteed.Commands.HorseStats;
import me.screescree.SuperiorSteed.Commands.ReadPersistentData;
import me.screescree.SuperiorSteed.Commands.SummonHorse;
import me.screescree.SuperiorSteed.Commands.UpdateHorse;

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
