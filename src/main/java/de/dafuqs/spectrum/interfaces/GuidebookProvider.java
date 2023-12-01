package de.dafuqs.spectrum.interfaces;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface GuidebookProvider {
    void openGuidebook(ServerPlayerEntity serverPlayerEntity);
    void openGuidebook(ServerPlayerEntity serverPlayerEntity, Identifier entry, int page);
}
