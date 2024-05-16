package de.dafuqs.spectrum.entity.type_specific_predicates;

import com.google.gson.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.variants.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class KindlingPredicate implements TypeSpecificPredicate {
	
	private static final String CLIPPED_KEY = "clipped";
	private static final String ANGRY_KEY = "angry";
	private static final String VARIANT_KEY = "variant";

	private final Optional<Boolean> clipped;
	private final Optional<Boolean> angry;
	private final Optional<KindlingVariant> variant;

	private KindlingPredicate(Optional<Boolean> clipped, Optional<Boolean> angry, Optional<KindlingVariant> variant) {
		this.clipped = clipped;
		this.angry = angry;
		this.variant = variant;
	}

	public static KindlingPredicate of(Optional<Boolean> clipped, Optional<Boolean> angry, Optional<KindlingVariant> variant) {
		return new KindlingPredicate(clipped, angry, variant);
	}
	
	public static KindlingPredicate fromJson(JsonObject json) {
		JsonElement clippedElement = json.get(CLIPPED_KEY);
		Optional<Boolean> clippedOptional = clippedElement == null ? Optional.empty() : Optional.of(clippedElement.getAsBoolean());
		JsonElement angryElement = json.get(ANGRY_KEY);
		Optional<Boolean> angryOptional = angryElement == null ? Optional.empty() : Optional.of(angryElement.getAsBoolean());
		JsonElement variantElement = json.get(VARIANT_KEY);
		Optional<KindlingVariant> variantOptional = variantElement == null ? Optional.empty() : SpectrumRegistries.KINDLING_VARIANT.getOrEmpty(Identifier.tryParse(variantElement.getAsString()));
		return new KindlingPredicate(clippedOptional, angryOptional, variantOptional);
	}
	
	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		this.clipped.ifPresent(clipped -> jsonObject.add(CLIPPED_KEY, new JsonPrimitive(clipped)));
		this.angry.ifPresent(angry -> jsonObject.add(ANGRY_KEY, new JsonPrimitive(angry)));
		this.variant.ifPresent(variant -> jsonObject.add(VARIANT_KEY, new JsonPrimitive(SpectrumRegistries.KINDLING_VARIANT.getId(variant).toString())));
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
					&& (this.angry.isEmpty() || this.angry.get() == (kindling.getAngerTime() == 0)
					&& (this.variant.isEmpty() || this.variant.get() == kindling.getKindlingVariant()));
		}
	}
	
}
