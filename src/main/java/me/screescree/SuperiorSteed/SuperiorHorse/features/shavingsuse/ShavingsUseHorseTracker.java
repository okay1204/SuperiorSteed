package me.screescree.SuperiorSteed.superiorhorse.features.shavingsuse;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.jeff_media.customblockdata.CustomBlockData;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class ShavingsUseHorseTracker implements LoopingTask<SuperiorHorse> {
    public static final NamespacedKey SHAVINGS_TIMER_KEY = new NamespacedKey(SuperiorSteed.getInstance(), "shavingUseTimer");

    @Override
    public int getIntervalTicks() {
        // run every 1 second
        return 20;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        // check if the horse is on top of yellow glazed terracotta
        Block blockUnderneath = superiorHorse.getBukkitEntity().getLocation().add(0, -1, 0).getBlock();
        
        if (blockUnderneath.getType() == Material.YELLOW_GLAZED_TERRACOTTA) {
            // if so, tick the internal timer of the block
            
            // get the block's PDC
            PersistentDataContainer container = new CustomBlockData(blockUnderneath, SuperiorSteed.getInstance());

            // if the timer key exists, grab the current timer value
            int timer = 0;

            if (container.has(SHAVINGS_TIMER_KEY, PersistentDataType.INTEGER)) {
                timer = container.get(SHAVINGS_TIMER_KEY, PersistentDataType.INTEGER);
            }
            
            // increment the timer
            timer++;

            // if the timer is greater than or equal to 10800 seconds (3 hours), replace the block with brown glazed terracotta and remove the timer key
            if (timer >= 10800) {
                blockUnderneath.setType(Material.BROWN_GLAZED_TERRACOTTA);
                container.remove(SHAVINGS_TIMER_KEY);
            }
            // otherwise, set the timer
            else {
                container.set(SHAVINGS_TIMER_KEY, PersistentDataType.INTEGER, timer);
            }
        }
    }
}