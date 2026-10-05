package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;


public abstract class EnchanterCategory<T extends EnchanterDisplay> extends GatedDisplayCategory<T> {
	
	public final static ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	public static final EntryIngredient ENCHANTER = EntryIngredients.of(SpectrumBlocks.ENCHANTER);
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.ENCHANTER);
	}
	
	public abstract int getCraftingTime(T display);
	
	public abstract Component getDescriptionText(T display);
	
	@Override
	public int getDisplayHeight() {
		return 92;
	}
	
}
