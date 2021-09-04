package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SpectrumRenderLayers extends RenderLayer {

    // Deeper down portal
    public static final Identifier OVERLAY_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/entity/portal/deeper_down_portal_overlay.png");
    public static final Identifier PORTAL_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/entity/portal/deeper_down_portal.png");
    public static final RenderLayer DEEPER_DOWN_PORTAL_RENDER_LAYER = RenderLayer.getEndPortal();
            // TODO: Use render layer with custom textures
            /*RenderLayer.of("deeper_down_portal",
            VertexFormats.POSITION,
            VertexFormat.DrawMode.QUADS,
            256, false, false,
            MultiPhaseParameters.builder().shader(END_PORTAL_SHADER)
                    .texture(Textures.create()
                            .add(OVERLAY_TEXTURE, false, false)
                            .add(PORTAL_TEXTURE, false, false)
                            .build()).build(false));*/

    public SpectrumRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

}
