package de.dafuqs.spectrum.interfaces;

import de.dafuqs.spectrum.accessor.WorldRendererAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.WorldRenderer;

public interface Cloakable {

    abstract boolean isCloaked(ClientPlayerEntity clientPlayerEntity, BlockState blockState);
    abstract void setCloaked();
    abstract void setUncloaked();
    abstract boolean wasLastCloaked();
    abstract void setLastCloaked(boolean lastCloaked);

    @Deprecated
    default void checkCloak(BlockState state) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;

        boolean isCloaked = isCloaked(clientPlayerEntity, state);
        if (isCloaked != wasLastCloaked()) {
            if (clientPlayerEntity != null && MinecraftClient.getInstance().world != null) {
                if (MinecraftClient.getInstance().worldRenderer != null && MinecraftClient.getInstance().player != null) {

                    if(isCloaked) {
                        setUncloaked();
                    } else {
                        setCloaked();
                    }

                    WorldRenderer renderer = MinecraftClient.getInstance().worldRenderer;
                    ((WorldRendererAccessor) renderer).rebuildAllChunks();
                }
            }
            setLastCloaked(isCloaked);
        }
    }
}
