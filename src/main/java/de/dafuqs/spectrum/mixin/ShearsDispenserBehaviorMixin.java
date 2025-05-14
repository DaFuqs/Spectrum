package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.deeper_down.flora.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ShearsDispenseItemBehavior.class)
public class ShearsDispenserBehaviorMixin {
	
	@Inject(at = @At("HEAD"), method = "tryShearBeehive", cancellable = true)
	private static void spectrum$shearsShearSawbladeHollyBushes(ServerLevel world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos);
		if (blockState.is(SpectrumBlocks.SAWBLADE_HOLLY_BUSH)) {
			int age = blockState.getValue(SawbladeHollyBushBlock.AGE);
            if (SawbladeHollyBushBlock.canBeSheared(age)) {
                // we do not have the real shears item used in the dispenser here, but for the default loot table that does not make much of a difference
				for (ItemStack stack : JadeVinePlantBlock.getHarvestedStacks(blockState, world, pos, world.getBlockEntity(pos), null, Items.SHEARS.getDefaultInstance(), SpectrumLootTables.SAWBLADE_HOLLY_SHEARING)) {
					SawbladeHollyBushBlock.popResource(world, pos, stack);
                }
				
				BlockState newState = blockState.setValue(SawbladeHollyBushBlock.AGE, age - 1);
				world.setBlock(pos, newState, Block.UPDATE_CLIENTS);
				world.gameEvent(null, GameEvent.SHEAR, pos);
				world.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
    
                cir.setReturnValue(true);
            }
        }
    }

}
