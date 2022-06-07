package de.dafuqs.spectrum.particle.effect;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;

public class BlockPosEventTransfer {
	
	public static final Codec<BlockPosEventTransfer> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BlockPos.CODEC.fieldOf("origin").forGetter((experienceOrbTransfer) -> {
			return experienceOrbTransfer.origin;
		}), PositionSource.CODEC.fieldOf("destination").forGetter((experienceOrbTransfer) -> {
			return experienceOrbTransfer.destination;
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((experienceOrbTransfer) -> {
			return experienceOrbTransfer.arrivalInTicks;
		})).apply(instance, (Function3) (BlockPosEventTransfer::new));
	});
	private final BlockPos origin;
	private final PositionSource destination;
	private final int arrivalInTicks;
	
	public BlockPosEventTransfer(BlockPos origin, PositionSource destination, int arrivalInTicks) {
		this.origin = origin;
		this.destination = destination;
		this.arrivalInTicks = arrivalInTicks;
	}
	
	public BlockPosEventTransfer(Object origin, Object destination, Object arrivalInTicks) {
		this((BlockPos) origin, (PositionSource) destination, (int) arrivalInTicks);
	}
	
	public static BlockPosEventTransfer readFromBuf(PacketByteBuf buf) {
		BlockPos blockPos = buf.readBlockPos();
		PositionSource positionSource = PositionSourceType.read(buf);
		int i = buf.readVarInt();
		return new BlockPosEventTransfer(blockPos, positionSource, i);
	}
	
	public static void writeToBuf(PacketByteBuf buf, BlockPosEventTransfer experienceTransfer) {
		buf.writeBlockPos(experienceTransfer.origin);
		PositionSourceType.write(experienceTransfer.destination, buf);
		buf.writeVarInt(experienceTransfer.arrivalInTicks);
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
