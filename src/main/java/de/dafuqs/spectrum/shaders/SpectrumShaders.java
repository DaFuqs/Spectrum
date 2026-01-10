package de.dafuqs.spectrum.shaders;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;

import java.io.*;
import java.util.*;

public class SpectrumShaders {
	
	public static final ResourceLocation COLOR_GRADING_ID = SpectrumCommon.locate("shaders/post/dd_color_grading.json");
	public static Optional<PostChain> colorGradingPostProcess = Optional.empty();
	
	public static final ResourceLocation NOISE_EDGE_ID = SpectrumCommon.locate("shaders/post/noise_edge.json");
	public static Optional<PostChain> noiseEdgePostProcess = Optional.empty();
	
	private static final String[] COLOR_GRADING_UNIFORMS = new String[] {"Saturation", "Rubedo", "ColorTemperature", "DesaturateThreshold", "BloomThreshold"};
	
	public static void initializeShaders(Minecraft client) {
		if (colorGradingPostProcess.isEmpty()) {
			colorGradingPostProcess = SpectrumShaders.loadPostProcess(client, SpectrumShaders.COLOR_GRADING_ID);
		}
		if (noiseEdgePostProcess.isEmpty()) {
			noiseEdgePostProcess = SpectrumShaders.loadPostProcess(client, SpectrumShaders.NOISE_EDGE_ID);
		}
	}
	
	public static Optional<PostChain> loadPostProcess(Minecraft client, ResourceLocation id) {
		PostChain post = null;
		try {
			post = new PostChain(client.getTextureManager(), client.getResourceManager(), client.getMainRenderTarget(), id);
		} catch (IOException e) {
			SpectrumCommon.LOGGER.error("Failed to load post-process shader [{}]", id);
			SpectrumCommon.LOGGER.error("", e);
		}
		
		if (post != null)
			post.resize(client.getWindow().getWidth(), client.getWindow().getHeight());
		
		return Optional.ofNullable(post);
	}
	
	public static void updateShaders(Minecraft client, ClientLevel world) {
		tickNoise(client);
		tickColorGrading(world);
	}
	
	private static void tickColorGrading(ClientLevel world) {
		if (world.dimension().equals(SpectrumDimensions.DIMENSION_KEY)) {
			colorGradingPostProcess.ifPresent(pps -> {
				for (int i = 0; i < 5; i++) {
					pps.setUniform(COLOR_GRADING_UNIFORMS[i], DimensionRenderEffects.ColorGrading.GRADING_OUT[i]);
				}
			});
		}
	}
	
	public static void clearShaders() {
		if (colorGradingPostProcess.isPresent()) {
			colorGradingPostProcess.get().close();
			colorGradingPostProcess = Optional.empty();
		}
		if (noiseEdgePostProcess.isPresent()) {
			noiseEdgePostProcess.get().close();
			noiseEdgePostProcess = Optional.empty();
		}
	}
	
	public static void resizeShaders(int width, int height) {
		colorGradingPostProcess.ifPresent(pps -> pps.resize(width, height));
		noiseEdgePostProcess.ifPresent(pps -> pps.resize(width, height));
	}
	
	public static void tickNoise(Minecraft client) {
		Entity cameraEntity = client.getCameraEntity();
		if (cameraEntity == null || noiseEdgePostProcess.isEmpty()) return;
		
		float intensity = 0.5F;
		
		/*MonstrosityEntity monstrosity = MonstrosityEntity.getTheOneAndOnly();
		if(monstrosity != null) {
			float distance = cameraEntity.distanceTo(monstrosity) - monstrosity.getBbHeight();
			float alpha = 1.0F - distance * 0.05F;
			if(alpha > 0) {
				intensity = alpha;
			}
		}*/
		
		noiseEdgePostProcess.get().setUniform("Intensity", intensity);
		noiseEdgePostProcess.get().setUniform("Time", client.getTimer().getGameTimeDeltaTicks());
	}
	
}
