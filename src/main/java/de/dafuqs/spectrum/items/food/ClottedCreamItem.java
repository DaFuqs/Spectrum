package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class ClottedCreamItem extends ItemWithTooltip {
	
	public ClottedCreamItem(Properties settings, String[] tooltips) {
		super(settings, tooltips);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (!world.isClientSide) {
			user.removeAllEffects();
		}
		
		return super.finishUsingItem(stack, world, user);
	}
	
}
