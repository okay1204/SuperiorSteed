package me.screescree.SuperiorSteed;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.screescree.SuperiorSteed.listeners.BrewingSeeds;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorsesManager;
import me.screescree.SuperiorSteed.superiorhorse.features.horsenofalldamage.HorseNoFallDamageListener;
import me.screescree.SuperiorSteed.superiorhorse.features.horseridepermissions.HorseRidePermissionsListener;
import me.screescree.SuperiorSteed.superiorhorse.features.horsespeedchanger.HorseSpeedChangerListener;
import me.screescree.SuperiorSteed.superiorhorse.features.truststatmanager.TrustStatManagerListener;

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
        
        // general listeners
        pm.registerEvents(new BrewingSeeds(), this);

        // superiorhorse features
        pm.registerEvents(new HorseSpeedChangerListener(), this);
        pm.registerEvents(new HorseNoFallDamageListener(), this);
        pm.registerEvents(new TrustStatManagerListener(), this);
        pm.registerEvents(new HorseRidePermissionsListener(), this);

        new LoopingTaskManager().start();
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
