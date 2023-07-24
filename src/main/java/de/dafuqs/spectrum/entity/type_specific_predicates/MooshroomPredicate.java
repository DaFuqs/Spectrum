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

public class MooshroomPredicate implements TypeSpecificPredicate {
	
	private static final String TYPE_KEY = "mooshroom_type";
	
	final MooshroomEntity.Type type;
	
	private MooshroomPredicate(MooshroomEntity.Type type) {
		this.type = type;
	}
	
	public static MooshroomPredicate of(MooshroomEntity.Type color) {
		return new MooshroomPredicate(color);
	}
	
	public static MooshroomPredicate fromJson(JsonObject json) {
		return new MooshroomPredicate(MooshroomEntity.Type.valueOf(json.get(TYPE_KEY).getAsString().toUpperCase(Locale.ROOT)));
	}
	
	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(TYPE_KEY, new JsonPrimitive(this.type.toString().toLowerCase(Locale.ROOT)));
		return jsonObject;
	}
	
	@Override
	public Deserializer getDeserializer() {
		return SpectrumTypeSpecificPredicates.MOOSHROOM;
	}
	
	@Override
	public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
		if (!(entity instanceof MooshroomEntity mooshroomEntity)) {
			return false;
		} else {
			return this.type == mooshroomEntity.getMooshroomType();
		}
	}
}
