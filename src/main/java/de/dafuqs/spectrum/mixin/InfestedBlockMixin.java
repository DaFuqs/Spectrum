package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.InfestedBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InfestedBlock.class)
public class InfestedBlockMixin {
	
	/*
	 * Do not spawn silverfish when block is broken with Resonance Tool
	 */
	@Inject(at = @At("HEAD"), method = "onStacksDropped(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V", cancellable = true)
	public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
		if (EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, stack) > 0) {
			ci.cancel();
		} else if (EnchantmentHelper.getLevel(SpectrumEnchantments.PEST_CONTROL, stack) > 0) {
			SilverfishEntity silverfishEntity = EntityType.SILVERFISH.create(world);
			silverfishEntity.refreshPositionAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
			world.spawnEntity(silverfishEntity);
			silverfishEntity.playSpawnEffects();
			silverfishEntity.kill();
			ci.cancel();
		}
	}
	
}
