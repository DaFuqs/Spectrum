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
	
	private static final Identifier TEXTURE = SpectrumCommon.locate("entity/ink_projectile");
	
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
	
	private void renderTexture(MatrixStack matrixStack, VertexConsumer vertexConsumer, Sprite sprite, Vec3f color, int alpha, int overlay) {
		float minU = sprite.getMinU();
		float maxU = sprite.getMaxU();
		float minV = sprite.getMinV();
		float maxV = sprite.getMaxV();
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		
		int r = (int) (color.getX() * 255.0F);
		int g = (int) (color.getY() * 255.0F);
		int b = (int) (color.getZ() * 255.0F);
		int light = 0xF000F0;
		
		Matrix4f mat = matrixStack.peek().getPositionMatrix();
		// POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL
		vertexConsumer.vertex(mat, 0.0F - f5, 0.0F - f6, 0.0F).color(r, g, b, alpha).texture(minU, maxV).overlay(overlay).light(light).normal(0f, 1f, 0f).next();
		vertexConsumer.vertex(mat, f4 - f5, 0.0F - f6, 0.0F).color(r, g, b, alpha).texture(maxU, maxV).overlay(overlay).light(light).normal(0f, 1f, 0f).next();
		vertexConsumer.vertex(mat, f4 - f5, f4 - f6, 0.0F).color(r, g, b, alpha).texture(maxU, minV).overlay(overlay).light(light).normal(0f, 1f, 0f).next();
		vertexConsumer.vertex(mat, 0.0F - f5, f4 - f6, 0.0F).color(r, g, b, alpha).texture(minU, minV).overlay(overlay).light(light).normal(0f, 1f, 0f).next();
	}
	
}
