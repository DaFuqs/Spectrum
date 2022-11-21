package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.entity.render.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.id.incubus_core.blocklikeentities.api.client.BlockLikeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class SpectrumEntityRenderers {
	
	public static void registerClient() {
		register(SpectrumEntityTypes.FLOAT_BLOCK, BlockLikeEntityRenderer::new);
		register(SpectrumEntityTypes.SHOOTING_STAR, ShootingStarEntityRenderer::new);
		register(SpectrumEntityTypes.PHANTOM_FRAME, PhantomFrameEntityRenderer::new);
		register(SpectrumEntityTypes.PHANTOM_GLOW_FRAME, PhantomFrameEntityRenderer::new);
		register(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE, FlyingItemEntityRenderer::new);
		register(SpectrumEntityTypes.INK_PROJECTILE, InkProjectileEntityRenderer::new);
		register(SpectrumEntityTypes.LAGOON_FISHING_BOBBER, LagoonFishingBobberEntityRenderer::new);
		register(SpectrumEntityTypes.MOLTEN_FISHING_BOBBER, MoltenFishingBobberEntityRenderer::new);
		register(SpectrumEntityTypes.BEDROCK_FISHING_BOBBER, BedrockFishingBobberEntityRenderer::new);
		register(SpectrumEntityTypes.FIREPROOF_ITEM, ItemEntityRenderer::new);
		register(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG, EggLayingWoolyPigEntityRenderer::new);
	}
	
	private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory) {
		EntityRendererRegistry.register(type, factory);
	}
	
}