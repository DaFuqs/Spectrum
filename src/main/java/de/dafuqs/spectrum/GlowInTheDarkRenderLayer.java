package de.dafuqs.spectrum;

import de.dafuqs.spectrum.mixin.client.RenderLayerAccessor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

/**
 * Render layer to be used in Spectrums magical renderers.
 * Like glow ink, it glows in the dark, always with max brightness
 */
public class GlowInTheDarkRenderLayer extends RenderLayer {
    
    public GlowInTheDarkRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer get(Identifier texture) {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().texture(new RenderPhase.Texture(texture, false, false)).transparency(Transparency.TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(DISABLE_OVERLAY_COLOR).layering(VIEW_OFFSET_Z_LAYERING).shader(ENERGY_SWIRL_SHADER).build(true);
        return RenderLayerAccessor.invokeOf("spectrum", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, false, multiPhaseParameters);
    }
}