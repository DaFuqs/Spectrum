package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class LightPlacingModifier extends ParticleAddingModifier {
	
	public LightPlacingModifier(ExplosionModifierType type, ParticleEffect effect, int displayColor) {
		super(type, effect, displayColor);
	}
	
	@Override
	public void applyToBlocks(@NotNull World world, @NotNull Iterable<BlockPos> blocks) {
		BlockState lightState = SpectrumBlocks.WAND_LIGHT_BLOCK.getDefaultState();
		for (BlockPos pos : blocks) {
			if (world.getRandom().nextInt(12) == 0 && world.getBlockState(pos).isAir()) {
				world.setBlockState(pos, lightState);
			}
		}
	}
	
}
