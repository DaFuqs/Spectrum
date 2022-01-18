package de.dafuqs.spectrum;

import de.dafuqs.spectrum.items.PigmentItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import oshi.util.tuples.Triplet;

import java.util.HashMap;
import java.util.Optional;

public class ColorHelper {
	
	private static final HashMap<DyeColor, Triplet<Float, Float, Float>> dyeColors = new HashMap<>() {{
		put(DyeColor.BLACK, new Triplet<>(0.1F, 0.1F, 0.1F));
		put(DyeColor.BROWN, new Triplet<>(0.31F, 0.16F, 0.05F));
		put(DyeColor.CYAN, new Triplet<>(0.0F, 1.0F, 1.0F));
		put(DyeColor.GRAY, new Triplet<>(0.3F, 0.3F, 0.3F));
		put(DyeColor.GREEN, new Triplet<>(0.14F, 0.24F, 0.0F));
		put(DyeColor.LIGHT_BLUE, new Triplet<>(0.0F, 0.75F, 0.95F));
		put(DyeColor.LIGHT_GRAY, new Triplet<>(0.68F, 0.68F, 0.68F));
		put(DyeColor.LIME, new Triplet<>(0.0F, 0.86F, 0.0F));
		put(DyeColor.MAGENTA, new Triplet<>(1.0F, 0.0F, 1.0F));
		put(DyeColor.ORANGE, new Triplet<>(0.93F, 0.39F, 0.0F));
		put(DyeColor.PINK, new Triplet<>(1.0F, 0.78F, 0.87F));
		put(DyeColor.PURPLE, new Triplet<>(0.43F, 0.0F, 0.68F));
		put(DyeColor.RED, new Triplet<>(0.95F, 0.0F, 0.0F));
		put(DyeColor.WHITE, new Triplet<>(0.97F, 0.97F, 0.97F));
		put(DyeColor.YELLOW, new Triplet<>(0.93F, 0.93F, 0.0F));
	}};
	
	public static Triplet<Float, Float, Float> getRGB(DyeColor dyeColor) {
		return dyeColors.get(dyeColor);
	}
	
	public static Optional<DyeColor> getDyeColorOfItemStack(ItemStack itemStack) {
		if (!itemStack.isEmpty()) {
			Item item = itemStack.getItem();
			if (item instanceof DyeItem dyeItem) {
				return Optional.of(dyeItem.getColor());
			} else if (item instanceof PigmentItem pigmentItem) {
				return Optional.of(pigmentItem.getColor());
			}
		}
		return Optional.empty();
	}
	
}