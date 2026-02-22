package de.dafuqs.spectrum.blocks.geology;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.api.distmarker.*;

public class AzuriteBlock extends SpectrumFacingBlock implements AzureAuraEmitting {
	
	public AzuriteBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public MapCodec<? extends AzuriteBlock> codec() {
		return null;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		
		BlockAuraSoundInstance.addToExistingInstanceOrCreateNewOne(world, pos);
	}
	
}
