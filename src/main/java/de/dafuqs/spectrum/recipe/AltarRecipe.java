/*package de.dafuqs.spectrum.recipe;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.blocks.altar.AltarBlockEntity;
import de.dafuqs.spectrum.mixin.AccessorShapedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

public class AltarRecipe extends ShapedRecipe {

	private int timeTicks;
	private int tier;
	private int cyan;
	private int magenta;
	private int yellow;
	private int white;
	private int black;

	public AltarRecipe(Identifier identifier,  int width, int height, DefaultedList<Ingredient> craftingIngredients, ItemStack output) {
		super(identifier, null, width, height, craftingIngredients, output);
	}

	public AltarRecipe(Identifier identifier, int width, int height, DefaultedList<Ingredient> craftingIngredients, ItemStack output, int timeTicks, int tier, int cyan, int magenta, int yellow, int white, int black) {
		super(identifier, null, width, height, craftingIngredients, output);

		this.timeTicks = timeTicks;
		this.tier = tier;
		this.cyan = cyan;
		this.magenta = magenta;
		this.yellow = yellow;
		this.white = white;
		this.black = black;
	}

	public int getTimeTicks() {
		return timeTicks;
	}

	public int getTier() {
		return tier;
	}

	public int getCyan() {
		return cyan;
	}

	public int getMagenta() {
		return magenta;
	}

	public int getYellow() {
		return yellow;
	}

	public int getWhite() {
		return white;
	}

	public int getBlack() {
		return black;
	}

	public boolean canCraft(final AltarBlockEntity altarBlockEntity) {
		return altarBlockEntity.getTier() >= tier;
	}

	public static class Serializer implements RecipeSerializer<AltarRecipe> {

		public AltarRecipe read(Identifier identifier, JsonObject jsonObject) {
			Map<String, Ingredient> map = AccessorShapedRecipe.getComponents(JsonHelper.getObject(jsonObject, "key"));
			String[] strings = AccessorShapedRecipe.combinePattern(AccessorShapedRecipe.getPattern(JsonHelper.getArray(jsonObject, "pattern")));
			int width = strings[0].length();
			int height = strings.length;
			DefaultedList<Ingredient> craftingIngredients = AccessorShapedRecipe.getIngredients(strings, map, width, height);
			ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));

			int timeTicks = JsonHelper.getInt(jsonObject, "time");
			int tier = JsonHelper.getInt(jsonObject, "time");
			int cyan = JsonHelper.getInt(jsonObject, "cyan");
			int magenta = JsonHelper.getInt(jsonObject, "magenta");
			int yellow = JsonHelper.getInt(jsonObject, "yellow");
			int white = JsonHelper.getInt(jsonObject, "white");
			int black = JsonHelper.getInt(jsonObject, "black");

			return new AltarRecipe(identifier, width, height, craftingIngredients, output, timeTicks, tier, cyan, magenta, yellow, white, black);
		}

		public AltarRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
			int width = packetByteBuf.readVarInt();
			int height = packetByteBuf.readVarInt();
			DefaultedList<Ingredient> craftingIngredients = DefaultedList.ofSize(width * height, Ingredient.EMPTY);

			for (int k = 0; k < craftingIngredients.size(); ++k) {
				craftingIngredients.set(k, Ingredient.fromPacket(packetByteBuf));
			}

			ItemStack output = packetByteBuf.readItemStack();

			int timeTicks = packetByteBuf.readInt();
			int tier = packetByteBuf.readInt();
			int cyan = packetByteBuf.readInt();
			int magenta = packetByteBuf.readInt();
			int yellow = packetByteBuf.readInt();
			int white = packetByteBuf.readInt();
			int black = packetByteBuf.readInt();

			return new AltarRecipe(identifier, width, height, craftingIngredients, output, timeTicks, tier, cyan, magenta, yellow, white, black);
		}

		@Override
		public void write(PacketByteBuf packetByteBuf, AltarRecipe recipe) {
			packetByteBuf.writeVarInt(recipe.getWidth());
			packetByteBuf.writeVarInt(recipe.getHeight());
			packetByteBuf.writeString(recipe.getGroup());

			for (Ingredient ingredient : recipe.getPreviewInputs()) {
				ingredient.write(packetByteBuf);
			}

			packetByteBuf.writeItemStack(recipe.getOutput());

			packetByteBuf.writeInt(recipe.getTimeTicks());
			packetByteBuf.writeInt(recipe.getTier());
			packetByteBuf.writeInt(recipe.getCyan());
			packetByteBuf.writeInt(recipe.getMagenta());
			packetByteBuf.writeInt(recipe.getYellow());
			packetByteBuf.writeInt(recipe.getWhite());
			packetByteBuf.writeInt(recipe.getBlack());
		}
	}

}*/