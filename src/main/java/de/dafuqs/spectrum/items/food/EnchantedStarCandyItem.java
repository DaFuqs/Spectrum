package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.trinkets.WhispyCircletItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class EnchantedStarCandyItem extends Item {
	
	public EnchantedStarCandyItem(Settings settings) {
		super(settings);
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack itemStack = super.finishUsing(stack, world, user);
		
		user.heal(user.getMaxHealth());
		if (!world.isClient) {
			WhispyCircletItem.removeNegativeStatusEffects(user);
		}
		if (user instanceof PlayerEntity player) {
			player.getHungerManager().add(1000, 1.0F);
		}
		return itemStack;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(Text.translatable("item.spectrum.purple_star_candy.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.purple_star_candy.tooltip2").formatted(Formatting.GRAY));
	}
	
}
