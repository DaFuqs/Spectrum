package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;

public class SpectrumItemDamageImmunities {

	public static void registerDefaultItemStackImmunities() {
		ItemDamageImmunity.registerImmunity(SpectrumBlocks.CRACKED_END_PORTAL_FRAME, DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(SpectrumBlocks.CRACKED_END_PORTAL_FRAME, DamageTypeTags.IS_EXPLOSION);

		ItemDamageImmunity.registerImmunity(Items.NETHER_STAR, DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(Items.NETHER_STAR, DamageTypeTags.IS_EXPLOSION);

		ItemDamageImmunity.registerImmunity(SpectrumBlocks.LAVA_SPONGE.asItem(), DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(SpectrumBlocks.WET_LAVA_SPONGE.asItem(), DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(SpectrumBlocks.DOOMBLOOM.asItem(), DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(SpectrumItems.DOOMBLOOM_SEED, DamageTypeTags.IS_FIRE);

		ItemDamageImmunity.registerImmunity(SpectrumItems.PURE_NETHERITE_SCRAP, DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(SpectrumBlocks.PURE_NETHERITE_SCRAP_BLOCK, DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(SpectrumBlocks.SMALL_NETHERITE_SCRAP_BUD, DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(SpectrumBlocks.LARGE_NETHERITE_SCRAP_BUD, DamageTypeTags.IS_FIRE);
		ItemDamageImmunity.registerImmunity(SpectrumBlocks.NETHERITE_SCRAP_CLUSTER, DamageTypeTags.IS_FIRE);
	}
	
}
