package de.dafuqs.spectrum.blocks.mob_head;

import com.google.common.collect.ImmutableMap;
import de.dafuqs.spectrum.registries.SpectrumModelLayers;
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
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SpectrumSkullBlockEntityRenderer implements BlockEntityRenderer<SpectrumSkullBlockEntity> {
	
	private static Map<SkullBlock.SkullType, SkullBlockEntityModel> MODELS = new HashMap<>();
	
	public SpectrumSkullBlockEntityRenderer(BlockEntityRendererFactory.Context renderContext) {
		MODELS = getModels(renderContext.getLayerRenderDispatcher());
	}
	
	public static Map<SkullBlock.SkullType, SkullBlockEntityModel> getModels(EntityModelLoader modelLoader) {
		ImmutableMap.Builder<SkullBlock.SkullType, SkullBlockEntityModel> builder = ImmutableMap.builder();
		builder.put(SkullBlock.Type.PLAYER, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.PLAYER_HEAD)));
		builder.put(SpectrumSkullBlock.SpectrumSkullBlockType.EGG_LAYING_WOOLY_PIG, new EggLayingWoolyPigHeadModel(modelLoader.getModelPart(SpectrumModelLayers.EGG_LAYING_WOOLY_PIG_HEAD)));
		return builder.build();
	}
	
	public static SkullBlockEntityModel getModel(SkullBlock.SkullType skullType) {
		if (MODELS.containsKey(skullType)) {
			return MODELS.get(skullType);
		} else {
			return MODELS.get(SkullBlock.Type.PLAYER);
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
		SkullBlockEntityModel skullBlockEntityModel = MODELS.get(skullType.getModelType());
		RenderLayer renderLayer = getRenderLayer(skullType);
		renderSkull(direction, h, 0, matrixStack, vertexConsumerProvider, light, skullBlockEntityModel, renderLayer);
	}
	
	public static void renderSkull(@Nullable Direction direction, float yaw, float animationProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SkullBlockEntityModel model, RenderLayer renderLayer) {
		matrices.push();
		if (direction == null) {
			matrices.translate(0.5D, 0.0D, 0.5D);
		} else {
			matrices.translate((0.5F - (float) direction.getOffsetX() * 0.25F), 0.25D, (0.5F - (float) direction.getOffsetZ() * 0.25F));
		}
		
		matrices.scale(-1.0F, -1.0F, 1.0F);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		model.setHeadRotation(animationProgress, yaw, 0.0F);
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}
	
	public static RenderLayer getRenderLayer(SpectrumSkullBlock.SpectrumSkullBlockType type) {
		Identifier identifier = type.getTextureIdentifier();
		RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(identifier);
		if (renderLayer == null) {
			return RenderLayer.getEntityCutoutNoCullZOffset(new Identifier("textures/entity/zombie/zombie.png"));
		} else {
			return renderLayer;
		}
	}
	
}