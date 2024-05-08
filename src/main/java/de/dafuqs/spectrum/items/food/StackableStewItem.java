package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

public class StackableStewItem extends CustomUseTimeItem {

	public StackableStewItem(Item.Settings settings) {
		super(settings, 32);
	}

	public StackableStewItem(Item.Settings settings, int useTime) {
		super(settings, useTime);
	}

	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack returnStack = super.finishUsing(stack, world, user);

		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
			if (returnStack.isEmpty()) {
				return new ItemStack(Items.BOWL);
			}

			if (playerEntity != null) {
				playerEntity.getInventory().insertStack(new ItemStack(Items.BOWL));
			}
		}

		return returnStack;
	}

}
