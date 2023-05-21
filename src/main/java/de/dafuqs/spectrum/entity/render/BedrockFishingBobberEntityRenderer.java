package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.util.*;

public class BedrockFishingBobberEntityRenderer extends SpectrumFishingBobberEntityRenderer {
	
	protected static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/fishing_hooks/bedrock_fishing_hook.png");
	protected static final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);
	
	public BedrockFishingBobberEntityRenderer(EntityRendererFactory.Context context) {
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
