package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.deeper_down.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.placementmodifier.*;

public class SpectrumPlacementModifiers {
	public static PlacementModifierType<DragonFossilPlacementModifier> DRAGON_FOSSIL;
	
	public static void register() {
		DRAGON_FOSSIL = register("dragon_fossil", DragonFossilPlacementModifier.MODIFIER_CODEC);
	}
	
	private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
		return Registry.register(Registry.PLACEMENT_MODIFIER_TYPE, SpectrumCommon.locate(id), () -> codec);
	}
}
