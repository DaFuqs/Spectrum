package de.dafuqs.spectrum.blocks.weathering;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.common.*;
import org.jetbrains.annotations.*;

public class ShaleClayBlock extends Block implements Weathering {
	
	private final Weathering.WeatheringLevel weatheringLevel;
	
	public ShaleClayBlock(Weathering.WeatheringLevel weatheringLevel, BlockBehaviour.Properties settings) {
		super(settings);
		this.weatheringLevel = weatheringLevel;
	}
	
	@Override
	public MapCodec<? extends ShaleClayBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (shouldTryWeather(world, pos)) {
			this.changeOverTime(state, world, pos, random);
		}
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return Weathering.getIncreasedWeatheredBlock(state.getBlock()).isPresent();
	}
	
	@Override
	public Weathering.WeatheringLevel getAge() {
		return this.weatheringLevel;
	}
	
	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
		ItemStack handStack = context.getItemInHand();
		if (itemAbility == ItemAbilities.HOE_TILL && handStack.canPerformAction(itemAbility) && HoeItem.onlyIfAirAbove(context)) {
			return SpectrumBlocks.TILLED_SHALE_CLAY.get().defaultBlockState();
		}
		return null;
	}
	
}
