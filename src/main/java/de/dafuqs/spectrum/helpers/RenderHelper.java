package de.dafuqs.spectrum.helpers;

import com.mojang.blaze3d.systems.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import org.joml.*;

public class RenderHelper {

	public static final int GREEN_COLOR = 3289650;
    protected static final BufferBuilder builder = Tessellator.getInstance().getBuffer();
	
	/**
	 * Draws a filled triangle
	 * Attention: The points specified have to be ordered in counter-clockwise order, or will now show up at all
	 */
	public static void fillTriangle(MatrixStack matrices, int p1x, int p1y, int p2x, int p2y, int p3x, int p3y, Vector3f color) {
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		float red = color.x();
		float green = color.y();
		float blue = color.z();
		float alpha = 1.0F;
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		builder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
		builder.vertex(matrix, p1x, p1y, 0F).color(red, green, blue, alpha).next();
		builder.vertex(matrix, p2x, p2y, 0F).color(red, green, blue, alpha).next();
		builder.vertex(matrix, p3x, p3y, 0F).color(red, green, blue, alpha).next();
		BufferRenderer.drawWithGlobalProgram(builder.end());
		RenderSystem.disableBlend();
	}
	
	/**
	 * Draws a filled square
	 */
	public static void fillQuad(MatrixStack matrices, int x, int y, int height, int width, Vector3f color) {
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		float red = color.x();
		float green = color.y();
		float blue = color.z();
		float alpha = 1.0F;
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		builder.vertex(matrix, x, y, 0F).color(red, green, blue, alpha).next();
		builder.vertex(matrix, x, y + height, 0F).color(red, green, blue, alpha).next();
		builder.vertex(matrix, x + width, y + height, 0F).color(red, green, blue, alpha).next();
		builder.vertex(matrix, x + width, y, 0F).color(red, green, blue, alpha).next();
		BufferRenderer.drawWithGlobalProgram(builder.end());
		RenderSystem.disableBlend();
	}
	
	
	public static void renderFlatTrans(MatrixStack matrices, VertexConsumer vertices, boolean altAxis, float scale, float alpha, float uvOffset, int overlay) {
		var size = scale / 16F;
		matrices.translate(-size / 2F, -size / 2F, 0);
		
		var peek = matrices.peek();
		var model = peek.getPositionMatrix();
		renderSide(model, vertices, altAxis, alpha, uvOffset, scale, scale, 0, size, 0, size, overlay);
		matrices.translate(size / 2F, size / 2F, 0);
	}
	
	public static void renderFlatTransWithZYOffset(MatrixStack matrices, VertexConsumer vertices, boolean altAxis, float height, float scale, float alpha, float uvOffset, int overlay) {
		height /= 16F;
		var size = scale / 16F;
		matrices.translate(-size / 2F, height, -size / 2F);
		
		var peek = matrices.peek();
		var model = peek.getPositionMatrix();
		renderSide(model, vertices, altAxis, alpha, uvOffset, scale, scale, 0, size, 0, size, overlay);
		matrices.translate(size / 2F, -height, size / 2F);
	}
	
	public static void renderFlatTransWithZYOffsetAndColor(MatrixStack matrices, VertexConsumer vertices, boolean altAxis, float height, float scale, float alpha, float uvOffset, int overlay, float r, float g, float b) {
		height /= 16F;
		var size = scale / 16F;
		matrices.translate(-size / 2F, height, -size / 2F);
		
		var peek = matrices.peek();
		var model = peek.getPositionMatrix();
		renderSide(model, vertices, altAxis, alpha, uvOffset, scale, scale, 0, size, 0, size, r, g, b, overlay);
		matrices.translate(size / 2F, -height, size / 2F);
	}
	
	public static void renderSide(Matrix4f model, VertexConsumer vertices, boolean altAxis, float alpha, float uvOffset, float u, float v, float x1, float x2, float y1, float y2, float r, float g, float b, int overlay) {
		float u1 = uvOffset / 16F, v1 = uvOffset / 16F;
		float u2 = u1 + u / 16F, v2 = v1 + v / 16F;
		
		if (altAxis) {
			vertices.vertex(model, x1, 0, y2).color(r, g, b, alpha).texture(u1, v1).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
			vertices.vertex(model, x2, 0, y2).color(r, g, b, alpha).texture(u2, v1).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
			vertices.vertex(model, x2, 0, y1).color(r, g, b, alpha).texture(u2, v2).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
			vertices.vertex(model, x1, 0, y1).color(r, g, b, alpha).texture(u1, v2).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
		}
		else {
			vertices.vertex(model, x1, y2, 0).color(r, g, b, alpha).texture(u1, v1).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
			vertices.vertex(model, x2, y2, 0).color(r, g, b, alpha).texture(u2, v1).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
			vertices.vertex(model, x2, y1, 0).color(r, g, b, alpha).texture(u2, v2).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
			vertices.vertex(model, x1, y1, 0).color(r, g, b, alpha).texture(u1, v2).overlay(overlay).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 1, 0).next();
		}
	}
	
	public static void renderSide(Matrix4f model, VertexConsumer vertices, boolean altAxis, float alpha, float uvOffset, float u, float v, float x1, float x2, float y1, float y2, int overlay) {
		renderSide(model, vertices, altAxis, alpha, uvOffset, u, v, x1, x2, y1, y2, 1F, 1F, 1F, overlay);
	}
}