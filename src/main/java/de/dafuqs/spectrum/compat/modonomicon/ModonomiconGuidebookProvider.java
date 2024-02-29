package de.dafuqs.spectrum.compat.modonomicon;

import com.klikli_dev.modonomicon.client.gui.BookGuiManager;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.api.gui.GuidebookProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class ModonomiconGuidebookProvider implements GuidebookProvider {

    public final Identifier GUIDEBOOK_ID = SpectrumCommon.locate("guidebook");

    @Override
    public void openGuidebook(ClientPlayerEntity clientPlayerEntity) {
        BookGuiManager.get().openBook(GUIDEBOOK_ID);
    }

    @Override
    public void openGuidebook(ClientPlayerEntity clientPlayerEntity, Identifier entry, int page) {
        BookGuiManager.get().openEntry(GUIDEBOOK_ID, entry, page);
    }

}
