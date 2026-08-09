package de.dafuqs.spectrum.blocks.fluid;

import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.fluids.*;

public abstract class SpectrumFluidType extends FluidType {
	
	protected final boolean isVaporized;
	protected final GameRules.Key<GameRules.BooleanValue> isInfiniteGameRule;
	
	public SpectrumFluidType(boolean isVaporized, GameRules.Key<GameRules.BooleanValue> isInfiniteGameRule, Properties properties) {
		super(properties);
		this.isVaporized = isVaporized;
		this.isInfiniteGameRule = isInfiniteGameRule;
	}
	
	public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
		return isVaporized && level.dimensionType().ultraWarm();
	}
	
	@Override
	public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
		if (reader instanceof Level level) {
			return level.getGameRules().getBoolean(this.isInfiniteGameRule);
		}
		return super.canConvertToSource(state, reader, pos);
	}
	
}
