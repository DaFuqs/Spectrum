package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.inventories.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class BagOfHoldingItem extends Item {
	
	public BagOfHoldingItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		
		EnderChestInventory enderChestInventory = user.getEnderChestInventory();
		if (enderChestInventory != null) {
			user.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerx) -> new BagOfHoldingScreenHandler(syncId, playerx.getInventory(), playerx.getEnderChestInventory()), Text.translatable("container.enderchest")));
			
			return TypedActionResult.consume(itemStack);
		} else {
			return TypedActionResult.success(itemStack, world.isClient);
		}
	}
	
	
}
