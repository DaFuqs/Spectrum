package de.dafuqs.spectrum.items.misc;

import de.dafuqs.spectrum.enums.GemstoneColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CatkinItem extends Item {

    GemstoneColor gemstoneColor;
    boolean lucid;

    public CatkinItem(GemstoneColor gemstoneColor, boolean lucid, Settings settings) {
        super(settings);
        this.gemstoneColor = gemstoneColor;
        this.lucid = lucid;
    }

    public boolean hasGlint(ItemStack stack) {
        return lucid;
    }

}
