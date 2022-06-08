package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.items.conditional.CloakedItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

public class MoonstruckNectarItem extends CloakedItem {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder().hunger(2).saturationModifier(0.2F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 2), 1.0F).build();
	
	public MoonstruckNectarItem(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(new TranslatableText("item.spectrum.moonstruck_nectar.tooltip").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.moonstruck_nectar.tooltip2").formatted(Formatting.GRAY));
	}
	
}
