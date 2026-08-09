package de.dafuqs.spectrum.api.predicate.location;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import io.netty.buffer.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;

import java.util.function.*;

public enum WeatherPredicate implements StringRepresentable {
	CLEAR_SKY(world -> !world.isRaining()),
	RAIN(Level::isRaining),
	STRICT_RAIN(world -> world.isRaining() && !world.isThundering()),
	THUNDER(Level::isThundering),
	NOT_THUNDER(world -> !world.isThundering());
	
	public static final Codec<WeatherPredicate> CODEC = StringRepresentable.fromEnum(WeatherPredicate::values);
	public static final StreamCodec<ByteBuf, WeatherPredicate> PACKET_CODEC = PacketCodecHelper.enumOf(WeatherPredicate::values);
	
	private final Function<ServerLevel, Boolean> test;
	
	WeatherPredicate(Function<ServerLevel, Boolean> test) {
		this.test = test;
	}
	
	public boolean test(ServerLevel world) {
		return test.apply(world);
	}
	
	@Override
	public String getSerializedName() {
		return name().toLowerCase();
	}
	
}