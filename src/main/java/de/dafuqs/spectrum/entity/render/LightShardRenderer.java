package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.LightShardEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class LightShardRenderer extends EntityRenderer<LightShardEntity> {

    public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/projectile/light_shard.png");

    public LightShardRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(LightShardEntity shard, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        light = 15728850;
        var age = shard.age;
        var rotation = MathHelper.lerp(tickDelta, shard.lastRotationOffset, shard.rotationOffset);

        matrices.multiply(this.dispatcher.getRotation());
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180f));

        var alpha = MathHelper.clamp(1 - MathHelper.lerp(tickDelta, shard.getVanishingProgress(age - 1), shard.getVanishingProgress(age)), 0F, 1F);

        var scaleFactor = MathHelper.sin((age + tickDelta) / 8F) / 6F + shard.getScaleOffset();

        matrices.scale(scaleFactor, scaleFactor, 1);
        matrices.translate(-0.5F, -0.5F, 0);

        var consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(TEXTURE));
        var matrix = matrices.peek();
        var positions = matrix.getPositionMatrix();
        var normals = matrix.getNormalMatrix();

        consumer.vertex(positions, 0, 0, 0).color(1f, 1f, 1f, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normals, 0, 1, 0).next();
        consumer.vertex(positions, 1, 0, 0).color(1f, 1f, 1f, alpha).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normals, 0, 1, 0).next();
        consumer.vertex(positions, 1, 1, 0).color(1f, 1f, 1f, alpha).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normals, 0, 1, 0).next();
        consumer.vertex(positions, 0, 1, 0).color(1f, 1f, 1f, alpha).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normals, 0, 1, 0).next();

        matrices.pop();


        super.render(shard, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(LightShardEntity entity) {
        return TEXTURE;
    }
}
