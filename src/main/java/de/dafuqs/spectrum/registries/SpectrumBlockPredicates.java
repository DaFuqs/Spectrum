package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.api.predicate.block.OverlapsStructureBlockPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.registry.Registry;

public class SpectrumBlockPredicates {
    public static BlockPredicateType<OverlapsStructureBlockPredicate> OVERLAPS_STRUCTURE;
    public static void register() {
        OVERLAPS_STRUCTURE = Registry.register(Registries.BLOCK_PREDICATE_TYPE, new Identifier(SpectrumCommon.MOD_ID, "overlaps_structure"), () -> OverlapsStructureBlockPredicate.CODEC);
    }
}
