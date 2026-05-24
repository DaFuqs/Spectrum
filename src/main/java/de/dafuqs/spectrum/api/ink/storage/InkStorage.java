package de.dafuqs.spectrum.api.ink.storage;

import de.dafuqs.spectrum.api.ink.color.*;
import net.minecraft.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.Support.*;

/**
 * Defines an object can store ink
 **/
public abstract class InkStorage implements Clearable {
	
	/**
	 * Transfer Ink from one storage to another
	 * Transfers Ink using a "pressure like" system: Tries to balance the ink in source and destination.
	 * The more energy is in source, the more is getting transferred, up to when both storages even out.
	 *
	 * @param source      The ink storage that is getting drawn from
	 * @param destination The ink storage receiving energy
	 * @return the total amount of energy that could be transferred
	 */
	public static long transferInk(@NotNull InkStorage source, @NotNull InkStorage destination) {
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
	public static long transferInk(@NotNull InkStorage source, @NotNull InkStorage destination, @NotNull InkColor color) {
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
	
	// if the storage is able to store this kind of color
	public abstract boolean accepts(InkColor color);
	
	public Collection<InkColor> acceptedColors() {
		List<InkColor> colors = new ArrayList<>();
		for(InkColor color : InkColors.all()) {
			if(accepts(color)) colors.add(color);
		}
		return colors;
	}
	
	// returns the amount of energy that could not be added
	public abstract long addEnergy(InkColor color, long amount);
	
	// Drains energy from the storage. Returns the amount of energy that could be drained
	// In contrast to requestEnergy this drains the energy up until 0, even if not requestedAmount of energy is stored
	public abstract long drainEnergy(InkColor color, long requestedAmount);
	
	// gets the amount of stored energy of that type
	public abstract long getEnergy(InkColor color);
	
	// gets all stored ink
	// only use for syncing server <=> clientside
	@Deprecated
	public abstract Map<InkColor, Long> getEnergy();
	
	// sets the amount of stored energy of that type
	// only use for syncing server <=> clientside
	@Deprecated
	public abstract void setEnergy(Map<InkColor, Long> colors, long total);
	
	// the amount of energy that can be stored per individual color
	public abstract long getMaxPerColor();
	
	// the amount of energy that can be stored in total
	public abstract long getMaxTotal();
	
	// the amount of energy that is currently stored
	public abstract long getCurrentTotal();
	
	// true if no energy is stored
	public abstract boolean isEmpty();
	
	// true if the max total is reached
	public abstract boolean isFull();
	
	// fill up the storage with as much energy as possible
	public abstract void fillCompletely();
	
	// List instead of appended Components, since line breaks do not work in the GUI consistently
	// WONTFIX as of https://bugs.mojang.com/browse/MC/issues/MC-269003
	public abstract List<Component> getTooltip();
	
	public Tooltip getWidgetTooltip() {
		MutableComponent mutableComponent = Component.literal("");
		List<Component> lines = getTooltip();
		for (int i = 0; i < lines.size(); i++) {
			mutableComponent.append(lines.get(i));
			if (i < lines.size() - 1) {
				mutableComponent.append(CommonComponents.NEW_LINE);
			}
		}
		return Tooltip.create(mutableComponent);
	}
	
	public abstract long getRoom(InkColor color);
	
	public static Component getInkStoreBulletTooltip(InkColor color, long amount) {
		MutableComponent inkName = color.getColoredInkName();
		return Component.translatable("spectrum.tooltip.ink_powered.bullet_amount", Component.literal(getShortenedNumberString(amount)).withStyle(ChatFormatting.WHITE), inkName).setStyle(inkName.getStyle());
	}
	
}
