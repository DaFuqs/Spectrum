package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.helpers.CustomItemRender;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements CustomItemRender.Provider {
}
