package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.blocks.FluidLogging;
import de.dafuqs.spectrum.mixin.accessors.BucketItemAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(BucketItem.class)
public class BucketItemMixin {
    @Unique
    BucketItemAccessor thisObject = (BucketItemAccessor) this;

    @Inject(method = "placeFluid(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/hit/BlockHitResult;)Z",
            at = @At(value="FIELD", target= "Lnet/minecraft/world/World;isClient:Z", ordinal=0), cancellable = true)
    private void spectrum$BucketItemPlaceFluids(PlayerEntity player, World world, BlockPos pos, BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir)
    {

        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof FluidLogging.SpectrumFluidFillable && ((FluidLogging.SpectrumFluidFillable) block).canFillWithFluid(world,pos,world.getBlockState(pos),thisObject.getFluid())) {
            ((FluidLogging.SpectrumFluidFillable) block).tryFillWithFluid(world, pos, world.getBlockState(pos), ((FlowableFluid) thisObject.getFluid()).getStill(false));
            thisObject.callPlayEmptyingSound(player, world, pos);
            cir.setReturnValue(true);
        }
    }
    @ModifyVariable(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at= @At(value="STORE", ordinal= 0), ordinal = 2)
    private BlockPos spectrum$BucketItemPlacementPos(BlockPos blockPos3, World world, PlayerEntity user, Hand hand, @Local(ordinal=0) BlockPos blockPos)
    {
        BlockState blockState = world.getBlockState(blockPos);
        BlockPos newPos = blockPos3;
        if(blockPos3!=blockPos)
        {
            newPos = (blockState.getBlock() instanceof FluidLogging.SpectrumFluidFillable && ((FluidLogging.SpectrumFluidFillable) blockState.getBlock()).canFillWithFluid(world,blockPos,blockState,thisObject.getFluid())) ? blockPos : blockPos3;
        }
        return newPos;
    }

}
