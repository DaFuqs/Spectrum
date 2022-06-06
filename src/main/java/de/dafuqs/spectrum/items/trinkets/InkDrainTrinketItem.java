package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.storage.FixedSingleInkDrain;
import de.dafuqs.spectrum.helpers.Support;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InkDrainTrinketItem extends SpectrumTrinketItem implements InkStorageItem<FixedSingleInkDrain> {
	
	public final InkColor inkColor;
	public final long maxInk;
	
	public InkDrainTrinketItem(Settings settings, Identifier unlockIdentifier, InkColor inkColor, long maxInk) {
		super(settings, unlockIdentifier);
		this.inkColor = inkColor;
		this.maxInk = maxInk;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		FixedSingleInkDrain inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());

		long nextStepInk;
		int pow = 0;
		do {
			nextStepInk = (long) (100 * Math.pow(8, pow));
			pow++;
		} while (storedInk > nextStepInk);
		
		if(nextStepInk == maxInk) {
			tooltip.add(new TranslatableText("spectrum.tooltip.ink_drain.tooltip.maxed_out").formatted(Formatting.GRAY));
		}else {
			tooltip.add(new TranslatableText("spectrum.tooltip.ink_drain.tooltip.ink_for_next_step." + inkStorage.getStoredColor().toString(), Support.getShortenedNumberString(nextStepInk - storedInk)).formatted(Formatting.GRAY));
		}
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		FixedSingleInkDrain inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		return storedInk == maxInk;
	}
	
	@Override
	public Rarity getRarity(ItemStack stack) {
		FixedSingleInkDrain inkStorage = getEnergyStorage(stack);
		long storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
		if(storedInk == maxInk) {
			return Rarity.EPIC;
		} else {
			return super.getRarity(stack);
		}
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultStack() {
		return super.getDefaultStack();
	}
	
	@Override
	public FixedSingleInkDrain getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null && compound.contains("EnergyStore")) {
			return FixedSingleInkDrain.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new FixedSingleInkDrain(maxInk, inkColor);
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, FixedSingleInkDrain storage) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.put("EnergyStore", storage.toNbt());
	}
	
	@Override
	public ItemStack getFullStack() {
		return InkStorageItem.super.getFullStack();
	}
	
}