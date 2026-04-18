package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.energy.color.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.data.worldgen.placement.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ColoredGrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {
	
	private static final Map<InkColor, ColoredGrassBlock> BLOCKS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredGrassBlock(Properties settings, InkColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	
	@Override
	protected @NotNull MapCodec<ColoredGrassBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return level.getBlockState(pos.above()).isAir();
	}
	
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}
	
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		// TODO
	}
	
	public BonemealableBlock.Type getType() {
		return Type.NEIGHBOR_SPREADER;
	}
	
	public InkColor getColor() {
		return this.color;
	}
	
	public static ColoredGrassBlock byColor(InkColor color) {
		return BLOCKS.get(color);
	}
}
