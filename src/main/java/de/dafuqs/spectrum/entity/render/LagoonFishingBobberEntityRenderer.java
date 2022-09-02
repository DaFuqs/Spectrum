package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.SpectrumFishingBobberEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class LagoonFishingBobberEntityRenderer extends SpectrumFishingBobberEntityRenderer {
	
	protected Identifier TEXTURE = SpectrumCommon.locate("textures/entity/fishing_hooks/lagoon_fishing_hook.png");
	protected Identifier TEXTURE_OPEN_WATERS = SpectrumCommon.locate("textures/entity/fishing_hooks/lagoon_fishing_hook_open_waters.png");
	protected RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);
	protected RenderLayer LAYER_OPEN_WATERS = RenderLayer.getEntityCutout(TEXTURE_OPEN_WATERS);
	
	public LagoonFishingBobberEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	@Override
	public Identifier getTexture(SpectrumFishingBobberEntity fishingBobberEntity) {
		if(fishingBobberEntity.isInTheOpen()) {
			return TEXTURE_OPEN_WATERS;
		} else {
			return TEXTURE;
		}
	}
	
	public RenderLayer getLayer(SpectrumFishingBobberEntity bobber) {
		if(bobber.isInTheOpen(bobber.getBlockPos())) {
			return LAYER_OPEN_WATERS;
		} else {
			return LAYER;
		}
	}
	
}
