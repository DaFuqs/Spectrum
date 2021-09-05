package de.dafuqs.spectrum.recipe.fusion_shrine;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public enum FusionShrineRecipeWorldEffect {
    START_RAIN,
    START_THUNDER,
    LIGHTNING_ON_SHRINE,
    LIGHTNING_AROUND_SHRINE,
    VISUAL_EXPLOSION_ON_SHRINE;

    public void doEffect(ServerWorld world, BlockPos shrinePos) {
        //TODO
    }


}
