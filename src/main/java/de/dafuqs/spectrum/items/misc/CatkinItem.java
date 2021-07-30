package de.dafuqs.spectrum.items.misc;

import de.dafuqs.spectrum.enums.SpectrumColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CatkinItem extends Item {

    SpectrumColor spectrumColor;
    boolean lucid;

    public CatkinItem(SpectrumColor spectrumColor, boolean lucid, Settings settings) {
        super(settings);
        this.spectrumColor = spectrumColor;
        this.lucid = lucid;
    }

    public boolean hasGlint(ItemStack stack) {
        return lucid;
    }

}
