package de.dafuqs.spectrum.events;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;

public class SpectrumPositionSources {
	
	public static PositionSourceType<ExactPositionSource> EXACT;
	
	static <S extends PositionSourceType<T>, T extends PositionSource> S register(String id, S positionSourceType) {
		return Registry.register(Registry.POSITION_SOURCE_TYPE, SpectrumCommon.locate(id), positionSourceType);
	}
	
	public static void register() {
		EXACT = register("exact", new ExactPositionSource.Type());
	}
	
}
