package de.dafuqs.spectrum.datafixer.fix;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.datafixer.SpectrumDataFixers;
import net.minecraft.datafixer.fix.ItemNbtFix;

import java.util.List;
import java.util.Optional;

public class InkStorageItemFix extends ItemNbtFix {
    private static final List<String> INK_STORAGE_ITEMS = List.of(
            "spectrum:artists_palette",
            "spectrum:gloves_of_dawns_grasp",
            "spectrum:heartsingers_reward",
            "spectrum:ink_assortment",
            "spectrum:ink_drain",
            "spectrum:ink_flask",
            "spectrum:laurels_of_serenity",
            "spectrum:pigment_palette",
            "spectrum:ring_of_aerial_grace",
            "spectrum:ring_of_denser_steps",
            "spectrum:ring_of_pursuit",
            "spectrum:shieldgrasp_amulet"
    );

    public InkStorageItemFix(Schema outputSchema) {
        super(outputSchema, "SpectrumInkStorageItemFix", INK_STORAGE_ITEMS::contains);
    }

    @Override
    protected <T> Dynamic<T> fixNbt(Dynamic<T> dynamic) {
        Optional<? extends Dynamic<?>> optionalEnergyStore = dynamic.get("EnergyStore").result();
        if (optionalEnergyStore.isPresent()) {
            Dynamic<?> dynamicEnergyStore = optionalEnergyStore.get();
            Dynamic<?> processed = null;

            // SingleInkStorage
            Optional<? extends Dynamic<?>> optionalDynamicColor = dynamic.get("Color").result();
            if (optionalDynamicColor.isPresent()) {
                processed = SpectrumDataFixers.processSingle(dynamicEnergyStore, optionalDynamicColor.get());
            }

            // IndividualCappedInkStorage
            Optional<? extends Dynamic<?>> optionalMaxEnergyPerColor = dynamicEnergyStore.get("MaxEnergyPerColor").result();
            if (optionalMaxEnergyPerColor.isPresent()) {
                processed = SpectrumDataFixers.processMultiple(dynamicEnergyStore);
            }

            // TotalCappedInkStorage
            Optional<? extends Dynamic<?>> optionalMaxEnergyTotal = dynamicEnergyStore.get("MaxEnergyTotal").result();
            if (optionalMaxEnergyTotal.isPresent()) {
                processed = SpectrumDataFixers.processMultiple(dynamicEnergyStore);
            }

            if (processed != null) {
                return dynamic.set("EnergyStore", processed);
            } else {
                SpectrumCommon.logWarning("Something very wrong may have happened while datafixing ink storage items!");
            }
        }

        return dynamic;
    }
}
