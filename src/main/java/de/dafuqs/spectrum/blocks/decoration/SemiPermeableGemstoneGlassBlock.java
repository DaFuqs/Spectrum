package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.shapes.*;

public class SemiPermeableGemstoneGlassBlock extends GemstoneGlassBlock {
	
	public final MapCodec<SemiPermeableGemstoneGlassBlock> codec;
	
	public SemiPermeableGemstoneGlassBlock(Properties settings, GemstoneColor gemstoneColor) {
		super(settings, gemstoneColor);
		this.codec = RecordCodecBuilder.mapCodec(i -> i.group(
				propertiesCodec(),
				SpectrumRegistries.GEMSTONE_COLOR.byNameCodec().fieldOf("color").forGetter(b -> b.gemstoneColor)
		).apply(i, SemiPermeableGemstoneGlassBlock::new));
	}
	
	@Override
	public MapCodec<? extends SemiPermeableGemstoneGlassBlock> codec() {
		return codec;
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SemiPermeableGlassBlock.isPlayerOrPlayerRiderCollision(context) ? Shapes.empty() : state.getShape(world, pos);
	}
	
}
