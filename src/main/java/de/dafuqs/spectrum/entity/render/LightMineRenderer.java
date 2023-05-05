package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.effect.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

public class LightMineRenderer extends EntityRenderer<LightMineEntity> {
    
    protected static int LIGHT = 15728850;
    
    public LightMineRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }
    
    @Override
    public void render(LightMineEntity shard, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        
        var age = shard.age;
        
        matrices.multiply(this.dispatcher.getRotation());
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180f));
        
        var alpha = MathHelper.clamp(1 - MathHelper.lerp(tickDelta, shard.getVanishingProgress(age - 1), shard.getVanishingProgress(age)), 0F, 1F);
        var scaleFactor = MathHelper.sin((age + tickDelta) / 8F) / 6F + shard.getScaleOffset();
        
        matrices.scale(scaleFactor, scaleFactor, 1);
        matrices.translate(-0.5F, -0.5F, 0);
        
        var consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(getTexture(shard)));
        var matrix = matrices.peek();
        var positions = matrix.getPositionMatrix();
        var normals = matrix.getNormalMatrix();
        
        @Nullable StatusEffectInstance effect = shard.getEffect();
        if (effect == null) {
            consumer.vertex(positions, 0, 0, 0).color(1f, 1f, 1f, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(LIGHT).normal(normals, 0, 1, 0).next();
            consumer.vertex(positions, 1, 0, 0).color(1f, 1f, 1f, alpha).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(LIGHT).normal(normals, 0, 1, 0).next();
            consumer.vertex(positions, 1, 1, 0).color(1f, 1f, 1f, alpha).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(LIGHT).normal(normals, 0, 1, 0).next();
            consumer.vertex(positions, 0, 1, 0).color(1f, 1f, 1f, alpha).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(LIGHT).normal(normals, 0, 1, 0).next();
        } else {
            Vec3f color = ColorHelper.colorIntToVec(effect.getEffectType().getColor());
            consumer.vertex(positions, 0, 0, 0).color(color.getX(), color.getY(), color.getZ(), alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(LIGHT).normal(normals, 0, 1, 0).next();
            consumer.vertex(positions, 1, 0, 0).color(color.getX(), color.getY(), color.getZ(), alpha).overlay(OverlayTexture.DEFAULT_UV).light(LIGHT).normal(normals, 0, 1, 0).next();
            consumer.vertex(positions, 1, 1, 0).color(color.getX(), color.getY(), color.getZ(), alpha).overlay(OverlayTexture.DEFAULT_UV).light(LIGHT).normal(normals, 0, 1, 0).next();
            consumer.vertex(positions, 0, 1, 0).color(color.getX(), color.getY(), color.getZ(), alpha).overlay(OverlayTexture.DEFAULT_UV).light(LIGHT).normal(normals, 0, 1, 0).next();
        }
        
        matrices.pop();
        
        super.render(shard, yaw, tickDelta, matrices, vertexConsumers, light);
    }
    
    @Override
    public Identifier getTexture(LightMineEntity entity) {
        return entity.getTexture();
    }
    
}
