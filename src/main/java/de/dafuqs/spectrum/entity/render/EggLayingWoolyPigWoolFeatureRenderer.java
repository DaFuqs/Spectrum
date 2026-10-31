package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.*;


public class EggLayingWoolyPigWoolFeatureRenderer extends RenderLayer<EggLayingWoolyPigEntity, EggLayingWoolyPigEntityModel> {
	
	private final EggLayingWoolyPigHatEntityModel hat;
	private final EggLayingWoolyPigWoolEntityModel wool;
	
	public EggLayingWoolyPigWoolFeatureRenderer(EggLayingWoolyPigEntityRenderer context, EntityModelSet loader) {
		super(context);
		this.hat = new EggLayingWoolyPigHatEntityModel(loader.bakeLayer(SpectrumModelLayerLocations.WOOLY_PIG_HAT));
		this.wool = new EggLayingWoolyPigWoolEntityModel(loader.bakeLayer(SpectrumModelLayerLocations.WOOLY_PIG_WOOL));
	}
	
	@Override
	public void render(PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int i, EggLayingWoolyPigEntity entity, float f, float g, float h, float j, float k, float l) {
		int bodyFurColor = entity.getRenderedColor();
		int hatColor = entity.hasEatenColoredGrass() ? bodyFurColor : EggLayingWoolyPigEntity.DEFAULT_COLOR;
		
		if (entity.isInvisible()) {
			boolean hasOutline = Minecraft.getInstance().shouldEntityAppearGlowing(entity);
			if (hasOutline) {
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.outline(EggLayingWoolyPigEntityRenderer.TEXTURE));
				if (!entity.isHatless()) {
					this.getParentModel().copyPropertiesTo(this.hat);
					this.hat.prepareMobModel(entity, f, g, h);
					this.hat.setupAnim(entity, f, g, j, k, l);
					this.hat.renderToBuffer(poseStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), hatColor);
				}
				if (!entity.isSheared()) {
					this.getParentModel().copyPropertiesTo(this.wool);
					this.wool.prepareMobModel(entity, f, g, h);
					this.wool.setupAnim(entity, f, g, j, k, l);
					this.wool.renderToBuffer(poseStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), bodyFurColor);
				}
			}
		} else {
			if (!entity.isHatless()) {
				this.getParentModel().copyPropertiesTo(this.hat);
				this.hat.prepareMobModel(entity, f, g, h);
				this.hat.setupAnim(entity, f, g, j, k, l);
				coloredCutoutModelCopyLayerRender(this.getParentModel(), this.hat, getTextureLocation(entity), poseStack, vertexConsumerProvider, i, entity, f, g, j, k, l, h, hatColor);
			}
			if (!entity.isSheared()) {
				coloredCutoutModelCopyLayerRender(this.getParentModel(), this.wool, getTextureLocation(entity), poseStack, vertexConsumerProvider, i, entity, f, g, j, k, l, h, bodyFurColor);
			}
		}
	}
	
	@Override
	public ResourceLocation getTextureLocation(EggLayingWoolyPigEntity entity) {
		return EggLayingWoolyPigEntityRenderer.TEXTURE;
	}
	
}
