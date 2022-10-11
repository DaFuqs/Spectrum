package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class StarCandyItem extends Item {
	
	public static final float ENCHANTED_STAR_CANDY_CHANCE = 0.5F;
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.25F)
			.alwaysEdible().snack().build();
	
	public StarCandyItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player) {
		super.onCraft(stack, world, player);
		if(world.random.nextFloat() < ENCHANTED_STAR_CANDY_CHANCE) {
			int count = stack.getCount();
			stack.setCount(0);
			
			ItemStack enchantedStack = SpectrumItems.ENCHANTED_STAR_CANDY.getDefaultStack();
			enchantedStack.setCount(count);
			player.giveItemStack(enchantedStack);
		}
	}
	
}
