package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import javax.annotation.*;

import java.util.*;

public class DrinkItem extends Item {
	
	protected @Nullable Component tooltip;
	
	public DrinkItem(Properties settings) {
		super(settings);
	}
	
	public DrinkItem(Properties settings, String tooltip) {
		this(settings, Component.translatable(tooltip));
	}
	
	public DrinkItem(Properties settings, MutableComponent component) {
		super(settings);
		this.tooltip = component.withStyle(ChatFormatting.GRAY);
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
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		user.startUsingItem(hand);
		return InteractionResultHolder.consume(user.getItemInHand(hand));
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
		
		WithMilkComponent withMilk = stack.get(SpectrumDataComponentTypes.WITH_MILK);
		if (withMilk != null) {
			withMilk.addToTooltip(context, tooltip::add, type);
		}
	}
	
}
