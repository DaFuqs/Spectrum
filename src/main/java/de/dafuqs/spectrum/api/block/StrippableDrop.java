package de.dafuqs.spectrum.api.block;

import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * A block that drops additional items from a loot table when stripped
 * Call checkAndDropStrippedLoot() in the blocks onStateReplaced()
 */
public interface StrippableDrop {
	
	Block getStrippedBlock();
	
	ResourceKey<LootTable> getStrippingLootTableKey();
	
	default boolean checkAndDropStrippedLoot(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!moved && newState.is(getStrippedBlock())) {
			// we sadly don't have the entity or hand stack here, but oh well
			List<ItemStack> harvestedStacks = getStrippedStacks(state, (ServerLevel) world, pos, world.getBlockEntity(pos), null, ItemStack.EMPTY, getStrippingLootTableKey());
			for (ItemStack harvestedStack : harvestedStacks) {
				Containers.dropItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, harvestedStack);
			}
			return true;
		}
		return false;
	}
	
	static List<ItemStack> getStrippedStacks(BlockState state, ServerLevel world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, ResourceKey<LootTable> lootTableKey) {
		var builder = (new LootParams.Builder(world))
				.withParameter(LootContextParams.BLOCK_STATE, state)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
				.withParameter(LootContextParams.TOOL, stack)
				.withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
				.withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
		
		LootTable lootTable = world.getServer().reloadableRegistries().getLootTable(lootTableKey);
		return lootTable.getRandomItems(builder.create(LootContextParamSets.BLOCK));
	}
	
}
