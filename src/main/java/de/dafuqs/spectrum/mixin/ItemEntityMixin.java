package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.item.*;
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
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.PREVENTS_ITEM_DAMAGE))
			setUnlimitedLifetime();
	}
	
	@SuppressWarnings("UnreachableCode")
	@Inject(at = @At("TAIL"), method = "tick()V")
	public void tick(CallbackInfo ci) {
		// protect steadfast enchanted item stacks from the void by letting them float above it
		ItemEntity entity = ((ItemEntity) (Object) this);
		if (!entity.isNoGravity() && entity.level().getGameTime() % 8 == 0) {
			int worldMinY = entity.level().getMinBuildHeight();
			if (!entity.onGround()
					&& entity.position().y() < worldMinY + 2
					&& EnchantmentHelper.hasTag(entity.getItem(), SpectrumEnchantmentTags.PREVENTS_ITEM_DAMAGE)) {
				
				if (entity.position().y() < worldMinY + 1) {
					entity.setPos(entity.position().x, worldMinY + 1, entity.position().z);
				}
				
				entity.setDeltaMovement(0, 0, 0);
				entity.setNoGravity(true);
			}
		}
		
		if (entity.getItem().getItem() instanceof TickAwareItem tickingItem) {
			tickingItem.onItemEntityTicked(entity);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "hurt")
	public void spectrumItemStackDamageActions(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (amount > 0 && this.getItem().getItem() instanceof DamageAwareItem damageAwareItem) {
			damageAwareItem.onItemEntityDamaged(source, amount, (ItemEntity) (Object) this);
		}
	}
	
	@SuppressWarnings("UnreachableCode")
	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void isDamageProof(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		ItemEntity thisItemEntity = (ItemEntity) (Object) this;
		
		if (ItemDamageImmunity.isImmuneTo(thisItemEntity.getItem(), source))
			callbackInfoReturnable.setReturnValue(true);
		
		if (!source.is(SpectrumDamageTypes.PRIMORDIAL_FIRE)) return;
		Level world = thisItemEntity.level();
		
		if (!PrimordialFireBurningRecipe.processItemEntity(world, thisItemEntity)) return;
		callbackInfoReturnable.setReturnValue(true);
	}
	
	@SuppressWarnings("UnreachableCode")
	@Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
	private void isFireProof(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (ItemDamageImmunity.isImmuneTo(((ItemEntity) (Object) this).getItem(), DamageTypeTags.IS_FIRE))
			callbackInfoReturnable.setReturnValue(true);
	}
	
	@SuppressWarnings("UnreachableCode")
	@Inject(method = "tick()V", at = @At("TAIL"))
	public void doGravityEffects(CallbackInfo ci) {
		ItemEntity entity = ((ItemEntity) (Object) this);
		if (entity.isNoGravity()) return;
		
		ItemStack stack = entity.getItem();
		Item item = stack.getItem();
		// this is not affected by item entity stack count to make it more predictable
		float gravityMod = stack.getOrDefault(SpectrumDataComponentTypes.GRAVITABLE, 0.0f);
		if (gravityMod == 0.0f) return;
		
		// if the stack is floating really high => delete it
		if (entity.position().y() > entity.level().getMaxBuildHeight() + 200) entity.discard();
		// since an ItemEntity is much lighter than a player, we can x10 the gravity effect
		else entity.push(0, gravityMod * 10, 0);
	}
}
