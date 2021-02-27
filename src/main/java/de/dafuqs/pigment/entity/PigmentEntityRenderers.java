package de.dafuqs.pigment.entity;

import de.dafuqs.pigment.entity.entity.ShootingStarEntity;
import de.dafuqs.pigment.entity.render.GravityBlockRenderer;
import de.dafuqs.pigment.entity.render.ShootingStarEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class PigmentEntityRenderers {

    public static void registerClient() {
        register(PigmentEntityTypes.GRAVITY_BLOCK, GravityBlockRenderer::new);
        register(PigmentEntityTypes.SHOOTING_STAR, ShootingStarEntityRenderer::new);
    }

    private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererFactory<T> factory) {
        EntityRendererRegistry.INSTANCE.register(type, factory);
    }

}