package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.BowItem;

public class BedrockBowItem extends BowItem {

    public BedrockBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

}