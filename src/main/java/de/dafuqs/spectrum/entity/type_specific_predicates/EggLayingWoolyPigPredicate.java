package de.dafuqs.spectrum.entity.type_specific_predicates;

import com.google.gson.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EggLayingWoolyPigPredicate implements TypeSpecificPredicate {
	
	private static final String COLOR_KEY = "color";
	private static final String HATLESS_KEY = "hatless";
	private static final String SHEARED_KEY = "sheared";

	private final Optional<DyeColor> color;
	private final Optional<Boolean> hatless;
	private final Optional<Boolean> sheared;

	private EggLayingWoolyPigPredicate(Optional<DyeColor> color, Optional<Boolean> hatless, Optional<Boolean> sheared) {
		this.color = color;
		this.hatless = hatless;
		this.sheared = sheared;
	}

	public static EggLayingWoolyPigPredicate of(Optional<DyeColor> color, Optional<Boolean> hatless, Optional<Boolean> sheared) {
		return new EggLayingWoolyPigPredicate(color, hatless, sheared);
	}
	
	public static EggLayingWoolyPigPredicate fromJson(JsonObject json) {
		JsonElement colorElement = json.get(COLOR_KEY);
		Optional<DyeColor> color = colorElement == null ? Optional.empty() : Optional.of(DyeColor.valueOf(json.get(COLOR_KEY).getAsString().toUpperCase(Locale.ROOT)));

		JsonElement hatlessElement = json.get(HATLESS_KEY);
		Optional<Boolean> hatless = hatlessElement == null ? Optional.empty() : Optional.of(hatlessElement.getAsBoolean());

		JsonElement shearedElement = json.get(SHEARED_KEY);
		Optional<Boolean> sheared = shearedElement == null ? Optional.empty() : Optional.of(shearedElement.getAsBoolean());

		return new EggLayingWoolyPigPredicate(color, hatless, sheared);
	}
	
	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		this.hatless.ifPresent(clipped -> jsonObject.add(HATLESS_KEY, new JsonPrimitive(clipped)));
		this.sheared.ifPresent(angry -> jsonObject.add(SHEARED_KEY, new JsonPrimitive(angry)));
		jsonObject.add(COLOR_KEY, new JsonPrimitive(this.color.toString().toLowerCase(Locale.ROOT)));
		return jsonObject;
	}
	
	@Override
	public Deserializer getDeserializer() {
		return SpectrumTypeSpecificPredicates.EGG_LAYING_WOOLY_PIG;
	}
	
	@Override
	public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
		if (!(entity instanceof EggLayingWoolyPigEntity wooly)) {
			return false;
		} else {
			return (this.color.isEmpty() || this.color.get() == wooly.getColor())
					&& (this.hatless.isEmpty() || this.hatless.get() == wooly.isHatless())
					&& (this.sheared.isEmpty() || this.sheared.get() == wooly.isSheared());
		}
	}
}
