package de.dafuqs.spectrum.api.item;

import net.minecraft.*;
import net.minecraft.network.chat.*;

import java.util.*;

public interface CreativeOnlyItem {
	
	Component DESCRIPTION = Component.translatable("item.spectrum.creative_only").withStyle(ChatFormatting.DARK_PURPLE);
	
	static void appendTooltip(List<Component> tooltip) {
		tooltip.add(DESCRIPTION);
	}
	
}
