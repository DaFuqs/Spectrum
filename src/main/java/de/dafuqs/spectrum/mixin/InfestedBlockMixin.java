package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(InfestedBlock.class)
public abstract class InfestedBlockMixin {
	
	/*
	 * Do not spawn silverfish when block is broken with Resonance Tool
	 */
	@Inject(at = @At("HEAD"), method = "onStacksDropped(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Z)V", cancellable = true)
	public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience, CallbackInfo ci) {
		if (EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, stack) > 0) {
			ci.cancel();
		} else if (EnchantmentHelper.getLevel(SpectrumEnchantments.PEST_CONTROL, stack) > 0) {
			SilverfishEntity silverfishEntity = EntityType.SILVERFISH.create(world);
			silverfishEntity.refreshPositionAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
			world.spawnEntity(silverfishEntity);
			silverfishEntity.playSpawnEffects();
			silverfishEntity.kill();

			ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 10);
			world.spawnEntity(experienceOrbEntity);

			ci.cancel();
		}
	}
	
}
