package de.dafuqs.spectrum.particle.effect;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;

public class ColoredTransmission {
	
	public static final Codec<ColoredTransmission> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Vec3d.CODEC.fieldOf("origin").forGetter((coloredTransmission) -> {
			return coloredTransmission.origin;
		}), PositionSource.CODEC.fieldOf("destination").forGetter((coloredTransmission) -> {
			return coloredTransmission.destination;
		}), Codec.INT.fieldOf("dye_color").forGetter((coloredTransmission) -> {
			return coloredTransmission.dyeColor.getId();
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((coloredTransmission) -> {
			return coloredTransmission.arrivalInTicks;
		})).apply(instance, (Function4) (ColoredTransmission::new));
	});
	private final Vec3d origin;
	private final PositionSource destination;
	private final int arrivalInTicks;
	private final DyeColor dyeColor;
	
	public ColoredTransmission(Vec3d origin, PositionSource destination, int arrivalInTicks, DyeColor dyeColor) {
		this.origin = origin;
		this.destination = destination;
		this.arrivalInTicks = arrivalInTicks;
		this.dyeColor = dyeColor;
	}
	
	public ColoredTransmission(Object origin, Object destination, Object arrivalInTicks, Object dyeColor) {
		this((Vec3d) origin, (PositionSource) destination, (int) arrivalInTicks, DyeColor.values()[(int) dyeColor]);
	}
	
	public static ColoredTransmission readFromBuf(PacketByteBuf buf) {
		Vec3d origin = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		PositionSource destination = PositionSourceType.read(buf);
		int arrivalInTicks = buf.readVarInt();
		DyeColor dyeColor = DyeColor.byId(buf.readVarInt());
		return new ColoredTransmission(origin, destination, arrivalInTicks, dyeColor);
	}
	
	public static void writeToBuf(PacketByteBuf buf, ColoredTransmission transfer) {
		buf.writeDouble(transfer.origin.x);
		buf.writeDouble(transfer.origin.y);
		buf.writeDouble(transfer.origin.z);
		PositionSourceType.write(transfer.destination, buf);
		buf.writeVarInt(transfer.arrivalInTicks);
		buf.writeVarInt(transfer.dyeColor.getId());
	}
	
	public int getArrivalInTicks() {
		return this.arrivalInTicks;
	}
	
	public Vec3d getOrigin() {
		return this.origin;
	}
	
	public PositionSource getDestination() {
		return this.destination;
	}
	
	public DyeColor getDyeColor() {
		return this.dyeColor;
	}
	
}
