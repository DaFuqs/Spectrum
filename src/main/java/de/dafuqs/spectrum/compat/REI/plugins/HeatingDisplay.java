package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.Collections;

public class HeatingDisplay extends BlockToBlockWithChanceDisplay {
	
	public static final Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_mob_blocks");
	
	public HeatingDisplay(EntryStack<?> in, EntryStack<?> out, float chance) {
		super(Collections.singletonList(EntryIngredient.of(in)), Collections.singletonList(EntryIngredient.of(out)), chance);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.HEATING;
	}
	
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, UNLOCK_ADVANCEMENT_IDENTIFIER);
	}
	
}