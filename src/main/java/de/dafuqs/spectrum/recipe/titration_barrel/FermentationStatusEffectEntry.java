package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.entity.effect.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;

import java.util.*;

public record FermentationStatusEffectEntry(StatusEffect statusEffect, int baseDuration,
											List<StatusEffectPotencyEntry> potencyEntries) {
	
	public record StatusEffectPotencyEntry(int minAlcPercent, int minThickness, int potency) {
		
		public static StatusEffectPotencyEntry fromJson(JsonObject jsonObject) {
			int minAlcPercent = JsonHelper.getInt(jsonObject, "min_alc", 0);
			int minThickness = JsonHelper.getInt(jsonObject, "min_thickness", 0);
			int potency = JsonHelper.getInt(jsonObject, "potency", 0);
			return new StatusEffectPotencyEntry(minAlcPercent, minThickness, potency);
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
	
	public static FermentationStatusEffectEntry fromJson(JsonObject jsonObject) {
		Identifier statusEffectIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "id"));
		StatusEffect statusEffect = Registries.STATUS_EFFECT.get(statusEffectIdentifier);
		if (statusEffect == null) {
			SpectrumCommon.logError("Status effect " + statusEffectIdentifier + " does not exist in the status effect registry. Falling back to WEAKNESS");
			statusEffect = StatusEffects.WEAKNESS;
		}
		int baseDuration = JsonHelper.getInt(jsonObject, "base_duration", 1200);
		
		List<StatusEffectPotencyEntry> potencyEntries = new ArrayList<>();
		if (JsonHelper.hasArray(jsonObject, "potency")) {
			JsonArray potencyArray = JsonHelper.getArray(jsonObject, "potency");
			for (int i = 0; i < potencyArray.size(); i++) {
				JsonObject object = potencyArray.get(i).getAsJsonObject();
				potencyEntries.add(StatusEffectPotencyEntry.fromJson(object));
			}
		} else {
			potencyEntries.add(new StatusEffectPotencyEntry(0, 0, 0));
		}
		return new FermentationStatusEffectEntry(statusEffect, baseDuration, potencyEntries);
	}
	
	public void write(PacketByteBuf packetByteBuf) {
		packetByteBuf.writeString(Registries.STATUS_EFFECT.getId(this.statusEffect).toString());
		packetByteBuf.writeInt(baseDuration);
		packetByteBuf.writeInt(this.potencyEntries.size());
		for (StatusEffectPotencyEntry potencyEntry : this.potencyEntries) {
			potencyEntry.write(packetByteBuf);
		}
	}
	
	public static FermentationStatusEffectEntry read(PacketByteBuf packetByteBuf) {
		Identifier statusEffectIdentifier = Identifier.tryParse(packetByteBuf.readString());
		StatusEffect statusEffect = Registries.STATUS_EFFECT.get(statusEffectIdentifier);
		int baseDuration = packetByteBuf.readInt();
		int potencyEntryCount = packetByteBuf.readInt();
		List<StatusEffectPotencyEntry> potencyEntries = new ArrayList<>(potencyEntryCount);
		for (int i = 0; i < potencyEntryCount; i++) {
			potencyEntries.add(StatusEffectPotencyEntry.read(packetByteBuf));
		}
		return new FermentationStatusEffectEntry(statusEffect, baseDuration, potencyEntries);
	}
	
}
