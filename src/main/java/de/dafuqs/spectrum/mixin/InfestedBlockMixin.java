package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(InfestedBlock.class)
public abstract class InfestedBlockMixin {
	
	/*
	 * Do not spawn silverfish when block is broken with Resonance Tool
	 * And Kill the Silverfish when using Pest Control, dropping XP
	 */
	@Inject(at = @At("HEAD"), method = "spawnAfterBreak", cancellable = true)
	public void onStacksDropped(BlockState state, ServerLevel world, BlockPos pos, ItemStack stack, boolean dropExperience, CallbackInfo ci) {
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.RESONANT_BLOCK_DROPS)) {
			ci.cancel();
		}
		
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.AUTO_KILLS_SILVERFISH)) {
			Silverfish silverfishEntity = EntityType.SILVERFISH.create(world);
			if (silverfishEntity != null) {
				SplinterspawnInfestedBlock.spawnEntityKillAndDropXP(world, pos, dropExperience, silverfishEntity);
			}
			ci.cancel();
		}
	}
	
}
