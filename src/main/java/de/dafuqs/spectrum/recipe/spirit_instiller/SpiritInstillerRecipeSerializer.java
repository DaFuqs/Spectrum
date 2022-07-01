package de.dafuqs.spectrum.recipe.spirit_instiller;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlock;
import de.dafuqs.spectrum.recipe.RecipeUtils;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class SpiritInstillerRecipeSerializer implements RecipeSerializer<SpiritInstillerRecipe> {
	
	public final SpiritInstillerRecipeSerializer.RecipeFactory<SpiritInstillerRecipe> recipeFactory;
	
	public SpiritInstillerRecipeSerializer(SpiritInstillerRecipeSerializer.RecipeFactory<SpiritInstillerRecipe> recipeFactory) {
		this.recipeFactory = recipeFactory;
	}
	
	@Override
	public SpiritInstillerRecipe read(Identifier identifier, JsonObject jsonObject) {
		String group = JsonHelper.getString(jsonObject, "group", "");
		IngredientStack centerIngredient = RecipeUtils.ingredientStackFromJson(JsonHelper.getObject(jsonObject, "center_ingredient"));
		IngredientStack bowlIngredient1 = RecipeUtils.ingredientStackFromJson(JsonHelper.getObject(jsonObject, "ingredient1"));
		IngredientStack bowlIngredient2 = RecipeUtils.ingredientStackFromJson(JsonHelper.getObject(jsonObject, "ingredient2"));
		ItemStack outputItemStack = RecipeUtils.itemStackWithNbtFromJson(JsonHelper.getObject(jsonObject, "result"));
		
		int craftingTime = JsonHelper.getInt(jsonObject, "time", 200);
		float experience = JsonHelper.getFloat(jsonObject, "experience");
		
		boolean noBenefitsFromYieldAndEfficiencyUpgrades = false;
		if (JsonHelper.hasPrimitive(jsonObject, "disable_yield_and_efficiency_upgrades")) {
			noBenefitsFromYieldAndEfficiencyUpgrades = JsonHelper.getBoolean(jsonObject, "disable_yield_and_efficiency_upgrades", false);
		}
		
		Identifier requiredAdvancementIdentifier;
		if (JsonHelper.hasString(jsonObject, "required_advancement")) {
			requiredAdvancementIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "required_advancement"));
		} else {
			// No unlock advancement set. Will be set to the unlock advancement of the block itself
			requiredAdvancementIdentifier = SpiritInstillerBlock.UNLOCK_IDENTIFIER;
		}
		
		return this.recipeFactory.create(identifier, group, centerIngredient, bowlIngredient1, bowlIngredient2, outputItemStack, craftingTime, experience, noBenefitsFromYieldAndEfficiencyUpgrades, requiredAdvancementIdentifier);
	}
	
	@Override
	public void write(PacketByteBuf packetByteBuf, SpiritInstillerRecipe spiritInstillerRecipe) {
		packetByteBuf.writeString(spiritInstillerRecipe.group);
		spiritInstillerRecipe.centerIngredient.write(packetByteBuf);
		spiritInstillerRecipe.bowlIngredient1.write(packetByteBuf);
		spiritInstillerRecipe.bowlIngredient2.write(packetByteBuf);
		packetByteBuf.writeItemStack(spiritInstillerRecipe.outputItemStack);
		packetByteBuf.writeInt(spiritInstillerRecipe.craftingTime);
		packetByteBuf.writeFloat(spiritInstillerRecipe.experience);
		packetByteBuf.writeBoolean(spiritInstillerRecipe.noBenefitsFromYieldAndEfficiencyUpgrades);
		packetByteBuf.writeIdentifier(spiritInstillerRecipe.requiredAdvancementIdentifier);
	}
	
	@Override
	public SpiritInstillerRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
		String group = packetByteBuf.readString();
		IngredientStack centerIngredient = IngredientStack.fromByteBuf(packetByteBuf);
		IngredientStack bowlIngredient1 = IngredientStack.fromByteBuf(packetByteBuf);
		IngredientStack bowlIngredient2 = IngredientStack.fromByteBuf(packetByteBuf);
		ItemStack outputItemStack = packetByteBuf.readItemStack();
		int craftingTime = packetByteBuf.readInt();
		float experience = packetByteBuf.readFloat();
		boolean noBenefitsFromYieldAndEfficiencyUpgrades = packetByteBuf.readBoolean();
		Identifier requiredAdvancementIdentifier = packetByteBuf.readIdentifier();
		
		return this.recipeFactory.create(identifier, group, centerIngredient, bowlIngredient1, bowlIngredient2, outputItemStack, craftingTime, experience, noBenefitsFromYieldAndEfficiencyUpgrades, requiredAdvancementIdentifier);
	}
	
	public interface RecipeFactory<SpiritInstillerRecipe> {
		SpiritInstillerRecipe create(Identifier id, String group, IngredientStack centerIngredient, IngredientStack bowlIngredient1, IngredientStack bowlIngredient2, ItemStack outputItemStack,
		                             int craftingTime, float experience, boolean noBenefitsFromYieldAndEfficiencyUpgrades, Identifier requiredAdvancementIdentifier);
	}
	
}
