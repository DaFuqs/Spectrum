package de.dafuqs.spectrum.progression.revelationary;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;

public class RevelationDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {

    public static final RevelationDataLoader INSTANCE = new RevelationDataLoader();

    private RevelationDataLoader() {
        super(new Gson(), "relevationary");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        RevelationRegistry.clear();
        prepared.forEach((identifier, jsonElement) -> {
            RevelationRegistry.registerFromJson(jsonElement.getAsJsonObject());
        });
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(SpectrumCommon.MOD_ID, "relevationary");
    }
    
}