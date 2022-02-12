package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.trinkets.PigmentPaletteItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PigmentPaletteUseCriterion extends AbstractCriterion<PigmentPaletteUseCriterion.Conditions> {

	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "pigment_palette_use");

	public Identifier getId() {
		return ID;
	}

	public PigmentPaletteUseCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		NumberRange.IntRange blackRange = NumberRange.IntRange.fromJson(jsonObject.get("black"));
		NumberRange.IntRange blueRange = NumberRange.IntRange.fromJson(jsonObject.get("blue"));
		NumberRange.IntRange brownRange = NumberRange.IntRange.fromJson(jsonObject.get("brown"));
		NumberRange.IntRange cyanRange = NumberRange.IntRange.fromJson(jsonObject.get("cyan"));
		NumberRange.IntRange grayRange = NumberRange.IntRange.fromJson(jsonObject.get("gray"));
		NumberRange.IntRange greenRange = NumberRange.IntRange.fromJson(jsonObject.get("green"));
		NumberRange.IntRange lightBlueRange = NumberRange.IntRange.fromJson(jsonObject.get("light_blue"));
		NumberRange.IntRange lightGrayRange = NumberRange.IntRange.fromJson(jsonObject.get("light_gray"));
		NumberRange.IntRange limeRange = NumberRange.IntRange.fromJson(jsonObject.get("lime"));
		NumberRange.IntRange magentaRange = NumberRange.IntRange.fromJson(jsonObject.get("magenta"));
		NumberRange.IntRange orangeRange = NumberRange.IntRange.fromJson(jsonObject.get("orange"));
		NumberRange.IntRange pinkRange = NumberRange.IntRange.fromJson(jsonObject.get("pink"));
		NumberRange.IntRange purpleRange = NumberRange.IntRange.fromJson(jsonObject.get("purple"));
		NumberRange.IntRange redRange = NumberRange.IntRange.fromJson(jsonObject.get("red"));
		NumberRange.IntRange whiteRange = NumberRange.IntRange.fromJson(jsonObject.get("white"));
		NumberRange.IntRange yellowRange = NumberRange.IntRange.fromJson(jsonObject.get("yellow"));
		
		NumberRange.IntRange changeRange = NumberRange.IntRange.fromJson(jsonObject.get("change"));
		return new PigmentPaletteUseCriterion.Conditions(extended, blackRange, blueRange, brownRange, cyanRange, grayRange, greenRange, lightBlueRange, lightGrayRange, limeRange, magentaRange, orangeRange, pinkRange, purpleRange, redRange, whiteRange, yellowRange, changeRange);
	}

	public void trigger(ServerPlayerEntity player, int change) {
		this.trigger(player, (conditions) -> {
			Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
			if(component.isPresent()) {
				List<Pair<SlotReference, ItemStack>> equipped = component.get().getEquipped(SpectrumItems.PIGMENT_PALETTE);
				ItemStack firstPalette = equipped.get(0).getRight();
				
				HashMap<DyeColor, Integer> storedEnergy = PigmentPaletteItem.getStoredEnergy(firstPalette);
				return conditions.matches(storedEnergy.get(DyeColor.BLACK),storedEnergy.get(DyeColor.BLUE),storedEnergy.get(DyeColor.BROWN),storedEnergy.get(DyeColor.CYAN),storedEnergy.get(DyeColor.GRAY),storedEnergy.get(DyeColor.GREEN),storedEnergy.get(DyeColor.LIGHT_BLUE),storedEnergy.get(DyeColor.LIGHT_GRAY),storedEnergy.get(DyeColor.LIME),storedEnergy.get(DyeColor.MAGENTA),storedEnergy.get(DyeColor.ORANGE),storedEnergy.get(DyeColor.PINK),storedEnergy.get(DyeColor.PURPLE),storedEnergy.get(DyeColor.RED),storedEnergy.get(DyeColor.WHITE),storedEnergy.get(DyeColor.YELLOW), change);
			}
			return false;
		});
	}

	public static PigmentPaletteUseCriterion.Conditions create(NumberRange.IntRange blackRange, NumberRange.IntRange blueRange, NumberRange.IntRange brownRange, NumberRange.IntRange cyanRange, NumberRange.IntRange grayRange, NumberRange.IntRange greenRange, NumberRange.IntRange lightBlueRange, NumberRange.IntRange lightGrayRange, NumberRange.IntRange limeRange, NumberRange.IntRange magentaRange, NumberRange.IntRange orangeRange, NumberRange.IntRange pinkRange, NumberRange.IntRange purpleRange, NumberRange.IntRange redRange, NumberRange.IntRange whiteRange, NumberRange.IntRange yellowRange, NumberRange.IntRange changeRange) {
		return new PigmentPaletteUseCriterion.Conditions(EntityPredicate.Extended.EMPTY, blackRange, blueRange, brownRange, cyanRange, grayRange, greenRange, lightBlueRange, lightGrayRange, limeRange, magentaRange, orangeRange, pinkRange, purpleRange, redRange, whiteRange, yellowRange, changeRange);
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final NumberRange.IntRange blackRange;
		private final NumberRange.IntRange blueRange;
		private final NumberRange.IntRange brownRange;
		private final NumberRange.IntRange cyanRange;
		private final NumberRange.IntRange grayRange;
		private final NumberRange.IntRange greenRange;
		private final NumberRange.IntRange lightBlueRange;
		private final NumberRange.IntRange lightGrayRange;
		private final NumberRange.IntRange limeRange;
		private final NumberRange.IntRange magentaRange;
		private final NumberRange.IntRange orangeRange;
		private final NumberRange.IntRange pinkRange;
		private final NumberRange.IntRange purpleRange;
		private final NumberRange.IntRange redRange;
		private final NumberRange.IntRange whiteRange;
		private final NumberRange.IntRange yellowRange;
		
		private final NumberRange.IntRange changeRange;

		public Conditions(EntityPredicate.Extended player, NumberRange.IntRange blackRange, NumberRange.IntRange blueRange, NumberRange.IntRange brownRange, NumberRange.IntRange cyanRange, NumberRange.IntRange grayRange, NumberRange.IntRange greenRange, NumberRange.IntRange lightBlueRange, NumberRange.IntRange lightGrayRange, NumberRange.IntRange limeRange, NumberRange.IntRange magentaRange, NumberRange.IntRange orangeRange, NumberRange.IntRange pinkRange, NumberRange.IntRange purpleRange, NumberRange.IntRange redRange, NumberRange.IntRange whiteRange, NumberRange.IntRange yellowRange, NumberRange.IntRange changeRange) {
			super(ID, player);
			this.blackRange = blackRange;
			this.blueRange = blueRange;
			this.brownRange = brownRange;
			this.cyanRange = cyanRange;
			this.grayRange = grayRange;
			this.greenRange = greenRange;
			this.lightBlueRange = lightBlueRange;
			this.lightGrayRange = lightGrayRange;
			this.limeRange = limeRange;
			this.magentaRange = magentaRange;
			this.orangeRange = orangeRange;
			this.pinkRange = pinkRange;
			this.purpleRange = purpleRange;
			this.redRange = redRange;
			this.whiteRange = whiteRange;
			this.yellowRange = yellowRange;
			
			this.changeRange = changeRange;
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("black", this.blackRange.toString());
			jsonObject.addProperty("blue", this.blueRange.toString());
			jsonObject.addProperty("brown", this.brownRange.toString());
			jsonObject.addProperty("cyan", this.cyanRange.toString());
			jsonObject.addProperty("gray", this.grayRange.toString());
			jsonObject.addProperty("green", this.greenRange.toString());
			jsonObject.addProperty("lightBlue", this.lightBlueRange.toString());
			jsonObject.addProperty("lightGray", this.lightGrayRange.toString());
			jsonObject.addProperty("lime", this.limeRange.toString());
			jsonObject.addProperty("magenta", this.magentaRange.toString());
			jsonObject.addProperty("orange", this.orangeRange.toString());
			jsonObject.addProperty("pink", this.pinkRange.toString());
			jsonObject.addProperty("purple", this.purpleRange.toString());
			jsonObject.addProperty("red", this.redRange.toString());
			jsonObject.addProperty("white", this.whiteRange.toString());
			jsonObject.addProperty("yellow", this.yellowRange.toString());
			jsonObject.addProperty("change", this.changeRange.toString());
			return jsonObject;
		}

		public boolean matches(int black, int blue, int brown, int cyan, int gray, int green, int lightBlue, int lightGray, int lime, int magenta, int o, int pink, int purple, int red, int white, int yellow, int change) {
			return blackRange.test(black) && blueRange.test(blue) && brownRange.test(brown)
					&& cyanRange.test(cyan) && grayRange.test(gray) && greenRange.test(green)
					&& lightBlueRange.test(lightBlue) && lightGrayRange.test(lightGray) && limeRange.test(lime)
					&& magentaRange.test(magenta) && orangeRange.test(o) && pinkRange.test(pink)
					&& purpleRange.test(purple) && redRange.test(red) && whiteRange.test(white) && yellowRange.test(yellow)
					&& changeRange.test(change);

		}
	}

}
