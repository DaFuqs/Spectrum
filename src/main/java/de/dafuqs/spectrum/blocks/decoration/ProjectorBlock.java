package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class ProjectorBlock extends Block implements BlockEntityProvider {

	private final VoxelShape shape;
	final float heightOffset, bobMultiplier, scaling;
	final Identifier texture;

	public ProjectorBlock(Settings settings, String path, double width, double height, float heightOffset, float bobMultiplier, float scaling) {
		super(settings);
		this.heightOffset = heightOffset;
		this.bobMultiplier = bobMultiplier;
		this.scaling = scaling;
		var min = (16 - width) / 2;
		var max = width + min;
		shape = Block.createCuboidShape(min, 0, min, max, height, max);
		this.texture = SpectrumCommon.locate("textures/block/" + path + ".png");
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return shape;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ProjectorBlockEntity(pos, state);
	}
}
