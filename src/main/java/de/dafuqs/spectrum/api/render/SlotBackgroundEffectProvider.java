package de.dafuqs.spectrum.api.render;

import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public interface SlotBackgroundEffectProvider {
	
	SlotEffect backgroundType(@Nullable Player player, ItemStack stack);
	
	int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta);
	
	default float getEffectOpacity(@Nullable Player player, ItemStack stack, float tickDelta) {
		return 1F;
	}
	
	enum SlotEffect {
		PULSE,
		BORDER,
		BORDER_FADE,
		FULL_PACKAGE,
		NONE
	}
}
