package dev.itsmeow.toadterror.entity;

import java.util.EnumSet;
import java.util.Optional;

import javax.annotation.Nullable;

import dev.itsmeow.toadterror.init.ModEntities;
import dev.itsmeow.toadterror.init.ModResources;
import dev.itsmeow.toadterror.init.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ToadSentinelEntity extends CreatureEntity {

    protected final ServerBossInfo bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_20);
    protected static final DataParameter<Integer> TONGUE_TICKS = EntityDataManager.createKey(ToadSentinelEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> ATTACK_TICK = EntityDataManager.createKey(ToadSentinelEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Optional<BlockPos>> TARGET_POS = EntityDataManager.createKey(ToadSentinelEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    public int hopProgress = 0;
    protected boolean inc = true;

    public ToadSentinelEntity(World worldIn) {
        super(ModEntities.TOAD_SENTINEL.entityType, worldIn);
        this.stepHeight = 1F;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new ToadSentinelJumpSlamAttackGoal(this));
        this.goalSelector.addGoal(2, new ToadSentinelRibbitAttackGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1D, true) {
            @Override
            public boolean shouldExecute() {
                LivingEntity t = ToadSentinelEntity.this.getAttackTarget();
                return super.shouldExecute() && t != null && ToadSentinelEntity.this.getTongueTicks() == 0;
            }

            @Override
            protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
                super.checkAndPerformAttack(enemy, distToEnemySqr);
                ToadSentinelEntity.this.dataManager.set(ATTACK_TICK, attackTick);
            }

            @Override
            protected double getAttackReachSqr(LivingEntity attackTarget) {
                return (double) (this.attacker.getWidth() * this.attacker.getWidth() + attackTarget.getWidth()) + 0.5D;
            }
        });
        this.goalSelector.addGoal(4, new ToadSentinelTongueAttackGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, false, false));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(2D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.TOAD_SENTINEL_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.TOAD_SENTINEL_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundEvents.TOAD_SENTINEL_HURT;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return ModResources.TOAD_SENTINEL_LOOT_TABLE;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(TONGUE_TICKS, 0);
        this.dataManager.register(TARGET_POS, Optional.empty());
        this.dataManager.register(ATTACK_TICK, 0);
    }

    public void setTongueTicks(int tick) {
        this.dataManager.set(TONGUE_TICKS, tick);
    }

    public int getTongueTicks() {
        return this.dataManager.get(TONGUE_TICKS);
    }

    public int getAttackTick() {
        return this.dataManager.get(ATTACK_TICK);
    }

    @OnlyIn(Dist.CLIENT)
    public Optional<BlockPos> getTargetPos() {
        return this.dataManager.get(TARGET_POS);
    }

    public void setTargetPos(Optional<BlockPos> pos) {
        this.dataManager.set(TARGET_POS, pos);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || (source instanceof IndirectEntityDamageSource && source.damageType.equals("arrow")) || source == DamageSource.FIREWORKS || source == DamageSource.FALL;
    }

    @Override
    public void tick() {
        super.tick();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if(!this.getEntityWorld().isRemote()) {
            if(this.getAttackTarget() != null) {
                this.setTargetPos(Optional.of(this.getAttackTarget().getPosition()));
            } else {
                this.setTargetPos(Optional.empty());
            }
        }
        if(this.getMotion().lengthSquared() > 0.01D && this.onGround) {
            if(inc) {
                hopProgress++;
                if(hopProgress == 10) {
                    inc = false;
                    hopProgress = 6;
                }
            } else {
                if(hopProgress == 1) {
                    inc = true;
                    hopProgress = 4;
                }
                hopProgress--;
            }
        } else {
            this.hopProgress = 0;
            inc = true;
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if(this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }

    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return 1.8F * this.getJumpFactor();
    }

    @Override
    public void setAttackTarget(LivingEntity newT) {
        LivingEntity old = this.getAttackTarget();
        if(old != null && old.isAlive() && !this.onGround && newT == null) {
            return;
        }
        super.setAttackTarget(newT);
    }

    public static class ToadSentinelTongueAttackGoal extends Goal {

        protected final ToadSentinelEntity toad;
        protected float startDistance;
        protected int cooldown = 60;
        protected boolean attackedThisCycle = false;

        public ToadSentinelTongueAttackGoal(ToadSentinelEntity toad) {
            this.toad = toad;
            this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() {
            if(cooldown > 0) {
                cooldown--;
            }
            LivingEntity target = toad.getAttackTarget();
            return target != null && target.isAlive() && cooldown == 0 && toad.getDistance(target) > 8D;
        }

        @Override
        public boolean shouldContinueExecuting() {
            LivingEntity target = toad.getAttackTarget();
            return target != null && target.isAlive() && toad.getTongueTicks() > 0 && !attackedThisCycle;
        }

        @Override
        public void startExecuting() {
            this.attackedThisCycle = false;
            LivingEntity target = toad.getAttackTarget();
            if(target != null) {
                this.startDistance = toad.getDistance(target) * 15F;
                toad.setTongueTicks((int) startDistance);
                toad.lookAt(EntityAnchorArgument.Type.EYES, target.getPositionVec());
            }
        }

        @Override
        public void tick() {
            LivingEntity target = toad.getAttackTarget();
            if(target != null && toad.getTongueTicks() > 0 && startDistance > 0) {
                toad.lookAt(EntityAnchorArgument.Type.EYES, target.getPositionVec());
                double dirX = (target.getPosX() - toad.getPosX());
                double dirY = (target.getPosY() - toad.getPosY());
                double dirZ = (target.getPosZ() - toad.getPosZ());
                Vec3d targetPos = new Vec3d(dirX, dirY, dirZ).scale(toad.getTongueTicks() / startDistance).add(toad.getPositionVec());
                target.setPositionAndUpdate(targetPos.x, targetPos.y, targetPos.z);
                if(!attackedThisCycle && toad.getDistanceSq(target) <= getAttackReachSqr(target)) {
                    target.attackEntityFrom(DamageSource.causeMobDamage(toad), 2F);
                    this.attackedThisCycle = true;
                }
                toad.setTongueTicks(toad.getTongueTicks() - 1);
            }
        }

        @Override
        public void resetTask() {
            toad.setTongueTicks(0);
            this.cooldown = 60;
            this.attackedThisCycle = false;
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double) (this.toad.getWidth() * 2.0F * this.toad.getWidth() * 2.0F + attackTarget.getWidth());
        }
    }

    public static class ToadSentinelJumpSlamAttackGoal extends Goal {

        protected final ToadSentinelEntity toad;
        protected int cooldown = 200;
        protected float startDistance = 0;
        protected int ticks = 0;
        protected boolean lastOnGround = false;
        protected boolean landed = false;

        public ToadSentinelJumpSlamAttackGoal(ToadSentinelEntity toad) {
            this.toad = toad;
            this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() {
            if(cooldown > 0) {
                cooldown--;
            }
            LivingEntity target = toad.getAttackTarget();
            return target != null && target.isAlive() && cooldown == 0 && toad.getTongueTicks() == 0 && toad.getDistance(target) >= 3D;
        }

        @Override
        public boolean shouldContinueExecuting() {
            LivingEntity target = toad.getAttackTarget();
            return target != null && target.isAlive() && (!landed || ticks > 0);
        }

        @Override
        public void startExecuting() {
            toad.jump();
            LivingEntity target = toad.getAttackTarget();
            if(target != null) {
                float dx = (float) (toad.getPosX() - target.getPosX());
                float dz = (float) (toad.getPosZ() - target.getPosZ());
                this.startDistance = MathHelper.sqrt(dx * dx + dz * dz);
            }
            this.landed = false;
            this.lastOnGround = true;
            ticks = (int) this.startDistance;
        }

        @Override
        public void tick() {
            LivingEntity target = toad.getAttackTarget();
            if(target != null && startDistance > 0) {
                if(ticks >= 0) {
                    double dirX = (target.getPosX() - toad.getPosX()) * (ticks) / startDistance * 0.5F;
                    double dirZ = (target.getPosZ() - toad.getPosZ()) * (ticks) / startDistance * 0.5F;
                    toad.setMotion(dirX, toad.getMotion().getY(), dirZ);
                    ticks--;
                }
                if(!this.landed && toad.onGround && !lastOnGround) {
                    toad.playSound(SoundEvents.BLOCK_ANVIL_LAND, 2F, 0.2F);
                    toad.playSound(SoundEvents.BLOCK_ANVIL_LAND, 2F, 0.2F);
                    toad.playSound(SoundEvents.BLOCK_ANVIL_LAND, 2F, 0.2F);
                    int i = MathHelper.floor(toad.getPosX());
                    int j = MathHelper.floor(toad.getPosY() - (double) 0.2F);
                    int k = MathHelper.floor(toad.getPosZ());
                    BlockPos blockpos = new BlockPos(i, j, k);
                    BlockState blockstate = toad.world.getBlockState(blockpos);
                    for(int b = 0; b < 5; b++) {
                        toad.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(blockpos), toad.getPosX() + ((double) toad.rand.nextFloat() * 8F - 4D), toad.getPosY() + 0.5D, toad.getPosZ() + ((double) toad.rand.nextFloat() * 8F - 4D), 0F, 0D, 0F);
                    }
                    toad.world.getEntitiesWithinAABB(LivingEntity.class, toad.getBoundingBox().grow(10D)).forEach(living -> {
                        if(living != toad && EntityPredicate.DEFAULT.canTarget(toad, living) && living.attackEntityFrom(DamageSource.causeMobDamage(toad), 10F)) {
                            toad.knockBackNoResist(target, 4F);
                        }
                    });
                    this.landed = true;
                }
                this.lastOnGround = toad.onGround;
            }
        }

        @Override
        public void resetTask() {
            this.cooldown = 200;
            this.landed = false;
            this.ticks = 0;
        }
    }

    public static class ToadSentinelRibbitAttackGoal extends Goal {

        protected final ToadSentinelEntity toad;
        protected int cooldown = 100;

        public ToadSentinelRibbitAttackGoal(ToadSentinelEntity toad) {
            this.toad = toad;
            this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() {
            if(cooldown > 0) {
                cooldown--;
            }
            LivingEntity target = toad.getAttackTarget();
            return target != null && target.isAlive() && cooldown == 0 && toad.getTongueTicks() == 0 && toad.getBoundingBox().intersects(target.getBoundingBox());
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            LivingEntity target = toad.getAttackTarget();
            if(target != null) {
                toad.playSound(ModSoundEvents.TOAD_SENTINEL_RIBBIT, 2F, 1F);
                toad.playSound(ModSoundEvents.TOAD_SENTINEL_RIBBIT, 2F, 1F);
                toad.playSound(ModSoundEvents.TOAD_SENTINEL_RIBBIT, 2F, 1F);
                if(target.attackEntityFrom(DamageSource.causeMobDamage(toad), 8F)) {
                    toad.knockBackNoResist(target, !target.onGround ? 6F : 0.5F);
                }
            }
        }

        @Override
        public void resetTask() {
            this.cooldown = 100;
        }
    }

    protected void knockBackNoResist(LivingEntity target, float strengthIn) {
        float strength = strengthIn;
        double xRatio = this.getPosX() - target.getPosX();
        double zRatio = this.getPosZ() - target.getPosZ();
        net.minecraftforge.event.entity.living.LivingKnockBackEvent event = net.minecraftforge.common.ForgeHooks.onLivingKnockBack(target, this, strength, xRatio, zRatio);
        if(!event.isCanceled()) {
            strength = event.getStrength();
            xRatio = event.getRatioX();
            zRatio = event.getRatioZ();
            target.isAirBorne = true;
            Vec3d vec3d = target.getMotion();
            Vec3d vec3d1 = (new Vec3d(xRatio, 0.0D, zRatio)).normalize().scale((double) strength);
            target.setMotion(vec3d.x / 2.0D - vec3d1.x, target.onGround ? Math.min(0.4D, vec3d.y / 2.0D + (double) strength) : vec3d.y, vec3d.z / 2.0D - vec3d1.z);
        }
    }
}
