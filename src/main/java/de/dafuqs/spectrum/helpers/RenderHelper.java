package de.dafuqs.spectrum.helpers;

import com.mojang.blaze3d.systems.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import org.joml.*;

public class RenderHelper {
	
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
		BufferRenderer.draw(builder.end());
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
	
}
