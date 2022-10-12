package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.trinkets.WhispyCircletItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class StarCookieItem extends Item {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder()
			.hunger(3).saturationModifier(0.25F)
			.alwaysEdible().snack().build();
	
	public StarCookieItem(Settings settings) {
		super(settings);
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack itemStack = super.finishUsing(stack, world, user);
		WhispyCircletItem.removeSingleHarmfulStatusEffects(user, world);
		return itemStack;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(new TranslatableText("item.spectrum.star_cookie.tooltip").formatted(Formatting.GRAY));
	}
	
}
