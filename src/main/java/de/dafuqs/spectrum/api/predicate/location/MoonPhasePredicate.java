package de.dafuqs.spectrum.api.predicate.location;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import io.netty.buffer.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public enum MoonPhasePredicate implements StringRepresentable {
	FULL_MOON,
	WANING_GIBBOUS,
	THIRD_QUARTER,
	WANING_CRESCENT,
	NEW_MOON,
	WAXING_CRESCENT,
	FIRST_QUARTER,
	WAXING_GIBBOUS;
	
	public static final Codec<MoonPhasePredicate> CODEC = StringRepresentable.fromEnum(MoonPhasePredicate::values);
	public static final StreamCodec<ByteBuf, MoonPhasePredicate> PACKET_CODEC = PacketCodecHelper.enumOf(MoonPhasePredicate::values);
	
	public boolean test(ServerLevel world) {
		return ordinal() == world.getMoonPhase();
	}
	
	@Override
	public @NotNull String getSerializedName() {
		return name().toLowerCase();
	}
	
}