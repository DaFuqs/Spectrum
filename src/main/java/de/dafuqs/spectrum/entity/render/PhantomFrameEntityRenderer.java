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

	// TODO - These need a namespace now, and probably need moving
	public static final ModelIdentifier NORMAL_FRAME_MODEL_IDENTIFIER = new ModelIdentifier("item_frame", "map=false");
	public static final ModelIdentifier MAP_FRAME_MODEL_IDENTIFIER = new ModelIdentifier("item_frame", "map=true");
	public static final ModelIdentifier GLOW_FRAME_MODEL_IDENTIFIER = new ModelIdentifier("glow_item_frame", "map=false");
	public static final ModelIdentifier MAP_GLOW_FRAME_MODEL_IDENTIFIER = new ModelIdentifier("glow_item_frame", "map=true");

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
	public void render(PhantomFrameEntity itemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		super.render(itemFrameEntity, f, g, matrixStack, vertexConsumerProvider, light);
		matrixStack.push();
		Direction direction = itemFrameEntity.getHorizontalFacing();
		Vec3d vec3d = this.getPositionOffset(itemFrameEntity, g);
		matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
		double d = 0.46875D;
		matrixStack.translate((double) direction.getOffsetX() * d, (double) direction.getOffsetY() * d, (double) direction.getOffsetZ() * d);
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(itemFrameEntity.getPitch()));
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - itemFrameEntity.getYaw()));
		boolean isInvisible = itemFrameEntity.isInvisible();
		ItemStack itemStack = itemFrameEntity.getHeldItemStack();
		if (!isInvisible) {
			BlockRenderManager blockRenderManager = this.client.getBlockRenderManager();
			BakedModelManager bakedModelManager = blockRenderManager.getModels().getModelManager();
			ModelIdentifier modelIdentifier = this.getModelId(itemFrameEntity, itemStack);
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
			
			int bakedModelManager = isRenderingMap ? itemFrameEntity.getRotation() % 4 * 2 : itemFrameEntity.getRotation();
			matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) bakedModelManager * 360.0F / 8.0F));
			if (isRenderingMap) {
				matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
				float scale = 0.0078125F;
				matrixStack.scale(scale, scale, scale);
				matrixStack.translate(-64.0D, -64.0D, 0.0D);
				Integer mapId = FilledMapItem.getMapId(itemStack);
				MapState mapState = FilledMapItem.getMapState(mapId, itemFrameEntity.world);
				matrixStack.translate(0.0D, 0.0D, -1.0D);
				if (mapState != null) {
					int finalLight = this.getLight(itemFrameEntity, light);
					this.client.gameRenderer.getMapRenderer().draw(matrixStack, vertexConsumerProvider, mapId, mapState, true, finalLight);
				}
			} else {
				int finalLight = this.getLight(itemFrameEntity, light);
				float scale = 0.85F;
				matrixStack.scale(scale, scale, scale);
				this.itemRenderer.renderItem(itemStack, ModelTransformationMode.FIXED, finalLight, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, itemFrameEntity.getId());
			}
		}

		matrixStack.pop();
	}

	private int getLight(PhantomFrameEntity itemFrame, int regularLight) {
		boolean isGlowPhantomFrame = itemFrame.getType() == SpectrumEntityTypes.GLOW_PHANTOM_FRAME;
		boolean isRedstonePowered = itemFrame.isRedstonePowered();
		return isGlowPhantomFrame == isRedstonePowered ? regularLight : 15728850;
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