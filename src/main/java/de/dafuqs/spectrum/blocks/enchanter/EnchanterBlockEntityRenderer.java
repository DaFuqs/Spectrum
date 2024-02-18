package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;
import org.joml.Math;
import org.joml.*;

public class EnchanterBlockEntityRenderer implements BlockEntityRenderer<de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity> {

	protected static final double ITEM_STACK_RENDER_HEIGHT = 0.95F;
	
	protected static RenderLayer layer;
	protected static Identifier texture;
	protected static EntityRenderDispatcher dispatcher;
	
	public EnchanterBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		texture = new Identifier("textures/entity/experience_orb.png");
		layer = RenderLayer.getEntityTranslucent(texture);
		dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
	}
	
	private static void vertex(@NotNull VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, int red, int green, int blue, float u, float v, int light) {
		vertexConsumer.vertex(positionMatrix, x, y, 0.0F).color(red, green, blue, 128).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
	}
	
	@Override
	public void render(EnchanterBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		
		// The item lying on top of the enchanter
		ItemStack stack = blockEntity.getStack(0);
		if (!stack.isEmpty() && blockEntity.getItemFacingDirection() != null) {
			Direction itemFacingDirection = blockEntity.getItemFacingDirection();
			
			matrixStack.push();
			// item stack rotation
			switch (itemFacingDirection) {
				case NORTH -> {
					matrixStack.translate(0.5, ITEM_STACK_RENDER_HEIGHT, 0.7);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
					matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
				}
				case SOUTH -> { // perfect
					matrixStack.translate(0.5, ITEM_STACK_RENDER_HEIGHT, 0.3);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
				}
				case EAST -> {
					matrixStack.translate(0.3, ITEM_STACK_RENDER_HEIGHT, 0.5);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
					matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(270));
				}
				case WEST -> {
					matrixStack.translate(0.7, ITEM_STACK_RENDER_HEIGHT, 0.5);
					matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
					matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
					matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
				}
				default -> { }
			}
			
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
			matrixStack.pop();
		}
		
		// The Experience Item rendered in the air
		ItemStack experienceItemStack = blockEntity.getStack(1);
		if (!experienceItemStack.isEmpty() && experienceItemStack.getItem() instanceof ExperienceStorageItem) {
			renderExperienceOrb(
					(float) (blockEntity.getWorld().getTime() % 50000) + tickDelta,
					ExperienceHelper.getExperienceOrbSizeForExperience(ExperienceStorageItem.getStoredExperience(experienceItemStack)),
					matrixStack, vertexConsumerProvider, LightmapTextureManager.MAX_LIGHT_COORDINATE);
		}
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
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
		
		float scale = 0.5F + (float) (Math.sin(timeWithTickDelta / 8.0) / 8.0);
		matrixStack.scale(scale, scale, scale);
		
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(layer);
		MatrixStack.Entry entry = matrixStack.peek();
		Matrix4f matrix4f = entry.getPositionMatrix();
		Matrix3f matrix3f = entry.getNormalMatrix();
		
		vertex(vertexConsumer, matrix4f, matrix3f, -0.5F, -0.25F, s, 255, u, h, m, i);
		vertex(vertexConsumer, matrix4f, matrix3f, 0.5F, -0.25F, s, 255, u, k, m, i);
		vertex(vertexConsumer, matrix4f, matrix3f, 0.5F, 0.75F, s, 255, u, k, l, i);
		vertex(vertexConsumer, matrix4f, matrix3f, -0.5F, 0.75F, s, 255, u, h, l, i);
		
		matrixStack.pop();
	}

}
