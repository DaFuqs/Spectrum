package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;
import javax.annotation.*;

public class ProjectorBlock extends Block implements EntityBlock {
	
	public static final MapCodec<ProjectorBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			propertiesCodec(),
			ResourceLocation.CODEC.fieldOf("texture").forGetter(b -> b.texture),
			Codec.DOUBLE.fieldOf("width").forGetter(b -> b.width),
			Codec.DOUBLE.fieldOf("height").forGetter(b -> b.height),
			Codec.FLOAT.fieldOf("heightOffset").forGetter(b -> b.heightOffset),
			Codec.FLOAT.fieldOf("bobMultiplier").forGetter(b -> b.bobMultiplier),
			Codec.FLOAT.fieldOf("scaling").forGetter(b -> b.scaling)
	).apply(i, ProjectorBlock::new));
	
	private final double width, height;
	private final VoxelShape shape;
	final float heightOffset, bobMultiplier, scaling;
	final ResourceLocation texture;
	
	public ProjectorBlock(Properties settings, String path, double width, double height, float heightOffset, float bobMultiplier, float scaling) {
		this(settings, SpectrumCommon.locate("textures/block/" + path + ".png"), width, height, heightOffset, bobMultiplier, scaling);
	}
	
	public ProjectorBlock(Properties settings, ResourceLocation texture, double width, double height, float heightOffset, float bobMultiplier, float scaling) {
		super(settings);
		this.heightOffset = heightOffset;
		this.bobMultiplier = bobMultiplier;
		this.scaling = scaling;
		this.width = width;
		this.height = height;
		var min = (16 - width) / 2;
		var max = width + min;
		shape = Block.box(min, 0, min, max, height, max);
		this.texture = texture;
	}
	
	@Override
	public MapCodec<? extends ProjectorBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return shape;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ProjectorBlockEntity(pos, state);
	}
}
