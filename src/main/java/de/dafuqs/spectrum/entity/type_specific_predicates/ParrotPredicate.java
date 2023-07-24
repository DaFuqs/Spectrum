package de.dafuqs.spectrum.entity.type_specific_predicates;

import com.google.gson.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

public class ParrotPredicate implements TypeSpecificPredicate {
	
	private static final String VARIANT_KEY = "variant";
	
	private final int variant;
	
	private ParrotPredicate(int variant) {
		this.variant = variant;
	}
	
	public static ParrotPredicate of(int variant) {
		return new ParrotPredicate(variant);
	}
	
	public static ParrotPredicate fromJson(JsonObject json) {
		return new ParrotPredicate(json.get(VARIANT_KEY).getAsInt());
	}
	
	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(VARIANT_KEY, this.variant);
		return jsonObject;
	}
	
	@Override
	public Deserializer getDeserializer() {
		return SpectrumTypeSpecificPredicates.PARROT;
	}
	
	@Override
	public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
		if (!(entity instanceof ParrotEntity parrotEntity)) {
			return false;
		} else {
			return this.variant == parrotEntity.getVariant();
		}
	}
}
