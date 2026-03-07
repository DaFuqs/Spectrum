package de.dafuqs.spectrum.components;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;

/**
 * Making Items immune to certain forms of damage
 */
public record DamageImmuneComponent(List<TagKey<DamageType>> damageTags) {
	
	public static final Codec<DamageImmuneComponent> CODEC = TagKey.codec(Registries.DAMAGE_TYPE).listOf().xmap(DamageImmuneComponent::new, DamageImmuneComponent::damageTags);
	public static final DamageImmuneComponent DEFAULT = new DamageImmuneComponent(List.of());
	
	public DamageImmuneComponent(TagKey<DamageType>... damageTags) {
		this(List.of(damageTags));
	}
	
	public static boolean isImmuneTo(ItemStack stack, DamageSource damageSource) {
		// otherwise items would fall endlessly when falling into the end, causing lag
		if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {
			return false;
		}
		
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.PREVENTS_ITEM_DAMAGE)) {
			return true;
		}

		// is item immune to this specific kind of damage?
		DamageImmuneComponent immunities = stack.get(SpectrumDataComponentTypes.DAMAGE_IMMUNE);
		if(immunities == null) {
			return false;
		}
		
		for(TagKey<DamageType> immunity : immunities.damageTags()) {
			if(damageSource.is(immunity)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isImmuneTo(ItemStack stack, TagKey<DamageType> damageTypeTag) {
		DamageImmuneComponent immunities = stack.get(SpectrumDataComponentTypes.DAMAGE_IMMUNE);
		return immunities != null && immunities.damageTags().contains(damageTypeTag);
	}
	
}
