package de.dafuqs.spectrum.recipe.crystallarieum;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.recipe.GatedRecipeSerializer;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class CrystallarieumRecipeSerializer implements GatedRecipeSerializer<CrystallarieumRecipe> {
	
	public final CrystallarieumRecipeSerializer.RecipeFactory<CrystallarieumRecipe> recipeFactory;
	
	public CrystallarieumRecipeSerializer(CrystallarieumRecipeSerializer.RecipeFactory<CrystallarieumRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	public interface RecipeFactory<CrystallarieumRecipe> {
		CrystallarieumRecipe create(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient inputIngredient, List<BlockState> growthStages, int secondsPerGrowthStage, InkColor inkColor, int inkPerSecond, boolean growsWithoutCatalyst, List<CrystallarieumCatalyst> catalysts);
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
		int inkPerSecond = JsonHelper.getInt(jsonObject, "ink_per_second");
		boolean growsWithoutCatalyst = JsonHelper.getBoolean(jsonObject, "grows_without_catalyst");
		
		List<CrystallarieumCatalyst> catalysts = new ArrayList<>();
		JsonArray catalystArray = JsonHelper.getArray(jsonObject, "catalysts");
		for (int i = 0; i < catalystArray.size(); i++) {
			catalysts.add(CrystallarieumCatalyst.fromJson(catalystArray.get(i).getAsJsonObject()));
		}
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, inputIngredient, growthStages, secondsPerGrowthStage, inkColor, inkPerSecond, growsWithoutCatalyst, catalysts);
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
		
		return this.recipeFactory.create(identifier, group, secret, requiredAdvancementIdentifier, inputIngredient, growthStages, secondsPerGrowthStage, inkColor, inkPerSecond, growthWithoutCatalyst, catalysts);
	}
	
}
