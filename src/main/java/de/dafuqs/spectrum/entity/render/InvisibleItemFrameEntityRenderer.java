package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class InvisibleItemFrameEntityRenderer<T extends ItemFrameEntity> extends ItemFrameEntityRenderer<T> {
	
	public static final ModelIdentifier NORMAL_FRAME_MODEL_IDENTIFIER = new ModelIdentifier("item_frame", "map=false");
	public static final ModelIdentifier MAP_FRAME_MODEL_IDENTIFIER = new ModelIdentifier("item_frame", "map=true");
	public static final ModelIdentifier GLOW_FRAME_MODEL_IDENTIFIER = new ModelIdentifier("glow_item_frame", "map=false");
	public static final ModelIdentifier MAP_GLOW_FRAME_MODEL_IDENTIFIER = new ModelIdentifier("glow_item_frame", "map=true");
	
	private final MinecraftClient client = MinecraftClient.getInstance();
	private final ItemRenderer itemRenderer;
	
	public InvisibleItemFrameEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}
	
	protected int getBlockLight(T itemFrameEntity, BlockPos blockPos) {
		return itemFrameEntity.getType() == SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME ? Math.max(5, super.getBlockLight(itemFrameEntity, blockPos)) : super.getBlockLight(itemFrameEntity, blockPos);
	}
	
	public void render(T itemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(itemFrameEntity, f, g, matrixStack, vertexConsumerProvider, i);
		matrixStack.push();
		Direction direction = itemFrameEntity.getHorizontalFacing();
		Vec3d vec3d = this.getPositionOffset(itemFrameEntity, g);
		matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
		double d = 0.46875D;
		matrixStack.translate((double) direction.getOffsetX() * d, (double) direction.getOffsetY() * d, (double) direction.getOffsetZ() * d);
		matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(itemFrameEntity.getPitch()));
		matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - itemFrameEntity.getYaw()));
		boolean bl = itemFrameEntity.isInvisible();
		ItemStack itemStack = itemFrameEntity.getHeldItemStack();
		if (!bl) {
			BlockRenderManager blockRenderManager = this.client.getBlockRenderManager();
			BakedModelManager bakedModelManager = blockRenderManager.getModels().getModelManager();
			ModelIdentifier modelIdentifier = this.getModelId(itemFrameEntity, itemStack);
			matrixStack.push();
			matrixStack.translate(-0.5D, -0.5D, -0.5D);
			blockRenderManager.getModelRenderer().render(matrixStack.peek(), vertexConsumerProvider.getBuffer(TexturedRenderLayers.getEntitySolid()), null, bakedModelManager.getModel(modelIdentifier), 1.0F, 1.0F, 1.0F, i, OverlayTexture.DEFAULT_UV);
			matrixStack.pop();
		}
		
		if (!itemStack.isEmpty()) {
			boolean blockRenderManager = itemStack.isOf(Items.FILLED_MAP);
			if (bl) {
				matrixStack.translate(0.0D, 0.0D, 0.5D);
			} else {
				matrixStack.translate(0.0D, 0.0D, 0.4375D);
			}
			
			int bakedModelManager = blockRenderManager ? itemFrameEntity.getRotation() % 4 * 2 : itemFrameEntity.getRotation();
			matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float) bakedModelManager * 360.0F / 8.0F));
			if (blockRenderManager) {
				matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
				float modelIdentifier = 0.0078125F;
				matrixStack.scale(modelIdentifier, modelIdentifier, modelIdentifier);
				matrixStack.translate(-64.0D, -64.0D, 0.0D);
				Integer integer = FilledMapItem.getMapId(itemStack);
				MapState mapState = FilledMapItem.getMapState(integer, itemFrameEntity.world);
				matrixStack.translate(0.0D, 0.0D, -1.0D);
				if (mapState != null) {
					int j = this.getLight(itemFrameEntity, 15728850, i);
					this.client.gameRenderer.getMapRenderer().draw(matrixStack, vertexConsumerProvider, integer, mapState, true, j);
				}
			} else {
				int modelIdentifier = this.getLight(itemFrameEntity, 15728880, i);
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, modelIdentifier, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, itemFrameEntity.getId());
			}
		}
		
		matrixStack.pop();
	}
	
	private int getLight(T itemFrame, int glowLight, int regularLight) {
		return itemFrame.getType() == SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME ? glowLight : regularLight;
	}
	
	private ModelIdentifier getModelId(T entity, ItemStack stack) {
		boolean bl = entity.getType() == SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME;
		if (stack.isOf(Items.FILLED_MAP)) {
			return bl ? MAP_GLOW_FRAME_MODEL_IDENTIFIER : MAP_FRAME_MODEL_IDENTIFIER;
		} else {
			return bl ? GLOW_FRAME_MODEL_IDENTIFIER : NORMAL_FRAME_MODEL_IDENTIFIER;
		}
	}
	
	
}