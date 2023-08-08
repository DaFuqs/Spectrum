package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class EraserEntityRenderer extends SpiderEntityRenderer<EraserEntity> {
	
	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/eraser/eraser.png");
	public static final Identifier TEXTURE_BLINKING = SpectrumCommon.locate("textures/entity/eraser/eraser_blinking.png");
	
	private static final float SCALE = 0.3F;
	
	public EraserEntityRenderer(EntityRendererFactory.Context context) {
		super(context, EntityModelLayers.SPIDER);
		this.shadowRadius *= SCALE;
	}
	
	@Override
	protected void scale(EraserEntity entity, MatrixStack matrixStack, float f) {
		matrixStack.scale(SCALE, SCALE, SCALE);
	}
	
	@Override
	public Identifier getTexture(EraserEntity entity) {
		return entity.world.getTime() % 120 == 0 ? TEXTURE_BLINKING : TEXTURE; // TODO: base this on the entities id, so not all blink at the same time
	}
	
}
