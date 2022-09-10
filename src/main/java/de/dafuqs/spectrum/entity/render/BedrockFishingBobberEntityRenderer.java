package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.SpectrumFishingBobberEntity;
import de.dafuqs.spectrum.items.tools.SpectrumFishingRodItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;

public class BedrockFishingBobberEntityRenderer extends SpectrumFishingBobberEntityRenderer {
	
	protected Identifier TEXTURE = SpectrumCommon.locate("textures/entity/bedrock_fishing_hook.png");
	protected RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);
	
	public BedrockFishingBobberEntityRenderer(EntityRendererFactory.Context context) {
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
