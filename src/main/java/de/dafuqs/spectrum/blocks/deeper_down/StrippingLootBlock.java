package de.dafuqs.spectrum.blocks.deeper_down;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.block.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import org.jspecify.annotations.Nullable;

public class StrippingLootBlock extends Block implements StrippableDrop {
    
    private final Block sourceBlock;
	private final ResourceKey<LootTable> strippingLootTableKey;
	
	public StrippingLootBlock(Properties settings, Block sourceBlock, ResourceKey<LootTable> strippingLootTableKey) {
        super(settings);
        this.sourceBlock = sourceBlock;
        this.strippingLootTableKey = strippingLootTableKey;
    }

	@Override
	public @Nullable MapCodec<? extends StrippingLootBlock> codec() {
        //TODO: Make the codec
        return null;
    }
    
    @Override
    public Block getStrippedBlock() {
        return sourceBlock;
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
