package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public abstract class EnchanterCategory<T extends EnchanterDisplay> extends GatedDisplayCategory<T> {
	
	public final static ResourceLocation BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	public static final EntryIngredient ENCHANTER = EntryIngredients.of(SpectrumBlocks.ENCHANTER);
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.ENCHANTER);
	}
	
	public abstract int getCraftingTime(@NotNull T display);
	
	public abstract Component getDescriptionText(@NotNull T display);
	
	@Override
	public int getDisplayHeight() {
		return 92;
	}
	
}
