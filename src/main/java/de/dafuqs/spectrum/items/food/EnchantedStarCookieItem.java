package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.trinkets.WhispyCircletItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
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

public class EnchantedStarCookieItem extends Item {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder()
			.hunger(20).saturationModifier(0.25F)
			.statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 20), 1.0F)
			.alwaysEdible().snack().build();
	
	public EnchantedStarCookieItem(Settings settings) {
		super(settings);
	}
	
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		WhispyCircletItem.removeNegativeStatusEffects(user);
		return stack;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(new TranslatableText("item.spectrum.enchanted_star_cookie.tooltip").formatted(Formatting.GRAY));
	}
	
}
