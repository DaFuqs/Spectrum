package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.GravitableItem;
import de.dafuqs.spectrum.inventories.AutoCompactingInventory;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumItemStackDamageImmunities;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	
	private static AutoCompactingInventory autoCompactingInventory;
	
	@Shadow
	public abstract ItemStack getStack();
	
	@Shadow
	public abstract void setNeverDespawn();
	
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
			int worldMinY = thisItemEntity.world.getDimension().getMinimumY();
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
		if (DamageSource.ANVIL.equals(source) || SpectrumDamageSources.FLOATBLOCK.equals(source)) {
			doAnvilCrafting(amount);
			
			// prevent the source itemStack taking damage.
			// ItemEntities have a health of 5 and can actually get killed by a falling anvil
			callbackInfoReturnable.setReturnValue(true);
		}
		
		if (amount > 1 && source.isExplosive() && this.getStack().isOf(SpectrumItems.LIGHTNING_STONE)) {
			doLightningExplosion();
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
		Optional<AnvilCrushingRecipe> optionalAnvilCrushingRecipe = SpectrumCommon.minecraftServer.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.ANVIL_CRUSHING, autoCompactingInventory, world);
		if (optionalAnvilCrushingRecipe.isPresent()) {
			// Item can be crafted via anvil. Do anvil crafting
			AnvilCrushingRecipe recipe = optionalAnvilCrushingRecipe.get();
			
			int itemStackAmount = thisEntity.getStack().getCount();
			int crushingInputAmount = Math.min(itemStackAmount, (int) (recipe.getCrushedItemsPerPointOfDamage() * damageAmount));
			
			if (crushingInputAmount > 0) {
				Vec3d position = thisEntity.getPos();
				
				ItemStack crushingOutput = recipe.getOutput();
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
				
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, new BlockPos(position), recipe.getParticleEffectIdentifier(), recipe.getParticleCount());
			}
		}
	}
	
	private void doLightningExplosion() {
		ItemEntity thisEntity = (ItemEntity) (Object) this;
		ItemStack thisItemStack = thisEntity.getStack();
		World world = thisEntity.getEntityWorld();
		
		BlockPos blockPos = thisEntity.getBlockPos();
		Vec3d pos = thisEntity.getPos();
		int count = thisItemStack.getCount();
		
		// remove it before dealing damage, otherwise it would cause a stack overflow
		thisEntity.remove(Entity.RemovalReason.KILLED);
		
		// strike lightning...
		if (world.isSkyVisible(thisEntity.getBlockPos())) {
			LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
			if (lightningEntity != null) {
				lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
				world.spawnEntity(lightningEntity);
			}
		}
		
		// ...and boom!
		float powerMod = 1.0F;
		Biome biomeAtPos = world.getBiome(blockPos).value();
		if (!biomeAtPos.isHot(blockPos) && !biomeAtPos.isCold(blockPos)) {
			// there is no rain/thunder in deserts or snowy biomes
			powerMod = world.isThundering() ? 1.5F : world.isRaining() ? 1.25F : 1.0F;
		}
		
		world.createExplosion(thisEntity, pos.getX(), pos.getY(), pos.getZ(), count * powerMod, Explosion.DestructionType.BREAK);
	}
	
	
	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	private void isDamageProof(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		ItemStack itemStack = ((ItemEntity) (Object) this).getStack();
		
		if (itemStack != ItemStack.EMPTY && source != DamageSource.OUT_OF_WORLD) {
			boolean isImmune = SpectrumItemStackDamageImmunities.isDamageImmune(itemStack, source);
			if (isImmune) {
				callbackInfoReturnable.setReturnValue(true);
			}
		}
	}
	
	@Inject(method = "Lnet/minecraft/entity/ItemEntity;isFireImmune()Z", at = @At("HEAD"), cancellable = true)
	private void isFireProof(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		ItemStack itemStack = ((ItemEntity) (Object) this).getStack();
		
		if (itemStack != ItemStack.EMPTY) {
			boolean isImmune = SpectrumItemStackDamageImmunities.isFireDamageImmune(itemStack);
			if (isImmune) {
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
