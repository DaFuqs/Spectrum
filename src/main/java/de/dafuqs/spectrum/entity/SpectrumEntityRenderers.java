package de.dafuqs.spectrum.entity;

import de.dafuqs.spectrum.entity.render.GravityBlockRenderer;
import de.dafuqs.spectrum.entity.render.ShootingStarEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class SpectrumEntityRenderers {

    public static void registerClient() {
        register(SpectrumEntityTypes.GRAVITY_BLOCK, GravityBlockRenderer::new);
        register(SpectrumEntityTypes.SHOOTING_STAR, ShootingStarEntityRenderer::new);
        register(SpectrumEntityTypes.INVISIBLE_ITEM_FRAME, ItemFrameEntityRenderer::new);
        register(SpectrumEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, ItemFrameEntityRenderer::new);
    }

    private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory) {
        EntityRendererRegistry.INSTANCE.register(type, factory);
    }

}