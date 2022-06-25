package me.screescree.SuperiorSteed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.features.comfortabilitystatmanager.BuckOffLoop;
import me.screescree.SuperiorSteed.superiorhorse.features.comfortabilitystatmanager.ComfortabilityWithPlayer;
import me.screescree.SuperiorSteed.superiorhorse.features.shavingsuse.ShavingsUseHorseTracker;
import me.screescree.SuperiorSteed.superiorhorse.features.speedchanger.SendActionBarLoop;
import me.screescree.SuperiorSteed.superiorhorse.features.traits.LonerHandler;

public class LoopingTaskManager {
    private ArrayList<LoopingTask<Player>> playerTasks = new ArrayList<>();
    private ArrayList<LoopingTask<SuperiorHorse>> horseTasks = new ArrayList<>();

    private static Map<Integer, HashSet<LoopingTask<Player>>> playerLoops = new HashMap<Integer, HashSet<LoopingTask<Player>>>();
    private static Map<Integer, HashSet<LoopingTask<SuperiorHorse>>> horseLoops = new HashMap<Integer, HashSet<LoopingTask<SuperiorHorse>>>();

    public LoopingTaskManager() {
        playerTasks.add(new SendActionBarLoop());
        playerTasks.add(new BuckOffLoop());
        
        horseTasks.add(new ComfortabilityWithPlayer());
        horseTasks.add(new ShavingsUseHorseTracker());
        horseTasks.add(new LonerHandler());
    }

    public void start() {
        // create a new task for each interval
        for (LoopingTask<Player> task : playerTasks) {
            // if hashset doesn't exist, create it
            if (!playerLoops.containsKey(task.getIntervalTicks())) {
                playerLoops.put(task.getIntervalTicks(), new HashSet<LoopingTask<Player>>());
            }

            // add the task to the hashset
            playerLoops.get(task.getIntervalTicks()).add(task);
        }

        for (LoopingTask<SuperiorHorse> task : horseTasks) {
            // if hashset doesn't exist, create it
            if (!horseLoops.containsKey(task.getIntervalTicks())) {
                horseLoops.put(task.getIntervalTicks(), new HashSet<LoopingTask<SuperiorHorse>>());
            }

            // add the task to the hashset
            horseLoops.get(task.getIntervalTicks()).add(task);
        }

        // start the tasks
        for (Map.Entry<Integer, HashSet<LoopingTask<Player>>> entry : playerLoops.entrySet()) {
            Bukkit.getScheduler().runTaskTimer(SuperiorSteed.getInstance(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (LoopingTask<Player> task : entry.getValue()) {
                        task.runLoopingTask(player);
                    }
                }
            }, entry.getKey(), entry.getKey());
        }

        for (Map.Entry<Integer, HashSet<LoopingTask<SuperiorHorse>>> entry : horseLoops.entrySet()) {
            Bukkit.getScheduler().runTaskTimer(SuperiorSteed.getInstance(), () -> {
                for (SuperiorHorse horse : SuperiorSteed.getInstance().getHorseManager().getCache()) {
                    for (LoopingTask<SuperiorHorse> task : entry.getValue()) {
                        task.runLoopingTask(horse);
                    }
                }
            }, entry.getKey(), entry.getKey());
        }
    }
}
