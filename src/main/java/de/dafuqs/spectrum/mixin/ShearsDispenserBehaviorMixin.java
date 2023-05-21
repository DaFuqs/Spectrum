package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.dd_deco.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ShearsDispenserBehavior.class)
public class ShearsDispenserBehaviorMixin {

    @Inject(at = @At("HEAD"), method = "tryShearBlock(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
    private static void spectrum$shearsShearSawbladeHollyBushes(ServerWorld world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isOf(SpectrumBlocks.SAWBLADE_HOLLY_BUSH)) {
            int age = blockState.get(SawbladeHollyBushBlock.AGE);
            if (SawbladeHollyBushBlock.canBeSheared(age)) {
                // we do not have the real shears item used in the dispenser here, but for the default loot table that does not make much of a difference
                for (ItemStack stack : JadeVinePlantBlock.getHarvestedStacks(blockState, world, pos, world.getBlockEntity(pos), null, Items.SHEARS.getDefaultStack(), SawbladeHollyBushBlock.SHEARING_LOOT_TABLE_ID)) {
                    SawbladeHollyBushBlock.dropStack(world, pos, stack);
                }
    
                BlockState newState = blockState.with(SawbladeHollyBushBlock.AGE, age - 1);
                world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
                world.emitGameEvent(null, GameEvent.SHEAR, pos);
                world.playSound(null, pos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
    
                cir.setReturnValue(true);
            }
        }
    }

}
