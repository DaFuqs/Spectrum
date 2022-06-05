package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.entity.render.InvisibleItemFrameEntityRenderer;
import de.dafuqs.spectrum.entity.render.ShootingStarEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.id.incubus_core.blocklikeentities.api.client.BlockLikeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class SpectrumEntityRenderers {
	
	public static void registerClient() {
		register(SpectrumEntityTypes.FLOAT_BLOCK, BlockLikeEntityRenderer::new);
		register(SpectrumEntityTypes.SHOOTING_STAR, ShootingStarEntityRenderer::new);
		register(SpectrumEntityTypes.INVISIBLE_ITEM_FRAME, InvisibleItemFrameEntityRenderer::new);
		register(SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, InvisibleItemFrameEntityRenderer::new);
		register(SpectrumEntityTypes.BLOCK_FLOODER_PROJECTILE, FlyingItemEntityRenderer::new);
	}
	
	private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory) {
		EntityRendererRegistry.INSTANCE.register(type, factory);
	}
	
}