package de.dafuqs.spectrum.recipe;

import java.util.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;

public abstract class RecipeScaling {
	
	public static final Codec<ScalingData> CODEC = RecordCodecBuilder.<ScalingData>create(i -> i.group(
			SpectrumRegistries.RECIPE_SCALING.getCodec().fieldOf("type").forGetter(d -> d.type),
			Codec.INT.optionalFieldOf("start", 0).forGetter(d -> d.start),
			Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("scaling_value", 0).forGetter(d -> d.scalingValue),
			Codec.doubleRange(0.0, Double.MAX_VALUE).optionalFieldOf("scaling_factor", 1.0).forGetter(d -> d.scalingFactor),
			Codec.INT.listOf(0, 255).optionalFieldOf("indexes", Collections.emptyList()).forGetter(d -> d.indexes)
	).apply(i, ScalingData::new));
	
	public static final PacketCodec<RegistryByteBuf, ScalingData> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.registryValue(SpectrumRegistryKeys.RECIPE_SCALING), d -> d.type,
			PacketCodecs.VAR_INT, d -> d.start,
			PacketCodecs.VAR_INT, d -> d.scalingValue,
			PacketCodecs.DOUBLE, d -> d.scalingFactor,
			PacketCodecs.VAR_INT.collect(PacketCodecs.toList()), d -> d.indexes,
			ScalingData::new
	);
	
	public static final RecipeScaling LINEAR = new RecipeScaling(SpectrumCommon.locate("linear")) {
		@Override
		int getInputCount(double scaling, ScalingData data) {
			return data.start + (int) Math.round(data.scalingValue * scaling * data.scalingFactor);
		}
	};
	
	public static final RecipeScaling DOUBLING = new RecipeScaling(SpectrumCommon.locate("doubling")) {
		@Override
		int getInputCount(double scaling, ScalingData data) {
			return data.start + data.scalingValue << Math.round(((scaling - 1) * data.scalingFactor));
		}
	};
	
	public static final RecipeScaling EXPONENTIAL = new RecipeScaling(SpectrumCommon.locate("exponential")) {
		@Override
		int getInputCount(double scaling, ScalingData data) {
			return (int) (data.start + Math.round(Math.pow(data.scalingValue, scaling * data.scalingFactor)));
		}
	};
	
	public static final RecipeScaling INDEXED = new RecipeScaling(SpectrumCommon.locate("indexed")) {
		@Override
		int getInputCount(double scaling, ScalingData data) {
			var size = data.indexes.size();
			return data.indexes.get(Math.clamp((int) Math.round(scaling - 1), 0, size - 1));
		}
	};
	
	private final Identifier id;
	
	public RecipeScaling(Identifier id) {
		this.id = id;
	}
	
	abstract int getInputCount(double scaling, ScalingData data);
	
	public Identifier getId() {
		return id;
	}
	
	public record ScalingData(RecipeScaling type, int start, int scalingValue, double scalingFactor, List<Integer> indexes) {
		public int apply(double scaling) {
			return type.getInputCount(scaling, this);
		}
	}
	
	public static ScalingData linear(int start, int scalingValue, double scalingFactor, Integer... indices) {
		return new ScalingData(LINEAR, start, scalingValue, scalingFactor, List.of(indices));
	}
	
	public static ScalingData doubling(int scalingValue, Integer... indices) {
		return new ScalingData(DOUBLING, 0, scalingValue, 1.0F, List.of(indices));
	}
	
	public static ScalingData doubling(int start, int scalingValue, double scalingFactor, Integer... indices) {
		return new ScalingData(DOUBLING, start, scalingValue, scalingFactor, List.of(indices));
	}
	
	public static ScalingData exponential(int start, int scalingValue, double scalingFactor, Integer... indices) {
		return new ScalingData(EXPONENTIAL, start, scalingValue, scalingFactor, List.of(indices));
	}
	
	public static ScalingData indices(Integer... indices) {
		return indexed(0, 0, 1.0f, indices);
	}
	
	public static ScalingData indexed(int start, int scalingValue, double scalingFactor, Integer... indices) {
		return new ScalingData(INDEXED, start, scalingValue, scalingFactor, List.of(indices));
	}
}
