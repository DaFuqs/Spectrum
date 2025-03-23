package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;
import org.joml.*;

import java.awt.*;
import java.lang.Math;
import java.util.List;
import java.util.*;

public class ColorHelper {
	
	/**
	 * A list of the first 16 dye colors
	 * In case a mod extends the DyeColor enum
	 */
	public static List<DyeColor> VANILLA_DYE_COLORS = Arrays.stream(DyeColor.values()).filter(dyeColor -> dyeColor.getId() < 16).toList();
	public static final Vector3f WASH = new Vector3f(1F, 1F, 1F);
	
	public static Vector3f getRGBVec(DyeColor dyeColor) {
		return InkColor.ofDyeColor(dyeColor).getColorVec();
	}
	
	public static int getInt(DyeColor dyeColor) {
		Vector3f vec = getRGBVec(dyeColor);
		return new Color(vec.x(), vec.y(), vec.z()).getRGB() & 0x00FFFFFF;
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
	public static Vector3f colorIntToVec(int color) {
		Color colorObj = new Color(color);
		float[] argb = new float[4];
		colorObj.getColorComponents(argb);
		return new Vector3f(argb[0], argb[1], argb[2]);
	}
	
	public static int colorVecToRGB(Vector3f color) {
		Color colorObj = new Color(color.x, color.y, color.z);
		return colorObj.getRGB();
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
	
	public static int interpolate(Vector3f start, Vector3f end, float delta) {
		var blendedRed = Math.round(MathHelper.lerp(delta, start.x, end.x) * 255F);
		var blendedGreen = Math.round(MathHelper.lerp(delta, start.y, end.y) * 255F);
		var blendedBlue = Math.round(MathHelper.lerp(delta, start.z, end.z) * 255F);
		return (blendedRed & 255) << 16 | (blendedGreen & 255) << 8 | (blendedBlue & 255) | 0xFF000000;
	}
	
}