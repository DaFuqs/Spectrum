package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.GatedRecipeDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class NaturesStaffConversionsDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	public static final Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_natures_staff");
	
	public NaturesStaffConversionsDisplay(EntryStack<?> in, EntryStack<?> out) {
		this(Collections.singletonList(EntryIngredient.of(in)), Collections.singletonList(EntryIngredient.of(out)));
	}
	
	public NaturesStaffConversionsDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(inputs, outputs);
	}
	
	public static BasicDisplay.Serializer<NaturesStaffConversionsDisplay> serializer() {
		return BasicDisplay.Serializer.ofSimpleRecipeLess(NaturesStaffConversionsDisplay::new);
	}
	
	public final EntryIngredient getIn() {
		return getInputEntries().get(0);
	}
	
	public final EntryIngredient getOut() {
		return getOutputEntries().get(0);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.NATURES_STAFF;
	}
	
	@Environment(EnvType.CLIENT)
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, UNLOCK_ADVANCEMENT_IDENTIFIER);
	}
}