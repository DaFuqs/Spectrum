package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EnchantedStarCandyItem extends Item {
	
	public static final Predicate<MobEffectInstance> EFFECT_CLEAR_PREDICATE = instance -> instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL;
	
	public EnchantedStarCandyItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		ItemStack itemStack = super.finishUsingItem(stack, world, user);
		
		user.heal(user.getMaxHealth());
		if (!world.isClientSide) {
			MobEffectHelper.clearEffects(user, EFFECT_CLEAR_PREDICATE);
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
