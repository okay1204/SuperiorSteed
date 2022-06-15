package me.screescree.SuperiorSteed;

import me.screescree.SuperiorSteed.commands.HorseCache;
import me.screescree.SuperiorSteed.commands.HorseStats;
import me.screescree.SuperiorSteed.commands.ReadPersistentData;
import me.screescree.SuperiorSteed.commands.SummonHorse;
import me.screescree.SuperiorSteed.commands.UpdateHorse;

public class CommandHandler {
    public CommandHandler() {
        new HorseStats();
        new SummonHorse();
        new UpdateHorse();
        new HorseCache();
        new ReadPersistentData();
    }
}
