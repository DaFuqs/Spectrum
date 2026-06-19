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
		long total = 0;
		
		// collect targets that accept this color
		List<InkStorage> accepting = new ArrayList<>(targets.size());
		for (InkCapability s : targets) {
			InkStorage storage = s.getStorage();
			if (storage.accepts(color)) {
				accepting.add(storage);
				total += storage.getEnergy(color);
			}
		}
		
		if (accepting.isEmpty() || total == 0) return;
		
		long idealTarget = total / accepting.size();
		
		// drain targets above target value
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
		
		// refill targets below target
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
					long toAdd = Math.min(pooled, room);
					long added = -s.addEnergy(color, toAdd);
					pooled -= added;
				}
				if (pooled <= 0) break;
			}
		}
	}
}
