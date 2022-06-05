package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
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

public class BonemealingMobBlock extends MobBlock {
	
	public BonemealingMobBlock(Settings settings, ParticleEffect particleEffect) {
		super(settings, particleEffect);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText("block.spectrum.bonemealing_mob_block.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int startDirection = world.random.nextInt(4);
		for (int i = 0; i < 4; i++) {
			Direction currentDirection = Direction.fromHorizontal(startDirection + i);
			BlockPos offsetPos = blockPos.offset(currentDirection);
			BlockState offsetState = world.getBlockState(offsetPos);
			if (offsetState.getBlock() instanceof Fertilizable fertilizable) {
				if (fertilizable.isFertilizable(world, offsetPos, offsetState, false) && fertilizable.canGrow(world, world.random, offsetPos, offsetState)) {
					fertilizable.grow(world, world.getRandom(), offsetPos, offsetState);
					world.syncWorldEvent(1505, offsetPos, 0); // particles
					return true;
				}
			}
			
		}
		return true;
	}
	
}
