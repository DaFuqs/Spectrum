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

	private static final Identifier WOOLY = SpectrumCommon.locate("textures/entity/wooly_pig/wooly.png");
	private static final Identifier SHEARED = SpectrumCommon.locate("textures/entity/wooly_pig/sheared.png");
	private static final Identifier WOOLY_HATLESS = SpectrumCommon.locate("textures/entity/wooly_pig/wooly_hatless.png");
	private static final Identifier SHEARED_HATLESS = SpectrumCommon.locate("textures/entity/wooly_pig/sheared_hatless.png");

	public EggLayingWoolyPigEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new EggLayingWoolyPigEntityModel(context.getPart(SpectrumModelLayers.WOOLY_PIG)), 0.6F);
		//this.addFeature(new EggLayingWoolyPigWoolFeatureRenderer(this, context.getModelLoader()));
	}
	
	@Override
	public Identifier getTexture(EggLayingWoolyPigEntity entity) {
		if (entity.isBaby()) {
			return SHEARED;
		}
		return entity.isSheared() ? SHEARED : WOOLY;
	}
	
}