package de.dafuqs.spectrum.worldgen;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.block.enums.*;
import net.minecraft.nbt.*;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.chunk.*;

import java.util.*;

public class SingleBlockPoolElement extends StructurePoolElement {
	
	public static final Codec<SingleBlockPoolElement> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			BlockState.CODEC.fieldOf("block").forGetter((pool) -> pool.state),
			NbtCompound.CODEC.fieldOf("nbt").forGetter((pool) -> pool.blockNbt),
			projectionGetter()
	).apply(instance, SingleBlockPoolElement::new));
	
	protected final BlockState state;
	protected final NbtCompound blockNbt;
	
	private static final NbtCompound jigsawNbt = createDefaultJigsawNbt();
	
	protected SingleBlockPoolElement(BlockState state, NbtCompound blockNbt, StructurePool.Projection projection) {
		super(projection);
		this.state = state;
		this.blockNbt = blockNbt;
	}
	
	private static NbtCompound createDefaultJigsawNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.putString("name", "spectrum:main");
		nbtCompound.putString("final_state", "minecraft:air");
		nbtCompound.putString("pool", "minecraft:empty");
		nbtCompound.putString("target", "minecraft:empty");
		nbtCompound.putString("joint", JigsawBlockEntity.Joint.ROLLABLE.asString());
		return nbtCompound;
	}
	
	@Override
	public Vec3i getStart(StructureTemplateManager structureTemplateManager, BlockRotation rotation) {
		return Vec3i.ZERO;
	}
	
	@Override
	public List<StructureTemplate.StructureBlockInfo> getStructureBlockInfos(StructureTemplateManager structureTemplateManager, BlockPos pos, BlockRotation rotation, Random random) {
		return List.of(new StructureTemplate.StructureBlockInfo(pos, Blocks.JIGSAW.getDefaultState().with(JigsawBlock.ORIENTATION, JigsawOrientation.byDirections(Direction.DOWN, Direction.SOUTH)), jigsawNbt));
	}
	
	@Override
	public BlockBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos pos, BlockRotation rotation) {
		Vec3i start = this.getStart(structureTemplateManager, rotation);
		return new BlockBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + start.getX(), pos.getY() + start.getY(), pos.getZ() + start.getZ());
	}
	
	@Override
	public boolean generate(StructureTemplateManager structureTemplateManager, StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, BlockPos pos, BlockPos pivot, BlockRotation rotation, BlockBox box, Random random, boolean keepJigsaws) {
		if (keepJigsaws) {
			return true;
		}
		
		if (world.setBlockState(pos.down(), this.state, Block.FORCE_STATE | Block.NOTIFY_ALL)) {
			if (this.blockNbt.isEmpty()) {
				return true;
			}
			
			BlockEntity blockEntity = world.getBlockEntity(pos.down());
			if (blockEntity != null) {
				blockEntity.readNbt(this.blockNbt);
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
