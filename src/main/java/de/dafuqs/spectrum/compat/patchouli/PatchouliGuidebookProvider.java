package de.dafuqs.spectrum.compat.patchouli;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.GuidebookProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import vazkii.patchouli.api.*;

public class PatchouliGuidebookProvider implements GuidebookProvider {

    public final Identifier GUIDEBOOK_ID = SpectrumCommon.locate("guidebook");

    @Override
    public void openGuidebook(ClientPlayerEntity serverPlayerEntity) {
        //PatchouliAPI.get().openBookGUI(serverPlayerEntity, GUIDEBOOK_ID);
    }

    @Override
    public void openGuidebook(ClientPlayerEntity serverPlayerEntity, Identifier entry, int page) {
        //PatchouliAPI.get().openBookEntry(serverPlayerEntity, GUIDEBOOK_ID, entry, page);
    }

}