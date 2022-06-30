package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.util.Random;

import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.DrinkWaterGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.EatSeedsGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.HorseBreedGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SuperiorHorseEntity extends Horse {
    private final SuperiorHorse superiorHorseWrapper;
    
    public SuperiorHorseEntity(org.bukkit.World world, SuperiorHorse superiorHorseWrapper) {
        super(EntityType.HORSE, ((CraftWorld) world).getHandle());
        this.superiorHorseWrapper = superiorHorseWrapper;
    }

    public SuperiorHorseEntity(org.bukkit.entity.Horse horse, SuperiorHorse superiorHorseWrapper) {
        this(horse.getWorld(), superiorHorseWrapper);
    }

    public SuperiorHorse getWrapper() {
        return superiorHorseWrapper;
    }

    public Random getRandom() {
        return random;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new HorseBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new DrinkWaterGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new EatSeedsGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(new ItemLike[]{Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE}), false));
    }    
}