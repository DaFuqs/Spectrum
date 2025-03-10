package de.dafuqs.spectrum.api.energy;

import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.Support.*;

/**
 * This interface defines that an object can
 * store pigment energy and how much
 **/
public interface InkStorage extends Clearable {
	
	/**
	 * Transfer Ink from one storage to another
	 * Transfers Ink using a "pressure like" system: Tries to balance the ink in source and destination.
	 * The more energy is in source, the more is getting transferred, up to when both storages even out.
	 *
	 * @param source      The ink storage that is getting drawn from
	 * @param destination The ink storage receiving energy
	 * @return the total amount of energy that could be transferred
	 */
	static long transferInk(@NotNull InkStorage source, @NotNull InkStorage destination) {
		long transferred = 0;
		for (InkColor inkColor : source.getEnergy().keySet()) {
			transferred += transferInk(source, destination, inkColor);
		}
		return transferred;
	}
	
	/**
	 * Transfer Ink from one storage to another
	 * Transfers Ink using a "pressure like" system: Tries to balance the ink in source and destination.
	 * The more energy is in source, the more is getting transferred, up to when both storages even out.
	 *
	 * @param source      The ink storage that is getting drawn from
	 * @param destination The ink storage receiving energy
	 * @param color       The ink type to transfer
	 * @return the amount of energy that could be transferred
	 */
	static long transferInk(@NotNull InkStorage source, @NotNull InkStorage destination, @NotNull InkColor color) {
		if (!destination.accepts(color)) {
			return 0;
		}
		
		long sourceAmount = source.getEnergy(color);
		if (sourceAmount > 0) {
			long destinationRoom = destination.getRoom(color);
			if (destinationRoom > 0) {
				long destinationAmount = destination.getEnergy(color);
				if (sourceAmount > destinationAmount + 1) {
					long transferAmount = Math.max(1, (sourceAmount - destinationAmount) / 32); // the constant here is simulating pressure flow
					transferAmount = Math.min(transferAmount, Math.min(sourceAmount, destinationRoom));
					destination.addEnergy(color, transferAmount);
					source.drainEnergy(color, transferAmount);
					return transferAmount;
				}
			}
		}
		return 0;
	}
	
	/**
	 * Transfer Ink from one storage to another
	 * Transfers a fixed amount of energy
	 * => Use the pressure like system without fixed amount, where possible
	 *
	 * @param source      The ink storage that is getting drawn from
	 * @param destination The ink storage receiving energy
	 * @param color       The ink type to transfer
	 * @param amount      The fixed amount of ink to transfer
	 * @return the amount of energy that could be transferred
	 */
	@Deprecated
	static long transferInk(@NotNull InkStorage source, @NotNull InkStorage destination, @NotNull InkColor color, long amount) {
		if (!destination.accepts(color)) {
			return 0;
		}
		
		long sourceAmount = source.getEnergy(color);
		if (sourceAmount > 0) {
			long destinationRoom = destination.getRoom(color);
			if (destinationRoom > 0) {
				long transferAmount = Math.min(amount, Math.min(sourceAmount, destinationRoom));
				if (transferAmount > 0) {
					destination.addEnergy(color, transferAmount);
					source.drainEnergy(color, transferAmount);
					return transferAmount;
				}
			}
		}
		return 0;
	}
	
	// if the storage is able to store this kind of color
	boolean accepts(InkColor color);
	
	// returns the amount of energy that could not be added
	long addEnergy(InkColor color, long amount);
	
	// Drains energy from the storage. Returns the amount of energy that could be drained
	// In contrast to requestEnergy this drains the energy up until 0, even if not requestedAmount of energy is stored
	long drainEnergy(InkColor color, long requestedAmount);
	
	// returns true if the energy could be drained successfully
	// if not enough energy is stored, the amount of stored energy remains unchanged
	boolean requestEnergy(InkColor color, long requestedAmount);
	
	// gets the amount of stored energy of that type
	long getEnergy(InkColor color);
	
	// gets all stored ink
	// only use for syncing server <=> clientside
	@Deprecated
	Map<InkColor, Long> getEnergy();
	
	// sets the amount of stored energy of that type
	// only use for syncing server <=> clientside
	@Deprecated
	void setEnergy(Map<InkColor, Long> colors, long total);
	
	// the amount of energy that can be stored per individual color
	long getMaxPerColor();
	
	// the amount of energy that can be stored in total
	long getMaxTotal();
	
	// the amount of energy that is currently stored
	long getCurrentTotal();
	
	// true if no energy is stored
	boolean isEmpty();
	
	// true if the max total is reached
	boolean isFull();
	
	// fill up the storage with as much energy as possible
	void fillCompletely();
	
	// completely empty the storage
	void clear();
	
	// Returns how full the storage is, as a float
	default float getFillPercent(InkColor color) {
		return MathHelper.clamp((float) getEnergy(color) / getMaxPerColor(), 0, 1);
	}
	
	// Same as above, but in total.
	default float getTotalFillPercent() {
		return MathHelper.clamp((float) getCurrentTotal() / getMaxTotal(), 0, 1);
	}
	
	void addTooltip(List<Text> tooltip);
	
	long getRoom(InkColor color);
	
	static @NotNull Map<InkColor, Long> readEnergy(NbtCompound compound) {
		Map<InkColor, Long> energy = new HashMap<>();
		if (compound != null) {
			for (String key : compound.getKeys()) {
				Optional<InkColor> color = InkColor.ofIdString(key);
				if (color.isPresent()) {
					long amount = compound.getLong(key);
					energy.put(color.get(), amount);
				}
			}
		}
		return energy;
	}
	
	static @NotNull NbtCompound writeEnergy(Map<InkColor, Long> storedEnergy) {
		NbtCompound energy = new NbtCompound();
		for (Map.Entry<InkColor, Long> color : storedEnergy.entrySet()) {
			energy.putLong(color.getKey().getID().toString(), color.getValue());
		}
		return energy;
	}
	
	static void addInkStoreBulletTooltip(List<Text> tooltip, InkColor color, long amount) {
		MutableText inkName = color.getColoredInkName();
		tooltip.add(Text.translatable("spectrum.tooltip.ink_powered.bullet_amount", Text.literal(getShortenedNumberString(amount)).formatted(Formatting.WHITE), inkName).setStyle(inkName.getStyle()));
	}
	
}