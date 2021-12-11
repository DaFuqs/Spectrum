package de.dafuqs.spectrum.recipe.enchanter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeWorldCondition;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeWorldEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnchanterRecipeSerializer<T extends EnchanterRecipe> implements RecipeSerializer<T> {

	public final EnchanterRecipeSerializer.RecipeFactory<T> recipeFactory;

	public EnchanterRecipeSerializer(EnchanterRecipeSerializer.RecipeFactory<T> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}

	@Override
	public T read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		
		JsonArray ingredientArray = JsonHelper.getArray(jsonObject, "ingredients");
		DefaultedList<Ingredient> craftingInputs = DefaultedList.ofSize(ingredientArray.size());
		for(int i = 0; i < ingredientArray.size(); i++) {
			craftingInputs.add(Ingredient.fromJson(ingredientArray.get(i)));
		}

		ItemStack output = RecipeUtils.outputWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		int requiredExperience = JsonHelper.getInt(jsonObject, "required_experience", 0);
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		
		boolean noBenefitsFromYieldAndEfficiencyUpgrades = false;
		if(JsonHelper.hasPrimitive(jsonObject, "disable_yield_and_efficiency_upgrades")) {
			noBenefitsFromYieldAndEfficiencyUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_and_efficiency_upgrades", false);
		}
		
		Identifier requiredAdvancementIdentifier = null;
		if(JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		}
		
		return this.recipeFactory.create(identifier, group, craftingInputs, output, craftingTime, requiredExperience, noBenefitsFromYieldAndEfficiencyUpgrades, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, T enchanterRecipe) {
		packetByteBuf.writeString(enchanterRecipe.group);

		packetByteBuf.writeShort(enchanterRecipe.inputs.size());
		for(Ingredient ingredient : enchanterRecipe.inputs) {
			ingredient.write(packetByteBuf);
		}
		
		packetByteBuf.writeItemStack(enchanterRecipe.output);
		packetByteBuf.writeInt(enchanterRecipe.craftingTime);
		packetByteBuf.writeInt(enchanterRecipe.requiredExperience);
		packetByteBuf.writeBoolean(enchanterRecipe.noBenefitsFromYieldAndEfficiencyUpgrades);
		packetByteBuf.writeIdentifier(enchanterRecipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		
		short craftingInputCount = packetByteBuf.readShort();
		DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(craftingInputCount, Ingredient.EMPTY);
		for(short i = 0; i < craftingInputCount; i++) {
			ingredients.set(i, Ingredient.fromPacket(packetByteBuf));
		}
		
		ItemStack output = packetByteBuf.readItemStack();
		int craftingTime = packetByteBuf.readInt();
		int requiredExperience = packetByteBuf.readInt();
		boolean noBenefitsFromYieldAndEfficiencyUpgrades = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, group, ingredients, output, craftingTime, requiredExperience, noBenefitsFromYieldAndEfficiencyUpgrades, requiredAdvancementIdentifier);
	}
	
	public interface RecipeFactory<T extends EnchanterRecipe> {
		T create(Identifier id, String group, DefaultedList<Ingredient> inputs, ItemStack output, int craftingTime, int requiredExperience, boolean noBenefitsFromYieldAndEfficiencyUpgrades, @Nullable Identifier requiredAdvancementIdentifier);
	}

}
