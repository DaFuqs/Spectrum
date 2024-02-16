package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
	
	@ModifyVariable(method = "onStacksDropped", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public boolean spectrum$preventXPDropsWhenUsingResonance(boolean dropExperience, ServerWorld world, BlockPos pos, ItemStack stack) {
		if (ResonanceDropsDataLoader.preventNextXPDrop && EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, stack) > 0) {
			ResonanceDropsDataLoader.preventNextXPDrop = false;
			return false;
		}
		return dropExperience;
	}
	
}
