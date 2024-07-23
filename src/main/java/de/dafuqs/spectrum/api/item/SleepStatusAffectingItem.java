package de.dafuqs.spectrum.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface SleepStatusAffectingItem {

    /**
     * @return a value between 0 - 1 indicating a reduction in sleep effect vulnerability.
     */
    float getSleepResistance(PlayerEntity player, ItemStack stack);
}
