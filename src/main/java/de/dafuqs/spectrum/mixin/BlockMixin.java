package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

// the use of mixin extras @ModifyReturnValue ensues mods end up compatible when mods use it
@Mixin(Block.class)
public abstract class BlockMixin {
	
	@Unique
	@Nullable PlayerEntity spectrum$breakingPlayer;
	
	@ModifyReturnValue(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
	private static List<ItemStack> spectrum$getDroppedStacks(List<ItemStack> original, BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack) {
		List<ItemStack> droppedStacks = original;
		Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.get(stack);
		
		// Voiding curse: no drops
		if (enchantmentMap.containsKey(SpectrumEnchantments.VOIDING)) {
			world.spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.05);
			droppedStacks.clear();
			return droppedStacks;
		}
		
		// Resonance: drop self or modify drops for some items
		if (enchantmentMap.containsKey(SpectrumEnchantments.RESONANCE) && SpectrumEnchantments.RESONANCE.canEntityUse(entity)) {
			ResonanceDropsDataLoader.applyResonance(state, blockEntity, droppedStacks);
		}
		
		if (!droppedStacks.isEmpty()) {
			// Foundry enchant: try smelting recipe for each stack
			if (enchantmentMap.containsKey(SpectrumEnchantments.FOUNDRY) && SpectrumEnchantments.FOUNDRY.canEntityUse(entity)) {
				droppedStacks = FoundryEnchantment.applyFoundry(world, droppedStacks);
			}
			
			// Inventory Insertion enchant? Add it to players inventory if there is room
			if (enchantmentMap.containsKey(SpectrumEnchantments.INVENTORY_INSERTION) && SpectrumEnchantments.INVENTORY_INSERTION.canEntityUse(entity)) {
				List<ItemStack> leftoverReturnStacks = new ArrayList<>();
				
				if (entity instanceof PlayerEntity playerEntity) {
					boolean anyAdded = false;
					for (ItemStack itemStack : droppedStacks) {
						Item item = itemStack.getItem();
						int count = itemStack.getCount();
						
						if (playerEntity.getInventory().insertStack(itemStack)) {
							anyAdded = true;
							if (itemStack.isEmpty()) {
								itemStack.setCount(count);
							}
							playerEntity.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), count);
						} else {
							leftoverReturnStacks.add(itemStack);
						}
					}
					if(anyAdded) {
						playerEntity.getWorld().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
								SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
								0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
					}
				}
				droppedStacks = leftoverReturnStacks;
			}
		}
		
		return droppedStacks;
	}
	
	@ModifyArg(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"), index = 2)
	private int spectrum$applyExuberance(int originalXP) {
		if (spectrum$breakingPlayer == null) {
			return originalXP;
		}
		return (int) (originalXP * ExuberanceEnchantment.getExuberanceMod(spectrum$breakingPlayer));
	}
	
	@Inject(method = "afterBreak", at = @At("HEAD"))
	public void spectrum$afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack, CallbackInfo callbackInfo) {
		spectrum$breakingPlayer = player;
	}
	
}
