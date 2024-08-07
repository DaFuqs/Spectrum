package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.decoration.*;
import net.minecraft.item.*;
import net.minecraft.item.map.*;
import net.minecraft.util.math.*;

public class PhantomFrameEntityRenderer<T extends ItemFrameEntity> extends ItemFrameEntityRenderer<PhantomFrameEntity> {

	public static final ModelIdentifier NORMAL_FRAME_MODEL_IDENTIFIER = ModelIdentifier.ofVanilla("item_frame", "map=false");
	public static final ModelIdentifier MAP_FRAME_MODEL_IDENTIFIER = ModelIdentifier.ofVanilla("item_frame", "map=true");
	public static final ModelIdentifier GLOW_FRAME_MODEL_IDENTIFIER = ModelIdentifier.ofVanilla("glow_item_frame", "map=false");
	public static final ModelIdentifier MAP_GLOW_FRAME_MODEL_IDENTIFIER = ModelIdentifier.ofVanilla("glow_item_frame", "map=true");

	private final MinecraftClient client = MinecraftClient.getInstance();
	private final ItemRenderer itemRenderer;

	public PhantomFrameEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	protected int getBlockLight(PhantomFrameEntity itemFrameEntity, BlockPos blockPos) {
		return itemFrameEntity.getType() == SpectrumEntityTypes.GLOW_PHANTOM_FRAME ? Math.max(5, super.getBlockLight(itemFrameEntity, blockPos)) : super.getBlockLight(itemFrameEntity, blockPos);
	}

	@Override
	public void render(PhantomFrameEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		if (this.hasLabel(entity)) {
			this.renderLabelIfPresent(entity, entity.getDisplayName(), matrixStack, vertexConsumerProvider, light);
		}
		
		matrixStack.push();
		
		Direction direction = entity.getHorizontalFacing();
		Vec3d vec3d = this.getPositionOffset(entity, g);
		matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
		double d = 0.46875D;
		matrixStack.translate((double) direction.getOffsetX() * d, (double) direction.getOffsetY() * d, (double) direction.getOffsetZ() * d);
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getPitch()));
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - entity.getYaw()));
		boolean isInvisible = entity.isInvisible();
		ItemStack itemStack = entity.getHeldItemStack();
		if (!isInvisible) {
			BlockRenderManager blockRenderManager = this.client.getBlockRenderManager();
			BakedModelManager bakedModelManager = blockRenderManager.getModels().getModelManager();
			ModelIdentifier modelIdentifier = this.getModelId(entity, itemStack);
			matrixStack.push();
			matrixStack.translate(-0.5D, -0.5D, -0.5D);
			blockRenderManager.getModelRenderer().render(matrixStack.peek(), vertexConsumerProvider.getBuffer(TexturedRenderLayers.getEntitySolid()), null, bakedModelManager.getModel(modelIdentifier), 1.0F, 1.0F, 1.0F, light, OverlayTexture.DEFAULT_UV);
			matrixStack.pop();
		}
		
		if (!itemStack.isEmpty()) {
			boolean isRenderingMap = itemStack.isOf(Items.FILLED_MAP);
			if (isInvisible) {
				matrixStack.translate(0.0D, 0.0D, 0.5D);
			} else {
				matrixStack.translate(0.0D, 0.0D, 0.4375D);
			}
			
			int renderLight = entity.shouldRenderAtMaxLight() ? LightmapTextureManager.MAX_LIGHT_COORDINATE : light;
			
			int bakedModelManager = isRenderingMap ? entity.getRotation() % 4 * 2 : entity.getRotation();
			matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) bakedModelManager * 360.0F / 8.0F));
			if (isRenderingMap) {
				matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
				float scale = 0.0078125F;
				matrixStack.scale(scale, scale, scale);
				matrixStack.translate(-64.0D, -64.0D, 0.0D);
				Integer mapId = FilledMapItem.getMapId(itemStack);
				MapState mapState = FilledMapItem.getMapState(mapId, entity.getWorld());
				matrixStack.translate(0.0D, 0.0D, -1.0D);
				if (mapState != null) {
					this.client.gameRenderer.getMapRenderer().draw(matrixStack, vertexConsumerProvider, mapId, mapState, true, renderLight);
				}
			} else {
				float scale = 0.75F;
				matrixStack.scale(scale, scale, scale);
				this.itemRenderer.renderItem(itemStack, ModelTransformationMode.FIXED, renderLight, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, entity.getWorld(), entity.getId());
			}
		}

		matrixStack.pop();
	}
	
	@Override
	protected boolean hasLabel(PhantomFrameEntity itemFrameEntity) {
		if (MinecraftClient.isHudEnabled() && !itemFrameEntity.getHeldItemStack().isEmpty() && itemFrameEntity.getHeldItemStack().hasCustomName() && this.dispatcher.targetedEntity == itemFrameEntity) {
			double d = this.dispatcher.getSquaredDistanceToCamera(itemFrameEntity);
			float f = itemFrameEntity.isSneaky() ? 32.0F : 64.0F;
			return d < (double) (f * f);
		} else {
			return false;
		}
	}
	
	private ModelIdentifier getModelId(PhantomFrameEntity entity, ItemStack stack) {
		boolean bl = entity.getType() == SpectrumEntityTypes.GLOW_PHANTOM_FRAME;
		if (stack.isOf(Items.FILLED_MAP)) {
			return bl ? MAP_GLOW_FRAME_MODEL_IDENTIFIER : MAP_FRAME_MODEL_IDENTIFIER;
		} else {
			return bl ? GLOW_FRAME_MODEL_IDENTIFIER : NORMAL_FRAME_MODEL_IDENTIFIER;
		}
	}


}