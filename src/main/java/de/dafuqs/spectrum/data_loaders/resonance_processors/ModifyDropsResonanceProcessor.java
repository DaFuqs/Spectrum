package de.dafuqs.spectrum.data_loaders.resonance_processors;

import com.google.gson.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.predicate.block.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

import java.util.*;

public class ModifyDropsResonanceProcessor extends ResonanceDropProcessor {
	
	public static class Serializer implements ResonanceDropProcessor.Serializer {
		
		@Override
		public ResonanceDropProcessor fromJson(JsonObject json) throws Exception {
			BrokenBlockPredicate blockTarget = BrokenBlockPredicate.fromJson(json.get("block"));
			
			Map<Ingredient, Item> modifiedDrops = new HashMap<>();
			JsonArray modifyDropsArray = JsonHelper.getArray(json, "modify_drops");
			for (JsonElement entry : modifyDropsArray) {
				if (!(entry instanceof JsonObject entryObject)) {
					throw new JsonSyntaxException("modify_drops is not an json object");
				}
				Ingredient ingredient = Ingredient.fromJson(entryObject.get("input"));
				Item output = Registries.ITEM.get(Identifier.tryParse(JsonHelper.getString(entryObject, "output")));
				modifiedDrops.put(ingredient, output);
			}
			
			return new ModifyDropsResonanceProcessor(blockTarget, modifiedDrops);
		}
		
	}
	
	public Map<Ingredient, Item> modifiedDrops;
	
	public ModifyDropsResonanceProcessor(BrokenBlockPredicate blockTarget, Map<Ingredient, Item> modifiedDrops) throws Exception {
		super(blockTarget);
		this.modifiedDrops = modifiedDrops;
	}
	
	@Override
	public boolean process(BlockState state, BlockEntity blockEntity, List<ItemStack> droppedStacks) {
		if (blockPredicate.test(state)) {
			modifyDrops(droppedStacks);
			return true;
		}
		return false;
	}
	
	private void modifyDrops(List<ItemStack> droppedStacks) {
		for (ItemStack stack : droppedStacks) {
			for (Map.Entry<Ingredient, Item> modifiedDrop : modifiedDrops.entrySet()) {
				if (modifiedDrop.getKey().test(stack)) {
					ItemStack convertedStack;
					convertedStack = modifiedDrop.getValue().getDefaultStack();
					convertedStack.setCount(stack.getCount());
					
					droppedStacks.remove(stack);
					droppedStacks.add(convertedStack);
					break;
				}
			}
		}
	}
}
