package de.dafuqs.spectrum.items;

import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public interface CreativeOnlyItem {
	
	Text DESCRIPTION = Text.translatable("item.spectrum.creative_only").formatted(Formatting.DARK_PURPLE);
	
	static void appendTooltip(List<Text> tooltip) {
		tooltip.add(DESCRIPTION);
	}
	
}
