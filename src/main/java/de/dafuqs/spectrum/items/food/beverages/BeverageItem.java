package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.*;
import de.dafuqs.spectrum.items.food.beverages.properties.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.world.*;

import java.util.*;

public abstract class BeverageItem extends DrinkItem {
	
	public BeverageItem(Settings settings) {
		super(settings);
	}
	
	public abstract BeverageProperties getBeverageProperties(ItemStack itemStack);
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		getBeverageProperties(itemStack).addTooltip(itemStack, tooltip);
	}
	
	public static boolean isPreviewStack(ItemStack stack) {
		NbtCompound nbtCompound = stack.getNbt();
		return nbtCompound != null && nbtCompound.getBoolean("Preview");
	}
	
	public static void setPreviewStack(ItemStack stack) {
		NbtCompound compound = stack.getOrCreateNbt();
		compound.putBoolean("Preview", true);
		stack.setNbt(compound);
	}
	
}
