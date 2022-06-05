package de.dafuqs.spectrum.items.magic_items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class EnderBagItem extends Item {
	
	public EnderBagItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		
		EnderChestInventory enderChestInventory = user.getEnderChestInventory();
		if (enderChestInventory != null) {
			user.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerx) -> {
				return GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderChestInventory);
			}, new TranslatableText("container.enderchest")));
			
			return TypedActionResult.consume(itemStack);
		} else {
			return TypedActionResult.success(itemStack, world.isClient);
		}
	}
	
	
}
