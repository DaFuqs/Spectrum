package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.PaintProjectileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public class PaintProjectileEntityRenderer extends EntityRenderer<PaintProjectileEntity> {
	
	public PaintProjectileEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	@Override
	public void render(PaintProjectileEntity tntEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		super.render(tntEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
		
		matrixStack.push();
		matrixStack.translate(0.0D, 0.5D, 0.0D);
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
		matrixStack.translate(-0.5D, -0.5D, 0.5D);
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
		TntMinecartEntityRenderer.renderFlashingBlock(Blocks.TNT.getDefaultState(), matrixStack, vertexConsumerProvider, light, false);
		matrixStack.pop();
		super.render(tntEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
	}
	
	@Override
	public Identifier getTexture(PaintProjectileEntity entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
	
	
}
