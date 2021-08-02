package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

    BlockState carriedBlockState = SpectrumBlocks.ENDER_TREASURE.getDefaultState();

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        EndermanEntity endermanEntity = ((EndermanEntity)(Object) this);
        if(endermanEntity.getEntityWorld() != null && endermanEntity.getEntityWorld() instanceof ServerWorld) {
            Random random = endermanEntity.getEntityWorld().random;

            float chance;
            if(endermanEntity.getEntityWorld().getRegistryKey().equals(World.END)) {
                chance = SpectrumCommon.CONFIG.EndermanHoldingEnderTreasureInEndChance;
            } else {
                chance = SpectrumCommon.CONFIG.EndermanHoldingEnderTreasureChance;
            }

            if(random.nextFloat() < chance) {
                if (endermanEntity.getCarriedBlock() == null) {
                    endermanEntity.setCarriedBlock(carriedBlockState);
                }
            }
        }
    }

}
