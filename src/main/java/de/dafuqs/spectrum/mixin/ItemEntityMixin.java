package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
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
	public void spectrumItemStackDamageActions(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (amount > 0 && this.getItem().getItem() instanceof DamageAwareItem damageAwareItem) {
			damageAwareItem.onItemEntityDamaged(source, amount, (ItemEntity) (Object) this);
		}
	}
	
	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void isDamageProof(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		ItemEntity thisItemEntity = (ItemEntity) (Object) this;
		if (DamageImmunityHelper.isImmuneTo(thisItemEntity.getItem(), source)) {
			cir.setReturnValue(false);
		} else if (source.is(SpectrumDamageTypes.PRIMORDIAL_FIRE)) {
			if (PrimordialFireBurningRecipe.processItemEntity(thisItemEntity.level(), thisItemEntity)) {
				cir.setReturnValue(true);
			}
		}
	}
	
	@Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
	private void spectrum$isFireImmune(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (DamageImmunityHelper.isImmuneTo(((ItemEntity) (Object) this).getItem(), DamageTypeTags.IS_FIRE)) {
			callbackInfoReturnable.setReturnValue(true);
		}
	}
	
}
