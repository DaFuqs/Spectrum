package de.dafuqs.spectrum.api.energy.color;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import org.joml.*;

import java.util.*;

public class InkColors {
	
	public static final Identifier BASE_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static final Identifier BLACK_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static final Identifier WHITE_ADVANCEMENT_ID = SpectrumCommon.locate("lategame/collect_moonstone_shard");
	
	public static final ElementalColor CYAN = registerElemental("cyan", new ElementalColor(DyeColor.CYAN, new Vector3f(0.36f, 1f, 0.93f), BASE_ADVANCEMENT_ID));
	public static final ElementalColor MAGENTA = registerElemental("magenta", new ElementalColor(DyeColor.MAGENTA, new Vector3f(0.92f, 0.25f, 0.94f), BASE_ADVANCEMENT_ID));
	public static final ElementalColor YELLOW = registerElemental("yellow", new ElementalColor(DyeColor.YELLOW, new Vector3f(0.93F, 0.93F, 0.0F), BASE_ADVANCEMENT_ID));
	public static final ElementalColor BLACK = registerElemental("black", new ElementalColor(DyeColor.BLACK, new Vector3f(0.04f, 0.04f, 0.04f), BLACK_ADVANCEMENT_ID));
	public static final ElementalColor WHITE = registerElemental("white", new ElementalColor(DyeColor.WHITE, new Vector3f(0.93f, 0.95f, 0.98f), WHITE_ADVANCEMENT_ID));

	public static final CompoundColor ORANGE = registerCompound("orange", new CompoundColor(DyeColor.ORANGE, new Vector3f(0.90F, 0.38F, 0.0F), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}}));
	public static final CompoundColor LIME = registerCompound("lime", new CompoundColor(DyeColor.LIME, new Vector3f(0.23f, 1f, 0.22f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}}));
	public static final CompoundColor PINK = registerCompound("pink", new CompoundColor(DyeColor.PINK, new Vector3f(1.0F, 0.78F, 0.87F), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 3F);
		put(YELLOW, 1F / 3F);
	}}));
	public static final CompoundColor RED = registerCompound("red", new CompoundColor(DyeColor.RED, new Vector3f(0.85f, 0.1f, 0.04f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 4F);
		put(YELLOW, 2F / 4F);
	}}));
	public static final CompoundColor LIGHT_BLUE = registerCompound("light_blue", new CompoundColor(DyeColor.LIGHT_BLUE, new Vector3f(0.69f, 0.78f, 1f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 4F);
		put(MAGENTA, 1F / 4F);
	}}));
	public static final CompoundColor GREEN = registerCompound("green", new CompoundColor(DyeColor.GREEN, new Vector3f(0.32f, 0.42f, 0.06f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 5F);
		put(MAGENTA, 1F / 5F);
		put(YELLOW, 2F / 5F);
	}}));
	public static final CompoundColor BLUE = registerCompound("blue", new CompoundColor(DyeColor.BLUE, new Vector3f(0.24f, 0.31f, 0.76f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 1F / 6F);
	}}));
	public static final CompoundColor PURPLE = registerCompound("purple", new CompoundColor(DyeColor.PURPLE, new Vector3f(0.5f, 0.17f, 0.77f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 3F / 6F);
		put(YELLOW, 1F / 6F);
	}}));
	public static final CompoundColor BROWN = registerCompound("brown", new CompoundColor(DyeColor.BROWN, new Vector3f(0.44f, 0.25f, 0.05f), BLACK_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 5F);
		put(YELLOW, 1F / 5F);
		put(BLACK, 2F / 5F);
	}}));
	public static final CompoundColor LIGHT_GRAY = registerCompound("light_gray", new CompoundColor(DyeColor.LIGHT_GRAY, new Vector3f(0.68F, 0.68F, 0.68F), WHITE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 2F / 6F);
	}}));
	public static final CompoundColor GRAY = registerCompound("gray", new CompoundColor(DyeColor.GRAY, new Vector3f(0.26f, 0.25f, 0.28f), WHITE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 2F / 6F);
	}}));
	
	public static void register() {
	
	}
	
	protected static ElementalColor registerElemental(String name, ElementalColor inkColor) {
		return Registry.register(SpectrumRegistries.INK_COLORS, new Identifier(SpectrumCommon.MOD_ID, name), inkColor);
	}

	protected static CompoundColor registerCompound(String name, CompoundColor inkColor) {
		return Registry.register(SpectrumRegistries.INK_COLORS, new Identifier(SpectrumCommon.MOD_ID, name), inkColor);
	}

}
