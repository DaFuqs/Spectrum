package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;

public class SpectrumPotions {
	
	public static Potion PIGMENT_POTION;
	
	private static Potion register(String name, Potion potion) {
		return Registry.register(Registry.POTION, SpectrumCommon.locate(name), potion);
	}
	
	public static void register() {
		PIGMENT_POTION = register("pigment_potion", new Potion());
	}
	
}
