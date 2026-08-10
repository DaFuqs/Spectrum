package de.dafuqs.spectrum.api.ink;

import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;

import javax.annotation.*;
import java.util.*;

public class InkTransferHelper {
	
	/**
	 * Transfers Ink from storages with lots of Ink
	 * to storages with less.
	 * When called repeatedly (like every tick) the values of all storages will equalize,
	 * limited by their max storage.
	 * Don't forget to mark your stuff dirty, like with InkStorageBlockEntity.setInkDirty()!
	 * @param capabilities The InkCapabilities to run an equalization tick on
	 */
	public static void equalizeInk(List<InkCapability> capabilities) {
		SpectrumRegistries.INK_COLOR.stream().forEach(inkColor -> equalizeInk(capabilities, inkColor));
	}
	
	public static void equalizeInk(InkCapability capability1, InkCapability capability2, @Nullable InkColor color) {
		if(color == null) {
			equalizeInk(capability1, capability2);
		} else {
			equalizeInk(List.of(capability1, capability2), color);
		}
	}
	
	public static void equalizeInk(InkCapability capability1, InkCapability capability2) {
		SpectrumRegistries.INK_COLOR.stream().forEach(inkColor -> equalizeInk(List.of(capability1, capability2), inkColor));
	}
	
	private static void equalizeInk(List<InkCapability> targets, InkColor color) {
		final double PRESSURE_FACTOR = 0.02;
		
		Map<InkStorage, InkCapability> map = new Object2ObjectArrayMap<>();
		Set<InkCapability> dirtyCapabilities = new ObjectArraySet<>();
		for (InkCapability cap : targets) {
			InkStorage storage = cap.getStorage();
			if (storage.accepts(color)) {
				map.put(storage, cap);
			}
		}
		
		if (map.isEmpty()) {
			return;
		}
		
		// Compute average
		long total = 0;
		for (InkStorage s : map.keySet()) {
			total += s.getEnergy(color);
		}
		double avg = (double) total / map.size();
		
		for (Map.Entry<InkStorage, InkCapability> source : map.entrySet()) {
			InkCapability sourceCapability = source.getValue();
			InkStorage sourceStorage = source.getKey();
			long current = sourceStorage.getEnergy(color);
			double diff = avg - current;
			
			if (diff > 0) { // Needs ink
				if (!sourceCapability.canFill()) continue;
				
				long remainingNeed = (long) Math.ceil(diff * PRESSURE_FACTOR);
				
				for (Map.Entry<InkStorage, InkCapability> other : map.entrySet()) {
					if (other == sourceStorage) continue;
					InkCapability otherCapability = other.getValue();
					if (!otherCapability.canDrain(false)) continue;
					
					InkStorage otherStorage = other.getKey();
					long available = otherStorage.getEnergy(color);
					if (available <= 0) continue;
					
					long room = sourceStorage.getRoom(color);
					if (room <= 0) break;
					
					long toTransfer = Math.min(Math.min(available, remainingNeed), room);
					if (toTransfer > 0) {
						long drained = otherStorage.drainEnergy(color, toTransfer);
						sourceStorage.addEnergy(color, drained);
						dirtyCapabilities.add(otherCapability);
						dirtyCapabilities.add(sourceCapability);
						
						remainingNeed -= drained;
						if (remainingNeed <= 0) break;
					}
				}
				
			} else if (diff < 0) { // Has excess
				if (!sourceCapability.canDrain(false)) continue;
				
				long remainingExcess = (long) Math.ceil(-diff * PRESSURE_FACTOR);
				
				for (Map.Entry<InkStorage, InkCapability> destination : map.entrySet()) {
					if (destination == sourceStorage) continue;
					InkCapability destinationCapability = destination.getValue();
					if (!destinationCapability.canFill()) continue;
					
					InkStorage destinationStorage = destination.getKey();
					long room = destinationStorage.getRoom(color);
					if (room <= 0) continue;
					
					long toTransfer = Math.min(room, remainingExcess);
					if (toTransfer > 0) {
						long drained = sourceStorage.drainEnergy(color, toTransfer);
						destinationStorage.addEnergy(color, drained);
						dirtyCapabilities.add(destinationCapability);
						dirtyCapabilities.add(sourceCapability);
						
						remainingExcess -= drained;
						if (remainingExcess <= 0) break;
					}
				}
			}
		}
		
		for (InkCapability cap : dirtyCapabilities) {
			cap.markDirty();
		}
	}
	
}
