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
import javax.annotation.*;

public class TreasureChestBlock extends SpectrumChestBlock {
	
	public static final MapCodec<TreasureChestBlock> CODEC = simpleCodec(TreasureChestBlock::new);
	
	public TreasureChestBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends TreasureChestBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof TreasureChestBlockEntity treasureChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				if (treasureChestBlockEntity.canOpen(player)) {
					player.openMenu(treasureChestBlockEntity);
				} else {
					world.playSound(null, pos, SoundEvents.CHEST_LOCKED, SoundSource.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TreasureChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide ? createTickerHelper(type, SpectrumBlockEntities.PRESERVATION_CHEST.get(), TreasureChestBlockEntity::clientTick) : null;
	}
	
	@Override
	public Material getTextureLocation() {
		return new Material(InventoryMenu.BLOCK_ATLAS, SpectrumCommon.locate("block/preservation_chest"));
	}
	
}
