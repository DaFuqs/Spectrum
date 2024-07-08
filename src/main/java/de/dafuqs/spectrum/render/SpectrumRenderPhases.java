package de.dafuqs.spectrum.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

@Environment(EnvType.CLIENT)
public class SpectrumRenderPhases {

    public static final RenderPhase.ShaderProgram STARFIELD_PROGRAM = new RenderPhase.ShaderProgram(SpectrumShaderPrograms::getGenericStars);
    public static final RenderLayer STARFIELD = RenderLayer.of("starfield", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 2097152, false, false, RenderLayer.MultiPhaseParameters.builder().program(STARFIELD_PROGRAM).build(false));

    public static void init() {}
}
