package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.helpers.ExperienceHelper;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.NotNull;

public class EnchanterBlockEntityRenderer implements BlockEntityRenderer<de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity> {
	
	protected static RenderLayer LAYER;
	protected static Identifier TEXTURE;
	protected static EntityRenderDispatcher dispatcher;
	protected int experienceSpriteBrightness = 15728768; // max brightness
	protected double itemStackRenderHeight = 0.65F;
	
	public EnchanterBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		TEXTURE = new Identifier("textures/entity/experience_orb.png");
		LAYER = RenderLayer.getEntityTranslucentCull(TEXTURE);
		dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
	}
	
	private static void vertex(@NotNull VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, int red, int green, int blue, float u, float v, int light) {
		vertexConsumer.vertex(positionMatrix, x, y, 0.0F).color(red, green, blue, 128).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
	}
	
	@Override
	public void render(EnchanterBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		
		// The item lying on top of the enchanter
		ItemStack stack = blockEntity.inventory.getStack(0);
		if (!stack.isEmpty() && blockEntity.getItemFacingDirection() != null) {
			Direction itemFacingDirection = blockEntity.getItemFacingDirection();
			
			matrixStack.push();
			// item stack rotation
			switch (itemFacingDirection) {
				case NORTH -> {
					matrixStack.translate(0.5, itemStackRenderHeight, 0.7);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
					matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
				}
				case SOUTH -> { // perfect
					matrixStack.translate(0.5, itemStackRenderHeight, 0.3);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
				}
				case EAST -> {
					matrixStack.translate(0.3, itemStackRenderHeight, 0.5);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
					matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(270));
				}
				case WEST -> {
					matrixStack.translate(0.7, itemStackRenderHeight, 0.5);
					matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
					matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90));
					matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
				}
			}
			
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, 0);
			matrixStack.pop();
		}
		
		// The Experience Item rendered in the air
		ItemStack experienceItemStack = blockEntity.inventory.getStack(1);
		if (!experienceItemStack.isEmpty() && experienceItemStack.getItem() instanceof ExperienceStorageItem) {
			renderExperienceOrb((float) blockEntity.getWorld().getTime() + tickDelta, ExperienceHelper.getExperienceOrbSizeForExperience(ExperienceStorageItem.getStoredExperience(experienceItemStack)), matrixStack, vertexConsumerProvider, experienceSpriteBrightness);
		}
		
		//renderLight(blockEntity, matrixStack, vertexConsumerProvider, 3, 0, null, null, null, null);
	}
	
	public void renderExperienceOrb(float timeWithTickDelta, int orbSize, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		
		float h = (float) (orbSize % 4 * 16) / 64.0F;
		float k = (float) (orbSize % 4 * 16 + 16) / 64.0F;
		float l = (float) (orbSize / 4 * 16) / 64.0F;
		float m = (float) (orbSize / 4 * 16 + 16) / 64.0F;
		float r = timeWithTickDelta / 2.0F;
		int s = (int) ((MathHelper.sin(r + 0.0F) + 1.0F) * 0.5F * 255.0F);
		int u = (int) ((MathHelper.sin(r + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
		
		matrixStack.translate(0.5D, 2.5D, 0.5D);
		matrixStack.multiply(dispatcher.camera.getRotation());
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
		
		float scale = 0.5F + (float) (Math.sin(timeWithTickDelta / 8.0) / 8.0);
		matrixStack.scale(scale, scale, scale);
		
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
		MatrixStack.Entry entry = matrixStack.peek();
		Matrix4f matrix4f = entry.getPositionMatrix();
		Matrix3f matrix3f = entry.getNormalMatrix();
		
		vertex(vertexConsumer, matrix4f, matrix3f, -0.5F, -0.25F, s, 255, u, h, m, i);
		vertex(vertexConsumer, matrix4f, matrix3f, 0.5F, -0.25F, s, 255, u, k, m, i);
		vertex(vertexConsumer, matrix4f, matrix3f, 0.5F, 0.75F, s, 255, u, k, l, i);
		vertex(vertexConsumer, matrix4f, matrix3f, -0.5F, 0.75F, s, 255, u, h, l, i);
		
		matrixStack.pop();
	}
	
	/*
	public void renderUpgradeLines(EnchanterBlockEntity enchanterBlockEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
	                               int xOffsets, int yOffsets,
	                               @Nullable Upgradeable.UpgradeType upgradeType1, @Nullable Upgradeable.UpgradeType upgradeType2, @Nullable Upgradeable.UpgradeType upgradeType3, @Nullable Upgradeable.UpgradeType upgradeType4) {
		
		BlockPos sourcePos = new BlockPos(0, 0, 0);
		Vec3i sizeVector = new Vec3i(3, 3, 3);
		
		double sourceX = sourcePos.getX() + 0.5;
		double sourceY = sourcePos.getY() + 0.5;
		double sourceZ = sourcePos.getZ() + 0.5;
		
		double y2 = sourceY + sizeVector.getY();
		double k = sizeVector.getX();
		double l = sizeVector.getZ();
		
		double x1 = k < 0.0D ? sourceX + 1.0D : sourceX;
		double z1 = l < 0.0D ? sourceZ + 1.0D : sourceZ;
		double x2 = x1 + k;
		double z2 = z1 + l;
		
		float alpha = 1.0F;
		float red = 0.9F;
		float xAxisRed = 0.5F;
		
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
		drawLines(matrixStack, vertexConsumer, (float) x1, (float) sourceY, (float) z1, (float) x2, (float) y2, (float) z2, red, red, red, alpha, xAxisRed, xAxisRed, xAxisRed);
	}
	
	public static void drawLines(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float red, float green, float blue, float alpha, float xAxisRed, float yAxisGreen, float zAxisBlue) {
		Matrix4f matrix4f = matrices.peek().getPositionMatrix();
		Matrix3f matrix3f = matrices.peek().getNormalMatrix();
		
		matrices.push();
		matrices.scale(50.0F, 50.0F, 50.0F);
		
		vertexConsumer.vertex(matrix4f, x1, y1, z1).color(red, red, red, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, x1-3, y1 + 0.5F, z1-3).color(red, red, red, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).next();
		
		vertexConsumer.vertex(matrix4f, x1, y1, z1).color(red, red, red, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, x1-3, y1 + 0.5F, z1+3).color(red, red, red, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		
		vertexConsumer.vertex(matrix4f, x1, y1, z1).color(red, red, red, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).next();
		vertexConsumer.vertex(matrix4f, x1+3, y1 + 0.5F, z1-3).color(red, red, red, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).next();
		
		vertexConsumer.vertex(matrix4f, x1, y1, z1).color(red, red, red, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, x1+3, y1 + 0.5F, z1+3).color(red, red, red, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		
		matrices.pop();
		
		/*
		vertexConsumer.vertex(matrix4f, i, j, h).color(red, green, blue, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, f, j, h).color(red, green, blue, alpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, f, j, h).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).next();
		vertexConsumer.vertex(matrix4f, f, j, k).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).next();
		vertexConsumer.vertex(matrix4f, f, j, k).color(red, green, blue, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, f, g, k).color(red, green, blue, alpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, f, g, k).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, i, g, k).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, i, g, k).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).next();
		vertexConsumer.vertex(matrix4f, i, g, h).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).next();
		vertexConsumer.vertex(matrix4f, f, j, k).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, i, j, k).color(red, green, blue, alpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, i, g, k).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, i, j, k).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, i, j, h).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).next();
		vertexConsumer.vertex(matrix4f, i, j, k).color(red, green, blue, alpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).next();
		
	}*/
	/*
	public static void renderLight(MatrixStack matrixStack, VertexConsumerProvider buffer, Color color, long seed, int minScale, float scale, int count, int light) {
		Random rand = new Random();
		rand.setSeed(seed);
		
		int orbSize = 2;
		float u = (float)(orbSize % 4 * 16) / 64.0F;
		float v = (float)(orbSize % 4 * 16 + 16) / 64.0F;
		
		float f1 = MinecraftClient.getInstance().world.getTime() / 400.0F;
		float f2 = 0.0F;
		int alpha = 128;
		//int alpha = (int) (255.0F * (1.0F - f2));
		
		VertexConsumer vb = buffer.getBuffer(LAYER);
		MatrixStack.Entry entry = matrixStack.peek();
		Matrix3f normalMatrix = entry.getNormalMatrix();
		Matrix4f positionMatrix = entry.getPositionMatrix();
		
		matrixStack.push();
		for (int i = 0; i < count; i++) {
			matrixStack.push();
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(rand.nextFloat() * 360.0F));
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rand.nextFloat() * 360.0F));
			matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rand.nextFloat() * 360.0F));
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(rand.nextFloat() * 360.0F));
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rand.nextFloat() * 360.0F));
			matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rand.nextFloat() * 360.0F + f1 * 360.0F));

			float fa = rand.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
			float f4 = rand.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
			float fa1 = 30.0F / (Math.min(minScale, 10 * scale) / 10.0F);
			fa /= fa1;
			f4 /= fa1;
			
			
			vb.vertex(positionMatrix, 0F,      0F, 0F)        .color(color.getRed(), color.getGreen(), color.getBlue(), alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, 0F,      0F, 0F)        .color(color.getRed(), color.getGreen(), color.getBlue(), alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, -0.7F * f4, fa, -0.5F * f4).color(color.getRed(), color.getGreen(), color.getBlue(), 0).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix,  0.7F * f4, fa, -0.5F * f4).color(color.getRed(), color.getGreen(), color.getBlue(), 0).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, 0F,     0F, 0F)        .color(color.getRed(), color.getGreen(), color.getBlue(), alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, 0F,     0F, 0F)        .color(color.getRed(), color.getGreen(), color.getBlue(), alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, 0.7F * f4, fa, -0.5F * f4).color(color.getRed(), color.getGreen(), color.getBlue(), 0).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, 0F,        fa,    1F * f4).color(color.getRed(), color.getGreen(), color.getBlue(), 0).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, 0F,      0F, 0F)        .color(color.getRed(), color.getGreen(), color.getBlue(), alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, 0F,      0F, 0F)        .color(color.getRed(), color.getGreen(), color.getBlue(), alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, 0F,         fa,    1F * f4).color(color.getRed(), color.getGreen(), color.getBlue(), 0).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			vb.vertex(positionMatrix, -0.7F * f4, fa, -0.5F * f4).color(color.getRed(), color.getGreen(), color.getBlue(), 0).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
			
			matrixStack.pop();
		}
		matrixStack.pop();
		
		refreshDrawing(buffer, RenderLayer.getTranslucent());
	}
	
	public static void refreshDrawing(VertexConsumerProvider vb, RenderLayer type) {
		if (vb instanceof BufferBuilder) {
			type.draw((BufferBuilder) vb, 0, 0, 0);
			((BufferBuilder) vb).begin(type.getDrawMode(), type.getVertexFormat());
		}
	}*/
	
}
