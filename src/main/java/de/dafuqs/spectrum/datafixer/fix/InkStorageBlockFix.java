package de.dafuqs.spectrum.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import de.dafuqs.spectrum.datafixer.SpectrumDataFixers;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.fix.ChoiceFix;

import java.util.Optional;

public class InkStorageBlockFix extends ChoiceFix {
    public InkStorageBlockFix(Schema outputSchema, String choiceName) {
        super(outputSchema, false, "SpectrumInkStorageBlockFix", TypeReferences.BLOCK_ENTITY, choiceName);
    }

    @Override
    protected Typed<?> transform(Typed<?> inputType) {
        return inputType.update(DSL.remainderFinder(), dynamic -> {
            Optional<? extends Dynamic<?>> optionalInkStorage = dynamic.get("InkStorage").result();
            if (optionalInkStorage.isPresent()) {
                Dynamic<?> dynamicInkStorage = optionalInkStorage.get();

                // IndividualCappedInkStorage
                Optional<? extends Dynamic<?>> optionalMaxEnergyPerColor = dynamicInkStorage.get("MaxEnergyPerColor").result();
                // TotalCappedInkStorage
                Optional<? extends Dynamic<?>> optionalMaxEnergyTotal = dynamicInkStorage.get("MaxEnergyTotal").result();

                if (optionalMaxEnergyPerColor.isPresent()) {
                    return dynamic.remove("InkStorage").set("InkStorage", SpectrumDataFixers.processMultiple(dynamicInkStorage));
                } else if (optionalMaxEnergyTotal.isPresent()) {
                    return dynamic.remove("InkStorage").set("InkStorage", SpectrumDataFixers.processMultiple(dynamicInkStorage));
                }
            }

            return dynamic;
        });
    }
}
