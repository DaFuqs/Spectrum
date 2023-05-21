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
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ExplosionMobBlock extends MobBlock {
	
	protected final float power;
	protected final boolean createFire;
	protected final Explosion.DestructionType destructionType;
	
	public ExplosionMobBlock(Settings settings, ParticleEffect particleEffect, float power, boolean createFire, Explosion.DestructionType destructionType) {
		super(settings, particleEffect);
		this.power = power;
		this.createFire = createFire;
		this.destructionType = destructionType;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.explosion_mob_block.tooltip", power));
	}
	
	@Override
	public boolean trigger(ServerWorld world, final BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		// why power + 1 you ask? Since the explosion happens inside the block, some explosion power
		// is blocked by this block itself, weakening it. So to better match the original value we have to make it a tad stronger
		world.createExplosion(null, DamageSource.explosion((Explosion) null), new SpareBlockExplosionBehavior(blockPos), blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, this.power + 1, this.createFire, Explosion.DestructionType.DESTROY);
		return true;
	}
	
	/**
	 * Overriding canDestroyBlock makes it so the mob block itself does not get destroyed
	 * Increasing its hardness would make the block immune to other explosions, too
	 * and would not let explosions happen from the center of it
	 */
	private static class SpareBlockExplosionBehavior extends ExplosionBehavior {
		
		public final BlockPos sparedPos;
		
		public SpareBlockExplosionBehavior(BlockPos sparedPos) {
			this.sparedPos = sparedPos;
		}
		
		@Override
		public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
			return !pos.equals(sparedPos) && super.canDestroyBlock(explosion, world, pos, state, power);
		}
	}
	
}
