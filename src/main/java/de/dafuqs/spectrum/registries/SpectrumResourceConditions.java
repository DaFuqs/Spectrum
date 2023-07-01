package de.dafuqs.spectrum.registries;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import net.fabricmc.fabric.api.resource.conditions.v1.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public class SpectrumResourceConditions {
	
	public static final Identifier ENCHANTMENTS_EXIST = SpectrumCommon.locate("enchantments_exist");
	public static final Identifier INTEGRATION_PACK_ACTIVE = SpectrumCommon.locate("integration_pack_active");
	
	public static void register() {
		ResourceConditions.register(ENCHANTMENTS_EXIST, SpectrumResourceConditions::enchantmentExistsMatch);
		ResourceConditions.register(INTEGRATION_PACK_ACTIVE, SpectrumResourceConditions::integrationPackActive);
	}
	
	private static boolean enchantmentExistsMatch(JsonObject object) {
		JsonArray array = JsonHelper.getArray(object, "values");
		
		for (JsonElement element : array) {
			if (element.isJsonPrimitive()) {
				Identifier identifier = Identifier.tryParse(element.getAsString());
				Enchantment enchantment = Registry.ENCHANTMENT.get(identifier);
				
				return enchantment != null;
			} else {
				throw new JsonParseException("Invalid enchantment id entry: " + element);
			}
		}
		
		return false;
	}
	
	private static boolean integrationPackActive(JsonObject object) {
		if (object.has("integration_pack")) {
			return SpectrumIntegrationPacks.isIntegrationPackActive(object.get("integration_pack").getAsString());
		}
		return false;
	}
	
	public static final ThreadLocal<DynamicRegistryManager.Immutable> CURRENT_REGISTRIES = new ThreadLocal<>();
	
}
