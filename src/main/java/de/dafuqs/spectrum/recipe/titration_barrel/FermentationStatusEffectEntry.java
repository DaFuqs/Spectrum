package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.entity.effect.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

import java.util.*;

public record FermentationStatusEffectEntry(StatusEffect statusEffect, int baseDuration,
											List<StatusEffectPotencyEntry> potencyEntries) {
	
	public record StatusEffectPotencyEntry(int minAlcPercent, int minThickness, int potency) {
		
		private static final String MIN_ALC_STRING = "min_alc";
		private static final String MIN_THICKNESS_STRING = "min_thickness";
		private static final String MIN_POTENCY_STRING = "potency";
		
		public static StatusEffectPotencyEntry fromJson(JsonObject jsonObject) {
			int minAlcPercent = JsonHelper.getInt(jsonObject, MIN_ALC_STRING, 0);
			int minThickness = JsonHelper.getInt(jsonObject, MIN_THICKNESS_STRING, 0);
			int potency = JsonHelper.getInt(jsonObject, MIN_POTENCY_STRING, 0);
			return new StatusEffectPotencyEntry(minAlcPercent, minThickness, potency);
		}
		
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty(MIN_ALC_STRING, this.minAlcPercent);
			json.addProperty(MIN_THICKNESS_STRING, this.minThickness);
			json.addProperty(MIN_POTENCY_STRING, this.potency);
			return json;
		}
		
		public void write(PacketByteBuf packetByteBuf) {
			packetByteBuf.writeInt(this.minAlcPercent);
			packetByteBuf.writeInt(this.minThickness);
			packetByteBuf.writeInt(this.potency);
		}
		
		public static StatusEffectPotencyEntry read(PacketByteBuf packetByteBuf) {
			return new StatusEffectPotencyEntry(packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readInt());
		}
	}
	
	private static final String EFFECT_ID_STRING = "id";
	private static final String BASE_DURATION_STRING = "base_duration";
	private static final String POTENCY_STRING = "potency";
	
	public static FermentationStatusEffectEntry fromJson(JsonObject jsonObject) {
		Identifier statusEffectIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, EFFECT_ID_STRING));
		StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectIdentifier);
		if (statusEffect == null) {
			SpectrumCommon.logError("Status effect " + statusEffectIdentifier + " does not exist in the status effect registry. Falling back to WEAKNESS");
			statusEffect = StatusEffects.WEAKNESS;
		}
		int baseDuration = JsonHelper.getInt(jsonObject, BASE_DURATION_STRING, 1200);
		
		List<StatusEffectPotencyEntry> potencyEntries = new ArrayList<>();
		if (JsonHelper.hasArray(jsonObject, POTENCY_STRING)) {
			JsonArray potencyArray = JsonHelper.getArray(jsonObject, POTENCY_STRING);
			for (int i = 0; i < potencyArray.size(); i++) {
				JsonObject object = potencyArray.get(i).getAsJsonObject();
				potencyEntries.add(StatusEffectPotencyEntry.fromJson(object));
			}
		} else {
			potencyEntries.add(new StatusEffectPotencyEntry(0, 0, 0));
		}
		return new FermentationStatusEffectEntry(statusEffect, baseDuration, potencyEntries);
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		
		json.addProperty(EFFECT_ID_STRING, Registry.STATUS_EFFECT.getId(this.statusEffect).toString());
		json.addProperty(BASE_DURATION_STRING, this.baseDuration);
		JsonArray effects = new JsonArray();
		for (StatusEffectPotencyEntry entry : this.potencyEntries) {
			effects.add(entry.toJson());
		}
		json.add(POTENCY_STRING, effects);
		
		return json;
	}
	
	public void write(PacketByteBuf packetByteBuf) {
		packetByteBuf.writeString(Registry.STATUS_EFFECT.getId(this.statusEffect).toString());
		packetByteBuf.writeInt(baseDuration);
		packetByteBuf.writeInt(this.potencyEntries.size());
		for (StatusEffectPotencyEntry potencyEntry : this.potencyEntries) {
			potencyEntry.write(packetByteBuf);
		}
	}
	
	public static FermentationStatusEffectEntry read(PacketByteBuf packetByteBuf) {
		Identifier statusEffectIdentifier = Identifier.tryParse(packetByteBuf.readString());
		StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectIdentifier);
		int baseDuration = packetByteBuf.readInt();
		int potencyEntryCount = packetByteBuf.readInt();
		List<StatusEffectPotencyEntry> potencyEntries = new ArrayList<>(potencyEntryCount);
		for (int i = 0; i < potencyEntryCount; i++) {
			potencyEntries.add(StatusEffectPotencyEntry.read(packetByteBuf));
		}
		return new FermentationStatusEffectEntry(statusEffect, baseDuration, potencyEntries);
	}
	
}
