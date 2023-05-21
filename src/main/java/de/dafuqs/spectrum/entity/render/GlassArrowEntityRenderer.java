package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class GlassArrowEntityRenderer extends EntityRenderer<GlassArrowEntity> {

    private final ItemRenderer itemRenderer;

    public GlassArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }
    
    @Override
    public void render(GlassArrowEntity persistentProjectileEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        ItemStack itemStack = persistentProjectileEntity.getVariant().getArrow().getDefaultStack();
        renderAsItemStack(persistentProjectileEntity, tickDelta, matrixStack, vertexConsumerProvider, light, itemStack);
        super.render(persistentProjectileEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }

    private void renderAsItemStack(PersistentProjectileEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, ItemStack itemStack) {
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.world, null, entity.getId());
        boolean hasDepth = bakedModel.hasDepth();

        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(getAdditionalPitch() + MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));

        float yScale = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.getY();
        matrixStack.translate(0.0, (0.25F * yScale), 0.0);

        float scale = getScale();
        matrixStack.scale(scale, scale, scale);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(0)); // TODO: needed?

        float scaleX = bakedModel.getTransformation().ground.scale.getX();
        float scaleY = bakedModel.getTransformation().ground.scale.getY();
        float scaleZ = bakedModel.getTransformation().ground.scale.getZ();
        if (!hasDepth) {
            float r = -0.0F * (float) (0) * 0.5F * scaleX;
            float s = -0.0F * (float) (0) * 0.5F * scaleY;
            float t = -0.09375F * (float) (0) * 0.5F * scaleZ;
            matrixStack.translate(r, s, t);
        }
        float shake = (float) entity.shake - tickDelta;
        if (shake > 0.0F) {
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-MathHelper.sin(shake * 3.0F) * shake));
        }

        this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, false, matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV, bakedModel);
    
        matrixStack.pop();
    }
    
    public float getScale() {
        return 1.5F;
    }
    
    public int getAdditionalPitch() {
        return -45;
    }
    
    @Override
    public Identifier getTexture(GlassArrowEntity itemEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
    
}
