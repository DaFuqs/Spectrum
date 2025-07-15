package de.dafuqs.spectrum.items.food;

import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DrinkItem extends Item {
	
	protected @Nullable Component tooltip;
	
	public DrinkItem(Properties settings) {
		super(settings);
	}
	
	public DrinkItem(Properties settings, String tooltip) {
		super(settings);
		this.tooltip = Component.translatable(tooltip).withStyle(ChatFormatting.GRAY);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		ItemStack itemStack = super.finishUsingItem(stack, world, user);
		
		if (user instanceof Player player) {
			if (!player.getAbilities().instabuild) {
				if (stack.isEmpty()) {
					return new ItemStack(Items.GLASS_BOTTLE);
				}
				player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		
		return itemStack;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}
	
	public SoundEvent getEatingSound() {
		return SoundEvents.GENERIC_DRINK;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		if (this.tooltip != null) {
			tooltip.add(this.tooltip);
		}
	}
	
}
