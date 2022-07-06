package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.lang.reflect.Field;
import java.util.Random;

import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.attribute.CraftAttributeMap;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.AngryBusyBeeAttackGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.AttackHorseGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.DrinkWaterGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.EatGrassGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.EatSeedsGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.FleeFromHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.FollowHorseParentGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.HorseBreedGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.goals.HurtByHorseGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
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
    private boolean isUsingConsumingGoal;
    
    public SuperiorHorseEntity(org.bukkit.World world, SuperiorHorse superiorHorseWrapper) {
        super(EntityType.HORSE, ((CraftWorld) world).getHandle());
        this.superiorHorseWrapper = superiorHorseWrapper;

        AttributeMap attributeMap = new AttributeMap(createBaseHorseAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).build());
        CraftAttributeMap craftAttributeMap = new CraftAttributeMap(attributeMap);
        try {
            Field attributes = LivingEntity.class.getDeclaredField("bQ");
            Field craftAttributes = LivingEntity.class.getDeclaredField("craftAttributes");
            attributes.setAccessible(true);
            attributes.set(this, attributeMap);
            craftAttributes.setAccessible(true);
            craftAttributes.set(this, craftAttributeMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100);
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

    public boolean isUsingConsumingGoal() {
        return isUsingConsumingGoal;
    }

    public void setUsingConsumingGoal(boolean isUsingConsumingGoal) {
        this.isUsingConsumingGoal = isUsingConsumingGoal;
    }

    @Override
    public void inactiveTick() {
        tick();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new FleeFromHorse(this, 10.0F, 1.5D, 2.0D));
        this.goalSelector.addGoal(3, new RunAroundLikeCrazyGoal(this, 1.2D));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(4, new HorseBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new DrinkWaterGoal(this, 1.2D));
        this.goalSelector.addGoal(7, new EatSeedsGoal(this, 1.2D));
        this.goalSelector.addGoal(8, new EatGrassGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new FollowHorseParentGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25D, Ingredient.of(new ItemLike[]{Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE}), false));
        this.targetSelector.addGoal(0, new HurtByHorseGoal(this));
        this.targetSelector.addGoal(1, new AttackHorseGoal(this));
        this.targetSelector.addGoal(2, new AngryBusyBeeAttackGoal(this));
    }
}