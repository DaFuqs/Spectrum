package de.dafuqs.spectrum.items.food;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class StackableStewItem extends Item {
	
	public StackableStewItem(Item.Properties settings) {
		super(settings);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		ItemStack returnStack = super.finishUsingItem(stack, world, user);
		
		Player playerEntity = user instanceof Player ? (Player) user : null;
		if (playerEntity == null || !playerEntity.getAbilities().instabuild) {
			if (returnStack.isEmpty()) {
				return new ItemStack(Items.BOWL);
			}
			
			if (playerEntity != null) {
				playerEntity.getInventory().add(new ItemStack(Items.BOWL));
			}
		}
		
		return returnStack;
	}
	
}
