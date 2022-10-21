package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.EggLayingWoolyPigEntity;
import de.dafuqs.spectrum.entity.models.EggLayingWoolyPigEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EggLayingWoolyPigEntityRenderer extends MobEntityRenderer<EggLayingWoolyPigEntity, EggLayingWoolyPigEntityModel<EggLayingWoolyPigEntity>> {
	
	private static final Identifier TEXTURE = new Identifier("textures/entity/sheep/sheep.png");
	
	public EggLayingWoolyPigEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new EggLayingWoolyPigEntityModel(context.getPart(EntityModelLayers.SHEEP)), 0.7F);
		this.addFeature(new EggLayingWoolyPigWoolFeatureRenderer(this, context.getModelLoader()));
	}
	
	@Override
	public Identifier getTexture(EggLayingWoolyPigEntity entity) {
		return TEXTURE;
	}
	
}