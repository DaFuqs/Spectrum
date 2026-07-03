package de.dafuqs.spectrum.blocks.deeper_down;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.flammable.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import org.jspecify.annotations.Nullable;

import java.util.function.*;

public class StrippingLootPillarBlock extends FlammableLogBlock implements StrippableDrop {
	
	private final ResourceKey<LootTable> strippingLootTableKey;
	
	public StrippingLootPillarBlock(Properties settings, Supplier<? extends RotatedPillarBlock> strippedBlock, ResourceKey<LootTable> strippingLootTableKey) {
		super(settings, strippedBlock);
		this.strippingLootTableKey = strippingLootTableKey;
	}

	@Override
	public @Nullable MapCodec<? extends StrippingLootPillarBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public Block getStrippedBlock() {
		return strippedBlock.get();
	}
	
	@Override
	public ResourceKey<LootTable> getStrippingLootTableKey() {
		return strippingLootTableKey;
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		checkAndDropStrippedLoot(state, world, pos, newState, moved);
		super.onRemove(state, world, pos, newState, moved);
	}
	
}
