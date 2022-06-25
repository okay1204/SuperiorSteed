package me.screescree.SuperiorSteed.superiorhorse.features.speedchanger;

import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.LoopingTask;

public class SendActionBarLoop implements LoopingTask<Player> {
    @Override
    public void runLoopingTask(Player player) {
        Funcs.sendHorseSpeedActionBar(player);
    }

    @Override
    public int getIntervalTicks() {
        return 20;
    }

}
