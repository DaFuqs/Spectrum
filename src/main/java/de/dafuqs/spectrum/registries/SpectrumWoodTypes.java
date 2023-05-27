package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeRegistry;
import net.minecraft.block.WoodType;

import static de.dafuqs.spectrum.SpectrumCommon.locate;

public class SpectrumWoodTypes {
    public static final WoodType SLATE_NOXWOOD = WoodTypeRegistry.register(locate("slate_noxwood"), SpectrumBlockSetTypes.NOXWOOD);
    public static final WoodType EBONY_NOXWOOD = WoodTypeRegistry.register(locate("ebony_noxwood"), SpectrumBlockSetTypes.NOXWOOD);
    public static final WoodType IVORY_NOXWOOD = WoodTypeRegistry.register(locate("ivory_noxwood"), SpectrumBlockSetTypes.NOXWOOD);
    public static final WoodType CHESTNUT_NOXWOOD = WoodTypeRegistry.register(locate("chestnut_noxwood"), SpectrumBlockSetTypes.NOXWOOD);
    public static final WoodType COLORED_WOOD = WoodTypeRegistry.register(locate("colored_wood"), SpectrumBlockSetTypes.COLORED_WOOD);
}
