package de.dafuqs.pigment.items.misc;

import de.dafuqs.pigment.enums.PigmentColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CatkinItem extends Item {

    PigmentColor pigmentColor;
    boolean lucid;

    public CatkinItem(PigmentColor pigmentColor, boolean lucid, Settings settings) {
        super(settings);
        this.pigmentColor = pigmentColor;
        this.lucid = lucid;
    }

    public boolean hasGlint(ItemStack stack) {
        return lucid;
    }

}
