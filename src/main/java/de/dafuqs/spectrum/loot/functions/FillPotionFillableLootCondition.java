package de.dafuqs.spectrum.loot.functions;

import com.google.gson.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.loot.function.*;
import net.minecraft.loot.provider.number.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

import java.util.*;

public class FillPotionFillableLootCondition extends ConditionalLootFunction {
	
	record InkPoweredPotionTemplate(boolean ambient, boolean showParticles, LootNumberProvider duration,
									List<StatusEffect> statusEffects, int color, LootNumberProvider amplifier,
									List<InkColor> inkColors, LootNumberProvider inkCost, boolean unidentifiable, boolean incurable) {
		
		public static InkPoweredPotionTemplate fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
			boolean ambient = JsonHelper.getBoolean(jsonObject, "ambient", false);
			boolean showParticles = JsonHelper.getBoolean(jsonObject, "show_particles", false);
			boolean unidentifiable = JsonHelper.getBoolean(jsonObject, "unidentifiable", false);
			boolean incurable = JsonHelper.getBoolean(jsonObject, "incurable", false);
			LootNumberProvider duration = JsonHelper.deserialize(jsonObject, "duration", jsonDeserializationContext, LootNumberProvider.class);
			Set<StatusEffect> statusEffects = new HashSet<>();
			JsonElement statusEffectElement = jsonObject.get("status_effect");
			if (statusEffectElement instanceof JsonArray jsonArray) {
				for (JsonElement element : jsonArray) {
					statusEffects.add(Registries.STATUS_EFFECT.get(Identifier.tryParse(element.getAsString())));
				}
			} else {
				statusEffects.add(Registries.STATUS_EFFECT.get(Identifier.tryParse(statusEffectElement.getAsString())));
			}
			
			int color = JsonHelper.getInt(jsonObject, "color", -1);
			LootNumberProvider amplifier = JsonHelper.deserialize(jsonObject, "amplifier", jsonDeserializationContext, LootNumberProvider.class);
			LootNumberProvider inkCost = JsonHelper.deserialize(jsonObject, "ink_cost", jsonDeserializationContext, LootNumberProvider.class);
			
			Set<InkColor> inkColors = new HashSet<>();
			JsonElement colorElement = jsonObject.get("ink_color");
			if (colorElement instanceof JsonArray jsonArray) {
				for (JsonElement element : jsonArray) {
					String s = element.getAsString();
					inkColors.add(InkColor.ofIdString(s).orElseThrow());
				}
			} else {
				String s = colorElement.getAsString();
				inkColors.add(InkColor.ofIdString(s).orElseThrow());
			}
			
			return new InkPoweredPotionTemplate(ambient, showParticles, duration, statusEffects.stream().toList(), color, amplifier, inkColors.stream().toList(), inkCost, unidentifiable, incurable);
		}
		
		public void toJson(JsonObject jsonObject, JsonSerializationContext jsonSerializationContext) {
			jsonObject.addProperty("ambient", this.ambient);
			jsonObject.addProperty("show_particles", this.showParticles);
			jsonObject.add("duration", jsonSerializationContext.serialize(this.duration));
			JsonArray statusEffectArray = new JsonArray();
			for (StatusEffect statusEffect : this.statusEffects) {
				statusEffectArray.add(Registries.STATUS_EFFECT.getId(statusEffect).toString());
			}
			jsonObject.add("status_effect", statusEffectArray);
			jsonObject.addProperty("color", this.color);
			jsonObject.addProperty("unidentifiable", this.unidentifiable);
			jsonObject.addProperty("incurable", this.incurable);
			jsonObject.add("amplifier", jsonSerializationContext.serialize(this.amplifier));
			jsonObject.add("ink_cost", jsonSerializationContext.serialize(this.inkCost));
			
			JsonArray inkColorArray = new JsonArray();
			for (InkColor inkColor : this.inkColors) {
				inkColorArray.add(inkColor.getID().toString());
			}
			jsonObject.add("ink_color", inkColorArray);
		}
		
		public InkPoweredStatusEffectInstance get(LootContext context) {
			StatusEffect statusEffect = this.statusEffects.get(context.getRandom().nextInt(this.statusEffects.size()));
			StatusEffectInstance statusEffectInstance = new StatusEffectInstance(statusEffect, this.duration.nextInt(context), this.amplifier.nextInt(context), ambient, showParticles, true);
			InkColor inkColor = this.inkColors.get(context.getRandom().nextInt(this.inkColors.size()));
			int cost = this.inkCost.nextInt(context);
			return new InkPoweredStatusEffectInstance(statusEffectInstance, new InkCost(inkColor, cost), this.color, this.unidentifiable, this.incurable);
		}
		
	}
	
	final InkPoweredPotionTemplate template;
	
	FillPotionFillableLootCondition(LootCondition[] conditions, InkPoweredPotionTemplate template) {
		super(conditions);
		this.template = template;
	}
	
	@Override
	public LootFunctionType getType() {
		return SpectrumLootFunctionTypes.FILL_POTION_FILLABLE;
	}
	
	@Override
	public ItemStack process(ItemStack stack, LootContext context) {
		if (this.template == null) {
			return stack;
		}
		if (!(stack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable)) {
			return stack;
		}
		if (inkPoweredPotionFillable.isFull(stack)) {
			return stack;
		}
		
		InkPoweredStatusEffectInstance effect = template.get(context);
		inkPoweredPotionFillable.addOrUpgradeEffects(stack, List.of(effect));
		
		return stack;
	}
	
	public static ConditionalLootFunction.Builder<?> builder(InkPoweredPotionTemplate template) {
		return builder((conditions) -> new FillPotionFillableLootCondition(conditions, template));
	}
	
	public static class Serializer extends ConditionalLootFunction.Serializer<FillPotionFillableLootCondition> {
		
		@Override
		public void toJson(JsonObject jsonObject, FillPotionFillableLootCondition lootFunction, JsonSerializationContext jsonSerializationContext) {
			super.toJson(jsonObject, lootFunction, jsonSerializationContext);
			lootFunction.template.toJson(jsonObject, jsonSerializationContext);
		}
		
		@Override
		public FillPotionFillableLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
			return new FillPotionFillableLootCondition(lootConditions, InkPoweredPotionTemplate.fromJson(jsonObject, jsonDeserializationContext));
		}
	}
	
}
