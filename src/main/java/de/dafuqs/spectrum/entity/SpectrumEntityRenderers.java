package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.entity.render.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.id.incubus_core.blocklikeentities.api.client.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.entity.*;

@Environment(EnvType.CLIENT)
public class SpectrumEntityRenderers {
	
	public static void registerClient() {
		register(SpectrumEntityTypes.FLOAT_BLOCK, BlockLikeEntityRenderer::new);
		register(SpectrumEntityTypes.SHOOTING_STAR, ShootingStarEntityRenderer::new);
		register(SpectrumEntityTypes.PHANTOM_FRAME, PhantomFrameEntityRenderer::new);
		register(SpectrumEntityTypes.GLOW_PHANTOM_FRAME, PhantomFrameEntityRenderer::new);
		register(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE, FlyingItemEntityRenderer::new);
		register(SpectrumEntityTypes.INK_PROJECTILE, MagicProjectileEntityRenderer::new);
		register(SpectrumEntityTypes.LAGOON_FISHING_BOBBER, LagoonFishingBobberEntityRenderer::new);
		register(SpectrumEntityTypes.MOLTEN_FISHING_BOBBER, MoltenFishingBobberEntityRenderer::new);
		register(SpectrumEntityTypes.BEDROCK_FISHING_BOBBER, BedrockFishingBobberEntityRenderer::new);
		register(SpectrumEntityTypes.FIREPROOF_ITEM, ItemEntityRenderer::new);
		register(SpectrumEntityTypes.EGG_LAYING_WOOLY_PIG, EggLayingWoolyPigEntityRenderer::new);
		register(SpectrumEntityTypes.GLASS_ARROW, GlassArrowEntityRenderer::new);
		register(SpectrumEntityTypes.MINING_PROJECTILE, MagicProjectileEntityRenderer::new);
		register(SpectrumEntityTypes.BIDENT, BidentEntityRenderer::new);
		register(SpectrumEntityTypes.BIDENT_MIRROR_IMAGE, BidentEntityRenderer::new);
	}
	
	private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory) {
		EntityRendererRegistry.register(type, factory);
	}
	
}