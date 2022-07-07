package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.attribute.CraftAttributeMap;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class SuperiorHorseEntity extends Horse {
    private final SuperiorHorse superiorHorseWrapper;
    private boolean isUsingConsumingGoal;
    private Method moveTailMethod;
    private Field eatingCounterField;
    private Method animalAiStepMethod;
    
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

        try {
                moveTailMethod = AbstractHorse.class.getDeclaredMethod("fy");
                moveTailMethod.setAccessible(true);

                eatingCounterField = AbstractHorse.class.getDeclaredField("cw");
                eatingCounterField.setAccessible(true);

                animalAiStepMethod = Animal.class.getDeclaredMethod("w_");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        this.goalSelector.addGoal(6, new DrinkWaterGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new EatSeedsGoal(this, 1.0D));
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

    @Override
    public void aiStep() {
        if (this.random.nextInt(200) == 0) {
            try {
                moveTailMethod.invoke(this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            animalAiStepMethod.invoke(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (!this.level.isClientSide && this.isAlive()) {
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F, RegainReason.REGEN);
            }

            if (this.canEatGrass()) {
                if (!this.isEating() && !this.isVehicle() && this.random.nextInt(300) == 0 && this.level.getBlockState(this.blockPosition().below()).is(Blocks.GRASS_BLOCK)) {
                    this.setEating(true);
                }

                if (this.isEating()) {
                    try {
                        eatingCounterField.set(this, eatingCounterField.getInt(this) + 1);

                        if (eatingCounterField.getInt(this) > 50) {
                            eatingCounterField.set(this, 0);
                            this.setEating(false);
                        }
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            this.followMommy();
        }
    }
}