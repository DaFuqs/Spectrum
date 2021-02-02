package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.accessor.WorldRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BuiltChunkStorage;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = WorldRenderer.class, priority = 900)
public abstract class WorldRendererMixin implements WorldRendererAccessor {

    @Shadow
    private BuiltChunkStorage chunks;

    @Shadow public abstract void scheduleTerrainUpdate();

    @Invoker("scheduleChunkRender")
    @Override
    public abstract void scheduleChunkRenderPublic(int x, int y, int z, boolean important);

    /**
     * When triggered on client side lets the client redraw ALL chunks
     * Warning: Costly + LagSpike!
     */
    public void rebuildAllChunks() {
        if (MinecraftClient.getInstance().world != null) {
            if (MinecraftClient.getInstance().worldRenderer != null && MinecraftClient.getInstance().player != null) {
                for(ChunkBuilder.BuiltChunk chunk : this.chunks.chunks) {
                    chunk.scheduleRebuild(true);
                }
                scheduleTerrainUpdate();
            }
        }

    }

}