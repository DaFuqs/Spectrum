package de.dafuqs.spectrum.entity.type_specific_predicates;

import com.google.gson.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ShulkerPredicate implements TypeSpecificPredicate {
	
	private static final String COLOR_KEY = "color";
	
	private final Optional<DyeColor> color;
	
	private ShulkerPredicate(Optional<DyeColor> color) {
		this.color = color;
	}
	
	public static ShulkerPredicate of(Optional<DyeColor> color) {
		return new ShulkerPredicate(color);
	}
	
	public static ShulkerPredicate fromJson(JsonObject json) {
		String colorString = json.get(COLOR_KEY).getAsString();
		if (colorString.isBlank()) {
			return new ShulkerPredicate(Optional.empty());
		}
		return new ShulkerPredicate(Optional.of(DyeColor.valueOf(colorString.toUpperCase(Locale.ROOT))));
	}
	
	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(COLOR_KEY, new JsonPrimitive(this.color.toString().toLowerCase(Locale.ROOT)));
		return jsonObject;
	}
	
	@Override
	public Deserializer getDeserializer() {
		return SpectrumTypeSpecificPredicates.SHULKER;
	}
	
	@Override
	public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
		if (!(entity instanceof ShulkerEntity shulkerEntity)) {
			return false;
		} else {
			return this.color.equals(shulkerEntity.getVariant());
		}
	}
	
}
