package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ThreatConfluxItem extends BlockItem implements Preenchanted {
	
	public ThreatConfluxItem(Block block, Item.Properties properties) {
		super(block, properties);
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, List<Component> tooltip, @NotNull TooltipFlag type) {
		tooltip.add(Component.translatable("block.spectrum.threat_conflux.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.spectrum.threat_conflux.tooltip2").withStyle(ChatFormatting.GRAY).append(SpectrumItems.MIDNIGHT_CHIP.get().getDefaultInstance().getDisplayName()));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	@Override
	public int getEnchantmentValue(@NotNull ItemStack stack) {
		return 12;
	}
	
	@Override
	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return super.supportsEnchantment(stack, enchantment) || enchantment.is(SpectrumEnchantmentTags.ON_ARCANE_CHARGES);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.SHARPNESS, 1);
	}
	
}
