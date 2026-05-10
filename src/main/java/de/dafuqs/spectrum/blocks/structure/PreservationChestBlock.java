package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.Nullable;

public class PreservationChestBlock extends SpectrumChestBlock {
	
	public static final MapCodec<PreservationChestBlock> CODEC = simpleCodec(PreservationChestBlock::new);
	
	public PreservationChestBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends PreservationChestBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationChestBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide() ? createTickerHelper(type, SpectrumBlockEntities.PRESERVATION_CHEST, PreservationChestBlockEntity::clientTick) : null;
	}
	
	@Override
	public Material getTextureLocation() {
		return new Material(InventoryMenu.BLOCK_ATLAS, SpectrumCommon.locate("block/preservation_chest"));
	}
	
}
