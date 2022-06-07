package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.color.InkColor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This interface defines that an object can
 * store pigment energy and how much
 **/
public interface InkStorage {
	
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
				long transferAmount = Math.max(0, (sourceAmount - destinationAmount) / 32); // the constant here is simulating pressure flow
				transferAmount = Math.min(transferAmount, Math.min(sourceAmount, destinationRoom));
				if (transferAmount > 0) {
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
	
	// returns requestedAmount of energy. Returns the amount of energy that could be drained
	// In contrast to requestEnergy this drains the energy up until 0, even if not requestedAmount of energy is stored
	long drainEnergy(InkColor color, long requestedAmount);
	
	// returns true if the energy could be drained successfully
	// if not enough energy is stored, the amount of stored energy remains unchanged
	boolean requestEnergy(InkColor color, long requestedAmount);
	
	// gets the amount of stored energy of that type
	long getEnergy(InkColor color);
	
	// gets the amount of energy that can be stored per individual color
	long getMaxPerColor();
	
	// gets the amount of energy that can be stored in total
	long getMaxTotal();
	
	// gets the amount of energy that is currently stored
	long getCurrentTotal();
	
	// returns true if no energy is stored
	boolean isEmpty();
	
	// returns true if the max total is reached
	boolean isFull();
	
	// fill up the storage with as much energy as possible
	void fillCompletely();
	
	// returns true if all energy could be drained successfully
	// boolean requestEnergy(Map<CMYKColor, Integer> colors);
	
	// returns all stored energy with amounts
	//Map<ICMYKColor, Integer> getEnergy();
	
	void addTooltip(World world, List<Text> tooltip, TooltipContext context);
	
	long getRoom(InkColor color);
	
}