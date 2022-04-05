package de.dafuqs.spectrum.registries.color;

import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Optional;

public abstract class ColorRegistry<T> {
	
	public static ItemColorRegistry ITEM_COLORS;
	public static FluidColorRegistry FLUID_COLORS;
	
	public static void registerColorRegistries() {
		ITEM_COLORS = new ItemColorRegistry();
		FLUID_COLORS = new FluidColorRegistry();
	}

	public abstract void registerColorMapping(Identifier identifier, DyeColor dyeColor);
	
	public abstract void registerColorMapping(T object, DyeColor dyeColor);
	
	public abstract Optional<DyeColor> getMapping(T element);
	
	public static Vec3f getColor(DyeColor dyeColor) {
		return switch (dyeColor) {
			case BLACK -> new Vec3f(0.1F, 0.1F, 0.1F);
			case BLUE -> new Vec3f(0.05F, 0.011F, 0.95F);
			case BROWN ->  new Vec3f(0.31F, 0.16F, 0.05F);
			case CYAN ->  new Vec3f(0.0F, 1.0F, 1.0F);
			case GRAY ->  new Vec3f(0.3F, 0.3F, 0.3F);
			case GREEN -> new Vec3f(0.14F, 0.24F, 0.0F);
			case LIGHT_BLUE -> new Vec3f(0.0F, 0.75F, 0.95F);
			case LIGHT_GRAY -> new Vec3f(0.68F, 0.68F, 0.68F);
			case LIME -> new Vec3f(0.0F, 0.86F, 0.0F);
			case MAGENTA -> new Vec3f(1.0F, 0.0F, 1.0F);
			case ORANGE -> new Vec3f(0.93F, 0.39F, 0.0F);
			case PINK -> new Vec3f(1.0F, 0.78F, 0.87F);
			case PURPLE -> new Vec3f(0.43F, 0.0F, 0.68F);
			case RED -> new Vec3f(0.95F, 0.0F, 0.0F);
			case WHITE -> new Vec3f( 0.97F, 0.97F, 0.97F);
			default -> new Vec3f(0.93F, 0.93F, 0.0F);
		};
	}
	
	public static Color colorFromRGB(int r, int g, int b) {
		return colorFromRGBA(r, g, b, 255);
	}
	
	public static Color colorFromRGB(float r, float g, float b) {
		return colorFromRGBA(r, g, b, 1f);
	}
	
	public static Color colorFromRGBA(float r, float g, float b, float a) {
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
	
}
