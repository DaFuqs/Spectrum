package de.dafuqs.spectrum.dimension;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class DeeperDownDimension {
	
	public static final Identifier DEEPER_DOWN_DIMENSION_ID = new Identifier(SpectrumCommon.MOD_ID, "deeper_down_dimension");
	public static final RegistryKey<World> DEEPER_DOWN_DIMENSION_KEY = RegistryKey.of(Registry.WORLD_KEY, DEEPER_DOWN_DIMENSION_ID);
	
	public static void setup() {
	
	}
	
}
