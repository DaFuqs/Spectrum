package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

    float carriedBlockChance = 0.5F;
    BlockState carriedBlockState = SpectrumBlocks.CITRINE_BLOCK.getDefaultState();

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        EndermanEntity endermanEntity = ((EndermanEntity)(Object) this);
        if(endermanEntity.getEntityWorld() != null && endermanEntity.getEntityWorld() instanceof ServerWorld) {
            Random random = endermanEntity.getEntityWorld().random;
            if(random.nextFloat() < carriedBlockChance) {
                if (endermanEntity.getCarriedBlock() == null) {
                    endermanEntity.setCarriedBlock(carriedBlockState);
                }
            }
        }
    }

}
