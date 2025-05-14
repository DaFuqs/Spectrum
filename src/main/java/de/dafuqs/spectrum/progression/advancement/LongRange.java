package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import net.minecraft.advancements.critereon.*;

import java.util.*;

public record LongRange(Optional<Long> min, Optional<Long> max, Optional<Long> minSquared, Optional<Long> maxSquared) implements MinMaxBounds<Long> {
	
	// The generic specifier is required for some reason
	public static final Codec<LongRange> CODEC = MinMaxBounds.<Long, LongRange>createCodec(Codec.LONG, LongRange::new);
	public static final LongRange ANY = new LongRange(Optional.empty(), Optional.empty());
	
	private LongRange(Optional<Long> min, Optional<Long> max) {
		this(min, max, square(min), square(max));
	}
	
	private static Optional<Long> square(Optional<Long> value) {
		return value.map(d -> d * d);
	}
	
	public static LongRange exactly(long value) {
		return new LongRange(Optional.of(value), Optional.of(value));
	}
	
	public static LongRange between(long min, long max) {
		return new LongRange(Optional.of(min), Optional.of(max));
	}
	
	public static LongRange atLeast(long value) {
		return new LongRange(Optional.of(value), Optional.empty());
	}
	
	public static LongRange atMost(long value) {
		return new LongRange(Optional.empty(), Optional.of(value));
	}
	
	public boolean test(long value) {
		return (this.min.isEmpty() || this.min.get() <= value)
				&& (this.max.isEmpty() || this.max.get() >= value);
	}
}