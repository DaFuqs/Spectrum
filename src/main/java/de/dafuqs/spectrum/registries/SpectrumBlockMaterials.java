package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.*;
import net.minecraft.block.*;
import net.minecraft.block.piston.*;

public class SpectrumBlockMaterials {

	public static final Material DECAY = new Material(MapColor.BLACK, false, true, true, true, false, false, PistonBehavior.DESTROY);

	private static Material fluid(MapColor mapColor) {
		return new FabricMaterialBuilder(mapColor).allowsMovement().lightPassesThrough().notSolid().destroyedByPiston().replaceable().liquid().build();
	}

	public static final Material MUD = fluid(MapColor.TERRACOTTA_BROWN);
	public static final Material LIQUID_CRYSTAL = fluid(MapColor.LIGHT_GRAY);
	public static final Material MIDNIGHT_SOLUTION = fluid(MapColor.DARK_AQUA);
	public static final Material DRAGONROT = fluid(MapColor.PALE_PURPLE);

}
