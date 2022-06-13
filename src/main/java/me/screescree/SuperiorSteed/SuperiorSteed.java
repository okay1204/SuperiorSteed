package me.screescree.SuperiorSteed;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorsesManager;

public class SuperiorSteed extends JavaPlugin
{
    private static SuperiorSteed instance;

    private CommandHandler commandHandler;
    private SuperiorHorsesManager superiorHorsesManager;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();
        
        commandHandler = new CommandHandler();
        
        // set up listeners
        PluginManager pm = getServer().getPluginManager();
        superiorHorsesManager = new SuperiorHorsesManager();
        pm.registerEvents(superiorHorsesManager, this);
        
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

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }
}
