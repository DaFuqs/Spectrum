package de.dafuqs.pigment.entity;

import de.dafuqs.pigment.entity.render.GravityBlockRenderer;
import de.dafuqs.pigment.entity.render.ShootingStarEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class PigmentEntityRenderers {

    public static void registerClient() {
        register(PigmentEntityTypes.GRAVITY_BLOCK, GravityBlockRenderer::new);
        register(PigmentEntityTypes.SHOOTING_STAR, ShootingStarEntityRenderer::new);
        register(PigmentEntityTypes.INVISIBLE_ITEM_FRAME, ItemFrameEntityRenderer::new);
        register(PigmentEntityTypes.INVISIBLE_GLOW_ITEM_FRAME, ItemFrameEntityRenderer::new);
    }

    private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory) {
        EntityRendererRegistry.INSTANCE.register(type, factory);
    }

}