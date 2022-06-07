package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonElement;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.StringReader;
import java.util.function.Function;

public class LongRange extends NumberRange<Long> {
	
	public static final LongRange ANY = new LongRange(null, null);
	
	private LongRange(@Nullable Long min, @Nullable Long max) {
		super(min, max);
	}
	
	@Contract("_, null, _ -> new; _, !null, null -> new")
	private static @NotNull LongRange parse(StringReader reader, @Nullable Long min, @Nullable Long max) throws CommandSyntaxException {
		if (min != null && max != null && min > max) {
			throw EXCEPTION_SWAPPED.createWithContext((ImmutableStringReader) reader);
		} else {
			return new LongRange(min, max);
		}
	}
	
	public static LongRange exactly(long value) {
		return new LongRange(value, value);
	}
	
	public static LongRange between(long min, long max) {
		return new LongRange(min, max);
	}
	
	public static LongRange atLeast(long value) {
		return new LongRange(value, null);
	}
	
	public static LongRange atMost(long value) {
		return new LongRange(null, value);
	}
	
	public static LongRange fromJson(@Nullable JsonElement element) {
		return fromJson(element, ANY, JsonHelper::asLong, LongRange::new);
	}
	
	public static LongRange parse(StringReader reader) throws CommandSyntaxException {
		return fromStringReader(reader, (value) -> value);
	}
	
	public static LongRange fromStringReader(StringReader reader, Function<Long, Long> converter) throws CommandSyntaxException {
		return parse(reader);
	}
	
	public boolean test(long value) {
		if (this.min != null && this.min > value) {
			return false;
		} else {
			return this.max == null || this.max >= value;
		}
	}
	
}