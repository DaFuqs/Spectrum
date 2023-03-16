package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.storage.*;
import de.dafuqs.spectrum.items.*;
import net.fabricmc.api.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CreativeInkAssortmentItem extends Item implements InkStorageItem<CreativeInkStorage>, CreativeOnlyItem {
	
	public CreativeInkAssortmentItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(context.getBlockPos());
			if (blockEntity instanceof InkStorageBlockEntity inkStorageBlockEntity) {
				inkStorageBlockEntity.getEnergyStorage().fillCompletely();
				inkStorageBlockEntity.setInkDirty();
				blockEntity.markDirty();
			}
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.ALWAYS;
	}
	
	@Override
	public CreativeInkStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return CreativeInkStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new CreativeInkStorage();
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, CreativeInkStorage storage) {
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		CreativeOnlyItem.appendTooltip(tooltip);
		getEnergyStorage(stack).addTooltip(tooltip, true);
	}
	
}