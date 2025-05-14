package de.dafuqs.spectrum.worldgen.structure_pool_elements;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.*;

public class SingleBlockPoolElement extends StructurePoolElement {
	
	public static final MapCodec<SingleBlockPoolElement> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			BlockState.CODEC.fieldOf("block").forGetter((pool) -> pool.state),
			CompoundTag.CODEC.fieldOf("nbt").forGetter((pool) -> pool.blockNbt),
			projectionCodec()
	).apply(instance, SingleBlockPoolElement::new));
	
	protected final BlockState state;
	protected final CompoundTag blockNbt;
	
	private static final CompoundTag jigsawNbt = createDefaultJigsawNbt();
	
	protected SingleBlockPoolElement(BlockState state, CompoundTag blockNbt, StructureTemplatePool.Projection projection) {
		super(projection);
		this.state = state;
		this.blockNbt = blockNbt;
	}
	
	private static CompoundTag createDefaultJigsawNbt() {
		CompoundTag nbtCompound = new CompoundTag();
		nbtCompound.putString("name", "spectrum:main");
		nbtCompound.putString("final_state", "minecraft:air");
		nbtCompound.putString("pool", "minecraft:empty");
		nbtCompound.putString("target", "minecraft:empty");
		nbtCompound.putString("joint", JigsawBlockEntity.JointType.ROLLABLE.getSerializedName());
		return nbtCompound;
	}
	
	@Override
	public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) {
		return Vec3i.ZERO;
	}
	
	@Override
	public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos pos, Rotation rotation, RandomSource random) {
		return List.of(new StructureTemplate.StructureBlockInfo(pos, Blocks.JIGSAW.defaultBlockState().setValue(JigsawBlock.ORIENTATION, FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH)), jigsawNbt));
	}
	
	@Override
	public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos pos, Rotation rotation) {
		Vec3i start = this.getSize(structureTemplateManager, rotation);
		return new BoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + start.getX(), pos.getY() + start.getY(), pos.getZ() + start.getZ());
	}
	
	@Override
	public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, BlockPos pos, BlockPos pivot, Rotation rotation, BoundingBox box, RandomSource random, LiquidSettings liquidSettings, boolean keepJigsaws) {
		if (keepJigsaws) {
			return true;
		}
		
		if (world.setBlock(pos.below(), this.state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL)) {
			if (this.blockNbt.isEmpty()) {
				return true;
			}
			
			BlockEntity blockEntity = world.getBlockEntity(pos.below());
			if (blockEntity != null) {
				blockEntity.loadCustomOnly(this.blockNbt, world.registryAccess());
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public StructurePoolElementType<?> getType() {
		return SpectrumStructurePoolElementTypes.SINGLE_BLOCK_ELEMENT;
	}
	
	@Override
	public String toString() {
		return "SpectrumSingleBlock[" + this.state.toString() + "]" + this.blockNbt;
	}
	
}
