package de.dafuqs.spectrum.api.energy.color;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.*;
import org.joml.*;

import java.util.*;

public class InkColors {
	
	public static final Identifier BASE_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static final Identifier BLACK_ADVANCEMENT_ID = SpectrumCommon.locate("midgame/spectrum_midgame");
	public static final Identifier WHITE_ADVANCEMENT_ID = SpectrumCommon.locate("lategame/collect_moonstone");
	
	/**
	 * A lot of places where color is displayed have black backgrounds, which would make displaying normal black on them... daft.
	 * <p>
	 * So, instead, we use something closer to midnight solution in shade.
	 */
	public static final int BLACK_TEXT_COLOR = 0xff302951;
	public static final Vector3f BLACK_TEXT_VEC = ColorHelper.colorIntToVec(BLACK_TEXT_COLOR);
	
	public static final int CYAN_COLOR = 0xff5bffed;
	public static final int MAGENTA_COLOR = 0xffff4ff6;
	public static final int YELLOW_COLOR = 0xffeded00;
	public static final int BLACK_COLOR = 0xff020106;
	public static final int WHITE_COLOR = 0xffffffff;
	public static final int ORANGE_COLOR = 0xfff97b2d;
	public static final int LIME_COLOR = 0xff98ff37;
	public static final int PINK_COLOR = 0xffff9fc6;
	public static final int RED_COLOR = 0xfff12a34;
	public static final int LIGHT_BLUE_COLOR = 0xff7a9eff;
	public static final int GREEN_COLOR = 0xff526b0f;
	public static final int BLUE_COLOR = 0xff2432ff;
	public static final int PURPLE_COLOR = 0xff802bc4;
	public static final int BROWN_COLOR = 0xff70400d;
	public static final int LIGHT_GRAY_COLOR = 0xffadadad;
	public static final int GRAY_COLOR = 0xff464646;
	
	public static final InkColor CYAN = register("cyan", new InkColor(DyeColor.CYAN, CYAN_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor LIGHT_BLUE = register("light_blue", new InkColor(DyeColor.LIGHT_BLUE, LIGHT_BLUE_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor BLUE = register("blue", new InkColor(DyeColor.BLUE, BLUE_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor PURPLE = register("purple", new InkColor(DyeColor.PURPLE, PURPLE_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor MAGENTA = register("magenta", new InkColor(DyeColor.MAGENTA, MAGENTA_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor PINK = register("pink", new InkColor(DyeColor.PINK, PINK_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor RED = register("red", new InkColor(DyeColor.RED, RED_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor ORANGE = register("orange", new InkColor(DyeColor.ORANGE, ORANGE_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor YELLOW = register("yellow", new InkColor(DyeColor.YELLOW, YELLOW_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor LIME = register("lime", new InkColor(DyeColor.LIME, LIME_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor GREEN = register("green", new InkColor(DyeColor.GREEN, GREEN_COLOR, BASE_ADVANCEMENT_ID));
	public static final InkColor BROWN = register("brown", new InkColor(DyeColor.BROWN, BROWN_COLOR, BLACK_ADVANCEMENT_ID));
	public static final InkColor BLACK = register("black", new InkColor(DyeColor.BLACK, BLACK_COLOR, BLACK_TEXT_COLOR, BLACK_ADVANCEMENT_ID));
	public static final InkColor GRAY = register("gray", new InkColor(DyeColor.GRAY, GRAY_COLOR, WHITE_ADVANCEMENT_ID));
	public static final InkColor LIGHT_GRAY = register("light_gray", new InkColor(DyeColor.LIGHT_GRAY, LIGHT_GRAY_COLOR, WHITE_ADVANCEMENT_ID));
	public static final InkColor WHITE = register("white", new InkColor(DyeColor.WHITE, WHITE_COLOR, WHITE_ADVANCEMENT_ID));

	public static final InkColor BLANK = register("blank", new InkColor(DyeColor.WHITE, WHITE_COLOR, BASE_ADVANCEMENT_ID) {
		@Override
		public boolean isBlank() {
			return true;
		}
	});
	
	// in case an addon adds new colors
	// for places where we have to use a fixed size list, like GUIs with limited space
	public static final List<InkColor> BUILTIN_COLORS = List.of(InkColors.CYAN, InkColors.LIGHT_BLUE, InkColors.BLUE, InkColors.PURPLE, InkColors.MAGENTA, InkColors.PINK, InkColors.RED, InkColors.ORANGE, InkColors.YELLOW, InkColors.LIME, InkColors.GREEN, InkColors.BROWN, InkColors.BLACK, InkColors.GRAY, InkColors.LIGHT_GRAY, InkColors.WHITE);
	
	protected static InkColor register(String name, InkColor inkColor) {
		return Registry.register(SpectrumRegistries.INK_COLORS, SpectrumCommon.locate(name), inkColor);
	}
	
	public static void register() {
	
	}
	
	public static Iterable<InkColor> all() {
		return SpectrumRegistries.INK_COLORS;
	}
	
	public static RegistryEntryList.Named<InkColor> elementals() {
		return SpectrumRegistries.INK_COLORS.getEntryList(InkColorTags.ELEMENTAL_COLORS).get();
	}
	
	public static RegistryEntryList.Named<InkColor> compounds() {
		return SpectrumRegistries.INK_COLORS.getEntryList(InkColorTags.COMPOUND_COLORS).get();
	}
	
}
