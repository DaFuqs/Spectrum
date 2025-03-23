package de.dafuqs.spectrum.api.item;

import net.minecraft.entity.player.PlayerEntity;

public interface SleepAlteringItem {

    void applyPenalties(PlayerEntity player);
}
