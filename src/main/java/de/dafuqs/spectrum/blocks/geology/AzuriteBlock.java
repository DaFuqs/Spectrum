package de.dafuqs.spectrum.blocks.geology;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import org.jspecify.annotations.*;

public class AzuriteBlock extends SpectrumDirectionalBlock implements AzureAuraEmitting {
	
	public AzuriteBlock(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable MapCodec<? extends AzuriteBlock> codec() {
		return null;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		
		BlockAuraSoundInstance.addToExistingInstanceOrCreateNewOne(world, pos);
	}
	
	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (world.isClientSide()) {
			onBreak(world, pos);
		}
		
		return super.playerWillDestroy(world, pos, state, player);
	}
	
}
