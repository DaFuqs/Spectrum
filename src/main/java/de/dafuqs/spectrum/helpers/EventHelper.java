package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.blocks.redstone.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;

public class EventHelper {
	
	public static DyeColor getRedstoneEventDyeColor(GameEvent.ListenerInfo message) {
		return message.context().affectedState().getOptionalValue(RedstoneTransceiverBlock.CHANNEL).orElse(null);
	}
	
	public static int getRedstoneEventPower(Level world, GameEvent.ListenerInfo message) {
		var pos = message.source();
		var blockEntity = world.getBlockEntity(BlockPos.containing(pos.x, pos.y, pos.z));
		if (blockEntity instanceof RedstoneTransceiverBlockEntity transceiver) {
			return transceiver.getCurrentSignalStrength();
		}
		return 0;
	}
	
}
