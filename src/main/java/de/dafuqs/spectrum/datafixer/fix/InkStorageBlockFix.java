package de.dafuqs.spectrum.datafixer.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.datafixer.SpectrumDataFixers;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.fix.ChoiceFix;

import java.util.Optional;

public class InkStorageBlockFix extends ChoiceFix {
    public InkStorageBlockFix(Schema outputSchema) {
        super(outputSchema, false, "SpectrumInkStorageBlockFix", TypeReferences.BLOCK_ENTITY, "spectrum:color_picker");
    }

    @Override
    protected Typed<?> transform(Typed<?> inputType) {
        return inputType.update(DSL.remainderFinder(), dynamic -> {
            Optional<? extends Dynamic<?>> optionalInkStorage = dynamic.get("InkStorage").result();
            if (optionalInkStorage.isPresent()) {
                Dynamic<?> processed = SpectrumDataFixers.processMultiple(optionalInkStorage.get());
                if (processed != null) {
                    return dynamic.set("InkStorage", processed);
                } else {
                    SpectrumCommon.logWarning("Something very wrong may have happened while datafixing ink storage blocks!");
                }
            }

            return dynamic;
        });
    }
}
