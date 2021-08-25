package de.dafuqs.spectrum.enums;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum PedestalRecipeTier {
    BASIC,
    SIMPLE,
    ADVANCED,
    COMPLEX;

    @Contract(pure = true)
    public static GemstoneColor[] getAvailableGemstoneDustColors(@NotNull PedestalRecipeTier pedestalRecipeTier) {
        switch (pedestalRecipeTier) {
            case COMPLEX -> {
                return GemstoneColor.values();
            }
            case ADVANCED -> {
                return new GemstoneColor[]{GemstoneColor.CYAN, GemstoneColor.MAGENTA, GemstoneColor.YELLOW, GemstoneColor.BLACK};
            }
            default -> {
                return new GemstoneColor[]{GemstoneColor.CYAN, GemstoneColor.MAGENTA, GemstoneColor.YELLOW};
            }
        }
    }

}
