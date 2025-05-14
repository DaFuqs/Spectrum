package de.dafuqs.spectrum.api.color;

import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public class FluidColors extends ColorRegistry<Fluid> {
	
	private static final HashMap<Fluid, InkColor> COLORS = new HashMap<>() {{
		put(Fluids.WATER, InkColors.BLUE);
		put(Fluids.LAVA, InkColors.ORANGE);
	}};

	@Override
	public void registerColorMapping(Fluid fluid, InkColor color) {
		COLORS.put(fluid, color);
	}
	
	@Override
	public Optional<InkColor> getMapping(Fluid fluid) {
		if (COLORS.containsKey(fluid)) {
			return Optional.of(COLORS.get(fluid));
		} else {
			return Optional.empty();
		}
	}
	
	public InkColor getMapping(Fluid fluid, InkColor fallback) {
		return COLORS.getOrDefault(fluid, fallback);
	}
	
}
