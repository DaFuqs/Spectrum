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
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (state.is(SpectrumBlockTags.C_END_PORTAL_FRAMES)) {
            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            } else {
                BlockState targetState;
                boolean facingVertical;
                if (state.is(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)) {
					facingVertical = state.getValue(CrackedEndPortalFrameBlock.FACING_VERTICAL);
					targetState = SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get().defaultBlockState()
							.setValue(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_PERTURBED_EYE)
							.setValue(CrackedEndPortalFrameBlock.FACING_VERTICAL, facingVertical);
                } else {
					Direction direction = state.hasProperty(EndPortalFrameBlock.FACING) ? state.getValue(EndPortalFrameBlock.FACING) : Direction.NORTH;
					facingVertical = direction.equals(Direction.EAST) || direction.equals(Direction.WEST);
					targetState = SpectrumBlocks.CRACKED_END_PORTAL_FRAME.get().defaultBlockState()
							.setValue(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.VANILLA_WITH_PERTURBED_EYE)
							.setValue(CrackedEndPortalFrameBlock.FACING_VERTICAL, facingVertical);
                }

                Block.pushEntitiesUp(state, targetState, level, pos);
                level.setBlock(pos, targetState, 2);
                level.updateNeighbourForOutputSignal(pos, Blocks.END_PORTAL_FRAME);
                context.getItemInHand().shrink(1);
                level.levelEvent(LevelEvent.END_PORTAL_FRAME_FILL, pos, 0);

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
