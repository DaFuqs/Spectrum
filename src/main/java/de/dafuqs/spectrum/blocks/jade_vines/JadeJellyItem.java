package de.dafuqs.spectrum.blocks.jade_vines;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class JadeJellyItem extends Item {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder().hunger(4).saturationModifier(0.75F).statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 2), 0.2F).snack().build();
	
	public JadeJellyItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(new TranslatableText("item.spectrum.jade_jelly.tooltip").formatted(Formatting.GRAY));
	}
	
}
