package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.util.*;

public class MoltenFishingBobberEntityRenderer extends SpectrumFishingBobberEntityRenderer {
	
	protected final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/fishing_hooks/molten_fishing_hook.png");
	protected final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);
	
	public MoltenFishingBobberEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	@Override
	public Identifier getTexture(SpectrumFishingBobberEntity fishingBobberEntity) {
		return TEXTURE;
	}
	
	@Override
	public RenderLayer getLayer(SpectrumFishingBobberEntity bobber) {
		return LAYER;
	}
	
}
