package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.trinkets.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class EnchantedStarCandyItem extends Item {
	
	public EnchantedStarCandyItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		ItemStack itemStack = super.finishUsingItem(stack, world, user);
		
		user.heal(user.getMaxHealth());
		if (!world.isClientSide) {
			WhispyCircletItem.removeNegativeStatusEffects(user);
		}
		if (user instanceof Player player) {
			player.getFoodData().eat(1000, 1.0F);
		}
		return itemStack;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.enchanted_star_candy.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.enchanted_star_candy.tooltip2").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
	
}
