package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.progression.advancement.*;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;

public class SpectrumAdvancementCriteria {

    public static HasAdvancementCriterion ADVANCEMENT_GOTTEN;
    public static HadRevelationCriterion HAD_REVELATION;
    public static PedestalCraftingCriterion PEDESTAL_CRAFTING;
    public static CompletedMultiblockCriterion COMPLETED_MULTIBLOCK;
    public static BlockBrokenCriterion BLOCK_BROKEN;

    public static void register() {
        ADVANCEMENT_GOTTEN = CriteriaAccessor.callRegister(new HasAdvancementCriterion());
        HAD_REVELATION = CriteriaAccessor.callRegister(new HadRevelationCriterion());
        PEDESTAL_CRAFTING = CriteriaAccessor.callRegister(new PedestalCraftingCriterion());
        COMPLETED_MULTIBLOCK = CriteriaAccessor.callRegister(new CompletedMultiblockCriterion());
        BLOCK_BROKEN = CriteriaAccessor.callRegister(new BlockBrokenCriterion());
    }

}
