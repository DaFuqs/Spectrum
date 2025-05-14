package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeItemMixin {
	
	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	public void useOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> callbackInfoReturnable) {
		Level world = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		BlockState blockState = world.getBlockState(blockPos);
		boolean eyeAdded = false;
		if (blockState.is(SpectrumBlocks.CRACKED_END_PORTAL_FRAME) && blockState.getValue(CrackedEndPortalFrameBlock.EYE_TYPE).equals(CrackedEndPortalFrameBlock.EndPortalFrameEye.NONE)) {
			BlockState targetBlockState = blockState.setValue(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.WITH_EYE_OF_ENDER);
			Block.pushEntitiesUp(blockState, targetBlockState, world, blockPos);
			world.setBlock(blockPos, targetBlockState, 2);
			world.updateNeighbourForOutputSignal(blockPos, SpectrumBlocks.CRACKED_END_PORTAL_FRAME);
			eyeAdded = true;
		} else if (blockState.is(Blocks.END_PORTAL_FRAME) && blockState.getValue(EndPortalFrameBlock.HAS_EYE).equals(false)) {
			BlockState targetBlockState = blockState.setValue(EndPortalFrameBlock.HAS_EYE, true);
			Block.pushEntitiesUp(blockState, targetBlockState, world, blockPos);
			world.setBlock(blockPos, targetBlockState, 2);
			world.updateNeighbourForOutputSignal(blockPos, Blocks.END_PORTAL_FRAME);
			eyeAdded = true;
		}
		
		if (eyeAdded) {
			if (world.isClientSide) {
				callbackInfoReturnable.setReturnValue(InteractionResult.SUCCESS);
			} else {
				context.getItemInHand().shrink(1);
				world.levelEvent(LevelEvent.END_PORTAL_FRAME_FILL, blockPos, 0);
				
				// Search for a valid end portal position. Found => create portal!
				CrackedEndPortalFrameBlock.checkAndFillEndPortal(world, blockPos);
				
				callbackInfoReturnable.setReturnValue(InteractionResult.CONSUME);
			}
		}
	}
	
}
