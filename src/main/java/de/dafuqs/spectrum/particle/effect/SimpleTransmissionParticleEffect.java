package de.dafuqs.spectrum.particle.effect;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;

public class SimpleTransmissionParticleEffect {
	
	public enum Variant {
		BLOCK_POS,
		ITEM,
		EXPERIENCE,
		HUMMINGSTONE
	}
	
	public static final Codec<SimpleTransmissionParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Vec3d.CODEC.fieldOf("origin").forGetter((itemTransfer) -> {
			return itemTransfer.origin;
		}), PositionSource.CODEC.fieldOf("destination").forGetter((itemTransfer) -> {
			return itemTransfer.destination;
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((itemTransfer) -> {
			return itemTransfer.arrivalInTicks;
		}), Codec.INT.fieldOf("variant").forGetter((itemTransfer) -> {
			return itemTransfer.variant.ordinal();
		})).apply(instance, (Function4) (SimpleTransmissionParticleEffect::new));
	});
	private final Vec3d origin;
	private final PositionSource destination;
	private final int arrivalInTicks;
	private final Variant variant;
	
	public SimpleTransmissionParticleEffect(Vec3d origin, PositionSource destination, int arrivalInTicks, Variant variant) {
		this.origin = origin;
		this.destination = destination;
		this.arrivalInTicks = arrivalInTicks;
		this.variant = variant;
	}
	
	public SimpleTransmissionParticleEffect(Object origin, Object destination, Object arrivalInTicks, Object variant) {
		this((Vec3d) origin, (PositionSource) destination, (int) arrivalInTicks, (Variant) variant);
	}
	
	public static SimpleTransmissionParticleEffect readFromBuf(PacketByteBuf buf) {
		Vec3d origin = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		PositionSource positionSource = PositionSourceType.read(buf);
		int arrivalInTicks = buf.readVarInt();
		Variant variant = Variant.values()[buf.readVarInt()];
		return new SimpleTransmissionParticleEffect(origin, positionSource, arrivalInTicks, variant);
	}
	
	public static void writeToBuf(PacketByteBuf buf, SimpleTransmissionParticleEffect transfer) {
		buf.writeDouble(transfer.origin.x);
		buf.writeDouble(transfer.origin.y);
		buf.writeDouble(transfer.origin.z);
		PositionSourceType.write(transfer.destination, buf);
		buf.writeVarInt(transfer.arrivalInTicks);
		buf.writeVarInt(transfer.variant.ordinal());
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
	
	public Variant getVariant() {
		return this.variant;
	}
}
