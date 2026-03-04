package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.gameevent.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

public class SpectrumPositionSources {
	
	private static final DeferredRegister<PositionSourceType<?>> REGISTRAR = DeferredRegister.create(Registries.POSITION_SOURCE_TYPE, SpectrumCommon.MOD_ID);
	
	public static DeferredHolder<PositionSourceType<?>, ExactPositionSource.Type> EXACT = register("exact", ExactPositionSource.Type::new);
	
	private static <S extends PositionSourceType<T>, T extends PositionSource> DeferredHolder<PositionSourceType<?>, S> register(String name, Supplier<S> positionSourceType) {
		return REGISTRAR.register(name, positionSourceType);
	}
	
	public static void register(IEventBus bus) {
		REGISTRAR.register(bus);
	}
	
}
