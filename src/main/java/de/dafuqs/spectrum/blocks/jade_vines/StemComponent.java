package de.dafuqs.spectrum.blocks.jade_vines;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public enum StemComponent implements StringIdentifiable {
    BASE("base"),
    STEM("stem"),
    STEMALT("stemalt");


    public static final EnumProperty<StemComponent> PROPERTY = EnumProperty.of("part", StemComponent.class);

    public final String identifier;

    StemComponent(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String asString() {
        return identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
