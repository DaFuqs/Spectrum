package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.CompoundColor;
import de.dafuqs.spectrum.energy.color.ElementalColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.dafuqs.spectrum.helpers.Support.getShortenedNumberString;

public class TotalCappedElementalInkStorage implements InkStorage {
	
	protected final long maxEnergyTotal;
	protected long currentTotal; // This is a cache for quick lookup. Can be recalculated anytime using the values in storedEnergy.
	protected final Map<ElementalColor, Long> storedEnergy = new HashMap<>();
	
	public TotalCappedElementalInkStorage(long maxEnergyTotal) {
		this.maxEnergyTotal = maxEnergyTotal;
		this.currentTotal = 0;

		for(ElementalColor color : InkColor.elementals()) {
			this.storedEnergy.put(color, 0L);
		}
	}
	
	public TotalCappedElementalInkStorage(long maxEnergyTotal, long cyan, long magenta, long yellow, long black, long white) {
		this.maxEnergyTotal = maxEnergyTotal;
		this.currentTotal = cyan+magenta+yellow+black+white;
		
		this.storedEnergy.put(InkColors.CYAN, cyan);
		this.storedEnergy.put(InkColors.MAGENTA, magenta);
		this.storedEnergy.put(InkColors.YELLOW, yellow);
		this.storedEnergy.put(InkColors.BLACK, black);
		this.storedEnergy.put(InkColors.WHITE, white);
	}
	
	@Override
	public boolean accepts(InkColor color) {
		return color instanceof ElementalColor;
	}
	
	@Override
	public long addEnergy(InkColor color, long amount) {
		if(color instanceof ElementalColor elementalColor) {
			long resultingAmount = this.storedEnergy.get(color) + amount;
			if(resultingAmount > this.maxEnergyTotal) {
				long overflow = resultingAmount - this.maxEnergyTotal + this.currentTotal;
				this.currentTotal = this.currentTotal + (resultingAmount - this.maxEnergyTotal);
				this.storedEnergy.put(elementalColor, this.maxEnergyTotal);
				return overflow;
			} else {
				this.currentTotal += amount;
				this.storedEnergy.put(elementalColor, resultingAmount);
				return 0;
			}
		}
		return amount;
	}
	
	@Override
	public boolean requestEnergy(InkColor color, long amount) {
		if(color instanceof ElementalColor elementalColor) {
			// can be output directly
			long storedAmount = this.storedEnergy.get(elementalColor);
			if(storedAmount < amount) {
				return false;
			} else {
				this.currentTotal -= amount;
				this.storedEnergy.put(elementalColor, storedAmount - amount);
				return true;
			}
		} else if(color instanceof CompoundColor compoundColor) {
			// mix!
			Map<ElementalColor, Float> requiredElementals = compoundColor.getElementalColorsToMix();
			
			// check if we have enough
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				long storedAmount = this.storedEnergy.get(entry.getKey());
				long requiredAmount = (int) Math.ceil(entry.getValue() * amount);
				if(storedAmount < requiredAmount) {
					return false;
				}
			}
			
			// yes, we got stored enough. Drain
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				long storedAmount = this.storedEnergy.get(entry.getKey());
				long requiredAmount = (int) Math.ceil(entry.getValue() * amount);
				this.currentTotal -= requiredAmount;
				this.storedEnergy.put(entry.getKey(), storedAmount - requiredAmount);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public long drainEnergy(InkColor color, long amount) {
		if(color instanceof ElementalColor elementalColor) {
			// can be output directly
			long storedAmount = this.storedEnergy.get(elementalColor);
			long drainedAmount = Math.min(storedAmount, amount);
			this.storedEnergy.put(elementalColor, storedAmount - drainedAmount);
			
			this.currentTotal -= drainedAmount;
			return drainedAmount;
		} else if(color instanceof CompoundColor compoundColor) {
			// mix!
			Map<ElementalColor, Float> requiredElementals = compoundColor.getElementalColorsToMix();
			
			// calculate the max amount that can be drained over all colors
			float percentageAbleToDrain = 1.0F;
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				long storedAmount = this.storedEnergy.get(entry.getKey());
				long requiredAmount = (int) Math.ceil(entry.getValue() * amount);
				if(storedAmount < requiredAmount) {
					percentageAbleToDrain = Math.min(percentageAbleToDrain, storedAmount / (float) requiredAmount);
				}
			}
			
			// drain
			for(Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				long storedAmount = this.storedEnergy.get(entry.getKey());
				long drainedAmount = (int) Math.ceil(entry.getValue() * amount * percentageAbleToDrain);
				this.storedEnergy.put(entry.getKey(), storedAmount - drainedAmount);
			}
			
			long drainedAmount = (int) Math.floor(percentageAbleToDrain * amount);
			this.currentTotal -= drainedAmount;
			return drainedAmount;
		}
		return 0;
	}
	
	@Override
	public long getEnergy(InkColor color) {
		if(color instanceof ElementalColor elementalColor) {
			// can be output directly
			return this.storedEnergy.get(elementalColor);
		} else if(color instanceof CompoundColor compoundColor) {
			// mix!
			long maxAmount = Long.MAX_VALUE;
			Map<ElementalColor, Float> requiredElementals = compoundColor.getElementalColorsToMix();
			for (Map.Entry<ElementalColor, Float> entry : requiredElementals.entrySet()) {
				long mixedAmount = (long) Math.floor(entry.getValue() * this.storedEnergy.get(entry.getKey()));
				maxAmount = Math.min(maxAmount, mixedAmount);
			}
			return maxAmount;
		}
		return 0;
	}
	
	@Override
	public long getMaxTotal() {
		return this.maxEnergyTotal;
	}
	
	@Override
	public long getMaxPerColor() {
		return this.maxEnergyTotal;
	}
	
	@Override
	public long getCurrentTotal() {
		return this.currentTotal;
	}
	
	@Override
	public boolean isEmpty() {
		return this.currentTotal == 0;
	}
	
	@Override
	public boolean isFull() {
		return this.currentTotal >= this.maxEnergyTotal;
	}
	
	public static @Nullable TotalCappedElementalInkStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			long cyan = compound.getLong("Cyan");
			long magenta = compound.getLong("Magenta");
			long yellow = compound.getLong("Yellow");
			long black = compound.getLong("Black");
			long white = compound.getLong("White");
			return new TotalCappedElementalInkStorage(maxEnergyTotal, cyan, magenta, yellow, black, white);
		}
		return null;
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyTotal", this.maxEnergyTotal);
		compound.putLong("Cyan", this.storedEnergy.get(InkColors.CYAN));
		compound.putLong("Magenta", this.storedEnergy.get(InkColors.MAGENTA));
		compound.putLong("Yellow", this.storedEnergy.get(InkColors.YELLOW));
		compound.putLong("Black", this.storedEnergy.get(InkColors.BLACK));
		compound.putLong("White", this.storedEnergy.get(InkColors.WHITE));
		return compound;
	}
	
	@Override
	public void fillCompletely() {
		long energyPerColor = this.maxEnergyTotal / this.storedEnergy.size();
		this.storedEnergy.replaceAll((c, v) -> energyPerColor);
	}
	
	@Environment(EnvType.CLIENT)
	public void addTooltip(World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("item.spectrum.artists_palette.tooltip", getShortenedNumberString(this.maxEnergyTotal)));
		tooltip.add(new TranslatableText("item.spectrum.artists_palette.tooltip.mix_on_demand"));
		
		long cyan = this.storedEnergy.get(InkColors.CYAN);
		if(cyan > 0) {
			tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.cyan", getShortenedNumberString(cyan)));
		}
		long magenta = this.storedEnergy.get(InkColors.MAGENTA);
		if(magenta > 0) {
			tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.magenta", getShortenedNumberString(magenta)));
		}
		long yellow = this.storedEnergy.get(InkColors.YELLOW);
		if(yellow > 0) {
			tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.yellow", getShortenedNumberString(yellow)));
		}
		long black = this.storedEnergy.get(InkColors.BLACK);
		if(black > 0) {
			tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.black", getShortenedNumberString(black)));
		}
		long white = this.storedEnergy.get(InkColors.WHITE);
		if(white > 0) {
			tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.white", getShortenedNumberString(white)));
		}
	}
	
}