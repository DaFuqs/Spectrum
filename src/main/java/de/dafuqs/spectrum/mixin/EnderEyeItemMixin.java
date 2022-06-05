package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.CrackedEndPortalFrameBlock;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {
	
	@Inject(method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
	public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		boolean eyeAdded = false;
		if (blockState.isOf(SpectrumBlocks.CRACKED_END_PORTAL_FRAME) && blockState.get(CrackedEndPortalFrameBlock.EYE_TYPE).equals(CrackedEndPortalFrameBlock.EndPortalFrameEye.NONE)) {
			BlockState targetBlockState = blockState.with(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_EYE_OF_ENDER);
			Block.pushEntitiesUpBeforeBlockChange(blockState, targetBlockState, world, blockPos);
			world.setBlockState(blockPos, targetBlockState, 2);
			world.updateComparators(blockPos, SpectrumBlocks.CRACKED_END_PORTAL_FRAME);
			eyeAdded = true;
		} else if (blockState.isOf(Blocks.END_PORTAL_FRAME) && blockState.get(EndPortalFrameBlock.EYE).equals(false)) {
			BlockState targetBlockState = blockState.with(EndPortalFrameBlock.EYE, true);
			Block.pushEntitiesUpBeforeBlockChange(blockState, targetBlockState, world, blockPos);
			world.setBlockState(blockPos, targetBlockState, 2);
			world.updateComparators(blockPos, Blocks.END_PORTAL_FRAME);
			eyeAdded = true;
		}
		
		if (eyeAdded) {
			if (world.isClient) {
				callbackInfoReturnable.setReturnValue(ActionResult.SUCCESS);
			} else {
				context.getStack().decrement(1);
				world.syncWorldEvent(1503, blockPos, 0);
				
				// Search for a valid end portal position. Found => create portal!
				CrackedEndPortalFrameBlock.checkAndFillEndPortal(world, blockPos);
				
				callbackInfoReturnable.setReturnValue(ActionResult.CONSUME);
			}
		}
	}
	
}
