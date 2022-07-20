package de.dafuqs.spectrum.registries;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class SpectrumResourceConditions {
	
	public static final Identifier ENCHANTMENTS_EXIST = SpectrumCommon.locate("enchantments_exist");
	
	public static void register() {
		ResourceConditions.register(ENCHANTMENTS_EXIST, object -> enchantmentExistsMatch(object));
	}
	
	public static boolean enchantmentExistsMatch(JsonObject object) {
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
	
}
