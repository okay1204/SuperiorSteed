package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.SpigotTimings;
import org.bukkit.craftbukkit.v1_20_R2.attribute.CraftAttributeMap;
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
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SuperiorHorseEntity extends Horse {
    private final SuperiorHorse superiorHorseWrapper;
    private boolean isUsingConsumingGoal;

    // Reflection for removing eat grass default animation
    private Method moveTailMethod;
    private Field eatingCounterField;
    private Field noJumpDelayField;
    private Method updateFallFlyingMethod;
    private Method travelRiddenMethod;

    @SuppressWarnings("null")
    public SuperiorHorseEntity(org.bukkit.World world, SuperiorHorse superiorHorseWrapper) {
        super(EntityType.HORSE, ((CraftWorld) world).getHandle());
        this.superiorHorseWrapper = superiorHorseWrapper;

        AttributeMap attributeMap = new AttributeMap(
                createBaseHorseAttributes().add(Attributes.ATTACK_DAMAGE, 3.0).build());
        CraftAttributeMap craftAttributeMap = new CraftAttributeMap(attributeMap);
        try {
            Field attributes = LivingEntity.class.getDeclaredField("bO");
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
            // Reflection for removing eat grass default animation
            moveTailMethod = AbstractHorse.class.getDeclaredMethod("t");
            moveTailMethod.setAccessible(true);

            eatingCounterField = AbstractHorse.class.getDeclaredField("cC");
            eatingCounterField.setAccessible(true);

            noJumpDelayField = LivingEntity.class.getDeclaredField("ca");
            noJumpDelayField.setAccessible(true);

            updateFallFlyingMethod = LivingEntity.class.getDeclaredMethod("F");
            updateFallFlyingMethod.setAccessible(true);

            travelRiddenMethod = LivingEntity.class.getDeclaredMethod("c", Player.class, Vec3.class);
            travelRiddenMethod.setAccessible(true);
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
        this.goalSelector.addGoal(5,
                new TemptGoal(this, 1.25D, Ingredient
                        .of(new ItemLike[] { Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE }),
                        false));
        this.targetSelector.addGoal(0, new HurtByHorseGoal(this));
        this.targetSelector.addGoal(1, new AttackHorseGoal(this));
        this.targetSelector.addGoal(2, new AngryBusyBeeAttackGoal(this));
    }

    public double randomNextDouble(double min, double max) {
        return min + (max - min) * this.random.nextDouble();
    }

    @Override
    @SuppressWarnings("resource")
    public void aiStep() {
        try {
            if (this.random.nextInt(200) == 0) {
                moveTailMethod.invoke(this);
            }

            // SECTION all superclass stuff happens here
            if (noJumpDelayField.getInt(this) > 0) {
                noJumpDelayField.setInt(this, noJumpDelayField.getInt(this) - 1);
            }

            if (this.isControlledByLocalInstance()) {
                this.lerpSteps = 0;
                this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
            }

            if (this.lerpSteps > 0) {
                this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot,
                        this.lerpXRot);
                --this.lerpSteps;
            } else if (!this.isEffectiveAi()) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
            }

            if (this.lerpHeadSteps > 0) {
                this.lerpHeadRotationStep(this.lerpHeadSteps, this.lerpYHeadRot);
                --this.lerpHeadSteps;
            }

            Vec3 vec3d = this.getDeltaMovement();
            double d0 = vec3d.x;
            double d1 = vec3d.y;
            double d2 = vec3d.z;
            if (Math.abs(vec3d.x) < 0.003D) {
                d0 = 0.0D;
            }

            if (Math.abs(vec3d.y) < 0.003D) {
                d1 = 0.0D;
            }

            if (Math.abs(vec3d.z) < 0.003D) {
                d2 = 0.0D;
            }

            this.setDeltaMovement(d0, d1, d2);
            this.level().getProfiler().push("ai");
            SpigotTimings.timerEntityAI.startTiming();
            if (this.isImmobile()) {
                this.jumping = false;
                this.xxa = 0.0F;
                this.zza = 0.0F;
            } else if (this.isEffectiveAi()) {
                this.level().getProfiler().push("newAi");
                this.serverAiStep();
                this.level().getProfiler().pop();
            }

            SpigotTimings.timerEntityAI.stopTiming();
            this.level().getProfiler().pop();
            this.level().getProfiler().push("jump");
            if (this.jumping && this.isAffectedByFluids()) {
                double d3;
                if (this.isInLava()) {
                    d3 = this.getFluidHeight(FluidTags.LAVA);
                } else {
                    d3 = this.getFluidHeight(FluidTags.WATER);
                }

                boolean flag = this.isInWater() && d3 > 0.0D;
                double d4 = this.getFluidJumpThreshold();
                if (!flag || this.onGround() && !(d3 > d4)) {
                    if (this.isInLava() && (!this.onGround() || d3 > d4)) {
                        this.jumpInLiquid(FluidTags.LAVA);
                    } else if ((this.onGround() || flag && d3 <= d4) && noJumpDelayField.getInt(this) == 0) {
                        this.jumpFromGround();
                        noJumpDelayField.setInt(this, 10);
                    }
                } else {
                    this.jumpInLiquid(FluidTags.WATER);
                }
            } else {
                noJumpDelayField.setInt(this, 0);
            }

            this.level().getProfiler().pop();
            this.level().getProfiler().push("travel");
            this.xxa *= 0.98F;
            this.zza *= 0.98F;
            updateFallFlyingMethod.invoke(this);
            AABB axisalignedbb = this.getBoundingBox();
            Vec3 vec3d1 = new Vec3((double) this.xxa, (double) this.yya, (double) this.zza);
            if (this.hasEffect(MobEffects.SLOW_FALLING) || this.hasEffect(MobEffects.LEVITATION)) {
                this.resetFallDistance();
            }

            label103: {
                SpigotTimings.timerEntityAIMove.startTiming();
                LivingEntity entityliving = this.getControllingPassenger();
                if (entityliving instanceof Player) {
                    Player entityhuman = (Player) entityliving;
                    if (this.isAlive()) {
                        travelRiddenMethod.invoke(this, entityhuman, vec3d1);
                        break label103;
                    }
                }

                this.travel(vec3d1);
            }

            SpigotTimings.timerEntityAIMove.stopTiming();
            this.level().getProfiler().pop();
            this.level().getProfiler().push("freezing");
            if (!this.level().isClientSide && !this.isDeadOrDying()) {
                int i = this.getTicksFrozen();
                if (this.isInPowderSnow && this.canFreeze()) {
                    this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), i + 1));
                } else {
                    this.setTicksFrozen(Math.max(0, i - 2));
                }
            }

            this.removeFrost();
            this.tryAddFrost();
            if (!this.level().isClientSide && this.tickCount % 40 == 0 && this.isFullyFrozen() && this.canFreeze()) {
                this.hurt(this.damageSources().freeze(), 1.0F);
            }

            this.level().getProfiler().pop();
            this.level().getProfiler().push("push");
            if (this.autoSpinAttackTicks > 0) {
                --this.autoSpinAttackTicks;
                this.checkAutoSpinAttack(axisalignedbb, this.getBoundingBox());
            }

            SpigotTimings.timerEntityAICollision.startTiming();
            this.pushEntities();
            SpigotTimings.timerEntityAICollision.stopTiming();
            this.level().getProfiler().pop();
            if (!this.level().isClientSide && this.isSensitiveToWater() && this.isInWaterRainOrBubble()) {
                this.hurt(this.damageSources().drown(), 1.0F);
            }

            this.level().getProfiler().push("looting");
            if (!this.level().isClientSide && this.canPickUpLoot() && this.isAlive() && !this.dead
                    && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                Vec3i baseblockposition = this.getPickupReach();
                List<ItemEntity> list = this.level().getEntitiesOfClass(ItemEntity.class,
                        this.getBoundingBox().inflate((double) baseblockposition.getX(),
                                (double) baseblockposition.getY(), (double) baseblockposition.getZ()));
                Iterator<ItemEntity> iterator = list.iterator();

                while (iterator.hasNext()) {
                    ItemEntity entityitem = (ItemEntity) iterator.next();
                    if (!entityitem.isRemoved() && !entityitem.getItem().isEmpty() && !entityitem.hasPickUpDelay()
                            && this.wantsToPickUp(entityitem.getItem())) {
                        this.pickUpItem(entityitem);
                    }
                }
            }

            this.level().getProfiler().pop();

            if (!this.level().isClientSide && !this.ageLocked) {
                if (this.isAlive()) {
                    int i = this.getAge();
                    if (i < 0) {
                        ++i;
                        this.setAge(i);
                    } else if (i > 0) {
                        --i;
                        this.setAge(i);
                    }
                }
            } else if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D),
                            this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
                }

                --this.forcedAgeTimer;
            }

            if (this.getAge() != 0) {
                this.inLove = 0;
            }

            if (this.inLove > 0) {
                --this.inLove;
                if (this.inLove % 10 == 0) {
                    double d0_1 = this.random.nextGaussian() * 0.02D;
                    double d1_1 = this.random.nextGaussian() * 0.02D;
                    double d2_1 = this.random.nextGaussian() * 0.02D;
                    this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D,
                            this.getRandomZ(1.0D), d0_1, d1_1, d2_1);
                }
            }

            // !SECTION

            if (!this.level().isClientSide && this.isAlive()) {
                if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                    this.heal(1.0F, RegainReason.REGEN);
                }

                if (this.canEatGrass()) {
                    if (this.isEating()) {
                        eatingCounterField.set(this, eatingCounterField.getInt(this) + 1);

                        if (eatingCounterField.getInt(this) > 50) {
                            eatingCounterField.set(this, 0);
                            this.setEating(false);
                        }
                    }
                }

                this.followMommy();
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}