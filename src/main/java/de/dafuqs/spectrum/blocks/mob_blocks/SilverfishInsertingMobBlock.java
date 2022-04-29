package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.InfestedBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SilverfishInsertingMobBlock extends MobBlock {
	
	public SilverfishInsertingMobBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.silverfish_inserting_mob_block.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		boolean anyConverted = false;
		for(Direction direction : Direction.values()) {
			BlockPos offsetPos = blockPos.offset(direction);
			BlockState offsetState = world.getBlockState(offsetPos);
			if(InfestedBlock.isInfestable(offsetState)) {
				BlockState infestedState = InfestedBlock.fromRegularState(offsetState);
				world.setBlockState(offsetPos, infestedState);
				world.addBlockBreakParticles(offsetPos, infestedState);
				anyConverted = true;
			}
		}
		return anyConverted;
	}
	
}
