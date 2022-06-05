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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
		tooltip.add(new TranslatableText("block.spectrum.fall_damage_negating_mob_block.tooltip"));
		tooltip.add(new TranslatableText("block.spectrum.fall_damage_negating_mob_block.tooltip2"));
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
