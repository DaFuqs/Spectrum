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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements CustomItemRender.Stack, CustomItemRender.Stack.Extra {
    @Shadow public abstract Item getItem();

    @Override
    public boolean shouldRenderCustom(ModelTransformationMode mode) {
        ItemStack s = ((ItemStack)(Object)this);
        return s.getItem().shouldRenderCustom(s, mode);
    }

    @Override
    public void render(ItemRenderer instance, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
        ItemStack s = ((ItemStack)(Object)this);
        s.getItem().render(instance, s, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);
    }

    // UNSTABLE CustomItemRender.Stack.Extra implementation.
    @Unique
    boolean spectrum$currentlyRendering = false;
    @Override
    public boolean isCurrentlyRendering() {
        return spectrum$currentlyRendering;
    }
    @Override
    public void setCurrentlyRendering(boolean value) {
        spectrum$currentlyRendering = value;
    }
}
