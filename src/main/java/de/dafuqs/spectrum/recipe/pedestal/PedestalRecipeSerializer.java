package de.dafuqs.spectrum.recipe.pedestal;

import com.google.gson.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class PedestalRecipeSerializer<T extends PedestalRecipe> implements GatedRecipeSerializer<T> {
	
	public PedestalRecipeSerializer() {
	}
	
	protected @NotNull
	static Map<GemstoneColor, Integer> readGemstonePowderInputs(JsonObject jsonObject) {
		HashMap<GemstoneColor, Integer> gemInputs = new HashMap<>();
		int amount = JsonHelper.getInt(jsonObject, "cyan", 0);
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.CYAN, amount);
		}
		amount = JsonHelper.getInt(jsonObject, "magenta", 0);
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.MAGENTA, amount);
		}
		amount = JsonHelper.getInt(jsonObject, "yellow", 0);
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.YELLOW, amount);
		}
		amount = JsonHelper.getInt(jsonObject, "black", 0);
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.BLACK, amount);
		}
		amount = JsonHelper.getInt(jsonObject, "white", 0);
		if (amount > 0) {
			gemInputs.put(BuiltinGemstoneColor.WHITE, amount);
		}
		return gemInputs;
	}
	
	protected void writeGemstonePowderInputs(@NotNull PacketByteBuf packetByteBuf, @NotNull PedestalRecipe recipe) {
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.CYAN));
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.MAGENTA));
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.YELLOW));
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.BLACK));
		packetByteBuf.writeInt(recipe.getGemstonePowderAmount(BuiltinGemstoneColor.WHITE));
	}
	
	protected @NotNull Map<GemstoneColor, Integer> readGemstonePowderInputs(@NotNull PacketByteBuf packetByteBuf) {
		int cyan = packetByteBuf.readInt();
		int magenta = packetByteBuf.readInt();
		int yellow = packetByteBuf.readInt();
		int black = packetByteBuf.readInt();
		int white = packetByteBuf.readInt();
		Map<GemstoneColor, Integer> gemInputs = new HashMap<>();
		if (cyan > 0) {
			gemInputs.put(BuiltinGemstoneColor.CYAN, cyan);
		}
		if (magenta > 0) {
			gemInputs.put(BuiltinGemstoneColor.MAGENTA, magenta);
		}
		if (yellow > 0) {
			gemInputs.put(BuiltinGemstoneColor.YELLOW, yellow);
		}
		if (black > 0) {
			gemInputs.put(BuiltinGemstoneColor.BLACK, black);
		}
		if (white > 0) {
			gemInputs.put(BuiltinGemstoneColor.WHITE, white);
		}
		return gemInputs;
	}
	
}
