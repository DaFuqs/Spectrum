package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class LizardEntityRenderer extends MobEntityRenderer<LizardEntity, LizardEntityModel<LizardEntity>> {
	
	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/lizard/lizard.png");
	public static final Identifier TEXTURE_BLINKING = SpectrumCommon.locate("textures/entity/lizard/lizard_blinking.png");
	
	public LizardEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new LizardEntityModel<>(context.getPart(SpectrumModelLayers.LIZARD_SCALES)), 0.8F);
		this.addFeature(new LizardEyesFeatureRenderer<>(this));
		this.addFeature(new LizardHornsFeatureRenderer<>(this));
		this.addFeature(new LizardFrillsFeatureRenderer<>(this));
	}
	
	@Override
	public Identifier getTexture(LizardEntity entity) {
		return (entity.getId() - entity.world.getTime()) % 120 == 0 ? TEXTURE_BLINKING : TEXTURE; // based on the entities' id, so not all blink at the same time
	}
	
}
