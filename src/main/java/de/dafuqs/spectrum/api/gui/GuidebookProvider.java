package de.dafuqs.spectrum.api.gui;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public interface GuidebookProvider {
    void openGuidebook(ClientPlayerEntity clientPlayerEntity);
    void openGuidebook(ClientPlayerEntity clientPlayerEntity, Identifier entry, int page);
}
