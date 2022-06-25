package me.screescree.SuperiorSteed.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.jeff_media.customblockdata.CustomBlockData;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.features.shavingsuse.ShavingsUseHorseTracker;

public class ShavingTimer extends CustomCommand {
    public ShavingTimer() {
        super("shavingtimer");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            sender.sendMessage(Utils.colorize("&cThis command can only be used by a player."));
            return true;
        }

        // get block player is looking at
        Block block = player.getTargetBlockExact(5);
        if (block == null) {
            sender.sendMessage(Utils.colorize("&cYou must be looking at a block."));
            return true;
        }
        else if (!(block.getType() == Material.YELLOW_GLAZED_TERRACOTTA)) {
            sender.sendMessage(Utils.colorize("&cYou must be looking at a yellow glazed terracotta."));
            return true;
        }

        // get block's PDC
        PersistentDataContainer container = new CustomBlockData(block, SuperiorSteed.getInstance());
        int seconds = 0;
        if (container.has(ShavingsUseHorseTracker.SHAVINGS_TIMER_KEY, PersistentDataType.INTEGER)) {
            seconds = container.get(ShavingsUseHorseTracker.SHAVINGS_TIMER_KEY, PersistentDataType.INTEGER);
        }

        // send message
        sender.sendMessage(Utils.colorize("&aThe current shaving timer for this block is &e" + seconds + "&a seconds."));

        return true;
    }
}
