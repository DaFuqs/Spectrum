package de.dafuqs.spectrum.events.listeners;

import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public class ItemEntityEventQueue extends EventQueue<ItemEntityEventQueue.Entry> {
	
	public record Entry(Holder<GameEvent> event, ItemEntity itemEntity, int distance) { }
	
	public ItemEntityEventQueue(PositionSource positionSource, int range, Callback<Entry> listener) {
		super(positionSource, range, listener);
	}
	
	@Override
	public void acceptEvent(Level world, GameEvent.ListenerInfo event, Vec3 sourcePos) {
		if (world instanceof ServerLevel && event.context().sourceEntity() instanceof ItemEntity itemEntity) {
			Vec3 pos = event.source();
			Entry entry = new Entry(event.gameEvent(), itemEntity, Mth.floor(pos.distanceTo(sourcePos)));
			int delay = entry.distance * 2;
			this.schedule(entry, delay);
			TypedTransmissionPayload.playTransmissionParticle((ServerLevel) world, new TypedTransmission(pos, this.positionSource, delay, TypedTransmission.Variant.ITEM));
		}
	}
	
}