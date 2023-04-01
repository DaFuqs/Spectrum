package de.dafuqs.spectrum.particle.effect;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;

public class ColoredTransmission extends SimpleTransmission {
	
	public static final Codec<ColoredTransmission> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Vec3d.CODEC.fieldOf("origin").forGetter((coloredTransmission) -> {
			return coloredTransmission.origin;
		}), PositionSource.CODEC.fieldOf("destination").forGetter((coloredTransmission) -> {
			return coloredTransmission.destination;
		}), Codec.INT.fieldOf("dye_color").forGetter((coloredTransmission) -> {
			return coloredTransmission.dyeColor.getId();
		}), Codec.INT.fieldOf("arrival_in_ticks").forGetter((coloredTransmission) -> {
			return coloredTransmission.arrivalInTicks;
		})).apply(instance, ColoredTransmission::new);
	});
	
	protected final DyeColor dyeColor;
	
	public ColoredTransmission(Vec3d origin, PositionSource destination, int arrivalInTicks, DyeColor dyeColor) {
		super(origin, destination, arrivalInTicks);
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
	
	public DyeColor getDyeColor() {
		return this.dyeColor;
	}
	
}
