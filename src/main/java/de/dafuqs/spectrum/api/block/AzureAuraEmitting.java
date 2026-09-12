package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

/**
 * Blocks with this interface increase nearby azure auras.
 * They regularity need to call
 * `BlockAuraSoundInstance.addToExistingInstanceOrCreateNewOne(world, pos);`
 * clientside, like via `animateTick`, to add them to an azure aura
 */
public interface AzureAuraEmitting {
	
	default void onBreak(Level world, BlockPos pos) {
		ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_AURA, 1, false, Vec3.ZERO, 0, true, Vec3.atCenterOf(pos), new Vec3(0, 0.08D + world.getRandom().nextDouble() * 0.04, 0));
		ParticleHelper.playParticleAroundBlockSides(world, SpectrumParticleTypes.AZURE_MOTE_SMALL, pos, Direction.values(), 3, Vec3.ZERO);
		BlockAuraSoundInstance.removeFromSoundInstances(world, pos);
	}
	
}
