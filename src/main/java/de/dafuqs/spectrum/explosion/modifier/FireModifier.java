package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FireModifier extends DamageChangingModifier {
	
	public FireModifier(ExplosionModifierType type, ParticleEffect effect, int displayColor) {
		super(type, effect, displayColor);
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
	public Optional<DamageSource> getDamageSource(@Nullable LivingEntity owner) {
		if (owner == null) {
			return Optional.empty();
		} else {
			return Optional.of(owner.getDamageSources().inFire());
		}
	}

	@Override
	public void applyToEntity(@NotNull Entity entity, double distance) {
		entity.setFireTicks(20);
	}
	
}
