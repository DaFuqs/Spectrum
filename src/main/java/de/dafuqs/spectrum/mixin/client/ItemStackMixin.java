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
public abstract class ItemStackMixin implements CustomItemRender.Provider {
    @Shadow public abstract Item getItem();
    @Override
    public Object getRender() {
        return this.getItem().getRender() == null ? null : new CustomItemRender.DefaultProviders.ItemStackRender((ItemStack) (Object) this);
    }
}
