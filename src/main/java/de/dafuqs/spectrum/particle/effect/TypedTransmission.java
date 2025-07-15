package de.dafuqs.spectrum.particle.effect;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public class TypedTransmission extends SimpleTransmission {
	
	public enum Variant implements StringRepresentable {
		BLOCK_POS,
		ITEM,
		EXPERIENCE,
		REDSTONE,
		HUMMINGSTONE;
		
		@Override
		public String getSerializedName() {
			return name();
		}
	}
	
	public static final Codec<TypedTransmission> CODEC = RecordCodecBuilder.create(i -> i.group(
			Vec3.CODEC.fieldOf("origin").forGetter(c -> c.origin),
			PositionSource.CODEC.fieldOf("destination").forGetter(c -> c.destination),
			Codec.INT.fieldOf("arrival_in_ticks").forGetter(c -> c.arrivalInTicks),
			StringRepresentable.fromEnum(Variant::values).fieldOf("variant").forGetter(c -> c.variant)
	).apply(i, TypedTransmission::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, TypedTransmission> PACKET_CODEC = StreamCodec.composite(
			PacketCodecHelper.VEC3D, c -> c.origin,
			PositionSource.STREAM_CODEC, c -> c.destination,
			ByteBufCodecs.INT, c -> c.arrivalInTicks,
			PacketCodecHelper.enumOf(Variant::values), c -> c.variant,
			TypedTransmission::new
	);
	
	private final Variant variant;
	
	public TypedTransmission(Vec3 origin, PositionSource destination, int arrivalInTicks, Variant variant) {
		super(origin, destination, arrivalInTicks);
		this.variant = variant;
	}
	
	public Variant getVariant() {
		return this.variant;
	}
	
}
