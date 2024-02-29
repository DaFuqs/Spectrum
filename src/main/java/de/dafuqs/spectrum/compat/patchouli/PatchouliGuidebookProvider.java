package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.api.gui.GuidebookProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.*;
import vazkii.patchouli.api.*;

public class PatchouliGuidebookProvider implements GuidebookProvider {

    public final Identifier GUIDEBOOK_ID = SpectrumCommon.locate("guidebook");

    @Override
    public void openGuidebook(ClientPlayerEntity clientPlayerEntity) {
        PatchouliAPI.get().openBookGUI(GUIDEBOOK_ID);
    }

    @Override
    public void openGuidebook(ClientPlayerEntity clientPlayerEntity, Identifier entry, int page) {
        PatchouliAPI.get().openBookEntry(GUIDEBOOK_ID, entry, page);
    }

}
