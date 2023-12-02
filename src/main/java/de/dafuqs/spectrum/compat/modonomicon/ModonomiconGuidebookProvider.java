package de.dafuqs.spectrum.compat.modonomicon;

import com.klikli_dev.modonomicon.client.gui.BookGuiManager;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.GuidebookProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ModonomiconGuidebookProvider implements GuidebookProvider {

    public final Identifier GUIDEBOOK_ID = SpectrumCommon.locate("guidebook");

    @Override
    public void openGuidebook(ClientPlayerEntity serverPlayerEntity) {
        BookGuiManager.get().openBook(GUIDEBOOK_ID);
    }

    @Override
    public void openGuidebook(ClientPlayerEntity serverPlayerEntity, Identifier entry, int page) {
        BookGuiManager.get().openEntry(GUIDEBOOK_ID, entry, page);
    }

}
