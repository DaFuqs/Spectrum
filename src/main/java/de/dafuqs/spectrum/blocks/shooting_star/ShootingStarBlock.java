package de.dafuqs.spectrum.blocks.shooting_star;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.shapes.*;

public class ShootingStarBlock extends PlacedItemBlock implements ShootingStar {
	
	public static final MapCodec<ShootingStarBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			Variant.CODEC.fieldOf("shooting_star_type").forGetter(ShootingStarBlock::getShootingStarType)
	).apply(instance, ShootingStarBlock::new));
	
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	public final Variant shootingStarType;
	
	public ShootingStarBlock(Properties settings, Variant shootingStarType) {
		super(settings);
		this.shootingStarType = shootingStarType;
	}
	
	public Variant getShootingStarType() {
		return shootingStarType;
	}
	
	@Override
	public MapCodec<? extends ShootingStarBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

}
