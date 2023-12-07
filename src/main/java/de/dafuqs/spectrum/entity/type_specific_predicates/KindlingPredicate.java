package de.dafuqs.spectrum.entity.type_specific_predicates;

import com.google.gson.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class KindlingPredicate implements TypeSpecificPredicate {
	
	private static final String CLIPPED_KEY = "clipped";
	private static final String ANGRY_KEY = "angry";
	
	private final Optional<Boolean> clipped;
	private final Optional<Boolean> angry;
	
	private KindlingPredicate(Optional<Boolean> clipped, Optional<Boolean> angry) {
		this.clipped = clipped;
		this.angry = angry;
	}
	
	public static KindlingPredicate of(Optional<Boolean> clipped, Optional<Boolean> angry) {
		return new KindlingPredicate(clipped, angry);
	}
	
	public static KindlingPredicate fromJson(JsonObject json) {
		JsonElement clippedElement = json.get(CLIPPED_KEY);
		Optional<Boolean> clippedOptional = clippedElement == null ? Optional.empty() : Optional.of(clippedElement.getAsBoolean());
		JsonElement angryElement = json.get(ANGRY_KEY);
		Optional<Boolean> angryOptional = angryElement == null ? Optional.empty() : Optional.of(angryElement.getAsBoolean());
		return new KindlingPredicate(clippedOptional, angryOptional);
	}
	
	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		this.clipped.ifPresent(aBoolean -> jsonObject.add(CLIPPED_KEY, new JsonPrimitive(aBoolean)));
		this.angry.ifPresent(aBoolean -> jsonObject.add(ANGRY_KEY, new JsonPrimitive(aBoolean)));
		return jsonObject;
	}
	
	@Override
	public Deserializer getDeserializer() {
		return SpectrumTypeSpecificPredicates.KINDLING;
	}
	
	@Override
	public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
		if (!(entity instanceof KindlingEntity kindling)) {
			return false;
		} else {
			return (this.clipped.isEmpty() || this.clipped.get() == kindling.isClipped())
					&& (this.angry.isEmpty() || this.angry.get() == (kindling.getAngerTime() == 0));
		}
	}
	
}
