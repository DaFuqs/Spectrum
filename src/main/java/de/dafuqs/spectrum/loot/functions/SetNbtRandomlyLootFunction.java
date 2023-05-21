package de.dafuqs.spectrum.loot.functions;

import com.google.common.collect.*;
import com.google.gson.*;
import com.mojang.brigadier.exceptions.*;
import de.dafuqs.spectrum.loot.*;
import net.minecraft.item.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.*;
import net.minecraft.loot.function.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;

import java.util.*;

public class SetNbtRandomlyLootFunction extends ConditionalLootFunction {
	
	final List<NbtCompound> nbts;
	
	SetNbtRandomlyLootFunction(LootCondition[] conditions, Collection<NbtCompound> nbts) {
		super(conditions);
		this.nbts = ImmutableList.copyOf(nbts);
	}
	
	@Override
	public LootFunctionType getType() {
		return SpectrumLootFunctionTypes.SET_NBT_RANDOMLY;
	}
	
	@Override
	public ItemStack process(ItemStack stack, LootContext context) {
		if (this.nbts.isEmpty()) {
			return stack;
		}
		
		NbtCompound compound = this.nbts.get(context.getRandom().nextInt(this.nbts.size()));
		stack.getOrCreateNbt().copyFrom(compound);
		return stack;
	}
	
	public static Builder create() {
		return new Builder();
	}
	
	public static ConditionalLootFunction.Builder<?> builder() {
		return builder((conditions) -> new SetNbtRandomlyLootFunction(conditions, ImmutableList.of()));
	}
	
	public static class Builder extends ConditionalLootFunction.Builder<SetNbtRandomlyLootFunction.Builder> {
		private final Set<NbtCompound> nbts = Sets.newHashSet();
		
		@Override
		protected SetNbtRandomlyLootFunction.Builder getThisBuilder() {
			return this;
		}
		
		public SetNbtRandomlyLootFunction.Builder add(NbtCompound nbt) {
			this.nbts.add(nbt);
			return this;
		}
		
		@Override
		public LootFunction build() {
			return new SetNbtRandomlyLootFunction(this.getConditions(), this.nbts);
		}
	}
	
	public static class Serializer extends ConditionalLootFunction.Serializer<SetNbtRandomlyLootFunction> {
		
		@Override
		public void toJson(JsonObject jsonObject, SetNbtRandomlyLootFunction lootFunction, JsonSerializationContext jsonSerializationContext) {
			super.toJson(jsonObject, lootFunction, jsonSerializationContext);
			if (!lootFunction.nbts.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				for (NbtCompound nbt : lootFunction.nbts) {
					jsonArray.add(new JsonPrimitive(nbt.toString()));
				}
				jsonObject.add("tags", jsonArray);
			}
		}
		
		@Override
		public SetNbtRandomlyLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
			List<NbtCompound> nbts = Lists.newArrayList();
			if (jsonObject.has("tags")) {
				JsonArray jsonArray = JsonHelper.getArray(jsonObject, "tags");
				for (JsonElement jsonElement : jsonArray) {
					try {
						NbtCompound nbtCompound = StringNbtReader.parse(jsonElement.getAsString());
						nbts.add(nbtCompound);
					} catch (CommandSyntaxException e) {
						throw new RuntimeException(e);
					}
				}
			}
			
			return new SetNbtRandomlyLootFunction(lootConditions, nbts);
		}
	}
	
}
