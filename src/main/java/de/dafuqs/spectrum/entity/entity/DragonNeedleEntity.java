package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.items.tools.DragonNeedleItem;
import de.dafuqs.spectrum.mixin.accessors.PersistentProjectileEntityAccessor;
import de.dafuqs.spectrum.mixin.accessors.TridentEntityAccessor;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumDamageTypes;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DragonNeedleEntity extends BidentBaseEntity {

    private static final TrackedData<Boolean> HIT = DataTracker.registerData(DragonNeedleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public DragonNeedleEntity(World world) {
        this(SpectrumEntityTypes.DRAGON_NEEDLE, world);
    }

    public DragonNeedleEntity(EntityType<? extends TridentEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (dataTracker.get(HIT) || isNoClip())
            return;

        dataTracker.set(HIT, true);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity attacked = entityHitResult.getEntity();
        float f = 2.0F;
        if (attacked instanceof LivingEntity livingAttacked) {
            f *= (getDamage(getStack()) + EnchantmentHelper.getAttackDamage(getStack(), livingAttacked.getGroup()));
        }

        Entity owner = this.getOwner();
        DamageSource damageSource = SpectrumDamageTypes.impaling(getWorld(), this, owner);
        ((TridentEntityAccessor) this).spectrum$setDealtDamage(true);
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
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

        recall();
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        float g = 1.0F;

        this.playSound(soundEvent, g, 1.0F);
    }

    private float getDamage(ItemStack stack) {
        return (float) stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .stream()
                .mapToDouble(EntityAttributeModifier::getValue)
                .sum();
    }

    @Override
    protected void onHit(LivingEntity target) {
        var owner = getOwner();
        var targetPos = target.getPos();
        var ownerPos = owner.getPos();
        var relativePos = targetPos.relativize(ownerPos);
        var angle = relativePos.normalize();

        var sizeDif = getVolumeDif(target);

        var horizontal = relativePos.multiply(1, 0, 1).length() * -0.25;
        var vertical = Math.min(relativePos.multiply(0, 1, 0).length() * 0.25, 20);

        target.addVelocity(new Vec3d(-horizontal * sizeDif, vertical, -horizontal * sizeDif).multiply(angle));
        target.velocityModified = true;
        target.velocityDirty = true;
    }

    private float getVolumeDif(LivingEntity target) {
        var ownerBox = getOwner().getBoundingBox();
        var targetBox = target.getBoundingBox();
        float ownerVolume = (float) (ownerBox.getXLength() * ownerBox.getYLength() * ownerBox.getZLength());
        float targetVolume = (float) (targetBox.getXLength() * targetBox.getYLength() * targetBox.getZLength());
        return Math.max(Math.min(ownerVolume / (targetVolume / 3), 0.8F), 0.4F);
    }

    public void recall() {
        if (dataTracker.get(HIT) && !isNoClip()) {
            var owner = getOwner();
            var needlePos = getPos();
            var ownerPos = owner.getPos();
            var relativePos = needlePos.relativize(ownerPos);
            var angle = relativePos.normalize();

            var horizontal = relativePos.multiply(1, 0, 1).length() * -0.65;
            var vertical = relativePos.multiply(0, 1, 0).length() * -0.5;

            owner.addVelocity(new Vec3d(horizontal, vertical, horizontal).multiply(angle));
            owner.velocityModified = true;
            owner.velocityDirty = true;
        }

        getDataTracker().set(TridentEntityAccessor.spectrum$getLoyalty(), (byte) 4);
        setNoClip(true);
    }

    @Override
    public void age() {
        if (!getRootStack().isEmpty())
            return;

        var life = ((PersistentProjectileEntityAccessor) this).getLife() + 1;
        ((PersistentProjectileEntityAccessor) this).setLife(life);
        if (life >= 1200) {
            this.discard();
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        var rootStack = getRootStack();
        if (!rootStack.isEmpty()) {
            DragonNeedleItem.markReserved(rootStack, false);
        }
        super.remove(reason);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HIT, false);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(HIT, nbt.getBoolean("hit"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("hit", this.dataTracker.get(HIT));
    }

    private ItemStack getRootStack() {
        if (getOwner() instanceof PlayerEntity player) {
            var rootStack = DragonNeedleItem.findThrownStack(player, uuid);
            return rootStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        var rootStack = DragonNeedleItem.findThrownStack(player, uuid);
        if (!rootStack.isEmpty()) {
            DragonNeedleItem.markReserved(rootStack, false);
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public ItemEntity dropStack(ItemStack stack) {
        return null;
    }
}
