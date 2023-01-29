package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PerturbedEyeItem extends Item {

	public PerturbedEyeItem(Settings settings) {
		super(settings);
	}

	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.isOf(Blocks.END_PORTAL_FRAME) || blockState.isOf(SpectrumBlocks.CRACKED_END_PORTAL_FRAME)) {
			if (world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				BlockState targetBlockState;
				boolean facingVertical;
				if (blockState.isOf(Blocks.END_PORTAL_FRAME)) {
					Direction direction = blockState.get(EndPortalFrameBlock.FACING);
					facingVertical = direction.equals(Direction.EAST) || direction.equals(Direction.WEST);
					targetBlockState = SpectrumBlocks.CRACKED_END_PORTAL_FRAME.getDefaultState()
							.with(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.VANILLA_WITH_PERTURBED_EYE)
							.with(CrackedEndPortalFrameBlock.FACING_VERTICAL, facingVertical);
				} else {
					facingVertical = blockState.get(CrackedEndPortalFrameBlock.FACING_VERTICAL);
					targetBlockState = SpectrumBlocks.CRACKED_END_PORTAL_FRAME.getDefaultState()
							.with(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_PERTURBED_EYE)
							.with(CrackedEndPortalFrameBlock.FACING_VERTICAL, facingVertical);
				}

				Block.pushEntitiesUpBeforeBlockChange(blockState, targetBlockState, world, blockPos);
				world.setBlockState(blockPos, targetBlockState, 2);
				world.updateComparators(blockPos, Blocks.END_PORTAL_FRAME);
				context.getStack().decrement(1);
				world.syncWorldEvent(1503, blockPos, 0);

				return ActionResult.CONSUME;
			}
		} else {
			return ActionResult.PASS;
		}
	}

	public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.spectrum.perturbed_eye.tooltip").formatted(Formatting.GRAY));
	}

}
