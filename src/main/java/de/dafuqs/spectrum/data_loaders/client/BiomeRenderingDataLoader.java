package de.dafuqs.spectrum.data_loaders.client;

import com.google.gson.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.render.biome_rendering.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.*;
import net.minecraft.world.level.biome.*;
import net.neoforged.neoforge.common.conditions.*;

import java.util.*;

public class BiomeRenderingDataLoader extends SimpleJsonResourceReloadListener {
	
	public static final String ID = "biome_rendering_data";
	public static final BiomeRenderingDataLoader INSTANCE = new BiomeRenderingDataLoader();
	
	private static final Map<ResourceKey<Biome>, Pair<ColorGrading, EnvironmentalData>> DATA = new HashMap<>();
	
	public record Entry(ResourceKey<Biome> biome, Optional<EnvironmentalData> environmentalData, Optional<ColorGrading> colorGrading) {
		public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
				ResourceKey.codec(Registries.BIOME).fieldOf("biome").forGetter(Entry::biome),
				EnvironmentalData.CODEC.optionalFieldOf("environmental_data").forGetter(Entry::environmentalData),
				ColorGrading.CODEC.optionalFieldOf("color_grading").forGetter(Entry::colorGrading)
		).apply(i, Entry::new));
	}
	
	private static final Pair<ColorGrading, EnvironmentalData> DEFAULT = new Pair<>(ColorGrading.DEFAULT, EnvironmentalData.NOOP);
	
	private BiomeRenderingDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager resourceManager, ProfilerFiller profiler) {
		DATA.clear();
		
		ConditionalOps<JsonElement> ops = makeConditionalOps();
		files.forEach((location, element) -> {
			DataResult<Entry> data = Entry.CODEC.parse(ops, element);
			if (data.error().isPresent()) {
				SpectrumCommon.LOGGER.error("[Biome Rendering Data] Error loading entry [{}]: {}", location, data.error().get());
				return;
			}
			
			if(data.result().isPresent()) {
				Entry entry = data.getOrThrow();
				DATA.put(entry.biome(), new Pair<>(entry.colorGrading().orElse(DEFAULT.getFirst()), entry.environmentalData().orElse(DEFAULT.getSecond())));
			}
		});
	}
	
	public static Pair<ColorGrading, EnvironmentalData> get(ResourceKey<Biome> biome) {
		return DATA.getOrDefault(biome, DEFAULT);
	}
	
}