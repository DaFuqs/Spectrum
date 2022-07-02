package de.dafuqs.spectrum.graces.crystal;

import com.google.common.collect.ImmutableSet;

public enum ColorPool {
    BLACK(),
    WHITE(),
    GREY(BLACK, WHITE),
    LIGHT_GREY(GREY, WHITE),
    CYAN(),
    MAGENTA(),
    YELLOW(),
    RED(MAGENTA, YELLOW),
    BLUE(MAGENTA, CYAN),
    GREEN(CYAN, YELLOW),
    PINK(RED, WHITE),
    LIGHT_BLUE(BLUE, WHITE),
    LIME(GREEN, YELLOW),
    ORANGE(RED, YELLOW),
    PURPLE(BLUE, MAGENTA),
    BROWN(ORANGE, BLACK),
    CHROMATIC(BLACK, WHITE, GREY, LIGHT_GREY, CYAN, MAGENTA, YELLOW, RED, BLUE, GREEN, PINK, LIGHT_BLUE, LIME, ORANGE, PURPLE, BROWN),
    MURKY();


    ColorPool(ColorPool... components) {
        var componentBuilder = ImmutableSet.<ColorPool>builder();
        componentBuilder.add(components);
        componentSet = componentBuilder.build();
    }

    public final ImmutableSet<ColorPool> componentSet;


    public boolean rejects(ColorPool other) {
        return switch (this) {
            case WHITE -> other == BLACK;
            case BLACK -> other == WHITE;
            case CYAN -> other == ORANGE;
            case MAGENTA -> other == GREEN;
            case YELLOW -> other == BLUE;
            case BLUE -> other == YELLOW || other == ORANGE;
            case GREEN -> other == MAGENTA || other == PURPLE;
            case ORANGE -> other == CYAN || other == BLUE;
            default -> false;
        };
    }

    public boolean synergizes(ColorPool target) {
        return target == this;
    }
}
