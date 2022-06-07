package de.dafuqs.spectrum.worldgen.structure_features;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;
import net.minecraft.block.JigsawBlock;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.Structure.StructureBlockInfo;
import net.minecraft.structure.StructureGeneratorFactory.Context;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.pool.EmptyPoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePool.Projection;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.ChunkRandom;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;

public class SpectrumStructurePoolBasedGenerator {
	
	static final Logger LOGGER = LogUtils.getLogger();
	
	public SpectrumStructurePoolBasedGenerator() {
	}
	
	public static Optional<StructurePiecesGenerator<SpectrumUndergroundStructurePoolFeatureConfig>> generate(Context<SpectrumUndergroundStructurePoolFeatureConfig> context, SpectrumStructurePoolBasedGenerator.PieceFactory pieceFactory, BlockPos pos, boolean bl, boolean bl2) {
		ChunkRandom chunkRandom = new ChunkRandom(new AtomicSimpleRandom(0L));
		chunkRandom.setCarverSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
		DynamicRegistryManager dynamicRegistryManager = context.registryManager();
		StructurePoolFeatureConfig structurePoolFeatureConfig = context.config();
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		StructureManager structureManager = context.structureManager();
		HeightLimitView heightLimitView = context.world();
		Predicate<RegistryEntry<Biome>> predicate = context.validBiome();
		StructureFeature.init();
		Registry<StructurePool> registry = dynamicRegistryManager.get(Registry.STRUCTURE_POOL_KEY);
		BlockRotation blockRotation = BlockRotation.random(chunkRandom);
		StructurePool structurePool = structurePoolFeatureConfig.getStartPool().value();
		StructurePoolElement structurePoolElement = structurePool.getRandomElement(chunkRandom);
		if (structurePoolElement == EmptyPoolElement.INSTANCE) {
			return Optional.empty();
		} else {
			PoolStructurePiece poolStructurePiece = pieceFactory.create(structureManager, structurePoolElement, pos, structurePoolElement.getGroundLevelDelta(), blockRotation, structurePoolElement.getBoundingBox(structureManager, pos, blockRotation));
			BlockBox blockBox = poolStructurePiece.getBoundingBox();
			int i = (blockBox.getMaxX() + blockBox.getMinX()) / 2;
			int j = (blockBox.getMaxZ() + blockBox.getMinZ()) / 2;
			int k;
			if (bl2) {
				k = pos.getY() + chunkGenerator.getHeightOnGround(i, j, Type.WORLD_SURFACE_WG, heightLimitView);
			} else {
				k = pos.getY();
			}
			
			if (!predicate.test(chunkGenerator.getBiomeForNoiseGen(BiomeCoords.fromBlock(i), BiomeCoords.fromBlock(k), BiomeCoords.fromBlock(j)))) {
				return Optional.empty();
			} else {
				int l = blockBox.getMinY() + poolStructurePiece.getGroundLevelDelta();
				poolStructurePiece.translate(0, k - l, 0);
				return Optional.of((structurePiecesCollector, contextx) -> {
					List<PoolStructurePiece> list = Lists.newArrayList();
					list.add(poolStructurePiece);
					if (structurePoolFeatureConfig.getSize() > 0) {
						Box box = new Box((i - 80), (k - 80), (j - 80), (i + 80 + 1), (k + 80 + 1), (j + 80 + 1));
						SpectrumStructurePoolBasedGenerator.StructurePoolGenerator structurePoolGenerator = new SpectrumStructurePoolBasedGenerator.StructurePoolGenerator(registry, structurePoolFeatureConfig.getSize(), pieceFactory, chunkGenerator, structureManager, list, chunkRandom);
						structurePoolGenerator.structurePieces.addLast(new SpectrumStructurePoolBasedGenerator.ShapedPoolStructurePiece(poolStructurePiece, new MutableObject(VoxelShapes.combineAndSimplify(VoxelShapes.cuboid(box), VoxelShapes.cuboid(Box.from(blockBox)), BooleanBiFunction.ONLY_FIRST)), 0));
						
						while (!structurePoolGenerator.structurePieces.isEmpty()) {
							SpectrumStructurePoolBasedGenerator.ShapedPoolStructurePiece shapedPoolStructurePiece = structurePoolGenerator.structurePieces.removeFirst();
							structurePoolGenerator.generatePiece(shapedPoolStructurePiece.piece, shapedPoolStructurePiece.pieceShape, shapedPoolStructurePiece.currentSize, bl, heightLimitView);
						}
						
						Objects.requireNonNull(structurePiecesCollector);
						list.forEach(structurePiecesCollector::addPiece);
					}
				});
			}
		}
	}
	
	public static void generate(DynamicRegistryManager registryManager, PoolStructurePiece piece, int maxDepth, SpectrumStructurePoolBasedGenerator.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureManager structureManager, List<? super PoolStructurePiece> results, Random random, HeightLimitView world) {
		Registry<StructurePool> registry = registryManager.get(Registry.STRUCTURE_POOL_KEY);
		SpectrumStructurePoolBasedGenerator.StructurePoolGenerator structurePoolGenerator = new SpectrumStructurePoolBasedGenerator.StructurePoolGenerator(registry, maxDepth, pieceFactory, chunkGenerator, structureManager, results, random);
		structurePoolGenerator.structurePieces.addLast(new SpectrumStructurePoolBasedGenerator.ShapedPoolStructurePiece(piece, new MutableObject(VoxelShapes.UNBOUNDED), 0));
		
		while (!structurePoolGenerator.structurePieces.isEmpty()) {
			SpectrumStructurePoolBasedGenerator.ShapedPoolStructurePiece shapedPoolStructurePiece = structurePoolGenerator.structurePieces.removeFirst();
			structurePoolGenerator.generatePiece(shapedPoolStructurePiece.piece, shapedPoolStructurePiece.pieceShape, shapedPoolStructurePiece.currentSize, false, world);
		}
	}
	
	public interface PieceFactory {
		PoolStructurePiece create(StructureManager structureManager, StructurePoolElement poolElement, BlockPos pos, int groundLevelDelta, BlockRotation rotation, BlockBox elementBounds);
	}
	
	static final class StructurePoolGenerator {
		final Deque<SpectrumStructurePoolBasedGenerator.ShapedPoolStructurePiece> structurePieces = Queues.newArrayDeque();
		private final Registry<StructurePool> registry;
		private final int maxSize;
		private final SpectrumStructurePoolBasedGenerator.PieceFactory pieceFactory;
		private final ChunkGenerator chunkGenerator;
		private final StructureManager structureManager;
		private final List<? super PoolStructurePiece> children;
		private final Random random;
		
		StructurePoolGenerator(Registry<StructurePool> registry, int maxSize, SpectrumStructurePoolBasedGenerator.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureManager structureManager, List<? super PoolStructurePiece> children, Random random) {
			this.registry = registry;
			this.maxSize = maxSize;
			this.pieceFactory = pieceFactory;
			this.chunkGenerator = chunkGenerator;
			this.structureManager = structureManager;
			this.children = children;
			this.random = random;
		}
		
		void generatePiece(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int minY, boolean modifyBoundingBox, HeightLimitView world) {
			StructurePoolElement structurePoolElement = piece.getPoolElement();
			BlockPos blockPos = piece.getPos();
			BlockRotation blockRotation = piece.getRotation();
			Projection projection = structurePoolElement.getProjection();
			boolean bl = projection == Projection.RIGID;
			MutableObject<VoxelShape> mutableObject = new MutableObject();
			BlockBox blockBox = piece.getBoundingBox();
			int i = blockBox.getMinY();
			Iterator var14 = structurePoolElement.getStructureBlockInfos(this.structureManager, blockPos, blockRotation, this.random).iterator();
			
			while (true) {
				while (true) {
					while (true) {
						label93:
						while (var14.hasNext()) {
							StructureBlockInfo structureBlockInfo = (StructureBlockInfo) var14.next();
							Direction direction = JigsawBlock.getFacing(structureBlockInfo.state);
							BlockPos blockPos2 = structureBlockInfo.pos;
							BlockPos blockPos3 = blockPos2.offset(direction);
							int j = blockPos2.getY() - i;
							int k = -1;
							Identifier identifier = new Identifier(structureBlockInfo.nbt.getString("pool"));
							Optional<StructurePool> optional = this.registry.getOrEmpty(identifier);
							if (optional.isPresent() && ((optional.get()).getElementCount() != 0 || Objects.equals(identifier, StructurePools.EMPTY.getValue()))) {
								Identifier identifier2 = (optional.get()).getTerminatorsId();
								Optional<StructurePool> optional2 = this.registry.getOrEmpty(identifier2);
								if (optional2.isPresent() && ((optional2.get()).getElementCount() != 0 || Objects.equals(identifier2, StructurePools.EMPTY.getValue()))) {
									boolean bl2 = blockBox.contains(blockPos3);
									MutableObject mutableObject2;
									if (bl2) {
										mutableObject2 = mutableObject;
										if (mutableObject.getValue() == null) {
											mutableObject.setValue(VoxelShapes.cuboid(Box.from(blockBox)));
										}
									} else {
										mutableObject2 = pieceShape;
									}
									
									List<StructurePoolElement> list = Lists.newArrayList();
									if (minY != this.maxSize) {
										list.addAll((optional.get()).getElementIndicesInRandomOrder(this.random));
									}
									
									list.addAll((optional2.get()).getElementIndicesInRandomOrder(this.random));
									Iterator var28 = list.iterator();
									
									while (var28.hasNext()) {
										StructurePoolElement structurePoolElement2 = (StructurePoolElement) var28.next();
										if (structurePoolElement2 == EmptyPoolElement.INSTANCE) {
											break;
										}
										
										Iterator var30 = BlockRotation.randomRotationOrder(this.random).iterator();
										
										label133:
										while (var30.hasNext()) {
											BlockRotation blockRotation2 = (BlockRotation) var30.next();
											List<StructureBlockInfo> list2 = structurePoolElement2.getStructureBlockInfos(this.structureManager, BlockPos.ORIGIN, blockRotation2, this.random);
											BlockBox blockBox2 = structurePoolElement2.getBoundingBox(this.structureManager, BlockPos.ORIGIN, blockRotation2);
											int l;
											if (modifyBoundingBox && blockBox2.getBlockCountY() <= 16) {
												l = list2.stream().mapToInt((structureBlockInfox) -> {
													if (!blockBox2.contains(structureBlockInfox.pos.offset(JigsawBlock.getFacing(structureBlockInfox.state)))) {
														return 0;
													} else {
														Identifier id = new Identifier(structureBlockInfox.nbt.getString("pool"));
														Optional<StructurePool> optionalZ = this.registry.getOrEmpty(identifier);
														Optional<StructurePool> optional2Z = optional.flatMap((pool) -> this.registry.getOrEmpty(pool.getTerminatorsId()));
														int iZ = optionalZ.map((pool) -> pool.getHighestY(this.structureManager)).orElse(0);
														int jZ = optional2Z.map((pool) -> pool.getHighestY(this.structureManager)).orElse(0);
														return Math.max(iZ, jZ);
													}
												}).max().orElse(0);
											} else {
												l = 0;
											}
											
											Iterator<StructureBlockInfo> var35 = list2.iterator();
											
											Projection projection2;
											boolean bl3;
											int n;
											int o;
											int p;
											BlockBox blockBox4;
											BlockPos blockPos6;
											int r;
											do {
												StructureBlockInfo structureBlockInfo2;
												do {
													if (!var35.hasNext()) {
														continue label133;
													}
													
													structureBlockInfo2 = var35.next();
												} while (!JigsawBlock.attachmentMatches(structureBlockInfo, structureBlockInfo2));
												
												BlockPos blockPos4 = structureBlockInfo2.pos;
												BlockPos blockPos5 = blockPos3.subtract(blockPos4);
												BlockBox blockBox3 = structurePoolElement2.getBoundingBox(this.structureManager, blockPos5, blockRotation2);
												int m = blockBox3.getMinY();
												projection2 = structurePoolElement2.getProjection();
												bl3 = projection2 == Projection.RIGID;
												n = blockPos4.getY();
												o = j - n + JigsawBlock.getFacing(structureBlockInfo.state).getOffsetY();
												if (bl && bl3) {
													p = i + o;
												} else {
													if (k == -1) {
														k = this.chunkGenerator.getHeightOnGround(blockPos2.getX(), blockPos2.getZ(), Type.WORLD_SURFACE_WG, world);
													}
													
													p = k - n;
												}
												
												int q = p - m;
												blockBox4 = blockBox3.offset(0, q, 0);
												blockPos6 = blockPos5.add(0, q, 0);
												if (l > 0) {
													r = Math.max(l + 1, blockBox4.getMaxY() - blockBox4.getMinY());
													blockBox4.encompass(new BlockPos(blockBox4.getMinX(), blockBox4.getMinY() + r, blockBox4.getMinZ()));
												}
											} while (VoxelShapes.matchesAnywhere((VoxelShape) mutableObject2.getValue(), VoxelShapes.cuboid(Box.from(blockBox4).contract(0.25D)), BooleanBiFunction.ONLY_SECOND));
											
											mutableObject2.setValue(VoxelShapes.combine((VoxelShape) mutableObject2.getValue(), VoxelShapes.cuboid(Box.from(blockBox4)), BooleanBiFunction.ONLY_FIRST));
											r = piece.getGroundLevelDelta();
											int s;
											if (bl3) {
												s = r - o;
											} else {
												s = structurePoolElement2.getGroundLevelDelta();
											}
											
											PoolStructurePiece poolStructurePiece = this.pieceFactory.create(this.structureManager, structurePoolElement2, blockPos6, s, blockRotation2, blockBox4);
											int t;
											if (bl) {
												t = i + j;
											} else if (bl3) {
												t = p + n;
											} else {
												if (k == -1) {
													k = this.chunkGenerator.getHeightOnGround(blockPos2.getX(), blockPos2.getZ(), Type.WORLD_SURFACE_WG, world);
												}
												
												t = k + o / 2;
											}
											
											piece.addJunction(new JigsawJunction(blockPos3.getX(), t - j + r, blockPos3.getZ(), o, projection2));
											poolStructurePiece.addJunction(new JigsawJunction(blockPos2.getX(), t - n + s, blockPos2.getZ(), -o, projection));
											this.children.add(poolStructurePiece);
											if (minY + 1 <= this.maxSize) {
												this.structurePieces.addLast(new SpectrumStructurePoolBasedGenerator.ShapedPoolStructurePiece(poolStructurePiece, mutableObject2, minY + 1));
											}
											continue label93;
										}
									}
								} else {
									SpectrumStructurePoolBasedGenerator.LOGGER.warn("Empty or non-existent fallback pool: {}", identifier2);
								}
							} else {
								SpectrumStructurePoolBasedGenerator.LOGGER.warn("Empty or non-existent pool: {}", identifier);
							}
						}
						
						return;
					}
				}
			}
		}
	}
	
	private static final class ShapedPoolStructurePiece {
		final PoolStructurePiece piece;
		final MutableObject<VoxelShape> pieceShape;
		final int currentSize;
		
		ShapedPoolStructurePiece(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int currentSize) {
			this.piece = piece;
			this.pieceShape = pieceShape;
			this.currentSize = currentSize;
		}
	}
}
