package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public abstract class EnchanterCategory<T extends EnchanterDisplay> extends GatedDisplayCategory<T> {
	
	public final static Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	public static final EntryIngredient ENCHANTER = EntryIngredients.of(SpectrumBlocks.ENCHANTER);
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.ENCHANTER);
	}
	
	public abstract int getCraftingTime(@NotNull T display);
	
	public abstract Text getDescriptionText(@NotNull T display);
	
	@Override
	public int getDisplayHeight() {
		return 92;
	}
	
}
