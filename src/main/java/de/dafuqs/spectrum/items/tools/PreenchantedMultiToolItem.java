package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PreenchantedMultiToolItem extends MultiToolItem implements Preenchanted, LoomPatternProvider  {
	
	public PreenchantedMultiToolItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.EFFICIENCY, 1);
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(getDefaultEnchantedStack(this));
		}
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.MULTITOOL;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}
