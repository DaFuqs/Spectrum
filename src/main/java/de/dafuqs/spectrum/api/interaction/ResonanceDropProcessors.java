package de.dafuqs.spectrum.api.interaction;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ResonanceDropProcessors {

	protected static Map<Identifier, ResonanceDropProcessor.Serializer> PROCESSORS = new Object2ObjectOpenHashMap<>();

	public static void register(Identifier id, ResonanceDropProcessor.Serializer target) {
		PROCESSORS.put(id, target);
	}

	public static @Nullable ResonanceDropProcessor.Serializer get(Identifier id) {
		return PROCESSORS.getOrDefault(id, null);
	}
	
}
