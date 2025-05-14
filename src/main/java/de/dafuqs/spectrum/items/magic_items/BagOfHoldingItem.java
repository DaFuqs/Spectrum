package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.inventories.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class BagOfHoldingItem extends Item {
	
	public BagOfHoldingItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		
		PlayerEnderChestContainer enderChestInventory = user.getEnderChestInventory();
		if (enderChestInventory != null) {
			user.openMenu(new SimpleMenuProvider((syncId, inventory, playerx) -> new BagOfHoldingScreenHandler(syncId, playerx.getInventory(), playerx.getEnderChestInventory()), Component.translatable("container.enderchest")));
			
			return InteractionResultHolder.consume(itemStack);
		} else {
			return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide);
		}
	}
	
	
}
