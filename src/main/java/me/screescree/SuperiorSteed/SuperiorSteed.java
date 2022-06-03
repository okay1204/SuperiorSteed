package me.screescree.SuperiorSteed;

import java.util.Map;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorse;

public class SuperiorSteed extends JavaPlugin
{
    private Map<String, Map<String, Object>> commands;
    private CommandListener commandListener;

    @Override
    public void onEnable() {
        PluginDescriptionFile pdfFile = getDescription();
        commands = pdfFile.getCommands();
        
        
        commandListener = new CommandListener(this);
        for (Map.Entry<String, Map<String, Object>> command : commands.entrySet()) {
            getCommand(command.getKey()).setExecutor(commandListener);
        }
        
        SuperiorHorse.setPlugin(this);
        
        // set up listeners
        PluginManager pm = getServer().getPluginManager();
        
        getLogger().info("SuperiorSteed has been enabled!");        
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SuperiorSteed has been disabled!");
    }
}
