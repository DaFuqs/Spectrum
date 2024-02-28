package de.dafuqs.spectrum.interfaces;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public interface GuidebookProvider {
    void openGuidebook(ClientPlayerEntity serverPlayerEntity);
    void openGuidebook(ClientPlayerEntity serverPlayerEntity, Identifier entry, int page);
}
