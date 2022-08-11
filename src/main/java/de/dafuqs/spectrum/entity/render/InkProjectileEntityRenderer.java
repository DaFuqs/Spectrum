package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.entity.entity.InkProjectileEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

public class InkProjectileEntityRenderer extends EntityRenderer<InkProjectileEntity> {
	
	private static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/ink_projectile.png");
	
	public InkProjectileEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	@Override
	public void render(InkProjectileEntity tEntity, float yaw, float tickDelta, MatrixStack ms, VertexConsumerProvider vertexConsumerProvider, int light) {
		ms.push();
		double time = (tEntity.world.getTime() % 24000) + tickDelta + new Random(tEntity.getId()).nextInt(200);
		float a = 0.1F + (tEntity.isInvisible() ? 0 : 1) * 0.8F;
		int alpha = (int) ((0.7 + 0.3 * (Math.sin(time / 5.0) + 0.5) * 2) * a * 255.0);
		Vec3f starColor = InkColor.of(DyeColor.byId(tEntity.getColor())).getColor();
		float scale = 0.75F + 0.1F * (float) Math.sin(time / 10);
		ms.scale(scale, scale, scale);
		
		VertexConsumer buffer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
		ms.push();
		Sprite sprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, TEXTURE).getSprite();
		ms.multiply(MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getRotation());
		ms.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
		renderTexture(ms, buffer, sprite, starColor, alpha, 0);
		
		ms.pop();
		ms.pop();
	}

	@Override
	public Identifier getTexture(InkProjectileEntity entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
	
	private void renderTexture(MatrixStack ms, VertexConsumer buffer, Sprite icon, Vec3f color, int alpha, int overlay) {
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		
		int a = (int) (color.getX() * 255);
		int r = (int) (color.getY() * 255);
		int g = (int) (color.getZ() * 255);
		int b = 180;
		Matrix4f mat = ms.peek().getPositionMatrix();
		// POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL
		buffer.vertex(mat, 0.0F - f5, 0.0F - f6, 0.0F).color(r, g, b, a).texture(f, f3).overlay(overlay).light(alpha).normal(0f, 1f, 0f).next();
		buffer.vertex(mat, f4 - f5, 0.0F - f6, 0.0F).color(r, g, b, a).texture(f1, f3).overlay(overlay).light(alpha).normal(0f, 1f, 0f).next();
		buffer.vertex(mat, f4 - f5, f4 - f6, 0.0F).color(r, g, b, a).texture(f1, f2).overlay(overlay).light(alpha).normal(0f, 1f, 0f).next();
		buffer.vertex(mat, 0.0F - f5, f4 - f6, 0.0F).color(r, g, b, a).texture(f, f2).overlay(overlay).light(alpha).normal(0f, 1f, 0f).next();
	}
	
}
