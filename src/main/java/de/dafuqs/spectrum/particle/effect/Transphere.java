package de.dafuqs.spectrum.particle.effect;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;

public class Transphere {
	
	public static final Codec<Transphere> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BlockPos.CODEC.fieldOf("origin").forGetter((transphere) -> {
			return transphere.origin;
		}), PositionSource.CODEC.fieldOf("destination").forGetter((transphere) -> {
			return transphere.destination;
		}), Codec.INT.fieldOf("dye_color").forGetter((transphere) -> {
			return transphere.dyeColor.ordinal();
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((transphere) -> {
			return transphere.arrivalInTicks;
		})).apply(instance, (Function4) (Transphere::new));
	});
	private final BlockPos origin;
	private final PositionSource destination;
	private final int arrivalInTicks;
	private final DyeColor dyeColor;
	
	public Transphere(BlockPos origin, PositionSource destination, int arrivalInTicks, DyeColor dyeColor) {
		this.origin = origin;
		this.destination = destination;
		this.arrivalInTicks = arrivalInTicks;
		this.dyeColor = dyeColor;
	}
	
	public Transphere(Object origin, Object destination, Object arrivalInTicks, Object dyeColor) {
		this((BlockPos) origin, (PositionSource) destination, (int) arrivalInTicks, DyeColor.values()[(int) dyeColor]);
	}
	
	public static Transphere readFromBuf(PacketByteBuf buf) {
		BlockPos blockPos = buf.readBlockPos();
		PositionSource positionSource = PositionSourceType.read(buf);
		DyeColor dyeColor = DyeColor.values()[buf.readInt()];
		int arrivalInTicks = buf.readVarInt();
		return new Transphere(blockPos, positionSource, arrivalInTicks, dyeColor);
	}
	
	public static void writeToBuf(PacketByteBuf buf, Transphere transphere) {
		buf.writeBlockPos(transphere.origin);
		PositionSourceType.write(transphere.destination, buf);
		buf.writeInt(transphere.getDyeColor().ordinal());
		buf.writeVarInt(transphere.arrivalInTicks);
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
	
	public DyeColor getDyeColor() {
		return this.dyeColor;
	}
	
}
