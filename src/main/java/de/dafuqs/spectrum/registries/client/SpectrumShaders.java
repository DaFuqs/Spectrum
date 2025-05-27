package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.gl.*;
import net.minecraft.client.world.*;
import net.minecraft.util.*;

import java.io.*;
import java.util.*;

public class SpectrumShaders {
	
	public static final Identifier COLOR_GRADING_ID = SpectrumCommon.locate("shaders/post/dd_color_grading.json");
	public static Optional<PostEffectProcessor> colorGradingPostProcess = Optional.empty();
	
	private static final String[] COLOR_GRADING_UNIFORMS = new String[] {"Saturation", "Rubedo", "ColorTemperature", "DesaturateThreshold", "BloomThreshold"};
	
	public static Optional<PostEffectProcessor> loadPostProcess(MinecraftClient client, Identifier id) {
		PostEffectProcessor post = null;
		try {
			post = new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), id);
		} catch (IOException e) {
			SpectrumCommon.LOGGER.error("Failed to load post-process shader [{}]", COLOR_GRADING_ID);
			SpectrumCommon.LOGGER.error("", e);
		}
		
		if (post != null)
			post.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
		
		return Optional.ofNullable(post);
	}
	
	public static void updateDimensionShaders(ClientWorld world) {
		if (!world.getRegistryKey().equals(SpectrumDimensions.DIMENSION_KEY))
			return;
		
		colorGradingPostProcess.ifPresent(pps -> {
			for (int i = 0; i < 5; i++) {
				pps.setUniforms(COLOR_GRADING_UNIFORMS[i], DimensionRenderEffects.ColorGrading.GRADING_OUT[i]);
			}
		});
	}
	
	public static void clearDimensionShaders() {
		if (colorGradingPostProcess.isPresent()) {
			colorGradingPostProcess.get().close();
			colorGradingPostProcess = Optional.empty();
		}
	}
	
	public static void resizeShaders(int width, int height) {
		colorGradingPostProcess.ifPresent(pps -> pps.setupDimensions(width, height));
	}
}
