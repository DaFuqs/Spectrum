package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.animal.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

public class SpectrumCatVariants {
	
	private static final DeferredRegister<CatVariant> REGISTRAR = DeferredRegister.create(Registries.CAT_VARIANT, SpectrumCommon.MOD_ID);
	
	public static final Holder<CatVariant> FELIS = register("felis", () -> new CatVariant(SpectrumCommon.locate("textures/entity/cat/felis.png")));
	public static final Holder<CatVariant> CATUS = register("catus", () -> new CatVariant(SpectrumCommon.locate("textures/entity/cat/catus.png")));
	
	private static Holder<CatVariant> register(String id, Supplier<CatVariant> entry) {
		return REGISTRAR.register(id, entry);
	}
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}
