package de.dafuqs.spectrum.render;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class SpectrumShaderPrograms {

    public static final Identifier GENERIC_STARS_ID = SpectrumCommon.locate("rendertype_generic_stars");
    private static ShaderProgram genericStarsProgram;

    public static void initPrograms() {
        CoreShaderRegistrationCallback.EVENT.register(context -> {
            context.register(GENERIC_STARS_ID, VertexFormats.POSITION_COLOR, (shaderProgram -> genericStarsProgram = shaderProgram));
        });
    }

    public static ShaderProgram getGenericStars() {
        return genericStarsProgram;
    }
}
