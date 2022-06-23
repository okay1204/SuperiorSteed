package me.screescree.SuperiorSteed;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.screescree.SuperiorSteed.listeners.BrewingSeeds;
import me.screescree.SuperiorSteed.listeners.HorseNoFallDamage;
import me.screescree.SuperiorSteed.listeners.HorseSpeedChanger;
import me.screescree.SuperiorSteed.listeners.TrustStatManager;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorsesManager;

public class SuperiorSteed extends JavaPlugin
{
    private static SuperiorSteed instance;

    private SuperiorHorsesManager superiorHorsesManager;
    private Database database;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        database = new Database();
        new CommandHandler();
        
        // set up listeners
        PluginManager pm = getServer().getPluginManager();
        superiorHorsesManager = new SuperiorHorsesManager();
        pm.registerEvents(superiorHorsesManager, this);
        
        pm.registerEvents(new HorseSpeedChanger(), this);
        pm.registerEvents(new BrewingSeeds(), this);
        pm.registerEvents(new HorseNoFallDamage(), this);
        pm.registerEvents(new TrustStatManager(), this);
    }
    
    @Override
    public void onDisable() {
        database.safeDisconnect();
    }

    public static SuperiorSteed getInstance() {
        return instance;
    }

    public SuperiorHorsesManager getHorseManager() {
        return superiorHorsesManager;
    }

    public Database getDatabase() {
        return database;
    }
}
