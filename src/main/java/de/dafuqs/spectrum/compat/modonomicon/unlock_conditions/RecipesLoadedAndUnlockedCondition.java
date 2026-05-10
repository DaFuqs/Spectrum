package de.dafuqs.spectrum.compat.modonomicon.unlock_conditions;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.conditions.context.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.crafting.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class RecipesLoadedAndUnlockedCondition extends BookCondition {
	
	protected static final String TOOLTIP = "book.condition.tooltip." + SpectrumCommon.MOD_ID + ".recipes_loaded_and_unlocked";
	
	protected List<ResourceLocation> recipeIDs;
	
	public RecipesLoadedAndUnlockedCondition(@Nullable Component tooltip, List<ResourceLocation> recipeIDs) {
		super(tooltip);
		this.recipeIDs = recipeIDs;
	}
	
	public static RecipesLoadedAndUnlockedCondition fromJson(ResourceLocation conditionParentId, JsonObject json, HolderLookup.Provider provider) {
		List<ResourceLocation> recipeIDs = new ArrayList<>();
		
		JsonArray array = GsonHelper.getAsJsonArray(json, "recipe_ids");
		for (JsonElement element : array) {
			recipeIDs.add(ResourceLocation.parse(element.getAsString()));
		}
		Component tooltip = tooltipFromJson(json, provider);
		return new RecipesLoadedAndUnlockedCondition(tooltip, recipeIDs);
	}
	
	public static RecipesLoadedAndUnlockedCondition fromNetwork(RegistryFriendlyByteBuf buffer) {
		Component tooltip = buffer.readBoolean() ? ComponentSerialization.STREAM_CODEC.decode(buffer) : null;
		int recipeCount = buffer.readInt();
		List<ResourceLocation> recipeIDs = new ArrayList<>();
		for (int i = 0; i < recipeCount; i++) {
			recipeIDs.add(buffer.readResourceLocation());
		}
		return new RecipesLoadedAndUnlockedCondition(tooltip, recipeIDs);
	}
	
	@Override
	public ResourceLocation getType() {
		return ModonomiconCompat.RECIPE_LOADED_AND_UNLOCKED;
	}
	
	@Override
	public void toNetwork(RegistryFriendlyByteBuf buffer) {
		buffer.writeBoolean(this.tooltip != null);
		if (this.tooltip != null) {
			ComponentSerialization.STREAM_CODEC.encode(buffer, this.tooltip);
		}
		buffer.writeInt(this.recipeIDs.size());
		for (ResourceLocation identifier : this.recipeIDs) {
			buffer.writeResourceLocation(identifier);
		}
	}
	
	@Override
	public boolean test(BookConditionContext context, Player player) {
		for (ResourceLocation recipeID : this.recipeIDs) {
			Optional<RecipeHolder<?>> optionalRecipe = player.level().getRecipeManager().byKey(recipeID);
			if (optionalRecipe.isPresent()) {
				Recipe<?> recipe = optionalRecipe.get().value();
				if (recipe instanceof GatedRecipe<?> gatedRecipe) {
					if (gatedRecipe.canPlayerCraft(player)) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public List<Component> getTooltip(Player player, BookConditionContext context) {
		if (this.tooltip == null) {
			this.tooltip = Component.translatable(TOOLTIP, this.recipeIDs);
		}
		return super.getTooltip(player, context);
	}
}
