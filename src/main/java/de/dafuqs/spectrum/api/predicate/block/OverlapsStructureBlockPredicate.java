package de.dafuqs.spectrum.api.predicate.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.structure.Structure;
import de.dafuqs.spectrum.registries.SpectrumBlockPredicates;

import java.util.Map;
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

    public boolean test(StructureWorldAccess structureWorldAccess, BlockPos blockPos) {
        if(structureWorldAccess.toServerWorld().getStructureAccessor().hasStructureReferences(blockPos.add(this.offset)))
        {
            return false;
        }
        BlockBox exclusionZone = new BlockBox(blockPos.add(this.offset).getX() - this.range, blockPos.add(this.offset).getY() - this.range, blockPos.add(this.offset).getZ() - this.range,blockPos.add(this.offset).getX() + this.range, blockPos.add(this.offset).getY() + this.range, blockPos.add(this.offset).getZ() + this.range);
        Map<Structure, StructureStart> structureMap = structureWorldAccess.toServerWorld().getChunk(blockPos.add(this.offset)).getStructureStarts();
        if(structureMap.isEmpty())
        {
            return false;
        }
        for(var struct : structureMap.entrySet())
        {
            if(this.structure.isPresent())
            {
                Structure targetStruct = structureWorldAccess.toServerWorld().getStructureAccessor().getRegistryManager().get(RegistryKeys.STRUCTURE).get(this.structure.get());
                if(targetStruct!=struct.getKey())
                {
                    continue;
                }
            }
            if(struct.getValue().getBoundingBox().intersects(exclusionZone))
            {
                return true;
            }
        }
        return false;
    }

    public BlockPredicateType<?> getType() {
        return SpectrumBlockPredicates.OVERLAPS_STRUCTURE;
    }

}
