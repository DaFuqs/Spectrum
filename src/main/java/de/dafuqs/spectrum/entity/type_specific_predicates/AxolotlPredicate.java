package de.dafuqs.spectrum.entity.type_specific_predicates;

import com.google.gson.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AxolotlPredicate implements TypeSpecificPredicate {
	
	private static final String VARIANT_KEY = "variant";
	
	private final AxolotlEntity.Variant variant;
	
	private AxolotlPredicate(AxolotlEntity.Variant variant) {
		this.variant = variant;
	}
	
	public static AxolotlPredicate of(AxolotlEntity.Variant color) {
		return new AxolotlPredicate(color);
	}
	
	public static AxolotlPredicate fromJson(JsonObject json) {
		return new AxolotlPredicate(AxolotlEntity.Variant.valueOf(json.get(VARIANT_KEY).getAsString().toUpperCase(Locale.ROOT)));
	}
	
	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(VARIANT_KEY, new JsonPrimitive(this.variant.toString().toLowerCase(Locale.ROOT)));
		return jsonObject;
	}
	
	@Override
	public Deserializer getDeserializer() {
		return SpectrumTypeSpecificPredicates.AXOLOTL;
	}
	
	@Override
	public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
		if (!(entity instanceof AxolotlEntity axolotlEntity)) {
			return false;
		} else {
			return this.variant == axolotlEntity.getVariant();
		}
	}
}
