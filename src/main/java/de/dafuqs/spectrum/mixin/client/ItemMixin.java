package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.helpers.CustomItemRender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(Item.class)
public class ItemMixin implements CustomItemRender.Provider {
    @Unique
    private static final CustomItemRender.Render render = new CustomItemRender.DefaultProviders.ItemRender();
    @Override
    public Object getRender() {
        return render;
    }
}
