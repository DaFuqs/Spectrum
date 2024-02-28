package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	
	@Shadow
	public abstract ItemStack getStack();
	
	@Shadow
	public abstract void setNeverDespawn();
	
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);
	
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;DDD)V")
	public void ItemEntity(World world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
		// item stacks that are enchanted with damage proof should never despawn
		if (EnchantmentHelper.getLevel(SpectrumEnchantments.STEADFAST, stack) > 0) {
			setNeverDespawn();
		}
	}
	
	@Inject(at = @At("TAIL"), method = "tick()V")
	public void tick(CallbackInfo ci) {
		// protect steadfast enchanted item stacks from the void by letting them float above it
		ItemEntity thisItemEntity = ((ItemEntity) (Object) this);
		if (!thisItemEntity.hasNoGravity() && thisItemEntity.getWorld().getTime() % 8 == 0) {
			int worldMinY = thisItemEntity.getWorld().getBottomY();
			if (!thisItemEntity.isOnGround()
					&& thisItemEntity.getPos().getY() < worldMinY + 2
					&& EnchantmentHelper.getLevel(SpectrumEnchantments.STEADFAST, thisItemEntity.getStack()) > 0) {
				
				if (thisItemEntity.getPos().getY() < worldMinY + 1) {
					thisItemEntity.setPosition(thisItemEntity.getPos().x, worldMinY + 1, thisItemEntity.getPos().z);
				}
				
				thisItemEntity.setVelocity(0, 0, 0);
				thisItemEntity.setNoGravity(true);
			}
		}
		
		if (thisItemEntity.getStack().getItem() instanceof TickAwareItem tickingItem) {
			tickingItem.onItemEntityTicked(thisItemEntity);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void spectrumItemStackDamageActions(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (amount > 0 && this.getStack().getItem() instanceof DamageAwareItem damageAwareItem) {
			damageAwareItem.onItemEntityDamaged(source, amount, (ItemEntity) (Object) this);
		}
	}
	
	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	private void isDamageProof(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (ItemDamageImmunity.isImmuneTo(((ItemEntity) (Object) this).getStack(), source)) {
			callbackInfoReturnable.setReturnValue(true);
		}
		if(source.isOf(SpectrumDamageTypes.PRIMORDIAL_FIRE)) {
			ItemEntity thisItemEntity = ((ItemEntity) (Object) this);
			World world = thisItemEntity.getWorld();

			if(PrimordialFireBurningRecipe.processItemEntity(world, thisItemEntity)) {
				callbackInfoReturnable.setReturnValue(true);
			}
		}
	}
	
	@Inject(method = "isFireImmune()Z", at = @At("HEAD"), cancellable = true)
	private void isFireProof(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (ItemDamageImmunity.isImmuneTo(((ItemEntity) (Object) this).getStack(), DamageTypeTags.IS_FIRE)) {
			callbackInfoReturnable.setReturnValue(true);
		}
	}
	
	@Inject(method = "tick()V", at = @At("TAIL"))
	public void doGravityEffects(CallbackInfo ci) {
		ItemEntity itemEntity = ((ItemEntity) (Object) this);
		
		if (itemEntity.hasNoGravity()) {
			return;
		}
		
		ItemStack stack = itemEntity.getStack();
		Item item = stack.getItem();
		
		if (item instanceof GravitableItem gravitableItem) {
			// if the stack is floating really high => delete it
			gravitableItem.applyGravity(stack, itemEntity.getWorld(), itemEntity);
		}
		
	}
	
}
