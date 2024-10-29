package de.dafuqs.spectrum.compat.malum;

import com.sammy.malum.client.screen.codex.BookEntry;
import com.sammy.malum.client.screen.codex.pages.EntryReference;
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage;
import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen;
import de.dafuqs.spectrum.compat.SpectrumIntegrationPacks;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class MalumCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
    public void register() {

    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerClient() {
        //couldn't get this to work
     /*   var spiritCrystalAndScytheAddendum = BookEntry.build("spirit_crystals.addendum")
                .addPage(new HeadlineTextPage("spirit_crystals.addendum", "spirit_crystals.addendum.1"))
                .build();
        BookEntry.build("spirit_crystals")
                .addReference(new EntryReference(SpectrumBlocks.PEDESTAL_ALL_BASIC.asItem(), spiritCrystalAndScytheAddendum))
                .build(); */
    }
}
