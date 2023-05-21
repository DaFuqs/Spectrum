package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.util.*;

public class LagoonFishingBobberEntityRenderer extends SpectrumFishingBobberEntityRenderer {
	
	protected final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/fishing_hooks/lagoon_fishing_hook.png");
	protected final Identifier TEXTURE_OPEN_WATERS = SpectrumCommon.locate("textures/entity/fishing_hooks/lagoon_fishing_hook_open_waters.png");
	protected final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);
	protected final RenderLayer LAYER_OPEN_WATERS = RenderLayer.getEntityCutout(TEXTURE_OPEN_WATERS);
	
	public LagoonFishingBobberEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	@Override
	public Identifier getTexture(SpectrumFishingBobberEntity fishingBobberEntity) {
		if (fishingBobberEntity.isInTheOpen()) {
			return TEXTURE_OPEN_WATERS;
		} else {
			return TEXTURE;
		}
	}
	
	@Override
	public RenderLayer getLayer(SpectrumFishingBobberEntity bobber) {
		if (bobber.isInTheOpen(bobber.getBlockPos())) {
			return LAYER_OPEN_WATERS;
		} else {
			return LAYER;
		}
	}
	
}
