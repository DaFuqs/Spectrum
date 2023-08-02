package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.type.*;
import net.minecraft.block.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumWoodTypes {
	public static final WoodType SLATE_NOXWOOD = new WoodTypeBuilder().register(locate("slate_noxwood"), SpectrumBlockSetTypes.NOXWOOD);
	public static final WoodType EBONY_NOXWOOD = new WoodTypeBuilder().register(locate("ebony_noxwood"), SpectrumBlockSetTypes.NOXWOOD);
	public static final WoodType IVORY_NOXWOOD = new WoodTypeBuilder().register(locate("ivory_noxwood"), SpectrumBlockSetTypes.NOXWOOD);
	public static final WoodType CHESTNUT_NOXWOOD = new WoodTypeBuilder().register(locate("chestnut_noxwood"), SpectrumBlockSetTypes.NOXWOOD);
	public static final WoodType COLORED_WOOD = new WoodTypeBuilder().register(locate("colored_wood"), SpectrumBlockSetTypes.COLORED_WOOD);
}
