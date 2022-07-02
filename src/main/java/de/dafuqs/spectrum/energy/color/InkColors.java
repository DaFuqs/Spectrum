package de.dafuqs.spectrum.energy.color;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import java.util.HashMap;

public class InkColors {
//Hello
    public static Identifier BASE_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
    public static Identifier BLACK_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
    public static Identifier WHITE_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/collect_moonstone_shard");
	
	public static Identifier BASE_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static Identifier BLACK_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static Identifier WHITE_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/collect_moonstone_shard");
	
	public static ElementalColor CYAN = new ElementalColor(DyeColor.CYAN, new Vec3f(0.36f, 1f, 0.93f), BASE_ADVANCEMENT_ID);
	public static ElementalColor MAGENTA = new ElementalColor(DyeColor.MAGENTA, new Vec3f(0.84f, 0.25f, 0.94f), BASE_ADVANCEMENT_ID);
	public static ElementalColor YELLOW = new ElementalColor(DyeColor.YELLOW, new Vec3f(0.97f, 0.75f, 0.24f), BASE_ADVANCEMENT_ID);
	public static ElementalColor BLACK = new ElementalColor(DyeColor.BLACK, new Vec3f(0.07f, 0.07f, 0.07f), BLACK_ADVANCEMENT_ID);
	public static ElementalColor WHITE = new ElementalColor(DyeColor.WHITE, new Vec3f(0.93f, 0.95f, 0.98f), WHITE_ADVANCEMENT_ID);

	public static CompoundColor ORANGE = new CompoundColor(DyeColor.ORANGE, new Vec3f(1f, 0.61f, 0.08f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}});
	public static CompoundColor LIME = new CompoundColor(DyeColor.LIME, new Vec3f(0.73f, 1f, 0.22f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}});
	public static CompoundColor PINK = new CompoundColor(DyeColor.PINK, new Vec3f(0.99f, 0.56f, 1f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 3F);
		put(YELLOW, 1F / 3F);
	}});
	public static CompoundColor RED = new CompoundColor(DyeColor.RED, new Vec3f(0.71f, 0.19f, 0.09f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 4F);
		put(YELLOW, 2F / 4F);
	}});
	public static CompoundColor LIGHT_BLUE = new CompoundColor(DyeColor.LIGHT_BLUE, new Vec3f(0.69f, 0.78f, 1f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 4F);
		put(MAGENTA, 1F / 4F);
	}});
	public static CompoundColor GREEN = new CompoundColor(DyeColor.GREEN, new Vec3f(0.32f, 0.42f, 0.06f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 5F);
		put(MAGENTA, 1F / 5F);
		put(YELLOW, 2F / 5F);
	}});
	public static CompoundColor BLUE = new CompoundColor(DyeColor.BLUE, new Vec3f(0.24f, 0.31f, 0.76f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 1F / 6F);
	}});
	public static CompoundColor PURPLE = new CompoundColor(DyeColor.PURPLE, new Vec3f(0.69f, 0.33f, 1f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 3F / 6F);
		put(YELLOW, 1F / 6F);
	}});
	public static CompoundColor BROWN = new CompoundColor(DyeColor.BROWN, new Vec3f(0.44f, 0.25f, 0.05f), BLACK_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 5F);
		put(YELLOW, 1F / 5F);
		put(BLACK, 2F / 5F);
	}});
	public static CompoundColor LIGHT_GRAY = new CompoundColor(DyeColor.LIGHT_GRAY, new Vec3f(0.76f, 0.72f, 0.69f), WHITE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 2F / 6F);
	}});
	public static CompoundColor GRAY = new CompoundColor(DyeColor.GRAY, new Vec3f(0.29f, 0.28f, 0.31f), WHITE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 2F / 6F);
	}});
	
	public static void register() {
	
	}
	
}
