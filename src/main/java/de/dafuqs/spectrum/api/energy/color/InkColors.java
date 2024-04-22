package de.dafuqs.spectrum.api.energy.color;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

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
	public static final int ALT_BLACK = 0x302951;
	
	public static final ElementalColor CYAN = registerElemental("cyan", new ElementalColor(DyeColor.CYAN, ColorHelper.colorIntToVec(0x5bffed), BASE_ADVANCEMENT_ID));
	public static final ElementalColor MAGENTA = registerElemental("magenta", new ElementalColor(DyeColor.MAGENTA, ColorHelper.colorIntToVec(0xff4ff6), BASE_ADVANCEMENT_ID));
	public static final ElementalColor YELLOW = registerElemental("yellow", new ElementalColor(DyeColor.YELLOW, ColorHelper.colorIntToVec(0xeded00), BASE_ADVANCEMENT_ID));
	public static final ElementalColor BLACK = registerElemental("black", new ElementalColor(DyeColor.BLACK, ColorHelper.colorIntToVec(0x020106), BLACK_ADVANCEMENT_ID, true));
	public static final ElementalColor WHITE = registerElemental("white", new ElementalColor(DyeColor.WHITE, ColorHelper.colorIntToVec(0xFFFFFF), WHITE_ADVANCEMENT_ID));

	public static final CompoundColor ORANGE = registerCompound("orange", new CompoundColor(DyeColor.ORANGE, ColorHelper.colorIntToVec(0xff6303), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}}));
	public static final CompoundColor LIME = registerCompound("lime", new CompoundColor(DyeColor.LIME, ColorHelper.colorIntToVec(0x98ff37), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}}));
	public static final CompoundColor PINK = registerCompound("pink", new CompoundColor(DyeColor.PINK, ColorHelper.colorIntToVec(0xff9fc6), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 3F);
		put(YELLOW, 1F / 3F);
	}}));
	public static final CompoundColor RED = registerCompound("red", new CompoundColor(DyeColor.RED, ColorHelper.colorIntToVec(0xff000d), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 4F);
		put(YELLOW, 2F / 4F);
	}}));
	public static final CompoundColor LIGHT_BLUE = registerCompound("light_blue", new CompoundColor(DyeColor.LIGHT_BLUE, ColorHelper.colorIntToVec(0x7a9eff), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 4F);
		put(MAGENTA, 1F / 4F);
	}}));
	public static final CompoundColor GREEN = registerCompound("green", new CompoundColor(DyeColor.GREEN, ColorHelper.colorIntToVec(0x526b0f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 5F);
		put(MAGENTA, 1F / 5F);
		put(YELLOW, 2F / 5F);
	}}));
	public static final CompoundColor BLUE = registerCompound("blue", new CompoundColor(DyeColor.BLUE, ColorHelper.colorIntToVec(0x2432ff), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 1F / 6F);
	}}));
	public static final CompoundColor PURPLE = registerCompound("purple", new CompoundColor(DyeColor.PURPLE, ColorHelper.colorIntToVec(0x802bc4), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 3F / 6F);
		put(YELLOW, 1F / 6F);
	}}));
	public static final CompoundColor BROWN = registerCompound("brown", new CompoundColor(DyeColor.BROWN, ColorHelper.colorIntToVec(0x70400d), BLACK_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 5F);
		put(YELLOW, 1F / 5F);
		put(BLACK, 2F / 5F);
	}}, true));
	public static final CompoundColor LIGHT_GRAY = registerCompound("light_gray", new CompoundColor(DyeColor.LIGHT_GRAY, ColorHelper.colorIntToVec(0xadadad), WHITE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 2F / 6F);
	}}));
	public static final CompoundColor GRAY = registerCompound("gray", new CompoundColor(DyeColor.GRAY, ColorHelper.colorIntToVec(0x464646), WHITE_ADVANCEMENT_ID, new HashMap<>() {{
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
