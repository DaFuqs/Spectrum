package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.helpers.CustomItemRender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(Item.class)
public class ItemMixin implements CustomItemRender {
    // default impl. Relies on the UNSTABLE CustomItemRender.Stack.Extra
    @Override
    public boolean shouldRender(ItemStack stack, ModelTransformationMode mode) {
        return CustomItemRender.super.shouldRender(stack, mode) && !stack.isCurrentlyRendering();
    }

    // Fallback. Only called if the item supporting custom rendering doesn't implement its own render method.
    @Override
    public void render(ItemRenderer instance, ItemStack stack, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
        stack.setCurrentlyRendering(true);
        stack.render(instance, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);
    }
}
