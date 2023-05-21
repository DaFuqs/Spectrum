package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.event.*;

public class SpectrumPositionSources {
	
	public static PositionSourceType<ExactPositionSource> EXACT;
	
	static <S extends PositionSourceType<T>, T extends PositionSource> S register(String id, S positionSourceType) {
		return Registry.register(Registry.POSITION_SOURCE_TYPE, SpectrumCommon.locate(id), positionSourceType);
	}
	
	public static void register() {
		EXACT = register("exact", new ExactPositionSource.Type());
	}
	
}
