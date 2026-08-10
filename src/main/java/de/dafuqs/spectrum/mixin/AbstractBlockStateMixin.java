package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
	
	@ModifyVariable(method = "spawnAfterBreak", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public boolean spectrum$preventXPDropsWhenUsingResonance(boolean dropExperience, ServerLevel world, BlockPos pos, ItemStack stack) {
		if (ResonanceProcessor.preventNextXPDrop && EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.RESONANT_BLOCK_DROPS)) {
			ResonanceProcessor.preventNextXPDrop = false;
			return false;
		}
		return dropExperience;
	}
	
}
