package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.PigmentBlocks;
import de.dafuqs.pigment.blocks.CrackedEndPortalFrameBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
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

    @Inject(method = "Lnet/minecraft/item/EnderEyeItem;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(PigmentBlocks.CRACKED_END_PORTAL_FRAME) && blockState.get(CrackedEndPortalFrameBlock.EYE_TYPE).equals(CrackedEndPortalFrameBlock.EndPortalFrameEye.NONE)) {
            if (world.isClient) {
                callbackInfoReturnable.setReturnValue(ActionResult.SUCCESS);
            } else {
                BlockState targetBlockState = blockState.with(CrackedEndPortalFrameBlock.EYE_TYPE, CrackedEndPortalFrameBlock.EndPortalFrameEye.EYE_OF_ENDER);
                Block.pushEntitiesUpBeforeBlockChange(blockState, targetBlockState, world, blockPos);
                world.setBlockState(blockPos, targetBlockState, 2);
                world.updateComparators(blockPos, PigmentBlocks.CRACKED_END_PORTAL_FRAME);
                context.getStack().decrement(1);
                world.syncWorldEvent(1503, blockPos, 0);
                BlockPattern.Result result = CrackedEndPortalFrameBlock.getCompletedFramePattern().searchAround(world, blockPos);
                if (result != null) {
                    // since the custom portal does not have
                    // fixed directions we can estimate the
                    // portal position based on some simple checks instead
                    BlockPos portalTopLeft = result.getFrontTopLeft().add(-3, 0, -3);
                    if(world.getBlockState(portalTopLeft.add(7, 0, 0)).getBlock().equals(PigmentBlocks.CRACKED_END_PORTAL_FRAME)) {
                        portalTopLeft = portalTopLeft.add(4, 0, 0);
                    } else if(world.getBlockState(portalTopLeft.add(0, 0, 7)).getBlock().equals(PigmentBlocks.CRACKED_END_PORTAL_FRAME)) {
                        portalTopLeft = portalTopLeft.add(0, 0, 4);
                    }

                    for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; ++j) {
                            world.setBlockState(portalTopLeft.add(i, 0, j), Blocks.END_PORTAL.getDefaultState(), 2);
                        }
                    }

                    world.syncGlobalEvent(1038, portalTopLeft.add(1, 0, 1), 0);
                }

                callbackInfoReturnable.setReturnValue(ActionResult.CONSUME);
            }
        }
    }

}
