package de.dafuqs.spectrum.compat.modonomicon;

import de.dafuqs.spectrum.compat.SpectrumIntegrationPacks;
import de.dafuqs.spectrum.items.GuidebookItem;
import de.dafuqs.spectrum.registries.SpectrumItems;

public class ModonomiconCompat extends SpectrumIntegrationPacks.ModIntegrationPack {

    @Override
    public void register() {
        if (SpectrumItems.GUIDEBOOK instanceof GuidebookItem guidebook) {
            guidebook.registerProvider(new ModonomiconGuidebookProvider());
        }
    }

}
