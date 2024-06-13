package de.dafuqs.spectrum.recipe.anvil_crushing;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class AnvilCrusher {
	
	private static final Inventory INVENTORY = new SimpleInventory(1);
	
	public static void crush(ItemEntity itemEntity, float damageAmount) {
		ItemStack thisItemStack = itemEntity.getStack();
		World world = itemEntity.getWorld();
		
		INVENTORY.setStack(0, thisItemStack);
		
		Optional<AnvilCrushingRecipe> optionalAnvilCrushingRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.ANVIL_CRUSHING, INVENTORY, world);
		if (optionalAnvilCrushingRecipe.isPresent()) {
			// Item can be crafted via anvil. Do anvil crafting
			AnvilCrushingRecipe recipe = optionalAnvilCrushingRecipe.get();
			
			int itemStackAmount = itemEntity.getStack().getCount();
			int crushingInputAmount = Math.min(itemStackAmount, (int) (recipe.getCrushedItemsPerPointOfDamage() * damageAmount));
			
			if (crushingInputAmount > 0) {
				Vec3d position = itemEntity.getPos();

				ItemStack crushingOutput = recipe.getOutput(world.getRegistryManager()).copy();
				crushingOutput.setCount(crushingOutput.getCount() * crushingInputAmount);
				
				// Remove the input amount from the source stack
				// Or the source stack altogether if it would be empty
				int remainingItemStackAmount = itemStackAmount - crushingInputAmount;
				if (remainingItemStackAmount > 0) {
					thisItemStack.setCount(remainingItemStackAmount);
				} else {
					itemEntity.remove(Entity.RemovalReason.DISCARDED);
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
	
}
