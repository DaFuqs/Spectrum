package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class FireModifier extends DamageChangingModifier {
	
	public FireModifier(ExplosionModifierType type, DamageSource damageSource, ParticleEffect effect, int displayColor) {
		super(type, damageSource, effect, displayColor);
	}
	
	@Override
	public void applyToBlocks(@NotNull World world, @NotNull Iterable<BlockPos> blocks) {
		for (BlockPos pos : blocks) {
			if (world.getRandom().nextInt(3) == 0 && world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isOpaqueFullCube(world, pos.down())) {
				world.setBlockState(pos, FireBlock.getState(world, pos));
			}
		}
		super.applyToBlocks(world, blocks);
	}
	
	@Override
	public void applyToEntity(@NotNull Entity entity, double distance) {
		entity.setFireTicks(20);
	}
	
}
