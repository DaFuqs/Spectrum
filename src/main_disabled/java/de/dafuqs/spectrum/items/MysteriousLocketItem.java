package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class MysteriousLocketItem extends Item {
	
	public MysteriousLocketItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (!world.isClientSide) {
			ItemStack handStack = user.getItemInHand(hand);
			if (handStack.has(SpectrumDataComponentTypes.SOCKETED)) {
				handStack.shrink(1);
				user.getInventory().placeItemBackInInventory(SpectrumItems.MYSTERIOUS_COMPASS.getDefaultInstance());
				world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.UNLOCK, SoundSource.NEUTRAL, 1.0F, 1.0F);
			}
		}
		return super.use(world, user, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.mysterious_locket.tooltip").withStyle(ChatFormatting.GRAY));
		if (stack.has(SpectrumDataComponentTypes.SOCKETED)) {
			tooltip.add(Component.translatable("item.spectrum.mysterious_locket.tooltip_socketed").withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(Component.translatable("item.spectrum.mysterious_locket.tooltip_empty").withStyle(ChatFormatting.GRAY));
		}
	}
	
}
