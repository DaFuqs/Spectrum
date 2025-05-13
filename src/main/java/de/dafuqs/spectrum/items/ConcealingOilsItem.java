package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.items.food.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.component.*;
import net.minecraft.component.type.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.*;
import net.minecraft.screen.slot.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class ConcealingOilsItem extends DrinkItem implements InkPoweredPotionFillable {
	
	public static final int POISONED_COLOUR = 0x3d1125;
	
	public ConcealingOilsItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (!InkPoweredPotionFillable.getEffects(stack).isEmpty())
			tooltip.add(Text.translatable("item.spectrum.concealing_oils.tooltip").styled(s -> s.withFormatting(Formatting.GRAY).withItalic(true)));
		appendPotionFillableTooltip(stack, tooltip, Text.translatable("item.spectrum.concealing_oils.when_poisoned"), true, context.getUpdateTickRate());
	}
	
	@Override
	public boolean onStackClicked(ItemStack oilsStack, Slot slot, ClickType clickType, PlayerEntity player) {
		if (clickType != ClickType.RIGHT)
			return false;
		
		var stackToApplyTo = slot.getStack();
		
		if (!stackToApplyTo.contains(DataComponentTypes.FOOD))
			return false;
		
		if (!isFull(oilsStack))
			return false;
		
		if (tryApplyOil(oilsStack, stackToApplyTo, player)) {
			if (!player.getAbilities().creativeMode)
				oilsStack.decrement(1);
			player.playSound(SoundEvents.ITEM_BOTTLE_EMPTY, 1, 1);
			return true;
		}
		
		return false;
	}
	
	private boolean tryApplyOil(ItemStack oilsStack, ItemStack foodStack, PlayerEntity user) {
		if (foodStack.getItem() instanceof DrinkItem)
			return false;
		if (foodStack.contains(SpectrumDataComponentTypes.CONCEALED_EFFECT))
			return false;
		
		var effect = InkPoweredPotionFillable.getEffects(oilsStack).getFirst();
		if (!InkPowered.tryDrainEnergy(user, effect.getInkCost().color(), effect.getInkCost().cost()))
			return false;
		
		var foodComponent = foodStack.get(DataComponentTypes.FOOD);
		if (foodComponent != null &&
				foodComponent
						.effects()
						.stream()
						.map(FoodComponent.StatusEffectEntry::effect)
						.anyMatch(e -> e.equals(effect.getStatusEffectInstance().getEffectType())))
			return false;
		
		foodStack.set(DataComponentTypes.PROFILE, new ProfileComponent(user.getGameProfile()));
		foodStack.set(SpectrumDataComponentTypes.CONCEALED_EFFECT, effect.getStatusEffectInstance());
		return true;
	}
	
	@Override
	public int maxEffectCount() {
		return 1;
	}
	
	@Override
	public int maxEffectAmplifier() {
		return 3;
	}
}
