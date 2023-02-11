package de.dafuqs.spectrum.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomUseTimeItem extends Item {

    private final int useTime;

    public CustomUseTimeItem(Settings settings, int useTime) {
        super(settings);
        this.useTime = useTime;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return useTime;
    }
}
