package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.entry.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NaturesStaffConversionsDisplay extends BasicDisplay implements GatedRecipeDisplay {
	
	public static final Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("unlocks/items/natures_staff");
	private final @Nullable Identifier requiredAdvancementIdentifier;
	
	public NaturesStaffConversionsDisplay(EntryStack<?> in, EntryStack<?> out, @Nullable Identifier requiredAdvancementIdentifier) {
		super(Collections.singletonList(EntryIngredient.of(in)), Collections.singletonList(EntryIngredient.of(out)));
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.NATURES_STAFF;
	}
	
	@Override
	public boolean isUnlocked() {
		return AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, this.requiredAdvancementIdentifier)
				&& AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, UNLOCK_ADVANCEMENT_IDENTIFIER);
	}
	
	@Override
	public boolean isSecret() {
		return false;
	}
	
}