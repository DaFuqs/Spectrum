package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.enchantments.AutoSmeltEnchantment;
import de.dafuqs.spectrum.enchantments.ExuberanceEnchantment;
import de.dafuqs.spectrum.enchantments.ResonanceEnchantment;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(Block.class)
public abstract class BlockMixin {
	
	PlayerEntity spectrum$breakingPlayer;
	
	@Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
	private static void spectrum$getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
		List<ItemStack> returnStacks = cir.getReturnValue();
		if (returnStacks.size() > 0) {
			Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.get(stack);
			
			// Voiding curse: no drops
			if (enchantmentMap.containsKey(SpectrumEnchantments.VOIDING)) {
				world.spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.05);
				cir.setReturnValue(new ArrayList<>());
			} else {
				// Foundry enchant: try smelting recipe for each stack
				if (enchantmentMap.containsKey(SpectrumEnchantments.FOUNDRY) && SpectrumEnchantments.FOUNDRY.canEntityUse(entity)) {
					returnStacks = AutoSmeltEnchantment.applyAutoSmelt(world, returnStacks);
				}
				// Inventory Insertion enchant? Add it to players inventory if there is room
				if (enchantmentMap.containsKey(SpectrumEnchantments.INVENTORY_INSERTION) && SpectrumEnchantments.INVENTORY_INSERTION.canEntityUse(entity)) {
					List<ItemStack> leftoverReturnStacks = new ArrayList<>();
					
					if (entity instanceof PlayerEntity playerEntity) {
						for (ItemStack itemStack : returnStacks) {
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
					
					returnStacks = leftoverReturnStacks;
				}
				cir.setReturnValue(returnStacks);
			}
		}
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
