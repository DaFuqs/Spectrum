package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ExperienceOrbEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
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
	
	@Override
	public void render(EnchanterBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {

		// The item lying on top of the enchanter
		ItemStack stack = blockEntity.inventory.getStack(0);
		if(!stack.isEmpty() && blockEntity.getItemFacingDirection() != null) {
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
		if(!experienceItemStack.isEmpty() && experienceItemStack.getItem() instanceof ExperienceStorageItem) {
			render(blockEntity.getWorld(), Support.getExperienceOrbSizeForExperience(ExperienceStorageItem.getStoredExperience(experienceItemStack)), tickDelta, matrixStack, vertexConsumerProvider, experienceSpriteBrightness);
		}
	}
	
	public void render(World world, int orbSize, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		
		float h = (float)(orbSize % 4 * 16) / 64.0F;
		float k = (float)(orbSize % 4 * 16 + 16) / 64.0F;
		float l = (float)(orbSize / 4 * 16) / 64.0F;
		float m = (float)(orbSize / 4 * 16 + 16) / 64.0F;
		float r = ((float) world.getTime() + tickDelta) / 2.0F;
		int s = (int)((MathHelper.sin(r + 0.0F) + 1.0F) * 0.5F * 255.0F);
		int u = (int)((MathHelper.sin(r + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
		
		matrixStack.translate(0.5D, 2.5D, 0.5D);
		matrixStack.multiply(dispatcher.camera.getRotation());
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
		
		float scale = 0.5F + (float) (Math.sin((world.getTime() + tickDelta) / 8.0) / 8.0);
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
	
	private static void vertex(@NotNull VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, int red, int green, int blue, float u, float v, int light) {
		vertexConsumer.vertex(positionMatrix, x, y, 0.0F).color(red, green, blue, 128).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
	}
	
}
