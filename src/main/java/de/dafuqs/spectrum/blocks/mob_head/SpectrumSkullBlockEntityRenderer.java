package de.dafuqs.spectrum.blocks.mob_head;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.mob_head.models.*;
import de.dafuqs.spectrum.entity.render.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class SpectrumSkullBlockEntityRenderer implements BlockEntityRenderer<SpectrumSkullBlockEntity> {
	
	private static Map<SkullBlock.SkullType, SkullBlockEntityModel> MODELS = new HashMap<>();
	
	public SpectrumSkullBlockEntityRenderer(BlockEntityRendererFactory.Context renderContext) {
		MODELS = getModels(renderContext.getLayerRenderDispatcher());
	}
	
	public static Map<SkullBlock.SkullType, SkullBlockEntityModel> getModels(EntityModelLoader modelLoader) {
		ImmutableMap.Builder<SkullBlock.SkullType, SkullBlockEntityModel> builder = ImmutableMap.builder();
		builder.put(SkullBlock.Type.PLAYER, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.PLAYER_HEAD)));
		builder.put(SpectrumSkullBlockType.EGG_LAYING_WOOLY_PIG, new EggLayingWoolyPigHeadModel(modelLoader.getModelPart(SpectrumModelLayers.EGG_LAYING_WOOLY_PIG_HEAD)));
		builder.put(SpectrumSkullBlockType.MONSTROSITY, new MonstrosityHeadModel(modelLoader.getModelPart(SpectrumModelLayers.MONSTROSITY_HEAD)));
		builder.put(SpectrumSkullBlockType.KINDLING, new KindlingHeadModel(modelLoader.getModelPart(SpectrumModelLayers.KINDLING_HEAD)));
		builder.put(SpectrumSkullBlockType.LIZARD, new LizardHeadModel(modelLoader.getModelPart(SpectrumModelLayers.LIZARD_HEAD)));
		builder.put(SpectrumSkullBlockType.GUARDIAN_TURRET, new GuardianTurretHeadModel(modelLoader.getModelPart(SpectrumModelLayers.GUARDIAN_TURRET_HEAD)));
		builder.put(SpectrumSkullBlockType.WARDEN, new WardenHeadModel(modelLoader.getModelPart(SpectrumModelLayers.WARDEN_HEAD)));
		return builder.build();
	}
	
	public static SkullBlockEntityModel getModel(SkullBlock.SkullType skullType) {
		if (MODELS.containsKey(skullType)) {
			return MODELS.get(skullType);
		} else {
			return MODELS.get(SkullBlock.Type.PLAYER);
		}
	}
	
	@Override
	public void render(SpectrumSkullBlockEntity spectrumSkullBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
		BlockState blockState = spectrumSkullBlockEntity.getCachedState();
		boolean bl = blockState.getBlock() instanceof WallSkullBlock;
		Direction direction = bl ? blockState.get(WallSkullBlock.FACING) : null;
		float h = 22.5F * (float) (bl ? (2 + direction.getHorizontal()) * 4 : blockState.get(SkullBlock.ROTATION));
		SpectrumSkullBlockType skullType = spectrumSkullBlockEntity.getSkullType();
		if (skullType == null) {
			skullType = SpectrumSkullBlockType.PIG;
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
	
	public static RenderLayer getRenderLayer(SpectrumSkullBlockType type) {
		Identifier identifier = getTextureIdentifier(type);
		RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(identifier);
		if (renderLayer == null) {
			return RenderLayer.getEntityCutoutNoCullZOffset(new Identifier("textures/entity/zombie/zombie.png"));
		} else {
			return renderLayer;
		}
	}
	
	protected static Identifier getTextureIdentifier(SpectrumSkullBlockType type) {
		switch (type) {
			case EGG_LAYING_WOOLY_PIG -> {
				return EggLayingWoolyPigEntityRenderer.TEXTURE;
			}
			case GUARDIAN_TURRET -> {
				return GuardianTurretEntityRenderer.TEXTURE;
			}
			case MONSTROSITY -> {
				return MonstrosityEntityRenderer.TEXTURE;
			}
			case LIZARD -> {
				return LizardEntityRenderer.TEXTURE;
			}
			case KINDLING -> {
				return KindlingEntityRenderer.TEXTURE;
			}
			case WARDEN -> {
				return new Identifier("textures/entity/warden/warden.png");
			}
			default -> {
				return SpectrumCommon.locate("textures/entity/mob_head/" + type.toString().toLowerCase(Locale.ROOT) + ".png");
			}
		}
	}
	
}