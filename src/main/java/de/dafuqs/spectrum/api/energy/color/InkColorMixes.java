package de.dafuqs.spectrum.api.energy.color;

import net.minecraft.util.math.random.Random;

import java.util.*;

import static de.dafuqs.spectrum.api.energy.color.InkColors.*;

public class InkColorMixes {
	
	private static final Map<InkColor, Map<InkColor, Float>> MIXES = new HashMap<>();
	
	public static void registerMix(InkColor color, Map<InkColor, Float> compounds) {
		MIXES.put(color, compounds);
	}
	
	public static void register() {
		registerMix(InkColors.LIGHT_BLUE, Map.of(CYAN, 2F / 3F, MAGENTA, 1F / 3F));
		registerMix(InkColors.BLUE, Map.of(CYAN, 3F / 6F, MAGENTA, 2F / 6F, YELLOW, 1F / 6F));
		registerMix(InkColors.PURPLE, Map.of(CYAN, 2F / 6F, MAGENTA, 3F / 6F, YELLOW, 1F / 6F));
		registerMix(InkColors.PINK, Map.of(MAGENTA, 2F / 3F, YELLOW, 1F / 3F));
		registerMix(InkColors.RED, Map.of(MAGENTA, 1F / 2F, YELLOW, 1F / 2F));
		registerMix(InkColors.ORANGE, Map.of(MAGENTA, 1F / 3F, YELLOW, 2F / 3F));
		registerMix(InkColors.LIME, Map.of(CYAN, 1F / 3F, YELLOW, 2F / 3F));
		registerMix(InkColors.GREEN, Map.of(CYAN, 2F / 6F, MAGENTA, 1F / 6F, YELLOW, 3F / 6F));
		registerMix(InkColors.BROWN, Map.of(MAGENTA, 1F / 6F, YELLOW, 2F / 6F, BLACK, 3F / 6F));
		registerMix(InkColors.GRAY, Map.of(BLACK, 2F / 3F, WHITE, 1 / 3F));
		registerMix(InkColors.LIGHT_GRAY, Map.of(BLACK, 1F / 3F, WHITE, 2F / 3F));
	}
	
	public static Optional<Map<InkColor, Float>> getColorsToMix(InkColor color) {
		return Optional.ofNullable(MIXES.get(color));
	}
	
	public static boolean isMixedUsing(InkColor mixedColor, InkColor ingredientColor1, InkColor ingredientColor2) {
		Map<InkColor, Float> mix = MIXES.get(mixedColor);
		if (mix == null) {
			return false;
		}
		return mix.containsKey(ingredientColor1) && mix.containsKey(ingredientColor2);
	}
	
	public static InkColor getRandomMixedColor(InkColor color1, InkColor color2, Random random) {
		boolean color1Elemental = color1.isIn(InkColorTags.ELEMENTAL_COLORS);
		boolean color2Elemental = color2.isIn(InkColorTags.ELEMENTAL_COLORS);
		
		if (color1Elemental && color2Elemental) {
			List<InkColor> possibleOutcomes = new ArrayList<>();
			
			for (Map.Entry<InkColor, Map<InkColor, Float>> entries : MIXES.entrySet()) {
				InkColor color = entries.getKey();
				if (isMixedUsing(color, color1, color2)) {
					possibleOutcomes.add(color);
				}
			}
			
			if (!possibleOutcomes.isEmpty()) {
				Collections.shuffle(possibleOutcomes);
				return possibleOutcomes.get(0);
			}
			return color1;
		} else if (color1Elemental) {
			return color1;
		} else if (color2Elemental) {
			return color2;
		} else {
			return random.nextBoolean() ? color1 : color2;
		}
	}
	
	
}
