package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.ElementalColor;
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
import java.util.Locale;
import java.util.Map;

public class IndividualCappedSimplePigmentEnergyStorage implements PigmentEnergyStorage {
	
	protected final long maxEnergyPerColor;
	protected long currentTotal; // This is a cache for quick lookup. Can be recalculated anytime using the values in storedEnergy.
	protected final Map<CMYKColor, Long> storedEnergy;
	
	public IndividualCappedSimplePigmentEnergyStorage(long maxEnergyPerColor) {
		this.maxEnergyPerColor = maxEnergyPerColor;
		this.currentTotal = 0;
		
		this.storedEnergy = new HashMap<>();
		for(CMYKColor color : CMYKColor.all()) {
			this.storedEnergy.put(color, 0L);
		}
	}
	
	public IndividualCappedSimplePigmentEnergyStorage(long maxEnergyPerColor, Map<CMYKColor, Long> colors) {
		this.maxEnergyPerColor = maxEnergyPerColor;
		
		this.storedEnergy = colors;
		for(Map.Entry<CMYKColor, Long> color : colors.entrySet()) {
			this.storedEnergy.put(color.getKey(), color.getValue());
			this.currentTotal += color.getValue();
		}
	}
	
	@Override
	public boolean accepts(CMYKColor color) {
		return color instanceof ElementalColor;
	}
	
	@Override
	public long addEnergy(CMYKColor color, long amount) {
		long resultingAmount = this.storedEnergy.get(color) + amount;
		if(resultingAmount > this.maxEnergyPerColor) {
			long overflow = resultingAmount - this.maxEnergyPerColor;
			this.currentTotal = this.maxEnergyPerColor;
			this.storedEnergy.put(color, this.maxEnergyPerColor);
			return overflow;
		} else {
			this.currentTotal += amount;
			this.storedEnergy.put(color, resultingAmount);
			return 0;
		}
	}
	
	@Override
	public boolean requestEnergy(CMYKColor color, long amount) {
		long storedAmount = this.storedEnergy.get(color);
		if(storedAmount < amount) {
			return false;
		} else {
			this.currentTotal -= amount;
			this.storedEnergy.put(color, storedAmount - amount);
			return true;
		}
	}
	
	@Override
	public long drainEnergy(CMYKColor color, long amount) {
		long storedAmount = this.storedEnergy.get(color);
		long drainedAmount = Math.min(storedAmount, amount);
		this.storedEnergy.put(color, storedAmount - drainedAmount);
		this.currentTotal -= drainedAmount;
		return drainedAmount;
	}
	
	@Override
	public long getEnergy(CMYKColor color) {
		return this.storedEnergy.get(color);
	}
	
	@Override
	public long getMaxTotal() {
		return this.maxEnergyPerColor * this.storedEnergy.size();
	}
	
	@Override
	public long getMaxPerColor() {
		return this.maxEnergyPerColor;
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
		return this.currentTotal >= this.getMaxTotal();
	}
	
	public static @Nullable IndividualCappedSimplePigmentEnergyStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyPerColor", NbtElement.LONG_TYPE)) {
			long maxEnergyPerColor = compound.getLong("MaxEnergyPerColor");
			
			Map<CMYKColor, Long> colors = new HashMap<>();
			for(CMYKColor color : CMYKColor.all()) {
				colors.put(color, compound.getLong(color.toString()));
			}
			return new IndividualCappedSimplePigmentEnergyStorage(maxEnergyPerColor, colors);
		}
		return null;
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putLong("MaxEnergyPerColor", this.maxEnergyPerColor);
		for(Map.Entry<CMYKColor, Long> color : this.storedEnergy.entrySet()) {
			compound.putLong(color.getKey().toString(), color.getValue());
		}
		return compound;
	}
	
	@Environment(EnvType.CLIENT)
	public void addTooltip(World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip", maxEnergyPerColor));
		for(Map.Entry<CMYKColor, Long> color : this.storedEnergy.entrySet()) {
			if(color.getValue() > 0) {
				tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy." + color.getKey().toString().toLowerCase(Locale.ROOT), color.getValue()));
			}
		}
	}
	
	public void fillCompletely() {
		for(CMYKColor color : CMYKColor.all()) {
			storedEnergy.put(color, this.maxEnergyPerColor);
		}
	}
	
}