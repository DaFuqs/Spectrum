package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	
	private static AutoCompactingInventory autoCompactingInventory;
	
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
		// protect damage proof enchanted item stacks from the void by letting them float above it
		ItemEntity thisItemEntity = ((ItemEntity) (Object) this);
		if (!thisItemEntity.hasNoGravity() && thisItemEntity.world.getTime() % 8 == 0) {
			int worldMinY = thisItemEntity.world.getDimension().minY();
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
	}
	
	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
	public void spectrumItemStackDamageActions(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (source.isOf(DamageTypes.FALLING_ANVIL) || SpectrumDamageSources.FLOATBLOCK.equals(source)) {
			doAnvilCrafting(amount);
			
			// prevent the source itemStack taking damage.
			// ItemEntities have a health of 5 and can actually get killed by a falling anvil
			callbackInfoReturnable.setReturnValue(true);
		}
		
		if (amount > 1 && this.getStack().getItem() instanceof DamageAwareItem damageAwareItem) {
			damageAwareItem.onItemEntityDamaged(source, amount, (ItemEntity) (Object) this);
		}
		
	}
	
	private void doAnvilCrafting(float damageAmount) {
		ItemEntity thisEntity = (ItemEntity) (Object) this;
		ItemStack thisItemStack = thisEntity.getStack();
		World world = thisEntity.getEntityWorld();
		
		if (autoCompactingInventory == null) {
			autoCompactingInventory = new AutoCompactingInventory();
		}
		autoCompactingInventory.setCompacting(AutoCompactingInventory.AutoCraftingMode.OneXOne, thisItemStack);
		Optional<AnvilCrushingRecipe> optionalAnvilCrushingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.ANVIL_CRUSHING, autoCompactingInventory, world);
		if (optionalAnvilCrushingRecipe.isPresent()) {
			// Item can be crafted via anvil. Do anvil crafting
			AnvilCrushingRecipe recipe = optionalAnvilCrushingRecipe.get();
			
			int itemStackAmount = thisEntity.getStack().getCount();
			int crushingInputAmount = Math.min(itemStackAmount, (int) (recipe.getCrushedItemsPerPointOfDamage() * damageAmount));
			
			if (crushingInputAmount > 0) {
				Vec3d position = thisEntity.getPos();
				
				ItemStack crushingOutput = recipe.getOutput(world.getRegistryManager());
				crushingOutput.setCount(crushingOutput.getCount() * crushingInputAmount);
				
				// Remove the input amount from the source stack
				// Or the source stack altogether if it would be empty
				int remainingItemStackAmount = itemStackAmount - crushingInputAmount;
				if (remainingItemStackAmount > 0) {
					thisItemStack.setCount(remainingItemStackAmount);
				} else {
					thisEntity.remove(Entity.RemovalReason.DISCARDED);
				}
				
				// Spawn the resulting item stack in the world
				ItemEntity craftedEntity = new ItemEntity(world, position.x, position.y, position.z, crushingOutput);
				world.spawnEntity(craftedEntity);
				
				// Spawn XP depending on how much is crafted, but at least 1
				float craftingXPFloat = recipe.getExperience() * crushingInputAmount;
				int craftingXP = Support.getIntFromDecimalWithChance(craftingXPFloat, world.random);
				
				if (craftingXP > 0) {
					ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, position.x, position.y, position.z, craftingXP);
					world.spawnEntity(experienceOrbEntity);
				}
				
				// Play sound
				SoundEvent soundEvent = recipe.getSoundEvent();
				if (soundEvent != null) {
					float randomVolume = 1.0F + world.getRandom().nextFloat() * 0.2F;
					float randomPitch = 0.9F + world.getRandom().nextFloat() * 0.2F;
					world.playSound(null, position.x, position.y, position.z, soundEvent, SoundCategory.PLAYERS, randomVolume, randomPitch);
				}
				
				SpectrumS2CPacketSender.playParticleWithExactVelocity((ServerWorld) world, position, recipe.getParticleEffect(), recipe.getParticleCount(), Vec3d.ZERO);
			}
		}
	}
	
	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	private void isDamageProof(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		ItemStack itemStack = ((ItemEntity) (Object) this).getStack();
		
		if (itemStack != ItemStack.EMPTY && !source.isOf(DamageTypes.OUT_OF_WORLD)) {
			boolean isImmune = SpectrumItemStackDamageImmunities.isDamageImmune(itemStack, source);
			if (isImmune) {
				callbackInfoReturnable.setReturnValue(true);
			}
		}
	}
	
	@Inject(method = "isFireImmune()Z", at = @At("HEAD"), cancellable = true)
	private void isFireProof(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		ItemStack itemStack = ((ItemEntity) (Object) this).getStack();
		
		if (itemStack != ItemStack.EMPTY) {
			if (itemStack.isIn(SpectrumDamageSources.FIRE_IMMUNE_ITEMS)) {
				callbackInfoReturnable.setReturnValue(true);
			}
		}
	}
	
	@Inject(method = "tick()V", at = @At("TAIL"))
	public void doGravityEffects(CallbackInfo ci) {
		ItemEntity itemEntity = ((ItemEntity) (Object) this);
		Item item = itemEntity.getStack().getItem();
		if (item instanceof GravitableItem) {
			// if the stack is floating really high => delete it
			if (itemEntity.getPos().getY() > itemEntity.getEntityWorld().getTopY() + 200) {
				itemEntity.discard();
			} else {
				double mod = ((GravitableItem) item).getGravityModForItemEntity();
				itemEntity.addVelocity(0, mod, 0);
			}
		}
	}
	
}
