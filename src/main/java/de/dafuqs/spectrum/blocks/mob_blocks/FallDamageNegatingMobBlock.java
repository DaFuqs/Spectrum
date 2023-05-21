package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FallDamageNegatingMobBlock extends MobBlock {
	
	public FallDamageNegatingMobBlock(Settings settings, ParticleEffect particleEffect) {
		super(settings, particleEffect);
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity != null && entity.getVelocity().getY() < -0.01) {
			entity.setVelocity(0, 0.5, 0); // makes it feel bouncy
			entity.velocityModified = true;
			entity.velocityDirty = true;
			entity.fallDistance = 0;
			return true;
		}
		return false;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.fall_damage_negating_mob_block.tooltip"));
		tooltip.add(Text.translatable("block.spectrum.fall_damage_negating_mob_block.tooltip2"));
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!hasCooldown(state) && fallDistance > 3F) {
			entity.handleFallDamage(fallDistance, 0.0F, DamageSource.FALL);
			if (!world.isClient) {
				playTriggerParticles((ServerWorld) world, pos);
				playTriggerSound(world, pos);
				triggerCooldown(world, pos);
			}
		}
	}
	
}
