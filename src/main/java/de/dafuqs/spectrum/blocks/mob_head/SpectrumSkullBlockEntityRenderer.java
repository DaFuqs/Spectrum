package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Environment(EnvType.CLIENT)
public class SpectrumSkullBlockEntityRenderer implements BlockEntityRenderer<SpectrumSkullBlockEntity> {
	
	private static EntityModelLoader entityModelLoader;
	private static SkullEntityModel model;
	
	public SpectrumSkullBlockEntityRenderer(BlockEntityRendererFactory.Context renderContext) {
		model = new SkullEntityModel(entityModelLoader.getModelPart(EntityModelLayers.PLAYER_HEAD));
	}
	
	public static void setModelLoader(EntityModelLoader entityModelLoader) {
		SpectrumSkullBlockEntityRenderer.entityModelLoader = entityModelLoader;
	}
	
	public static EntityModelLoader getEntityModelLoader() {
		return entityModelLoader;
	}
	
	public static void renderSkull(@Nullable Direction direction, float yaw, float animationProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, RenderLayer renderLayer) {
		matrices.push();
		if (direction == null) {
			matrices.translate(0.5D, 0.0D, 0.5D);
		} else {
			float f = 0.25F;
			matrices.translate((0.5F - (float) direction.getOffsetX() * 0.25F), 0.25D, (0.5F - (float) direction.getOffsetZ() * 0.25F));
		}
		
		matrices.scale(-1.0F, -1.0F, 1.0F);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		model.setHeadRotation(animationProgress, yaw, 0.0F);
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}
	
	public static RenderLayer getRenderLayer(SkullBlock.SkullType type) {
		Identifier identifier = new Identifier(SpectrumCommon.MOD_ID, "textures/entity/mob_head/" + type.toString().toLowerCase(Locale.ROOT) + ".png");
		RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(identifier);
		if (renderLayer == null) {
			return RenderLayer.getEntityCutoutNoCullZOffset(new Identifier("textures/entity/zombie/zombie.png"));
		} else {
			return renderLayer;
		}
	}
	
	public void render(SpectrumSkullBlockEntity spectrumSkullBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
		BlockState blockState = spectrumSkullBlockEntity.getCachedState();
		boolean bl = blockState.getBlock() instanceof WallSkullBlock;
		Direction direction = bl ? blockState.get(WallSkullBlock.FACING) : null;
		float h = 22.5F * (float) (bl ? (2 + direction.getHorizontal()) * 4 : blockState.get(SkullBlock.ROTATION));
		SpectrumSkullBlock.SpectrumSkullBlockType skullType = spectrumSkullBlockEntity.getSkullType();
		if (skullType == null) {
			skullType = SpectrumSkullBlock.SpectrumSkullBlockType.PIG;
		}
		RenderLayer renderLayer = getRenderLayer(skullType);
		renderSkull(direction, h, 0, matrixStack, vertexConsumerProvider, light, renderLayer);
	}
	
}