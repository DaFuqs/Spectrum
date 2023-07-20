package de.dafuqs.spectrum.energy.color;

import de.dafuqs.spectrum.*;
import net.minecraft.util.*;
import org.joml.*;

import java.util.*;

public class InkColors {
	
	public static final Identifier BASE_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static final Identifier BLACK_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static final Identifier WHITE_ADVANCEMENT_ID = SpectrumCommon.locate("lategame/collect_moonstone_shard");
	
	
	public static final ElementalColor CYAN = new ElementalColor(DyeColor.CYAN, new Vector3f(0.36f, 1f, 0.93f), BASE_ADVANCEMENT_ID);
	public static final ElementalColor MAGENTA = new ElementalColor(DyeColor.MAGENTA, new Vector3f(0.92f, 0.25f, 0.94f), BASE_ADVANCEMENT_ID);
	public static final ElementalColor YELLOW = new ElementalColor(DyeColor.YELLOW, new Vector3f(0.93F, 0.93F, 0.0F), BASE_ADVANCEMENT_ID);
	public static final ElementalColor BLACK = new ElementalColor(DyeColor.BLACK, new Vector3f(0.04f, 0.04f, 0.04f), BLACK_ADVANCEMENT_ID);
	public static final ElementalColor WHITE = new ElementalColor(DyeColor.WHITE, new Vector3f(0.93f, 0.95f, 0.98f), WHITE_ADVANCEMENT_ID);
	
	public static final CompoundColor ORANGE = new CompoundColor(DyeColor.ORANGE, new Vector3f(0.90F, 0.38F, 0.0F), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}});
	public static final CompoundColor LIME = new CompoundColor(DyeColor.LIME, new Vector3f(0.23f, 1f, 0.22f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 1F / 3F);
		put(YELLOW, 2F / 3F);
	}});
	public static final CompoundColor PINK = new CompoundColor(DyeColor.PINK, new Vector3f(1.0F, 0.78F, 0.87F), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 3F);
		put(YELLOW, 1F / 3F);
	}});
	public static final CompoundColor RED = new CompoundColor(DyeColor.RED, new Vector3f(0.85f, 0.1f, 0.04f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 4F);
		put(YELLOW, 2F / 4F);
	}});
	public static final CompoundColor LIGHT_BLUE = new CompoundColor(DyeColor.LIGHT_BLUE, new Vector3f(0.69f, 0.78f, 1f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 4F);
		put(MAGENTA, 1F / 4F);
	}});
	public static final CompoundColor GREEN = new CompoundColor(DyeColor.GREEN, new Vector3f(0.32f, 0.42f, 0.06f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 5F);
		put(MAGENTA, 1F / 5F);
		put(YELLOW, 2F / 5F);
	}});
	public static final CompoundColor BLUE = new CompoundColor(DyeColor.BLUE, new Vector3f(0.24f, 0.31f, 0.76f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 3F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 1F / 6F);
	}});
	public static final CompoundColor PURPLE = new CompoundColor(DyeColor.PURPLE, new Vector3f(0.5f, 0.17f, 0.77f), BASE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 3F / 6F);
		put(YELLOW, 1F / 6F);
	}});
	public static final CompoundColor BROWN = new CompoundColor(DyeColor.BROWN, new Vector3f(0.44f, 0.25f, 0.05f), BLACK_ADVANCEMENT_ID, new HashMap<>() {{
		put(MAGENTA, 2F / 5F);
		put(YELLOW, 1F / 5F);
		put(BLACK, 2F / 5F);
	}});
	public static final CompoundColor LIGHT_GRAY = new CompoundColor(DyeColor.LIGHT_GRAY, new Vector3f(0.68F, 0.68F, 0.68F), WHITE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 2F / 6F);
	}});
	public static final CompoundColor GRAY = new CompoundColor(DyeColor.GRAY, new Vector3f(0.26f, 0.25f, 0.28f), WHITE_ADVANCEMENT_ID, new HashMap<>() {{
		put(CYAN, 2F / 6F);
		put(MAGENTA, 2F / 6F);
		put(YELLOW, 2F / 6F);
	}});
	
	public static void register() {
	
	}
	
}
