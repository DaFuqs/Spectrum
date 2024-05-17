package de.dafuqs.spectrum.entity.type_specific_predicates;

import com.google.gson.*;
import de.dafuqs.spectrum.api.energy.color.*;
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

public class LizardPredicate implements TypeSpecificPredicate {

	private static final String COLOR_KEY = "color";
	private static final String FRILLS_VARIANT_KEY = "frills_variant";
	private static final String HORN_VARIANT_KEY = "horn_variant";

	private final Optional<InkColor> color;
	private final Optional<LizardFrillVariant> frills;
	private final Optional<LizardHornVariant> horns;

	private LizardPredicate(Optional<InkColor> color, Optional<LizardFrillVariant> frills, Optional<LizardHornVariant> horns) {
		this.color = color;
		this.frills = frills;
		this.horns = horns;
	}

	public static LizardPredicate of(Optional<InkColor> color, Optional<LizardFrillVariant> frills, Optional<LizardHornVariant> horns) {
		return new LizardPredicate(color, frills, horns);
	}

	public static LizardPredicate fromJson(JsonObject json) {
		JsonElement colorElement = json.get(COLOR_KEY);
		Optional<InkColor> color = colorElement == null ? Optional.empty() : Optional.of(InkColor.of(json.get(COLOR_KEY).getAsString().toUpperCase(Locale.ROOT)));

		JsonElement frillsElement = json.get(HORN_VARIANT_KEY);
		Optional<LizardFrillVariant> frills = frillsElement == null ? Optional.empty() : SpectrumRegistries.LIZARD_FRILL_VARIANT.getOrEmpty(Identifier.tryParse(frillsElement.getAsString()));

		JsonElement hornsElement = json.get(HORN_VARIANT_KEY);
		Optional<LizardHornVariant> horns = hornsElement == null ? Optional.empty() : SpectrumRegistries.LIZARD_HORN_VARIANT.getOrEmpty(Identifier.tryParse(hornsElement.getAsString()));

		return new LizardPredicate(color, frills, horns);
	}

	@Override
	public JsonObject typeSpecificToJson() {
		JsonObject jsonObject = new JsonObject();
		this.color.ifPresent(color -> jsonObject.add(COLOR_KEY, new JsonPrimitive(color.toString())));
		this.frills.ifPresent(frills -> jsonObject.add(FRILLS_VARIANT_KEY, new JsonPrimitive(SpectrumRegistries.LIZARD_FRILL_VARIANT.getId(frills).toString())));
		this.horns.ifPresent(horns -> jsonObject.add(HORN_VARIANT_KEY, new JsonPrimitive(SpectrumRegistries.LIZARD_HORN_VARIANT.getId(horns).toString())));
		return jsonObject;
	}

	@Override
	public Deserializer getDeserializer() {
		return SpectrumTypeSpecificPredicates.LIZARD;
	}

	@Override
	public boolean test(Entity entity, ServerWorld world, @Nullable Vec3d pos) {
		if (!(entity instanceof LizardEntity lizard)) {
			return false;
		} else {
			return (this.color.isEmpty() || this.color.get() == lizard.getColor())
					&& (this.frills.isEmpty() || this.frills.get() == lizard.getFrills())
					&& (this.horns.isEmpty() || this.horns.get() == lizard.getHorns());
		}
	}

}
