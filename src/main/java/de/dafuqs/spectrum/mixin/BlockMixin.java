package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.dafuqs.spectrum.data_loaders.ResonanceDropsDataLoader;
import de.dafuqs.spectrum.enchantments.FoundryEnchantment;
import de.dafuqs.spectrum.enchantments.ExuberanceEnchantment;
import de.dafuqs.spectrum.enchantments.ResonanceEnchantment;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InfestedBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// increased priority, cause https://github.com/Luligabi1/Incantationem/blob/b87d864cba60601c78c70c9999b6df37cce9fd03/src/main/java/me/luligabi/incantationem/mixin/BlockMixin.java#L60 would cancel the spectrum$getDroppedStacks call
// the use of @ModifyReturnValue ensues both end up compatible as soon as both mods use it
@Mixin(value = Block.class, priority = 999)
public abstract class BlockMixin {
	
	PlayerEntity spectrum$breakingPlayer;
	
	@ModifyReturnValue(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
	private static List<ItemStack> spectrum$getDroppedStacks(List<ItemStack> original, BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack) {
		List<ItemStack> droppedStacks = original;
		Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.get(stack);
		
		// Voiding curse: no drops
		if (enchantmentMap.containsKey(SpectrumEnchantments.VOIDING)) {
			world.spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.05);
			droppedStacks.clear();
		// Resonance: drop itself
		} else if(enchantmentMap.containsKey(SpectrumEnchantments.RESONANCE) && (state.isIn(SpectrumBlockTags.RESONANCE_HARVESTABLES) || state.getBlock() instanceof InfestedBlock) && SpectrumEnchantments.RESONANCE.canEntityUse(entity)) {
			droppedStacks.clear();
			droppedStacks.add(state.getBlock().asItem().getDefaultStack());
		}
		
		if (droppedStacks.size() > 0) {
			// Resonance enchant: grant different drops for some items
			if(enchantmentMap.containsKey(SpectrumEnchantments.RESONANCE) && SpectrumEnchantments.RESONANCE.canEntityUse(entity)) {
				for(int i = 0; i < droppedStacks.size(); i++) {
					droppedStacks.set(i, ResonanceDropsDataLoader.applyResonance(droppedStacks.get(i)));
				}
			}
			
			// Foundry enchant: try smelting recipe for each stack
			if (enchantmentMap.containsKey(SpectrumEnchantments.FOUNDRY) && SpectrumEnchantments.FOUNDRY.canEntityUse(entity)) {
				droppedStacks = FoundryEnchantment.applyAutoSmelt(world, droppedStacks);
			}
			
			// Inventory Insertion enchant? Add it to players inventory if there is room
			if (enchantmentMap.containsKey(SpectrumEnchantments.INVENTORY_INSERTION) && SpectrumEnchantments.INVENTORY_INSERTION.canEntityUse(entity)) {
				List<ItemStack> leftoverReturnStacks = new ArrayList<>();
				
				if (entity instanceof PlayerEntity playerEntity) {
					for (ItemStack itemStack : droppedStacks) {
						Item item = itemStack.getItem();
						int count = itemStack.getCount();
						
						if (playerEntity.getInventory().insertStack(itemStack)) {
							if (itemStack.isEmpty()) {
								itemStack.setCount(count);
							}
							playerEntity.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), count);
						} else {
							leftoverReturnStacks.add(itemStack);
						}
					}
				}
				droppedStacks = leftoverReturnStacks;
			}
		}
		
		return droppedStacks;
	}
	
	@Inject(method = "afterBreak", at = @At("HEAD"))
	private void spectrum$saveBreakingPlayerReference(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
		spectrum$breakingPlayer = player;
	}
	
	@ModifyArg(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"), index = 2)
	private int spectrum$applyExuberance(int originalXP) {
		return (int) (originalXP * ExuberanceEnchantment.getExuberanceMod(spectrum$breakingPlayer));
	}
	
	@Inject(method = "afterBreak", at = @At("HEAD"), cancellable = true)
	public void spectrum$afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack, CallbackInfo callbackInfo) {
		if (ResonanceEnchantment.checkResonanceForSpawnerMining(world, pos, state, blockEntity, stack) && SpectrumEnchantments.RESONANCE.canEntityUse(player)) {
			callbackInfo.cancel();
		}
	}
	
}
