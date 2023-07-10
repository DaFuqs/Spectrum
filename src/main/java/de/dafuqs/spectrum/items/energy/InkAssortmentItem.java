package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.storage.*;
import net.fabricmc.api.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InkAssortmentItem extends Item implements InkStorageItem<IndividualCappedInkStorage> {
	
	private final long maxEnergy;
	
	public InkAssortmentItem(Settings settings, long maxEnergy) {
		super(settings);
		this.maxEnergy = maxEnergy;
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.ALWAYS;
	}
	
	@Override
	public IndividualCappedInkStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return IndividualCappedInkStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new IndividualCappedInkStorage(this.maxEnergy);
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, IndividualCappedInkStorage storage) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.put("EnergyStore", storage.toNbt());
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		getEnergyStorage(stack).addTooltip(tooltip, true);
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		if (this.isIn(group)) {
			stacks.add(getFullStack());
		}
	}
	
}