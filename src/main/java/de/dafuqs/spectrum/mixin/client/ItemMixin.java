package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.helpers.CustomItemRender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(Item.class)
public class ItemMixin implements CustomItemRender {
    // default impl. Relies on the UNSTABLE CustomItemRender.Stack.Extra
    @Override
    public boolean shouldRender(ItemStack stack, ModelTransformationMode mode) {
        return !stack.isCurrentlyRendering();
    }
}
