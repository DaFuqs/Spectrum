package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.items.PigmentItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Optional;

public class ColorHelper {
	
	public static Vec3f getVec(DyeColor dyeColor) {
		return switch (dyeColor) {
			case BLACK -> new Vec3f(0.1F, 0.1F, 0.1F);
			case BLUE -> new Vec3f(0.05F, 0.011F, 0.95F);
			case BROWN -> new Vec3f(0.31F, 0.16F, 0.05F);
			case CYAN -> new Vec3f(0.0F, 1.0F, 1.0F);
			case GRAY -> new Vec3f(0.3F, 0.3F, 0.3F);
			case GREEN -> new Vec3f(0.14F, 0.24F, 0.0F);
			case LIGHT_BLUE -> new Vec3f(0.0F, 0.75F, 0.95F);
			case LIGHT_GRAY -> new Vec3f(0.68F, 0.68F, 0.68F);
			case LIME -> new Vec3f(0.0F, 0.86F, 0.0F);
			case MAGENTA -> new Vec3f(1.0F, 0.0F, 1.0F);
			case ORANGE -> new Vec3f(0.93F, 0.39F, 0.0F);
			case PINK -> new Vec3f(1.0F, 0.78F, 0.87F);
			case PURPLE -> new Vec3f(0.43F, 0.0F, 0.68F);
			case RED -> new Vec3f(0.95F, 0.0F, 0.0F);
			case WHITE -> new Vec3f(0.97F, 0.97F, 0.97F);
			default -> new Vec3f(0.93F, 0.93F, 0.0F);
		};
	}
	
	public static int getInt(DyeColor dyeColor) {
		Vec3f vec = getVec(dyeColor);
		return new Color(vec.getX(), vec.getY(), vec.getZ()).getRGB();
	}
	
	@Contract(value = "_, _, _ -> new", pure = true)
	public static @NotNull Color colorFromRGB(int r, int g, int b) {
		return colorFromRGBA(r, g, b, 255);
	}
	
	@Contract(value = "_, _, _ -> new", pure = true)
	public static @NotNull Color colorFromRGB(float r, float g, float b) {
		return colorFromRGBA(r, g, b, 1f);
	}
	
	@Contract(value = "_, _, _, _ -> new", pure = true)
	public static @NotNull Color colorFromRGBA(float r, float g, float b, float a) {
		return new Color(
				(int) (r * 255 + 0.5),
				(int) (g * 255 + 0.5),
				(int) (b * 255 + 0.5),
				(int) (a * 255 + 0.5)
		);
	}
	
	@NotNull
	public static Vec3f colorIntToVec(int color2) {
		Color colorValue2 = new Color(color2);
		float[] argb2 = new float[4];
		colorValue2.getColorComponents(argb2);
		return new Vec3f(argb2[0], argb2[1], argb2[2]);
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