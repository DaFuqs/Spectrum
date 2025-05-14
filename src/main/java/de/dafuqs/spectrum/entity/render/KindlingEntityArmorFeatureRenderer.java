package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

@Environment(EnvType.CLIENT)
public class KindlingEntityArmorFeatureRenderer extends RenderLayer<KindlingEntity, KindlingEntityModel> {
	
	public static final ResourceLocation TEXTURE_DIAMOND = SpectrumCommon.locate("textures/entity/kindling/armor/diamond.png");
	public static final ResourceLocation TEXTURE_GOLD = SpectrumCommon.locate("textures/entity/kindling/armor/gold.png");
	public static final ResourceLocation TEXTURE_IRON = SpectrumCommon.locate("textures/entity/kindling/armor/iron.png");
	public static final ResourceLocation TEXTURE_LEATHER = SpectrumCommon.locate("textures/entity/kindling/armor/leather.png");
	
	private final KindlingEntityModel model;
	
	public KindlingEntityArmorFeatureRenderer(RenderLayerParent<KindlingEntity, KindlingEntityModel> context, EntityModelSet loader) {
		super(context);
		this.model = new KindlingEntityModel(loader.bakeLayer(SpectrumModelLayers.KINDLING_ARMOR));
	}
	
	@Override
	public void render(PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int i, KindlingEntity kindlingEntity, float f, float g, float h, float j, float k, float l) {
		ItemStack itemStack = kindlingEntity.getBodyArmorItem();
		
		if (itemStack.getItem() instanceof AnimalArmorItem armorItem) {
			this.getParentModel().copyPropertiesTo(this.model);
			this.model.prepareMobModel(kindlingEntity, f, g, h);
			this.model.setupAnim(kindlingEntity, f, g, j, k, l);
			var color = itemStack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(itemStack, DyedItemColor.LEATHER_COLOR)) : -1;
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCull(getTextureForArmor(armorItem)));
			this.model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, color);
		}
	}
	
	public static ResourceLocation getTextureForArmor(AnimalArmorItem item) {
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
