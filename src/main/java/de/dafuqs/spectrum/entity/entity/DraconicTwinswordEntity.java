package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.entity.NonLivingAttackable;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.items.tools.DraconicTwinswordItem;
import de.dafuqs.spectrum.mixin.accessors.TridentEntityAccessor;
import de.dafuqs.spectrum.registries.SpectrumDamageTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DraconicTwinswordEntity extends BidentBaseEntity implements NonLivingAttackable {

    private static final TrackedData<Boolean> HIT = DataTracker.registerData(DraconicTwinswordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> PROPELLED = DataTracker.registerData(DraconicTwinswordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> REBOUND = DataTracker.registerData(DraconicTwinswordEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final EntityDimensions initialSize = EntityDimensions.changing(1.5F, 1.5F);
    private static final EntityDimensions shortSize = EntityDimensions.changing(1F, 1F);
    private static final EntityDimensions tallSize = EntityDimensions.changing(1F, 1.8F);

    private int travelingTicks = 0, jiggleTicks = 20, jiggleIntensity = 8;


    public DraconicTwinswordEntity(World world) {
        this(SpectrumEntityTypes.DRAGON_TWINSWORD, world);
    }

    public DraconicTwinswordEntity(EntityType<? extends TridentEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        var pos = blockHitResult.getBlockPos();
        var state = getWorld().getBlockState(pos);

        var slime = state.isOf(Blocks.SLIME_BLOCK);
        var bounce = (state.isOf(Blocks.SLIME_BLOCK) || state.getHardness(getWorld(), pos) >= 25F) && !state.isIn(BlockTags.PLANKS) && !state.isIn(BlockTags.DIRT);
        var boost = getVelocity().length() < 1 || slime ? 1.4 : 0.9;

        if (isPropelled() && bounce && getVelocity().lengthSquared() > 1) {
            switch (blockHitResult.getSide().getAxis()) {
                case X -> setVelocity(getVelocity().multiply(-boost, boost, boost));
                case Y -> setVelocity(getVelocity().multiply(boost, -boost, boost));
                case Z -> setVelocity(getVelocity().multiply(boost, boost, -boost));
            }
            playSound(SpectrumSoundEvents.METAL_HIT, 1, 1.5F);
            travelingTicks = 0;
            return;
        }

        if (!isRebounding() && !isPropelled() && bounce) {
            travelingTicks = 0;
            rebound(getOwner().getPos(), 0.105, 0.15);
            playSound(SpectrumSoundEvents.METAL_TAP, 1, 1.5F);
            return;
        }

        super.onBlockHit(blockHitResult);
        if (dataTracker.get(HIT) || isNoClip())
            return;

        setRebounding(false);
        setPropelled(false);
        dataTracker.set(HIT, true);
        jiggleTicks = 0;
        jiggleIntensity = 4;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public void tick() {
        if (isPropelled() && !isRebounding()) {
            if (travelingTicks < 12) {
                travelingTicks++;

                if (travelingTicks > 6 && getVelocity().lengthSquared() > 2) {
                    setVelocity(getVelocity().multiply(0.5));
                    velocityDirty = true;
                    velocityModified = true;
                }
            }
        }
        else if(inGround){


            if (jiggleTicks < 15) {
                jiggleTicks++;

                var intensity = 1 - (jiggleTicks / 15F);

                prevPitch = getPitch();
                setPitch(prevPitch + jiggleIntensity * intensity / 2 * (random.nextInt(3) - 1));
                prevYaw = getYaw();
                setYaw(prevYaw + jiggleIntensity * intensity * (random.nextInt(3) - 1));
            }

            for(Entity thornCandidate : getWorld().getOtherEntities(this, calculateBoundingBox(), this::canHit)) {
                if (dataTracker.get(HIT)) {
                    if (!(thornCandidate instanceof ItemEntity) && thornCandidate.damage(getDamageSources().thorns(this), 4))
                        playSound(SoundEvents.ENCHANT_THORNS_HIT, 1, 0.9F + random.nextFloat() * 0.2F);
                }
            }
        }

        super.tick();
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        var striker = source.getAttacker();

        if (striker == null)
            return false;

        if (!dataTracker.get(HIT)) {
            travelingTicks = 0;
            this.setVelocity(striker.getPitch(), striker.getYaw(), 0.0F, 3F);
            setPitch(striker.getPitch());
            setYaw(striker.getYaw());
            prevPitch = getPitch();
            prevYaw = getYaw();
            setPropelled(true);
            setRebounding(false);
            ((TridentEntityAccessor) this).spectrum$setDealtDamage(false);
            playSound(SpectrumSoundEvents.METAL_HIT, 0.8F, 0.8F + random.nextFloat() * 0.4F);
        }
        else {
            jiggleTicks = 0;
            jiggleIntensity = 8;
            playSound(SoundEvents.ITEM_TRIDENT_HIT_GROUND, 1, 1);
        }

        return false;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public Box calculateBoundingBox() {
        if (isPropelled()) {
            return super.calculateBoundingBox();
        }
        else if(isRebounding()) {
            return shortSize.getBoxAt(getPos());
        }

        if (inGround) {
            var absPitch = Math.abs(getPitch());
            if (absPitch > 55)
                return tallSize.getBoxAt(getPos());
            return shortSize.getBoxAt(getPos());
        }

        return initialSize.getBoxAt(getPos());
    }

    public void setVelocity(float pitch, float yaw, float roll, float speed) {
        float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        float g = -MathHelper.sin((pitch + roll) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        setVelocity(new Vec3d(f, g, h).multiply(speed));
        velocityDirty = true;
        velocityModified = true;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        var propelled = isPropelled();
        Entity attacked = entityHitResult.getEntity();
        float f = propelled ? 2F : 1F;
        boolean crit = false;

        if (attacked instanceof LivingEntity livingAttacked) {
            f *= (getDamage(getTrackedStack()) + EnchantmentHelper.getAttackDamage(getTrackedStack(), livingAttacked.getGroup()));
        }

        if (!attacked.isOnGround() && propelled) {
            f *= 3;
            crit = true;
        }

        Entity owner = this.getOwner();
        DamageSource damageSource = SpectrumDamageTypes.impaling(getWorld(), this, owner);
        ((TridentEntityAccessor) this).spectrum$setDealtDamage(true);
        SoundEvent soundEvent = SpectrumSoundEvents.IMPALING_HIT;
        if (attacked.damage(damageSource, f)) {
            if (attacked.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (attacked instanceof LivingEntity livingAttacked) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingAttacked, owner);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)owner, livingAttacked);
                }

                this.onHit(livingAttacked);
            }
        }

        this.setVelocity(this.getVelocity().multiply(-1, 1, -1));
        travelingTicks = 0;
        float g = 1.0F;

        setPropelled(false);
        if (owner != null)
            rebound(owner.getPos(), 0.105, 0.15);

        this.playSound(soundEvent, g, 1.0F);
        if (crit) {
            this.playSound(SpectrumSoundEvents.CRITICAL_CRUNCH, 1F, 1.0F);
            this.playSound(SpectrumSoundEvents.IMPACT_BASE, 1.8F, 0.5F);
        }
        else {
            this.playSound(SpectrumSoundEvents.IMPALING_HIT, 1F, 0.9F + random.nextFloat() * 0.2F);
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity);
    }

    @Nullable
    @Override
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(
                this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), this::canHit
        );
    }

    private float getDamage(ItemStack stack) {
        return (float) stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .stream()
                .mapToDouble(EntityAttributeModifier::getValue)
                .sum();
    }

    @Override
    public void age() {}

    public boolean isPropelled() {
        return dataTracker.get(PROPELLED);
    }

    public boolean isRebounding() {
        return dataTracker.get(REBOUND);
    }

    public void setPropelled(boolean propelled) {
        dataTracker.set(PROPELLED, propelled);
    }

    public void setRebounding(boolean rebounding) {
        dataTracker.set(REBOUND, rebounding);
    }

    public void rebound(Vec3d target, double xMod, double yMod) {
        setRebounding(true);

        var yPos = this.getPos();
        var heightDif = Math.abs(yPos.y - target.y);
        var velocity = target.subtract(yPos);

        yMod = Math.max(0.0725, yMod * (1 - (heightDif * 0.024)));

        this.setVelocity(velocity.multiply(xMod, yMod, xMod).add(0, 0.3, 0));
        this.setYaw(-getYaw());
        this.setPitch(-getPitch());
        this.velocityModified = true;
        this.velocityDirty = true;
    }

    @Override
    public void remove(RemovalReason reason) {
        var rootStack = getRootStack();
        if (!rootStack.isEmpty()) {
            SpectrumItems.DRACONIC_TWINSWORD.markReserved(rootStack, false);
        }
        super.remove(reason);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HIT, false);
        this.dataTracker.startTracking(PROPELLED, false);
        this.dataTracker.startTracking(REBOUND, false);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(HIT, nbt.getBoolean("hit"));
        setTrackedStack(ItemStack.fromNbt(nbt));
        setPropelled(nbt.getBoolean("propelled"));
        setRebounding(nbt.getBoolean("rebounding"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        getTrackedStack().writeNbt(nbt);
        nbt.putBoolean("hit", this.dataTracker.get(HIT));
        nbt.putBoolean("propelled", isPropelled());
        nbt.putBoolean("rebounding", isRebounding());
    }

    private ItemStack getRootStack() {
        if (getOwner() instanceof PlayerEntity player) {
            var rootStack = DraconicTwinswordItem.findThrownStack(player, uuid);
            return rootStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {

    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        return tryPickup(player) ? ActionResult.SUCCESS : ActionResult.FAIL;
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        if (player != getOwner()) {
            player.damage(getDamageSources().thorns(this), 20);
            playSound(SoundEvents.ENCHANT_THORNS_HIT, 1, 0.9F + random.nextFloat() * 0.2F);
            return false;
        }

        var rootStack = DraconicTwinswordItem.findThrownStack(player, uuid);
        if (!rootStack.isEmpty()) {
            if (this.getWorld().isClient())
                return true;

            SpectrumItems.DRACONIC_TWINSWORD.markReserved(rootStack, false);
            player.sendPickup(this, 1);
            player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 1);
            discard();
            return true;
        }
        else {
            discard();
        }
        return false;
    }

    @Nullable
    @Override
    public ItemEntity dropStack(ItemStack stack) {
        return null;
    }

    @Nullable
    @Override
    public ItemEntity dropStack(ItemStack stack, float yOffset) {
        return null;
    }
}
