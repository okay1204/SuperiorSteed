package me.screescree.SuperiorSteed;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.screescree.SuperiorSteed.listeners.HorseSpeedChanger;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorsesManager;

public class SuperiorSteed extends JavaPlugin
{
    private static SuperiorSteed instance;

    private SuperiorHorsesManager superiorHorsesManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        new CommandHandler();
        
        // set up listeners
        PluginManager pm = getServer().getPluginManager();
        superiorHorsesManager = new SuperiorHorsesManager();
        pm.registerEvents(superiorHorsesManager, this);
        
        pm.registerEvents(new HorseSpeedChanger(), this);
        
        getLogger().info("SuperiorSteed has been enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SuperiorSteed has been disabled!");
    }

    public static SuperiorSteed getInstance() {
        return instance;
    }

    public SuperiorHorsesManager getHorseManager() {
        return superiorHorsesManager;
    }
}
