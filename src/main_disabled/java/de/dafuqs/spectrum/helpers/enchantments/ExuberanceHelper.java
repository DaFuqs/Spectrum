package de.dafuqs.spectrum.helpers.enchantments;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.player.*;

public class ExuberanceHelper {
	
	public static float getExuberanceMod(Player breakingPlayer) {
		if (breakingPlayer != null) {
			var drm = breakingPlayer.level().registryAccess();
			int exuberanceLevel = SpectrumEnchantmentHelper.getEquipmentLevel(drm, SpectrumEnchantments.EXUBERANCE, breakingPlayer);
			return getExuberanceMod(exuberanceLevel);
		} else {
			return 1.0F;
		}
	}
	
	public static float getExuberanceMod(int level) {
		return 1.0F + level * SpectrumCommon.CONFIG.ExuberanceBonusExperiencePercentPerLevel;
	}
	
}