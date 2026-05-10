package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.*;

@Environment(EnvType.CLIENT)
public class MarrowRenderer extends SkeletonRenderer<Marrow> {
	
	public static final ResourceLocation MARROW_SKELETON_LOCATION = SpectrumCommon.locate("textures/entity/skeleton/marrow.png");
	public static final ResourceLocation MARROW_CLOTHES_LOCATION = SpectrumCommon.locate("textures/entity/skeleton/marrow_overlay.png");
	
	public MarrowRenderer(EntityRendererProvider.Context context) {
		super(context, SpectrumModelLayers.MARROW, SpectrumModelLayers.MARROW_INNER_ARMOR, SpectrumModelLayers.MARROW_OUTER_ARMOR);
		this.addLayer(new SkeletonClothingLayer<>(this, context.getModelSet(), SpectrumModelLayers.MARROW_OUTER_LAYER, MARROW_CLOTHES_LOCATION));
	}
	
	public ResourceLocation getTextureLocation(Marrow entity) {
		return MARROW_SKELETON_LOCATION;
	}
}
