package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LightShardEntity extends ProjectileEntity {

    public static final int DEFAULT_MAX_AGE = 200, DECELERATION_PHASE_LENGTH = 20;
    public static final float DEFAULT_ACCELERATION = 0.03F, DEFAULT_DAMAGE = 4f;
    private float scaleOffset, damage;
    private long maxAge;
    private Optional<UUID> target = Optional.empty();
    private Optional<Entity> targetEntity = Optional.empty();
    private Vec3d initialVelocity = Vec3d.ZERO;
    public float rotationOffset, lastRotationOffset;

    public LightShardEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
        scaleOffset = world.random.nextFloat() + 0.334F;
        rotationOffset = world.random.nextFloat() * 360 - 180;
        maxAge = (int) (DEFAULT_MAX_AGE + MathHelper.nextGaussian(world.getRandom(), 10, 7));
        damage = DEFAULT_DAMAGE;
    }

    public LightShardEntity(World world, LivingEntity owner, float damageMod, float lifespanMod) {
        this(world, owner, Optional.empty(), damageMod, lifespanMod);
    }

    public LightShardEntity(World world, LivingEntity owner, Optional<Entity> target, float damageMod, float lifespanMod) {
        super(SpectrumEntityTypes.LIGHT_SHARD, world);
        target.ifPresent(this::setTarget);
        this.setOwner(owner);

        var random = world.getRandom();

        scaleOffset = random.nextFloat() * 0.5F + 0.5F;
        rotationOffset = random.nextFloat() * 360 - 180;
        maxAge = (long) (DEFAULT_MAX_AGE * lifespanMod);
        damage = DEFAULT_DAMAGE * damageMod;
    }

    @Override
    public void tick() {
        super.tick();

        if (world.isClient() && age > DECELERATION_PHASE_LENGTH - 1 && getVelocity().length() > 0.075) {
            if (getVelocity().length() > 0.2 || world.getTime() % 2 == 0)
                world.addParticle(SpectrumParticleTypes.LIGHT_TRAIL, true, prevX, prevY, prevZ, 0, 0, 0);
        }

        if(age > maxAge) {
            playSound(SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_BREAK, random.nextFloat() + 0.25F, 1.2F + random.nextFloat());
            this.remove(RemovalReason.DISCARDED);
        }
    
        var velocity = getVelocity();
        updatePosition(getX() + velocity.getX(), getY() + velocity.getY(), getZ() + velocity.getZ());

        if (age < DECELERATION_PHASE_LENGTH) {
            var deceleration = Math.max((float) age / DECELERATION_PHASE_LENGTH, 0.334);
            setVelocity(
                    MathHelper.lerp(deceleration, initialVelocity.x, 0),
                    MathHelper.lerp(deceleration, initialVelocity.y, 0),
                    MathHelper.lerp(deceleration, initialVelocity.z, 0)
            );
            lastRotationOffset = rotationOffset;
            rotationOffset += (1 - deceleration);
            velocityDirty = true;
            scheduleVelocityUpdate();
            return;
        }

        var hitResult = ProjectileUtil.getCollision(this, this::canHit);

        onCollision(hitResult);

        if(isTargetInvalid(targetEntity)) {

            if (world.isClient)
                return;

            if (random.nextFloat() > 0.25)
                return;

            var serverWorld = (ServerWorld) world;

            var entities = serverWorld.getOtherEntities(this, Box.of(getPos(),48, 48, 48));

            Collections.shuffle(entities);
            var potentialTarget = entities
                    .stream()
                    .filter(entity -> entity instanceof HostileEntity)
                    .filter(entity -> ((HostileEntity) entity).canSee(this))
                    .findAny();

            if (isTargetInvalid(potentialTarget))
                return;

            var newTarget = potentialTarget.get();
            setTarget(newTarget);
        }

        var entity = targetEntity.get();

        var transformVector = entity
                .getPos()
                .add(0, entity.getHeight() / 2, 0)
                .subtract(getPos())
                .normalize();

        var accelerationVector = transformVector.multiply(DEFAULT_ACCELERATION);
        addVelocity(accelerationVector.x, accelerationVector.y, accelerationVector.z);
    }

    public void setInitialVelocity(Vec3d vector) {
        initialVelocity = vector;
        setVelocity(vector);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        var attacked = entityHitResult.getEntity();

        if (attacked == getOwner())
            return;

        if (attacked instanceof Tameable pet && pet.getOwner() == getOwner())
            return;

        if (attacked instanceof PlayerEntity player && targetEntity.map(entity -> entity == player).orElse(false)) {
            return;
        }

        if (!(attacked instanceof LivingEntity))
            return;

        var finalDamage = damage * (random.nextFloat() + 0.5F) * (1 - getVanishingProgress(age));

        attacked.timeUntilRegen = 0;
        attacked.damage(SpectrumDamageSources.irradiance((LivingEntity) getOwner()), finalDamage);

        attacked.playSound(SpectrumSoundEvents.BLOCK_MOONSTONE_CLUSTER_BREAK, 2, 0.8F + random.nextFloat());
        attacked.playSound(SoundEvents.BLOCK_GLASS_BREAK, random.nextFloat() * 0.5F + 0.2F, 0.8F + random.nextFloat());
        this.remove(RemovalReason.DISCARDED);
        super.onEntityHit(entityHitResult);
    }

    @Override
    public void onRemoved() {
        var bound = random.nextInt(11);
        for (int i = 0; i < bound + 5; i++) {
            if(random.nextFloat() < 0.665) {
                world.addImportantParticle(SpectrumParticleTypes.WHITE_SPARKLE_RISING, true, getX(), getY(), getZ(),
                        random.nextFloat() * 0.25F - 0.125F,
                        random.nextFloat() * 0.25F - 0.125F,
                        random.nextFloat() * 0.25F - 0.125F
                );
            }
            else {
                world.addImportantParticle(SpectrumParticleTypes.SHOOTING_STAR, true, getX(), getY(), getZ(),
                        random.nextFloat() * 0.5F - 0.25F,
                        random.nextFloat() * 0.5F - 0.25F,
                        random.nextFloat() * 0.5F - 0.25F
                );
            }
        }
        super.onRemoved();
    }

    public float getScaleOffset() {
        return scaleOffset;
    }
    
    public float getVanishingProgress(int age) {
        return 1 - (float) Math.min(maxAge - age, getVanishingLength()) / getVanishingLength();
    }

    public int getVanishingLength() {
        return Math.round(maxAge / 4F);
    }

    public void setTarget(@NotNull Entity target) {
        this.target = Optional.ofNullable(target.getUuid());
        this.targetEntity = Optional.of(target);
    }

    public boolean isTargetInvalid(Optional<Entity> entityOptional) {
        if (entityOptional.isEmpty())
            return true;

        var entity = entityOptional.get();

        return entity.isRemoved() || !entity.isAlive() || entity.isInvulnerable();
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        target.ifPresent(uuid -> nbt.putUuid("target", uuid));
        nbt.putDouble("initX", initialVelocity.x);
        nbt.putDouble("initY", initialVelocity.y);
        nbt.putDouble("initZ", initialVelocity.z);

        nbt.putFloat("damage", damage);
        nbt.putFloat("scale", scaleOffset);
        nbt.putLong("maxAge", maxAge);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("target")) {
            target = Optional.ofNullable(nbt.getUuid("target"));
        }

        initialVelocity = new Vec3d(
                nbt.getDouble("initX"),
                nbt.getDouble("initY"),
                nbt.getDouble("initZ")
        );

        damage = nbt.getFloat("damage");
        scaleOffset = nbt.getFloat("scale");
        maxAge = nbt.getLong("maxAge");
    }
}
