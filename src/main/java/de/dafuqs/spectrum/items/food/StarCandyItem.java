package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import javax.annotation.*;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StarCandyItem extends Item {
	
	public StarCandyItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		ItemStack itemStack = super.finishUsingItem(stack, world, user);
		if (!world.isClientSide) {
			MobEffectHelper.clearRandomEffect(user, instance -> instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL);
		}
		return itemStack;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.star_candy.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
}
