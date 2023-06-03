package de.dafuqs.spectrum.recipe.crystallarieum;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;

import java.util.*;

public class CrystallarieumRecipeSerializer implements GatedRecipeSerializer<CrystallarieumRecipe> {
	
	public final CrystallarieumRecipeSerializer.RecipeFactory<CrystallarieumRecipe> recipeFactory;
	
	public CrystallarieumRecipeSerializer(CrystallarieumRecipeSerializer.RecipeFactory<CrystallarieumRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<CrystallarieumRecipe> {
		CrystallarieumRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient inputIngredient, List<BlockState> growthStages, int secondsPerGrowthStage, InkColor inkColor, int inkPerSecond, boolean growsWithoutCatalyst, List<CrystallarieumCatalyst> catalysts, List<ItemStack> additionalOutputs);
	}
	
	@Override
	public CrystallarieumRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = readGroup(jsonObject);
		boolean secret = readSecret(jsonObject);
		Identifier requiredAdvancementIdentifier = readRequiredAdvancementIdentifier(jsonObject);
		
		Ingredient inputIngredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
		
		List<BlockState> growthStages = new ArrayList<>();
		JsonArray growthStageArray = JsonHelper.getArray(jsonObject, "growth_stage_states");
		for (int i = 0; i < growthStageArray.size(); i++) {
			String blockStateString = growthStageArray.get(i).getAsString();
			try {
				growthStages.add(RecipeUtils.blockStateFromString(blockStateString));
			} catch (CommandSyntaxException e) {
				SpectrumCommon.logError("Recipe " + identifier + " specifies block state " + blockStateString + " that does not seem valid or the block does not exist. Recipe will be ignored.");
				return null;
			}
		}
		int secondsPerGrowthStage = JsonHelper.getInt(jsonObject, "seconds_per_growth_stage");
		InkColor inkColor = InkColor.of(JsonHelper.getString(jsonObject, "ink_color"));
		int inkCostTier = JsonHelper.getInt(jsonObject, "ink_cost_tier");
		int inkPerSecond = inkCostTier == 0 ? 0 : (int) Math.pow(2, inkCostTier - 1); // 0=0; 1=1; 2=4; 3=16; 4=64; 5=256)
		boolean growsWithoutCatalyst = JsonHelper.getBoolean(jsonObject, "grows_without_catalyst");
		
		List<CrystallarieumCatalyst> catalysts = new ArrayList<>();
		if (jsonObject.has("catalysts")) {
			JsonArray catalystArray = JsonHelper.getArray(jsonObject, "catalysts");
			for (JsonElement jsonElement : catalystArray) {
				catalysts.add(CrystallarieumCatalyst.fromJson(jsonElement.getAsJsonObject()));
			}
		}
		List<ItemStack> additionalOutputs = new ArrayList<>();
		if (jsonObject.has("additional_recipe_manager_outputs")) {
			JsonArray additionalOutputArray = JsonHelper.getArray(jsonObject, "additional_recipe_manager_outputs");
			for (JsonElement jsonElement : additionalOutputArray) {
				Identifier additionalOutputItemIdentifier = new Identifier(jsonElement.getAsString());
				ItemStack itemStack = new ItemStack(Registries.ITEM.getOrEmpty(additionalOutputItemIdentifier).orElseThrow(() -> new IllegalStateException("Item: " + additionalOutputItemIdentifier + " does not exist")));
				additionalOutputs.add(itemStack);
			}
		}

		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, inputIngredient, growthStages, secondsPerGrowthStage, inkColor, inkPerSecond, growsWithoutCatalyst, catalysts, additionalOutputs);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, CrystallarieumRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		packetByteBuf.writeBoolean(recipe.secret);
		writeNullableIdentifier(packetByteBuf, recipe.requiredAdvancementIdentifier);
		
		recipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeInt(recipe.growthStages.size());
		for (BlockState state : recipe.growthStages) {
			packetByteBuf.writeString(RecipeUtils.blockStateToString(state));
		}
		packetByteBuf.writeInt(recipe.secondsPerGrowthStage);
		packetByteBuf.writeString(recipe.inkColor.toString());
		packetByteBuf.writeInt(recipe.inkPerSecond);
		packetByteBuf.writeBoolean(recipe.growsWithoutCatalyst);
		packetByteBuf.writeInt(recipe.catalysts.size());
		for (CrystallarieumCatalyst catalyst : recipe.catalysts) {
			catalyst.write(packetByteBuf);
		}
		packetByteBuf.writeInt(recipe.additionalOutputs.size());
		for (ItemStack additionalOutput : recipe.additionalOutputs) {
			packetByteBuf.writeItemStack(additionalOutput);
		}
	}
	
	@Override
	public CrystallarieumRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		boolean secret = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = readNullableIdentifier(packetByteBuf);
		
		Ingredient inputIngredient = Ingredient.fromPacket(packetByteBuf);
		List<BlockState> growthStages = new ArrayList<>();
		int count = packetByteBuf.readInt();
		for (int i = 0; i < count; i++) {
			String blockStateString = packetByteBuf.readString();
			try {
				growthStages.add(RecipeUtils.blockStateFromString(blockStateString));
			} catch (CommandSyntaxException e) {
				SpectrumCommon.logError("Recipe " + identifier + " specifies block state " + blockStateString + " that does not seem valid or the block does not exist. Recipe will be ignored.");
				return null;
			}
		}

		int secondsPerGrowthStage = packetByteBuf.readInt();
		InkColor inkColor = InkColor.of(packetByteBuf.readString());
		int inkPerSecond = packetByteBuf.readInt();
		boolean growthWithoutCatalyst = packetByteBuf.readBoolean();
		List<CrystallarieumCatalyst> catalysts = new ArrayList<>();
		count = packetByteBuf.readInt();
		for (int i = 0; i < count; i++) {
			catalysts.add(CrystallarieumCatalyst.fromPacket(packetByteBuf));
		}
		List<ItemStack> additionalOutputs = new ArrayList<>();
		count = packetByteBuf.readInt();
		for (int i = 0; i < count; i++) {
			additionalOutputs.add(packetByteBuf.readItemStack());
		}

		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, inputIngredient, growthStages, secondsPerGrowthStage, inkColor, inkPerSecond, growthWithoutCatalyst, catalysts, additionalOutputs);
	}
	
}
