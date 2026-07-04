package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.fluids.*;
import org.jspecify.annotations.*;

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
