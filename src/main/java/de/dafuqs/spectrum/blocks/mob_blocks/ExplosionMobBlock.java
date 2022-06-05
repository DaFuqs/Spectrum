package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExplosionMobBlock extends MobBlock {
	
	protected float power;
	protected boolean createFire;
	protected Explosion.DestructionType destructionType;
	
	public ExplosionMobBlock(Settings settings, ParticleEffect particleEffect, float power, boolean createFire, Explosion.DestructionType destructionType) {
		super(settings, particleEffect);
		this.power = power;
		this.createFire = createFire;
		this.destructionType = destructionType;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText("block.spectrum.explosion_mob_block.tooltip", power));
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
		
		public BlockPos sparedPos;
		
		public SpareBlockExplosionBehavior(BlockPos sparedPos) {
			this.sparedPos = sparedPos;
		}
		
		public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
			return !pos.equals(sparedPos) && super.canDestroyBlock(explosion, world, pos, state, power);
		}
	}
	
}
