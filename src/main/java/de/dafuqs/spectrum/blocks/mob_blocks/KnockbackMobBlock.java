package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class KnockbackMobBlock extends MobBlock {
	
	protected final float horizontalKnockback;
	protected final float verticalKnockback;
	
	public KnockbackMobBlock(Settings settings, ParticleEffect particleEffect, float horizontalKnockback, float verticalKnockback) {
		super(settings, particleEffect);
		this.horizontalKnockback = horizontalKnockback;
		this.verticalKnockback = verticalKnockback;
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity != null) {
			switch (side) {
				case NORTH -> {
					entity.addVelocity(0, verticalKnockback, -horizontalKnockback);
					entity.velocityModified = true;
				}
				case EAST -> {
					entity.addVelocity(horizontalKnockback, verticalKnockback, 0);
					entity.velocityModified = true;
				}
				case SOUTH -> {
					entity.addVelocity(0, verticalKnockback, horizontalKnockback);
					entity.velocityModified = true;
				}
				case WEST -> {
					entity.addVelocity(-horizontalKnockback, verticalKnockback, 0);
					entity.velocityModified = true;
				}
				case UP -> {
					entity.addVelocity(0, (horizontalKnockback / 4), 0);
					entity.velocityModified = true;
				}
				default -> {
					entity.addVelocity(0, -(horizontalKnockback / 4), 0);
					entity.velocityModified = true;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.knockback_mob_block.tooltip"));
	}
	
}
