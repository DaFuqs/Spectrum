package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.PigmentBlocks;
import de.dafuqs.pigment.blocks.BedrockAnvilBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/block/AnvilBlock;getLandingState(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;", cancellable = true)
    private static void makeBedrockAnvilUnbreakable(BlockState fallingState, CallbackInfoReturnable<BlockState> callbackInfoReturnable) {
        if(fallingState.isOf(PigmentBlocks.BEDROCK_ANVIL)) {
            callbackInfoReturnable.setReturnValue(fallingState);
        }
    }
}
