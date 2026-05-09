package de.dafuqs.spectrum.api.block;

import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import javax.annotation.*;

public interface MoonstoneStrikeableBlock {
	
	void onMoonstoneStrike(Level world, BlockPos pos, @Nullable LivingEntity striker);
    
}
