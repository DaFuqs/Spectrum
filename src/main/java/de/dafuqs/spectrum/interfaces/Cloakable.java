package de.dafuqs.spectrum.interfaces;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.progression.BlockCloakManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public interface Cloakable {
	
	VoxelShape EMPTY_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	
	Identifier getCloakAdvancementIdentifier();
	
	default void registerCloak() {
		BlockCloakManager.registerAdvancementCloak(this, getCloakAdvancementIdentifier());
	}
	
	Hashtable<BlockState, BlockState> getBlockStateCloaks();
	
	@Nullable Pair<Item, Item> getItemCloak();
	
	default void onCloak() {
	}
	
	;
	
	default void onUncloak() {
	}
	
	;
	
	default boolean isVisibleTo(ShapeContext context) {
		if (context instanceof EntityShapeContext) {
			Entity entity = ((EntityShapeContext) context).getEntity();
			if (entity instanceof PlayerEntity) {
				return this.isVisibleTo((PlayerEntity) entity);
			}
		}
		return true;
	}
	
	default boolean isVisibleTo(PlayerEntity playerEntity) {
		return Support.hasAdvancement(playerEntity, getCloakAdvancementIdentifier());
	}
	
	default PlayerEntity getLootPlayerEntity(LootContext.Builder lootContextBuilder) {
		if (lootContextBuilder.getNullable(LootContextParameters.THIS_ENTITY) == null) {
			return null;
		} else {
			Entity entity = lootContextBuilder.get(LootContextParameters.THIS_ENTITY);
			if (entity instanceof PlayerEntity) {
				return (PlayerEntity) entity;
			} else {
				return null;
			}
		}
	}
	
	default List<ItemStack> getCloakedDroppedStacks(BlockState state, LootContext.Builder builder) {
		PlayerEntity lootPlayerEntity = getLootPlayerEntity(builder);
		
		Identifier identifier;
		BlockState cloakedBlockState = null;
		if (lootPlayerEntity == null || !isVisibleTo(lootPlayerEntity)) {
			cloakedBlockState = BlockCloakManager.getBlockStateCloak(state);
			if (cloakedBlockState == null) {
				identifier = state.getBlock().getLootTableId();
			} else {
				identifier = cloakedBlockState.getBlock().getLootTableId();
			}
		} else {
			identifier = state.getBlock().getLootTableId();
		}
		
		if (identifier == LootTables.EMPTY) {
			return Collections.emptyList();
		} else {
			LootContext lootContext;
			if (cloakedBlockState == null) {
				lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
			} else {
				lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, cloakedBlockState).build(LootContextTypes.BLOCK);
			}
			ServerWorld serverWorld = lootContext.getWorld();
			LootTable lootTable = serverWorld.getServer().getLootManager().getTable(identifier);
			return lootTable.generateLoot(lootContext);
		}
	}
	
}
