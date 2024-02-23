package de.dafuqs.spectrum.api.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

// Similar to FAPIs DynamicItemRenderer, except with a little more information.
@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface DynamicItemRenderer {
    
    Object2ObjectArrayMap<Item, DynamicItemRenderer> RENDERERS = new Object2ObjectArrayMap<>();
    
    /**
     * Renders an item stack.
     *
     * @param renderer        the currently used ItemRenderer instance
     * @param stack           the rendered item stack
     * @param mode            the model transformation mode
     * @param leftHanded      the handedness boolean in the original render methods arguments
     * @param matrices        the matrix stack
     * @param vertexConsumers the vertex consumer provider
     * @param light           packed lightmap coordinates
     * @param overlay         the overlay UV passed to {@link net.minecraft.client.render.VertexConsumer#overlay(int)}
     * @param model           the original model [use this to render the underlying item model]
     */
    void render(ItemRenderer renderer, ItemStack stack, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model);
}
