package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import static net.minecraft.client.render.TexturedRenderLayers.*;

public class TreasureChestBlock extends SpectrumChestBlock {
	
	public TreasureChestBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof TreasureChestBlockEntity treasureChestBlockEntity) {
			if (!isChestBlocked(world, pos)) {
				if (treasureChestBlockEntity.canOpen(player)) {
					player.openHandledScreen(treasureChestBlockEntity);
				} else {
					world.playSound(null, pos, SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.PLAYERS, 1.0F, 1.0F);
				}
			}
		}
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TreasureChestBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? checkType(type, SpectrumBlockEntities.TREASURE_CHEST, TreasureChestBlockEntity::clientTick) : null;
	}
	
	@Override
	public SpriteIdentifier getTexture() {
		return new SpriteIdentifier(CHEST_ATLAS_TEXTURE, SpectrumCommon.locate("entity/treasure_chest"));
	}
	
}
