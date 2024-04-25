package de.dafuqs.spectrum.api.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ExtendedItemBarProvider {

    BarSignature PASS = new BarSignature(0, 0, 0, 0, 0, 0);

    int DEFAULT_BACKGROUND_COLOR = 0xFF000000;

    int barCount(ItemStack stack);

    default boolean allowVanillaDurabilityBarRendering(@Nullable PlayerEntity player, ItemStack stack) {
        return true;
    }

    BarSignature getSignature(@Nullable PlayerEntity player, @NotNull ItemStack stack, int index);

    record BarSignature(int xPos, int yPos, int length, int fill, int fillHeight, int fillColor, boolean drawBackground, int backgroundHeight, int backgroundColor) {
        public BarSignature(int xPos, int yPos, int length, int fill, int fillHeight, int fillColor) {
            this(xPos, yPos, length, fill, fillHeight, fillColor, false, 0, 0);
        }

        public BarSignature(int xPos, int yPos, int length, int fill, int fillHeight, int fillColor, int backgroundHeight, int backgroundColor) {
            this(xPos, yPos, length, fill, fillHeight, fillColor, true, backgroundHeight, backgroundColor);
        }
    }
}
