package de.dafuqs.spectrum.compat.REI;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;

import java.util.Collections;
import java.util.List;

public class NaturesStaffConversionsDisplay extends BasicDisplay {

		public NaturesStaffConversionsDisplay(EntryStack<?> in, EntryStack<?> out) {
			this(Collections.singletonList(EntryIngredient.of(in)), Collections.singletonList(EntryIngredient.of(out)));
		}

		public NaturesStaffConversionsDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
			super(inputs, outputs);
		}

		public final EntryIngredient getIn() {
			return getInputEntries().get(0);
		}

		public final EntryIngredient getOut() {
			return getOutputEntries().get(0);
		}

		@Override
		public CategoryIdentifier<?> getCategoryIdentifier() {
			return NaturesStaffConversionsCategory.ID;
		}

		public static BasicDisplay.Serializer<NaturesStaffConversionsDisplay> serializer() {
			return BasicDisplay.Serializer.ofSimpleRecipeLess(NaturesStaffConversionsDisplay::new);
		}
	}