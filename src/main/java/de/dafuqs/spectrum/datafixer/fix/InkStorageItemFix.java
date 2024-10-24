package de.dafuqs.spectrum.datafixer.fix;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import de.dafuqs.spectrum.datafixer.SpectrumDataFixers;
import net.minecraft.datafixer.fix.ItemNbtFix;

import java.util.List;
import java.util.Optional;

public class InkStorageItemFix extends ItemNbtFix {
    public static final List<String> INK_STORAGE_ITEMS = List.of(
            "spectrum:gloves_of_dawns_grasp",
            "spectrum:heartsingers_reward",
            "spectrum:ink_assortment",
            "spectrum:ink_flask",
            "spectrum:pigment_palette",
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

            // SingleInkStorage
            Optional<? extends Dynamic<?>> optionalColor = dynamicEnergyStore.get("Color").result();
            // IndividualCappedInkStorage
            Optional<? extends Dynamic<?>> optionalMaxEnergyPerColor = dynamicEnergyStore.get("MaxEnergyPerColor").result();
            // TotalCappedInkStorage
            Optional<? extends Dynamic<?>> optionalMaxEnergyTotal = dynamicEnergyStore.get("MaxEnergyTotal").result();

            if (optionalColor.isPresent()) {
                return dynamic.remove("EnergyStore").set("EnergyStore", SpectrumDataFixers.processSingle(dynamicEnergyStore, optionalColor.get()));
            } else if (optionalMaxEnergyPerColor.isPresent()) {
                return dynamic.remove("EnergyStore").set("EnergyStore", SpectrumDataFixers.processMultiple(dynamicEnergyStore));
            } else if (optionalMaxEnergyTotal.isPresent()) {
                return dynamic.remove("EnergyStore").set("EnergyStore", SpectrumDataFixers.processMultiple(dynamicEnergyStore));
            }
        }

        return dynamic;
    }
}
