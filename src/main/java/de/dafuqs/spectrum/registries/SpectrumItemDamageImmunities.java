package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;

public class SpectrumItemDamageImmunities {
	
	public static void registerDefaultItemStackImmunities() {
		ItemDamageImmunity.registerImmunity(Items.NETHER_STAR, DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(Items.NETHER_STAR, DamageTypeTags.IS_EXPLOSION);
		
		ItemDamageImmunity.registerImmunity(SpectrumBlocks.CRACKED_END_PORTAL_FRAME, DamageTypeTags.IS_EXPLOSION);
		ItemDamageImmunity.registerImmunity(SpectrumItems.DOOMBLOOM_SEED, DamageTypeTags.IS_EXPLOSION);
	}
	
}
