package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.items.conditional.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

public class CatkinItem extends GemstonePowderItem {
	
	protected final boolean lucid;
	
	public CatkinItem(@NotNull GemstoneColor gemstoneColor, boolean lucid, Settings settings) {
		super(settings, SpectrumCommon.locate("endgame/grow_ominous_sapling"), gemstoneColor);
		this.lucid = lucid;
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return lucid;
	}
	
}
