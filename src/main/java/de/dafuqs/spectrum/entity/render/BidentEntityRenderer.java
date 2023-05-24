package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class BidentEntityRenderer extends EntityRenderer<BidentEntity> {

    private final ItemRenderer itemRenderer;

    public BidentEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(BidentEntity bidentEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        ItemStack itemStack = bidentEntity.getStack();
        renderAsItemStack(bidentEntity, tickDelta, matrixStack, vertexConsumerProvider, light, itemStack);
        super.render(bidentEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }

    private void renderAsItemStack(BidentEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, ItemStack itemStack) {
        SpectrumModelPredicateProviders.currentItemRenderMode = ModelTransformationMode.NONE;
        BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.world, null, entity.getId());

        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-135 + MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90.0F));

        float scale = 2.0F;
        matrixStack.scale(scale, scale, scale);

        this.itemRenderer.renderItem(itemStack, ModelTransformationMode.NONE, false, matrixStack, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV, bakedModel);

        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(BidentEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

}
