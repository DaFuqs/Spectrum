package de.dafuqs.pigment.items.tools;

import net.minecraft.item.FishingRodItem;

public class BedrockFishingRodItem extends FishingRodItem {

    public BedrockFishingRodItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

}