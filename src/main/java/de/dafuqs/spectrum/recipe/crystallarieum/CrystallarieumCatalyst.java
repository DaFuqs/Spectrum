package de.dafuqs.spectrum.recipe.crystallarieum;

import com.google.gson.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;

public class CrystallarieumCatalyst {
	
	public final Ingredient ingredient;
	public final float growthAccelerationMod;
	public final float inkConsumptionMod;
	public final float consumeChancePerSecond;
	
	protected CrystallarieumCatalyst(Ingredient ingredient, float growthAccelerationMod, float inkConsumptionMod, float consumeChancePerSecond) {
		this.ingredient = ingredient;
		this.growthAccelerationMod = growthAccelerationMod;
		this.inkConsumptionMod = inkConsumptionMod;
		this.consumeChancePerSecond = consumeChancePerSecond;
	}
	
	public static CrystallarieumCatalyst fromJson(JsonObject jsonObject) {
		Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
		float growthAccelerationMod = JsonHelper.getFloat(jsonObject, "growth_acceleration_mod");
		float inkConsumptionMod = JsonHelper.getFloat(jsonObject, "ink_consumption_mod");
		float consumeChancePerSecond = JsonHelper.getFloat(jsonObject, "consume_chance_per_second");
		return new CrystallarieumCatalyst(ingredient, growthAccelerationMod, inkConsumptionMod, consumeChancePerSecond);
	}
	
	public void write(PacketByteBuf packetByteBuf) {
		this.ingredient.write(packetByteBuf);
		packetByteBuf.writeFloat(growthAccelerationMod);
		packetByteBuf.writeFloat(inkConsumptionMod);
		packetByteBuf.writeFloat(consumeChancePerSecond);
	}
	
	public static CrystallarieumCatalyst fromPacket(PacketByteBuf packetByteBuf) {
		Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
		float growthAccelerationMod = packetByteBuf.readFloat();
		float inkConsumptionMod = packetByteBuf.readFloat();
		float consumeChancePerSecond = packetByteBuf.readFloat();
		return new CrystallarieumCatalyst(ingredient, growthAccelerationMod, inkConsumptionMod, consumeChancePerSecond);
	}
	
}