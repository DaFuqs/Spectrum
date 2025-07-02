package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.gameevent.*;

public class SpectrumPositionSources {
	
	public static PositionSourceType<ExactPositionSource> EXACT;
	
	static <S extends PositionSourceType<T>, T extends PositionSource> S register(String id, S positionSourceType) {
		return Registry.register(BuiltInRegistries.POSITION_SOURCE_TYPE, SpectrumCommon.locate(id), positionSourceType);
	}
	
	public static void register() {
		EXACT = register("exact", new ExactPositionSource.Type());
	}
	
}
