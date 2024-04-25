package de.dafuqs.spectrum.api.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface SlotBackgroundEffectProvider {

    SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack);

    int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta);

    default float getEffectOpacity(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
        return 1F;
    }

    enum SlotEffect {
        PULSE,
        BORDER,
        BORDER_FADE,
        FULL_PACKAGE,
        NONE
    }
}
