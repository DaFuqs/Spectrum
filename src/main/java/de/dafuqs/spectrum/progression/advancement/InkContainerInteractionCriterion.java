package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.PigmentColors;
import de.dafuqs.spectrum.energy.storage.PigmentEnergyStorage;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InkContainerInteractionCriterion extends AbstractCriterion<InkContainerInteractionCriterion.Conditions> {

	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "ink_container_interaction");

	public Identifier getId() {
		return ID;
	}

	public InkContainerInteractionCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		LongRange blackRange = LongRange.fromJson(jsonObject.get("black"));
		LongRange blueRange = LongRange.fromJson(jsonObject.get("blue"));
		LongRange brownRange = LongRange.fromJson(jsonObject.get("brown"));
		LongRange cyanRange = LongRange.fromJson(jsonObject.get("cyan"));
		LongRange grayRange = LongRange.fromJson(jsonObject.get("gray"));
		LongRange greenRange = LongRange.fromJson(jsonObject.get("green"));
		LongRange lightBlueRange = LongRange.fromJson(jsonObject.get("light_blue"));
		LongRange lightGrayRange = LongRange.fromJson(jsonObject.get("light_gray"));
		LongRange limeRange = LongRange.fromJson(jsonObject.get("lime"));
		LongRange magentaRange = LongRange.fromJson(jsonObject.get("magenta"));
		LongRange orangeRange = LongRange.fromJson(jsonObject.get("orange"));
		LongRange pinkRange = LongRange.fromJson(jsonObject.get("pink"));
		LongRange purpleRange = LongRange.fromJson(jsonObject.get("purple"));
		LongRange redRange = LongRange.fromJson(jsonObject.get("red"));
		LongRange whiteRange = LongRange.fromJson(jsonObject.get("white"));
		LongRange yellowRange = LongRange.fromJson(jsonObject.get("yellow"));
		
		ColorPredicate changeColor = ColorPredicate.fromJson(jsonObject.get("change_color"));
		LongRange changeRange = LongRange.fromJson(jsonObject.get("change_range"));
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
		return new InkContainerInteractionCriterion.Conditions(extended, itemPredicate, blackRange, blueRange, brownRange, cyanRange, grayRange, greenRange, lightBlueRange, lightGrayRange, limeRange, magentaRange, orangeRange, pinkRange, purpleRange, redRange, whiteRange, yellowRange, changeColor, changeRange);
	}

	public void trigger(ServerPlayerEntity player, ItemStack stack, PigmentEnergyStorage storage, CMYKColor changeColor, long changeAmount) {
		this.trigger(player, (conditions) -> conditions.matches(
				stack,
				storage.getEnergy(PigmentColors.BLACK),
				storage.getEnergy(PigmentColors.BLUE),
				storage.getEnergy(PigmentColors.BROWN),
				storage.getEnergy(PigmentColors.CYAN),
				storage.getEnergy(PigmentColors.GRAY),
				storage.getEnergy(PigmentColors.GREEN),
				storage.getEnergy(PigmentColors.LIGHT_BLUE),
				storage.getEnergy(PigmentColors.LIGHT_GRAY),
				storage.getEnergy(PigmentColors.LIME),
				storage.getEnergy(PigmentColors.MAGENTA),
				storage.getEnergy(PigmentColors.ORANGE),
				storage.getEnergy(PigmentColors.PINK),
				storage.getEnergy(PigmentColors.PURPLE),
				storage.getEnergy(PigmentColors.RED),
				storage.getEnergy(PigmentColors.WHITE),
				storage.getEnergy(PigmentColors.YELLOW),
				changeColor,
				changeAmount
		));
	}

	public static InkContainerInteractionCriterion.Conditions create(ItemPredicate itemPredicate, LongRange blackRange, LongRange blueRange, LongRange brownRange, LongRange cyanRange, LongRange grayRange, LongRange greenRange, LongRange lightBlueRange, LongRange lightGrayRange, LongRange limeRange, LongRange magentaRange, LongRange orangeRange, LongRange pinkRange, LongRange purpleRange, LongRange redRange, LongRange whiteRange, LongRange yellowRange, ColorPredicate changeColor, LongRange changeRange) {
		return new InkContainerInteractionCriterion.Conditions(EntityPredicate.Extended.EMPTY, itemPredicate, blackRange, blueRange, brownRange, cyanRange, grayRange, greenRange, lightBlueRange, lightGrayRange, limeRange, magentaRange, orangeRange, pinkRange, purpleRange, redRange, whiteRange, yellowRange, changeColor, changeRange);
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate itemPredicate;
		
		private final LongRange blackRange;
		private final LongRange blueRange;
		private final LongRange brownRange;
		private final LongRange cyanRange;
		private final LongRange grayRange;
		private final LongRange greenRange;
		private final LongRange lightBlueRange;
		private final LongRange lightGrayRange;
		private final LongRange limeRange;
		private final LongRange magentaRange;
		private final LongRange orangeRange;
		private final LongRange pinkRange;
		private final LongRange purpleRange;
		private final LongRange redRange;
		private final LongRange whiteRange;
		private final LongRange yellowRange;
		
		private final ColorPredicate changeColor;
		private final LongRange changeRange;

		public Conditions(EntityPredicate.Extended player, ItemPredicate itemPredicate, LongRange blackRange, LongRange blueRange, LongRange brownRange, LongRange cyanRange, LongRange grayRange, LongRange greenRange, LongRange lightBlueRange, LongRange lightGrayRange, LongRange limeRange, LongRange magentaRange, LongRange orangeRange, LongRange pinkRange, LongRange purpleRange, LongRange redRange, LongRange whiteRange, LongRange yellowRange, ColorPredicate changeColor, LongRange changeRange) {
			super(ID, player);
			this.itemPredicate = itemPredicate;
			
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
			
			this.changeColor = changeColor;
			this.changeRange = changeRange;
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("item", this.itemPredicate.toString());
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
			jsonObject.addProperty("change_color", this.changeColor.toString());
			jsonObject.addProperty("change_range", this.changeRange.toString());
			return jsonObject;
		}

		public boolean matches(ItemStack stack, long black, long blue, long brown, long cyan, long gray, long green, long lightBlue, long lightGray, long lime, long magenta, long o, long pink, long purple, long red, long white, long yellow, CMYKColor color, long change) {
			return itemPredicate.test(stack) && changeRange.test(change) && changeColor.test(color)
					&& blackRange.test(black) && blueRange.test(blue) && brownRange.test(brown)
					&& cyanRange.test(cyan) && grayRange.test(gray) && greenRange.test(green)
					&& lightBlueRange.test(lightBlue) && lightGrayRange.test(lightGray) && limeRange.test(lime)
					&& magentaRange.test(magenta) && orangeRange.test(o) && pinkRange.test(pink)
					&& purpleRange.test(purple) && redRange.test(red) && whiteRange.test(white) && yellowRange.test(yellow);

		}
	}

}