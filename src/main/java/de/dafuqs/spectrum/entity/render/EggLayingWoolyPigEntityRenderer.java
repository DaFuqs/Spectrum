package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.EggLayingWoolyPigEntity;
import de.dafuqs.spectrum.entity.models.EggLayingWoolyPigEntityModel;
import de.dafuqs.spectrum.registries.SpectrumModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EggLayingWoolyPigEntityRenderer extends MobEntityRenderer<EggLayingWoolyPigEntity, EggLayingWoolyPigEntityModel> {

	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/egg_laying_wooly_pig.png");

	public EggLayingWoolyPigEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new EggLayingWoolyPigEntityModel(context.getPart(SpectrumModelLayers.WOOLY_PIG)), 0.6F);
		this.addFeature(new EggLayingWoolyPigWoolFeatureRenderer(this, context.getModelLoader()));
	}
	
	@Override
	public Identifier getTexture(EggLayingWoolyPigEntity entity) {
		return TEXTURE;
	}
	
}