package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeItemMixin {
	
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
