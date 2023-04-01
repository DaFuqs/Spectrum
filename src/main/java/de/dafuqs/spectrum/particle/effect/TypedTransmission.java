package de.dafuqs.spectrum.particle.effect;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;

public class TypedTransmission extends SimpleTransmission {
	
	public enum Variant {
		BLOCK_POS,
		ITEM,
		EXPERIENCE,
		REDSTONE,
		HUMMINGSTONE
	}
	
	public static final Codec<TypedTransmission> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Vec3d.CODEC.fieldOf("origin").forGetter((itemTransfer) -> {
			return itemTransfer.origin;
		}), PositionSource.CODEC.fieldOf("destination").forGetter((itemTransfer) -> {
			return itemTransfer.destination;
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((itemTransfer) -> {
			return itemTransfer.arrivalInTicks;
		}), Codec.INT.fieldOf("variant").forGetter((itemTransfer) -> {
			return itemTransfer.variant.ordinal();
		})).apply(instance, TypedTransmission::new);
	});
	
	private final Variant variant;
	
	public TypedTransmission(Vec3d origin, PositionSource destination, int arrivalInTicks, Variant variant) {
		super(origin, destination, arrivalInTicks);
		this.variant = variant;
	}
	
	public TypedTransmission(Object origin, Object destination, Object arrivalInTicks, Object variant) {
		this((Vec3d) origin, (PositionSource) destination, (int) arrivalInTicks, (Variant) variant);
	}
	
	public static TypedTransmission readFromBuf(PacketByteBuf buf) {
		Vec3d origin = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		PositionSource positionSource = PositionSourceType.read(buf);
		int arrivalInTicks = buf.readInt();
		Variant variant = Variant.values()[buf.readInt()];
		return new TypedTransmission(origin, positionSource, arrivalInTicks, variant);
	}
	
	public static void writeToBuf(PacketByteBuf buf, TypedTransmission transfer) {
		buf.writeDouble(transfer.origin.x);
		buf.writeDouble(transfer.origin.y);
		buf.writeDouble(transfer.origin.z);
		PositionSourceType.write(transfer.destination, buf);
		buf.writeInt(transfer.arrivalInTicks);
		buf.writeInt(transfer.variant.ordinal());
	}
	
	public Variant getVariant() {
		return this.variant;
	}
	
}
