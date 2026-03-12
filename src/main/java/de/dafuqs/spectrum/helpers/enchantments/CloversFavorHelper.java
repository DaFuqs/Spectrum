package de.dafuqs.spectrum.helpers.enchantments;

import de.dafuqs.additionalentityattributes.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

public class CloversFavorHelper {
	
	public static float rollChance(float baseChance, @Nullable Entity entity) {
		if (entity instanceof Player player) {
			float rareLootLevel = (float) player.getAttributeValue(AdditionalEntityAttributes.BONUS_RARE_LOOT_ROLLS);
			if (rareLootLevel > 0) {
				return baseChance * rareLootLevel;
			}
		}
		return 0;
	}
	
}
