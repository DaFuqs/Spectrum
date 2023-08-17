package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.explosion.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.particle.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PrimordialFireModifier extends DamageChangingModifier {
	
	
	public PrimordialFireModifier(Identifier id, ExplosionModifierType type, DamageSource damageSource, ParticleEffect effect, int displayColor) {
		super(id, type, damageSource, effect, displayColor);
	}
	
	@Override
	public void applyToBlocks(@NotNull World world, @NotNull List<BlockPos> blocks) {
		for (BlockPos pos : blocks) {
			if (world.getRandom().nextInt(3) == 0 && world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isOpaqueFullCube(world, pos.down())) {
				world.setBlockState(pos, PrimordialFireBlock.getState(world, pos));
			}
		}
		super.applyToBlocks(world, blocks);
	}
	
	@Override
	public void applyToEntities(@NotNull List<Entity> entity) {
		for (Entity affected : entity) {
			if (affected instanceof LivingEntity livingEntity)
				OnPrimordialFireComponent.addPrimordialFireTicks(livingEntity, 10);
		}
	}
	
	@Override
	public float getBlastRadiusModifier() {
		return 1.25F;
	}
}
