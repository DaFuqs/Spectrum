package de.dafuqs.spectrum.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import de.dafuqs.spectrum.datafixer.SpectrumDataFixers;
import net.minecraft.datafixer.TypeReferences;

import java.util.Optional;

public class InkStorageBlockFix extends DataFix {
    private final String choiceName;

    public InkStorageBlockFix(Schema outputSchema, String choiceName) {
        super(outputSchema, false);
        this.choiceName = choiceName;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> inType = this.getInputSchema().getChoiceType(TypeReferences.BLOCK_ENTITY, this.choiceName);
        Type<?> outType = this.getOutputSchema().getChoiceType(TypeReferences.BLOCK_ENTITY, this.choiceName);
        return this.fixTypeEverywhereTyped("SpectrumInkStorageBlockFix for "+this.choiceName, this.getInputSchema().getType(TypeReferences.BLOCK_ENTITY), typed -> typed.updateTyped(DSL.namedChoice(this.choiceName, inType), outType, typedx -> typedx.update(DSL.remainderFinder(), dynamic -> {
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
        })));
    }
}
