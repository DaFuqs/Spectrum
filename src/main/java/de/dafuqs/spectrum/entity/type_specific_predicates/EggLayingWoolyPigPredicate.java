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
	
	private final DyeColor color;
	
	private EggLayingWoolyPigPredicate(DyeColor color) {
		this.color = color;
	}
	
	public static EggLayingWoolyPigPredicate of(DyeColor color) {
		return new EggLayingWoolyPigPredicate(color);
	}
	
	public static EggLayingWoolyPigPredicate fromJson(JsonObject json) {
		return new EggLayingWoolyPigPredicate(DyeColor.valueOf(json.get(COLOR_KEY).getAsString().toUpperCase(Locale.ROOT)));
	}
	
	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(COLOR_KEY, new JsonPrimitive(this.color.toString().toLowerCase(Locale.ROOT)));
		return jsonObject;
	}
	
	@Override
	public Deserializer getDeserializer() {
		return SpectrumTypeSpecificPredicates.EGG_LAYING_WOOLY_PIG;
	}
	
	@Override
	public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
		if (!(entity instanceof EggLayingWoolyPigEntity eggLayingWoolyPigEntity)) {
			return false;
		} else {
			return this.color == eggLayingWoolyPigEntity.getColor();
		}
	}
}
