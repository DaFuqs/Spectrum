package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import dev.emi.trinkets.api.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.ai.attributes.*;

public interface SingleSlotGlove {
	
	static void disableSecondGloveSlot(Multimap<Holder<Attribute>, AttributeModifier> modifiers) {
		SlotAttributes.addSlotModifier(modifiers, "offhand/glove", SpectrumCommon.locate("disable_offhand_glove"), -1, AttributeModifier.Operation.ADD_VALUE);
	}
	
}
