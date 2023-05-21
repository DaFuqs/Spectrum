package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class EggLayingWoolyPigWoolFeatureRenderer extends FeatureRenderer<EggLayingWoolyPigEntity, EggLayingWoolyPigEntityModel> {
	
	private final EggLayingWoolyPigHatEntityModel hat;
	private final EggLayingWoolyPigWoolEntityModel wool;
	
	public EggLayingWoolyPigWoolFeatureRenderer(EggLayingWoolyPigEntityRenderer context, EntityModelLoader loader) {
		super(context);
		this.hat = new EggLayingWoolyPigHatEntityModel(loader.getModelPart(SpectrumModelLayers.WOOLY_PIG_HAT));
		this.wool = new EggLayingWoolyPigWoolEntityModel(loader.getModelPart(SpectrumModelLayers.WOOLY_PIG_WOOL));
	}
	
	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, EggLayingWoolyPigEntity entity, float f, float g, float h, float j, float k, float l) {
		if (entity.isInvisible()) {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			boolean bl = minecraftClient.hasOutline(entity);
			if (bl) {
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(EggLayingWoolyPigEntityRenderer.TEXTURE));
				if (!entity.isHatless()) {
					this.getContextModel().copyStateTo(this.hat);
					this.hat.animateModel(entity, f, g, h);
					this.hat.setAngles(entity, f, g, j, k, l);
					this.hat.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0F), 0.0F, 0.0F, 0.0F, 1.0F);
				}
				if (!entity.isSheared()) {
					this.getContextModel().copyStateTo(this.wool);
					this.wool.animateModel(entity, f, g, h);
					this.wool.setAngles(entity, f, g, j, k, l);
					this.wool.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0F), 0.0F, 0.0F, 0.0F, 1.0F);
				}
			}
		} else {
			float[] rgbColor = EggLayingWoolyPigEntity.getRgbColor(entity.getColor());
			if (!entity.isHatless()) {
				this.getContextModel().copyStateTo(this.hat);
				this.hat.animateModel(entity, f, g, h);
				this.hat.setAngles(entity, f, g, j, k, l);
				render(this.getContextModel(), this.hat, getTexture(entity), matrixStack, vertexConsumerProvider, i, entity, f, g, j, k, l, h, rgbColor[0], rgbColor[1], rgbColor[2]);
			}
			if (!entity.isSheared()) {
				render(this.getContextModel(), this.wool, getTexture(entity), matrixStack, vertexConsumerProvider, i, entity, f, g, j, k, l, h, rgbColor[0], rgbColor[1], rgbColor[2]);
			}
		}
	}
	
	@Override
	public Identifier getTexture(EggLayingWoolyPigEntity entity) {
		return EggLayingWoolyPigEntityRenderer.TEXTURE;
	}
	
}
