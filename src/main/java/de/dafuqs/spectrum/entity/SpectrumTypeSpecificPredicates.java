package de.dafuqs.spectrum.entity;

import com.google.common.collect.*;
import com.google.gson.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.entity.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumTypeSpecificPredicates {
	
	public static final TypeSpecificPredicate.Deserializer EGG_LAYING_WOOLY_PIG_COLOR = EggLayingWoolyPigColorPredicate::fromJson;
	
	public static void register() {
		BiMap<String, TypeSpecificPredicate.Deserializer> map = TypeSpecificPredicate.Deserializers.TYPES;
		BiMap<String, TypeSpecificPredicate.Deserializer> newMap = HashBiMap.create(map);
		newMap.put("spectrum:egg_laying_wooly_pig", EGG_LAYING_WOOLY_PIG_COLOR);
		TypeSpecificPredicateDeserializerMixin.setTypes(newMap);
	}
	
	public static class EggLayingWoolyPigColorPredicate implements TypeSpecificPredicate {
		
		private static final String COLOR_KEY = "color";
		
		private final DyeColor color;
		
		private EggLayingWoolyPigColorPredicate(DyeColor color) {
			this.color = color;
		}
		
		public static EggLayingWoolyPigColorPredicate of(DyeColor color) {
			return new EggLayingWoolyPigColorPredicate(color);
		}
		
		public static EggLayingWoolyPigColorPredicate fromJson(JsonObject json) {
			return new EggLayingWoolyPigColorPredicate(DyeColor.valueOf(json.get(COLOR_KEY).getAsString().toUpperCase(Locale.ROOT)));
		}
		
		@Override
		public JsonObject typeSpecificToJson() {
			JsonObject jsonObject = new JsonObject();
			jsonObject.add(COLOR_KEY, new JsonPrimitive(this.color.toString().toLowerCase(Locale.ROOT)));
			return jsonObject;
		}
		
		@Override
		public TypeSpecificPredicate.Deserializer getDeserializer() {
			return SpectrumTypeSpecificPredicates.EGG_LAYING_WOOLY_PIG_COLOR;
		}
		
		@Override
		public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
			if (!(entity instanceof EggLayingWoolyPigEntity eggLayingWoolyPigEntity)) {
				return false;
			} else {
				return this.color.equals(eggLayingWoolyPigEntity.getColor());
			}
		}
	}
	
}
