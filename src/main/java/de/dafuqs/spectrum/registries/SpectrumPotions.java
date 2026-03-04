package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.alchemy.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumPotions {
	
	private static final DeferredRegister<Potion> REGISTRAR = DeferredRegister.create(Registries.POTION, SpectrumCommon.MOD_ID);
	
	public static Holder<Potion> PIGMENT_POTION = REGISTRAR.register("pigment_potion", () -> new Potion());
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}
