package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.interaction.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;
import net.minecraft.resource.*;
import net.minecraft.util.*;
import net.minecraft.util.profiler.*;

import java.util.*;

public class ResonanceDropsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	
	public static final String ID = "resonance_drops";
	public static final ResonanceDropsDataLoader INSTANCE = new ResonanceDropsDataLoader();
	
	protected static final List<ResonanceDropProcessor> RESONANCE_DROPS = new ArrayList<>();
	
	public static boolean preventNextXPDrop;
	
	private ResonanceDropsDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		RESONANCE_DROPS.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject json = jsonElement.getAsJsonObject();
			
			Identifier processorType = Identifier.tryParse(JsonHelper.getString(json, "type"));
			ResonanceDropProcessor.Serializer serializer = ResonanceDropProcessors.get(processorType);
			if (serializer == null) {
				SpectrumCommon.logError("Unknown ResonanceDropProcessor " + processorType + " in file " + identifier);
				return;
			}
			try {
				RESONANCE_DROPS.add(serializer.fromJson(json));
			} catch (Exception e) {
				SpectrumCommon.logError("Error parsing ResonanceDropProcessor " + identifier + ": " + e.getLocalizedMessage());
			}
		});
	}
	
	@Override
	public Identifier getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static void applyResonance(BlockState minedState, BlockEntity blockEntity, List<ItemStack> droppedStacks) {
		for (ResonanceDropProcessor entry : RESONANCE_DROPS) {
		entry.process(minedState, blockEntity, droppedStacks);
		}
	}
	
}
