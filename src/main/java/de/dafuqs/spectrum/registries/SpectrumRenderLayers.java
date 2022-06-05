package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.mixin.client.accessors.RenderLayerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

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
			RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().texture(new RenderPhase.Texture(texture, false, false)).transparency(Transparency.TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(DISABLE_OVERLAY_COLOR).layering(VIEW_OFFSET_Z_LAYERING).shader(ENERGY_SWIRL_SHADER).build(true);
			return RenderLayerAccessor.invokeOf("spectrum_glow", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, false, multiPhaseParameters);
		}
	}
	
	/**
	 * Render layer to be used for the deeper down portal
	 */
	public static class DeeperDownPortalRenderLayer extends RenderLayer {
		
		public static final Identifier OVERLAY_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/entity/portal/deeper_down_portal_overlay.png");
		public static final Identifier PORTAL_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/entity/portal/deeper_down_portal.png");
		
		public DeeperDownPortalRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		}
		
		public static RenderLayer get() {
			RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().shader(END_PORTAL_SHADER).texture(Textures.create()
					.add(OVERLAY_TEXTURE, false, false)
					.add(PORTAL_TEXTURE, false, false)
					.build()).build(false);
			
			return RenderLayerAccessor.invokeOf("spectrum_portal", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, multiPhaseParameters);
		}
	}
	
}
