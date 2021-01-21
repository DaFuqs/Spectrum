package de.dafuqs.spectrum.recipe.util;

import net.minecraft.util.collection.DefaultedList;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

//Taken from https://github.com/The-Acronym-Coders/BASE/blob/develop/1.12.0/src/main/java/com/teamacronymcoders/base/util/collections/NonnullListCollector.java, thanks for this ;)
public class DefaultedListCollector<T> implements Collector<T, DefaultedList<T>, DefaultedList<T>> {

	private final Set<Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));

	public static <T> DefaultedListCollector<T> toList() {
		return new DefaultedListCollector<>();
	}

	@Override
	public Supplier<DefaultedList<T>> supplier() {
		return DefaultedList::of;
	}

	@Override
	public BiConsumer<DefaultedList<T>, T> accumulator() {
		return DefaultedList::add;
	}

	@Override
	public BinaryOperator<DefaultedList<T>> combiner() {
		return (left, right) -> {
			left.addAll(right);
			return left;
		};
	}

	@Override
	public Function<DefaultedList<T>, DefaultedList<T>> finisher() {
		return i -> (DefaultedList<T>) i;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return CH_ID;
	}
}