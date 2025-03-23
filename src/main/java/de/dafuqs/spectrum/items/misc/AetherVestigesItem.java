package de.dafuqs.spectrum.items.misc;

import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

public class AetherVestigesItem extends ItemWithTooltip implements SlotBackgroundEffectProvider {

	public AetherVestigesItem(Settings settings, String tooltip) {
		super(settings, tooltip);
	}

	@Override
	public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		return SlotEffect.BORDER_FADE;
	}

	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return 0xFFFFFF;
	}
}
