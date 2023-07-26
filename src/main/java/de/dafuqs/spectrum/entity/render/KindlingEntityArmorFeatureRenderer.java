package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class KindlingEntityArmorFeatureRenderer extends FeatureRenderer<KindlingEntity, KindlingEntityModel> {
	
	public static final Identifier TEXTURE_DIAMOND = SpectrumCommon.locate("textures/entity/kindling/armor_diamond.png");
	public static final Identifier TEXTURE_GOLD = SpectrumCommon.locate("textures/entity/kindling/armor_gold.png");
	public static final Identifier TEXTURE_IRON = SpectrumCommon.locate("textures/entity/kindling/armor_iron.png");
	public static final Identifier TEXTURE_LEATHER = SpectrumCommon.locate("textures/entity/kindling/armor_leather.png");
	
	private final KindlingEntityModel model;
	
	public KindlingEntityArmorFeatureRenderer(FeatureRendererContext<KindlingEntity, KindlingEntityModel> context, EntityModelLoader loader) {
		super(context);
		this.model = new KindlingEntityModel(loader.getModelPart(SpectrumModelLayers.KINDLING_ARMOR));
	}
	
	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, KindlingEntity kindlingEntity, float f, float g, float h, float j, float k, float l) {
		ItemStack itemStack = kindlingEntity.getArmorType();
		
		if (itemStack.getItem() instanceof HorseArmorItem horseArmorItem) {
			this.getContextModel().copyStateTo(this.model);
			this.model.animateModel(kindlingEntity, f, g, h);
			this.model.setAngles(kindlingEntity, f, g, j, k, l);
			float red;
			float green;
			float blue;
			if (horseArmorItem instanceof DyeableHorseArmorItem dyeableHorseArmorItem) {
				int color = dyeableHorseArmorItem.getColor(itemStack);
				red = (float) (color >> 16 & 255) / 255.0F;
				green = (float) (color >> 8 & 255) / 255.0F;
				blue = (float) (color & 255) / 255.0F;
			} else {
				red = 1.0F;
				green = 1.0F;
				blue = 1.0F;
			}
			
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(getTextureForArmor(horseArmorItem)));
			this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
		}
	}
	
	public static Identifier getTextureForArmor(HorseArmorItem item) {
		if (item == Items.DIAMOND_HORSE_ARMOR) {
			return TEXTURE_DIAMOND;
		}
		if (item == Items.GOLDEN_HORSE_ARMOR) {
			return TEXTURE_GOLD;
		}
		if (item == Items.IRON_HORSE_ARMOR) {
			return TEXTURE_IRON;
		}
		return TEXTURE_LEATHER;
	}
	
}
