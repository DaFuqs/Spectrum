package de.dafuqs.spectrum.api.predicate.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.structure.Structure;
import de.dafuqs.spectrum.registries.SpectrumBlockPredicates;
import java.util.Optional;


public class OverlapsStructureBlockPredicate implements BlockPredicate {

    public static final Codec<OverlapsStructureBlockPredicate> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Vec3i.createOffsetCodec(16).optionalFieldOf("offset", BlockPos.ORIGIN).forGetter((predicate) -> predicate.offset),
     RegistryKey.createCodec(RegistryKeys.STRUCTURE).optionalFieldOf("structure").forGetter((predicate) -> predicate.structure)).apply(instance, OverlapsStructureBlockPredicate::new));
    private final Vec3i offset;

    private final Optional<RegistryKey<Structure>> structure;

    public OverlapsStructureBlockPredicate(Vec3i offset,  Optional<RegistryKey<Structure>> structure) {
        this.structure = structure;
        this.offset = offset;
    }

    public boolean test(StructureWorldAccess structureWorldAccess, BlockPos blockPos) {
        if(this.structure.isEmpty())
        {
            return structureWorldAccess.toServerWorld().getStructureAccessor().hasStructureReferences(blockPos.add(this.offset));
        }
        else return structureWorldAccess.toServerWorld().getStructureAccessor().getStructureContaining(blockPos.add(this.offset), this.structure.get()).hasChildren();
    }

    public BlockPredicateType<?> getType() {
        return SpectrumBlockPredicates.OVERLAPS_STRUCTURE;
    }

}
