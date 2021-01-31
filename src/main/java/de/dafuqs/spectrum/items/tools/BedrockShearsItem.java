package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.ShearsItem;

public class BedrockShearsItem extends ShearsItem {

    public BedrockShearsItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

}