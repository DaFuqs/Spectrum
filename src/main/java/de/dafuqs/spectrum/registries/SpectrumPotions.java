package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.alchemy.*;

public class SpectrumPotions {
	
	public static Holder<Potion> PIGMENT_POTION;
	
	private static Holder<Potion> register(String name, Potion potion) {
		return Registry.registerForHolder(BuiltInRegistries.POTION, SpectrumCommon.locate(name), potion);
	}
	
	public static void register() {
		PIGMENT_POTION = register("pigment_potion", new Potion());
	}
	
}
