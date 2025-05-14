package de.dafuqs.spectrum.helpers;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import org.joml.*;

import java.awt.*;
import java.lang.Math;
import java.util.*;
import java.util.List;
import java.util.regex.*;

public class SpectrumColorHelper {
	
	public static final Codec<Integer> CODEC = Codec.withAlternative(Codec.INT, Codec.STRING.comapFlatMap(CodecHelper.throwable(SpectrumColorHelper::fromString), SpectrumColorHelper::toString));
	
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
	
	private static final Pattern PARSE_PATTERN = Pattern.compile("#([0-9a-fA-F]{2})(?:([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})?)?");
	
	private int parseHex(String str) {
		return Integer.valueOf(str, 16);
	}
	
	/**
	 * Parses a hex color code into an ARGB int. The following patterns are accepted:
	 * <ul>
	 * <li>With opactiy: <code>#RRGGBBAA</code> -> <code>Argb(AA, RR, GG, BB)</code></li>
	 * <li>Full opacity: <code>#RRGGBB</code> -> <code>Argb(-1, RR, GG, BB)</code></li>
	 * <li>Grayscale: <code>#XX</code> -> <code>Argb(-1, XX, XX, XX)</code></li>
	 * </ul>
	 *
	 * @param color The string to parse
	 * @return The parsed ARGB color code
	 * @throws NumberFormatException If the pattern does not match
	 */
	public static int fromString(String color) throws NumberFormatException {
		Matcher m = PARSE_PATTERN.matcher(color);
		if (!m.matches())
			throw new NumberFormatException("Failed to parse the hex code '" + color + "'");
		
		int c1 = Integer.parseInt(m.group(1), 16);
		int c2 = m.group(2) == null ? c1 : Integer.parseInt(m.group(2), 16);
		int c3 = m.group(3) == null ? c1 : Integer.parseInt(m.group(3), 16);
		int c4 = m.group(4) == null ? -1 : Integer.parseInt(m.group(4), 16);
		
		return net.minecraft.util.FastColor.ARGB32.color(c4, c1, c2, c3);
	}
	
	public static String toString(int color) {
		return String.format("#%X%X%X%X",
				net.minecraft.util.FastColor.ARGB32.alpha(color),
				net.minecraft.util.FastColor.ARGB32.red(color),
				net.minecraft.util.FastColor.ARGB32.green(color),
				net.minecraft.util.FastColor.ARGB32.blue(color)
		);
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
				return Optional.of(dyeItem.getDyeColor());
			} else if (item instanceof PigmentItem pigmentItem) {
				return pigmentItem.getInkColor().getDyeColor();
			}
		}
		return Optional.empty();
	}
	
	public static int interpolate(Vector3f start, Vector3f end, float delta) {
		var blendedRed = Math.round(Mth.lerp(delta, start.x, end.x) * 255F);
		var blendedGreen = Math.round(Mth.lerp(delta, start.y, end.y) * 255F);
		var blendedBlue = Math.round(Mth.lerp(delta, start.z, end.z) * 255F);
		return (blendedRed & 255) << 16 | (blendedGreen & 255) << 8 | (blendedBlue & 255) | 0xFF000000;
	}
	
}