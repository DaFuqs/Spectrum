package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;

@Environment(EnvType.CLIENT)
public class EraserEntityRenderer extends MobRenderer<EraserEntity, EraserEntityModel> {
	
	public static final ResourceLocation TEXTURE = SpectrumCommon.locate("textures/entity/eraser/eraser_base.png");
	
	public EraserEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new EraserEntityModel(context.bakeLayer(SpectrumModelLayers.ERASER)), 0.175F);
		this.addLayer(new EraserOverlayFeatureRenderer(this));
	}
	
	@Override
	public ResourceLocation getTextureLocation(EraserEntity entity) {
		return TEXTURE;
	}
	
}
