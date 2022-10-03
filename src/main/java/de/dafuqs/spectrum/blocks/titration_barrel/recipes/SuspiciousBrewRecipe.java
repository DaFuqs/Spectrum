package de.dafuqs.spectrum.blocks.titration_barrel.recipes;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class SuspiciousBrewRecipe extends TitrationBarrelRecipe {
	
	@Override
	public boolean isValidIngredient(DefaultedList<ItemStack> content, ItemStack itemStack) {
		return getStewEffectFrom(itemStack).isPresent();
	}
	
	private Optional<Pair<StatusEffect, Integer>> getStewEffectFrom(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			if (block instanceof FlowerBlock flowerBlock) {
				return Optional.of(Pair.of(flowerBlock.getEffectInStew(), flowerBlock.getEffectInStewDuration()));
			}
		}
		return Optional.empty();
	}
	
	@Override
	public ItemStack tap(DefaultedList<ItemStack> content, int waterBuckets, long secondsFermented, float downfall, float temperature) {
		int yield = getYieldBottles(waterBuckets, secondsFermented, temperature);
		ItemStack stack = getBrew(content, waterBuckets, secondsFermented, downfall);
		stack.setCount(yield);
		return stack;
	}
	
	// every real-life day the effect gets a potency of + 1, but duration get's a 25 % hit
	protected static ItemStack getBrew(DefaultedList<ItemStack> content, int waterBuckets, long ticksFermented, float downfall) {
		ItemStack stack = SpectrumItems.SUSPICIOUS_BREW.getDefaultStack();
		//TODO brew effects
		return stack;
	}
	
}
