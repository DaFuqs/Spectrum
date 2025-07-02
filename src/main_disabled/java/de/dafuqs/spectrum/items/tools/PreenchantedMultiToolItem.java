package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public class PreenchantedMultiToolItem extends MultiToolItem implements Preenchanted, LoomPatternProvider {
	
	public PreenchantedMultiToolItem(Tier material, int attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.EFFICIENCY, 1);
	}
	
	@Override
	public ResourceKey<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.MULTITOOL;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		addBannerPatternProviderTooltip(tooltip);
	}
	
}
