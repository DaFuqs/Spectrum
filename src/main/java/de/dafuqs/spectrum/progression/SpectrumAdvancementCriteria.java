package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.progression.advancement.HadRevelationCriterion;
import de.dafuqs.spectrum.progression.advancement.HasAdvancementCriterion;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;

public class SpectrumAdvancementCriteria {

    public static HasAdvancementCriterion ADVANCEMENT_GOTTEN;
    public static HadRevelationCriterion HAD_REVELATION;

    public static void register() {
        ADVANCEMENT_GOTTEN = CriteriaAccessor.callRegister(new HasAdvancementCriterion());
        HAD_REVELATION = CriteriaAccessor.callRegister(new HadRevelationCriterion());
    }

}
