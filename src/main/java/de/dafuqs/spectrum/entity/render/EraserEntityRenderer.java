package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.EraserEntityModel;
import de.dafuqs.spectrum.registries.client.SpectrumModelLayers;
import net.fabricmc.api.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class EraserEntityRenderer extends MobEntityRenderer<EraserEntity, EraserEntityModel> {
	
	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/eraser/eraser_base.png");
	public static final Identifier TEXTURE_BLINKING = SpectrumCommon.locate("textures/entity/eraser/eraser_blinking.png");
	
	private static final float SCALE = 0.3F;
	
	public EraserEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new EraserEntityModel(context.getPart(SpectrumModelLayers.ERASER)), 0.175F);
		this.addFeature(new EraserOverlayFeatureRenderer(this));
		//this.shadowRadius *= SCALE;
	}

	//No longer useful. Maybe could be repurposed for minor size randomization?
	//@Override
	//		protected void scale(EraserEntity entity, MatrixStack matrixStack, float f) {
	//	matrixStack.scale(SCALE, SCALE, SCALE);
	//}
	
	@Override
	public Identifier getTexture(EraserEntity entity) {
		return TEXTURE; //TODO insects don't have eyelids dummy. Smh my head
	}
	
}
