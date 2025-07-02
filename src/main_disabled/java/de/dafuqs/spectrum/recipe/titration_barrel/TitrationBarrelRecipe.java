package de.dafuqs.spectrum.recipe.titration_barrel;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TitrationBarrelRecipe extends GatedStackSpectrumRecipe<StorageRecipeInput<SingleVariantStorage<FluidVariant>>> implements ITitrationBarrelRecipe {
	
	public static final List<Integer> FERMENTATION_DURATION_DISPLAY_TIME_MULTIPLIERS = new ArrayList<>() {{
		add(1);
		add(10);
		add(100);
	}};
	
	public final List<IngredientStack> inputStacks;
	public final ItemStack outputItemStack;
	public final Item tappingItem;
	public final FluidIngredient fluid;
	
	public final int minFermentationTimeHours;
	public final FermentationData fermentationData;
	
	public TitrationBarrelRecipe(
			String group,
			boolean secret,
			Optional<ResourceLocation> requiredAdvancementIdentifier,
			List<IngredientStack> inputStacks,
			FluidIngredient fluid,
			ItemStack outputItemStack,
			Item tappingItem,
			int minFermentationTimeHours,
			FermentationData fermentationData
	) {
		super(group, secret, requiredAdvancementIdentifier);
		
		this.inputStacks = inputStacks;
		this.fluid = fluid;
		this.minFermentationTimeHours = minFermentationTimeHours;
		this.outputItemStack = outputItemStack;
		this.tappingItem = tappingItem;
		this.fermentationData = fermentationData;
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(StorageRecipeInput<SingleVariantStorage<FluidVariant>> recipeInput, Level world) {
		SingleVariantStorage<FluidVariant> fluidStorage = recipeInput.getFluidStorage();
		if (!this.fluid.test(fluidStorage.variant)) {
			return false;
		}
		if (this.fluid != FluidIngredient.EMPTY) {
			if (fluidStorage.getAmount() != fluidStorage.getCapacity()) {
				return false;
			}
		}
		return matchIngredientStacksExclusively(recipeInput, getIngredientStacks());
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		return this.inputStacks;
	}
	
	@Override
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
	@Deprecated
	public ItemStack assemble(StorageRecipeInput<SingleVariantStorage<FluidVariant>> inventory, HolderLookup.Provider drm) {
		return getDefaultTap(1).copy();
	}
	
	public ItemStack getPreviewTap(int timeMultiplier) {
		return tapWith(1.0F, this.minFermentationTimeHours * 60L * 60L * timeMultiplier, 0.4F); // downfall equals the one in plains
	}
	
	public ItemStack getDefaultTap(int timeMultiplier) {
		ItemStack stack = getPreviewTap(timeMultiplier);
		stack.setCount(this.outputItemStack.getCount());
		FermentedItem.setPreviewStack(stack);
		return stack;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryManager) {
		return getDefaultTap(1);
	}
	
	// used for recipe viewers to show recipe outputs with a few example fermentation times
	public Collection<ItemStack> getOutputVariations(List<Integer> timeMultipliers) {
		List<ItemStack> list = new ArrayList<>();
		for (int timeMultiplier : timeMultipliers) {
			list.add(getDefaultTap(timeMultiplier));
		}
		return list;
	}
	
	@Override
	public FluidIngredient getFluidInput() {
		return fluid;
	}
	
	@Override
	public float getAngelsSharePerMcDay() {
		return this.fermentationData.angelsSharePercentPerMcDay();
	}
	
	@Override
	public ItemStack tap(Container inventory, long secondsFermented, float downfall) {
		int contentCount = InventoryHelper.countItemsInInventory(inventory);
		float thickness = getThickness(contentCount);
		return tapWith(thickness, secondsFermented, downfall);
	}
	
	private ItemStack tapWith(float thickness, long secondsFermented, float downfall) {
		ItemStack stack = this.outputItemStack.copy();
		return getFermentedStack(this.fermentationData, thickness, secondsFermented, downfall, stack);
	}
	
	public static ItemStack getFermentedStack(@NotNull FermentationData fermentationData, float thickness, long secondsFermented, float downfall, ItemStack inputStack) {
		float ageIngameDays = TimeHelper.minecraftDaysFromSeconds(secondsFermented);
		double alcPercent = 0;
		if (fermentationData.fermentationSpeedMod() > 0) {
			alcPercent = getAlcPercent(fermentationData.fermentationSpeedMod(), thickness, downfall, ageIngameDays);
			alcPercent = Math.max(0, alcPercent);
		}
		
		if (alcPercent >= 100 && inputStack.getItem() instanceof FermentedItem) {
			return SpectrumItems.PURE_ALCOHOL.getDefaultInstance();
		}
		
		// if it's not a set beverage (custom recipe) mark it as unknown
		if (!(inputStack.getItem() instanceof FermentedItem))
			inputStack.set(SpectrumDataComponentTypes.INFUSED_BEVERAGE, InfusedBeverageComponent.DEFAULT);
		
		var potionContents = inputStack.get(DataComponents.POTION_CONTENTS);
		if (potionContents != null) {
			float durationMultiplier = (float) (Support.logBase(1 + thickness, 2));
			
			List<MobEffectInstance> effects = new ArrayList<>();
			for (FermentationStatusEffectEntry entry : fermentationData.statusEffectEntries()) {
				int potency = -1;
				int durationTicks = entry.baseDuration();
				for (FermentationStatusEffectEntry.StatusEffectPotencyEntry potencyEntry : entry.potencyEntries()) {
					if (thickness >= potencyEntry.minThickness() && alcPercent >= potencyEntry.minAlcPercent()) {
						potency = potencyEntry.potency();
					}
				}
				if (potency > -1)
					effects.add(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(BuiltInRegistries.MOB_EFFECT.getResourceKey(entry.statusEffect()).get()), (int) (durationTicks * durationMultiplier), potency));
			}
			
			inputStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.empty(), effects));
		}
		
		inputStack.set(SpectrumDataComponentTypes.BEVERAGE, new BeverageComponent((long) ageIngameDays, (int) alcPercent, thickness));
		return inputStack;
	}
	
	protected static double getAlcPercent(float fermentationSpeedMod, float thickness, float downfall, float ageIngameDays) {
		return Support.logBase(1 + fermentationSpeedMod, ageIngameDays * (0.5D + thickness / 2D) * (0.5D + downfall / 2D));
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
		return SpectrumRecipeSerializers.TITRATION_BARREL;
	}
	
	// sadly we cannot use text.append() here, since the guidebook does not support it
	// but this way it might be easier for translations either way
	public static MutableComponent getDurationText(int minFermentationTimeHours, FermentationData fermentationData) {
		MutableComponent text;
		if (fermentationData.equals(FermentationData.DEFAULT)) {
			if (minFermentationTimeHours == 1) {
				text = Component.translatable("container.spectrum.rei.titration_barrel.time_hour");
			} else if (minFermentationTimeHours == 24) {
				text = Component.translatable("container.spectrum.rei.titration_barrel.time_day");
			} else if (minFermentationTimeHours >= 72) {
				text = Component.translatable("container.spectrum.rei.titration_barrel.time_days", Support.getWithOneDecimalAfterComma(minFermentationTimeHours / 24F));
			} else {
				text = Component.translatable("container.spectrum.rei.titration_barrel.time_hours", minFermentationTimeHours);
			}
		} else {
			if (minFermentationTimeHours == 1) {
				text = Component.translatable("container.spectrum.rei.titration_barrel.at_least_time_hour");
			} else if (minFermentationTimeHours == 24) {
				text = Component.translatable("container.spectrum.rei.titration_barrel.at_least_time_day");
			} else if (minFermentationTimeHours > 72) {
				text = Component.translatable("container.spectrum.rei.titration_barrel.at_least_time_days", Support.getWithOneDecimalAfterComma(minFermentationTimeHours / 24F));
			} else {
				text = Component.translatable("container.spectrum.rei.titration_barrel.at_least_time_hours", minFermentationTimeHours);
			}
		}
		return text;
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return ITitrationBarrelRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "titration_barrel";
	}
	
	public static class Serializer implements RecipeSerializer<TitrationBarrelRecipe> {
		
		public static final MapCodec<TitrationBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				IngredientStack.Serializer.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.inputStacks),
				FluidIngredient.CODEC.optionalFieldOf("fluid", FluidIngredient.EMPTY).forGetter(recipe -> recipe.fluid),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.outputItemStack),
				BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("tapping_item", Items.AIR).forGetter(recipe -> recipe.tappingItem),
				Codec.INT.optionalFieldOf("min_fermentation_time_hours", 24).forGetter(recipe -> recipe.minFermentationTimeHours),
				FermentationData.CODEC.optionalFieldOf("fermentation", FermentationData.DEFAULT).forGetter(recipe -> recipe.fermentationData)
		).apply(i, TitrationBarrelRecipe::new));
		
		private static final StreamCodec<RegistryFriendlyByteBuf, TitrationBarrelRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, c -> c.group,
				ByteBufCodecs.BOOL, c -> c.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), c -> c.requiredAdvancementIdentifier,
				IngredientStack.Serializer.PACKET_CODEC.apply(ByteBufCodecs.list()), c -> c.inputStacks,
				FluidIngredient.PACKET_CODEC, c -> c.fluid,
				ItemStack.STREAM_CODEC, c -> c.outputItemStack,
				ByteBufCodecs.registry(Registries.ITEM), recipe -> recipe.tappingItem,
				ByteBufCodecs.VAR_INT, recipe -> recipe.minFermentationTimeHours,
				FermentationData.PACKET_CODEC, recipe -> recipe.fermentationData,
				TitrationBarrelRecipe::new
		);
		
		@Override
		public MapCodec<TitrationBarrelRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, TitrationBarrelRecipe> streamCodec() {
			return PACKET_CODEC;
		}
		
	}
	
}
