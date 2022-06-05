package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KnockbackMobBlock extends MobBlock {
	
	protected float horizontalKnockback;
	protected float verticalKnockback;
	
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
		tooltip.add(new TranslatableText("block.spectrum.knockback_mob_block.tooltip"));
	}
	
}
