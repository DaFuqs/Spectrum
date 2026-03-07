package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.items.food.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.component.*;
import org.spongepowered.asm.mixin.*;

import java.util.*;
import java.util.function.*;

public class ConcealingOilsItem extends DrinkItem implements InkPoweredPotionFillable {
	
	public static final int POISONED_COLOUR = 0x3d1125;
	
	public ConcealingOilsItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		if (!InkPoweredPotionFillable.getEffects(stack).isEmpty()) {
			tooltip.add(Component.translatable("item.spectrum.concealing_oils.tooltip").withStyle(s -> s.applyFormat(ChatFormatting.GRAY).withItalic(true)));
		}
		appendPotionFillableTooltip(stack, tooltip, Component.translatable("item.spectrum.concealing_oils.when_poisoned"), true, context.tickRate());
	}
	
	@Override
	public int maxEffectCount() {
		return 1;
	}
	
	@Override
	public int maxEffectAmplifier() {
		return 3;
	}
	
	@Override
	public boolean overrideStackedOnOther(ItemStack oilsStack, Slot slot, ClickAction clickType, Player player) {
		if (clickType != ClickAction.SECONDARY)
			return false;
		if (!isFull(oilsStack))
			return false;
		
		ItemStack stackToApplyTo = slot.getItem();
		if (!stackToApplyTo.has(DataComponents.FOOD))
			return false;
		if (stackToApplyTo.has(SpectrumDataComponentTypes.CONCEALED_EFFECT)) {
			return false;
		}
		
		InkPoweredStatusEffectInstance effect = InkPoweredPotionFillable.getEffects(oilsStack).getFirst();
		if (InkPowered.tryDrainEnergy(player, effect.getInkCost().color(), effect.getInkCost().cost())) {
			stackToApplyTo.set(SpectrumDataComponentTypes.CONCEALED_EFFECT, effect.getStatusEffectInstance());
			stackToApplyTo.set(SpectrumDataComponentTypes.CONCEALED_EFFECT_PROFILE, new ResolvableProfile(player.getGameProfile()));
			
			if (!player.getAbilities().instabuild) {
				oilsStack.shrink(1);
			}
			player.playSound(SoundEvents.BOTTLE_EMPTY, 1, 1);
			return true;
		}
		
		return false;
	}
	
	public static void addConcealedEffectsTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> tooltipAdder, Player player) {
		MobEffectInstance oilEffect = stack.get(SpectrumDataComponentTypes.CONCEALED_EFFECT);
		if (oilEffect == null) {
			return;
		}
		
		ResolvableProfile profile = stack.get(SpectrumDataComponentTypes.CONCEALED_EFFECT_PROFILE);
		if (profile != null && player.getUUID().equals(profile.id().orElse(null))) {
			List<Component> subText = new ArrayList<>();
			PotionContents.addPotionTooltip(List.of(oilEffect), subText::add, 1f, context.tickRate());
			
			tooltipAdder.accept(Component.translatable("info.spectrum.tooltip.adulterated.info").withStyle(s -> s.withColor(ConcealingOilsItem.POISONED_COLOUR)));
			tooltipAdder.accept(Component.translatable("info.spectrum.tooltip.adulterated.effect", subText.getFirst()).withStyle(s -> s.withColor(ConcealingOilsItem.POISONED_COLOUR).withItalic(true)));
		}
	}
	
}
