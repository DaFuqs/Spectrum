package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FireModifier extends DamageChangingModifier {
	
	public FireModifier(ExplosionModifierType type, ParticleOptions effect, int displayColor) {
		super(type, effect, displayColor);
	}
	
	@Override
	public void applyToBlocks(@NotNull ServerLevel world, @NotNull Iterable<BlockPos> blocks) {
		for (BlockPos pos : blocks) {
			if (world.getRandom().nextInt(3) == 0 && world.getBlockState(pos).isAir() && world.getBlockState(pos.below()).isCollisionShapeFullBlock(world, pos.below())) {
				world.setBlockAndUpdate(pos, FireBlock.getState(world, pos));
			}
		}
		super.applyToBlocks(world, blocks);
	}
	
	@Override
	public Optional<DamageSource> getDamageSource(@Nullable LivingEntity owner) {
		if (owner == null) {
			return Optional.empty();
		} else {
			return Optional.of(owner.damageSources().inFire());
		}
	}
	
	@Override
	public void applyToEntity(@NotNull Entity entity, double distance) {
		entity.setRemainingFireTicks(20);
	}
	
}
