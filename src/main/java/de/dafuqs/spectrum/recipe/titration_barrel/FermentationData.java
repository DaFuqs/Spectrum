package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.*;
import net.minecraft.network.*;
import net.minecraft.util.*;

import java.util.*;

public record FermentationData(float fermentationSpeedMod, float angelsSharePercentPerMcDay,
							   List<FermentationStatusEffectEntry> statusEffectEntries) {
	
	public static FermentationData fromJson(JsonObject jsonObject) {
		float fermentationSpeedMod = JsonHelper.getFloat(jsonObject, "fermentation_speed_mod", 1.0F);
		float angelsSharePerMcDay = JsonHelper.getFloat(jsonObject, "angels_share_percent_per_mc_day", 0.1F);
		List<FermentationStatusEffectEntry> statusEffectEntries = new ArrayList<>();
		if (JsonHelper.hasArray(jsonObject, "effects")) {
			JsonArray effectsArray = JsonHelper.getArray(jsonObject, "effects");
			for (int i = 0; i < effectsArray.size(); i++) {
				JsonObject object = effectsArray.get(i).getAsJsonObject();
				statusEffectEntries.add(FermentationStatusEffectEntry.fromJson(object));
			}
		}
		return new FermentationData(fermentationSpeedMod, angelsSharePerMcDay, statusEffectEntries);
	}
	
	public void write(PacketByteBuf packetByteBuf) {
		packetByteBuf.writeFloat(this.fermentationSpeedMod);
		packetByteBuf.writeFloat(this.angelsSharePercentPerMcDay);
		packetByteBuf.writeInt(this.statusEffectEntries.size());
		for (FermentationStatusEffectEntry fermentationStatusEffectEntry : this.statusEffectEntries) {
			fermentationStatusEffectEntry.write(packetByteBuf);
		}
	}
	
	public static FermentationData read(PacketByteBuf packetByteBuf) {
		float fermentationSpeedMod = packetByteBuf.readFloat();
		float angelsSharePerMcDay = packetByteBuf.readFloat();
		int statusEffectEntryCount = packetByteBuf.readInt();
		List<FermentationStatusEffectEntry> statusEffectEntries = new ArrayList<>(statusEffectEntryCount);
		for (int i = 0; i < statusEffectEntryCount; i++) {
			statusEffectEntries.add(FermentationStatusEffectEntry.read(packetByteBuf));
		}
		return new FermentationData(fermentationSpeedMod, angelsSharePerMcDay, statusEffectEntries);
	}
	
	public JsonObject toJson() {
		JsonObject object = new JsonObject();
		// TODO: not necessary, but nice to have
		return object;
	}
	
}
