package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ColorHelper {

	/**
	 * A list of the first 16 dye colors
	 * In case a mod extends the DyeColor enum
	 */
	public static List<DyeColor> VANILLA_DYE_COLORS = Arrays.stream(DyeColor.values()).filter(dyeColor -> dyeColor.getId() < 16).toList();

	public static Vec3f getRGBVec(DyeColor dyeColor) {
		return InkColor.of(dyeColor).getColor();
	}
	
	public static int getInt(DyeColor dyeColor) {
		Vec3f vec = getRGBVec(dyeColor);
		return new Color(vec.getX(), vec.getY(), vec.getZ()).getRGB() & 0x00FFFFFF;
	}
	
	/**
	 * Returns a nicely saturated random color based on seed
	 *
	 * @param seed the seed to base the random color on
	 * @return the color
	 */
	public static int getRandomColor(int seed) {
		return Color.getHSBColor((float) seed / Integer.MAX_VALUE, 0.7F, 0.9F).getRGB();
	}
	
	@NotNull
	public static Vec3f colorIntToVec(int color) {
		Color colorObj = new Color(color);
		float[] argb = new float[4];
		colorObj.getColorComponents(argb);
		return new Vec3f(argb[0], argb[1], argb[2]);
	}

	public static Optional<DyeColor> getDyeColorOfItemStack(@NotNull ItemStack itemStack) {
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