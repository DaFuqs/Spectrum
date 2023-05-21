package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.compat.gofish.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.tag.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class SpectrumFishingRodItem extends FishingRodItem {
	
	public SpectrumFishingRodItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		
		PlayerEntityAccessor playerEntityAccessor = ((PlayerEntityAccessor) user);
		if (playerEntityAccessor.getSpectrumBobber() != null) {
			if (!world.isClient) {
				int damage = playerEntityAccessor.getSpectrumBobber().use(itemStack);
				itemStack.damage(damage, user, (p) -> p.sendToolBreakStatus(hand));
			}
			
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
		} else {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!world.isClient) {
				int luckOfTheSeaLevel = EnchantmentHelper.getLuckOfTheSea(itemStack);
				int lureLevel = EnchantmentHelper.getLure(itemStack);
				int exuberanceLevel = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.EXUBERANCE, itemStack, user);
				int bigCatchLevel = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.BIG_CATCH, itemStack, user);
				boolean inventoryInsertion = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.INVENTORY_INSERTION, itemStack, user) > 0;
				boolean foundry = shouldAutosmelt(itemStack, user);
				spawnBobber(user, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, inventoryInsertion, foundry);
			}
			
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
		}
		
		return TypedActionResult.success(itemStack, world.isClient());
	}
	
	public abstract void spawnBobber(PlayerEntity user, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, boolean inventoryInsertion, boolean foundry);
	
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.isIn(FluidTags.WATER);
	}
	
	public boolean shouldAutosmelt(ItemStack itemStack, PlayerEntity user) {
		return SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.FOUNDRY, itemStack, user) > 0 || GoFishCompat.hasDeepfry(itemStack);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.spectrum_fishing_rods.tooltip").formatted(Formatting.GRAY));
	}
	
}