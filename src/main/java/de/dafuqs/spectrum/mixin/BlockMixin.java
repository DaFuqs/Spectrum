package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Block.class)
public abstract class BlockMixin {
	
	@Unique
	@Nullable Player spectrum$breakingPlayer;
	
	@ModifyReturnValue(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
	private static List<ItemStack> spectrum$getDroppedStacks(List<ItemStack> original, BlockState state, ServerLevel world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack) {
		List<ItemStack> droppedStacks = original;
		
		// Voiding curse: no drops
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.NO_BLOCK_DROPS)) {
			world.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.05);
			droppedStacks.clear();
			return droppedStacks;
		}
		
		// Resonance: drop self or modify drops for some items
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.RESONANT_BLOCK_DROPS)) {
			ResonanceProcessor.applyResonance(world.registryAccess(), state, blockEntity, droppedStacks);
		}
		
		if (!droppedStacks.isEmpty()) {
			// Foundry enchant: try smelting recipe for each stack
			if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.SMELTS_MORE_LOOT)) {
				droppedStacks = FoundryHelper.applyFoundry(world, droppedStacks);
			}
			
			// Inventory insertion enchant: add it to the player's inventory if there is room
			if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.INVENTORY_INSERTION_EFFECT)) {
				List<ItemStack> leftoverReturnStacks = new ArrayList<>();
				
				if (entity instanceof Player playerEntity) {
					boolean anyAdded = false;
					for (ItemStack itemStack : droppedStacks) {
						Item item = itemStack.getItem();
						int count = itemStack.getCount();
						
						if (playerEntity.getInventory().add(itemStack)) {
							anyAdded = true;
							if (itemStack.isEmpty()) {
								itemStack.setCount(count);
							}
							playerEntity.awardStat(Stats.ITEM_PICKED_UP.get(item), count);
						} else {
							leftoverReturnStacks.add(itemStack);
						}
					}
					if (anyAdded) {
						playerEntity.level().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
								SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
								0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
					}
				}
				droppedStacks = leftoverReturnStacks;
			}
		}
		
		return droppedStacks;
	}
	
	@ModifyArg(method = "popExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"), index = 2)
	private int spectrum$applyExuberance(int originalXP) {
		if (spectrum$breakingPlayer == null) {
			return originalXP;
		}
		return (int) (originalXP * ExuberanceHelper.getExuberanceMod(spectrum$breakingPlayer));
	}
	
	@Inject(method = "playerDestroy", at = @At("HEAD"))
	public void spectrum$afterBreak(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack, CallbackInfo callbackInfo) {
		spectrum$breakingPlayer = player;
	}
	
}
