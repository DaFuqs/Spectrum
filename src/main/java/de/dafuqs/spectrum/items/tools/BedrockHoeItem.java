package de.dafuqs.spectrum.items.tools;

import com.mojang.datafixers.util.Pair;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;

import java.util.*;

public class BedrockHoeItem extends HoeItem implements Preenchanted {
	
	public BedrockHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.FORTUNE, 4);
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	static {
		TILLING_ACTIONS.put(SpectrumBlocks.SLUSH, Pair.of(HoeItem::canTillFarmland, createTillAction(SpectrumBlocks.TILLED_SLUSH.getDefaultState())));
		TILLING_ACTIONS.put(SpectrumBlocks.SHALE_CLAY, Pair.of(HoeItem::canTillFarmland, createTillAction(SpectrumBlocks.TILLED_SHALE_CLAY.getDefaultState())));
	}
}