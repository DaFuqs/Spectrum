package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.saveddata.maps.*;
import net.minecraft.world.phys.*;

public class PhantomFrameEntityRenderer<T extends PhantomFrameEntity> extends ItemFrameRenderer<T> {
	
	public static final ModelResourceLocation NORMAL_FRAME_MODEL_IDENTIFIER = new ModelResourceLocation(ResourceLocation.withDefaultNamespace("item_frame"), "map=false");
	public static final ModelResourceLocation MAP_FRAME_MODEL_IDENTIFIER = new ModelResourceLocation(ResourceLocation.withDefaultNamespace("item_frame"), "map=true");
	public static final ModelResourceLocation GLOW_FRAME_MODEL_IDENTIFIER = new ModelResourceLocation(ResourceLocation.withDefaultNamespace("glow_item_frame"), "map=false");
	public static final ModelResourceLocation MAP_GLOW_FRAME_MODEL_IDENTIFIER = new ModelResourceLocation(ResourceLocation.withDefaultNamespace("glow_item_frame"), "map=true");
	
	private final Minecraft client = Minecraft.getInstance();
	private final ItemRenderer itemRenderer;
	
	public PhantomFrameEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	protected int getBlockLightLevel(T entity, BlockPos blockPos) {
		return entity.getType() == SpectrumEntityTypes.GLOW_PHANTOM_FRAME ? Math.max(5, super.getBlockLightLevel(entity, blockPos)) : super.getBlockLightLevel(entity, blockPos);
	}

	@Override
	public void render(T entity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light) {
		if (this.shouldShowName(entity)) {
			this.renderNameTag(entity, entity.getDisplayName(), poseStack, vertexConsumerProvider, light, tickDelta);
		}
		
		poseStack.pushPose();
		
		Direction direction = entity.getDirection();
		Vec3 vec3d = this.getRenderOffset(entity, tickDelta);
		poseStack.translate(-vec3d.x(), -vec3d.y(), -vec3d.z());
		double d = 0.46875D;
		poseStack.translate((double) direction.getStepX() * d, (double) direction.getStepY() * d, (double) direction.getStepZ() * d);
		poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entity.getYRot()));
		boolean isInvisible = entity.isInvisible();
		ItemStack itemStack = entity.getItem();
		if (!isInvisible) {
			BlockRenderDispatcher blockRenderManager = this.client.getBlockRenderer();
			ModelManager bakedModelManager = blockRenderManager.getBlockModelShaper().getModelManager();
			ModelResourceLocation modelIdentifier = this.getModelId(entity, itemStack);
			poseStack.pushPose();
			poseStack.translate(-0.5D, -0.5D, -0.5D);
			blockRenderManager.getModelRenderer().renderModel(poseStack.last(), vertexConsumerProvider.getBuffer(Sheets.solidBlockSheet()), null, bakedModelManager.getModel(modelIdentifier), 1.0F, 1.0F, 1.0F, light, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}
		
		if (!itemStack.isEmpty()) {
			MapId mapIdComponent = entity.getFramedMapId(itemStack);
			if (isInvisible) {
				poseStack.translate(0.0D, 0.0D, 0.5D);
			} else {
				poseStack.translate(0.0D, 0.0D, 0.4375D);
			}
			
			int renderLight = entity.shouldRenderAtMaxLight() ? LightTexture.FULL_BRIGHT : light;
			
			int j = mapIdComponent != null ? entity.getRotation() % 4 * 2 : entity.getRotation();
			poseStack.mulPose(Axis.ZP.rotationDegrees((float) j * 360.0F / 8.0F));
			if (mapIdComponent != null) {
				poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
				poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
				poseStack.translate(-64.0F, -64.0F, 0.0F);
				MapItemSavedData mapState = MapItem.getSavedData(mapIdComponent, entity.level());
				poseStack.translate(0.0F, 0.0F, -1.0F);
				if (mapState != null) {
					int k = this.getLightVal(entity, 15728850, renderLight);
					Minecraft.getInstance().gameRenderer.getMapRenderer().render(poseStack, vertexConsumerProvider, mapIdComponent, mapState, true, k);
				}
			} else {
				int l = this.getLightVal(entity, 15728880, renderLight);
				poseStack.scale(0.5F, 0.5F, 0.5F);
				this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, l, OverlayTexture.NO_OVERLAY, poseStack, vertexConsumerProvider, entity.level(), entity.getId());
			}
		}
		
		poseStack.popPose();
	}
	
	private ModelResourceLocation getModelId(T entity, ItemStack stack) {
		boolean bl = entity.getType() == SpectrumEntityTypes.GLOW_PHANTOM_FRAME;
		if (stack.is(Items.FILLED_MAP)) {
			return bl ? MAP_GLOW_FRAME_MODEL_IDENTIFIER : MAP_FRAME_MODEL_IDENTIFIER;
		} else {
			return bl ? GLOW_FRAME_MODEL_IDENTIFIER : NORMAL_FRAME_MODEL_IDENTIFIER;
		}
	}


}
