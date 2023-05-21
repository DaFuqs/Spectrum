package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.mixin.client.accessors.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class SpectrumRenderLayers {
	
	/**
	 * Render layer to be used in Spectrums magical renderers.
	 * Like glow ink, it glows in the dark, always with max brightness
	 */
	public static class GlowInTheDarkRenderLayer extends RenderLayer {
		
		public GlowInTheDarkRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		}
		
		public static RenderLayer get(Identifier texture) {
			RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().texture(new RenderPhase.Texture(texture, false, false)).transparency(Transparency.TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(DISABLE_OVERLAY_COLOR).layering(VIEW_OFFSET_Z_LAYERING).program(RenderPhase.ENERGY_SWIRL_PROGRAM).build(true);
			return RenderLayerAccessor.invokeOf("spectrum_glow", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, false, multiPhaseParameters);
		}
	}
	
}
