package de.dafuqs.spectrum.blocks;

import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public interface MoonstoneStrikeableBlock {
    
    void onMoonstoneStrike(World world, BlockPos pos, @Nullable LivingEntity striker);
    
}
