package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.SpectrumFishingBobberEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class MoltenFishingBobberEntityRenderer extends SpectrumFishingBobberEntityRenderer {
	
	protected Identifier TEXTURE = SpectrumCommon.locate("textures/entity/molten_fishing_hook.png");
	protected RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);
	
	public MoltenFishingBobberEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	@Override
	public Identifier getTexture(SpectrumFishingBobberEntity fishingBobberEntity) {
		return TEXTURE;
	}
	
	public RenderLayer getLayer() {
		return LAYER;
	}
	
}
