package net.zaharenko424.protogenmod.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.event.EventHooks;
import net.zaharenko424.protogenmod.DamageSources;
import net.zaharenko424.protogenmod.registry.EntityRegistry;
import net.zaharenko424.protogenmod.util.DynamicClipContext;
import net.zaharenko424.protogenmod.util.PacketUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class PlasmaBolt extends Projectile {

    public static final float DAMAGE = 10;
    private static final float PARTICLE_DIST = 128;

    protected ItemStack weapon;

    public PlasmaBolt(EntityType<? extends PlasmaBolt> entityType, Level level) {
        super(entityType, level);
        setNoGravity(true);
    }

    public PlasmaBolt(LivingEntity owner, ItemStack weapon) {
        this(EntityRegistry.PLASMA_BOLT.get(), owner.level());
        setPos(owner.getX(), owner.getY(), owner.getZ());
        setOwner(owner);
        this.weapon = weapon;
    }

    protected PlasmaBolt(Level level, double x, double y, double z, @Nullable ItemStack weapon) {
        this(EntityRegistry.PLASMA_BOLT.get(), level);
        setPos(x, y, z);
        if (weapon != null) this.weapon = weapon.copy();
    }

    @Override
    public @Nullable ItemStack getWeaponItem() {
        return weapon;
    }

    @Override
    protected void onHit(HitResult result) {
        Level level = level();
        HitResult.Type type = result.getType();
        if (type == HitResult.Type.ENTITY) {
            onHitEntity((EntityHitResult) result);

            level.gameEvent(GameEvent.PROJECTILE_LAND, result.getLocation(), GameEvent.Context.of(this, null));
        } else if (type == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult) result;
            BlockPos hitPos = blockhitresult.getBlockPos();
            FluidState hitFluid = level.getFluidState(hitPos);
            if(!hitFluid.isEmpty()){
                onHitFluid(hitFluid);
            } else onHitBlock(blockhitresult);

            level().gameEvent(GameEvent.PROJECTILE_LAND, hitPos, GameEvent.Context.of(this, level().getBlockState(hitPos)));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (level().isClientSide) return;

        Entity entity = result.getEntity();
        float f = (float) getDeltaMovement().length();
        float d0 = DAMAGE;
        Entity owner = getOwner();
        DamageSource damagesource = DamageSources.plasma(this, owner);
        if (weapon != null && level() instanceof ServerLevel serverlevel) {
            d0 = EnchantmentHelper.modifyDamage(serverlevel, weapon, entity, damagesource, d0);
        }

        float j = Mth.clamp(f * d0, 0f, 2.147483647E9f);

        if (owner instanceof LivingEntity living) {
            living.setLastHurtMob(entity);
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        entity.igniteForSeconds(5);

        if (entity.hurt(damagesource, j)) {
            if (flag) return;

            if (entity instanceof LivingEntity livingentity) {
                doKnockback(livingentity, damagesource);
                if (level() instanceof ServerLevel sLevel) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(sLevel, livingentity, damagesource, weapon);
                }
            }
        } else {
            if (level() instanceof ServerLevel level) {
                PacketUtil.sendParticles(level, PacketUtil.distance(this, PARTICLE_DIST), ParticleTypes.SOUL_FIRE_FLAME, true, getX() - .325, getY(), getZ() - .325, 25, .75f, 1, .75f, 2);
            }
            explode(DamageSources.plasmaExplosion(this, owner), 2f, SoundEvents.DRAGON_FIREBALL_EXPLODE, 1);
        }

        discard();
    }

    protected void doKnockback(LivingEntity entity, DamageSource damageSource) {
        float d0 = weapon != null && level() instanceof ServerLevel serverlevel
                ? EnchantmentHelper.modifyKnockback(serverlevel, weapon, entity, damageSource, 0.0F) : 0.0F;

        if (d0 <= 0) return;

        double d1 = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        Vec3 vec3 = getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(d0 * 0.5 * d1);
        if (vec3.lengthSqr() > 0.0) {
            entity.push(vec3.x, 0.1, vec3.z);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Level level = level();
        if (level.isClientSide) return;

        if (level() instanceof ServerLevel sLevel) {
            PacketUtil.sendParticles(sLevel, PacketUtil.distance(this, PARTICLE_DIST), ParticleTypes.SOUL_FIRE_FLAME, true, getX() - .25, getY(), getZ() - .25, 25, .5f, 1, .5f, 2);
        }
        explode(DamageSources.plasma(this, getOwner()), 1f, SoundEvents.LAVA_EXTINGUISH, 0.5f);
        discard();
    }

    protected void onHitFluid(FluidState state){
        if (state.getFluidType().getTemperature() < 1300) {
            waterExplosion();
            return;
        }

        lavaHurt();
    }

    protected void waterExplosion(){
        if (level() instanceof ServerLevel level) {
            Predicate<ServerPlayer> send = PacketUtil.distance(this, PARTICLE_DIST);
            PacketUtil.sendCenteredParticles(level, send, ParticleTypes.EXPLOSION, this, 1, 0, 0, 0, 1);
            PacketUtil.sendCenteredParticles(level, send, ParticleTypes.BUBBLE, this, 100, 1, 1, 1, .5f);
            PacketUtil.sendParticles(level, send, ParticleTypes.CAMPFIRE_COSY_SMOKE, getX() - .325, getY(), getZ() - .325, 50, .75f, 1, .75f, .1f);
            PacketUtil.sendParticles(level, send, ParticleTypes.SOUL_FIRE_FLAME, true, getX() - .325, getY(), getZ() - .325, 75, .75f, 1, .75f, 2);
        }

        explode(DamageSources.plasmaExplosion(this, getOwner()), 2.5f, SoundEvents.LAVA_EXTINGUISH, 4);
        discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        HitResult hitresult = getHitResultOnMoveVector(position(), getDeltaMovement(), ClipContext.Block.COLLIDER, f -> !f.isEmpty());
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)){
            setOldPosAndRot();
            setPos(hitresult.getLocation().subtract(position()).scale(0.9).add(position()));
            onHit(hitresult);
            return;
        }

        if (isInWater()){
            waterExplosion();
            return;
        }

        Vec3 deltaMovement = getDeltaMovement();
        setOldPosAndRot();
        setPos(getX() + deltaMovement.x, getY() + deltaMovement.y, getZ() + deltaMovement.z);
    }

    protected HitResult getHitResultOnMoveVector(Vec3 pos, Vec3 deltaMovement, ClipContext.ShapeGetter block, Predicate<FluidState> canPick){
        Vec3 vec3 = pos.add(deltaMovement);
        HitResult hitresult = level().clip(new DynamicClipContext(pos, vec3, block, canPick, CollisionContext.of(this)));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec3 = hitresult.getLocation();
        }

        HitResult entityHit = ProjectileUtil.getEntityHitResult(
                level(), this, pos, vec3, this.getBoundingBox().expandTowards(deltaMovement).inflate(1.0), this::canHitEntity, 0
        );
        return entityHit != null ? entityHit : hitresult;
    }

    protected void explode(DamageSource damageSource, float radius, SoundEvent sound, float volume){
        Level level = level();
        Explosion explosion = new Explosion(level, this, damageSource, null, getX(), getY(), getZ(), radius, true, Explosion.BlockInteraction.KEEP, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
        explosion.explode();

        //Manually finalize explosion as most of the finalize code isn't needed
        BlockState state, toPlace;
        BlockPos relative;
        boolean anySet;
        for (BlockPos pos : explosion.getToBlow()) {
            if (random.nextInt(3) != 0) continue;

            state = level.getBlockState(pos);
            if (!state.isAir() && !state.canBeReplaced()) continue;

            if (level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)){
                level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
                continue;
            }

            toPlace = Blocks.FIRE.defaultBlockState();
            anySet = false;
            for (Direction dir : Direction.Plane.HORIZONTAL){
                relative = pos.relative(dir);
                if (level.getBlockState(relative).isFlammable(level, relative, dir.getOpposite())) {
                    toPlace = toPlace.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(dir), true);
                    anySet = true;
                }
            }

            if (anySet) level.setBlockAndUpdate(pos, toPlace);
        }

        playSound(sound, volume, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F);
    }

    @Override
    public float getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState, float explosionPower) {
        return 0;
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        setXRot(-getXRot());
        xRotO = getXRot();
    }

    @Override
    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -Mth.sin(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((x + z) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));
        shoot(f, f1, f2, velocity, inaccuracy);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 10.0;
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }

        d0 *= 64.0 * getViewScale();
        return distance < d0 * d0;
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        Entity owner = getOwner();
        return owner != null ? owner.getSoundSource() : SoundSource.NEUTRAL;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void lavaHurt() {
        discard();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (weapon != null) compound.put("weapon", weapon.save(registryAccess(), new CompoundTag()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("weapon", 10)) {
            weapon = ItemStack.parse(registryAccess(), compound.getCompound("weapon")).orElse(null);
        } else {
            weapon = null;
        }
    }
}
