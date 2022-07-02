package me.screescree.SuperiorSteed;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.jeff_media.customblockdata.CustomBlockData;

import me.screescree.SuperiorSteed.listeners.BrewingSeeds;
import me.screescree.SuperiorSteed.listeners.HorseWarp;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorsesManager;
import me.screescree.SuperiorSteed.superiorhorse.features.friendliness.AttackHorse;
import me.screescree.SuperiorSteed.superiorhorse.features.grooming.GroomItemUse;
import me.screescree.SuperiorSteed.superiorhorse.features.horsenofalldamage.HorseNoFallDamageListener;
import me.screescree.SuperiorSteed.superiorhorse.features.pregnancy.NoRideWhenPregnant;
import me.screescree.SuperiorSteed.superiorhorse.features.ridepermissions.RidePermissionsListener;
import me.screescree.SuperiorSteed.superiorhorse.features.speedchanger.SpeedChangerListener;
import me.screescree.SuperiorSteed.superiorhorse.features.traits.BusyBeeCalmDownHandler;
import me.screescree.SuperiorSteed.superiorhorse.features.trust.NoLeadAndJump;
import me.screescree.SuperiorSteed.superiorhorse.features.waterbravery.HurtInWater;

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

        // custom block data
        CustomBlockData.registerListener(this);
        
        // set up listeners
        PluginManager pm = getServer().getPluginManager();
        superiorHorsesManager = new SuperiorHorsesManager();
        pm.registerEvents(superiorHorsesManager, this);
        
        // general listeners
        pm.registerEvents(new BrewingSeeds(), this);
        pm.registerEvents(new HorseWarp(), this);

        // superiorhorse features
        pm.registerEvents(new SpeedChangerListener(), this);
        pm.registerEvents(new HorseNoFallDamageListener(), this);
        pm.registerEvents(new NoLeadAndJump(), this);
        pm.registerEvents(new RidePermissionsListener(), this);
        pm.registerEvents(new HurtInWater(), this);
        pm.registerEvents(new GroomItemUse(), this);
        pm.registerEvents(new NoRideWhenPregnant(), this);
        pm.registerEvents(new BusyBeeCalmDownHandler(), this);
        pm.registerEvents(new AttackHorse(), this);

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
