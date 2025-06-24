package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

public class LightPlacingModifier extends ParticleAddingModifier {
	
	public LightPlacingModifier(ExplosionModifierType type, ParticleOptions effect, int displayColor) {
		super(type, effect, displayColor);
	}
	
	@Override
	public void applyToBlocks(@NotNull ServerLevel world, @NotNull Iterable<BlockPos> blocks) {
		BlockState lightState = SpectrumBlocks.PERSISTENT_LIGHT.defaultBlockState();
		for (BlockPos pos : blocks) {
			if (world.getRandom().nextInt(12) == 0 && world.getBlockState(pos).isAir()) {
				world.setBlockAndUpdate(pos, lightState);
			}
		}
	}
	
}
