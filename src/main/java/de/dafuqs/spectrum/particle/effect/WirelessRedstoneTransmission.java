package de.dafuqs.spectrum.particle.effect;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;

public class WirelessRedstoneTransmission {
	
	public static final Codec<WirelessRedstoneTransmission> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BlockPos.CODEC.fieldOf("origin").forGetter((itemTransfer) -> {
			return itemTransfer.origin;
		}), PositionSource.CODEC.fieldOf("destination").forGetter((itemTransfer) -> {
			return itemTransfer.destination;
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((itemTransfer) -> {
			return itemTransfer.arrivalInTicks;
		})).apply(instance, (Function3) (WirelessRedstoneTransmission::new));
	});
	private final BlockPos origin;
	private final PositionSource destination;
	private final int arrivalInTicks;
	
	public WirelessRedstoneTransmission(BlockPos origin, PositionSource destination, int arrivalInTicks) {
		this.origin = origin;
		this.destination = destination;
		this.arrivalInTicks = arrivalInTicks;
	}
	
	public WirelessRedstoneTransmission(Object origin, Object destination, Object arrivalInTicks) {
		this((BlockPos) origin, (PositionSource) destination, (int) arrivalInTicks);
	}
	
	public static WirelessRedstoneTransmission readFromBuf(PacketByteBuf buf) {
		BlockPos blockPos = buf.readBlockPos();
		PositionSource positionSource = PositionSourceType.read(buf);
		int i = buf.readVarInt();
		return new WirelessRedstoneTransmission(blockPos, positionSource, i);
	}
	
	public static void writeToBuf(PacketByteBuf buf, WirelessRedstoneTransmission itemTransfer) {
		buf.writeBlockPos(itemTransfer.origin);
		PositionSourceType.write(itemTransfer.destination, buf);
		buf.writeVarInt(itemTransfer.arrivalInTicks);
	}
	
	public int getArrivalInTicks() {
		return this.arrivalInTicks;
	}
	
	public BlockPos getOrigin() {
		return this.origin;
	}
	
	public PositionSource getDestination() {
		return this.destination;
	}
}
