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

public class FoxPredicate implements TypeSpecificPredicate {
	
	private static final String TYPE_KEY = "fox_type";
	
	private final FoxEntity.Type type;
	
	private FoxPredicate(FoxEntity.Type type) {
		this.type = type;
	}
	
	public static FoxPredicate of(FoxEntity.Type type) {
		return new FoxPredicate(type);
	}
	
	public static FoxPredicate fromJson(JsonObject json) {
		return new FoxPredicate(FoxEntity.Type.valueOf(json.get(TYPE_KEY).getAsString().toUpperCase(Locale.ROOT)));
	}
	
	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(TYPE_KEY, new JsonPrimitive(this.type.toString().toLowerCase(Locale.ROOT)));
		return jsonObject;
	}
	
	@Override
	public Deserializer getDeserializer() {
		return SpectrumTypeSpecificPredicates.FOX;
	}
	
	@Override
	public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
		if (!(entity instanceof FoxEntity foxEntity)) {
			return false;
		} else {
			return this.type == foxEntity.getFoxType();
		}
	}
}
