package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.end_portal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class PerturbedEyeItem extends Item {
	
	public PerturbedEyeItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.is(Blocks.END_PORTAL_FRAME) || blockState.is(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)) {
			if (world.isClientSide) {
				return InteractionResult.SUCCESS;
			} else {
				BlockState targetBlockState;
				boolean facingVertical;
				if (blockState.is(Blocks.END_PORTAL_FRAME)) {
					Direction direction = blockState.getValue(EndPortalFrameBlock.FACING);
					facingVertical = direction.equals(Direction.EAST) || direction.equals(Direction.WEST);
					targetBlockState = SpectrumBlocks.CRACKED_END_PORTAL_FRAME.defaultBlockState()
							.setValue(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.VANILLA_WITH_PERTURBED_EYE)
							.setValue(CrackedEndPortalFrameBlock.FACING_VERTICAL, facingVertical);
				} else {
					facingVertical = blockState.getValue(CrackedEndPortalFrameBlock.FACING_VERTICAL);
					targetBlockState = SpectrumBlocks.CRACKED_END_PORTAL_FRAME.defaultBlockState()
							.setValue(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_PERTURBED_EYE)
							.setValue(CrackedEndPortalFrameBlock.FACING_VERTICAL, facingVertical);
				}
				
				Block.pushEntitiesUp(blockState, targetBlockState, world, blockPos);
				world.setBlock(blockPos, targetBlockState, 2);
				world.updateNeighbourForOutputSignal(blockPos, Blocks.END_PORTAL_FRAME);
				context.getItemInHand().shrink(1);
				world.levelEvent(LevelEvent.END_PORTAL_FRAME_FILL, blockPos, 0);
				
				return InteractionResult.CONSUME;
			}
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.perturbed_eye.tooltip").withStyle(ChatFormatting.GRAY));
	}

}
