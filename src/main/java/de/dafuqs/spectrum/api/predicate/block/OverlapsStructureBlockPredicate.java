package de.dafuqs.spectrum.api.predicate.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.structure.Structure;
import de.dafuqs.spectrum.registries.SpectrumBlockPredicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class OverlapsStructureBlockPredicate implements BlockPredicate {

    public static final Codec<OverlapsStructureBlockPredicate> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Vec3i.createOffsetCodec(16).optionalFieldOf("offset", BlockPos.ORIGIN).forGetter((predicate) -> predicate.offset),
     RegistryKey.createCodec(RegistryKeys.STRUCTURE).optionalFieldOf("structure").forGetter((predicate) -> predicate.structure),
     Codec.intRange(0, 32).optionalFieldOf("range", 0).forGetter((predicate) -> predicate.range)).apply(instance, OverlapsStructureBlockPredicate::new));

    private final Vec3i offset;
    private final int range;
    private final Optional<RegistryKey<Structure>> structure;
    public static final Logger LOGGER = LoggerFactory.getLogger("Spectrum");

    public OverlapsStructureBlockPredicate(Vec3i offset,  Optional<RegistryKey<Structure>> structure, int range) {
        this.structure = structure;
        this.offset = offset;
        this.range = range;
    }

    public boolean test(StructureWorldAccess structureWorldAccess, BlockPos blockPos) {
        ServerWorld serverWorld = structureWorldAccess.toServerWorld();
        BlockPos blockPosOffset = blockPos.add(this.offset);
        Map<Structure, StructureStart> structureMap = new HashMap<>();
        Chunk centerChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPosOffset.getX()), ChunkSectionPos.getSectionCoord(blockPosOffset.getZ()), ChunkStatus.EMPTY, false);
        if(centerChunk!=null && centerChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_STARTS))
        {
            structureMap.putAll(centerChunk.getStructureStarts());
        }
        Chunk westChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPosOffset.getX() - 16), ChunkSectionPos.getSectionCoord(blockPosOffset.getZ()), ChunkStatus.EMPTY, false);
        if(westChunk!=null && westChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_STARTS))
        {
            structureMap.putAll(westChunk.getStructureStarts());
        }
        Chunk northWestChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPosOffset.getX() - 16), ChunkSectionPos.getSectionCoord(blockPosOffset.getZ() - 16), ChunkStatus.EMPTY, false);
        if(northWestChunk!=null && northWestChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_STARTS))
        {
            structureMap.putAll(northWestChunk.getStructureStarts());
        }
        Chunk northChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPosOffset.getX()), ChunkSectionPos.getSectionCoord(blockPosOffset.getZ() - 16), ChunkStatus.EMPTY, false);
        if(northChunk!=null && northChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_STARTS))
        {
            structureMap.putAll(northChunk.getStructureStarts());
        }
        Chunk northEastChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPosOffset.getX() + 16), ChunkSectionPos.getSectionCoord(blockPosOffset.getZ() - 16), ChunkStatus.EMPTY, false);
        if(northEastChunk!=null && northEastChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_STARTS))
        {
            structureMap.putAll(northEastChunk.getStructureStarts());
        }
        Chunk eastChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPosOffset.getX() + 16), ChunkSectionPos.getSectionCoord(blockPosOffset.getZ()), ChunkStatus.EMPTY, false);
        if(eastChunk!=null && eastChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_STARTS))
        {
            structureMap.putAll(eastChunk.getStructureStarts());
        }
        Chunk southEastChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPosOffset.getX() + 16), ChunkSectionPos.getSectionCoord(blockPosOffset.getZ() + 16), ChunkStatus.EMPTY, false);
        if(southEastChunk!=null && southEastChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_STARTS))
        {
            structureMap.putAll(southEastChunk.getStructureStarts());
        }
        Chunk southChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPosOffset.getX()), ChunkSectionPos.getSectionCoord(blockPosOffset.getZ() + 16), ChunkStatus.EMPTY, false);
        if(southChunk!=null && southChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_STARTS))
        {
            structureMap.putAll(southChunk.getStructureStarts());
        }
        Chunk southWestChunk = serverWorld.getChunk(ChunkSectionPos.getSectionCoord(blockPosOffset.getX() - 16), ChunkSectionPos.getSectionCoord(blockPosOffset.getZ() + 16), ChunkStatus.EMPTY, false);
        if(southWestChunk!=null && southWestChunk.getStatus().isAtLeast(ChunkStatus.STRUCTURE_STARTS))
        {
            structureMap.putAll(southWestChunk.getStructureStarts());
        }
        Structure targetStruct = null;
        if(structureMap.isEmpty())
        {
            return false;
        }
        BlockBox exclusionZone = new BlockBox(blockPosOffset.getX() - this.range, blockPosOffset.getY() - this.range, blockPosOffset.getZ() - this.range,blockPosOffset.getX() + this.range, blockPosOffset.getY() + this.range, blockPosOffset.getZ() + this.range);
        if(this.structure.isPresent())
        {
            targetStruct = serverWorld.getStructureAccessor().getRegistryManager().get(RegistryKeys.STRUCTURE).get(this.structure.get());
        }
        for(var struct : structureMap.entrySet())
        {
            if(this.structure.isPresent())
            {
                if(targetStruct!=struct.getKey())
                {
                    continue;
                }
            }
            if(struct.getValue().getBoundingBox().intersects(exclusionZone))
            {
                return true;
            }
            else
            {
                for(StructurePiece piece : struct.getValue().getChildren())
                {
                    if(piece.getBoundingBox().intersects(exclusionZone)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public BlockPredicateType<?> getType() {
        return SpectrumBlockPredicates.OVERLAPS_STRUCTURE;
    }

}
