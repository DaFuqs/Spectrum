package de.dafuqs.spectrum.registries;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.piston.PistonBehavior;

public class SpectrumBlockMaterials {
	
	public static final Material DECAY = new Material(MapColor.BLACK, false, true, true, true, false, false, PistonBehavior.DESTROY);
	
}
