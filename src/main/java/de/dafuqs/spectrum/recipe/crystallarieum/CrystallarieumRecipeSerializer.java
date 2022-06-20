package de.dafuqs.spectrum.recipe.crystallarieum;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CrystallarieumRecipeSerializer implements RecipeSerializer<CrystallarieumRecipe> {
	
	public final CrystallarieumRecipeSerializer.RecipeFactory<CrystallarieumRecipe> recipeFactory;
	
	public CrystallarieumRecipeSerializer(CrystallarieumRecipeSerializer.RecipeFactory<CrystallarieumRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public CrystallarieumRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
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
		int ticksPerGrowthStage = JsonHelper.getInt(jsonObject, "ticks_per_growth_stage");
		InkColor inkColor = InkColor.of(JsonHelper.getString(jsonObject, "ink_color"));
		int inkPerTick = JsonHelper.getInt(jsonObject, "ink_per_tick");
		boolean growthWithoutCatalyst = JsonHelper.getBoolean(jsonObject, "grows_without_catalyst");
		
		List<CrystallarieumCatalyst> catalysts = new ArrayList<>();
		JsonArray catalystArray = JsonHelper.getArray(jsonObject, "catalysts");
		for (int i = 0; i < catalystArray.size(); i++) {
			catalysts.add(CrystallarieumCatalyst.fromJson(growthStageArray.get(i).getAsJsonObject()));
		}
		Identifier requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		
		return this.recipeFactory.create(identifier, group, inputIngredient, growthStages, ticksPerGrowthStage, inkColor, inkPerTick, growthWithoutCatalyst, catalysts, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, CrystallarieumRecipe recipe) {
		packetByteBuf.writeString(recipe.group);
		recipe.inputIngredient.write(packetByteBuf);
		packetByteBuf.writeInt(recipe.growthStages.size());
		for(BlockState state : recipe.growthStages) {
			packetByteBuf.writeString(RecipeUtils.blockStateToString(state));
		}
		packetByteBuf.writeInt(recipe.ticksPerGrowthStage);
		packetByteBuf.writeString(recipe.inkColor.toString());
		packetByteBuf.writeInt(recipe.inkPerTick);
		packetByteBuf.writeBoolean(recipe.growthWithoutCatalyst);
		packetByteBuf.writeInt(recipe.catalysts.size());
		for(CrystallarieumCatalyst catalyst : recipe.catalysts) {
			catalyst.write(packetByteBuf);
		}
		packetByteBuf.writeIdentifier(recipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public CrystallarieumRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		Ingredient inputIngredient = Ingredient.fromPacket(packetByteBuf);
		List<BlockState> growthStages = new ArrayList<>();
		int count = packetByteBuf.readInt();
		for(int i = 0; i < count; i++) {
			String blockStateString = packetByteBuf.readString();
			try {
				growthStages.add(RecipeUtils.blockStateFromString(blockStateString));
			} catch (CommandSyntaxException e) {
				SpectrumCommon.logError("Recipe " + identifier + " specifies block state " + blockStateString + " that does not seem valid or the block does not exist. Recipe will be ignored.");
				return null;
			}
		}
		
		int ticksPerGrowthStage = packetByteBuf.readInt();
		InkColor inkColor = InkColor.of(packetByteBuf.readString());
		int inkPerTick = packetByteBuf.readInt();
		boolean growthWithoutCatalyst = packetByteBuf.readBoolean();
		List<CrystallarieumCatalyst> catalysts = new ArrayList<>();
		count = packetByteBuf.readInt();
		for(int i = 0; i < count; i++) {
			catalysts.add(CrystallarieumCatalyst.fromPacket(packetByteBuf));
		}
		
		@Nullable Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, group, inputIngredient, growthStages, ticksPerGrowthStage, inkColor, inkPerTick, growthWithoutCatalyst, catalysts, requiredAdvancementIdentifier);
	}
	
	public interface RecipeFactory<CrystallarieumRecipe> {
		CrystallarieumRecipe create(Identifier id, String group, Ingredient inputIngredient, List<BlockState> growthStages, int ticksPerGrowthStage, InkColor inkColor, int inkPerTick, boolean growthWithoutCatalyst, List<CrystallarieumCatalyst> catalysts, @Nullable Identifier requiredAdvancementIdentifier);
	}
	
}
