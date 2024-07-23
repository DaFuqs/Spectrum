package de.dafuqs.spectrum.blocks.mob_head;

import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumWallSkullBlock extends WallSkullBlock {
	
	public static BiMap<SpectrumSkullType, Block> MOB_WALL_HEADS = EnumHashBiMap.create(SpectrumSkullType.class);
	
	public SpectrumWallSkullBlock(SpectrumSkullType skullType, Settings settings) {
		super(skullType, settings);
		MOB_WALL_HEADS.put(skullType, this);
	}
	
	public static Block getMobWallHead(SpectrumSkullType skullType) {
		return SpectrumWallSkullBlock.MOB_WALL_HEADS.get(skullType);
	}
	
	@Contract(pure = true)
	public static @NotNull Collection<Block> getMobWallHeads() {
		return SpectrumWallSkullBlock.MOB_WALL_HEADS.values();
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpectrumSkullBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return null;
	}
	
}
