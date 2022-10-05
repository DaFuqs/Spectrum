package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.items.food.BeverageItem;
import de.dafuqs.spectrum.items.food.InfusedBeverageItem;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TitrationBarrelRecipe implements ITitrationBarrelRecipe {
	
	public static final float SOLID_TO_WATER_RATIO = 4F;
	public static final ItemStack NOT_FERMENTED_LONG_ENOUGH_OUTPUT_STACK = Items.POTION.getDefaultStack();
	public static final ItemStack PURE_ALCOHOL_STACK = SpectrumItems.PURE_ALCOHOL.getDefaultStack();
	
	protected final Identifier id;
	protected final String group;
	
	protected final List<IngredientStack> inputStacks;
	protected final ItemStack outputItemStack;
	
	protected final int minFermentationTimeHours;
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
	public record FermentationData(double fermentationSpeedMod, List<StatusEffectEntry> statusEffectEntries) {
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
			packetByteBuf.writeDouble(this.fermentationSpeedMod);
			packetByteBuf.writeInt(this.statusEffectEntries.size());
			for(StatusEffectEntry statusEffectEntry : this.statusEffectEntries) {
				statusEffectEntry.write(packetByteBuf);
			}
		}
		
		public static FermentationData read(PacketByteBuf packetByteBuf) {
			double fermentationSpeedMod = packetByteBuf.readDouble();
			int statusEffectEntryCount = packetByteBuf.readInt();
			List<StatusEffectEntry> statusEffectEntries = new ArrayList<>(statusEffectEntryCount);
			for(int i = 0; i < statusEffectEntryCount; i++) {
				statusEffectEntries.add(StatusEffectEntry.read(packetByteBuf));
			}
			return new FermentationData(fermentationSpeedMod, statusEffectEntries);
		}
	}
	
	public TitrationBarrelRecipe(Identifier id, String group, List<IngredientStack> inputStacks, ItemStack outputItemStack, int minFermentationTimeHours, FermentationData fermentationData, Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		
		this.inputStacks = inputStacks;
		this.minFermentationTimeHours = minFermentationTimeHours;
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
	
	// should not be used. Instead, use getIngredientStacks(), which includes item counts
	@Override
	@Deprecated
	public DefaultedList<Ingredient> getIngredients() {
		return IngredientStack.listIngredients(this.inputStacks);
	}
	
	@Override
	public String getGroup() {
		return this.group;
	}
	
	public List<IngredientStack> getIngredientStacks() {
		return this.inputStacks;
	}
	
	@Override
	public int getMinFermentationTimeHours() {
		return this.minFermentationTimeHours;
	}
	
	@Override
	public FermentationData getFermentationData() {
		return this.fermentationData;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getOutput() {
		return tapWith(1.0F, this.minFermentationTimeHours * 60 * 60, 1.0F, 1.0F);
	}
	
	@Override
	public ItemStack tap(DefaultedList<ItemStack> content, int waterBuckets, long secondsFermented, float downfall, float temperature) {
		int contentCount = 0;
		for (ItemStack stack : content) {
			contentCount += stack.getCount();
		}
		float thickness = (contentCount / SOLID_TO_WATER_RATIO) / waterBuckets;
		return tapWith(thickness, secondsFermented, downfall, temperature);
	}
	
	public ItemStack tapWith(float thickness, long secondsFermented, float downfall, float temperature) {
		if(secondsFermented / 60 / 60 < this.minFermentationTimeHours) {
			return NOT_FERMENTED_LONG_ENOUGH_OUTPUT_STACK;
		}
		
		ItemStack stack = this.outputItemStack.copy();
		
		if(this.fermentationData != null) {
			float ageIngameDays = ITitrationBarrelRecipe.minecraftDaysFromSeconds(secondsFermented);
			double alcPercent = Support.logBase(1.08D, thickness * ageIngameDays * this.fermentationData.fermentationSpeedMod * (0.5D + downfall / 2D));
			
			if(alcPercent >= 100) {
				return PURE_ALCOHOL_STACK;
			}
			
			BeverageItem.BeverageProperties properties;
			if(stack.getItem() instanceof BeverageItem beverageItem) {
				properties = beverageItem.getBeverageProperties(stack);
			} else {
				// if it's not a set beverage (custom recipe) assume VariantBeverage to add that tag
				properties = InfusedBeverageItem.VariantBeverageProperties.getFromStack(stack);
			}
			
			if(properties instanceof InfusedBeverageItem.VariantBeverageProperties variantBeverageProperties) {
				List<StatusEffectInstance> effects = new ArrayList<>();
				
				for(StatusEffectEntry entry : this.fermentationData.statusEffectEntries) {
					int potency = 0;
					for(StatusEffectPotencyEntry potencyEntry : entry.potencyEntries) {
						if(thickness >= potencyEntry.minThickness && alcPercent >= potencyEntry.minAlcPercent) {
							potency = potencyEntry.potency;
						}
					}
					if(potency > 0) {
						// TODO: values for duration. the current value is a placeholder
						int durationTicks = (int) (20 * 60 * thickness);
						effects.add(new StatusEffectInstance(entry.statusEffect, durationTicks, potency));
					}
				}
				
				variantBeverageProperties.statusEffects = effects;
			}
			
			properties.alcPercent = (int) alcPercent;
			properties.ageDays = (long) ageIngameDays;
			properties.thickness = thickness;
			return properties.getStack(stack);
		}
		
		return outputItemStack;
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
	
	// sadly we cannot use text.append() here, since patchouli does not support it
	// but it might be easier for translations either way
	public static MutableText getDurationText(int minFermentationTimeHours, TitrationBarrelRecipe.FermentationData fermentationData) {
		MutableText text;
		if(fermentationData == null) {
			if (minFermentationTimeHours > 72) {
				text = new TranslatableText("container.spectrum.rei.titration_barrel.time_days", Support.getWithOneDecimalAfterComma(minFermentationTimeHours  / 24F));
			} else {
				text = new TranslatableText("container.spectrum.rei.titration_barrel.time_hours", minFermentationTimeHours);
			}
		} else {
			if (minFermentationTimeHours > 72) {
				text = new TranslatableText("container.spectrum.rei.titration_barrel.at_least_time_days", Support.getWithOneDecimalAfterComma(minFermentationTimeHours  / 24F));
			} else {
				text = new TranslatableText("container.spectrum.rei.titration_barrel.at_least_time_hours", minFermentationTimeHours);
			}
		}
		return text;
	}
	
}
