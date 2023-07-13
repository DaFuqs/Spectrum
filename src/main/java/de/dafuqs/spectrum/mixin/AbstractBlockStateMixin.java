package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
	
	@Shadow
	public abstract boolean isIn(TagKey<Block> tag);
	
	@Inject(at = @At("HEAD"), method = "onStacksDropped", cancellable = true)
	public void spectrum$preventSpawnerXPOnResonance(ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience, CallbackInfo ci) {
		if (this.isIn(SpectrumBlockTags.RESONANCE_HARVESTABLES) && EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, stack) > 0) {
			ci.cancel();
		}
	}
	
}
