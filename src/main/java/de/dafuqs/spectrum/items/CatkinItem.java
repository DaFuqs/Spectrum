package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.items.conditional.CloakedGemstoneColorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class CatkinItem extends CloakedGemstoneColorItem {
	
	protected boolean lucid;
	
	public CatkinItem(@NotNull GemstoneColor gemstoneColor, boolean lucid, Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "lategame/grow_ominous_sapling"), gemstoneColor);
		this.lucid = lucid;
	}
	
	public boolean hasGlint(ItemStack stack) {
		return lucid;
	}
	
}
