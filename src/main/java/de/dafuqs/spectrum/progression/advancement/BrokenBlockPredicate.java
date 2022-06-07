package de.dafuqs.spectrum.progression.advancement;

import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Since BlockPredicate requires world and pos as input we can not use that in BrokenBlockCriterion
 * When the predicate would be checked the block would already be broken, unable to be tested
 * here we require a block state, that can be checked against.
 * Since block entities are already destroyed at this stage the only things that can be checked is
 * block, state and block tag. Should suffice for 99 % of cases
 */
public class BrokenBlockPredicate {
	
	public static final BrokenBlockPredicate ANY;

	static {
		ANY = new BrokenBlockPredicate(null, null, StatePredicate.ANY);
	}

	@Nullable
	private final TagKey<Block> tag;
	@Nullable
	private final Set<Block> blocks;
	private final StatePredicate state;
	
	public BrokenBlockPredicate(@Nullable TagKey<Block> tag, @Nullable Set<Block> blocks, StatePredicate state) {
		this.tag = tag;
		this.blocks = blocks;
		this.state = state;
	}
	
	public static BrokenBlockPredicate fromJson(@Nullable JsonElement json) {
		if (json != null && !json.isJsonNull()) {
			JsonObject jsonObject = JsonHelper.asObject(json, "block");
			Set<Block> set = null;
			JsonArray jsonArray = JsonHelper.getArray(jsonObject, "blocks", null);
			if (jsonArray != null) {
				com.google.common.collect.ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
				
				for (JsonElement jsonElement : jsonArray) {
					Identifier identifier = new Identifier(JsonHelper.asString(jsonElement, "block"));
					builder.add(Registry.BLOCK.getOrEmpty(identifier).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown block id '" + identifier + "'");
					}));
				}
				
				set = builder.build();
			}
			
			TagKey<Block> tag = null;
			if (jsonObject.has("tag")) {
				Identifier identifier2 = new Identifier(JsonHelper.getString(jsonObject, "tag"));
				tag = TagKey.of(Registry.BLOCK_KEY, identifier2);
			}
			
			StatePredicate statePredicate = StatePredicate.fromJson(jsonObject.get("state"));
			return new BrokenBlockPredicate(tag, set, statePredicate);
		} else {
			return ANY;
		}
	}
	
	public boolean test(BlockState blockState) {
		if (this == ANY) {
			return true;
		} else {
			if (this.tag != null && !blockState.isIn(this.tag)) {
				return false;
			} else if (this.blocks != null && !this.blocks.contains(blockState.getBlock())) {
				return false;
			} else {
				return this.state.test(blockState);
			}
		}
	}
	
	public JsonElement toJson() {
		if (this == ANY) {
			return JsonNull.INSTANCE;
		} else {
			JsonObject jsonObject = new JsonObject();
			if (this.blocks != null) {
				JsonArray jsonArray = new JsonArray();
				
				for (Block block : this.blocks) {
					jsonArray.add(Registry.BLOCK.getId(block).toString());
				}
				
				jsonObject.add("blocks", jsonArray);
			}
			
			if (this.tag != null) {
				jsonObject.addProperty("tag", this.tag.id().toString());
			}
			
			jsonObject.add("state", this.state.toJson());
			return jsonObject;
		}
	}
	
	public static class Builder {
		@Nullable
		private Set<Block> blocks;
		@Nullable
		private TagKey<Block> tag;
		private StatePredicate state;
		
		private Builder() {
			this.state = StatePredicate.ANY;
		}
		
		public static BrokenBlockPredicate.Builder create() {
			return new BrokenBlockPredicate.Builder();
		}
		
		public BrokenBlockPredicate.Builder blocks(Block... blocks) {
			this.blocks = ImmutableSet.copyOf(blocks);
			return this;
		}
		
		public BrokenBlockPredicate.Builder blocks(Iterable<Block> blocks) {
			this.blocks = ImmutableSet.copyOf(blocks);
			return this;
		}
		
		public BrokenBlockPredicate.Builder tag(TagKey<Block> tag) {
			this.tag = tag;
			return this;
		}
		
		public BrokenBlockPredicate.Builder state(StatePredicate state) {
			this.state = state;
			return this;
		}
		
		public BrokenBlockPredicate build() {
			return new BrokenBlockPredicate(this.tag, this.blocks, this.state);
		}
	}
}
