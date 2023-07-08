package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.resource.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.profiler.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class ResonanceDropsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	
	public static final String ID = "resonance_drops";
	public static final ResonanceDropsDataLoader INSTANCE = new ResonanceDropsDataLoader();
	
	protected static final HashMap<Ingredient, ResonanceDropTarget> RESONANCE_DROPS = new HashMap<>();
	
	private ResonanceDropsDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		RESONANCE_DROPS.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			
			Ingredient ingredient = Ingredient.fromJson(jsonObject.get("input"));
			if (!ingredient.isEmpty()) {
				Item itemOut = Items.AIR;
				if (jsonObject.has("output")) {
					Registry.ITEM.get(Identifier.tryParse(JsonHelper.getString(jsonObject, "output")));
				}
				List<String> blockPropertiesToCopy = new ArrayList<>();
				if (jsonObject.has("state_tags_to_copy")) {
					for (JsonElement e : JsonHelper.getArray(jsonObject, "state_tags_to_copy")) {
						blockPropertiesToCopy.add(e.getAsString());
					}
				}
				List<String> nbtToCopy = new ArrayList<>();
				if (jsonObject.has("nbt_to_copy")) {
					for (JsonElement e : JsonHelper.getArray(jsonObject, "nbt_to_copy")) {
						nbtToCopy.add(e.getAsString());
					}
				}
				
				RESONANCE_DROPS.put(ingredient, new ResonanceDropTarget(itemOut, blockPropertiesToCopy, nbtToCopy));
			}
		});
	}
	
	@Override
	public Identifier getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static void applyResonance(BlockState minedState, BlockEntity blockEntity, List<ItemStack> droppedStacks) {
		droppedStacks.replaceAll(droppedStack -> ResonanceDropsDataLoader.applyResonance(minedState, blockEntity, droppedStack));
	}
	
	public static ItemStack applyResonance(BlockState minedState, BlockEntity blockEntity, ItemStack droppedStack) {
		for (Map.Entry<Ingredient, ResonanceDropTarget> entry : RESONANCE_DROPS.entrySet()) {
			if (entry.getKey().test(droppedStack)) {
				ResonanceDropTarget dropTarget = entry.getValue();
				
				ItemStack convertedStack;
				if (dropTarget.drop == Items.AIR) {
					convertedStack = droppedStack;
				} else {
					convertedStack = dropTarget.drop.getDefaultStack();
					convertedStack.setCount(droppedStack.getCount());
				}
				
				if (!dropTarget.blockPropertiesToCopy.isEmpty()) {
					copyBlockStateTags(minedState, dropTarget, convertedStack);
				}
				if (!dropTarget.nbtToCopy.isEmpty()) {
					copyNbt(blockEntity, dropTarget, convertedStack);
				}
				
				return convertedStack;
			}
		}
		
		return droppedStack;
	}
	
	private static void copyBlockStateTags(BlockState minedState, ResonanceDropTarget dropTarget, ItemStack convertedStack) {
		for (Property<?> blockProperty : minedState.getProperties()) {
			if (dropTarget.blockPropertiesToCopy.contains(blockProperty.getName())) {
				if (minedState.getBlock().getDefaultState().get(blockProperty) == minedState.get(blockProperty)) {
					// do not copy if the value matches the default to make it stack with others
					continue;
				}
				
				NbtCompound nbt = convertedStack.getOrCreateSubNbt("BlockStateTag");
				nbt.putString(blockProperty.getName(), getPropertyName(minedState, blockProperty));
			}
		}
	}
	
	private static void copyNbt(BlockEntity blockEntity, ResonanceDropTarget dropTarget, ItemStack convertedStack) {
		NbtCompound newNbt = new NbtCompound();
		
		NbtCompound beNbt = blockEntity.createNbt();
		for (String s : dropTarget.nbtToCopy) {
			if (beNbt.contains(s)) {
				newNbt.put(s, beNbt.get(s));
			}
		}
		
		if (!newNbt.isEmpty()) {
			convertedStack.getOrCreateSubNbt("BlockEntityTag").copyFrom(newNbt);
		}
	}
	
	private static <T extends Comparable<T>> String getPropertyName(BlockState state, Property<T> property) {
		return property.name(state.get(property));
	}
	
	public static class ResonanceDropTarget {
		
		public Item drop;
		public List<String> blockPropertiesToCopy;
		public List<String> nbtToCopy;
		
		public ResonanceDropTarget(Item drop, List<String> blockPropertiesToCopy, List<String> nbtToCopy) {
			this.drop = drop;
			this.blockPropertiesToCopy = blockPropertiesToCopy;
			this.nbtToCopy = nbtToCopy;
		}
		
	}
	
}