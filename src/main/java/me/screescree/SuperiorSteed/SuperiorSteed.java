package me.screescree.SuperiorSteed;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorse;
import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorsesManager;

public class SuperiorSteed extends JavaPlugin
{
    private CommandHandler commandHandler;
    private SuperiorHorsesManager superiorHorsesManager;

    @Override
    public void onEnable() {
        commandHandler = new CommandHandler(this);
        
        SuperiorHorse.setPlugin(this);
        
        // set up listeners
        PluginManager pm = getServer().getPluginManager();
        superiorHorsesManager = new SuperiorHorsesManager(this);
        pm.registerEvents(superiorHorsesManager, this);
        
        getLogger().info("SuperiorSteed has been enabled!");        
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SuperiorSteed has been disabled!");
    }

    public SuperiorHorsesManager getHorseManager() {
        return superiorHorsesManager;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
