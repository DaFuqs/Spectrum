package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class EraserEntityRenderer extends MobEntityRenderer<EraserEntity, EraserEntityModel> {
	
	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/eraser/eraser_base.png");
	
	public EraserEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new EraserEntityModel(context.getPart(SpectrumModelLayers.ERASER)), 0.175F);
		this.addFeature(new EraserOverlayFeatureRenderer(this));
	}
	
	@Override
	public Identifier getTexture(EraserEntity entity) {
		return TEXTURE;
	}
	
}
