package de.dafuqs.spectrum.blocks.mob_head;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumWallSkullBlock extends WallSkullBlock {

	public static final MapCodec<SpectrumWallSkullBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			SpectrumSkullType.CODEC.fieldOf("kind").forGetter(b -> b.skullType),
			propertiesCodec()
	).apply(i, SpectrumWallSkullBlock::new));

	public static BiMap<SpectrumSkullType, Block> MOB_WALL_HEADS = EnumHashBiMap.create(SpectrumSkullType.class);
	private final SpectrumSkullType skullType;
	
	public SpectrumWallSkullBlock(SpectrumSkullType skullType, Properties settings) {
		super(skullType, settings);
		this.skullType = skullType;
		MOB_WALL_HEADS.put(skullType, this);
	}

	@Override
	public MapCodec<? extends SpectrumWallSkullBlock> codec() {
		return CODEC;
	}
	
	public static Block getMobWallHead(SpectrumSkullType skullType) {
		return SpectrumWallSkullBlock.MOB_WALL_HEADS.get(skullType);
	}
	
	@Contract(pure = true)
	public static @NotNull Collection<Block> getMobWallHeads() {
		return SpectrumWallSkullBlock.MOB_WALL_HEADS.values();
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpectrumSkullBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return null;
	}
	
}
