package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

public class ClottedCreamItem extends ItemWithTooltip {
	
	public ClottedCreamItem(Settings settings, String[] tooltips) {
		super(settings, tooltips);
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
