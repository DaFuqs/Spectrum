package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.*;

import java.util.*;

public class ColorMappingDataLoader<T> extends SimpleJsonResourceReloadListener {
	
	public static final String ID = "ink_color_mapping/%s";
	
	protected final String directory;
	protected final Registry<T> registry;
	protected final HashMap<T, InkColor> mappings = new HashMap<>();
	
	public ColorMappingDataLoader(String name, Registry<T> registry) {
		super(new Gson(), ID.formatted(name));
		this.directory = ID.formatted(name);
		this.registry = registry;
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager manager, ProfilerFiller profiler) {
		mappings.clear();
		prepared.forEach((identifier, jsonElement) -> {
			
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				String inkColorString = entry.getKey();
				InkColor inkColor = SpectrumRegistries.INK_COLOR.get(ResourceLocation.parse(inkColorString));
				if(inkColor == null) {
					SpectrumCommon.LOGGER.error("[ColorMappingDataLoader] Invalid ink color: {} at: {}", inkColorString, getFileStringForLocation(identifier));
					continue;
				}
				
				for(JsonElement e :  entry.getValue().getAsJsonArray()) {
					String s = e.getAsString();
					Optional<T> value = this.registry.getOptional(ResourceLocation.parse(s));
					if(value.isEmpty()) {
						SpectrumCommon.LOGGER.error("[ColorMappingDataLoader] Invalid value for ink color {}: {} at: {}", inkColorString, s, getFileStringForLocation(identifier));
						continue;
					}
					
					mappings.put(value.get(), inkColor);
				}
			}
		});
	}
	
	protected String getFileStringForLocation(ResourceLocation identifier) {
		return identifier.getNamespace() + ":" + this.directory + "/" + identifier.getPath() + ".json";
	}
	
	public Optional<InkColor> getInkColor(T value) {
		return Optional.ofNullable(mappings.get(value));
	}
	
	public InkColor getInkColor(T value, InkColor fallback) {
		return mappings.getOrDefault(value, fallback);
	}
	
}
