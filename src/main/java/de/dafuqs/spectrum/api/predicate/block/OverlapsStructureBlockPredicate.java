package de.dafuqs.spectrum.api.predicate.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.*;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.structure.Structure;
import de.dafuqs.spectrum.registries.SpectrumBlockPredicates;
import java.util.Optional;


public class OverlapsStructureBlockPredicate implements BlockPredicate {

    public static final Codec<OverlapsStructureBlockPredicate> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Vec3i.createOffsetCodec(16).optionalFieldOf("offset", BlockPos.ORIGIN).forGetter((predicate) -> predicate.offset),
     RegistryKey.createCodec(RegistryKeys.STRUCTURE).optionalFieldOf("structure").forGetter((predicate) -> predicate.structure),
     Codec.intRange(0, 32).optionalFieldOf("range", 0).forGetter((predicate) -> predicate.range)).apply(instance, OverlapsStructureBlockPredicate::new));

    private final Vec3i offset;
    private final int range;
    private final Optional<RegistryKey<Structure>> structure;
    public OverlapsStructureBlockPredicate(Vec3i offset,  Optional<RegistryKey<Structure>> structure, int range) {
        this.structure = structure;
        this.offset = offset;
        this.range = range;
    }

    public boolean checkChunkStructureInFeature(Structure targetStruct, ServerWorld serverWorld, BlockPos blockPos, int xChunkOffset, int zChunkOffset)
    {
        BlockBox exclusionZone = new BlockBox(blockPos.getX() - this.range, blockPos.getY() - this.range, blockPos.getZ() - this.range,blockPos.getX() + this.range, blockPos.getY() + this.range, blockPos.getZ() + this.range);

        Chunk thisChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPos.getX() + xChunkOffset * 16), ChunkSectionPos.getSectionCoord(blockPos.getZ() + zChunkOffset * 16), ChunkStatus.EMPTY, false);

        if(thisChunk!=null && thisChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_REFERENCES))
        {
            for(var struct : thisChunk.getStructureReferences().entrySet())
            {
                if(targetStruct!= null && targetStruct!=struct.getKey())
                {
                    continue;
                }
                for (Long longPos : struct.getValue()) {
                    ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(new ChunkPos(longPos), serverWorld.getBottomSectionCoord());
                    StructureStart structureStart = serverWorld.getStructureAccessor().getStructureStart(chunkSectionPos, struct.getKey(), serverWorld.getChunk(chunkSectionPos.getSectionX(), chunkSectionPos.getSectionZ(), ChunkStatus.EMPTY, false));
                    if (structureStart == null) {
                        continue;
                    }
                    if (structureStart.getBoundingBox().intersects(exclusionZone)) {
                        return true;
                    } else {
                        for (StructurePiece piece : structureStart.getChildren()) {
                            if (piece.getBoundingBox().intersects(exclusionZone)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    public boolean test(StructureWorldAccess structureWorldAccess, BlockPos blockPos) {
        ServerWorld serverWorld = structureWorldAccess.toServerWorld();
        BlockPos blockPosOffset = blockPos.add(this.offset);
        Structure targetStruct = null;
        if(this.structure.isPresent())
        {
            targetStruct = serverWorld.getStructureAccessor().getRegistryManager().get(RegistryKeys.STRUCTURE).get(this.structure.get());
        }
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if(checkChunkStructureInFeature(targetStruct,serverWorld, blockPosOffset, x, z))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public BlockPredicateType<?> getType() {
        return SpectrumBlockPredicates.OVERLAPS_STRUCTURE;
    }

}
