package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class PigmentItem extends CloakedItem {

	protected final DyeColor color;

	public PigmentItem(Settings settings, DyeColor color) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "craft_colored_sapling"), getDyeItemForDyeColor(color));
		this.color = color;
	}

	public DyeColor getColor() {
		return this.color;
	}

	public static Item getDyeItemForDyeColor(DyeColor dyeColor) {
		switch (dyeColor) {
			case BLACK -> { return Items.BLACK_DYE; }
			case BLUE -> { return Items.BLUE_DYE; }
			case BROWN -> { return Items.BROWN_DYE; }
			case CYAN -> { return Items.CYAN_DYE; }
			case GRAY -> { return Items.GRAY_DYE; }
			case GREEN -> { return Items.GREEN_DYE; }
			case LIGHT_BLUE -> { return Items.LIGHT_BLUE_DYE; }
			case LIGHT_GRAY -> { return Items.LIGHT_GRAY_DYE; }
			case LIME -> { return Items.LIME_DYE; }
			case MAGENTA -> { return Items.MAGENTA_DYE; }
			case ORANGE -> { return Items.ORANGE_DYE; }
			case PINK -> { return Items.PINK_DYE; }
			case PURPLE -> { return Items.PURPLE_DYE; }
			case RED -> { return Items.RED_DYE; }
			case WHITE -> { return Items.WHITE_DYE; }
			default -> { return Items.YELLOW_DYE; }
		}
	}

}
