package de.dafuqs.spectrum.compat.emi.widgets;

import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.client.*;

import java.util.*;
import java.util.function.*;

// Generated Slots are locked to 1 second timings so... I guess we're doing this
public class DynamicStackWidget extends GeneratedSlotWidget {
	
	private final Function<MinecraftClient, EmiIngredient> stackSupplier;
	
	public DynamicStackWidget(Function<MinecraftClient, EmiIngredient> stackSupplier, int unique, int x, int y) {
		super(null, unique, x, y);
		this.stackSupplier = stackSupplier;
	}
	
	@Override
	public EmiIngredient getStack() {
		return stackSupplier.apply(MinecraftClient.getInstance());
	}
}
