package de.dafuqs.pigment.items.tools;

import net.minecraft.item.CrossbowItem;

public class BedrockCrossbowItem extends CrossbowItem {

    public BedrockCrossbowItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

}