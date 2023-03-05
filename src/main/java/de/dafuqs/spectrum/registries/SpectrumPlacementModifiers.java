package de.dafuqs.spectrum.registries;

import com.mojang.serialization.Codec;

import de.dafuqs.spectrum.deeper_down.DragonFossilPlacementModifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class SpectrumPlacementModifiers {
	public static PlacementModifierType<DragonFossilPlacementModifier> DRAGON_FOSSIL;
	
	public static void register() {
		DRAGON_FOSSIL = register("dragon_fossil", DragonFossilPlacementModifier.MODIFIER_CODEC);
	}
	
	private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
		return Registry.register(Registry.PLACEMENT_MODIFIER_TYPE, id, () -> codec);
	}
}
