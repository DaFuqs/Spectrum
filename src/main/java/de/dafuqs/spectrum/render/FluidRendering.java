package de.dafuqs.spectrum.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.crafting.*;
import org.joml.*;

import java.util.*;

public class FluidRendering {
	
	public static void renderFluid(VertexConsumer builder, Matrix4f pos, TextureAtlasSprite sprite, int light, int overlay, float x1, float x2, float y, float z1, float z2, int[] color) {
		x1 /= 16;
		x2 /= 16;
		z1 /= 16;
		z2 /= 16;
		
		final float u1 = sprite.getU0();
		final float u2 = sprite.getU1();
		final float v1 = sprite.getV0();
		final float v2 = sprite.getV1();
		builder.addVertex(pos, x1, y, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v2).setOverlay(overlay).setLight(light).setNormal(0f, 1f, 0f);
		builder.addVertex(pos, x2, y, z2).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v2).setOverlay(overlay).setLight(light).setNormal(0f, 1f, 0f);
		builder.addVertex(pos, x2, y, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u2, v1).setOverlay(overlay).setLight(light).setNormal(0f, 1f, 0f);
		builder.addVertex(pos, x1, y, z1).setColor(color[1], color[2], color[3], color[0]).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(0f, 1f, 0f);
	}
	
	public static int[] unpackColor(int color) {
		final int[] colors = new int[4];
		colors[0] = color >> 24 & 0xff; // alpha
		colors[1] = color >> 16 & 0xff; // red
		colors[2] = color >> 8 & 0xff; // green
		colors[3] = color & 0xff; // blue
		return colors;
	}
	
	// TODO: move to modonomicon helper class
	public static Ingredient fluidIngredientAsBucket(FluidIngredient ingredient) {
		return Ingredient.of(Arrays.stream(ingredient.getStacks())
				.map(stack -> stack.getFluid()
						.getBucket()
						.getDefaultInstance()));
	}
	
}
