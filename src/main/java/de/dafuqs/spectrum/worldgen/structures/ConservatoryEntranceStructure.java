package de.dafuqs.spectrum.worldgen.structures;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.heightproviders.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.*;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.pools.alias.*;
import net.minecraft.world.level.levelgen.structure.structures.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.storage.loot.*;

import java.util.*;
import java.util.function.*;

public class ConservatoryEntranceStructure extends UndergroundStructure {
	
	public record Entry(ResourceLocation bottomStructure, ResourceLocation middleStructure, ResourceLocation topStructure) {
		
		public static final Codec<Entry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
					ResourceLocation.CODEC.fieldOf("bottom").forGetter(entry -> entry.bottomStructure),
					ResourceLocation.CODEC.fieldOf("middle").forGetter(entry -> entry.middleStructure),
					ResourceLocation.CODEC.fieldOf("top").forGetter(entry -> entry.topStructure)
				).apply(instance, Entry::new));
		
	}
	
	public static final MapCodec<ConservatoryEntranceStructure> CODEC = RecordCodecBuilder.mapCodec((instance) ->
			instance.group(ConservatoryEntranceStructure.settingsCodec(instance),
					Entry.CODEC.listOf().fieldOf("entries").forGetter((structure) -> structure.entries),
					Codec.intRange(0, 7).fieldOf("size").forGetter((structure) -> structure.size),
					HeightProvider.CODEC.fieldOf("start_height").forGetter((structure) -> structure.startHeight),
					IntProvider.NON_NEGATIVE_CODEC.fieldOf("bury_depth").forGetter((structure) -> structure.buryDepth),
					Codec.intRange(0, 64).fieldOf("placement_check_width").forGetter((structure) -> structure.placementCheckWidth),
					Codec.intRange(0, 64).fieldOf("placement_check_height").forGetter((structure) -> structure.placementCheckHeight),
					Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((structure) -> structure.maxDistanceFromCenter)
			).apply(instance, ConservatoryEntranceStructure::new));
	
	protected final List<Entry> entries;
	
	public ConservatoryEntranceStructure(StructureSettings config, List<Entry> entries, Integer size, HeightProvider startHeight, IntProvider buryDepth, Integer placementCheckWidth, Integer placementCheckHeight, Integer maxDistanceFromCenter) {
		super(config, size, startHeight, buryDepth, placementCheckWidth, placementCheckHeight, maxDistanceFromCenter);
		this.entries = entries;
	}
	
	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		WorldgenRandom chunkRandom = context.random();
		WorldGenerationContext heightContext = new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor());
		
		int x = context.chunkPos().getMinBlockX() + chunkRandom.nextInt(16);
		int z = context.chunkPos().getMinBlockZ() + chunkRandom.nextInt(16);
		int y = this.startHeight.sample(chunkRandom, heightContext);
		
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		LevelHeightAccessor world = context.heightAccessor();
		RandomState noiseConfig = context.randomState();
		
		BoundingBox structureBox = BoundingBox.fromCorners(
				new BlockPos(x - placementCheckWidth / 2, y, z - placementCheckWidth / 2),
				new BlockPos(x + placementCheckWidth / 2, y + placementCheckHeight, z + placementCheckWidth / 2)
		);
		Optional<Integer> floorHeight = getFloorHeight(chunkRandom, chunkGenerator, world, noiseConfig, structureBox, buryDepth);
		if (floorHeight.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(new Structure.GenerationStub(new BlockPos(x, floorHeight.get(), z), (builder) -> generatePieces(builder, context, floorHeight.get())));
	}
	
	private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context, int y) {
		ChunkPos chunkpos = context.chunkPos();
		WorldgenRandom worldgenrandom = context.random();
		BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), y, chunkpos.getMinBlockZ());
		Rotation rotation = Rotation.NONE; //  Rotation.getRandom(worldgenrandom);
		addPieces(context, blockpos, rotation, builder, worldgenrandom);
	}
	
	public void addPieces(Structure.GenerationContext context, BlockPos startPos, Rotation rotation, StructurePieceAccessor pieces, RandomSource random) {
		StructureTemplateManager structureTemplateManager = context.structureTemplateManager();
		
		Entry e = this.entries.get(random.nextInt(this.entries.size()));
		
		for(int y = context.chunkGenerator().getMinY() + 3; y < startPos.getY(); y++) {
			pieces.addPiece(new Piece(structureTemplateManager, e.middleStructure, rotation, new BlockPos(startPos.getX(), y, startPos.getZ())));
		}
		pieces.addPiece(new Piece(structureTemplateManager, e.topStructure, rotation, startPos.offset(-2, 0, -2)));
		pieces.addPiece(new Piece(structureTemplateManager, e.bottomStructure, rotation, new BlockPos(startPos.getX(), context.chunkGenerator().getMinY() + 1, startPos.getZ())));
	}
	
	public static class Piece extends TemplateStructurePiece {
		
		public Piece(StructureTemplateManager structureTemplateManager, ResourceLocation location, Rotation rotation, BlockPos pos) {
			super(
					StructurePieceType.IGLOO,
					0,
					structureTemplateManager,
					location,
					location.toString(),
					new StructurePlaceSettings()
							.setRotation(rotation)
							.setMirror(Mirror.NONE)
							.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
							.setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING),
					pos
			);
		}
		
		@Override
		protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
		
		}
		
	}
	
	@Override
	public StructureType<ConservatoryEntranceStructure> type() {
		return SpectrumStructureTypes.CONSERVATORY_ENTRANCE;
	}
	
}
