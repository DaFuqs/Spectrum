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

public class SilverfishInsertingMobBlock extends MobBlock {
	
	public SilverfishInsertingMobBlock(Settings settings, ParticleEffect particleEffect) {
		super(settings, particleEffect);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.silverfish_inserting_mob_block.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int startDirection = world.random.nextInt(4);
		for (int i = 0; i < 4; i++) {
			Direction currentDirection = Direction.fromHorizontal(startDirection + i);
			BlockPos offsetPos = blockPos.offset(currentDirection);
			BlockState offsetState = world.getBlockState(offsetPos);
			if (InfestedBlock.isInfestable(offsetState)) {
				BlockState infestedState = InfestedBlock.fromRegularState(offsetState);
				world.setBlockState(offsetPos, infestedState);
				world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, offsetPos, Block.getRawIdFromState(offsetState)); // processed in WorldRenderer processGlobalEvent()
				return true;
			}
		}
		
		return false;
	}
	
}
