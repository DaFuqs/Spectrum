package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import net.minecraft.client.*;
import net.minecraft.util.*;

import java.util.*;

public class HeatingDisplay extends BlockToBlockWithChanceDisplay {
	
	public static final Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/mob_blocks");
	
	public HeatingDisplay(EntryStack<?> in, EntryStack<?> out, float chance) {
		super(Collections.singletonList(EntryIngredient.of(in)), Collections.singletonList(EntryIngredient.of(out)), chance);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.HEATING;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, UNLOCK_ADVANCEMENT_IDENTIFIER);
	}
	
}