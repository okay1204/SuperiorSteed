package me.screescree.SuperiorSteed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.superiorhorse.BirthInfo;
import me.screescree.SuperiorSteed.superiorhorse.Births;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorsesManager;
import me.screescree.SuperiorSteed.superiorhorse.features.ageing.NaturalAge;
import me.screescree.SuperiorSteed.superiorhorse.features.comfortability.BuckOffLoop;
import me.screescree.SuperiorSteed.superiorhorse.features.comfortability.ComfortabilityWithPlayer;
import me.screescree.SuperiorSteed.superiorhorse.features.friendliness.FriendlinessDecreaseTimer;
import me.screescree.SuperiorSteed.superiorhorse.features.friendliness.FriendlinessIncreaseIfPlayerRiding;
import me.screescree.SuperiorSteed.superiorhorse.features.grooming.GroomTimer;
import me.screescree.SuperiorSteed.superiorhorse.features.grooming.NotGroomedPenalty;
import me.screescree.SuperiorSteed.superiorhorse.features.lastridden.LastRiddenCounter;
import me.screescree.SuperiorSteed.superiorhorse.features.pregnancy.PregnancyTimer;
import me.screescree.SuperiorSteed.superiorhorse.features.shavingsuse.ShavingsUseHorseTracker;
import me.screescree.SuperiorSteed.superiorhorse.features.speedchanger.SendActionBarLoop;
import me.screescree.SuperiorSteed.superiorhorse.features.traits.AngelHandler;
import me.screescree.SuperiorSteed.superiorhorse.features.traits.ExtrovertHandler;
import me.screescree.SuperiorSteed.superiorhorse.features.traits.FriendlyHandler;
import me.screescree.SuperiorSteed.superiorhorse.features.traits.LonerHandler;
import me.screescree.SuperiorSteed.superiorhorse.features.trust.BabyLeashIncrease;
import me.screescree.SuperiorSteed.superiorhorse.features.trust.HungerHydrationModifier;
import me.screescree.SuperiorSteed.superiorhorse.features.waterbravery.WaterCheck;
import me.screescree.SuperiorSteed.superiorhorse.features.waterbravery.WaterScare;

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
        horseTasks.add(new AngelHandler());
        horseTasks.add(new FriendlyHandler());
        horseTasks.add(new ExtrovertHandler());
        horseTasks.add(new WaterCheck());
        horseTasks.add(new WaterScare());
        horseTasks.add(new NaturalAge());
        horseTasks.add(new GroomTimer());
        horseTasks.add(new NotGroomedPenalty());
        horseTasks.add(new FriendlinessIncreaseIfPlayerRiding());
        horseTasks.add(new FriendlinessDecreaseTimer());
        horseTasks.add(new BabyLeashIncrease());
        horseTasks.add(new HungerHydrationModifier());
        horseTasks.add(new PregnancyTimer());
        horseTasks.add(new LastRiddenCounter());
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
                SuperiorHorsesManager manager = SuperiorSteed.getInstance().getHorseManager();

                Iterator<SuperiorHorse> iterator = manager.getCache().iterator();

                while (iterator.hasNext()) {
                    SuperiorHorse horse = iterator.next();
                    
                    if (!manager.checkValidity(horse)) {
                        iterator.remove();
                        continue;
                    }

                    for (LoopingTask<SuperiorHorse> task : entry.getValue()) {
                        task.runLoopingTask(horse);
                    }
                }

                for (BirthInfo birthInfo : Births.get()) {
                    manager.newSuperiorHorse(birthInfo.getLocation(), birthInfo.getHorseInfo());
                }

                Births.clear();

            }, entry.getKey(), entry.getKey());
        }
    }
}
