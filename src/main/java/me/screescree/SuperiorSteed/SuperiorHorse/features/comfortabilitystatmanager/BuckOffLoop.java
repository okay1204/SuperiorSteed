package me.screescree.SuperiorSteed.superiorhorse.features.comfortabilitystatmanager;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import io.netty.util.internal.ThreadLocalRandom;
import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class BuckOffLoop implements LoopingTask<Player> {
    private static double buckChance(double comfortability) {
        return Math.max(0, (-0.00033333 * comfortability) + 0.0003);
    }

    @Override
    public void runLoopingTask(Player player) {
        if (!player.isInsideVehicle() || !(player.getVehicle() instanceof Horse) || !((Horse) player.getVehicle()).isTamed()) {
            return;
        }

        SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) player.getVehicle());

        // chance of the player being kicked off, depending on the comfortability stat
        double comfortability = superiorHorse.comfortabilityStat().get();

        if (ThreadLocalRandom.current().nextDouble() < buckChance(comfortability)) {
            superiorHorse.getBukkitEntity().eject();

            player.sendMessage(Utils.colorize("&c" + superiorHorse.getName(20) + " bucked you off. &7(Comfortability: " + (int) Math.ceil(comfortability * 100) + "%)"));
            superiorHorse.getNMSEntity().makeMad();
        }
    }

    @Override
    public int getIntervalTicks() {
        return 1;
    }    
}
