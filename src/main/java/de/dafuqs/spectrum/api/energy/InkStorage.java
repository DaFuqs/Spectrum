package de.dafuqs.spectrum.api.energy;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.Support.*;

/**
 * This interface defines that an object can
 * store pigment energy and how much
 * TODO: migrate to capability
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
	 * Transfers Ink from storages with lots of Ink
	 * to storages with less.
	 * When called repeatedly (like every tick) the values of all storages will equalize,
	 * limited by their max storage.
	 * Don't forget to mark your stuff dirty, like with InkStorageBlockEntity.setInkDirty()!
	 * @param inkStorages The storages to run an equalization tick on
	 */
	static void equalizeInk(List<InkStorage> inkStorages) {
		SpectrumRegistries.INK_COLOR.stream()
				.forEach(inkColor -> equalizeInk(inkStorages, inkColor));
	}
	
	private static void equalizeInk(List<InkStorage> storages, InkColor color) {
		long total = 0;
		
		// collect storages that accept this color
		List<InkStorage> accepting = new ArrayList<>(storages.size());
		for (InkStorage s : storages) {
			if (s.accepts(color)) {
				accepting.add(s);
				total += s.getEnergy(color);
			}
		}
		
		if (accepting.isEmpty() || total == 0) return;
		
		long idealTarget = total / accepting.size();
		
		// drain storages above target value
		long pooled = 0;
		for (InkStorage s : accepting) {
			long current = s.getEnergy(color);
			long delta = idealTarget - current;
			long step = delta / 32;
			if (step == 0 && delta != 0) {
				step = delta > 0 ? 1 : -1;
			}
			
			long smoothedTarget = current + step;
			
			if (current > smoothedTarget) {
				long excess = current - smoothedTarget;
				pooled += s.drainEnergy(color, excess);
			}
		}
		
		if (pooled <= 0) return;
		
		// refill storages below target
		for (InkStorage s : accepting) {
			long current = s.getEnergy(color);
			long delta = idealTarget - current;
			long step = delta / 32;
			if (step == 0 && delta != 0) {
				step = delta > 0 ? 1 : -1;
			}
			long smoothedTarget = current + step;
			
			if (current < smoothedTarget && pooled > 0) {
				long needed = smoothedTarget - current;
				long added = needed - s.addEnergy(color, needed);
				pooled -= added;
			}
		}
		
		// If leftover pooled energy exists (due to capacity limits),
		// redistribute with pressure flow
		if (pooled > 0) {
			for (InkStorage s : accepting) {
				long room = s.getRoom(color);
				if (room > 0) {
					long added = room - s.addEnergy(color, room);
					pooled -= added;
				}
				if (pooled <= 0) break;
			}
		}
	}
	
	// if the storage is able to store this kind of color
	boolean accepts(InkColor color);
	
	// returns the amount of energy that could not be added
	long addEnergy(InkColor color, long amount);
	
	// Drains energy from the storage. Returns the amount of energy that could be drained
	// In contrast to requestEnergy this drains the energy up until 0, even if not requestedAmount of energy is stored
	long drainEnergy(InkColor color, long requestedAmount);
	
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
	void clearContent();
	
	void addTooltip(List<Component> tooltip);
	
	long getRoom(InkColor color);
	
	static void addInkStoreBulletTooltip(List<Component> tooltip, InkColor color, long amount) {
		MutableComponent inkName = color.getColoredInkName();
		tooltip.add(Component.translatable("spectrum.tooltip.ink_powered.bullet_amount", Component.literal(getShortenedNumberString(amount)).withStyle(ChatFormatting.WHITE), inkName).setStyle(inkName.getStyle()));
	}
	
}
