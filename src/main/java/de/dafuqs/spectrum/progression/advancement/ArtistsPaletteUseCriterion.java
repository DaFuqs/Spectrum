package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.PigmentColors;
import de.dafuqs.spectrum.energy.storage.PigmentPaletteEnergyStorage;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ArtistsPaletteUseCriterion extends AbstractCriterion<ArtistsPaletteUseCriterion.Conditions> {

	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "artists_palette_use");

	public Identifier getId() {
		return ID;
	}

	public ArtistsPaletteUseCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		LongRange blackRange = LongRange.fromJson(jsonObject.get("black"));
		LongRange cyanRange = LongRange.fromJson(jsonObject.get("cyan"));
		LongRange magentaRange = LongRange.fromJson(jsonObject.get("magenta"));
		LongRange whiteRange = LongRange.fromJson(jsonObject.get("white"));
		LongRange yellowRange = LongRange.fromJson(jsonObject.get("yellow"));
		
		LongRange changeRange = LongRange.fromJson(jsonObject.get("change"));
		return new ArtistsPaletteUseCriterion.Conditions(extended, blackRange, cyanRange, magentaRange, whiteRange, yellowRange, changeRange);
	}

	public void trigger(ServerPlayerEntity player, PigmentPaletteEnergyStorage storage, long change) {
		this.trigger(player, (conditions) -> conditions.matches(
				storage.getEnergy(PigmentColors.BLACK),
				storage.getEnergy(PigmentColors.CYAN),
				storage.getEnergy(PigmentColors.MAGENTA),
				storage.getEnergy(PigmentColors.WHITE),
				storage.getEnergy(PigmentColors.YELLOW),
				change
		));
	}

	@Contract("_, _, _, _, _, _ -> new")
	public static ArtistsPaletteUseCriterion.@NotNull Conditions create(LongRange blackRange, LongRange cyanRange, LongRange magentaRange, LongRange whiteRange, LongRange yellowRange, LongRange changeRange) {
		return new ArtistsPaletteUseCriterion.Conditions(EntityPredicate.Extended.EMPTY, blackRange, cyanRange, magentaRange, whiteRange, yellowRange, changeRange);
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final LongRange blackRange;
		private final LongRange cyanRange;
		private final LongRange magentaRange;
		private final LongRange whiteRange;
		private final LongRange yellowRange;
		
		private final LongRange changeRange;

		public Conditions(EntityPredicate.Extended player, LongRange blackRange, LongRange cyanRange, LongRange magentaRange, LongRange whiteRange, LongRange yellowRange, LongRange changeRange) {
			super(ID, player);
			this.blackRange = blackRange;
			this.cyanRange = cyanRange;
			this.magentaRange = magentaRange;
			this.whiteRange = whiteRange;
			this.yellowRange = yellowRange;
			
			this.changeRange = changeRange;
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("black", this.blackRange.toString());
			jsonObject.addProperty("cyan", this.cyanRange.toString());
			jsonObject.addProperty("magenta", this.magentaRange.toString());
			jsonObject.addProperty("white", this.whiteRange.toString());
			jsonObject.addProperty("yellow", this.yellowRange.toString());
			jsonObject.addProperty("change", this.changeRange.toString());
			return jsonObject;
		}

		public boolean matches(long black, long cyan, long magenta, long white, long yellow, long change) {
			return blackRange.test(black) && cyanRange.test(cyan) && magentaRange.test(magenta) && whiteRange.test(white) && yellowRange.test(yellow) && changeRange.test(change);
		}
	}

}
