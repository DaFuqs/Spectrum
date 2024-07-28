package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.ItemWithTooltip;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;

public class EvernectarItem extends ItemWithTooltip {

    public EvernectarItem(Settings settings, String tooltip) {
        super(settings, tooltip);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
