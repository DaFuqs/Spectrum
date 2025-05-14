package de.dafuqs.spectrum.helpers;

import com.google.gson.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import org.apache.commons.lang3.math.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class CodecHelper {
	
	public static Codec<Fraction> FRACTION = Codec.mapPair(
			Codec.INT.fieldOf("numerator"),
			Codec.INT.fieldOf("denominator")
	).codec().xmap(
			pair -> Fraction.getFraction(pair.getFirst(), pair.getSecond()),
			frac -> new com.mojang.datafixers.util.Pair<>(frac.getNumerator(), frac.getDenominator())
	);
	
	public static Codec<ResourceLocation> SPECTRUM_DEFAULTED_IDENTIFIER = Codec.STRING.xmap(SpectrumCommon::ofSpectrumDefaulted, ResourceLocation::toString);
	
	public static MapCodec<HolderLookup.Provider> LOOKUP = new MapCodec<>() {
		@Override
		public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
			return Stream.empty();
		}
		
		@Override
		public <T> DataResult<HolderLookup.Provider> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
			if (dynamicOps instanceof RegistryOps<T> registryOps) {
				var infoGetter = ((RegistryOpsAccessor) registryOps).getLookupProvider();
				var lookup = ((CachedRegistryInfoGetterAccessor) infoGetter).getLookupProvider();
				return DataResult.success(lookup);
			}
			return DataResult.error(() -> "The LOOKUP codec requires RegistryOps.");
		}
		
		@Override
		public <T> RecordBuilder<T> encode(HolderLookup.Provider wrapperLookup, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
			return recordBuilder;
		}
	};
	
	public static <L, R> MapCodec<Tuple<L, R>> mapPair(MapCodec<L> leftCodec, MapCodec<R> rightCodec) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
				leftCodec.forGetter(Tuple::getA),
				rightCodec.forGetter(Tuple::getB)
		).apply(i, Tuple::new));
	}
	
	public static <K, V> MapCodec<Map<K, V>> registryMap(Registry<K> registry, Codec<V> valueCodec) {
		Codec<K> keyCodec = registry.byNameCodec();
		
		return new MapCodec<>() {
			@Override
			public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
				return registry.keys(dynamicOps);
			}
			
			@Override
			public <T> DataResult<Map<K, V>> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
				return DataResult.success(mapLike.entries()
						.map(entry -> {
							K keyResult = keyCodec.decode(dynamicOps, entry.getFirst()).result().map(com.mojang.datafixers.util.Pair::getFirst).orElse(null);
							V valueResult = valueCodec.decode(dynamicOps, entry.getSecond()).result().map(com.mojang.datafixers.util.Pair::getFirst).orElse(null);
							if (keyResult == null || valueResult == null)
								return null;
							return new Tuple<>(keyResult, valueResult);
						})
						.filter(Objects::nonNull)
						.collect(HashMap::new, (map, pair) -> map.put(pair.getA(), pair.getB()), HashMap::putAll));
			}
			
			@Override
			public <T> RecordBuilder<T> encode(Map<K, V> kvMap, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
				for (Map.Entry<K, V> entry : kvMap.entrySet()) {
					DataResult<T> keyData = keyCodec.encodeStart(dynamicOps, entry.getKey());
					DataResult<T> valueData = valueCodec.encodeStart(dynamicOps, entry.getValue());
					recordBuilder.add(keyData, valueData);
				}
				return recordBuilder;
			}
		};
	}
	
	public static <T> Codec<List<T>> singleOrList(Codec<T> codec) {
		return Codec.withAlternative(codec.listOf(), codec, List::of);
	}
	
	public interface ThrowableFunction<I, O, E extends Throwable> {
		O apply(I input) throws E;
	}
	
	public static <I, O, E extends Throwable> Function<I, DataResult<O>> throwable(ThrowableFunction<I, O, E> throwable) {
		return input -> {
			try {
				return DataResult.success(throwable.apply(input));
			} catch (Throwable e) {
				return DataResult.error(e::getMessage);
			}
		};
	}
	
	public static <T, D> Optional<T> from(DynamicOps<D> ops, Codec<T> codec, D elem) {
		if (elem == null) return Optional.empty();
		return codec.decode(ops, elem).result().map(com.mojang.datafixers.util.Pair::getFirst);
	}
	
	public static <T> Optional<T> fromNbt(Codec<T> codec, Tag nbt) {
		return from(NbtOps.INSTANCE, codec, nbt);
	}
	
	public static <T> T fromNbt(Codec<T> codec, Tag nbt, T defaultValue) {
		return fromNbt(codec, nbt).orElse(defaultValue);
	}
	
	public static <T> Optional<T> fromJson(Codec<T> codec, JsonElement json) {
		return from(JsonOps.INSTANCE, codec, json);
	}
	
	public static <T> T fromJson(Codec<T> codec, JsonElement json, T defaultValue) {
		return fromJson(codec, json).orElse(defaultValue);
	}
	
	public static <T> void toNbt(Codec<T> codec, T value, Consumer<? super Tag> ifValid) {
		codec.encodeStart(NbtOps.INSTANCE, value).result().ifPresent(ifValid);
	}
	
	public static <T> void writeNbt(CompoundTag nbt, String key, Codec<T> codec, T value) {
		toNbt(codec, value, elem -> nbt.put(key, elem));
	}
	
}
