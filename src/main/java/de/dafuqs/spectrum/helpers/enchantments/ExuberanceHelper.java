package de.dafuqs.spectrum.helpers.enchantments;

import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.player.*;

public class ExuberanceHelper {
	
	public static float getExuberanceMod(Player player) {
		if (player != null) {
			RegistryAccess registryAccess = player.level().registryAccess();
			int exuberanceLevel = SpectrumEnchantmentHelper.getEquipmentLevel(registryAccess, SpectrumEnchantmentKeys.EXUBERANCE, player);
			return getExuberanceMod(exuberanceLevel);
		}
		return 1.0F;
	}
	
	public static float getExuberanceMod(int level) {
		return 1.0F + level * SpectrumConfig.CONFIG.ExuberanceBonusExperiencePercentPerLevel.get();
	}
	
}