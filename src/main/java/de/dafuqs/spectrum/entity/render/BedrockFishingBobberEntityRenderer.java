package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;

public class BedrockFishingBobberEntityRenderer extends SpectrumFishingBobberEntityRenderer {
	
	protected static final ResourceLocation TEXTURE = SpectrumCommon.locate("textures/entity/fishing_hooks/bedrock_fishing_hook.png");
	protected static final RenderType LAYER = RenderType.entityCutout(TEXTURE);
	
	public BedrockFishingBobberEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	public ResourceLocation getTextureLocation(SpectrumFishingBobberEntity fishingBobberEntity) {
		return TEXTURE;
	}
	
	@Override
	public RenderType getLayer(SpectrumFishingBobberEntity bobber) {
		return LAYER;
	}
	
}
