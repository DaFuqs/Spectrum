package de.dafuqs.spectrum.api.ink;

import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;

import javax.annotation.*;
import java.util.*;

public class InkTransferHelper {
	
	public static long transferInkOneWay(InkCapability source, InkCapability destination, @Nullable InkColor color, @Nullable Player advancementPlayer, ItemStack advancementStack) {
		long amount = 0;
		if (color != null) {
			amount = InkStorage.transferInk(source.getStorage(), destination.getStorage(), color);
		} else {
			for (InkColor c : InkColors.all()) {
				amount += InkStorage.transferInk(source.getStorage(), destination.getStorage(), c);
			}
		}
		
		if (amount > 0) {
			source.markDirty();
			destination.markDirty();
			
			if (advancementPlayer instanceof ServerPlayer serverPlayer) {
				SpectrumAdvancementCriteria.INK_CONTAINER_INTERACTION.trigger(serverPlayer, advancementStack, source.getStorage(), color, amount);
			}
		}
		return amount;
	}
	
	/**
	 * Transfers Ink from storages with lots of Ink
	 * to storages with less.
	 * When called repeatedly (like every tick) the values of all storages will equalize,
	 * limited by their max storage.
	 * Don't forget to mark your stuff dirty, like with InkStorageBlockEntity.setInkDirty()!
	 * @param targets The storages to run an equalization tick on
	 */
	public static void equalizeInk(List<InkCapability> targets) {
		SpectrumRegistries.INK_COLOR.stream().forEach(inkColor -> equalizeInk(targets, inkColor));
		for(InkCapability inkStorage : targets) {
			inkStorage.markDirty();
		}
	}
	
	private static void equalizeInk(List<InkCapability> targets, InkColor color) {
		final double PRESSURE_FACTOR = 0.02;
		
		List<InkStorage> storages = new ArrayList<>();
		for (InkCapability cap : targets) {
			InkStorage storage = cap.getStorage();
			if (storage.accepts(color)) {
				storages.add(storage);
			}
		}
		
		if (storages.isEmpty()) {
			return;
		}
		
		// Compute average
		long total = 0;
		for (InkStorage s : storages) {
			total += s.getEnergy(color);
		}
		double avg = (double) total / storages.size();
		
		for (InkStorage s : storages) {
			long current = s.getEnergy(color);
			double diff = avg - current;
			
			if (diff > 0) { // Needs ink
				long remainingNeed = (long) Math.ceil(diff * PRESSURE_FACTOR);
				
				for (InkStorage other : storages) {
					if (other == s) continue;
					
					long available = other.getEnergy(color);
					if (available <= 0) continue;
					
					long room = s.getRoom(color);
					if (room <= 0) break;
					
					long toTransfer = Math.min(Math.min(available, remainingNeed), room);
					
					if (toTransfer > 0) {
						long drained = other.drainEnergy(color, toTransfer);
						long leftover = s.addEnergy(color, drained);
						
						if (leftover != 0) {
							other.addEnergy(color, leftover);
						}
						
						remainingNeed -= drained;
						if (remainingNeed <= 0) break;
					}
				}
				
			} else if (diff < 0) { // Has excess
				long remainingExcess = (long) Math.ceil(-diff * PRESSURE_FACTOR);
				
				for (InkStorage other : storages) {
					if (other == s) continue;
					
					long room = other.getRoom(color);
					if (room <= 0) continue;
					
					long toTransfer = Math.min(room, remainingExcess);
					
					if (toTransfer > 0) {
						long drained = s.drainEnergy(color, toTransfer);
						long leftover = other.addEnergy(color, drained);
						
						if (leftover != 0) {
							s.addEnergy(color, leftover);
						}
						
						remainingExcess -= drained;
						if (remainingExcess <= 0) break;
					}
				}
			}
		}
		
		for (InkCapability cap : targets) {
			cap.markDirty();
		}
	}
	
}
