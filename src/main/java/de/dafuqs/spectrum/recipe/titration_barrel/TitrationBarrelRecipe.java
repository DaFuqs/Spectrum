package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TitrationBarrelRecipe implements ITitrationBarrelRecipe {
	
	protected final Identifier id;
	protected final String group;
	
	protected final List<IngredientStack> inputStacks;
	protected final ItemStack outputItemStack;
	
	protected final int minTimeDays;
	protected final FermentationData fermentationData;
	
	protected final Identifier requiredAdvancementIdentifier;
	
	// data holders
	public record StatusEffectPotencyEntry(int minAlcPercent, int minThickness, int potency) {
		public static StatusEffectPotencyEntry fromJson(JsonObject jsonObject) {
			int minAlcPercent = JsonHelper.getInt(jsonObject, "min_alc", 0);
			int minThickness = JsonHelper.getInt(jsonObject, "min_thickness", 0);
			int potency = JsonHelper.getInt(jsonObject, "potency", 0);
			return new StatusEffectPotencyEntry(minAlcPercent, minThickness, potency);
		}
		
		public void write(PacketByteBuf packetByteBuf) {
			packetByteBuf.writeInt(this.minAlcPercent);
			packetByteBuf.writeInt(this.minThickness);
			packetByteBuf.writeInt(this.potency);
		}
		
		public static StatusEffectPotencyEntry read(PacketByteBuf packetByteBuf) {
			return new StatusEffectPotencyEntry(packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readInt());
		}
	}
	public record StatusEffectEntry(StatusEffect statusEffect, List<StatusEffectPotencyEntry> potencyEntries) {
		public static StatusEffectEntry fromJson(JsonObject jsonObject) {
			Identifier statusEffectIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "id"));
			StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectIdentifier);
			List<StatusEffectPotencyEntry> potencyEntries = new ArrayList<>();
			if(JsonHelper.hasArray(jsonObject, "potency")) {
				JsonArray potencyArray = JsonHelper.getArray(jsonObject, "potency");
				for (int i = 0; i < potencyArray.size(); i++) {
					JsonObject object = potencyArray.get(i).getAsJsonObject();
					potencyEntries.add(StatusEffectPotencyEntry.fromJson(object));
				}
			}
			return new StatusEffectEntry(statusEffect, potencyEntries);
		}
		
		public void write(PacketByteBuf packetByteBuf) {
			packetByteBuf.writeString(this.statusEffect.toString());
			packetByteBuf.writeInt(this.potencyEntries.size());
			for(StatusEffectPotencyEntry potencyEntry : this.potencyEntries) {
				potencyEntry.write(packetByteBuf);
			}
		}
		
		public static StatusEffectEntry read(PacketByteBuf packetByteBuf) {
			Identifier statusEffectIdentifier = Identifier.tryParse(packetByteBuf.readString());
			StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectIdentifier);
			int potencyEntryCount = packetByteBuf.readInt();
			List<StatusEffectPotencyEntry> potencyEntries = new ArrayList<>(potencyEntryCount);
			for(int i = 0; i < potencyEntryCount; i++) {
				potencyEntries.add(StatusEffectPotencyEntry.read(packetByteBuf));
			}
			return new StatusEffectEntry(statusEffect, potencyEntries);
		}
	}
	public record FermentationData(float fermentationSpeedMod, List<StatusEffectEntry> statusEffectEntries) {
		public static FermentationData fromJson(JsonObject jsonObject) {
			float fermentationSpeedMod = JsonHelper.getFloat(jsonObject, "fermentation_speed_mod", 1.0F);
			List<StatusEffectEntry> statusEffectEntries = new ArrayList<>();
			if(JsonHelper.hasArray(jsonObject, "effects")) {
				JsonArray effectsArray = JsonHelper.getArray(jsonObject, "effects");
				for (int i = 0; i < effectsArray.size(); i++) {
					JsonObject object = effectsArray.get(i).getAsJsonObject();
					statusEffectEntries.add(StatusEffectEntry.fromJson(object));
				}
			}
			return new FermentationData(fermentationSpeedMod, statusEffectEntries);
		}
		
		public void write(PacketByteBuf packetByteBuf) {
			packetByteBuf.writeFloat(this.fermentationSpeedMod);
			packetByteBuf.writeInt(this.statusEffectEntries.size());
			for(StatusEffectEntry statusEffectEntry : this.statusEffectEntries) {
				statusEffectEntry.write(packetByteBuf);
			}
		}
		
		public static FermentationData read(PacketByteBuf packetByteBuf) {
			float fermentationSpeedMod = packetByteBuf.readFloat();
			int statusEffectEntryCount = packetByteBuf.readInt();
			List<StatusEffectEntry> statusEffectEntries = new ArrayList<>(statusEffectEntryCount);
			for(int i = 0; i < statusEffectEntryCount; i++) {
				statusEffectEntries.add(StatusEffectEntry.read(packetByteBuf));
			}
			return new FermentationData(fermentationSpeedMod, statusEffectEntries);
		}
	}
	
	public TitrationBarrelRecipe(Identifier id, String group, List<IngredientStack> inputStacks, ItemStack outputItemStack, int minTimeDays, FermentationData fermentationData, Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		
		this.inputStacks = inputStacks;
		this.minTimeDays = minTimeDays;
		this.outputItemStack = outputItemStack;
		this.fermentationData = fermentationData;
		
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		
		registerInToastManager(SpectrumRecipeTypes.TITRATION_BARREL, this);
	}
	
	@Override
	public Identifier getRequiredAdvancementIdentifier() {
		return requiredAdvancementIdentifier;
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		return FusionShrineRecipe.matchIngredientStacksExclusively(inventory, getIngredientStacks());
	}
	
	// should not be used. Instead use getIngredientStacks(), which includes item counts
	@Override
	@Deprecated
	public DefaultedList<Ingredient> getIngredients() {
		return IngredientStack.listIngredients(this.inputStacks);
	}
	
	public List<IngredientStack> getIngredientStacks() {
		return this.inputStacks;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getOutput() {
		return outputItemStack;
	}
	
	@Override
	public ItemStack tap(DefaultedList<ItemStack> content, int waterBuckets, long secondsFermented, float downfall, float temperature) {
		return null; // TODO
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.TITRATION_BARREL_RECIPE_SERIALIZER;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof TitrationBarrelRecipe titrationBarrelRecipe) {
			return titrationBarrelRecipe.getId().equals(this.getId());
		}
		return false;
	}
	
}
