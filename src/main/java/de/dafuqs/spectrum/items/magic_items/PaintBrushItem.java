package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.energy.color.InkColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class PaintBrushItem extends Item {
	
	public static final String COLOR_NBT_STRING = "Color";
	
	public PaintBrushItem(Settings settings) {
		super(settings);
	}
	
	static void setColor(ItemStack stack, InkColor color) {
		NbtCompound compound = stack.getOrCreateNbt();
		compound.putString(COLOR_NBT_STRING, color.toString());
		stack.setNbt(compound);
	}
	
	static Optional<InkColor> getColor(ItemStack stack) {
		NbtCompound compound = stack.getNbt();
		if (compound != null && compound.contains(COLOR_NBT_STRING)) {
			return Optional.of(InkColor.of(compound.getString(COLOR_NBT_STRING)));
		}
		return Optional.empty();
	}
	
}
