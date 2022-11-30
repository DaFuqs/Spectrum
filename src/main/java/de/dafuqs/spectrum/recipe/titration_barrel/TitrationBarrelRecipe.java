package de.dafuqs.spectrum.recipe.titration_barrel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.items.food.beverages.BeverageItem;
import de.dafuqs.spectrum.items.food.beverages.properties.BeverageProperties;
import de.dafuqs.spectrum.items.food.beverages.properties.VariantBeverageProperties;
import de.dafuqs.spectrum.recipe.GatedSpectrumRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TitrationBarrelRecipe extends GatedSpectrumRecipe implements ITitrationBarrelRecipe {
	
	public static final ItemStack NOT_FERMENTED_LONG_ENOUGH_OUTPUT_STACK = Items.POTION.getDefaultStack();
	public static final ItemStack PURE_ALCOHOL_STACK = SpectrumItems.PURE_ALCOHOL.getDefaultStack();
	
	protected final List<IngredientStack> inputStacks;
	protected final ItemStack outputItemStack;
	protected final Item tappingItem;
	protected final Fluid fluid;
	
	protected final int minFermentationTimeHours;
	protected final FermentationData fermentationData;
	
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
	
	public record StatusEffectEntry(StatusEffect statusEffect, int baseDuration,
	                                List<StatusEffectPotencyEntry> potencyEntries) {
		
		public static StatusEffectEntry fromJson(JsonObject jsonObject) {
			Identifier statusEffectIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "id"));
			StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectIdentifier);
			if (statusEffect == null) {
				SpectrumCommon.logError("Status effect " + statusEffectIdentifier + " does not exist in the status effect registry. Falling back to WEAKNESS");
				statusEffect = StatusEffects.WEAKNESS;
			}
			int baseDuration = JsonHelper.getInt(jsonObject, "base_duration", 1200);
			
			List<StatusEffectPotencyEntry> potencyEntries = new ArrayList<>();
			if (JsonHelper.hasArray(jsonObject, "potency")) {
				JsonArray potencyArray = JsonHelper.getArray(jsonObject, "potency");
				for (int i = 0; i < potencyArray.size(); i++) {
					JsonObject object = potencyArray.get(i).getAsJsonObject();
					potencyEntries.add(StatusEffectPotencyEntry.fromJson(object));
				}
			} else {
				potencyEntries.add(new StatusEffectPotencyEntry(0, 0, 0));
			}
			return new StatusEffectEntry(statusEffect, baseDuration, potencyEntries);
		}
		
		public void write(PacketByteBuf packetByteBuf) {
			packetByteBuf.writeString(Registry.STATUS_EFFECT.getId(this.statusEffect).toString());
			packetByteBuf.writeInt(baseDuration);
			packetByteBuf.writeInt(this.potencyEntries.size());
			for (StatusEffectPotencyEntry potencyEntry : this.potencyEntries) {
				potencyEntry.write(packetByteBuf);
			}
		}
		
		public static StatusEffectEntry read(PacketByteBuf packetByteBuf) {
			Identifier statusEffectIdentifier = Identifier.tryParse(packetByteBuf.readString());
			StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectIdentifier);
			int baseDuration = packetByteBuf.readInt();
			int potencyEntryCount = packetByteBuf.readInt();
			List<StatusEffectPotencyEntry> potencyEntries = new ArrayList<>(potencyEntryCount);
			for (int i = 0; i < potencyEntryCount; i++) {
				potencyEntries.add(StatusEffectPotencyEntry.read(packetByteBuf));
			}
			return new StatusEffectEntry(statusEffect, baseDuration, potencyEntries);
		}
		
	}
	
	public record FermentationData(float fermentationSpeedMod, float angelsSharePercentPerMcDay,
	                               List<StatusEffectEntry> statusEffectEntries) {
		
		public static FermentationData fromJson(JsonObject jsonObject) {
			float fermentationSpeedMod = JsonHelper.getFloat(jsonObject, "fermentation_speed_mod", 1.0F);
			float angelsSharePerMcDay = JsonHelper.getFloat(jsonObject, "angels_share_percent_per_mc_day", 0.1F);
			List<StatusEffectEntry> statusEffectEntries = new ArrayList<>();
			if (JsonHelper.hasArray(jsonObject, "effects")) {
				JsonArray effectsArray = JsonHelper.getArray(jsonObject, "effects");
				for (int i = 0; i < effectsArray.size(); i++) {
					JsonObject object = effectsArray.get(i).getAsJsonObject();
					statusEffectEntries.add(StatusEffectEntry.fromJson(object));
				}
			}
			return new FermentationData(fermentationSpeedMod, angelsSharePerMcDay, statusEffectEntries);
		}
		
		public void write(PacketByteBuf packetByteBuf) {
			packetByteBuf.writeFloat(this.fermentationSpeedMod);
			packetByteBuf.writeFloat(this.angelsSharePercentPerMcDay);
			packetByteBuf.writeInt(this.statusEffectEntries.size());
			for (StatusEffectEntry statusEffectEntry : this.statusEffectEntries) {
				statusEffectEntry.write(packetByteBuf);
			}
		}
		
		public static FermentationData read(PacketByteBuf packetByteBuf) {
			float fermentationSpeedMod = packetByteBuf.readFloat();
			float angelsSharePerMcDay = packetByteBuf.readFloat();
			int statusEffectEntryCount = packetByteBuf.readInt();
			List<StatusEffectEntry> statusEffectEntries = new ArrayList<>(statusEffectEntryCount);
			for (int i = 0; i < statusEffectEntryCount; i++) {
				statusEffectEntries.add(StatusEffectEntry.read(packetByteBuf));
			}
			return new FermentationData(fermentationSpeedMod, angelsSharePerMcDay, statusEffectEntries);
		}
		
	}
	
	public TitrationBarrelRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, List<IngredientStack> inputStacks, Fluid fluid, ItemStack outputItemStack, Item tappingItem, int minFermentationTimeHours, FermentationData fermentationData) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.inputStacks = inputStacks;
		this.fluid = fluid;
		this.minFermentationTimeHours = minFermentationTimeHours;
		this.outputItemStack = outputItemStack;
		this.tappingItem = tappingItem;
		this.fermentationData = fermentationData;
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		return matchIngredientStacksExclusively(inventory, getIngredientStacks());
	}
	
	// should not be used. Instead, use getIngredientStacks(), which includes item counts
	@Override
	@Deprecated
	public DefaultedList<Ingredient> getIngredients() {
		return IngredientStack.listIngredients(this.inputStacks);
	}
	
	public List<IngredientStack> getIngredientStacks() {
		return this.inputStacks;
	}
	
	public Item getTappingItem() {
		return tappingItem;
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
		ItemStack stack = tapWith(1.0F, this.minFermentationTimeHours * 60L * 60L, 0.4F, 0.8F); // downfall & temperature are for plains
		stack.setCount(this.outputItemStack.getCount());
		return stack;
	}
	
	public Fluid getFluid() {
		return fluid;
	}
	
	@Override
	public float getAngelsSharePerMcDay() {
		if (this.fermentationData == null) {
			return 0;
		}
		return this.fermentationData.angelsSharePercentPerMcDay;
	}
	
	@Override
	public ItemStack tap(Inventory inventory, long secondsFermented, float downfall, float temperature) {
		int contentCount = InventoryHelper.countItemsInInventory(inventory);
		float thickness = getThickness(contentCount);
		return tapWith(thickness, secondsFermented, downfall, temperature);
	}
	
	private ItemStack tapWith(float thickness, long secondsFermented, float downfall, float temperature) {
		if (secondsFermented / 60 / 60 < this.minFermentationTimeHours) {
			return NOT_FERMENTED_LONG_ENOUGH_OUTPUT_STACK;
		}
		
		ItemStack stack = this.outputItemStack.copy();
		stack.setCount(1);
		
		if (this.fermentationData != null) {
			float ageIngameDays = TimeHelper.minecraftDaysFromSeconds(secondsFermented);
			double alcPercent = 0;
			if (this.fermentationData.fermentationSpeedMod > 0) {
				alcPercent = getAlcPercent(thickness, downfall, ageIngameDays);
				alcPercent = Math.max(0, alcPercent);
			}
			
			if (alcPercent >= 100) {
				return PURE_ALCOHOL_STACK;
			}
			
			BeverageProperties properties;
			if (stack.getItem() instanceof BeverageItem beverageItem) {
				properties = beverageItem.getBeverageProperties(stack);
			} else {
				// if it's not a set beverage (custom recipe) assume VariantBeverage to add that tag
				properties = VariantBeverageProperties.getFromStack(stack);
			}
			
			if (properties instanceof VariantBeverageProperties variantBeverageProperties) {
				float durationMultiplier = 1.5F - thickness / 2F;
				
				List<StatusEffectInstance> effects = new ArrayList<>();
				
				for (StatusEffectEntry entry : this.fermentationData.statusEffectEntries) {
					int potency = -1;
					int durationTicks = entry.baseDuration;
					for (StatusEffectPotencyEntry potencyEntry : entry.potencyEntries) {
						if (thickness >= potencyEntry.minThickness && alcPercent >= potencyEntry.minAlcPercent) {
							potency = potencyEntry.potency;
						}
					}
					if (potency > -1) {
						effects.add(new StatusEffectInstance(entry.statusEffect, (int) (durationTicks * durationMultiplier), potency));
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
	
	protected double getAlcPercent(float thickness, float downfall, float ageIngameDays) {
		return Support.logBase(1 + this.fermentationData.fermentationSpeedMod, ageIngameDays * (0.5D + thickness / 2D) * (0.5D + downfall / 2D));
	}
	
	protected float getThickness(int contentCount) {
		int inputStacksCount = 0;
		for (IngredientStack stack : inputStacks) {
			inputStacksCount += stack.getCount();
		}
		return contentCount / (float) inputStacksCount;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.TITRATION_BARREL_RECIPE_SERIALIZER;
	}
	
	// sadly we cannot use text.append() here, since patchouli does not support it
	// but this way it might be easier for translations either way
	public static MutableText getDurationText(int minFermentationTimeHours, TitrationBarrelRecipe.FermentationData fermentationData) {
		MutableText text;
		if (fermentationData == null) {
			if (minFermentationTimeHours == 1) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.time_hour");
			} else if (minFermentationTimeHours == 24) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.time_day");
			} else if (minFermentationTimeHours > 72) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.time_days", Support.getWithOneDecimalAfterComma(minFermentationTimeHours / 24F));
			} else {
				text = Text.translatable("container.spectrum.rei.titration_barrel.time_hours", minFermentationTimeHours);
			}
		} else {
			if (minFermentationTimeHours == 1) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.at_least_time_hour");
			} else if (minFermentationTimeHours == 24) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.at_least_time_day");
			} else if (minFermentationTimeHours > 72) {
				text = Text.translatable("container.spectrum.rei.titration_barrel.at_least_time_days", Support.getWithOneDecimalAfterComma(minFermentationTimeHours / 24F));
			} else {
				text = Text.translatable("container.spectrum.rei.titration_barrel.at_least_time_hours", minFermentationTimeHours);
			}
		}
		return text;
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return ITitrationBarrelRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.TITRATION_BARREL_ID;
	}
	
}
