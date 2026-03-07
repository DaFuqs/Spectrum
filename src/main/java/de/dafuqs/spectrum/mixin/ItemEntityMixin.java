package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	
	@Shadow
	public abstract ItemStack getItem();
	
	@Shadow
	public abstract void setUnlimitedLifetime();
	
	@Shadow
	public abstract boolean hurt(DamageSource source, float amount);
	
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;DDD)V")
	public void ItemEntity(Level world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
		// item stacks that are enchanted with damage proof should never despawn
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.PREVENTS_ITEM_DAMAGE)) {
			setUnlimitedLifetime();
		}
	}
	
	@Inject(at = @At("TAIL"), method = "tick()V")
	public void tick(CallbackInfo ci) {
		// protect steadfast enchanted item stacks from the void by letting them float above it
		ItemEntity thisItemEntity = ((ItemEntity) (Object) this);
		if (!thisItemEntity.isNoGravity() && thisItemEntity.level().getGameTime() % 8 == 0) {
			int worldMinY = thisItemEntity.level().getMinBuildHeight();
			if (!thisItemEntity.onGround()
					&& thisItemEntity.position().y() < worldMinY + 2
					&& EnchantmentHelper.hasTag(thisItemEntity.getItem(), SpectrumEnchantmentTags.PREVENTS_ITEM_DAMAGE)) {
				
				if (thisItemEntity.position().y() < worldMinY + 1) {
					thisItemEntity.setPos(thisItemEntity.position().x, worldMinY + 1, thisItemEntity.position().z);
				}
				
				thisItemEntity.setDeltaMovement(0, 0, 0);
				thisItemEntity.setNoGravity(true);
			}
		}
		
		if (thisItemEntity.getItem().getItem() instanceof TickAwareItem tickingItem) {
			tickingItem.onItemEntityTicked(thisItemEntity);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "hurt")
	public void spectrumItemStackDamageActions(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (amount > 0 && this.getItem().getItem() instanceof DamageAwareItem damageAwareItem) {
			damageAwareItem.onItemEntityDamaged(source, amount, (ItemEntity) (Object) this);
		}
	}
	
	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void isDamageProof(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		ItemEntity thisItemEntity = (ItemEntity) (Object) this;
		if (DamageImmuneComponent.isImmuneTo(thisItemEntity.getItem(), source)) {
			callbackInfoReturnable.setReturnValue(true);
		}
		if (source.is(SpectrumDamageTypes.PRIMORDIAL_FIRE)) {
			Level world = thisItemEntity.level();
			
			if (PrimordialFireBurningRecipe.processItemEntity(world, thisItemEntity)) {
				callbackInfoReturnable.setReturnValue(true);
			}
		}
	}
	
	@Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
	private void isFireProof(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (DamageImmuneComponent.isImmuneTo(((ItemEntity) (Object) this).getItem(), DamageTypeTags.IS_FIRE)) {
			callbackInfoReturnable.setReturnValue(true);
		}
	}
	
	@Inject(method = "tick()V", at = @At("TAIL"))
	public void doGravityEffects(CallbackInfo ci) {
		ItemEntity itemEntity = ((ItemEntity) (Object) this);
		
		if (itemEntity.isNoGravity()) {
			return;
		}
		
		ItemStack stack = itemEntity.getItem();
		Item item = stack.getItem();
		
		if (item instanceof GravitableItem gravitableItem) {
			// if the stack is floating really high => delete it
			gravitableItem.applyGravity(stack, itemEntity.level(), itemEntity);
		}
		
	}
	
}
