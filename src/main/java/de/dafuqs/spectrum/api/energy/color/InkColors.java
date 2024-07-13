package de.dafuqs.spectrum.api.energy.color;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import org.joml.*;

import java.util.*;

public class InkColors {
	
	public static final Identifier BASE_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static final Identifier BLACK_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static final Identifier WHITE_ADVANCEMENT_ID = SpectrumCommon.locate("lategame/collect_moonstone_shard");
	
	/**
	 * A lot of places where color is displayed have black backgrounds, which would make displaying normal black on them... daft.
	 * <p>
	 * So, instead, we use something closer to midnight solution in shade.
	 */
	public static final int ALT_BLACK_COLOR = 0x302951;
	public static final Vector3f ALT_BLACK = ColorHelper.colorIntToVec(0x302951);
	
	public static final int CYAN_COLOR = 0x5bffed;
	public static final int MAGENTA_COLOR = 0xff4ff6;
	public static final int YELLOW_COLOR = 0xeded00;
	public static final int BLACK_COLOR = 0x020106;
	public static final int WHITE_COLOR = 0xFFFFFF;
	public static final int ORANGE_COLOR = 0xff6303;
	public static final int LIME_COLOR = 0x98ff37;
	public static final int PINK_COLOR = 0xff9fc6;
	public static final int RED_COLOR = 0xff000d;
	public static final int LIGHT_BLUE_COLOR = 0x7a9eff;
	public static final int GREEN_COLOR = 0x526b0f;
	public static final int BLUE_COLOR = 0x2432ff;
	public static final int PURPLE_COLOR = 0x802bc4;
	public static final int BROWN_COLOR = 0x70400d;
	public static final int LIGHT_GRAY_COLOR = 0xadadad;
	public static final int GRAY_COLOR = 0x464646;
	
	public static final ElementalColor CYAN = registerElemental("cyan", new ElementalColor(DyeColor.CYAN, CYAN_COLOR, BASE_ADVANCEMENT_ID));
	public static final ElementalColor MAGENTA = registerElemental("magenta", new ElementalColor(DyeColor.MAGENTA, MAGENTA_COLOR, BASE_ADVANCEMENT_ID));
	public static final ElementalColor YELLOW = registerElemental("yellow", new ElementalColor(DyeColor.YELLOW, YELLOW_COLOR, BASE_ADVANCEMENT_ID));
	public static final ElementalColor BLACK = registerElemental("black", new ElementalColor(DyeColor.BLACK, BLACK_COLOR, ALT_BLACK_COLOR, BLACK_ADVANCEMENT_ID, true));
	public static final ElementalColor WHITE = registerElemental("white", new ElementalColor(DyeColor.WHITE, WHITE_COLOR, WHITE_ADVANCEMENT_ID));
	
	public static final CompoundColor ORANGE = registerCompound("orange", new CompoundColor(DyeColor.ORANGE, ORANGE_COLOR, BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}}));
	public static final CompoundColor LIME = registerCompound("lime", new CompoundColor(DyeColor.LIME, LIME_COLOR, BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}}));
	public static final CompoundColor PINK = registerCompound("pink", new CompoundColor(DyeColor.PINK, PINK_COLOR, BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 3F);
		put(YELLOW, 1F / 3F);
	}}));
	public static final CompoundColor RED = registerCompound("red", new CompoundColor(DyeColor.RED, RED_COLOR, BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 4F);
		put(YELLOW, 2F / 4F);
	}}));
	public static final CompoundColor LIGHT_BLUE = registerCompound("light_blue", new CompoundColor(DyeColor.LIGHT_BLUE, LIGHT_BLUE_COLOR, BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 4F);
		put(MAGENTA, 1F / 4F);
	}}));
	public static final CompoundColor GREEN = registerCompound("green", new CompoundColor(DyeColor.GREEN, GREEN_COLOR, BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 5F);
		put(MAGENTA, 1F / 5F);
		put(YELLOW, 2F / 5F);
	}}));
	public static final CompoundColor BLUE = registerCompound("blue", new CompoundColor(DyeColor.BLUE, BLUE_COLOR, BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 1F / 6F);
	}}));
	public static final CompoundColor PURPLE = registerCompound("purple", new CompoundColor(DyeColor.PURPLE, PURPLE_COLOR, BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 3F / 6F);
		put(YELLOW, 1F / 6F);
	}}));
	public static final CompoundColor BROWN = registerCompound("brown", new CompoundColor(DyeColor.BROWN, BROWN_COLOR, BROWN_COLOR, BLACK_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 5F);
		put(YELLOW, 1F / 5F);
		put(BLACK, 2F / 5F);
	}}, true));
	public static final CompoundColor LIGHT_GRAY = registerCompound("light_gray", new CompoundColor(DyeColor.LIGHT_GRAY, LIGHT_GRAY_COLOR, WHITE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 2F / 6F);
	}}));
	public static final CompoundColor GRAY = registerCompound("gray", new CompoundColor(DyeColor.GRAY, GRAY_COLOR, GRAY_COLOR, WHITE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 2F / 6F);
	}}, true));
	
	public static void register() {
	
	}
	
	protected static ElementalColor registerElemental(String name, ElementalColor inkColor) {
		return Registry.register(SpectrumRegistries.INK_COLORS, new Identifier(SpectrumCommon.MOD_ID, name), inkColor);
	}

	protected static CompoundColor registerCompound(String name, CompoundColor inkColor) {
		return Registry.register(SpectrumRegistries.INK_COLORS, new Identifier(SpectrumCommon.MOD_ID, name), inkColor);
	}

}
