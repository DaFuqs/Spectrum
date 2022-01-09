package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.SpawnerItem;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class SpawnerBlockMixin {

	@Inject(method = "afterBreak", at = @At("HEAD"), cancellable = true)
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack, CallbackInfo callbackInfo) {
		if(SpectrumEnchantments.RESONANCE.canEntityUse(player) && checkResonanceForSpawnerMining(world, pos, state, blockEntity, stack) && SpectrumEnchantments.RESONANCE.canEntityUse(player)) {
			callbackInfo.cancel();
		}
	}

	private static boolean checkResonanceForSpawnerMining(World world, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack stack) {
		if (blockState.equals(Blocks.SPAWNER.getDefaultState())) {
			if (EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, stack) > 0) {
				if (blockEntity instanceof MobSpawnerBlockEntity mobSpawnerBlockEntity) {
					ItemStack itemStack = SpawnerItem.toItemStack(mobSpawnerBlockEntity);

					Block.dropStack(world, pos, itemStack);
					world.playSound(null, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
					return true;
				}
			}
		}
		return false;
	}

}