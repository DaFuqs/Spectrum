package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.ItemWithTooltip;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ClottedCreamItem extends ItemWithTooltip {
	
	public ClottedCreamItem(Settings settings, String tooltip) {
		super(settings, tooltip);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient) {
			user.clearStatusEffects();
		}
		
		return super.finishUsing(stack, world, user);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 52;
	}
}
