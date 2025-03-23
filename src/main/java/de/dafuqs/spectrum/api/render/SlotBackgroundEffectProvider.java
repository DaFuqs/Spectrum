package de.dafuqs.spectrum.api.render;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

public interface SlotBackgroundEffectProvider {
	
	SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack);
	
	int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta);
	
	default float getEffectOpacity(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
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
