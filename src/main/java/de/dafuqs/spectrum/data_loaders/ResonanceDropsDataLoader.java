package de.dafuqs.spectrum.data_loaders;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.resource.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.command.argument.*;
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
	
	protected static final List<ResonanceDropTarget> RESONANCE_DROPS = new ArrayList<>();
	
	public static boolean preventNextXPDrop;
	
	private ResonanceDropsDataLoader() {
		super(new Gson(), ID);
	}
	
	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		RESONANCE_DROPS.clear();
		prepared.forEach((identifier, jsonElement) -> {
			RESONANCE_DROPS.add(ResonanceDropTarget.fromJson(jsonElement.getAsJsonObject()));
		});
	}
	
	@Override
	public Identifier getFabricId() {
		return SpectrumCommon.locate(ID);
	}
	
	public static void applyResonance(BlockState minedState, BlockEntity blockEntity, List<ItemStack> droppedStacks) {
		for (ResonanceDropTarget entry : RESONANCE_DROPS) {
			if (entry.process(minedState, blockEntity, droppedStacks)) {
				return;
			}
		}
	}
	
	public static class ResonanceBlockTarget {
		
		// we have to use a lazy here, since at the stage this is needed
		// block tags are not initialized yet
		public Lazy<Either<BlockArgumentParser.BlockResult, BlockArgumentParser.TagResult>> block;
		
		public ResonanceBlockTarget(String string) {
			block = new Lazy<>(() -> {
				try {
					return BlockArgumentParser.blockOrTag(Registry.BLOCK, string, false);
				} catch (CommandSyntaxException e) {
					throw new RuntimeException(e);
				}
			});
		}
		
		public boolean test(BlockState state) {
			Either<BlockArgumentParser.BlockResult, BlockArgumentParser.TagResult> target = block.get();
			if (target.left().isPresent()) {
				return state.isOf(target.left().get().blockState().getBlock());
			}
			if (target.right().isPresent()) {
				return target.right().get().tag().contains(state.getBlock().getRegistryEntry());
			}
			return false;
		}
		
	}
	
	public static class ResonanceDropTarget {
		
		public ResonanceBlockTarget blockTarget;
		public Map<Ingredient, Item> modifiedDrops;
		public boolean dropSelf;
		public List<String> blockPropertiesToCopy;
		public List<String> nbtToCopy;
		
		public ResonanceDropTarget(ResonanceBlockTarget blockTarget, boolean dropSelf, Map<Ingredient, Item> modifiedDrops, List<String> blockPropertiesToCopy, List<String> nbtToCopy) {
			this.blockTarget = blockTarget;
			this.dropSelf = dropSelf;
			this.modifiedDrops = modifiedDrops;
			this.blockPropertiesToCopy = blockPropertiesToCopy;
			this.nbtToCopy = nbtToCopy;
		}
		
		public static ResonanceDropTarget fromJson(JsonObject jsonObject) {
			ResonanceBlockTarget blockTarget = new ResonanceBlockTarget(jsonObject.get("block").getAsString());
			
			boolean dropSelf = JsonHelper.getBoolean(jsonObject, "drop_self", false);
			
			Map<Ingredient, Item> modifiedDrops = new HashMap<>();
			if (jsonObject.has("modify_drops")) {
				JsonArray modifyDropsArray = JsonHelper.getArray(jsonObject, "modify_drops");
				for (JsonElement entry : modifyDropsArray) {
					if (!(entry instanceof JsonObject entryObject)) {
						throw new JsonSyntaxException("modify_drops is not an json object");
					}
					Ingredient ingredient = Ingredient.fromJson(entryObject.get("input"));
					Item output = Registry.ITEM.get(Identifier.tryParse(JsonHelper.getString(entryObject, "output")));
					modifiedDrops.put(ingredient, output);
				}
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
			
			return new ResonanceDropTarget(blockTarget, dropSelf, modifiedDrops, blockPropertiesToCopy, nbtToCopy);
		}
		
		private boolean process(BlockState minedState, BlockEntity blockEntity, List<ItemStack> droppedStacks) {
			if (blockTarget.test(minedState)) {
				if (dropSelf) {
					dropSelf(minedState, blockEntity, droppedStacks);
					ResonanceDropsDataLoader.preventNextXPDrop = true;
				} else {
					modifyDrops(droppedStacks);
				}
				return true;
			}
			return false;
		}
		
		public void copyBlockStateTags(BlockState minedState, ItemStack convertedStack) {
			for (Property<?> blockProperty : minedState.getProperties()) {
				if (blockPropertiesToCopy.contains(blockProperty.getName())) {
					if (minedState.getBlock().getDefaultState().get(blockProperty) == minedState.get(blockProperty)) {
						// do not copy if the value matches the default to make it stack with others
						continue;
					}
					
					NbtCompound nbt = convertedStack.getOrCreateSubNbt("BlockStateTag");
					nbt.putString(blockProperty.getName(), getPropertyName(minedState, blockProperty));
				}
			}
		}
		
		private static <T extends Comparable<T>> String getPropertyName(BlockState state, Property<T> property) {
			return property.name(state.get(property));
		}
		
		public void copyNbt(BlockEntity blockEntity, ItemStack convertedStack) {
			NbtCompound newNbt = new NbtCompound();
			
			NbtCompound beNbt = blockEntity.createNbt();
			for (String s : nbtToCopy) {
				if (beNbt.contains(s)) {
					newNbt.put(s, beNbt.get(s));
				}
			}
			
			if (!newNbt.isEmpty()) {
				convertedStack.getOrCreateSubNbt("BlockEntityTag").copyFrom(newNbt);
			}
		}
		
		private void dropSelf(BlockState minedState, BlockEntity blockEntity, List<ItemStack> droppedStacks) {
			ItemStack selfStack = minedState.getBlock().asItem().getDefaultStack();
			if (!blockPropertiesToCopy.isEmpty()) {
				copyBlockStateTags(minedState, selfStack);
			}
			if (!nbtToCopy.isEmpty()) {
				copyNbt(blockEntity, selfStack);
			}
			
			droppedStacks.clear();
			droppedStacks.add(selfStack);
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
	
}