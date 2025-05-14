package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BonemealingIdolBlock extends IdolBlock {
	
	public BonemealingIdolBlock(Properties settings, ParticleOptions particleEffect) {
		super(settings, particleEffect);
	}

	@Override
	public MapCodec<? extends BonemealingIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.bonemealing_idol.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int startDirection = world.random.nextInt(4);
		for (int i = 0; i < 4; i++) {
			Direction currentDirection = Direction.from2DDataValue(startDirection + i);
			BlockPos offsetPos = blockPos.relative(currentDirection);
			BlockState offsetState = world.getBlockState(offsetPos);
			if (offsetState.getBlock() instanceof BonemealableBlock fertilizable) {
				if (fertilizable.isValidBonemealTarget(world, offsetPos, offsetState) && fertilizable.isBonemealSuccess(world, world.random, offsetPos, offsetState)) {
					fertilizable.performBonemeal(world, world.getRandom(), offsetPos, offsetState);
					world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, offsetPos, 0); // particles
					return true;
				}
			}
			
		}
		return true;
	}
	
}
